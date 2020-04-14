package pl.izidev.crawler;

import org.junit.Test;

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
}
