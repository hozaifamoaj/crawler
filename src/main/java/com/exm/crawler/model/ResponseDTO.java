package com.exm.crawler.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResponseDTO {

    //    Integer count;
    String title = "";
    String bodyContent = "";
    String headings = "";
    String url = "";
    String domains = "";
    String urlPath = "";
    String tag = "";
    String segment = "";
    String last_crawled_at;
//    String content;
}
