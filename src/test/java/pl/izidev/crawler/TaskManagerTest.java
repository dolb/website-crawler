package pl.izidev.crawler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import org.junit.Test;

public class TaskManagerTest {

	@Test
	public void addUrl_valid() {
		TaskManager taskManager = new TaskManager("example.com");
		taskManager.addUrl("http://example.com");
		assertEquals("http://example.com", taskManager.getNextUrl().orElse(null));
	}

	@Test
	public void addUrl_invalidDifferentHost() {
		TaskManager taskManager = new TaskManager("example.com");
		taskManager.addUrl("http://google.com/example.com");
		assertFalse(taskManager.getNextUrl().isPresent());
	}

	@Test
	public void addUrl_invalidAsItContainsAnchor() {
		TaskManager taskManager = new TaskManager("example.com");
		taskManager.addUrl("http://example.com#local-anchor");
		taskManager.addUrl("http://example.com/some-page#local-anchor");
		assertFalse(taskManager.getNextUrl().isPresent());
	}

	@Test
	public void addUrl_afterUrlWasPresent() {
		TaskManager taskManager = new TaskManager("example.com");
		taskManager.addUrl("http://example.com");
		taskManager.getNextUrl();
		assertFalse(taskManager.getNextUrl().isPresent());
	}

	@Test
	public void emptyQueue_afterUrlWasPresent() {
		TaskManager taskManager = new TaskManager("example.com");
		taskManager.addUrl("http://example.com");
		taskManager.getNextUrl();
		assertFalse(taskManager.getNextUrl().isPresent());
	}

	@Test
	public void emptyQueue_newTaskManager() {
		TaskManager taskManager = new TaskManager("example.com");
		assertFalse(taskManager.getNextUrl().isPresent());
	}


	@Test
	public void emptyQueue_afterWrongUrl() {
		TaskManager taskManager = new TaskManager("example.com");
		taskManager.addUrl("http://google.com/image.jpg");
		assertFalse(taskManager.getNextUrl().isPresent());
	}
}
