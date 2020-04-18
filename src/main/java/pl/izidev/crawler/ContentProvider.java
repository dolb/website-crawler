package pl.izidev.crawler;

import io.reactivex.functions.Consumer;
import io.reactivex.internal.functions.Functions;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import java.util.concurrent.TimeUnit;
import pl.izidev.client.ClientWrapper;
import pl.izidev.parser.WebsiteParser;

/**
 * The class allows to fetch data for given URLs, and supports single subscriber channel that emits parsed resources.
 */
class ContentProvider {

	private boolean subscribed = false;
	private Subject<WebsiteCrawlerResult> channel;
	private ClientWrapper client;
	private long SINGLE_CALL_TIMEOUT = 10;

	ContentProvider() {
		this.channel = PublishSubject.create();
		this.client = new ClientWrapper();
	}

	ContentProvider(ClientWrapper client, long timeout) {
		this.channel = PublishSubject.create();
		this.client = client;
		this.SINGLE_CALL_TIMEOUT = timeout;
	}

	private void emitPage(WebsiteCrawlerResult page) {
		this.channel.onNext(page);
	}

	final ContentProvider subscribe(Consumer<WebsiteCrawlerResult> onNext, Consumer<? super Throwable> onError) throws UnsupportedOperationException {
		if(this.subscribed) {
			throw new UnsupportedOperationException("Someone is already subscribed");
		}

		this.channel.subscribe(onNext, onError, Functions.EMPTY_ACTION, Functions.emptyConsumer());
		this.subscribed = true;
		return this;
	}

	/**
	 * Crawls URL, and emits result to content channel for an orchestrator to process
	 * @param url - url that will be pulled
	 * @throws UnsupportedOperationException if there is no subscriber on the channel
	 */
	void crawl(String url) throws UnsupportedOperationException {
		if (!this.subscribed) {
			throw new UnsupportedOperationException("Cannot start crawler - connect listeners first");
		}

		client
			.getContent(url, SINGLE_CALL_TIMEOUT)
			.thenApply(WebsiteParser::parse)
			.thenApply(result -> result.setUrl(url))
			.completeOnTimeout(
				new WebsiteCrawlerResult()
					.setUrl(url)
					.setException(String.format("Call timed out after %d seconds.", SINGLE_CALL_TIMEOUT)),
				SINGLE_CALL_TIMEOUT,
				TimeUnit.SECONDS
			)
			.exceptionally(
				exception -> new WebsiteCrawlerResult()
					.setUrl(url)
					.setException(exception.getClass().getSimpleName())
				)
			.thenAccept(this::emitPage);
	}
}
