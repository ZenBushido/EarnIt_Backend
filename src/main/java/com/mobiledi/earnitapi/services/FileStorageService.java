package com.mobiledi.earnitapi.services;

import java.io.BufferedInputStream;
import java.io.InputStream;

public interface FileStorageService {

  void storeFile(String fullPath, InputStream file);

  BufferedInputStream getFile(String fullPath);

}
