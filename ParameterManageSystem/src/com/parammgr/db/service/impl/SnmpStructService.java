package com.parammgr.db.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.snmp4j.smi.OID;

import com.parammgr.db.dao.ISnmpStructDao;
import com.parammgr.db.entity.CheckPoint;
import com.parammgr.db.entity.SnmpStruct;
import com.parammgr.db.entity.SnmpStructMember;
import com.parammgr.db.entity.WebStruct;
import com.parammgr.db.entity.WebStructMember;
import com.parammgr.db.service.ICheckPointService;
import com.parammgr.db.service.ISnmpStructService;
import com.parammgr.utils.FileUtil;

public class SnmpStructService implements ISnmpStructService {
	
	private ISnmpStructDao snmpStructDao;
	private ICheckPointService checkPointService;

	public ISnmpStructDao getSnmpStructDao() {
		return snmpStructDao;
	}

	public void setSnmpStructDao(ISnmpStructDao snmpStructDao) {
		this.snmpStructDao = snmpStructDao;
	}

	public ICheckPointService getCheckPointService() {
		return checkPointService;
	}

	public void setCheckPointService(ICheckPointService checkPointService) {
		this.checkPointService = checkPointService;
	}

	@Override
	public List<SnmpStruct> getAllStructs(String projectId) {
		try{
			return this.snmpStructDao.getAllStructs(projectId);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return new ArrayList<SnmpStruct>();
	}

	@Override
	public List<String> getAllStructNames(String projectId){
		try{
			return this.snmpStructDao.getAllStructNames(projectId);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return new ArrayList<String>();
	}
	
	@Override
	public List<String> getAllStructOids(String projectId){
		try{
			return this.snmpStructDao.getAllStructOids(projectId);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return new ArrayList<String>();
	}
	
	@Override
	public List<SnmpStruct> getAllStructOidsAndNames(String projectId) {
		return this.snmpStructDao.getAllStructOidsAndNames(projectId);
	}

	@Override
	public SnmpStruct getStruct(String snmpstructId){
		return this.snmpStructDao.getStruct(snmpstructId);
	}
	
	@Override
	public SnmpStruct getStructByStructName(String projectId, String structName){
		return this.snmpStructDao.getStructByStructName(projectId, structName);
	}
	
	@Override
	public SnmpStruct getStructByOid(String projectId, String oid){
		return this.snmpStructDao.getStructByOid(projectId, oid);
	}
	
	@Override
	public void addStruct(SnmpStruct snmpstruct) throws Exception{
		try{
			if(null != this.snmpStructDao.getStructByStructName(snmpstruct.getProjectId(), snmpstruct.getStructName()) 
					|| null != this.snmpStructDao.getStructByOid(snmpstruct.getProjectId(), snmpstruct.getOid())){
				throw new Exception("struct " + snmpstruct.getStructName() + "[" + snmpstruct.getOid() + "] has exists.");
			}
			this.snmpStructDao.addStruct(snmpstruct);
		}
		catch(Throwable e){
			e.printStackTrace();
			
			String causeStr = e.getMessage();
			Throwable cause = e.getCause();
			while(cause != null){
				causeStr += ", " + cause.getMessage();
				cause = cause.getCause();
			}
			
			throw new Exception("Add struct " + snmpstruct.getStructName() + "[" + snmpstruct.getOid() + "] failed, msg: " + causeStr);
		}
	}
	
	@Override
	public void updateStruct(SnmpStruct snmpstruct) throws Exception{
		try{
			this.snmpStructDao.updateStruct(snmpstruct);
		}catch(Throwable e){
			e.printStackTrace();

			String causeStr = e.getMessage();
			Throwable cause = e.getCause();
			while(cause != null){
				causeStr += ", " + cause.getMessage();
				cause = cause.getCause();
			}
			
			throw new Exception("Update struct failed, msg: " + causeStr);
		}
	}
	
	@Override
	public void deleteStruct(String snmpstructId) throws Exception{
		try{
			this.snmpStructDao.deleteStruct(snmpstructId);
		}catch(Throwable e){
			e.printStackTrace();

			String causeStr = e.getMessage();
			Throwable cause = e.getCause();
			while(cause != null){
				causeStr += ", " + cause.getMessage();
				cause = cause.getCause();
			}
			
			throw new Exception("Delete struct[" + snmpstructId + "] failed, msg: " + causeStr);
		}
	}

	@Override
	public void deleteStructByStructName(String projectId, String structName) throws Exception{
		SnmpStruct s = this.getStructByStructName(projectId, structName);
		if(null == s){
			return;
		}
		
		this.deleteStruct(s.getSnmpstructId());
	}

	@Override
	public void deleteStructByOid(String projectId, String oid) throws Exception{
		SnmpStruct s = this.getStructByOid(projectId, oid);
		if(null == s){
			return;
		}
		
		this.deleteStruct(s.getSnmpstructId());
	}
	
	@Override
	public void clearStructs(String projectId){
		this.snmpStructDao.clearStructs(projectId);
	}

	@Override
	public SnmpStructMember getStructMember(String snmpstructmemberId){
		return this.snmpStructDao.getStructMember(snmpstructmemberId);
	}

	@Override
	public void addStructMember(SnmpStructMember member) throws Exception{
		try{
			if(null != this.snmpStructDao.getStructMember(member.getSnmpstructmemberId())){
				throw new Exception("struct member has exists.");
			}
			
			this.snmpStructDao.addStructMember(member);
		}catch(Throwable e){
			e.printStackTrace();
			throw new Exception("Add struct member failed, msg: " + e.getMessage());
		}
	}

	@Override
	public void clearStructMembers(String snmpstructId){
		this.snmpStructDao.clearStructMembers(snmpstructId);
	}

	@Override
	public void deleteStructMember(SnmpStructMember member){
		this.snmpStructDao.deleteStructMember(member);
	}

	private boolean parseBoolean(String value){
		value = value.trim().toLowerCase();
		boolean re = false;
		if(null == value || "".equals(value) || "0".equals(value) || "false".equals(value)){
			return false;
		}else{
			re = true;
		}
		return re;
	}
	
	private String getElementAttribute(Element elmt, String attrName){
		 Attribute attr = elmt.attribute(attrName);
		 if(null != attr){
			 return attr.getValue().trim();
		 }
		 return "";
	}

	private SnmpStruct wrapStructFromXmlElement(Element structElement) throws Exception{
		String mibStructName = getElementAttribute(structElement, "structName");
		String oid = getElementAttribute(structElement, "oid");
		
		SnmpStruct struct = new SnmpStruct();
		struct.setSingleTable(structElement.getName().trim().equals("SingleTable"));
		struct.setStructName(mibStructName);
		struct.setOid(oid);
		
		Iterator<?> ie = structElement.elementIterator("column");
		while(ie.hasNext()){
			Element elmtMember = (Element)ie.next();
			
			String elementName = getElementAttribute(elmtMember, "name");
			String elmtOid = getElementAttribute(elmtMember, "oid");
			boolean writable = parseBoolean(getElementAttribute(elmtMember, "writable"));
			String dataType = getElementAttribute(elmtMember, "dataType");
			String structName = getElementAttribute(elmtMember, "structName");
			String memberName = getElementAttribute(elmtMember, "memberName");
			boolean primaryKey = parseBoolean(getElementAttribute(elmtMember, "primaryKey"));
			
			SnmpStructMember member = new SnmpStructMember();
			member.setElementName(elementName);
			member.setOid(elmtOid);
			member.setWritable(writable);
			member.setDataType(dataType);
			member.setStructName(structName);
			member.setMemberName(memberName);
			member.setPrimaryKey(primaryKey);
			
			struct.addMember(member);
		}
		return struct;
	}
	
	@Override
	public void importStructs(String projectId, File file) throws Exception{
		this.clearStructs(projectId);

		System.out.println("Importing Snmp Data Model: " + file.getPath());
		
		SAXReader inXml = new SAXReader();
		Document doc = inXml.read(file);
		
		Element rootElmt = doc.getRootElement();
		if(!"MIB".equals(rootElmt.getName())){
			throw new Exception("Invalid Snmp Data Model file.");
		}
		 
		//单实例
		Iterator<?> i = rootElmt.elementIterator("SingleTable");
		while(i.hasNext()){
			Element elmt = (Element)i.next();
			
			SnmpStruct struct = wrapStructFromXmlElement(elmt);
			struct.setProjectId(projectId);
			struct.check();
			this.addStruct(struct);
		}
		
		//多实例
		i = rootElmt.elementIterator("MultiTable");
		while(i.hasNext()){
			Element elmt = (Element)i.next();
			
			SnmpStruct struct = wrapStructFromXmlElement(elmt);
			struct.setProjectId(projectId);
			struct.check();
			this.addStruct(struct);
		}
	}
	
	public void exportStructs(String projectId, File file) throws Exception{
		System.out.println("exporting snmp data model of project[" + projectId + "] to " + file.getPath());
		
		Element rootElmt = DocumentHelper.createElement("MIB");
		Document doc = DocumentHelper.createDocument(rootElmt);
		
		List<SnmpStruct> structs = this.getAllStructs(projectId);
		for(SnmpStruct struct : structs){
			Element structElmt = DocumentHelper.createElement(struct.isSingleInstance() ? "SingleTable" : "MultiTable");
			
			structElmt.addAttribute("oid", struct.getOid());
			structElmt.addAttribute("structName", struct.getStructName());
			
			Set<SnmpStructMember> members = struct.getMembers();
			for(SnmpStructMember member : members){
				Element memberElmt = DocumentHelper.createElement("column");
				
				memberElmt.addAttribute("name", member.getElementName());
				memberElmt.addAttribute("oid", member.getOid());
				memberElmt.addAttribute("writable", member.isWritable() ? "1" : "0");
				memberElmt.addAttribute("dataType", member.getDataType());
				memberElmt.addAttribute("structName", member.getStructName());
				memberElmt.addAttribute("memberName", member.getMemberName());
				if(member.isPrimaryKey()){
					memberElmt.addAttribute("primaryKey", "true");
				}
				
				structElmt.add(memberElmt);
			}
			rootElmt.add(structElmt);
		}
		
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding("UTF-8");
		XMLWriter writer = new XMLWriter(new FileOutputStream(file), format);
		writer.write(doc);
		writer.close();
	}
	
	public String exportStructs(String projectId) throws Exception{
		String fileName = UUID.randomUUID().toString() + ".xml";
		File xmlFile = new File(FileUtil.getTempDir(), fileName);
		this.exportStructs(projectId, xmlFile);
		return xmlFile.getPath();
	}

	@Override
	public CheckPoint createCheckPoint(String projectId, String description) throws Exception {
		System.out.println("Creating snmp checkpoint of project " + projectId);
		String uuid = UUID.randomUUID().toString();
		File objsFile = new File(FileUtil.getTempDir() + "/" + uuid + ".objs");
		File zipFile = new File(FileUtil.getTempDir() + "/" + uuid + ".zip");
		try{
			List<SnmpStruct> structs = this.getAllStructs(projectId);
			FileOutputStream out = new FileOutputStream(objsFile);
			ObjectOutputStream oo = new ObjectOutputStream(out);
			oo.writeObject(structs);
			oo.close();
			out.close();
			FileUtil.zipFiles(objsFile.getPath(), zipFile);
			
			CheckPoint cp = new CheckPoint();
			cp.setProjectId(projectId);
			cp.setDescription(description);
			cp.setType("snmp");
			FileInputStream fi = new FileInputStream(zipFile);
			byte[] checkPointData = new byte[fi.available()];
			fi.read(checkPointData);
			fi.close();
			cp.setCheckPointData(checkPointData);
			
			this.checkPointService.addCheckPoint(cp);
			
			System.out.println("Created snmp checkpoint of project " + projectId);
			
			return cp;
		}catch(Exception e){
			throw e;
		}finally{
			FileUtil.rm(objsFile);
			FileUtil.rm(zipFile);
		}
	}

	@Override
	public boolean checkPointRecovery(String checkpointId) {
		System.out.println("Recovery snmp checkpoint " + checkpointId);
		
		String uuid = UUID.randomUUID().toString();
		File tmpDir = new File(FileUtil.getTempDir() + "/" + uuid);
		File zipFile = new File(FileUtil.getTempDir() + "/" + uuid + ".zip");
		tmpDir.mkdirs();
		
		try{
			CheckPoint cp = this.checkPointService.getCheckPoint(checkpointId);
			if (null == cp)
				return false;

			FileOutputStream out = new FileOutputStream(zipFile);
			out.write(cp.getCheckPointData());
			out.close();

			FileUtil.unzipFile(zipFile, tmpDir.getPath());

			List<String> files = new ArrayList<String>();
			FileUtil.listFiles(files, tmpDir.getPath(), "objs");
			if (files.isEmpty())
				return false;

			File objsFile = new File(files.get(0));
			FileInputStream in = new FileInputStream(objsFile);
			ObjectInputStream oi = new ObjectInputStream(in);
			List<SnmpStruct> structs = (List<SnmpStruct>) oi.readObject();
			oi.close();
			in.close();

			this.clearStructs(cp.getProjectId());
			for (SnmpStruct s : structs) {
				s.setSnmpstructId(null);
				for (SnmpStructMember m : s.getMembers()) {
					m.setSnmpstructmemberId(null);
				}
				this.addStruct(s);
			}
			
			return true;
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			FileUtil.rm(tmpDir);
			FileUtil.rm(zipFile);
		}
		
		return false;
	}
}
