package com.duplex.demo.service;

import com.duplex.demo.dto.CSVDataDTO;
import com.duplex.demo.kafka.Sender;
import com.duplex.demo.model.CSVHistory;
import com.duplex.demo.repo.CSVRepository;
import com.duplex.demo.util.HelperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class CSVCrunchingService implements CSVCruncherInterface {

  @Autowired private CSVRepository repository;

  @Autowired private Sender kafkaSender;

  @Value("${kafka.topic.name}")
  private String topicName;

  @Override
  public void processCSV(Path csvFileFilePath) {
    String line = "";
    String csvSplit = ",";

    List<CSVDataDTO> csvDataDTOList = new ArrayList<>();
    try (BufferedReader bufferedReader =
        new BufferedReader(new FileReader(csvFileFilePath.toFile()))) {
      while ((line = bufferedReader.readLine()) != null) {
        String[] data = line.split(csvSplit);
        if (data[0].equals("name")) { // header
          continue;
        }
        csvDataDTOList.add(new CSVDataDTO(data[0], Integer.valueOf(data[1]), data[2]));
      }

    } catch (IOException e) {
      e.printStackTrace();
    }

    CSVHistory history = null;

    if (csvDataDTOList.size() > 0) {
      Iterator<CSVHistory> csvRepositoryIterator = repository.findAll().iterator();
      history = csvRepositoryIterator.hasNext() ? csvRepositoryIterator.next() : new CSVHistory();

      history.addNumFiles(1);
      history.addNumLines(csvDataDTOList.size());
      repository.save(history);
    }

    computeAndPush(csvDataDTOList);
  }

  public void computeAndPush(List<CSVDataDTO> csvDataDTOList) {

    int averageAge = HelperUtil.getAverageAge(csvDataDTOList);
    List<CSVDataDTO> adultData = HelperUtil.getAdultData(csvDataDTOList);
    List<CSVDataDTO> kidsData = HelperUtil.getKidsData(csvDataDTOList, averageAge);

    // push this to Kafka
    String json = HelperUtil.generateJson(adultData);
    kafkaSender.send(topicName, json);

    json = HelperUtil.generateJson(kidsData);
    kafkaSender.send(topicName, json);
  }
}
