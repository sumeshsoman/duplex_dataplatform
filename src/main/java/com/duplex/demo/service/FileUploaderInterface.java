package com.duplex.demo.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileUploaderInterface {

  void uploadFile(MultipartFile file) throws IOException;
}
