package chapter6.beans;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {

	private int id;
	private int userid;
	private String text;
	private Date createdDate;
	private Date updatedDate;

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
	public int getUserId() {
		return userid;
	}
	public String getText() {
		return text;
	}
	public Date getCreatedDate() {
		return  createdDate;
	}
	public Date getUpdatedDate() {
		return updatedDate;
	}
}