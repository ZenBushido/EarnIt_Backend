package com.mobiledi.earnitapi.web;

import static com.mobiledi.earnitapi.util.MessageConstants.MAIL_SENT_FAILED;
import static com.mobiledi.earnitapi.util.MessageConstants.MAIL_SENT_FAILED_CODE;
import static com.mobiledi.earnitapi.util.MessageConstants.MAIL_SENT_SUCCESS;
import static com.mobiledi.earnitapi.util.MessageConstants.PASSWORD_REMINDER_MAIL_BODY;
import static com.mobiledi.earnitapi.util.MessageConstants.PASSWORD_REMINDER_MAIL_SUBJECT;
import static com.mobiledi.earnitapi.util.MessageConstants.USER_DOES_NOT_EXISTS;
import static com.mobiledi.earnitapi.util.MessageConstants.USER_DOES_NOT_EXISTS_CODE;
import static com.mobiledi.earnitapi.util.MessageConstants.USER_EXISTS;
import static com.mobiledi.earnitapi.util.MessageConstants.USER_EXISTS_CODE;

import com.mobiledi.earnitapi.domain.Account;
import com.mobiledi.earnitapi.domain.Children;
import com.mobiledi.earnitapi.domain.Parent;
import com.mobiledi.earnitapi.domain.custom.ApiError;
import com.mobiledi.earnitapi.domain.custom.LoginDomain;
import com.mobiledi.earnitapi.domain.custom.Response;
import com.mobiledi.earnitapi.repository.AccountRepository;
import com.mobiledi.earnitapi.repository.ChildrenRepository;
import com.mobiledi.earnitapi.repository.ParentRepository;
import com.mobiledi.earnitapi.repository.custom.ChildrenRepositoryCustom;
import com.mobiledi.earnitapi.repository.custom.ParentRepositoryCustom;
import com.mobiledi.earnitapi.util.AppConstants;
import com.mobiledi.earnitapi.util.AuthenticatedUserProvider;
import com.mobiledi.earnitapi.util.MailUtility;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class AccountController {

	@Autowired
	AccountRepository accountRepo;

	@Autowired
	ParentRepositoryCustom parentRepo;

	@Autowired
	ChildrenRepositoryCustom childRepo;

	@Autowired
	ChildrenRepository childrenRepository;

	@Autowired
	ParentRepository parentRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	AuthenticatedUserProvider authenticatedUserProvider;

	@RequestMapping("/account/{id}")
	public Account findById(@PathVariable int id) {
		return accountRepo.findById(id);
	}

	@PutMapping(value = "/encryptpassword")
	public String encryptpassword(){

		childrenRepository.findChildrenByIsPasswordEncryptedIsNull().forEach(children -> {
			children.setPasswordEncrypted(true);
			children.setPassword(passwordEncoder.encode(children.getPassword()));
			childrenRepository.save(children);
		});

		parentRepository.findParentByIsPasswordEncryptedIsNull().forEach(parent -> {
			parent.setPasswordEncrypted(true);
			parent.setPassword(passwordEncoder.encode(parent.getPassword()));
			parentRepository.save(parent);
		});

		return "ok";
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ResponseEntity<?> login() {

		String email = authenticatedUserProvider.getLoggedInUserEmail();
		Parent parent = accountRepo.findParentByEmail(email);
		if (parent != null) {
			parent.setUserType(AppConstants.USER_PARENT);
			return new ResponseEntity<Parent>(parent, HttpStatus.OK);
		} else {
			Children child = accountRepo.findChildByEmail(email);
			if (child != null) {
				child.setUserType(AppConstants.USER_CHILD);
				return new ResponseEntity<Children>(child, HttpStatus.OK);
			}
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

	@RequestMapping(value = "/passwordReminder", method = RequestMethod.POST)
	public ResponseEntity<?> passwordReminder(@RequestBody LoginDomain domain) throws JSONException {
		String password = null;

		Children children = accountRepo.findChildByEmail(domain.getEmail());
		if(children != null) {
			password = children.getPassword();
			log.debug("Email matched with child account" + children.getId());
		}

		if(password == null) {
			Parent parent = accountRepo.findParentByEmail(domain.getEmail());
			if(parent != null) {
				password = parent.getPassword();
				log.debug("Email matched with parent account" + parent.getId());
			}
		}

		if(password == null) {
			ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, USER_DOES_NOT_EXISTS_CODE,
					USER_DOES_NOT_EXISTS);

			return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
		}

		String body = PASSWORD_REMINDER_MAIL_BODY.replace("$PASSWORD", password);
		boolean sendMail = MailUtility.sendMail(domain.getEmail(), PASSWORD_REMINDER_MAIL_SUBJECT, body);

		if(sendMail) {
			return new ResponseEntity<>(new Response(MAIL_SENT_SUCCESS), HttpStatus.OK);
		} else {
			ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, MAIL_SENT_FAILED_CODE, MAIL_SENT_FAILED);
			return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/signup/parent", method = RequestMethod.POST)
	public ResponseEntity<?> saveParent(@RequestBody Parent parent) throws JSONException {

		boolean userExists = checkIfUserExists(parent.getEmail());
		if (!userExists) {
			parent.setPassword(passwordEncoder.encode(parent.getPassword()));
			parent.setPasswordEncrypted(true);
			Parent savedParent = parentRepo.persistParent(parent);
			if (savedParent != null) {
				savedParent.setUserType(AppConstants.USER_PARENT);
				return new ResponseEntity<Parent>(savedParent, HttpStatus.OK);
			}
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		} else {
			log.info(userExists + " Check if email exists: " + parent.getEmail());

			ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, USER_EXISTS_CODE,
					USER_EXISTS);

			return new ResponseEntity<ApiError>(apiError, HttpStatus.BAD_REQUEST);

		}
	}

	@RequestMapping(value = "/signup/child", method = RequestMethod.POST)
	public ResponseEntity<?> saveChild(@RequestBody Children child) throws JSONException {

		boolean userExists = checkIfUserExists(child.getEmail());
		if (!userExists) {
			child.setPassword(passwordEncoder.encode(child.getPassword()));
			child.setPasswordEncrypted(true);
			Children savedChild = childRepo.persistChild(child);
			if (savedChild != null) {
				savedChild.setUserType(AppConstants.USER_CHILD);
				return new ResponseEntity<Children>(savedChild, HttpStatus.OK);
			}
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		} else {
			log.info(userExists + " Check if email exists: " + child.getEmail());

			ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, USER_EXISTS_CODE,
					USER_EXISTS);

			return new ResponseEntity<ApiError>(apiError, HttpStatus.BAD_REQUEST);

		}
	}

	private boolean checkIfUserExists(String email) {
		log.info("Check if email exists: " + email);
		Children children = accountRepo.findChildByEmail(email);
		Parent parent = accountRepo.findParentByEmail(email);
		if (children != null || parent != null) {
			return true;
		}

		return false;
	}

}
