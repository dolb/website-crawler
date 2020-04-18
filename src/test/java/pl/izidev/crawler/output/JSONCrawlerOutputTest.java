package pl.izidev.crawler.output;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import pl.izidev.crawler.WebsiteCrawlerResult;

public class JSONCrawlerOutputTest {

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
		String converted = new JSONCrawlerOutput().convert(list);
		String expected = String.join(
			"\n",
			"{",
			"  \"list\" : [ {",
			"    \"url\" : \"http://example.com\",",
			"    \"images\" : [ \"http://example.com/image.jpg\" ],",
			"    \"scripts\" : [ ],",
			"    \"links\" : [ \"http://example.com/link\" ],",
			"    \"resources\" : [ \"http://example.com/resource\" ],",
			"    \"exception\" : null",
			"  } ]",
			"}"
		);
		Assert.assertEquals(expected, converted);
	}
}
