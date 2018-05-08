package com.duplex.demo.util;

import com.duplex.demo.dto.CSVDataDTO;
import com.google.gson.Gson;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.List;
import java.util.stream.Collectors;

public class HelperUtil {

  public static int getAverageAge(List<CSVDataDTO> csvDataDTOList) {
    Double averageAge = csvDataDTOList
            .stream()
            .collect(Collectors.averagingInt(p -> p.getAge()));
    return averageAge.intValue();
  }

  public static List<CSVDataDTO> getAdultData(List<CSVDataDTO> csvDataDTOList) {
    List<CSVDataDTO> above18Entries =
        csvDataDTOList
            .parallelStream()
            .filter(dto -> dto.getAge() >= 18)
            .collect(Collectors.toList());

    return above18Entries;
  }

  public static List<CSVDataDTO> getKidsData(List<CSVDataDTO> csvDataDTOList, int averageAge) {
    List<CSVDataDTO> below18Entries =
        csvDataDTOList
            .parallelStream()
            .filter(dto -> dto.getAge() < 18)
            .map(dto -> modifyData(dto, averageAge))
            .collect(Collectors.toList());
    return below18Entries;
  }

  public static String generateJson(List<CSVDataDTO> data) {
    String json = new Gson().toJson(data);
    return json;
  }

  private static CSVDataDTO modifyData(CSVDataDTO dto, int averageAge) {
    dto.setAge(averageAge);
    dto.setName(generateAlias());
    return dto;
  }

  private static String generateAlias() {
    int length = 10;
    boolean useLetters = true;
    boolean useNumbers = false;
    String generatedString = RandomStringUtils.random(length, useLetters, useNumbers);
    return generatedString;
  }
}
