package com.mobiledi.earnitapi.repository.custom;

import com.mobiledi.earnitapi.domain.Children;

public interface ChildrenRepositoryCustom {
	Children persistChild(Children child);

	Children findChild(int id);

}
