package com.duplex.demo.util;

import com.duplex.demo.dto.CSVDataDTO;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class HelperUtilTest {

  private List<CSVDataDTO> csvDataDTOList;

  @Before
  public void setUp() {
    CSVDataDTO dto1 = new CSVDataDTO("Joe", 12, "5.1");
    CSVDataDTO dto2 = new CSVDataDTO("Alice", 32, "5.7");
    CSVDataDTO dto3 = new CSVDataDTO("Madden", 55, "5.9");
    CSVDataDTO dto4 = new CSVDataDTO("Alfred", 21, "6.2");
    csvDataDTOList = Arrays.asList(dto1, dto2, dto3, dto4);
  }

  @Test
  public void getAverageAge() {
    assertEquals(30, HelperUtil.getAverageAge(csvDataDTOList));
  }

  @Test
  public void getAdultData() {
    List<CSVDataDTO> adultData = HelperUtil.getAdultData(csvDataDTOList);
    assertEquals(3, adultData.size());
  }

  @Test
  public void getKidsData() {
    int averageAge = HelperUtil.getAverageAge(csvDataDTOList);
    List<CSVDataDTO> kidsData = HelperUtil.getKidsData(csvDataDTOList, averageAge);
    assertEquals(1, kidsData.size());
    assertEquals("5.1", kidsData.get(0).getHeight());
    assertNotEquals("Joe", kidsData.get(0).getName());
    assertEquals(averageAge, kidsData.get(0).getAge());
  }

  @Test
  public void generateJson() {
    String json = HelperUtil.generateJson(csvDataDTOList);
    assertEquals(
        "[{\"name\":\"Joe\",\"age\":12,\"height\":\"5.1\"},{\"name\":\"Alice\",\"age\":32,\"height\":\"5.7\"},{\"name\":\"Madden\",\"age\":55,\"height\":\"5.9\"},{\"name\":\"Alfred\",\"age\":21,\"height\":\"6.2\"}]",
        json);
  }
}
