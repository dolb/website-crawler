package pl.izidev.crawler;

import org.junit.Test;
import pl.izidev.crawler.output.HTMLFileOutput;
import pl.izidev.threading.ThreadListener;

public class WebsiteCrawlerTest {

	@Test(expected = UnsupportedOperationException.class)
	public void invalidUrl_noProtocol() {
		new WebsiteCrawler("example.com", new ThreadListener(), new HTMLFileOutput());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void invalidUrl_contextRelatedProtocol() {
		new WebsiteCrawler("//example.com", new ThreadListener(), new HTMLFileOutput());
	}

	@Test
	public void validUrl_http() {
		new WebsiteCrawler("http://example.com", new ThreadListener(), new HTMLFileOutput());
	}

	@Test
	public void validUrl_https() {
		new WebsiteCrawler("https://example.com", new ThreadListener(), new HTMLFileOutput());
	}
}
