# website-crawler

This small project shall crawl through website by a given URL and build site map as HTML document.

## Implementation plan

The basic algorithm to implement the crawler:

1) Build a task queue, that will be populated with distinct URLs found on the website.
2) Every URL previously visited will be deleted from the task queue.
3) Repeat the process until the task queue is empty.
4) After whole map is created translate website map to HTML file.