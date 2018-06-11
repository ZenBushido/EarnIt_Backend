package com.mobiledi.earnitapi.web;

import com.mobiledi.earnitapi.domain.Parent;
import com.mobiledi.earnitapi.repository.ParentRepository;
import com.mobiledi.earnitapi.repository.custom.ParentRepositoryCustom;
import java.sql.Timestamp;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ParentController {

	@Autowired
	ParentRepository parentRepo;

	@Autowired
	ParentRepositoryCustom parentRepositoryCustom;

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


	@GetMapping(value = "/parents/{parentId}/profile/image")
	public String getProfilePicture(@PathVariable Integer parentId){

		return "";
	}

	@PostMapping(value = "/parents/{parentId}/profile/image")
  public String saveProfilePicture(@PathVariable Integer parentId){

		return "";
  }

	private void doesParentExist(Integer id) {
		Optional<Parent> parent = parentRepo.findById(id);
		if (!parent.isPresent()) {
			throw new ValidationException("Parent not found with id : " + id, 400);
		}
	}


}
