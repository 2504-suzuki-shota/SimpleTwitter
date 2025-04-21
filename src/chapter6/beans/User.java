package chapter6.beans;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {

	private int id;
	private String account;
	private String name;
	private String email;
	private String password;
	private String description;
	private Date createdDate;
	private Date updatedDate;

	//setter
	public void setId(int id) {
		this.id = id;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	//getter
	public int getId() {
		return id;
	}
	public String getAccount() {
		return  account;
	}
	public String getName() {
		return  name;
	}
	public String getEmail() {
		return  email;
	}
	public String getPassword() {
		return  password;
	}
	public String getDescription() {
		return  description;
	}
	public Date getCreatedDate() {
		return  createdDate;
	}
	public Date getUpdatedDate() {
		return updatedDate;
	}
}