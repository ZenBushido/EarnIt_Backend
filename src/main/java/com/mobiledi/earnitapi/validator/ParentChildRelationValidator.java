package com.mobiledi.earnitapi.validator;

import com.mobiledi.earnitapi.repository.ChildrenRepository;
import com.mobiledi.earnitapi.repository.ParentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ParentChildRelationValidator {

  @Autowired
  private ParentRepository parentRepository;

  @Autowired
  private ChildrenRepository childrenRepository;

  public boolean isParentOfGivenChild(Integer parentId, Integer childId) {
    return parentRepository.findParentById(parentId).getAccount().getChildrens().stream()
        .anyMatch(children -> childId == children.getId());
  }

}
