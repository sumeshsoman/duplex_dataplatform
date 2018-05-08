package com.duplex.demo.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

public class IntegrationTestUtil {

  public static byte[] convertObjectToJsonBytes(Object object) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
    byte[] butes = mapper.writeValueAsBytes(object);
    return butes;
  }

  public static void setTimeout(RestTemplate restTemplate, int timeout) {
    restTemplate.setRequestFactory(new SimpleClientHttpRequestFactory());
    SimpleClientHttpRequestFactory rf =
        (SimpleClientHttpRequestFactory) restTemplate.getRequestFactory();
    rf.setReadTimeout(timeout);
    rf.setConnectTimeout(timeout);
  }
}
