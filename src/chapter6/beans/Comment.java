package chapter6.beans;

import java.io.Serializable;
import java.util.Date;

public class Comment implements Serializable {

	private int id;
	private int userid;
	private String text;
	private String name;
	private String account;
	private Date createdDate;

	//setter
	public void setId(int id) {
		this.id = id;
	}

	public void setUserId(int userid) {
		this.userid = userid;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	//getter
	public int getId() {
		return id;
	}

	public int getUserId() {
		return userid;
	}

	public String getText() {
		return text;
	}

	public String getAccount() {
		return account;
	}

	public String getName() {
		return name;
	}

	public Date getCreatedDate() {
		return createdDate;
	}


}