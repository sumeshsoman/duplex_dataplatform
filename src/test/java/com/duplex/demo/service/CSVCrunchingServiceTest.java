package com.duplex.demo.service;

import com.duplex.demo.model.CSVHistory;
import com.duplex.demo.repo.CSVRepository;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.rule.KafkaEmbedded;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@Transactional
public class CSVCrunchingServiceTest {

  @ClassRule
  public static KafkaEmbedded embeddedKafka = new KafkaEmbedded(1, true, "DataForPresenter");
  @Value("${kafka.topic.name}")
  private static String topicName;
  private ClassLoader classLoader;
  @Autowired private FileUploaderService fileUploader;
  @Autowired private CSVRepository repository;

  @Before
  public void setUp() {
    classLoader = getClass().getClassLoader();
  }

  @Test
  public void testUploadAndCSVStats() throws IOException {
    File csvFile = new File(classLoader.getResource("data.csv").getFile());
    FileInputStream input = new FileInputStream(csvFile);
    MultipartFile multipartFile =
        new MockMultipartFile("file", csvFile.getName(), "text/plain", IOUtils.toByteArray(input));

    fileUploader.uploadFile(multipartFile);
    Assert.assertEquals(0, fileUploader.getLatch().getCount());

    Assert.assertEquals(1, repository.count());
    Iterator<CSVHistory> iterator = repository.findAll().iterator();
    CSVHistory history = iterator.next();
    Assert.assertEquals(1, history.getNumFiles());
    Assert.assertEquals(6, history.getNumLines());
  }
}
