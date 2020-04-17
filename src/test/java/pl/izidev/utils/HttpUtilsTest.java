package pl.izidev.utils;

import static org.junit.Assert.*;
import org.junit.Test;

public class HttpUtilsTest {

	@Test
	public void isMatchingHost_true() {
		assertTrue(HttpUtils.isMatchingHost("example.com", "http://example.com/test#local-reference"));
		assertTrue(HttpUtils.isMatchingHost("example.com", "https://example.com/test#local-reference"));
		assertTrue(HttpUtils.isMatchingHost("example.com", "//example.com/test#local-reference"));
	}

	@Test
	public void isMatchingHost_false() {
		assertFalse(HttpUtils.isMatchingHost("example.com", "http://google.com/example.com/wont-match"));
		assertFalse(HttpUtils.isMatchingHost("example.com", "https://google.com/example.com/wont-match"));
		assertFalse(HttpUtils.isMatchingHost("example.com", "//google.com/example.com/wont-match"));
		//This might be considered a valid relative path, but the parser will not treat it as such
		assertFalse(HttpUtils.isMatchingHost("example.com", "///google.com/example.com/this-will-match.jpg"));
		assertFalse(HttpUtils.isMatchingHost("example.com", "test#local-reference"));
		assertFalse(HttpUtils.isMatchingHost("example.com", "/test#local-reference"));
	}

	@Test
	public void getHost_http() {
		assertTrue(
			HttpUtils
				.getHost("http://example.com/some-path")
				.filter("example.com"::equals)
				.isPresent()
		);
	}

	@Test
	public void getHost_https() {
		assertTrue(
			HttpUtils
				.getHost("https://example.com/some-path")
				.filter("example.com"::equals)
				.isPresent()
		);
	}

	@Test
	public void getHost_failed() {
		assertFalse(
			HttpUtils
				.getHost("//example.com/some-path")
				.isPresent()
		);
		assertFalse(
			HttpUtils
				.getHost("/some-path")
				.isPresent()
		);
		assertFalse(
			HttpUtils
				.getHost("some-path")
				.isPresent()
		);
	}


	@Test
	public void isUrlContextRelative_true() {
		assertTrue(
			HttpUtils
				.isUrlContextRelative("//example.com/some-path")
		);
		assertTrue(
			HttpUtils
				.isUrlContextRelative("/some-path")
		);
		assertTrue(
			HttpUtils
				.isUrlContextRelative("some-path")
		);
		assertTrue(
			HttpUtils
				.isUrlContextRelative("#anchor")
		);
		assertTrue(
			HttpUtils
				.isUrlContextRelative("/#anchor")
		);
	}

	@Test
	public void isUrlContextRelative_false() {
		assertFalse(
			HttpUtils
				.isUrlContextRelative("http://example.com/some-path")
		);
		assertFalse(
			HttpUtils
				.isUrlContextRelative("https://example.com/some-path")
		);
	}
}
