package pl.izidev.crawler.output;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import pl.izidev.crawler.WebsiteCrawlerResult;
import pl.izidev.utils.HTMLHelper;

public class HTMLFileOutput extends CrawlerOutput {

	private final static String OUTPUT_FILE_NAME = "crawler-output.html";

	@Override
	public String convert(List<WebsiteCrawlerResult> crawledWebsites) {
		String routesHTML = crawledWebsites
			.stream()
			.map(el -> HTMLHelper
				.toListElement(
					HTMLHelper.toLocalElementReference(el.getUrl())))
			.collect(Collectors.joining(""));

		String contentHTML = crawledWebsites
			.stream()
			.map(this::convertSingle)
			.collect(Collectors.joining(""));

		Map<String, String> params = new HashMap<>();
		params.put("routes", routesHTML);
		params.put("content", contentHTML);

		return HTMLHelper.populateTemplateResource(
			"template.html",
			params
		);
	}

	@Override
	public CrawlerOutput saveFile() {
		saveFile(OUTPUT_FILE_NAME);
		return this;
	}

	private String convertSingle(WebsiteCrawlerResult summary) {
		return HTMLHelper.toDivWithId(
			summary.getUrl(),
			HTMLHelper.toTitle(summary.getUrl()),
			HTMLHelper.createLinkSection("Links", summary.getLinks()),
			HTMLHelper.createLinkSection("Images", summary.getImages()),
			HTMLHelper.createLinkSection("Scripts", summary.getScripts()),
			HTMLHelper.createLinkSection("Resources", summary.getResources())
		);
	}

}
