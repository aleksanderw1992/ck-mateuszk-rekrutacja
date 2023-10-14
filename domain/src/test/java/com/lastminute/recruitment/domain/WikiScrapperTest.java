package com.lastminute.recruitment.domain;

import static org.junit.jupiter.api.Assertions.*;

import com.lastminute.recruitment.domain.error.WikiPageNotFound;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
class WikiScrapperTest {

    @Mock
    private WikiReader wikiReader;

    @Mock
    private WikiPageRepository repository;

    private WikiScrapper scrapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        scrapper = new WikiScrapper(wikiReader, repository);
    }

    @Test
    void read_singlePage_noLinks() {
        when(wikiReader.read("testLink")).thenReturn(new WikiPage("Test", "Content", "testLink", Collections.emptyList()));

        scrapper.read("testLink");

        verify(repository).save(any(WikiPage.class));
        verify(wikiReader).read("testLink");
    }

    @Test
    void read_singlePage_withLinks() {
        when(wikiReader.read("testLink")).thenReturn(new WikiPage("Test", "Content", "testLink", Arrays.asList("link1", "link2")));
        when(wikiReader.read("link1")).thenReturn(new WikiPage("Link1Title", "Link1Content", "link1", Collections.emptyList()));
        when(wikiReader.read("link2")).thenReturn(new WikiPage("Link2Title", "Link2Content", "link2", Collections.emptyList()));

        scrapper.read("testLink");

        verify(repository, times(3)).save(any(WikiPage.class));
        verify(wikiReader).read("testLink");
        verify(wikiReader).read("link1");
        verify(wikiReader).read("link2");
    }

    @Test
    void read_pageWithLoopedLinks() {
        when(wikiReader.read("testLink")).thenReturn(new WikiPage("Test", "Content", "testLink", Arrays.asList("loopLink")));
        when(wikiReader.read("loopLink")).thenReturn(new WikiPage("Looped", "Content", "loopLink", Arrays.asList("testLink")));

        scrapper.read("testLink");

        verify(repository, times(2)).save(any(WikiPage.class));
        verify(wikiReader).read("testLink");
        verify(wikiReader).read("loopLink");
    }

    @Test
    void read_pageNotFound() {
        when(wikiReader.read("testLink")).thenThrow(new WikiPageNotFound("Page not found"));

        assertThrows(WikiPageNotFound.class, () -> scrapper.read("testLink"));

        verify(repository, never()).save(any(WikiPage.class));
        verify(wikiReader).read("testLink");
    }

    @Test
    void read_multipleInvocation_sameLink() {
        when(wikiReader.read("testLink")).thenReturn(new WikiPage("Test", "Content", "testLink", Collections.emptyList()));

        scrapper.read("testLink");
        scrapper.read("testLink");  // Read the same link again.

        verify(repository, times(1)).save(any(WikiPage.class));  // Ensure repository.save() is called only once.
        verify(wikiReader, times(1)).read("testLink");  // Ensure wikiReader.read() is called only once.
    }
}
