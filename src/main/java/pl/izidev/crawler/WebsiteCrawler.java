package pl.izidev.crawler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
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
	private int tasksStarted;
	private int tasksFinished;
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
		this.tasksStarted = 0;
		this.tasksFinished = 0;
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
					this.tasksStarted += 1;
					this.provider.crawl(url);
					processNextTask();
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
		this.tasksFinished += 1;
		if(this.tasksStarted == this.tasksFinished) {
			System.out.println("Crawling finished - printing result");
			generateOutput();
			synchronized (this.listener) {
				this.listener.notify();
			}
		}
	}

	private void taskProcessedCallback() {
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

	/**
	 * No error is emitted to the observable, so this method wont ever be called unless
	 * someone extends RX channel functionality
	 */
	private void processError(Throwable error) {
		error.printStackTrace();
		this.tasksFinished = this.tasksStarted;
		taskProcessedCallback();
	}

}
