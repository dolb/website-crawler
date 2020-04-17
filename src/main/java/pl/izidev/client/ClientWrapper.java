package pl.izidev.client;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import javax.net.ssl.SSLParameters;

/**
 * Java 11 async client wrapper, as it seems like Jsoup does not support connection pooling for its build-in client.
 */
public class ClientWrapper {

	private HttpClient client;

	public ClientWrapper() {
		this.client = HttpClient.newBuilder()
			.connectTimeout(Duration.ofSeconds(30))
			.executor(Executors.newFixedThreadPool(4))
			.followRedirects(HttpClient.Redirect.ALWAYS)
			.version(HttpClient.Version.HTTP_2)
			.sslParameters(new SSLParameters())
			.build();
	}

	public CompletableFuture<String> getContent(String url) {
		System.out.println(String.format("getContent :: %s", url));
		var request = HttpRequest.newBuilder()
			.GET()
			.uri(URI.create(url))
			.timeout(Duration.ofSeconds(15))
			.build();

		return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
			.thenApply(HttpResponse::body);
	}

}
