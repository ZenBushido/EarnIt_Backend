package com.mobiledi.earnitapi.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * The persistent class for the childrens database table.
 * 
 */
@Entity
@Table(name = "childrens")
@NamedQuery(name = "Children.findAll", query = "SELECT c FROM Children c")
@JsonIgnoreProperties({ "isPasswordEncrypted", "mobileApplications" })
public class Children implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "CHILDRENS_ID_GENERATOR")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CHILDRENS_ID_GENERATOR")
	private Integer id;

	private String avatar;

	@Column(name = "create_date")
	@JsonFormat(pattern="MMM d, yyyy hh:mm:ss a")
	private Timestamp createDate;

	private String email;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	private String message;

	private String password;

	private String phone;

	@Column(name = "update_date")
	@JsonFormat(pattern="MMM d, yyyy hh:mm:ss a")
	private Timestamp updateDate;

	// bi-directional many-to-one association to Account
	@ManyToOne
	private Account account;

	@Transient
	private String userType;

	// bi-directional many-to-one association to Task
	@OneToMany(mappedBy = "children")
	@OrderBy("due_date ASC")
	private List<Task> tasks;

	@Column(name = "fcm_token")
	private String fcmToken;

	private Boolean isDeleted;

	@Column(name = "is_password_encrypted")
	private Boolean isPasswordEncrypted;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "children", targetEntity = MobileApplication.class)
	@JsonIgnore
	private List<MobileApplication> mobileApplications;

	public Children() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAvatar() {
		return this.avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public Timestamp getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Timestamp getUpdateDate() {
		return this.updateDate;
	}

	public void setUpdateDate(Timestamp updateDate) {
		this.updateDate = updateDate;
	}

	public Account getAccount() {
		return this.account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public List<Task> getTasks() {
		return this.tasks;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}

	public Task addTask(Task task) {
		getTasks().add(task);
		task.setChildren(this);

		return task;
	}

	public Task removeTask(Task task) {
		getTasks().remove(task);
		task.setChildren(null);

		return task;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getFcmToken() {
		return fcmToken;
	}

	public void setFcmToken(String fcmToken) {
		this.fcmToken = fcmToken;
	}

	public Boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(Boolean deleted) {
		isDeleted = deleted;
	}

	public List<MobileApplication> getMobileApplications() {
		return mobileApplications;
	}

	public void setMobileApplications(
			List<MobileApplication> mobileApplications) {
		this.mobileApplications = mobileApplications;
	}

	public Boolean getPasswordEncrypted() {
		return isPasswordEncrypted;
	}

	public void setPasswordEncrypted(Boolean passwordEncrypted) {
		isPasswordEncrypted = passwordEncrypted;
	}
}