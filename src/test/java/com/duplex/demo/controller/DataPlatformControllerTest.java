package com.duplex.demo.controller;

import com.duplex.demo.kafka.Sender;
import com.duplex.demo.model.CSVHistory;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.rule.KafkaEmbedded;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@Transactional
public class DataPlatformControllerTest {

  private static String BOOT_TOPIC = "DataForPresenter";
  @ClassRule public static KafkaEmbedded embeddedKafka = new KafkaEmbedded(1, true, BOOT_TOPIC);
  @Resource private WebApplicationContext webApplicationContext;
  private MockMvc mockMvc;
  private RestTemplate restTemplate;
  private ObjectMapper objectMapper = new ObjectMapper();
  private ClassLoader classLoader;
  @Autowired private Sender sender;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    mockMvc =
        MockMvcBuilders.webAppContextSetup(webApplicationContext).dispatchOptions(true).build();
    restTemplate = new RestTemplate();
    IntegrationTestUtil.setTimeout(restTemplate, 5000);
    objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
    classLoader = getClass().getClassLoader();
  }

  @After
  public void tearDown() throws Exception {
    restTemplate = null;
    this.mockMvc = null;
  }

  @Test
  public void testUploadCSVAndGetStats() throws Exception {
    File csvFile = new File(classLoader.getResource("data.csv").getFile());
    FileInputStream input = new FileInputStream(csvFile);
    MockMultipartFile multipartFile =
        new MockMultipartFile("file", csvFile.getName(), "text/plain", IOUtils.toByteArray(input));

    mockMvc
        .perform(
            MockMvcRequestBuilders.multipart(DataPlatformController.csvURI).file(multipartFile))
        .andExpect(status().isOk());

    String query = DataPlatformController.csvStatsURI;
    MvcResult result = mockMvc.perform(get(query)).andExpect(status().isOk()).andReturn();

    System.out.println(result.getResponse().getContentAsString());
    CSVHistory history =
        objectMapper.readValue(result.getResponse().getContentAsByteArray(), CSVHistory.class);
    Assert.assertNotNull(history);
    Assert.assertEquals(1, history.getNumFiles());
    Assert.assertEquals(6, history.getNumLines());
  }
}
