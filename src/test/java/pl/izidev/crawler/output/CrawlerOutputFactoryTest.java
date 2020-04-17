package pl.izidev.crawler.output;

import java.util.Optional;
import org.junit.Assert;
import org.junit.Test;

public class CrawlerOutputFactoryTest {

	@Test
	public void getCrawlerOutput_default() {
		Assert.assertTrue(CrawlerOutputFactory.getCrawlerOutput(Optional.empty()) instanceof JSONCrawlerOutput);
	}

	@Test
	public void getCrawlerOutput_unknownReturnsDefault() {
		Assert.assertTrue(CrawlerOutputFactory.getCrawlerOutput(Optional.of("UNKNOWN")) instanceof JSONCrawlerOutput);
	}

	@Test
	public void getCrawlerOutput_json() {
		Assert.assertTrue(CrawlerOutputFactory.getCrawlerOutput(Optional.of("JsOn")) instanceof JSONCrawlerOutput);
	}

	@Test
	public void getCrawlerOutput_html() {
		Assert.assertTrue(CrawlerOutputFactory.getCrawlerOutput(Optional.of("HtMl")) instanceof HTMLCrawlerOutput);
	}

}
