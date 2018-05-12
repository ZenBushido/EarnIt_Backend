package com.mobiledi.earnitapi.repository.custom;

import com.mobiledi.earnitapi.domain.Parent;

public interface ParentRepositoryCustom {
	Parent persistParent(Parent parent);
	Parent updateParent(Parent parent);

}
