package pl.izidev;

import pl.izidev.crawler.WebsiteCrawler;
import pl.izidev.threading.ThreadListener;

public class Main {

	public static void main(String[] args) {

		ThreadListener listener = new ThreadListener();
		WebsiteCrawler crawler = new WebsiteCrawler("http://example.com", listener);
		Thread crawlerThread = new Thread(crawler);

		synchronized (listener) {
			try {
				crawlerThread.start();
				System.out.println("Waiting for crawler to finish the job");
				listener.wait();
				System.out.println("Crawler done - exiting");
				System.exit(0);
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
	}

}
