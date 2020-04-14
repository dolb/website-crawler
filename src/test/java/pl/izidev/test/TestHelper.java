package pl.izidev.test;

import java.nio.file.Files;
import java.nio.file.Path;

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
