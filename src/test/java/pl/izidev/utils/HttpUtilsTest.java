package pl.izidev.utils;

import static org.junit.Assert.*;
import org.junit.Test;

public class HttpUtilsTest {

	@Test
	public void isAbsolutePath_http() {
		assertTrue(HttpUtils.isAbsolutePath("http://example.com/test#local-reference"));
		assertTrue(HttpUtils.isAbsolutePath("http://example.com/test"));
		assertTrue(HttpUtils.isAbsolutePath("http://example.com"));
	}

	@Test
	public void isAbsolutePath_https() {
		assertTrue(HttpUtils.isAbsolutePath("https://example.com/test#local-reference"));
		assertTrue(HttpUtils.isAbsolutePath("https://example.com/test"));
		assertTrue(HttpUtils.isAbsolutePath("https://example.com"));
	}

	@Test
	public void isAbsolutePath_contextSpecific() {
		assertTrue(HttpUtils.isAbsolutePath("//example.com/test#local-reference"));
		assertTrue(HttpUtils.isAbsolutePath("//example.com/test"));
		assertTrue(HttpUtils.isAbsolutePath("//example.com"));
	}

	@Test
	public void isAbsolutePath_falseCases() {
		assertFalse(HttpUtils.isAbsolutePath("assets/image.png"));
		assertFalse(HttpUtils.isAbsolutePath("/assets/image.png"));
	}

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


}
