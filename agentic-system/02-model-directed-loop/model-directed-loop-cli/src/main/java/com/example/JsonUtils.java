package com.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonUtils {

  private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);

  private final ObjectMapper objectMapper;
  private final ObjectWriter prettyWriter;

  public JsonUtils(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
    this.prettyWriter = objectMapper.writerWithDefaultPrettyPrinter();
  }

  public String toPrettyJson(Object obj) {
    try {
      return prettyWriter.writeValueAsString(obj);
    } catch (Exception e) {
      logger.error("Failed to format JSON", e);
      return "[ERROR] Failed to format JSON: " + e.getMessage();
    }
  }
}
