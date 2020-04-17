package pl.izidev.crawler.output;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import pl.izidev.crawler.WebsiteCrawlerResult;

public class HTMLCrawlerOutputTest {

	@Test
	public void convert_simple() {
		List<WebsiteCrawlerResult> list = new ArrayList<>();
		list.add(
			new WebsiteCrawlerResult()
			.setUrl("http://example.com")
			.addImages(new HashSet<>(Arrays.asList("http://example.com/image.jpg")))
			.addLinks(new HashSet<>(Arrays.asList("http://example.com/link")))
			.addResources(new HashSet<>(Arrays.asList("http://example.com/resource")))
		);
		String converted = new HTMLCrawlerOutput().convert(list);


		String expected = String.join(
			"",
			"<html>",
			" <head></head>",
			" <body> ",
			"  <ol><li><a href=\"#http://example.com\">http://example.com</a></li></ol>",
			"  <div id=\"http://example.com\">",
			"   <h1>http://example.com</h1>",
			"   <div>",
			"    <h3>Links</h3>",
			"    <ul>",
			"     <li>http://example.com/link</li>",
			"    </ul>",
			"   </div>",
			"   <div>",
			"    <h3>Images</h3>",
			"    <ul>",
			"     <li>http://example.com/image.jpg</li>",
			"    </ul>",
			"   </div>",
			"   <div>",
			"    <h3>Scripts</h3>",
			"   </div>",
			"   <div>",
			"    <h3>Resources</h3>",
			"    <ul>",
			"     <li>http://example.com/resource</li>",
			"    </ul>",
			"   </div>",
			"  </div>",
			" </body>",
			"</html>"
		).replaceAll("\\s+", "");
		Assert.assertEquals(expected, converted.replaceAll("\\s+", "").replaceAll("\n", ""));
	}
}
