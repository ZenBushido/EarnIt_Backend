package com.mobiledi.earnitapi.web;

import java.sql.Timestamp;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mobiledi.earnitapi.domain.Parent;
import com.mobiledi.earnitapi.repository.ParentRepository;

@RestController
public class ParentController {

	@Autowired
	ParentRepository parentRepo;

	@RequestMapping(value = "/parent/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> findByChildId(@PathVariable Long id) throws JSONException {

		return new ResponseEntity<Parent>(parentRepo.findParentById(id), HttpStatus.OK);

	}

	@RequestMapping(value = "/parent", method = RequestMethod.PUT)
	public ResponseEntity<?> update(@RequestBody Parent parent) throws JSONException {
		parent.setUpdateDate(new Timestamp(new DateTime().getMillis()));

		Parent parentObject = parentRepo.save(parent);

		if (parentObject != null)
			return new ResponseEntity<Parent>(parent, HttpStatus.ACCEPTED);
		else
			return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);

	}
}
