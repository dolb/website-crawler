package pl.izidev.crawler.output;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import pl.izidev.crawler.WebsiteCrawlerResult;
import pl.izidev.utils.ResourceHelper;

/**
 * Output conversion base class for simple separation of different output types (HTML, JSON, XML, etc.)
 */
public abstract class CrawlerOutput {

	private final static String OUTPUT_FILE_NAME = "output";
	private String convertedBody;

	CrawlerOutput() {

	}


	public CrawlerOutput convertResults(List<WebsiteCrawlerResult> crawlerResults) {
		//FIXME Workaround due to possible concurrent modification https://github.com/dolb/website-crawler/issues/3
		this.convertedBody = convert(new ArrayList<>(crawlerResults));
		return this;
	}

	public CrawlerOutput print() {
		Optional
			.ofNullable(this.convertedBody)
			.ifPresent(System.out::println);
		return this;
	}

	protected abstract String convert(List<WebsiteCrawlerResult> crawledWebsites);

	public CrawlerOutput saveFile() {
		this.saveFile(OUTPUT_FILE_NAME);
		return this;
	}

	void saveFile(String fileName) {
		Optional
			.ofNullable(this.convertedBody)
			.ifPresent(
				body -> ResourceHelper
					.toFile(fileName, body)
					.ifPresentOrElse(
						__ -> System.out.println(String.format("%s saved successfully", fileName)),
						() -> {
							System.out.println("Printing output to the console:\n");
							System.out.println(body);
						}
					)
			);
	}
}
