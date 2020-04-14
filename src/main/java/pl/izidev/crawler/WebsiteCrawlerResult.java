package pl.izidev.crawler;

import java.util.Set;
import java.util.TreeSet;
import pl.izidev.parser.WebsiteParser;

/**
 * Summary model created after page body is parsed.
 * FIXME
 * Figure out how the parsed data will be provided to the model.
 */
public class WebsiteCrawlerResult {
	private String url;
	private Set<String> images;
	private Set<String> scripts;
	private Set<String> links;
	private Set<String> resources;


	public WebsiteCrawlerResult() {
		this.images = new TreeSet<>();
		this.scripts = new TreeSet<>();
		this.links = new TreeSet<>();
		this.resources = new TreeSet<>();
	}

	public WebsiteCrawlerResult setUrl(String url) {
		this.url = url;
		return this;
	}

	public void addLink(String link) {
		this.links.add(link);
	}

	public void addImage(String link) {
		this.images.add(link);
	}

	public void addScript(String link) {
		this.scripts.add(link);
	}

	public void addResource(String link) {
		this.resources.add(link);
	}

	public String toString() {
		return this.url
			+ "\n LINKS :: " + this.links
			+ "\n IMAGES :: " + this.images
			+ "\n SCRIPTS :: " + this.scripts
			+ "\n RESOURCES :: " + this.resources;
	}

	public Set<String> getLinks() {
		return links;
	}

	public WebsiteCrawlerResult parseBody(String body) {
		WebsiteParser.parse(body);
		return this;
	}
}
