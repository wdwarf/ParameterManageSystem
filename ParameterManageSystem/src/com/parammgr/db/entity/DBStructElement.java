package com.parammgr.db.entity;

import java.util.Date;

public class DBStructElement {
	private String projectId = "";
	private int structId = 0;
	private int elementId = 0;
	private String elementName = "";
	private String elementType = "";
	private int elementSize = 0;
	private boolean primaryKey = false;
	private String defaultValue = "";
	private String valueRegex = "";
	private Date createDate = new Date();
	
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public int getStructId() {
		return structId;
	}
	public void setStructId(int structId) {
		this.structId = structId;
	}
	public int getElementId() {
		return elementId;
	}
	public void setElementId(int elementId) {
		this.elementId = elementId;
	}
	public String getElementName() {
		return elementName;
	}
	public void setElementName(String elementName) {
		this.elementName = elementName;
	}
	public String getElementType() {
		return elementType;
	}
	public void setElementType(String elementType) {
		this.elementType = elementType;
	}
	public int getElementSize() {
		return elementSize;
	}
	public void setElementSize(int elementSize) {
		this.elementSize = elementSize;
	}
	public boolean isPrimaryKey() {
		return primaryKey;
	}
	public void setPrimaryKey(boolean primaryKey) {
		this.primaryKey = primaryKey;
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	public String getValueRegex() {
		return valueRegex;
	}
	public void setValueRegex(String valueRegex) {
		this.valueRegex = valueRegex;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
}
