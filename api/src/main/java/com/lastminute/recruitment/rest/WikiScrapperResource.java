package com.lastminute.recruitment.rest;

import com.lastminute.recruitment.domain.WikiScrapper;
import com.lastminute.recruitment.domain.error.WikiPageNotFound;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/wiki")
@RestController
public class WikiScrapperResource {
    private static final Logger LOG = Logger.getLogger(WikiScrapperResource.class.getName());
    @Autowired
    private WikiScrapper wikiScrapper;


    @PostMapping("/scrap")
    public void scrapWikipedia(@RequestBody String link) {
        wikiScrapper.read(link);
    }
    @ExceptionHandler(WikiPageNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleException(WikiPageNotFound ex) {
        LOG.log(Level.INFO, "Caught in controller", ex);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public void handleException(Exception ex) {
        LOG.log(Level.INFO, "Caught in controller", ex);
    }
}
