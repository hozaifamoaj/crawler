package com.exm.crawler.controller;

import com.exm.crawler.model.ResponseDTO;
import com.exm.crawler.service.ScraperService;
import org.jsoup.Jsoup;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

@RestController
@RequestMapping(path = "/api/crawler")
public class ScraperController {

    @Value("${website.prefix}")
    private String siteUrl;

    @Autowired
    ScraperService scraperService;

    @GetMapping(path = "/extract")
    public List<ResponseDTO> extractData() {
        return scraperService.extractData();
    }

    @GetMapping(path = "/extract-p2")
    public void getCrawlerData2() {
        scraperService.crawlDataUsingSelenium();
    }
}