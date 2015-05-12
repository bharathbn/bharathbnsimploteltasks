package com.simplotel.resources.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

// model for task


@XmlRootElement
public class SimplotelTask {
	
	private int id;
	private int userid;
	private String name;
	private String description;
	private int priority;
	private int state;
	private String created;
	private String last_modified;
	private String due_date;
	private String deleted;
	
	
	
	public String getDeleted() {
		return deleted;
	}
	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
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
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getCreated() {
		return created;
	}
	public void setCreated(String created) {
		this.created = created;
	}
	public String getLastmodified() {
		return last_modified;
	}
	public void setLastmodified(String lastmodified) {
		this.last_modified = lastmodified;
	}
	public String getDuedate() {
		return due_date;
	}
	public void setDuedate(String duedate) {
		this.due_date = duedate;
	}

}
