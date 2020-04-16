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
				this::processWebsiteSummary,
				this::processError
			);
		processNextTask();
	}

	private void printResult() {
		this.outputConverter
			.convertResults(this.crawledWebsites)
			.print()
			.saveFile();
	}

	private void checkIfThreadFinished() {
		if(this.callStack.isEmpty()) {
			System.out.println("Crawling finished - printing result");
			this.printResult();
			synchronized (this.listener) {
				this.listener.notify();
			}
		}
	}

	private void processWebsiteSummary(WebsiteCrawlerResult summary) {
		this.crawledWebsites.add(summary);
		this.callStack.pop();
		summary
			.getLinks()
			.forEach(taskManager::addUrl);
		processNextTask();
		checkIfThreadFinished();
	}

	private void processError(Throwable error) {
		System.err.println(error.toString());
		this.callStack.pop();
		checkIfThreadFinished();
	}

}
