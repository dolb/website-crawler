package pl.izidev;

import pl.izidev.crawler.WebsiteCrawler;

public class Main {

	public static void main(String[] args) {
		WebsiteCrawler crawler = new WebsiteCrawler("http://example.com");
		Thread mainThread = new Thread(crawler);

		mainThread.start();
	}

}
