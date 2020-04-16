package pl.izidev.utils;

import java.util.*;
import org.junit.Assert;
import org.junit.Test;

public class HTMLHelperTest {

	@Test
	public void populateTemplateTest() {

		final String template = "<body><div>{{param1}}</div><div>{{param2}}</div></body>";

		Map<String, String> params = new HashMap<>();
		params.put("param1", "CONTENT");
		params.put("param2", "<h1>HTML</h1>");

		Assert.assertEquals(
			"<body><div>CONTENT</div><div><h1>HTML</h1></div></body>",
			HTMLHelper.populateTemplate(template, params)
		);
	}

	@Test
	public void toListWithIdentifiers() {
		List<String> entries = new ArrayList<>();
		entries.add("EL1");
		entries.add("EL2");

		String expected = "<li id=\"EL1\">EL1</li><li id=\"EL2\">EL2</li>";

		Assert.assertEquals(expected, HTMLHelper.toListWithIdentifiers(entries));
	}

	@Test
	public void toDivWithId_noElements() {
		Assert.assertEquals("", HTMLHelper.toDivWithId("ID"));
	}

	@Test
	public void toDivWithId_hasElements() {
		Assert.assertEquals("<div id=\"ID\">EL1EL2</div>", HTMLHelper.toDivWithId("ID", "EL1", "EL2"));
	}

	@Test
	public void toTitle() {
		Assert.assertEquals("<h1>TITLE</h1>", HTMLHelper.toTitle("TITLE"));
	}

	@Test
	public void toLocalElementReference() {
		Assert.assertEquals(
			"<a href=\"#local-reference\">local-reference</a>",
			HTMLHelper.toLocalElementReference("local-reference"));
	}

}
