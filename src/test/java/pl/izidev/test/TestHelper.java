package pl.izidev.test;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Simple helper class that should be a part of some external test module.
 */
public class TestHelper {

	private static final String EMPTY_STRING = "";

	public static String getResourcesAsString(String path) {
		try {
			return Files.readString(Path.of(TestHelper.class.getResource(path).toURI()));
		} catch (Exception e) {
			return EMPTY_STRING;
		}

	}

}
