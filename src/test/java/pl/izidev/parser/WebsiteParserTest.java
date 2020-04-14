package pl.izidev.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.util.Set;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import pl.izidev.test.TestHelper;
import pl.izidev.crawler.WebsiteCrawlerResult;

public class WebsiteParserTest {

	private final Set<String> EXPECTED_IMAGES = Set.of("http://wiprodigital.com/localImage.png", "http://yahoo.com/externalImage.png");
	private final Set<String> EXPECTED_LINKS = Set.of("https://wiprodigital.com/local-page/", "https://wiprodigital.com/local-page#reference", "https://google.com/external-page/");
	private final Set<String> EXPECTED_SCRIPTS = Set.of("https://example.com/example.js", "http://wiprodigital.com/found.js");
	private final Set<String> EXPECTED_CSS = Set.of("assets/css/responsive.css");

	private void compareSets(Set<String> expected, Set<String> actual) {
		assertEquals(
			actual
				.stream()
				.filter(
					expected::contains
				).count(),
			expected.size()
		);
	}

	@Test
	public void testFindElementImages(){
		Document doc = Jsoup.parse(TestHelper.getResourcesAsString("/website/test.html"));
		compareSets(EXPECTED_IMAGES, WebsiteParser.findElements(doc, "img[src]", "src"));
	}

	@Test
	public void testFindLinks(){
		Document doc = Jsoup.parse(TestHelper.getResourcesAsString("/website/test.html"));
		compareSets(EXPECTED_LINKS, WebsiteParser.findElements(doc, "a[href]", "href"));
	}

	@Test
	public void testFindResources(){
		Document doc = Jsoup.parse(TestHelper.getResourcesAsString("/website/test.html"));
		compareSets(EXPECTED_CSS, WebsiteParser.findElements(doc, "link[rel$=stylesheet]", "href"));
	}

	@Test
	public void testFindElementScripts(){
		Document doc = Jsoup.parse(TestHelper.getResourcesAsString("/website/test.html"));
		compareSets(EXPECTED_SCRIPTS, WebsiteParser.findElements(doc, "script[type$=text/javascript]", "src"));
	}


	@Test
	public void parse(){
		String body = TestHelper.getResourcesAsString("/website/test.html");
		WebsiteCrawlerResult summary = WebsiteParser.parse(body);

		compareSets(EXPECTED_IMAGES, summary.getImages());
		compareSets(EXPECTED_LINKS, summary.getLinks());
		compareSets(EXPECTED_CSS, summary.getResources());
		compareSets(EXPECTED_SCRIPTS, summary.getScripts());
	}

	@Test
	public void parseNothingFound(){
		String body = "<html></html>";
		WebsiteCrawlerResult summary = WebsiteParser.parse(body);

		assertTrue(summary.getLinks().isEmpty());
		assertTrue(summary.getResources().isEmpty());
		assertTrue(summary.getScripts().isEmpty());
		assertTrue(summary.getImages().isEmpty());
	}
}
