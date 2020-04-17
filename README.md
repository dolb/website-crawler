# website-crawler

This small project shall crawl through website by a given URL and build site map as either JSON or HTML document.
It uses Java11 features, so that is the SDK needed for compilation and building phase.

## How to build

The project uses maven as a build tool. I will assume you have it installed, and added to your PATH in this README. If this is not the case please visit https://maven.apache.org/install.html. 

It assembles an executable JAR file on the packaging phase, so to create a fat-jar containing every library needed to run it use:

```mvn clean package```

If you are unfamiliar with maven, this command will compile, build and run unit tests. In the end it assembles the jar that will be available under 'target' directory.

## How to run

If you have the release fat-jar downloaded or assembled in target directory, you have two ourput modes.

### JSON output (default)

```java -jar crawler-VERSION-jar-with-dependencies.jar http://example.com```

This command will crawl **http://example.com** website and:
1) Print output on the console.
2) Create **crawler-output.json** file containing crawlers findings in your current directory.

### HTML output (optional)

You may also want to get an output as HTML document by adding **html** parameter:

```java -jar crawler-VERSION-jar-with-dependencies.jar http://example.com html```

As a result **crawler-output.html** document will be created. It has some build in JavaScript to navigate over crawlers findings that should work in most modern browsers. Simply run the output file in browser of your choice.

## Implementation plan

The basic algorithm to implement the crawler:

1) Build a task queue, that will be populated with distinct URLs found on the website.
2) Every URL previously visited will be deleted from the task queue.
3) Repeat the process until the task queue is empty.
4) After whole map is created translate website map to HTML file.

## Trade offs

My crawler has multiple trade offs that you need to know about:

1) As the crawler is taking its knowledge from DOM structure of visited URLs, any SPA (Single Page Application) website will not be mapped properly. You would need third party modules like PhantomJS to first render the page, and then parse its DOM.
2) I decided to use Jsoup for DOM parsing. In my implementation I am only interested in certain DOM elements:
    * **img** for images
    * **script** for scripts
    * **link** for resources
    * **a** for mapped links that
    
   Saying that there are few examples, that my crawler will miss:
    * If the page has images based on CSS background inherited either from class or style attribute.
    * Async scripts loaded by JavaScript.
    * Form elements, that uses GET methods and hidden inputs (as it is in most cases context- specific situation).


### Known issues

There are some issues known to me I will fix in the future commits:

1) Currently crawler omits context relative URLs in the crawling process (ie. some-page, /some-page, //example.com/some-page). They will be added to any link sections, but they will not be visited and parsed.
2) Mapped URLs has a Set based structure, so in case of finding similar URLs with an anchor reference (ie. /#anchor, #anchor) they will both be added.
3) Crawler has a build in logic to omit URLs with an anchor (ie. http://example.com/somepage#anchor). It is a hypothetical case, but if there is a single link on crawled website containing anchor without any other occurrence without an anchor it will not be visited by the crawler.
4) Crawler might be susceptible for sneaky Website creators :) For example if one might place a hidden link not visible to website users, that would look like an ordinary HTML reference. If it would not be an HTML document, parsing error would occur. It would not stop the crawler process, but such URL would be skipped in visited URLs section.

## Possible extensions

As mentioned in the 'Trade offs' section there are several enhancements that ay be added:

1) Parsing both DOM for style attribute and CSS files for background related patterns. Such urls might be added to **images** Set.
2) Parallel crawling is also a valid solution to speed up the crawling process. Currently the crawler is visiting another URL after it is done with the previous as the WebsiteCrawler is not thread safe. Adding thread safe version with synchronized state fields would allow the crawler to call 'crawl' method more often speeding up the process.
3) Implementing speed parameter would be a valuable option especially if we would like to add parallel implementation. In case of too many concurrent requests in short period of time many websites would drop crawler requests.
4) Adding separate sections for response codes other than 200 would be a valuable extension. Currently if a website has a broken link (that returns 404, 502 or other client or server side errors) it will not place it on the website map. Separate section would be useful to find broken links on the website as a tool for website administrators.