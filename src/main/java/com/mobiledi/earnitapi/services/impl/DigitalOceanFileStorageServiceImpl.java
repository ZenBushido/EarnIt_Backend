package com.mobiledi.earnitapi.services.impl;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.S3Object;
import com.mobiledi.earnitapi.services.FileStorageService;
import java.io.File;
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

  @Value("${storage.endpoint.digital.ocean}")
  private String endpointForDigitalOcean;

  @Override
  public String storeFile(String fullPath, File file) {
    amazonS3ClientForDigitalOcean.putObject(bucketForDigitalOcean, fullPath, file);
    return fullPath;
  }

  @Override
  public InputStream getFile(String filePath) {
    S3Object s3Object = amazonS3ClientForDigitalOcean.getObject(bucketForDigitalOcean, filePath);
    return s3Object.getObjectContent();
  }
}
