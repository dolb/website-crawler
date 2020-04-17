package pl.izidev.crawler.output;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import pl.izidev.crawler.WebsiteCrawlerResult;
import pl.izidev.utils.HTMLHelper;

public class JSONCrawlerOutput extends CrawlerOutput {

	private final static String OUTPUT_FILE_NAME = "crawler-output.json";

	private class WebsiteCrawlerSummary {
		private List<WebsiteCrawlerResult> list;

		private WebsiteCrawlerSummary(List<WebsiteCrawlerResult> list) {
			this.list = list;
		}

		public List<WebsiteCrawlerResult> getList() {
			return list;
		}
	}

	@Override
	protected String convert(List<WebsiteCrawlerResult> crawledWebsites) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper
				.writerWithDefaultPrettyPrinter()
				.writeValueAsString(new WebsiteCrawlerSummary(crawledWebsites));
		} catch (JsonProcessingException e) {
			System.err.println("Cannot parse objects to JSON");
			return "";
		}
	}

	@Override
	public CrawlerOutput saveFile() {
		saveFile(OUTPUT_FILE_NAME);
		return this;
	}

}
