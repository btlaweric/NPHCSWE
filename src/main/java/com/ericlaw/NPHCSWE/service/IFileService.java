package com.ericlaw.NPHCSWE.service;

import org.springframework.web.multipart.MultipartFile;

public interface IFileService {
  public void uploadUsersCSVFile(MultipartFile file);
}
