package com.lastminute.recruitment.client;

import com.lastminute.recruitment.domain.WikiPage;
import com.lastminute.recruitment.domain.WikiReader;
import com.lastminute.recruitment.domain.error.WikiPageNotFound;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class WikiReaderImplHtmlTest {

  private final WikiReader reader = new WikiReaderImplHtml();

  @Test
  void testValidRead() {
    WikiPage page = reader.read("./src/main/resources/wikiscrapper/site2.html");
    assertEquals("Site 2", page.getTitle());
    assertEquals("Content 2", page.getContent());
    assertEquals("http://wikiscrapper.test/site2", page.getSelfLink());
    assertEquals(List.of("http://wikiscrapper.test/site4", "http://wikiscrapper.test/site5"), page.getLinks());
  }

  @Test
  void testInvalidRead() {
    assertThrows(WikiPageNotFound.class, () -> reader.read("path_to_nonexistent_html_file.html"));
  }
}