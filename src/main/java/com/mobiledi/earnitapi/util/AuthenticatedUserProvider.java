package com.mobiledi.earnitapi.util;

import com.mobiledi.earnitapi.domain.Children;
import com.mobiledi.earnitapi.domain.Parent;
import com.mobiledi.earnitapi.repository.AccountRepository;
import com.mobiledi.earnitapi.repository.ChildrenRepository;
import com.mobiledi.earnitapi.repository.custom.ChildrenRepositoryCustom;
import java.util.Objects;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

@Component
public class AuthenticatedUserProvider {

  @Autowired
  private AccountRepository accountRepository;

  public Children getLoggedInChild() throws IllegalAccessException {
    Children children = getChild();
    if (Objects.isNull(children)) {
      throw new IllegalAccessException("No logged in child found.");
    }
    return children;
  }

  public Parent getLoggedInParent() throws IllegalAccessException {
    Parent parent = getParent();
    if (Objects.isNull(parent)) {
      throw new IllegalAccessException("No logged in parent found.");
    }
    return parent;
  }

  public String getLoggedInUserEmail() {
    User user = (User) SecurityContextHolder.getContext().getAuthentication()
        .getPrincipal();
    return user.getUsername();
  }

  @SneakyThrows
  public void raiseErrorIfParentIdIsDifferentThanLoggedInUser(Integer parentId) {
    Parent parent = getParent();
    raiseIdsAreDifferentError(parentId, parent.getId());

  }

  @SneakyThrows
  public void raiseErrorIfChildIdIsDifferentThanLoggedInUser(Integer childId) {
    Children children = getChild();
    raiseIdsAreDifferentError(childId, children.getId());
  }

  @SneakyThrows
  private void raiseIdsAreDifferentError(Integer id1, Integer id2) {
    if (!id1.equals(id2)) {
      throw new IllegalAccessException(
          "LoggedIn user id and provided id to access resource are different and now allowed");
    }
  }

  private Parent getParent() {
    User user = (User) SecurityContextHolder.getContext().getAuthentication()
        .getPrincipal();
    return accountRepository.findParentByEmail(user.getUsername());
  }

  private Children getChild() {
    User user = (User) SecurityContextHolder.getContext().getAuthentication()
        .getPrincipal();
    return accountRepository.findChildByEmail(user.getUsername());
  }

}
