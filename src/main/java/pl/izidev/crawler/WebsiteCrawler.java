package pl.izidev.crawler;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import pl.izidev.threading.ThreadListener;
import pl.izidev.utils.HttpUtils;
import pl.izidev.crawler.output.CrawlerOutput;

/**
 * Main instance to orchestrate whole crawling process outside of main method.
 */
public class WebsiteCrawler implements Runnable {


	private TaskManager taskManager;
	private ContentProvider provider;
	private List<WebsiteCrawlerResult> crawledWebsites;
	private Stack<String> callStack;
	private final ThreadListener listener;
	private final CrawlerOutput outputConverter;

	public WebsiteCrawler(
		final String startingUrl,
		ThreadListener listener,
		CrawlerOutput outputConverter
	) throws UnsupportedOperationException {
		this.taskManager = new TaskManager(
			HttpUtils.getHost(startingUrl).orElseThrow(
				() -> new UnsupportedOperationException(String.format("'%s' is not a valid starting URL", startingUrl))
			)
		).addUrl(startingUrl);
		this.outputConverter = outputConverter;
		this.listener = listener;
		this.callStack = new Stack<>();
		this.crawledWebsites = new ArrayList<>();
	}

	/**
	 * Processes next URL saved in TaskManager.
	 * If found it notifies ContentProvider it should start crawling process again.
	 */
	private void processNextTask() {
		this.taskManager
			.getNextUrl()
			.ifPresent(
				url -> {
					this.callStack.add(url);
					this.provider.crawl(url);
				}
			);
	}

	@Override
	public void run() {
		System.out.println("Website crawler main thread started");
		this.provider = new ContentProvider()
			.subscribe(
				this::processSinglePage,
				this::processError
			);
		processNextTask();
	}

	/**
	 * Generates output using injected CrawlerOutput.
	 */
	private void generateOutput() {
		this.outputConverter
			.convertResults(this.crawledWebsites)
			.print()
			.saveFile();
	}

	private void checkIfThreadFinished() {
		if(this.callStack.isEmpty()) {
			System.out.println("Crawling finished - printing result");
			generateOutput();
			synchronized (this.listener) {
				this.listener.notify();
			}
		}
	}

	private void taskProcessedCallback() {
		this.callStack.pop();
		processNextTask();
		checkIfThreadFinished();
	}

	private void processSinglePage(WebsiteCrawlerResult summary) {
		this.crawledWebsites.add(summary);
		summary
			.getLinks()
			.forEach(taskManager::addUrl);
		taskProcessedCallback();
	}

	private void processError(Throwable error) {
		System.err.println(error.toString());
		taskProcessedCallback();
	}

}
