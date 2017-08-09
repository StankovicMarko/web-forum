package model;

import java.util.Date;

public class Forum {

	private int id;
	private String name;
	private String description;//optional
	private String ownerUsername; //gen app, only admin
	private Date creationDate;///gen app
	private boolean locked;
	private String type;
	private int parentForumId; //optional
	private boolean deleted;
	
	
	public Forum() {}


	public Forum(int id, String name, String description, String ownerUsername, Date creationDate, boolean locked, String type,
			int parentForumId, boolean deleted) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.ownerUsername = ownerUsername;
		this.creationDate = creationDate;
		this.locked = locked;
		this.type = type;
		this.parentForumId = parentForumId;
		this.deleted = deleted;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public String getOwnerUsername() {
		return ownerUsername;
	}


	public void setOwnerUsername(String ownerUsername) {
		this.ownerUsername = ownerUsername;
	}


	public Date getCreationDate() {
		return creationDate;
	}


	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}


	public boolean isLocked() {
		return locked;
	}


	public void setLocked(boolean locked) {
		this.locked = locked;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public int getParentForumId() {
		return parentForumId;
	}


	public void setParentForumId(int parentForumId) {
		this.parentForumId = parentForumId;
	}


	public boolean isDeleted() {
		return deleted;
	}


	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}


	@Override
	public String toString() {
		return "Forum [id=" + id + ", name=" + name + ", owner=" + ownerUsername + ", locked=" + locked + ", type=" + type
				+ ", deteled=" + deleted + "]";
	}
	
	
}
