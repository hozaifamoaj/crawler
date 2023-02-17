package com.exm.crawler.service;

import com.exm.crawler.model.ResponseDTO;

import java.util.List;

public interface ScraperService {

    List<ResponseDTO> extractData();

    Object crawlDataUsingSelenium();
}