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

	public static boolean isUrlContextRelative(String url) {
		return (!url.startsWith(HTTP_PREFIX) && !url.startsWith(HTTPS_PREFIX));
	}

	public static boolean isMatchingHost(String host, String url) {
		String regex = ".*//" + host + "[/]?.*";
		return url.matches(regex);
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

	/**
	 * @param url child url that is context relative
	 * @param parentUrl must contain host and path
	 * @return extends context relative to absolute url appending parents URL host
	 */
	public static String extendContentRelativeLink(String url, String parentUrl) {
		try {
			URL parentURL = new URL(parentUrl);
			return url.startsWith(CONTEXT_SPECIFIC_PREFIX)
					? String.join(":", parentURL.getProtocol(), url)
					: String.join(":/", parentURL.getProtocol(), parentURL.getHost(), url);
		} catch (MalformedURLException e) {
			return "";
		}


	}
}
