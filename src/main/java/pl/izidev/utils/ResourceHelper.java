package pl.izidev.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Scanner;

public class ResourceHelper {

	static Optional<String> loadResourceAsString(String resourceName) {
		InputStream inputStream = ResourceHelper.class.getResourceAsStream(String.join("", "/", resourceName));
		String result;
		try (Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8.name())) {
			result = scanner.useDelimiter("\\A").next();
		}
		return Optional.ofNullable(result);
	}

	public static Optional<String> toFile(String name, String content) {
		Path path = Paths.get(name);
		try {
			Files.write(path, content.getBytes());
			return Optional.of(name);
		} catch (IOException e) {
			System.err.println(String.format("Could not save content to file %s", name));
			return Optional.empty();
		}
	}
}
