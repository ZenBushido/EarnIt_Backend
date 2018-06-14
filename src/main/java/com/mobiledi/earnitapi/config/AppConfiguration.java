package com.mobiledi.earnitapi.config;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AppConfiguration {

  @Value("${storage.accesskey.digital.ocean}")
  private String accessKeyForDigitalOcean;

  @Value("${storage.secretkey.digital.ocean}")
  private String secretKeyForDigitalOcean;

  @Value("${storage.endpoint.digital.ocean}")
  private String endpointForDigitalOcean;
/*
  @Value("storage.bucket.digital.ocean")
  private String bucketForDigitalOcean;
*/
  @Bean
  public DaoAuthenticationProvider authProvider(UserDetailsService userDetailsService) {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService);
    authProvider.setPasswordEncoder(passwordEncoder());
    return authProvider;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  public BasicAWSCredentials basicAWSCredentialsForDigitalOcean() {
    return new BasicAWSCredentials(accessKeyForDigitalOcean,
        secretKeyForDigitalOcean);
  }

  @Bean
  public AmazonS3Client amazonS3ClientForDigitalOcean() {
    AmazonS3Client amazonS3Client = new AmazonS3Client(basicAWSCredentialsForDigitalOcean());
    amazonS3Client.setEndpoint(endpointForDigitalOcean);
    //amazonS3Client.getBucketAccelerateConfiguration(bucketForDigitalOcean);
    return amazonS3Client;
  }

}
