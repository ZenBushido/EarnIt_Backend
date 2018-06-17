package com.mobiledi.earnitapi.services;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;

public interface FileStorageService {

  String storeFile(String fullPath, File tempFile);

  InputStream getFile(String fullPath);

}
