package com.lastminute.recruitment.domain;

import java.util.HashSet;
import java.util.Set;

public class WikiScrapper {

    private final WikiReader wikiReader;
    private final WikiPageRepository repository;
    private final Set<String> visitedLinks;

    public WikiScrapper(WikiReader wikiReader, WikiPageRepository repository) {
        this.wikiReader = wikiReader;
        this.repository = repository;
        // NOTE - I am not considering cases for reading the same twice. You need to restart application
        this.visitedLinks = new HashSet<>();  // To track visited WikiPages.
    }

    // not considering @Transactional for simplicity
    public void read(String link) {
        if (visitedLinks.contains(link)) {
            return;  // Avoid reading the same page again or getting into infinite loops
        }

        // Read the WikiPage.
        WikiPage currentPage = wikiReader.read(link);

        // Save the WikiPage to the repository.
        repository.save(currentPage);

        // Mark the page as visited after successful save.
        visitedLinks.add(link);

        // Recursively read all linked pages. Assuming either all links are correct or an error occurs and state is unpredictable
        for (String linkedPage : currentPage.getLinks()) {
            read(linkedPage);
        }
    }
}
