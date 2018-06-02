package com.mobiledi.earnitapi.services.impl;

import com.mobiledi.earnitapi.domain.Children;
import com.mobiledi.earnitapi.domain.Parent;
import com.mobiledi.earnitapi.repository.AccountRepository;
import com.mobiledi.earnitapi.util.AppConstants;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

  @Autowired
  AccountRepository accountRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Parent account = accountRepository.findParentByEmail(username);
    if (account != null) {
      log.info("Found User with email: " + username);

      return new User(account.getEmail(), account.getPassword(), true, true, true, true,
          AuthorityUtils.createAuthorityList(AppConstants.USER_PARENT));
    } else {
      Children child = accountRepository.findChildByEmail(username);
      if (child != null)
        return new User(child.getEmail(), child.getPassword(), true, true, true, true,
            AuthorityUtils.createAuthorityList(AppConstants.USER_CHILD));
      else
        throw new UsernameNotFoundException("could not find the user '" + username + "'");
    }
  }
}
