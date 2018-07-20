package com.mobiledi.earnitapi.services.impl;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.mobiledi.earnitapi.services.FileStorageService;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import lombok.SneakyThrows;
import org.apache.tomcat.util.http.fileupload.IOUtils;
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
  @SneakyThrows
  public InputStream getFile(String filePath) {
    S3Object s3Object = null;
    try {
      s3Object = amazonS3ClientForDigitalOcean.getObject(bucketForDigitalOcean, filePath);
      try (S3ObjectInputStream stream = s3Object.getObjectContent()) {
        ByteArrayOutputStream temp = new ByteArrayOutputStream();
        IOUtils.copy(stream, temp);
        return new ByteArrayInputStream(temp.toByteArray());
      }
    } finally {
      if (s3Object != null) {
        s3Object.close();
      }
    }
  }
}
