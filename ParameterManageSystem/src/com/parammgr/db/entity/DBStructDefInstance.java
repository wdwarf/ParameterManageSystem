package com.parammgr.db.entity;

import java.io.Serializable;

public class DBStructDefInstance implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String dbstructdefinstanceId;
	private String dbstructmemberId;
	private int instanceIndex;
	private String defValue;
	
	public String getDbstructdefinstanceId() {
		return dbstructdefinstanceId;
	}
	public void setDbstructdefinstanceId(String dbstructdefinstanceId) {
		this.dbstructdefinstanceId = dbstructdefinstanceId;
	}
	public String getDbstructmemberId() {
		return dbstructmemberId;
	}
	public void setDbstructmemberId(String dbstructmemberId) {
		this.dbstructmemberId = dbstructmemberId;
	}
	public String getDefValue() {
		return defValue;
	}
	public void setDefValue(String defValue) {
		this.defValue = defValue;

		if(null != this.defValue){
			this.defValue = this.defValue.trim();
		}
	}
	public int getInstanceIndex() {
		return instanceIndex;
	}
	public void setInstanceIndex(int instanceIndex) {
		this.instanceIndex = instanceIndex;
	}
	
}
