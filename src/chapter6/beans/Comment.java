package chapter6.beans;

import java.io.Serializable;
import java.util.Date;

public class Comment implements Serializable {

	private int id;
	private int userId;
	private int messageId;
	private String text;
	private Date createdDate;
	private Date updatedDate;

	//setter
	public void setId(int id) {
		this.id = id;
	}

	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
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
		return userId;
	}

	public int getMessageId() {
		return messageId;
	}

	public String getText() {
		return text;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

}