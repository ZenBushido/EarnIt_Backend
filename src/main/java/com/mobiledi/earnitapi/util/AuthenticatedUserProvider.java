package com.mobiledi.earnitapi.util;

import com.mobiledi.earnitapi.domain.Children;
import com.mobiledi.earnitapi.domain.Parent;
import com.mobiledi.earnitapi.repository.AccountRepository;
import com.mobiledi.earnitapi.repository.ChildrenRepository;
import com.mobiledi.earnitapi.repository.custom.ChildrenRepositoryCustom;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

@Component
public class AuthenticatedUserProvider {

  @Autowired
  private AccountRepository accountRepository;

  public Children getLoggedInChild() throws IllegalAccessException {
    User user = (User) SecurityContextHolder.getContext().getAuthentication()
        .getPrincipal();
    Children children = accountRepository.findChildByEmail(user.getUsername());
    if (Objects.isNull(children)) {
      throw new IllegalAccessException("No logged in child found.");
    }
    return children;
  }

  public Parent getLoggedInParent() throws IllegalAccessException {
    User user = (User) SecurityContextHolder.getContext().getAuthentication()
        .getPrincipal();
    Parent parent = accountRepository.findParentByEmail(user.getUsername());
    if (Objects.isNull(parent)) {
      throw new IllegalAccessException("No logged in parent found.");
    }
    return parent;
  }

  public String getLoggedInUserEmail(){
    User user = (User) SecurityContextHolder.getContext().getAuthentication()
        .getPrincipal();
    return user.getUsername();
  }

}
