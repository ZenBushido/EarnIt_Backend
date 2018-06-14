package com.mobiledi.earnitapi.services.impl;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.mobiledi.earnitapi.services.FileStorageService;
import java.io.BufferedInputStream;
import java.io.InputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DigitalOceanFileStorageServiceImpl implements FileStorageService {

  @Autowired
  private AmazonS3Client amazonS3ClientForDigitalOcean;

  @Value("${storage.bucket.digital.ocean}")
  private String bucketForDigitalOcean;

  @Override
  public void storeFile(String fullPath, InputStream file) {
    amazonS3ClientForDigitalOcean.putObject(bucketForDigitalOcean, fullPath, file);
  }

  @Override
  public BufferedInputStream getFile(String fullPath) {
    return null;
  }
}
