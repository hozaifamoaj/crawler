package com.exm.crawler.controller;

import com.exm.crawler.model.ResponseDTO;
import com.exm.crawler.service.ScraperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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