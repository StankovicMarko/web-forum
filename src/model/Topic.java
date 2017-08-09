package model;

import java.util.Date;

public class Topic {

	
	private int id;
	private String name;
	private String description;//optional
	private String content;
	private String ownerUsername; //gen app
	private Date creationDate;///gen app
	private boolean pinned; //samo moder/admin
	private boolean locked;
	private int parentForumId; //get app
	private boolean deleted;
	
	
	public Topic() {}


	public Topic(int id, String name, String description, String content, String ownerUsername, Date creationDate, boolean pinned,
			boolean locked, int parentForumId, boolean deleted) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.content = content;
		this.ownerUsername = ownerUsername;
		this.creationDate = creationDate;
		this.pinned = pinned;
		this.locked = locked;
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


	public Date getCreationDate() {
		return creationDate;
	}


	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}


	public boolean isPinned() {
		return pinned;
	}


	public void setPinned(boolean pinned) {
		this.pinned = pinned;
	}


	public boolean isLocked() {
		return locked;
	}


	public void setLocked(boolean locked) {
		this.locked = locked;
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
		return "Topic [id=" + id + ", name=" + name + ", owner=" + ownerUsername + ", deleted=" + deleted + "]";
	}
	

}
