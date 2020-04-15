package pl.izidev.crawler;

import io.reactivex.annotations.NonNull;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Queue;
import pl.izidev.utils.HttpUtils;

public class TaskManager {

	private Queue<String> tasks;
	private final String whitelistedHost;

	public TaskManager(String whitelistedHost) {
		this.tasks = new PriorityQueue<>();
		this.whitelistedHost = whitelistedHost;
	}

	public TaskManager addUrl(@NonNull String url) {
		Optional.ofNullable(url).ifPresent(
			element -> {
				if (HttpUtils.isMatchingHost(this.whitelistedHost, element) && !element.contains("#")) {
					tasks.add(element);
				}
			}
		);
		return this;
	}

	public Optional<String> getNextUrl() {
		return Optional.ofNullable(this.tasks.poll());
	}
}
