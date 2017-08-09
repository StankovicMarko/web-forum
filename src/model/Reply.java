package model;

import java.util.Date;

public class Reply {
	
	private int id;
	private String content;
	private String ownerUsername; //gen app
	private String ownerPicture;
	private Date creationDate;///gen app
	private int parentTopicId; //get app
	private boolean deleted;
	
	
	public Reply() {}


	public Reply(int id, String content, String ownerUsername, String ownerPicture, Date creationDate,
			int parentTopicId, boolean deleted) {
		super();
		this.id = id;
		this.content = content;
		this.ownerUsername = ownerUsername;
		this.ownerPicture = ownerPicture;
		this.creationDate = creationDate;
		this.parentTopicId = parentTopicId;
		this.deleted = deleted;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}


	public String getOwnerUsername() {
		return ownerUsername;
	}


	public void setOwnerUsername(String ownerUsername) {
		this.ownerUsername = ownerUsername;
	}


	public String getOwnerPicture() {
		return ownerPicture;
	}


	public void setOwnerPicture(String ownerPicture) {
		this.ownerPicture = ownerPicture;
	}


	public Date getCreationDate() {
		return creationDate;
	}


	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}


	public int getParentTopicId() {
		return parentTopicId;
	}


	public void setParentTopicId(int parentTopicId) {
		this.parentTopicId = parentTopicId;
	}


	public boolean isDeleted() {
		return deleted;
	}


	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}


	
	
}
