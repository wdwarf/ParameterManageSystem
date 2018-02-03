package com.parammgr.db.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.parammgr.db.dao.IWebStructDao;
import com.parammgr.db.entity.CheckPoint;
import com.parammgr.db.entity.DBStruct;
import com.parammgr.db.entity.DBStructDefInstance;
import com.parammgr.db.entity.DBStructMember;
import com.parammgr.db.entity.WebStruct;
import com.parammgr.db.entity.WebStructMember;
import com.parammgr.db.service.ICheckPointService;
import com.parammgr.db.service.IWebStructService;
import com.parammgr.utils.ExcelHelper;
import com.parammgr.utils.FileUtil;
import org.dom4j.io.OutputFormat;

public class WebStructService implements IWebStructService {
	
	private IWebStructDao webStructDao;
	private ICheckPointService checkPointService;

	public IWebStructDao getWebStructDao() {
		return webStructDao;
	}

	public void setWebStructDao(IWebStructDao webStructDao) {
		this.webStructDao = webStructDao;
	}

	public ICheckPointService getCheckPointService() {
		return checkPointService;
	}

	public void setCheckPointService(ICheckPointService checkPointService) {
		this.checkPointService = checkPointService;
	}

	@Override
	public List<WebStruct> getAllStructs(String projectId) {
		try{
			return this.webStructDao.getAllStructs(projectId);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return new ArrayList<WebStruct>();
	}

	@Override
	public List<String> getAllStructNames(String projectId){
		try{
			return this.webStructDao.getAllStructNames(projectId);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return new ArrayList<String>();
	}
	
	@Override
	public List<WebStruct> getAllStructIdsAndNames(String projectId) {
		// TODO Auto-generated method stub
		return this.webStructDao.getAllStructIdsAndNames(projectId);
	}

	@Override
	public WebStruct getStruct(String webstructId){
		return this.webStructDao.getStruct(webstructId);
	}
	
	@Override
	public WebStruct getStruct(String projectId, String structName){
		return this.webStructDao.getStruct(projectId, structName);
	}
	
	@Override
	public WebStruct getStruct(String projectId, int structId){
		return this.webStructDao.getStruct(projectId, structId);
	}
	
	@Override
	public void addStruct(WebStruct webstruct) throws Exception{
		try{
			if(null != this.webStructDao.getStruct(webstruct.getProjectId(), webstruct.getStructName()) 
					|| null != this.webStructDao.getStruct(webstruct.getProjectId(), webstruct.getStructId())){
				throw new Exception("struct has exists.");
			}
			this.webStructDao.addStruct(webstruct);
		}catch(Throwable e){
			e.printStackTrace();

			String causeStr = e.getMessage();
			Throwable cause = e.getCause();
			while(cause != null){
				causeStr += ", " + cause.getMessage();
				cause = cause.getCause();
			}
			
			throw new Exception("Add struct failed, msg: " + causeStr);
		}
	}
	
	@Override
	public void updateStruct(WebStruct webstruct) throws Exception{
		try{
			this.webStructDao.updateStruct(webstruct);
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
	public void deleteStruct(String webstructId) throws Exception{
		try{
			this.webStructDao.deleteStruct(webstructId);
		}catch(Throwable e){
			e.printStackTrace();

			String causeStr = e.getMessage();
			Throwable cause = e.getCause();
			while(cause != null){
				causeStr += ", " + cause.getMessage();
				cause = cause.getCause();
			}
			
			throw new Exception("Delete struct[" + webstructId + "] failed, msg: " + causeStr);
		}
	}
	
	@Override
	public void deleteStruct(String projectId, String structName) throws Exception{
		WebStruct s = this.getStruct(projectId, structName);
		if(null == s){
			return;
		}
		
		this.deleteStruct(s.getWebstructId());
	}
	
	@Override
	public void clearStructs(String projectId){
		this.webStructDao.clearStructs(projectId);
	}

	@Override
	public WebStructMember getStructMember(String webstructmemberId){
		return this.webStructDao.getStructMember(webstructmemberId);
	}

	@Override
	public void addStructMember(WebStructMember member) throws Exception{
		try{
			if(null != this.webStructDao.getStructMember(member.getWebstructmemberId())){
				throw new Exception("struct member has exists.");
			}
			
			this.webStructDao.addStructMember(member);
		}catch(Throwable e){
			e.printStackTrace();
			throw new Exception("Add struct member failed, msg: " + e.getMessage());
		}
	}

	@Override
	public void deleteStructMember(WebStructMember member){
		this.webStructDao.deleteStructMember(member);
	}

	@Override
	public void clearStructMembers(String webstructId){
		this.webStructDao.clearStructMembers(webstructId);
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

	public void doImportStructsOfXml(String projectId, File file) throws Exception{

		System.out.println("Importing web data model xml: " + file.getPath());
		
		SAXReader inXml = new SAXReader();
		Document doc = inXml.read(file);
		
		Element rootElmt = doc.getRootElement();
		if(!"Root".equals(rootElmt.getName())){
			throw new Exception("Invalid Web Data Model file.");
		}
		
		Set<String> structNames = new HashSet<String>();
		Set<Integer> structIds = new HashSet<Integer>();
		Iterator<?> i = rootElmt.elementIterator("Object");
		while(i.hasNext()){
			Element elmt = (Element)i.next();
			String structName = elmt.attribute("Name").getValue().trim();
			int structId = Integer.valueOf(elmt.attribute("StructID").getValue());
			String structCnName = elmt.attribute("StructCnName").getValue().trim();
			String classifyName = elmt.attribute("ClassifyName").getValue().trim();
			
			WebStruct struct = new WebStruct();
			struct.setProjectId(projectId);
			struct.setStructName(structName);
			struct.setClassifyName(classifyName);
			struct.setStructId(structId);
			struct.setStructCnName(structCnName);
			
			if(structNames.contains(structName)){
				throw new Exception("Duplicated struct name: " + structName);
			}

			if(structIds.contains(structId)){
				throw new Exception("Duplicated struct ID: " + structId);
			}
			
			structNames.add(structName);
			structIds.add(structId);
			
			System.out.println("Importing struct " + structName + "[" + structId + "]");
			
			Iterator<?> ie = elmt.elementIterator("Element");
			while(ie.hasNext()){
				Element elmtMember = (Element)ie.next();
				
				int memberId = Integer.valueOf(elmtMember.attribute("MemberID").getValue());
				memberId %= 1000;
				String memberName = elmtMember.attribute("Name").getValue().trim();
				boolean primaryKey = parseBoolean(elmtMember.attribute("IsPrimaryKey").getValue());
				String defaultValue = elmtMember.attribute("DefaultValue").getValue().trim();
				String memberCnName = elmtMember.attribute("MemberCnName").getValue().trim();
				String webTypeStr = elmtMember.attribute("WebType").getValue().trim();
				if(webTypeStr.isEmpty()) webTypeStr = "0";
				int webType = Integer.valueOf(webTypeStr);
				String typeDesc = elmtMember.attribute("TypeDesc").getValue().trim();
				String webTips = elmtMember.attribute("WebTips").getValue().trim();
				String setStatus = elmtMember.attribute("SetStatus").getValue().trim();
				
				WebStructMember member = new WebStructMember();
				member.setMemberId(memberId);
				member.setMemberName(memberName);
				member.setMemberCnName(memberCnName);
				member.setPrimaryKey(primaryKey);
				member.setDefaultValue(defaultValue);
				member.setWebType(webType);
				member.setTypeDesc(typeDesc);
				member.parseWebTips(webTips);
				member.setSetStatus(setStatus);
				struct.addMember(member);
			}
			this.webStructDao.addStruct(struct);
		}
	}
	
	@Override
	public void importStructsOfXml(String projectId, File file) throws Exception{
		this.clearStructs(projectId);
		doImportStructsOfXml(projectId, file);
	}

	private String getRowValueStr(Map<Integer, String> row, int index){
		try{
			String val = row.get(index);
			if(null != val && !val.isEmpty()){
				return val;
			}
		}catch(Exception e){
			e.printStackTrace();
			String str = "error row: ";
			for(Integer col : row.keySet()){
				str += row.get(col) + ", ";
			}
			System.out.println(str);
		}
		return "";
	}

	private int getRowValueInt(Map<Integer, String> row, int index){
		try{
			String val = row.get(index);
			if(null != val && !val.isEmpty()){
				return (int)Double.parseDouble(val);
			}
		}catch(Exception e){
			e.printStackTrace();
			String str = "error row: ";
			for(Integer col : row.keySet()){
				str += row.get(col) + ", ";
			}
			System.out.println(str);
		}
		return 0;
	}

	public void doImportStructsOfExcel(String projectId, File file) throws Exception{
		System.out.println("Importing web data model excel: " + file.getPath());
		
		Workbook wb = null;
		try{
			ExcelHelper excelHelper = new ExcelHelper();
			List<Map<Integer, String>> rows = excelHelper.loadFile(file);
			
			int rowCount = rows.size();
			if(rowCount < 2){
				return;
			}
			Map<Integer, String> titleRow = rows.get(0);
			int colCount = titleRow.size();
			if(0 == colCount){
				return;
			}
			
			int StructIDCol = -1;
			int StructNameCol = -1;
			int ClassifyNameCol = -1;
			int StructCnNameCol = -1;
			int MemberIDCol = -1;
			int MemberNameCol = -1;
			int IsPrimaryKeyCol = -1;
			int DefaultValueCol = -1;
			int MemberCnNameCol = -1;
			int WebTypeCol = -1;
			int TypeDescCol = -1;
			int WebTipsCol = -1;
			int SetStatusCol = -1;

			for(Integer col : titleRow.keySet()){
				String title = titleRow.get(col).trim().toLowerCase();
				if(title.isEmpty()) continue;
				switch (title) {
				case "structid": {
					StructIDCol = col;
					break;
				}
				case "structname": {
					StructNameCol = col;
					break;
				}
				case "classifyname": {
					ClassifyNameCol = col;
					break;
				}
				case "structcnname": {
					StructCnNameCol = col;
					break;
				}
				case "memberid": {
					MemberIDCol = col;
					break;
				}
				case "membername": {
					MemberNameCol = col;
					break;
				}
				case "isprimarykey": {
					IsPrimaryKeyCol = col;
					break;
				}
				case "defaultvalue": {
					DefaultValueCol = col;
					break;
				}
				case "membercnname": {
					MemberCnNameCol = col;
					break;
				}
				case "webtype": {
					WebTypeCol = col;
					break;
				}
				case "typedesc": {
					TypeDescCol = col;
					break;
				}
				case "webtips": {
					WebTipsCol = col;
					break;
				}
				case "setstatus": {
					SetStatusCol = col;
					break;
				}
				default:{
					//System.out.println(title);
					break;
				}
				}
			}

			if (-1 == StructIDCol || -1 == StructNameCol || -1 == ClassifyNameCol || -1 == StructCnNameCol || -1 == MemberIDCol
					|| -1 == MemberNameCol || -1 == IsPrimaryKeyCol || -1 == DefaultValueCol || -1 == MemberCnNameCol 
					|| -1 == WebTypeCol || -1 == TypeDescCol || -1 == WebTipsCol || -1 == SetStatusCol) {
				System.out.println("Invalid data model excel file.");
				return;
			}

			WebStruct struct = null;
			for(int r = 1; r < rowCount; ++r){
				Map<Integer, String> row = rows.get(r);
				if(null == row) continue;
				
				int structId = getRowValueInt(row, StructIDCol);
				String structName = getRowValueStr(row, StructNameCol);
				String classifyName = getRowValueStr(row, ClassifyNameCol);
				String structCnName = getRowValueStr(row, StructCnNameCol);
				int memberId = getRowValueInt(row, MemberIDCol);
				memberId %= 1000;
				String memberName = getRowValueStr(row, MemberNameCol);
				boolean primaryKey = (0 != getRowValueInt(row, IsPrimaryKeyCol));
				String defaultValue = getRowValueStr(row, DefaultValueCol);
				String memberCnName = getRowValueStr(row, MemberCnNameCol);
				int webType = getRowValueInt(row, WebTypeCol);
				String typeDesc = getRowValueStr(row, TypeDescCol);
				String webTips = getRowValueStr(row, WebTipsCol);
				String setStatus = getRowValueStr(row, SetStatusCol);

				if(structName.isEmpty() || memberName.isEmpty()){
					continue;
				}
				//System.out.println("Importing struct " + structName + "[" + structId + "]." + memberName + "[" + memberId + "]");
				
				if(null == struct || !struct.getStructName().equals(structName)){
					if(null != struct){
						struct.check();
						this.deleteStruct(projectId, struct.getStructName());
						this.addStruct(struct);
					}
					
					struct = new WebStruct();
					struct.setProjectId(projectId);
					struct.setStructName(structName);
					struct.setStructId(structId);
					struct.setStructCnName(structCnName);
					struct.setClassifyName(classifyName);
				}
				
				WebStructMember member = new WebStructMember();
				member.setMemberId(memberId);
				member.setMemberName(memberName);
				member.setMemberCnName(memberCnName);
				member.setPrimaryKey(primaryKey);
				member.setDefaultValue(defaultValue);
				member.setWebType(webType);
				member.setTypeDesc(typeDesc);
				member.parseWebTips(webTips);
				member.setSetStatus(setStatus);
				struct.addMember(member);
			}
			if(null != struct){
				struct.check();
				this.deleteStruct(projectId, struct.getStructName());
				this.addStruct(struct);
			}
		}catch(Exception e){
			throw e;
		}finally{
			if(null != wb){
				wb.close();
			}
		}
	}
	
	@Override
	public void importStructsOfExcel(String projectId, File file) throws Exception{
		this.clearStructs(projectId);
		doImportStructsOfExcel(projectId, file);
	}
	
	public void importStructs(String projectId, List<String> files) throws Exception{
		this.clearStructs(projectId);
		for (String f : files) {
			switch(FileUtil.getSuffix(f)){
			case "xlsx":{
				this.doImportStructsOfExcel(projectId, new File(f));
				break;
			}
			case "xml":{
				this.doImportStructsOfXml(projectId, new File(f));
				break;
			}
			default:{
				System.out.println("Invalid web data model file: " + f);
				break;
			}
			}
		}
	}
	
	@Override
	public String exportToXmlFile(String projectId) throws Exception{
		String fileName = UUID.randomUUID().toString() + ".xml";
		String xmlFile = FileUtil.getTempDir() + File.separator + fileName;
		
		System.out.println("exporting web data model[xml] of project[" + projectId + "] to " + xmlFile);
		
		Element rootElmt = DocumentHelper.createElement("Root");
		Document doc = DocumentHelper.createDocument(rootElmt);
		
		List<WebStruct> structs = this.getAllStructs(projectId);
		for(WebStruct struct : structs){
			Element structElmt = DocumentHelper.createElement("Object");
			
			structElmt.addAttribute("Name", struct.getStructName());
			structElmt.addAttribute("StructID", String.valueOf(struct.getStructId()));
			structElmt.addAttribute("StructCnName", struct.getStructCnName());
			structElmt.addAttribute("ClassifyName", struct.getClassifyName());
			
			Set<WebStructMember> members = struct.getMembers();
			for(WebStructMember member : members){
				Element memberElmt = DocumentHelper.createElement("Element");
				
				memberElmt.addAttribute("MemberID", String.valueOf(struct.getStructId() * 1000 + member.getMemberId()));
				memberElmt.addAttribute("Name", member.getMemberName());
				memberElmt.addAttribute("IsPrimaryKey", member.isPrimaryKey() ? "1" : "0");
				memberElmt.addAttribute("DefaultValue", member.getDefaultValue());
				memberElmt.addAttribute("MemberCnName", member.getMemberCnName());
				memberElmt.addAttribute("WebType", String.valueOf(member.getWebType()));
				memberElmt.addAttribute("TypeDesc", member.getTypeDesc());
				memberElmt.addAttribute("WebTips", member.genWebTips());
				memberElmt.addAttribute("SetStatus", member.getSetStatus());
				
				structElmt.add(memberElmt);
			}
			rootElmt.add(structElmt);
		}
		
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding("UTF-8");
		XMLWriter writer = new XMLWriter(new FileOutputStream(xmlFile), format);
		writer.write(doc);
		writer.close();
		
		return xmlFile;
	}
	
	@Override
	public String exportToExcelFile(String projectId) throws Exception{
		String uuid = UUID.randomUUID().toString();
		File tmpDir = new File(FileUtil.getTempDir() + File.separator + uuid);
		tmpDir.mkdirs();
		
		String fileName = "webstruct.xlsx";
		String excelFile = tmpDir.getPath() + File.separator + fileName;
		String zipFile = FileUtil.getTempDir() + File.separator + uuid + ".zip";
		
		System.out.println("exporting web data model[excel] of project[" + projectId + "] to " + excelFile);
		
		int rowNum = 0;
		Workbook wbDbstruct = new XSSFWorkbook();
		Sheet sheet = wbDbstruct.createSheet();
		sheet.createFreezePane(0, 1, 0, 1);

		CellStyle cellStyle = wbDbstruct.createCellStyle();
		Font cellFont = wbDbstruct.createFont();
		cellFont.setFontHeightInPoints((short) 9);
		cellFont.setFontName("新宋体");
		cellStyle.setFont(cellFont);
		cellStyle.setWrapText(false);
		for(int i = 0; i < 12; ++i){
			sheet.setDefaultColumnStyle(i, cellStyle);
		}
		
		CellStyle markCellStyle = wbDbstruct.createCellStyle();
		markCellStyle.setFillForegroundColor(IndexedColors.LEMON_CHIFFON.getIndex());
		markCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		Font markCellFont = wbDbstruct.createFont();
		markCellFont.setFontHeightInPoints((short) 9);
		markCellFont.setFontName("新宋体");
		markCellStyle.setFont(markCellFont);
		markCellStyle.setWrapText(false);
		
		Row row = sheet.createRow(rowNum);

		CellStyle titleStyle = wbDbstruct.createCellStyle();
		Font titleFont = wbDbstruct.createFont();
		titleFont.setFontHeightInPoints((short) 10);
		titleFont.setFontName("新宋体");
		titleFont.setColor(Font.COLOR_RED);
		titleFont.setBold(true);
		titleStyle.setFont(titleFont);
		titleStyle.setWrapText(false);
		
		row.createCell(0, CellType.STRING).setCellValue("StructID");
		row.createCell(1, CellType.STRING).setCellValue("StructName");
		row.createCell(2, CellType.STRING).setCellValue("ClassifyName");
		row.createCell(3, CellType.STRING).setCellValue("StructCnName");
		row.createCell(4, CellType.STRING).setCellValue("MemberID");
		row.createCell(5, CellType.STRING).setCellValue("MemberName");
		row.createCell(6, CellType.STRING).setCellValue("IsPrimaryKey");
		row.createCell(7, CellType.STRING).setCellValue("DefaultValue");
		row.createCell(8, CellType.STRING).setCellValue("MemberCnName");
		row.createCell(9, CellType.STRING).setCellValue("WebType");
		row.createCell(10, CellType.STRING).setCellValue("TypeDesc");
		row.createCell(11, CellType.STRING).setCellValue("WebTips");
		row.createCell(12, CellType.STRING).setCellValue("SetStatus");
		for(int i = row.getFirstCellNum(); i <= row.getLastCellNum(); ++i){
			Cell cell = row.getCell(i);
			if(null == cell) continue;
			cell.setCellStyle(titleStyle);
		}

		List<WebStruct> structs = this.getAllStructs(projectId);
		
		int structCount = 0;
		for(WebStruct struct : structs){
			boolean structInfoAdded = false;
			for(WebStructMember member : struct.getMembers()){
				row = sheet.createRow(++rowNum);
				row.createCell(0, CellType.STRING).setCellValue(struct.getStructId());
				row.createCell(1, CellType.STRING).setCellValue(struct.getStructName());
				if (!structInfoAdded) {
					row.createCell(2, CellType.STRING).setCellValue(struct.getClassifyName());
					row.createCell(3, CellType.STRING).setCellValue(struct.getStructCnName());
					structInfoAdded = true;
				}else{
					row.createCell(2, CellType.STRING).setCellValue("");
					row.createCell(3, CellType.STRING).setCellValue("");
				}
				int memberId = struct.getStructId() * 1000 + member.getMemberId();
				row.createCell(4, CellType.STRING).setCellValue(memberId);
				row.createCell(5, CellType.STRING).setCellValue(member.getMemberName());
				row.createCell(6, CellType.STRING).setCellValue(member.isPrimaryKey() ? 1 : 0);
				row.createCell(7, CellType.STRING).setCellValue(member.getDefaultValue());
				row.createCell(8, CellType.STRING).setCellValue(member.getMemberCnName());
				row.createCell(9, CellType.STRING).setCellValue(member.getWebType());
				row.createCell(10, CellType.STRING).setCellValue(member.getTypeDesc());
				row.createCell(11, CellType.STRING).setCellValue(member.genWebTips());
				row.createCell(12, CellType.STRING).setCellValue(member.getSetStatus());
				
				CellStyle cs = cellStyle;
				if(0 != (structCount % 2)){
					cs = markCellStyle;
				}
				for(int i = 0; i < 12; ++i){
					Cell cell = row.getCell(i);
					if(null == cell) continue;
					cell.setCellStyle(cs);
				}
			}
			++structCount;
		}
		
		for(int i = 0; i < 12; ++i){
			sheet.autoSizeColumn(i);
		}
		
		FileOutputStream structOutput = new FileOutputStream(excelFile);
		wbDbstruct.write(structOutput);
		wbDbstruct.close();
		structOutput.close();
		
		FileUtil.zipFiles(excelFile, new File(zipFile));
		FileUtil.rm(tmpDir);
		
		return zipFile;
	}

	@Override
	public CheckPoint createCheckPoint(String projectId, String description) throws Exception {
		System.out.println("Creating web checkpoint of project " + projectId);
		String uuid = UUID.randomUUID().toString();
		File objsFile = new File(FileUtil.getTempDir() + "/" + uuid + ".objs");
		File zipFile = new File(FileUtil.getTempDir() + "/" + uuid + ".zip");
		try{
			List<WebStruct> structs = this.getAllStructs(projectId);
			FileOutputStream out = new FileOutputStream(objsFile);
			ObjectOutputStream oo = new ObjectOutputStream(out);
			oo.writeObject(structs);
			oo.close();
			out.close();
			FileUtil.zipFiles(objsFile.getPath(), zipFile);
			
			CheckPoint cp = new CheckPoint();
			cp.setProjectId(projectId);
			cp.setDescription(description);
			cp.setType("web");
			FileInputStream fi = new FileInputStream(zipFile);
			byte[] checkPointData = new byte[fi.available()];
			fi.read(checkPointData);
			fi.close();
			cp.setCheckPointData(checkPointData);
			
			this.checkPointService.addCheckPoint(cp);
			
			System.out.println("Created web checkpoint of project " + projectId);
			
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
		System.out.println("Recovery web checkpoint " + checkpointId);
		
		String uuid = UUID.randomUUID().toString();
		File tmpDir = new File(FileUtil.getTempDir() + "/" + uuid);
		File zipFile = new File(FileUtil.getTempDir() + "/" + uuid + ".zip");
		tmpDir.mkdirs();
		
		try{
			CheckPoint cp = this.checkPointService.getCheckPoint(checkpointId);
			if (null == cp){
				System.out.println("checkpoint[" + checkpointId + "] not found");
				return false;
			}

			FileOutputStream out = new FileOutputStream(zipFile);
			out.write(cp.getCheckPointData());
			out.close();

			FileUtil.unzipFile(zipFile, tmpDir.getPath());

			List<String> files = new ArrayList<String>();
			FileUtil.listFiles(files, tmpDir.getPath(), "objs");
			if (files.isEmpty()){
				System.out.println("checkpoint[" + checkpointId + "]: invalid checkpointData");
				return false;
			}

			File objsFile = new File(files.get(0));
			FileInputStream in = new FileInputStream(objsFile);
			ObjectInputStream oi = new ObjectInputStream(in);
			List<WebStruct> structs = (List<WebStruct>) oi.readObject();
			oi.close();
			in.close();

			this.clearStructs(cp.getProjectId());

			for (WebStruct s : structs) {
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
