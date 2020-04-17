package pl.izidev.utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

/**
 * Miscellaneous helper methods for parsing URLs.
 */
public class HttpUtils {

	private static final String HTTP_PREFIX = "http://";
	private static final String HTTPS_PREFIX = "https://";
	private static final String CONTEXT_SPECIFIC_PREFIX = "//";

	static boolean isAbsolutePath(String url) {
		return url.startsWith(HTTP_PREFIX) || url.startsWith(HTTPS_PREFIX) || url.startsWith(CONTEXT_SPECIFIC_PREFIX);
	}

	public static boolean isMatchingHost(String host, String url) {
		String regex = ".*//" + host + "[/]?.*";
		return (isAbsolutePath(url) && url.matches(regex));
	}

	public static Optional<String> getHost(String url) {
		try {
			return Optional.ofNullable(new URL(url).getHost());
		} catch (MalformedURLException e) {
			return Optional.empty();
		}
	}

	public static String removeTrailingSlash(String url) {
		return Optional
			.ofNullable(url)
			.filter(el -> el.endsWith("/"))
			.map(el -> el.substring(0, el.length() - 1))
			.orElse(url);
	}
}
