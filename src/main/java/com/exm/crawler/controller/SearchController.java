package com.exm.crawler.controller;

import com.exm.crawler.model.ResponseDTO;
import com.exm.crawler.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/api/search")
public class SearchController {

    @Autowired
    SearchService service;

    @GetMapping(path = "")
    public List<ResponseDTO> extractData() {
        return service.searchContent();
    }
}
