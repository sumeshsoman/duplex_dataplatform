package com.duplex.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CountDownLatch;

@Service
public class FileUploaderService implements FileUploaderInterface {

  @Autowired private CSVCruncherInterface csvCruncher;

  private File uploadDirectory = new File(System.getProperty("java.io.tmpdir"));

  private CountDownLatch latch = new CountDownLatch(1);

  public CountDownLatch getLatch() {
    return latch;
  }

  @Override
  public void uploadFile(MultipartFile file) throws IOException {
    byte[] bytes = file.getBytes();
    Path path = Paths.get(uploadDirectory + file.getOriginalFilename());
    Files.write(path, bytes);
    csvCruncher.processCSV(path);
    latch.countDown();
  }
}
