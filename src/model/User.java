package model;

import java.util.Date;

public class User {

	private int id;
	private String username; //unique
	private String password;
	private String name; ///optional
	private String surName;//optional
	private String mail;
	private Date dateReg;///gen app
	private String role;
	private boolean banned;
	private String picture;//putanja
	private boolean deleted;
	
	public User() {}

	public User(int id, String username, String password, String name, String surName, String mail, Date dateReg,
			String role, boolean banned, String picture, boolean deleted) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.name = name;
		this.surName = surName;
		this.mail = mail;
		this.dateReg = dateReg;
		this.role = role;
		this.banned = banned;
		this.picture = picture;
		this.deleted = deleted;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurName() {
		return surName;
	}

	public void setSurName(String surName) {
		this.surName = surName;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public Date getDateReg() {
		return dateReg;
	}

	public void setDateReg(Date dateReg) {
		this.dateReg = dateReg;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public boolean isBanned() {
		return banned;
	}

	public void setBanned(boolean banned) {
		this.banned = banned;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", role=" + role + ", banned=" + banned + ", deleted="
				+ deleted + "]";
	}
	
	
}
