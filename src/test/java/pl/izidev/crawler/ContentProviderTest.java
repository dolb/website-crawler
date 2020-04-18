package pl.izidev.crawler;

import java.util.concurrent.CompletableFuture;
import org.junit.Assert;
import org.junit.Test;
import pl.izidev.client.ClientWrapper;
import pl.izidev.threading.ThreadListener;

public class ContentProviderTest {

	@Test(expected = UnsupportedOperationException.class)
	public void canCrawlOnlyIfSubscriberPresent() {
		new ContentProvider().crawl("https://example.com");
	}

	@Test(expected = UnsupportedOperationException.class)
	public void onlySingleSubscriberAllowed() {
		new ContentProvider().subscribe(
			next -> {},
			error -> {}
		).subscribe(
			next -> {},
			error -> {}
		);
	}

	private class ClientWrapperTimeoutMock extends ClientWrapper {

		ClientWrapperTimeoutMock() {}

		@Override
		public CompletableFuture<String> getContent(String url, long timeoutSeconds) {
			return new CompletableFuture<>();
		}
	}

	@Test
	public void crawl_timeout() throws InterruptedException {
		// This should be done with test extension modules like Mockito
		ClientWrapper client = new ClientWrapperTimeoutMock();
		ThreadListener listener = new ThreadListener();
		new ContentProvider(client, 1L)
			.subscribe(
				next -> {
					synchronized (listener) {
						Assert.assertEquals("Call timed out after 1 seconds.", next.getException());
						listener.notify();
					}
				},
				error -> {}
			).crawl("http://example.com");

		synchronized (listener) {
			listener.wait();
		}
	}


}
