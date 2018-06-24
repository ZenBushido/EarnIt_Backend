package com.mobiledi.earnitapi.util;

import com.mobiledi.earnitapi.domain.Children;
import com.mobiledi.earnitapi.domain.Parent;
import com.mobiledi.earnitapi.domain.Task;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.time.Instant;
import javax.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class ImageUtil {

  private static final String PROFILE_IMAGE_SUB_PATH_IN_PROFILE_URL = "/profile/images/";
  private static final String TASK_IMAGES = "/task/";

  public String createParentProfileUrl(Parent parent, String fileName) {
    return "parents/" + parent.getId() + PROFILE_IMAGE_SUB_PATH_IN_PROFILE_URL + fileName;
  }

  @SneakyThrows
  public File getTemporaryFileFromMultipartFile(MultipartFile file) {
    String fileName = file.getOriginalFilename();
    File tempFile = File
        .createTempFile(
            Instant.now().getNano() + " _ " + fileName.substring(0, fileName.indexOf('.')),
            fileName.substring(fileName.indexOf('.')));
    FileOutputStream fileOutputStream = new FileOutputStream(tempFile);
    IOUtils.copy(file.getInputStream(), fileOutputStream);
    return tempFile;
  }

  public String createTaskImageUrl(Task task, MultipartFile multipartFile) {
    String fileName = multipartFile.getOriginalFilename();
    return createTaskImageUrl(task, fileName.substring(0, fileName.indexOf('.')));
  }

  public String createTaskImageUrl(Task task, String fileName) {
    return "tasks/" + task.getId() + "/images/" + fileName;
  }

  public String createChildProfileUrl(Children children, String fileName) {
    return "childrens/" + children.getId() + PROFILE_IMAGE_SUB_PATH_IN_PROFILE_URL + fileName;
  }

}