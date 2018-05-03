package com.mobiledi.earnitapi.repository.custom;

import com.mobiledi.earnitapi.domain.Children;
import com.mobiledi.earnitapi.domain.Parent;

public interface AccountRepositoryCustom {

	Parent findParentbyemailandpassword(String email, String password);

	Children findChildbyemailandpassword(String email, String password);

	Parent findParentByEmail(String email);

	Children findChildByEmail(String email);

}
