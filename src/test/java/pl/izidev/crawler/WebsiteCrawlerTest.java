package pl.izidev.crawler;

import org.junit.Test;
import pl.izidev.crawler.output.HTMLCrawlerOutput;
import pl.izidev.threading.ThreadListener;

public class WebsiteCrawlerTest {

	@Test(expected = UnsupportedOperationException.class)
	public void invalidUrl_noProtocol() {
		new WebsiteCrawler("example.com", new ThreadListener(), new HTMLCrawlerOutput());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void invalidUrl_contextRelatedProtocol() {
		new WebsiteCrawler("//example.com", new ThreadListener(), new HTMLCrawlerOutput());
	}

	@Test
	public void validUrl_http() {
		new WebsiteCrawler("http://example.com", new ThreadListener(), new HTMLCrawlerOutput());
	}

	@Test
	public void validUrl_https() {
		new WebsiteCrawler("https://example.com", new ThreadListener(), new HTMLCrawlerOutput());
	}
}
