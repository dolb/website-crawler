package pl.izidev.crawler;

import java.util.*;
import pl.izidev.utils.HttpUtils;

class TaskManager {

	private Queue<String> tasks;
	private final String whitelistedHost;
	private Set<String> processedTasks;

	TaskManager(String whitelistedHost) {
		this.tasks = new PriorityQueue<>();
		this.whitelistedHost = whitelistedHost;
		this.processedTasks = new HashSet<>();
	}

	/**
	 * URL will be considered a valid crawler task if
	 * <ol>
	 *     <li>
	 *         It matches crawled domain host.
	 *     </li>
	 *     <li>
	 *         It does not contain a local anchor.
	 *     </li>
	 *     <li>
	 *         It was not a task processed before.
	 *     </li>
	 * </ol>
	 * @param url url that should be put into task queue
	 * @return own instance for chain- call code style
	 */
	TaskManager addUrl(String url) {
		Optional
			.ofNullable(url)
			.map(HttpUtils::removeTrailingSlash)
			.ifPresent(
				element -> {
					if (HttpUtils.isMatchingHost(this.whitelistedHost, element)
						&& !element.contains("#")
						&& !processedTasks.contains(element)) {
							this.tasks.add(element);
							this.processedTasks.add(element);
					}
				}
		);
		return this;
	}



	/**
	 * Polling next task from the task queue, packed with Optional.
	 */
	Optional<String> getNextUrl() {
		return Optional.ofNullable(this.tasks.poll());
	}
}
