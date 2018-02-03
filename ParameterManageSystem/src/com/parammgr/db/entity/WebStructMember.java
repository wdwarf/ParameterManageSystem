package com.parammgr.db.entity;

import java.io.Serializable;
import java.util.Date;

public class WebStructMember implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String webstructmemberId = null;
	private String webstructId = "";
	private int memberId;
	private String memberName = "";
	private String memberCnName = "";
	private boolean primaryKey = false;
	private String defaultValue = "";
	private int webType;
	private String typeDesc = "";
	private String unit = "";
	private String valueRangeCn = "";
	private String valueRange = "";
	private int memberLevel = 0;
	private int structLevel = 0;
	private boolean canAddOrDelete = false;
	private boolean unique = false;
	private boolean rebootEffective = false;
	private String setStatus = "";
	private Date createDate = new Date();

	public int getMemberId() {
		return memberId;
	}
	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}
	public String getWebstructmemberId() {
		return webstructmemberId;
	}
	public void setWebstructmemberId(String webstructmemberId) {
		this.webstructmemberId = webstructmemberId;

		if(null != this.webstructmemberId){
			this.webstructmemberId = this.webstructmemberId.trim();
		}
	}
	public String getWebstructId() {
		return webstructId;
	}
	public void setWebstructId(String webstructId) {
		this.webstructId = webstructId;

		if(null != this.webstructId){
			this.webstructId = this.webstructId.trim();
		}
	}
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;

		if(null != this.memberName){
			this.memberName = this.memberName.trim();
		}
	}
	public String getMemberCnName() {
		return memberCnName;
	}
	public void setMemberCnName(String memberCnName) {
		this.memberCnName = memberCnName;

		if(null != this.memberCnName){
			this.memberCnName = this.memberCnName.trim();
		}
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

		if(null != this.defaultValue){
			this.defaultValue = this.defaultValue.trim();
		}
	}
	public boolean isUnique() {
		return unique;
	}
	public void setUnique(boolean unique) {
		this.unique = unique;
	}
	
	public int getWebType() {
		return webType;
	}
	public void setWebType(int webType) {
		this.webType = webType;
	}
	public String getTypeDesc() {
		return typeDesc;
	}
	public void setTypeDesc(String typeDesc) {
		this.typeDesc = typeDesc;
	}
	
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getValueRangeCn() {
		return valueRangeCn;
	}
	public void setValueRangeCn(String valueRangeCn) {
		this.valueRangeCn = valueRangeCn;
	}
	public String getValueRange() {
		return valueRange;
	}
	public void setValueRange(String valueRange) {
		this.valueRange = valueRange;
	}
	public int getMemberLevel() {
		return memberLevel;
	}
	public void setMemberLevel(int memberLevel) {
		this.memberLevel = memberLevel;
	}
	public int getStructLevel() {
		return structLevel;
	}
	public void setStructLevel(int structLevel) {
		this.structLevel = structLevel;
	}
	public boolean isCanAddOrDelete() {
		return canAddOrDelete;
	}
	public void setCanAddOrDelete(boolean canAddOrDelete) {
		this.canAddOrDelete = canAddOrDelete;
	}
	public boolean isRebootEffective() {
		return rebootEffective;
	}
	public void setRebootEffective(boolean rebootEffective) {
		this.rebootEffective = rebootEffective;
	}
	public String getSetStatus() {
		return setStatus;
	}
	public void setSetStatus(String setStatus) {
		this.setStatus = setStatus;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	private boolean parseBoolean(String value){
		value = value.trim().toLowerCase();
		boolean re = false;
		if(null == value || "0".equals(value) || "false".equals(value)){
			return false;
		}else{
			re = true;
		}
		return re;
	}

	public String genWebTips(){
		String webTibs = this.unit + "|" + this.valueRangeCn + "|" 
				+ this.memberLevel + "|"
				+ this.structLevel + "|"
				+ (this.canAddOrDelete ? "1" : "0") + "|"
				+ (this.unique ? "1" : "0") + "|"
				+ this.valueRange + "|"
				+ (this.rebootEffective ? "1" : "0");
		return webTibs;
	}
	
	public void parseWebTips(String webTips){
		String[] webTipsPart = webTips.split("\\|");
		for(int index = 0; index < webTipsPart.length; ++index){
			String value = webTipsPart[index].trim();
			if(value.isEmpty()) continue;
			
			switch(index){
			case 0:{
				this.unit = value;
				break;
			}
			case 1:{
				this.valueRangeCn = value;
				break;
			}
			case 2:{
				this.memberLevel = Integer.valueOf(value);
				break;
			}
			case 3:{
				this.structLevel = Integer.valueOf(value);
				break;
			}
			case 4:{
				this.canAddOrDelete = parseBoolean(value);
				break;
			}
			case 5:{
				this.unique = parseBoolean(value);
				break;
			}
			case 6:{
				this.valueRange = value;
				break;
			}
			case 7:{
				this.rebootEffective = parseBoolean(value);
				break;
			}
			}
		}
	}
}
