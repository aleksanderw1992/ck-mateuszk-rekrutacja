package com.lastminute.recruitment;

import com.lastminute.recruitment.domain.WikiScrapper;
import com.lastminute.recruitment.domain.error.WikiPageNotFound;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class WikiScrapperResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WikiScrapper wikiScrapper;

    @Test
    public void testScrapWikipedia_200() throws Exception {
        String link = "https://example.com";

        mockMvc.perform(post("/wiki/scrap")
                .contentType(MediaType.TEXT_PLAIN)
                .content(link))
                .andExpect(status().isOk());
    }

    @Test
    public void testScrapWikipedia_404() throws Exception {
        String link = "https://example.com";
        doThrow(WikiPageNotFound.class).when(wikiScrapper).read(link);

        mockMvc.perform(post("/wiki/scrap")
                .contentType(MediaType.TEXT_PLAIN)
                .content(link))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testScrapWikipedia_500() throws Exception {
        String link = "https://example.com";
        doThrow(RuntimeException.class).when(wikiScrapper).read(link);

        mockMvc.perform(post("/wiki/scrap")
                .contentType(MediaType.TEXT_PLAIN)
                .content(link))
                .andExpect(status().isInternalServerError());
    }
}
