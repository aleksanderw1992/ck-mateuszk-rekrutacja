package com.lastminute.recruitment.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lastminute.recruitment.domain.WikiPage;
import com.lastminute.recruitment.domain.WikiReader;
import com.lastminute.recruitment.domain.error.WikiPageNotFound;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;

public class WikiReaderImplJson implements WikiReader {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public WikiPage read(String link) {
    try {
      byte[] jsonData = Files.readAllBytes(Paths.get(link));
      return objectMapper.readValue(jsonData, WikiPage.class);
    } catch (NoSuchFileException e) {
      throw new WikiPageNotFound("Wiki page not found at: " + link, e);

    } catch (IOException e) {
      throw new RuntimeException("Wiki page read failed: " + link, e);
    }
  }
}