package com.exm.crawler.service.impl;

import com.exm.crawler.model.ResponseDTO;
import com.exm.crawler.model.UrlValidityChecker;
import com.exm.crawler.service.ScraperService;
import com.exm.crawler.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

@Slf4j
@Service
@PropertySources(
        {@PropertySource("classpath:application.properties")}
)
public class ScraperServiceImpl implements ScraperService {

    @Value("#{'${website.urls}'.split(',')}")
    List<String> urls;

    @Value("${website.url}")
    private String websiteUrl;

    @Value("${website.prefix}")
    private String siteUrl;

    @Override
    public List<ResponseDTO> extractData() {

        Queue<String> urlQueue = new LinkedList<>();
        extractUrl(siteUrl, urlQueue);
        log.info("Found url {}", urlQueue.size());

        List<ResponseDTO> visitedURLs = new ArrayList<>();

        if (urlQueue != null) {
            for (String url : urlQueue) {
                ResponseDTO responseDTO = retrieveDataFromUrl(url);
                visitedURLs.add(responseDTO);
            }
        }

        return visitedURLs;
    }

    private void extractUrl(String url, Queue<String> urlQueue) {

        Elements elements = getElementsByAnchor(url);

        for (Element ads : elements) {

            if (StringUtils.isNotEmpty(ads.attr("href"))) {

                String hrefTxt = ads.attr("href");

                if (!Util.isCustomValidatorPass(hrefTxt)) {
                    log.debug("Invalid path: {}", hrefTxt);
                    continue;
                }

                UrlValidityChecker validityChecker = new UrlValidityChecker(url, hrefTxt);

                if (validityChecker.getIsValid() && !urlQueue.contains(validityChecker.getUrl())) {
                    log.info("Url found {}", validityChecker.getUrl());
                    urlQueue.add(validityChecker.getUrl());
                    extractUrl(url, urlQueue);
                }
            }
        }
    }

    private Elements getElementsByAnchor(String url) {
        Elements elements = null;
        try {
            Document document = Jsoup.connect(url).get();
            Element element = document.getElementsByTag("body").first();
            elements = element.getElementsByTag("a");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return elements;
    }

    private ResponseDTO retrieveDataFromUrl(String url) {
        try {
            Document document = Jsoup.connect(url).get();
            Element element = document.getElementsByClass("mainwrp").first();

            ResponseDTO responseDTO = new ResponseDTO();
            responseDTO.setUrl(url);
            if (element != null) {
                responseDTO.setBodyContent(element.text());
            } else {
                responseDTO.setBodyContent("");
            }
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
            LocalDateTime now = LocalDateTime.now();
            responseDTO.setLast_crawled_at(dtf.format(now));
            return responseDTO;
        } catch (IOException ex) {
            log.error("data not extracted: {}", url);
        }
        return new ResponseDTO();
    }

    @Override
    public Object crawlDataUsingSelenium() {

        // Set the path to the ChromeDriver executable
        System.setProperty("webdriver.chrome.driver", "E:/tmp/chromedriver_win32/chromedriver.exe");

        // Enable the headless flag
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");

        // Create a new ChromeDriver instance
        WebDriver driver = new ChromeDriver(options);

        // Wait for the page to fully load and render
        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);

        try {
            // Navigate to the website you want to crawl
            driver.get("https://konaplate.com/ko/developers/apis-specifications");

            // Wait for the page to fully load and render
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

            // Extract the relevant data from the page
            String title = driver.getTitle();
            String pageSource = driver.getPageSource();

            System.out.println("Title: " + title);
            System.out.println("page source:");
            System.out.println(Jsoup.parse(pageSource));

//            Document document = Jsoup.parse(pageSource);
//            Element element = document.getElementsByTag("body").first();
//            Elements elements = element.getElementsByTag("a");

            List<WebElement> allLinks = driver.findElements(By.tagName("a"));
//
            //Traversing through the list and printing its text along with link address
            for (WebElement link : allLinks) {
                System.out.println(link.getText() + " - " + link.getAttribute("href"));
            }
//            // Do something with the data, such as save it to a database or file
//            /*BufferedWriter writer = new BufferedWriter(new FileWriter("page.html"));
//            writer.write(pageSource);
//            writer.close();
//            */
//
//            System.out.println("Successfully crawled website: " + title);

            // that will validate the content of HTML in search of a URL.
            String urlPattern = "(www|http:|https:)+[^\s]+[\\w]";
            Pattern pattern = Pattern.compile(urlPattern);

            Queue<String> urlQueue = new LinkedList<>();

        } catch (Exception e) {
            // Handle any errors or exceptions that occur
            e.printStackTrace();
        } finally {
            // Close the browser
            driver.quit();
        }

        return new Object();
    }

}