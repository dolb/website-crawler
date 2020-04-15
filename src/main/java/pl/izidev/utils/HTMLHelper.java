package pl.izidev.utils;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class HTMLHelper {

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

	public static String toSubtitle(String title) {
		return String.format("<h3>%s</h3>", title);
	}

	protected static String toList(Set<String> urls) {
		if (urls.isEmpty()) {
			return String.format("<h4>%s</h4>", "-");
		}
		return String.format(
			"<ul>%s</ul>",
			urls.stream().map(HTMLHelper::toListElement).collect(Collectors.joining(""))
		);
	}

	private static String toListElement(String url) {
		return String.format("<li>%s</li>", url);
	}

	public static String toDiv(String... elements) {
		return String.format("<div>%s</div>", String.join("", elements));
	}
}
