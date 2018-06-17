package com.mobiledi.earnitapi.web;

import com.mobiledi.earnitapi.constants.StringConstant;
import com.mobiledi.earnitapi.domain.Parent;
import com.mobiledi.earnitapi.repository.ParentRepository;
import com.mobiledi.earnitapi.repository.custom.ParentRepositoryCustom;
import com.mobiledi.earnitapi.services.FileStorageService;
import com.mobiledi.earnitapi.util.AuthenticatedUserProvider;
import com.mobiledi.earnitapi.util.ImageUtil;
import java.io.File;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.Optional;
import javax.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
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
public class ParentController {

  @Autowired
  ParentRepository parentRepo;

  @Autowired
  ParentRepositoryCustom parentRepositoryCustom;

  @Autowired
  private FileStorageService fileStorageService;

  @Autowired
  private AuthenticatedUserProvider authenticatedUserProvider;

  @Autowired
  private ImageUtil imageUtil;

  @RequestMapping(value = "/parent/{id}", method = RequestMethod.GET)
  public ResponseEntity<?> findByChildId(@PathVariable Integer id) throws JSONException {

    return new ResponseEntity<Parent>(parentRepo.findParentById(id), HttpStatus.OK);

  }

  @RequestMapping(value = "/parent", method = RequestMethod.PUT)
  public ResponseEntity<?> update(@RequestBody Parent parent) throws JSONException {
    doesParentExist(parent.getId());
    parent.setUpdateDate(new Timestamp(new DateTime().getMillis()));
    parent = parentRepositoryCustom.updateParent(parent);
    return new ResponseEntity<Parent>(parent, HttpStatus.ACCEPTED);
  }

  @GetMapping(value = "/parents/{parentId}/profile/images/{imageName}")
  @SneakyThrows
  public void getProfilePicture(@PathVariable Integer parentId, @PathVariable String imageName, HttpServletResponse httpServletResponse) {
    Parent parent = authenticatedUserProvider.getLoggedInParent();
    authenticatedUserProvider.raiseErrorIfParentIdIsDifferentThanLoggedInUser(parentId);
    InputStream inputStream = fileStorageService.getFile(parent.getAvatar());
    httpServletResponse.setContentType(StringConstant.CONTENT_TYPE_OCTET_STREAM);
    IOUtils.copyLarge(inputStream, httpServletResponse.getOutputStream());
  }

  @PostMapping(value = "/parents/profile/images")
  @SneakyThrows
  public String saveProfilePicture(@RequestParam("file") MultipartFile file) {
    Parent parent = authenticatedUserProvider.getLoggedInParent();
    String profileImageUrl = imageUtil.createParentProfileUrl(parent, file.getOriginalFilename());
    File temporaryProfilePicture = imageUtil.getTemporaryFileFromMultipartFile(file);
    String urlPath = fileStorageService.storeFile(profileImageUrl, temporaryProfilePicture);
    parent.setAvatar(urlPath);
    parentRepo.save(parent);
    temporaryProfilePicture.delete();
    return urlPath;
  }

  private void doesParentExist(Integer id) {
    Optional<Parent> parent = parentRepo.findById(id);
    if (!parent.isPresent()) {
      throw new ValidationException("Parent not found with id : " + id, 400);
    }
  }

}
