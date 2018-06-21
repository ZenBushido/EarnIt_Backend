package com.mobiledi.earnitapi.web;

import static com.mobiledi.earnitapi.util.MessageConstants.CHILDREN_DELETED_FAILED;
import static com.mobiledi.earnitapi.util.MessageConstants.CHILDREN_DELETED_FAILED_CODE;

import com.mobiledi.earnitapi.constants.StringConstant;
import com.mobiledi.earnitapi.domain.Children;
import com.mobiledi.earnitapi.domain.Task;
import com.mobiledi.earnitapi.domain.custom.ApiError;
import com.mobiledi.earnitapi.domain.custom.Response;
import com.mobiledi.earnitapi.repository.ChildrenRepository;
import com.mobiledi.earnitapi.repository.custom.ChildrenRepositoryCustom;
import com.mobiledi.earnitapi.services.FileStorageService;
import com.mobiledi.earnitapi.util.AppConstants;
import com.mobiledi.earnitapi.util.AuthenticatedUserProvider;
import com.mobiledi.earnitapi.util.ImageUtil;
import com.mobiledi.earnitapi.util.MessageConstants;
import com.mobiledi.earnitapi.util.NotificationConstants.NotificationCategory;
import com.mobiledi.earnitapi.util.PushNotifier;
import java.io.File;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.joda.time.DateTime;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
public class ChildrenController {

  @Autowired
  private ChildrenRepository childrenRepo;

  @Autowired
  private ChildrenRepositoryCustom childrenRepositoryCustom;

  @Autowired
  private AuthenticatedUserProvider authenticatedUserProvider;

  @Autowired
  private FileStorageService fileStorageService;

  @Autowired
  private ImageUtil imageUtil;

  @RequestMapping("/childrens/{id}")
  public List<Children> findById(@PathVariable int id) {

    List<Children> childrenList = childrenRepo.findChildrenByAccountIdAndIsDeletedOrderByFirstNameAsc(id, false);
    childrenList.forEach(child -> {
      List<Task> toRemove = child.getTasks().stream()
          .filter(task -> task.getStatus().equals(AppConstants.TASK_CLOSED) && !task.isDeleted())
          .collect(Collectors.toList());
      child.getTasks().removeAll(toRemove);
      child.setUserType(AppConstants.USER_CHILD);
    });
    return childrenList;
  }

  @RequestMapping(value = "/childrens/{id}", method = RequestMethod.DELETE)
  public ResponseEntity<?> deleteChild(@PathVariable Integer id) {
    Optional<Children> childrenOptional = childrenRepo.findById(id);
    if (childrenOptional.isPresent()) {
      Children children = childrenOptional.get();
      children.setDeleted(true);
      children.setUpdateDate(new Timestamp(new DateTime().getMillis()));

      log.debug("Deleting child account: " + children.getId());
      childrenRepo.save(children);

      return new ResponseEntity<>(new Response(MessageConstants.CHILDREN_DELETED), HttpStatus.OK);
    }

    ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, CHILDREN_DELETED_FAILED_CODE,
        CHILDREN_DELETED_FAILED);
    return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
  }

  @RequestMapping(value = "/children", method = RequestMethod.PUT)
  public ResponseEntity<?> update(@RequestBody Children child) throws JSONException {

    doesChildExist(child.getId());
    child = childrenRepositoryCustom.updateChild(child);
    if (StringUtils.isNotBlank(child.getFcmToken()) && StringUtils.isNotBlank(child.getMessage())) {
      PushNotifier
          .sendPushNotification(0, child.getFcmToken(), NotificationCategory.MESSAGE_TO_CHILD,
              child.getMessage());
    }

    return new ResponseEntity<Children>(child, HttpStatus.ACCEPTED);

  }

  @GetMapping(value = "/childrens/{childId}/profile/images/{imageName}")
  @SneakyThrows
  public void getProfilePicture(@PathVariable Integer childId, @PathVariable String imageName, HttpServletResponse httpServletResponse) {
    Children child = authenticatedUserProvider.getLoggedInChild();
    authenticatedUserProvider.raiseErrorIfChildIdIsDifferentThanLoggedInUser(childId);
    InputStream inputStream = fileStorageService.getFile(child.getAvatar());
    httpServletResponse.setContentType(StringConstant.CONTENT_TYPE_OCTET_STREAM);
    IOUtils.copyLarge(inputStream, httpServletResponse.getOutputStream());
  }

  @PostMapping(value = "/childrens/profile/images")
  @SneakyThrows
  public String saveProfilePicture(@RequestParam("file") MultipartFile file) {
    Children child = authenticatedUserProvider.getLoggedInChild();
    String profileImageUrl = imageUtil.createChildProfileUrl(child, file.getOriginalFilename());
    File temporaryProfilePicture = imageUtil.getTemporaryFileFromMultipartFile(file);
    String urlPath = fileStorageService.storeFile(profileImageUrl, temporaryProfilePicture);
    child.setAvatar(urlPath);
    childrenRepo.save(child);
    temporaryProfilePicture.delete();
    return urlPath;
  }
  private void doesChildExist(Integer id) {
    Optional<Children> children = childrenRepo.findById(id);
    if (!children.isPresent()) {
      throw new ValidationException("Children not found with id : " + id, 400);
    }
  }

}
