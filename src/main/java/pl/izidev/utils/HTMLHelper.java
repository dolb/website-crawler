package pl.izidev.utils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Simple HTML helper used for generating HTML parts for HTMLCrawlerOutput class.
 * It supports simple templating and seemed faster to implement than using Jsoup library to create DOM.
 */
public class HTMLHelper {

	private final static String EMPTY_STRING = "";

	public static String populateTemplateResource(String templateName, Map<String, String> params) {
		return ResourceHelper
			.loadResourceAsString(templateName)
			.map(templateFileContent -> populateTemplate(templateFileContent, params))
			.orElse(EMPTY_STRING);
	}

	static String populateTemplate(String template, Map<String, String> params) {
		return params
			.entrySet()
			.stream()
			.reduce(
				template,
				(accumulator, element) -> accumulator.replace("{{" + element.getKey() + "}}", element.getValue()),
				(accumulator, element) -> accumulator);
	}

	public static String createLinkSection(String title, Set<String> urls) {
		return String.format(
			"<div>%s%s</div>",
			toSubtitle(title),
			toList(urls)
		);
	}

	public static String toTitle(String title) {
		return String.format("<h1>%s</h1>", title);
	}

	private static String toSubtitle(String title) {
		return String.format("<h3>%s</h3>", title);
	}

	private static String toList(Set<String> set) {
		if (set.isEmpty()) {
			return EMPTY_STRING;
		}
		return String.format(
			"<ul>%s</ul>",
			set.stream().map(HTMLHelper::toListElement).collect(Collectors.joining(EMPTY_STRING))
		);
	}

	static String toListWithIdentifiers(List<String> entries) {
		return entries
			.stream()
			.map(entry -> HTMLHelper.toListElementWithId(entry, entry))
			.collect(Collectors.joining(EMPTY_STRING));
	}

	public static String toListElement(String url) {
		return String.format("<li>%s</li>", url);
	}

	private static String toListElementWithId(String title, String id) {
		return String.format("<li id=\"%s\">%s</li>", id, title);
	}

	public static String toDivWithId(String id, String... elements) {
		if(elements.length < 1) {
			return EMPTY_STRING;
		}
		return String.format("<div id=\"%s\">%s</div>", id, String.join("", elements));
	}

	public static String toLocalElementReference(String element) {
		return String.format("<a href=\"%s\">%s</a>", String.join("", "#", element), element);
	}
}
