package pl.izidev.crawler;

import java.util.HashSet;
import java.util.Set;

/**
 * Summary model created after page body is parsed.
 */
public class WebsiteCrawlerResult {
	private String url;
	private Set<String> images;
	private Set<String> scripts;
	private Set<String> links;
	private Set<String> resources;
	private String exception;


	public WebsiteCrawlerResult() {
		this.images = new HashSet<>();
		this.scripts = new HashSet<>();
		this.links = new HashSet<>();
		this.resources = new HashSet<>();
	}

	public WebsiteCrawlerResult setUrl(String url) {
		this.url = url;
		return this;
	}

	public WebsiteCrawlerResult addLinks(Set<String> links) {
		this.links.addAll(links);
		return this;
	}

	public WebsiteCrawlerResult addImages(Set<String> links) {
		this.images.addAll(links);
		return this;
	}

	public WebsiteCrawlerResult addScripts(Set<String> links) {
		this.scripts.addAll(links);
		return this;
	}

	public WebsiteCrawlerResult addResources(Set<String> links) {
		this.resources.addAll(links);
		return this;
	}

	public String getUrl() {
		return url;
	}

	public Set<String> getImages() {
		return images;
	}

	public Set<String> getScripts() {
		return scripts;
	}

	public Set<String> getResources() {
		return resources;
	}

	public Set<String> getLinks() {
		return links;
	}

	public WebsiteCrawlerResult setException(String exception) {
		this.exception = exception;
		return this;
	}

	public String getException() {
		return exception;
	}

	public String toString() {
		return this.url
			+ "\n LINKS :: " + this.links
			+ "\n IMAGES :: " + this.images
			+ "\n SCRIPTS :: " + this.scripts
			+ "\n RESOURCES :: " + this.resources
			+ "\n EXCEPTION :: " + this.exception;
	}

}
