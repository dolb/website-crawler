package pl.izidev.crawler;

import io.reactivex.functions.Consumer;
import io.reactivex.internal.functions.Functions;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import pl.izidev.client.ClientWrapper;
import pl.izidev.parser.WebsiteParser;

/**
 * The class allows to fetch data for given URLs, and supports single subscriber channel that emits parsed resources.
 */
public class ContentProvider {

	private boolean subscribed = false;
	protected Subject<WebsiteCrawlerResult> channel;
	private ClientWrapper client;

	public ContentProvider() {
		this.channel = PublishSubject.create();
		this.client = new ClientWrapper();
	}

	private void emitPage(WebsiteCrawlerResult page) {
		this.channel.onNext(page);
	}

	public final ContentProvider subscribe(Consumer<WebsiteCrawlerResult> onNext, Consumer<? super Throwable> onError) throws UnsupportedOperationException {
		if(this.subscribed) {
			throw new UnsupportedOperationException("Someone is already subscribed");
		}

		this.channel.subscribe(onNext, onError, Functions.EMPTY_ACTION, Functions.emptyConsumer());
		this.subscribed = true;
		return this;
	}

	/**
	 * Crawls URL, and emits result to content channel for TaskManager to process
	 * @param url
	 * @throws UnsupportedOperationException
	 */
	public void crawl(String url) throws UnsupportedOperationException {
		if (!this.subscribed) {
			throw new UnsupportedOperationException("Cannot start crawler - connect listeners first");
		}

		//FIXME process onError and extend WebsiteCrawlerResult functionality (404, 502 and other HTTP codes)
		client.getContent(url)
			.thenApply(WebsiteParser::parse)
			.thenApply(result -> result.setUrl(url))
			.thenAccept(this::emitPage);
	}

}
