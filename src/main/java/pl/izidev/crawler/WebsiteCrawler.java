package pl.izidev.crawler;

import java.util.*;
import java.util.stream.Collectors;
import javax.swing.text.html.HTML;
import pl.izidev.threading.ThreadListener;
import pl.izidev.utils.HTMLHelper;
import pl.izidev.utils.HttpUtils;

/**
 * Main instance to orchestrate whole crawling process outside of main method.
 */
public class WebsiteCrawler implements Runnable {

	private TaskManager taskManager;
	private ContentProvider provider;
	private List<WebsiteCrawlerResult> crawledWebsites;
	private Stack<String> callStack;
	private final ThreadListener listener;

	public WebsiteCrawler(final String startingUrl, ThreadListener listener) throws UnsupportedOperationException {
		this.taskManager = new TaskManager(
			HttpUtils.getHost(startingUrl).orElseThrow(
				() -> new UnsupportedOperationException(String.format("'%s' is not a valid starting URL", startingUrl))
			)
		).addUrl(startingUrl);
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
		String routesHTML = this.crawledWebsites
			.stream()
			.map(el -> HTMLHelper
				.toListElement(
					HTMLHelper.toLocalElementReference(el.getUrl())))
			.collect(Collectors.joining(""));

		String contentHTML = this.crawledWebsites
			.stream()
			.map(WebsiteCrawlerResult::getUrl)
			.collect(Collectors.joining(""));

		Map<String, String> params = new HashMap<>();
		params.put("routes", routesHTML);
		params.put("content", contentHTML);

		String resultPage = HTMLHelper.populateTemplateResource(
			"template.html",
			params
		);

		System.out.println(resultPage);
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

	protected void processWebsiteSummary(WebsiteCrawlerResult summary) {
		this.crawledWebsites.add(summary);
		this.callStack.pop();
		summary
			.getLinks()
			//FIXME prevent recursive urls (visited HashSet cache would do)
			.forEach(taskManager::addUrl);
		processNextTask();
		checkIfThreadFinished();
	}

	protected void processError(Throwable error) {
		System.err.println(error.toString());
		this.callStack.pop();
		checkIfThreadFinished();
	}

}
