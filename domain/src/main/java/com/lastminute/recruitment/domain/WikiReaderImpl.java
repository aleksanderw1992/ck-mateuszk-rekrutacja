package com.lastminute.recruitment.domain;

import com.lastminute.recruitment.domain.error.WikiPageNotFound;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class WikiReaderImpl implements WikiReader {

    @Override
    public WikiPage read(String link) {
        try {
            // Parse the HTML file
            Document doc = Jsoup.parse(Paths.get(link).toFile(), "UTF-8");

            // Extract the title
            String title = doc.select("h1.title").first().text();

            // Extract the content
            String content = doc.select("p.content").first().text();

            // Extract the selfLink
            String selfLink = doc.select("meta[selfLink]").first().attr("selfLink");

            // Extract links
            List<String> links = new ArrayList<>();
            for (Element linkElement : doc.select("ul.links a")) {
                links.add(linkElement.attr("href"));
            }

            return new WikiPage(title, content, selfLink, links);

        } catch (IOException e) {
            throw new WikiPageNotFound("Wiki page not found at: " + link, e);
        }
    }
}