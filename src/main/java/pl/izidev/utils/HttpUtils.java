package pl.izidev.utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

public class HttpUtils {

	private static final String HTTP_PREFIX = "http://";
	private static final String HTTPS_PREFIX = "https://";
	private static final String CONTEXT_SPECIFIC_PREFIX = "//";

	protected static boolean isAbsolutePath(String url) {
		return url.startsWith(HTTP_PREFIX) || url.startsWith(HTTPS_PREFIX) || url.startsWith(CONTEXT_SPECIFIC_PREFIX);
	}

	public static boolean isMatchingHost(String host, String url) {
		String regex = ".*//" + host + "[/]?.*";
		return !isAbsolutePath(url) || (isAbsolutePath(url) && url.matches(regex));
	}

	public static Optional<String> getHost(String url) {
		try {
			return Optional.ofNullable(new URL(url).getHost());
		} catch (MalformedURLException e) {
			return Optional.empty();
		}
	}
}
