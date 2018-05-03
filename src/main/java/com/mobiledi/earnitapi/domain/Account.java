package com.mobiledi.earnitapi.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The persistent class for the accounts database table.
 * 
 */
@Entity
@Table(name = "accounts")
@NamedQuery(name = "Account.findAll", query = "SELECT a FROM Account a")
@JsonIgnoreProperties({ "parents", "childrens" })

public class Account implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "ACCOUNTS_ID_GENERATOR")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ACCOUNTS_ID_GENERATOR")
	private Integer id;

	@Column(name = "account_code")
	private String accountCode;

	@Column(name = "create_date")
	@JsonFormat(pattern="MMM d, yyyy hh:mm:ss a")
	private Timestamp createDate;

	// bi-directional many-to-one association to Children
	@OneToMany(mappedBy = "account")
	private List<Children> childrens;

	// // bi-directional many-to-one association to Goal
	// @OneToMany(mappedBy = "account")
	// private List<Goal> goals;

	// bi-directional many-to-one association to Parent
	@OneToMany(mappedBy = "account")
	private List<Parent> parents;

	public Account() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAccountCode() {
		return this.accountCode;
	}

	public void setAccountCode(String accountCode) {
		this.accountCode = accountCode;
	}

	public Timestamp getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	public List<Children> getChildrens() {
		return this.childrens;
	}

	public void setChildrens(List<Children> childrens) {
		this.childrens = childrens;
	}

	public Children addChildren(Children children) {
		getChildrens().add(children);
		children.setAccount(this);

		return children;
	}

	public Children removeChildren(Children children) {
		getChildrens().remove(children);
		children.setAccount(null);

		return children;
	}

	public List<Parent> getParents() {
		return this.parents;
	}

	public void setParents(List<Parent> parents) {
		this.parents = parents;
	}

	public Parent addParent(Parent parent) {
		getParents().add(parent);
		parent.setAccount(this);

		return parent;
	}

	public Parent removeParent(Parent parent) {
		getParents().remove(parent);
		parent.setAccount(null);

		return parent;
	}

}