package pl.izidev.crawler;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import pl.izidev.utils.HttpUtils;

/**
 * Main instance to orchestrate whole crawling process outside of main method.
 */
public class WebsiteCrawler implements Runnable {

	private TaskManager taskManager;
	private ContentProvider provider;
	private List<WebsiteCrawlerResult> crawledWebsites;
	private Stack<String> callStack;

	public WebsiteCrawler(final String startingUrl) throws UnsupportedOperationException {
		this.taskManager = new TaskManager(
			HttpUtils.getHost(startingUrl).orElseThrow(
				() -> new UnsupportedOperationException(String.format("'%s' is not a valid starting URL", startingUrl))
			)
		).addUrl(startingUrl);
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
		for(WebsiteCrawlerResult summary : this.crawledWebsites) {
			System.out.println(summary);
		}
	}

	private void checkIfThreadFinished() {
		if(this.callStack.isEmpty()) {
			System.out.println("RUNNABLE DONE RUNNING :: RESULT:");
			this.printResult();
			// TODO notify listener in Main class that thread is done running to clean up
		}
	}

	protected void processWebsiteSummary(WebsiteCrawlerResult summary) {
		this.crawledWebsites.add(summary);
		this.callStack.pop();
		summary
			.getLinks()
			.stream()
			.forEach(taskManager::addUrl);
		processNextTask();
		checkIfThreadFinished();
	}

	protected void processError(Throwable error) {
		System.err.println(error);
		this.callStack.pop();
		checkIfThreadFinished();
	}

}
