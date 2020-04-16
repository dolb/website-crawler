package pl.izidev.parser;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import pl.izidev.crawler.WebsiteCrawlerResult;

public class WebsiteParser {

	/**
	 * Helper will use Jsoup to parse body
	 * @param body HTML document loaded to String
	 * @return website crawling summary
	 */
	public static WebsiteCrawlerResult parse(String body) {
		WebsiteCrawlerResult result = new WebsiteCrawlerResult();
		Document doc = Jsoup.parse(body);
		result.addImages(findElements(doc, "img[src]", "src"));
		result.addLinks(findElements(doc, "a[href]", "href"));
		result.addScripts(findElements(doc, "script[src]", "src"));
		result.addResources(findElements(doc, "link[rel$=stylesheet]", "href"));
		return result;
	}

	static Set<String> findElements(Document doc, String selector, String attributeName) {
		return doc.select(selector)
			.stream()
			.map(
				imageElement -> imageElement.attr(attributeName)
			)
			.filter(
				el -> Optional.ofNullable(el).isPresent()
			).collect(Collectors.toSet());
	}

}
