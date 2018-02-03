package com.parammgr.db.entity;

import java.io.Serializable;
import java.util.Date;

import org.snmp4j.smi.OID;

public class SnmpStructMember implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String snmpstructmemberId = null;
	private String snmpstructId = "";
	private String elementName = "";
	private String structName = "";
	private String memberName = "";
	private String oid = "";
	private boolean primaryKey = false;
	private boolean writable = false;
	private String dataType = "";
	private Date createDate = new Date();
	
	public String getSnmpstructmemberId() {
		return snmpstructmemberId;
	}
	public void setSnmpstructmemberId(String snmpstructmemberId) {
		this.snmpstructmemberId = snmpstructmemberId;
	}
	public String getSnmpstructId() {
		return snmpstructId;
	}
	public void setSnmpstructId(String snmpstructId) {
		this.snmpstructId = snmpstructId;
	}
	public String getElementName() {
		return elementName;
	}
	public void setElementName(String elementName) {
		this.elementName = elementName.trim();
	}
	public String getStructName() {
		return structName;
	}
	public void setStructName(String structName) {
		this.structName = structName.trim();
	}
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName.trim();
	}
	public String getOid() {
		return oid;
	}
	public void setOid(String oid) throws Exception {
		try{
			OID _oid = new OID(oid);
			this.oid = _oid.toString();
		}catch(Exception e){
			e.printStackTrace();
			throw new Exception("Invalid OID: " + oid);
		}
	}
	public boolean isPrimaryKey() {
		return primaryKey;
	}
	public void setPrimaryKey(boolean primaryKey) {
		this.primaryKey = primaryKey;
	}
	public boolean isWritable() {
		return writable;
	}
	public void setWritable(boolean writable) {
		this.writable = writable;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) throws Exception {
		this.dataType = dataType.trim().toUpperCase();
		if(!"OCTETS".equals(this.dataType)
				&& !"UINT32".equals(this.dataType)
				&& !"INT32".equals(this.dataType)){
			throw new Exception("Invalid data type: " + dataType);
		}
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
}
