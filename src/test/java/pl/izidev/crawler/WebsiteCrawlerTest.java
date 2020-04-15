package pl.izidev.crawler;

import org.junit.Test;

public class WebsiteCrawlerTest {

	@Test(expected = UnsupportedOperationException.class)
	public void invalidUrl_noProtocol() {
		new WebsiteCrawler("example.com");
	}

	@Test(expected = UnsupportedOperationException.class)
	public void invalidUrl_contextRelatedProtocol() {
		new WebsiteCrawler("//example.com");
	}

	@Test
	public void validUrl_http() {
		new WebsiteCrawler("http://example.com");
	}

	@Test
	public void validUrl_https() {
		new WebsiteCrawler("https://example.com");
	}
}
