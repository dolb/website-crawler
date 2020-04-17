package pl.izidev;

import java.util.Optional;
import pl.izidev.crawler.WebsiteCrawler;
import pl.izidev.crawler.output.CrawlerOutput;
import pl.izidev.crawler.output.CrawlerOutputFactory;
import pl.izidev.threading.ThreadListener;

public class Main {

	public static void main(String[] args) {

		if (args.length < 1) {
			System.out.println("Please pass a proper URL for the crawler to start ie.");
			System.out.println("java -jar crawler.jar http://example.com");
			System.exit(1);
		}

		String startingURL = args[0];

		try {
			ThreadListener listener = new ThreadListener();
			CrawlerOutput output = CrawlerOutputFactory.getCrawlerOutput(
				Optional.of(args).filter(arr -> arr.length >= 2).map(arr -> arr[1])
			);
			WebsiteCrawler crawler = new WebsiteCrawler(startingURL, listener, output);
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
		} catch (RuntimeException e) {
			System.err.println(e.toString());
			System.exit(1);
		}
	}

}
