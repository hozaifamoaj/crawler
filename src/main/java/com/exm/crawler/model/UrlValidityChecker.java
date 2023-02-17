package com.exm.crawler.model;

import com.exm.crawler.util.Util;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UrlValidityChecker {

    String url;
    Boolean isValid;

    public UrlValidityChecker(String rootUrl, String path) {
        this.url = path;

        if (Util.validateUrl(path) && path.contains(rootUrl)) {
            this.isValid = true;
        } else if (Util.validateUrl(path) && !path.contains(rootUrl)) {
            this.isValid = false;
        } else {
            this.url = new StringBuilder().append(rootUrl).append(path).toString();
            this.isValid = Util.validateUrl(this.url);
        }
    }
}
