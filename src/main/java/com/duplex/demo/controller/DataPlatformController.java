package com.duplex.demo.controller;

import com.duplex.demo.model.CSVHistory;
import com.duplex.demo.repo.CSVRepository;
import com.duplex.demo.service.FileUploaderInterface;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class DataPlatformController {

  public static final String csvStatsURI = "/duplex/v1/csvstats";
  public static final String csvURI = "/duplex/v1/csv";
  @Autowired private CSVRepository repository;
  @Autowired private FileUploaderInterface fileUploader;

  @RequestMapping(
    value = csvStatsURI,
    produces = MediaType.APPLICATION_JSON_VALUE,
    method = RequestMethod.GET
  )
  public ResponseEntity<CSVHistory> getCSVStats() {
    HttpStatus status = HttpStatus.OK;
    CSVHistory csvHistory = repository.findAll().iterator().next();
    return new ResponseEntity<>(csvHistory, status);
  }

  @RequestMapping(
    value = csvURI,
    consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
    method = RequestMethod.POST
  )
  public ResponseEntity<?> uploadCSV(@RequestParam("file") MultipartFile uploadfile) {

    if (uploadfile.isEmpty()) {
      System.out.println("Upload file is empty");
      return new ResponseEntity("please select a file!", HttpStatus.OK);
    }

    String fileExtension = FilenameUtils.getExtension(uploadfile.getOriginalFilename());
    if (!fileExtension.equals("csv")) {
      System.out.println("File name Extension is wrong " + fileExtension);
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    try {
      fileUploader.uploadFile(uploadfile);
    } catch (IOException e) {
      System.out.println("IO Exception !!!!");
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    return new ResponseEntity(
        "Successfully uploaded - " + uploadfile.getOriginalFilename(),
        new HttpHeaders(),
        HttpStatus.OK);
  }
}
