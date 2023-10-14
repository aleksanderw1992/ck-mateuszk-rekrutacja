package com.lastminute.recruitment;

import com.lastminute.recruitment.client.WikiReaderImplHtml;
import com.lastminute.recruitment.client.WikiReaderImplJson;
import com.lastminute.recruitment.domain.WikiScrapper;
import com.lastminute.recruitment.domain.WikiPageRepository;
import com.lastminute.recruitment.domain.WikiReader;
import com.lastminute.recruitment.persistence.InMemoryWikiPageRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class WikiScrapperConfiguration {

    @Bean
    @Profile("!json") // should be @Profile("html") but leaving for simplicity in testing
    public WikiReader htmlWikiReader() {
        return new WikiReaderImplHtml();
    }

    @Bean
    @Profile("json")
    public WikiReader jsonWikiReader() {
        return new WikiReaderImplJson();
    }

    @Bean
    public WikiPageRepository wikiPageRepository() {
        return new InMemoryWikiPageRepository();
    }

    @Bean
    public WikiScrapper wikiScrapper(WikiPageRepository wikiPageRepository, WikiReader wikiReader) {
        return new WikiScrapper(wikiReader, wikiPageRepository);
    }
}
