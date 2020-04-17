package pl.izidev.crawler.output;

import java.util.Optional;

public class CrawlerOutputFactory {

	private final static String JSON = "json";
	private final static String HTML = "html";

	public static CrawlerOutput getCrawlerOutput(Optional<String> param) {
		switch (param.orElse(JSON).toLowerCase()) {
			case HTML: return new HTMLCrawlerOutput();
			case JSON:
			default: return new JSONCrawlerOutput();
		}
	}

}
