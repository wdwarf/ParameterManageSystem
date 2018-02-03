package com.parammgr.db.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

import com.parammgr.db.SqliteService;
import com.parammgr.db.dao.IDBStructDao;
import com.parammgr.db.entity.CheckPoint;
import com.parammgr.db.entity.DBStruct;
import com.parammgr.db.entity.DBStructDefInstance;
import com.parammgr.db.entity.DBStructMember;
import com.parammgr.db.service.ICheckPointService;
import com.parammgr.db.service.IDBStructService;
import com.parammgr.utils.ExcelHelper;
import com.parammgr.utils.FileUtil;

public class DBStructService implements IDBStructService {
	
	private IDBStructDao dbStructDao;
	private ICheckPointService checkPointService;

	public IDBStructDao getDbStructDao() {
		return dbStructDao;
	}

	public void setDbStructDao(IDBStructDao dbStructDao) {
		this.dbStructDao = dbStructDao;
	}

	public ICheckPointService getCheckPointService() {
		return checkPointService;
	}

	public void setCheckPointService(ICheckPointService checkPointService) {
		this.checkPointService = checkPointService;
	}

	@Override
	public List<DBStruct> getAllStructs(String projectId) {
		try{
			return this.dbStructDao.getAllStructs(projectId);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return new ArrayList<DBStruct>();
	}

	@Override
	public List<String> getAllStructNames(String projectId){
		try{
			return this.dbStructDao.getAllStructNames(projectId);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return new ArrayList<String>();
	}
	
	@Override
	public List<DBStruct> getAllStructIdsAndNames(String projectId){
		return this.dbStructDao.getAllStructIdsAndNames(projectId);
	}
	
	@Override
	public List<Integer> getAllStructIds(String projectId){
		try{
			return this.dbStructDao.getAllStructIds(projectId);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return new ArrayList<Integer>();
	}
	
	@Override
	public DBStruct getStruct(String dbstructId){
		return this.dbStructDao.getStruct(dbstructId);
	}
	
	@Override
	public DBStruct getStruct(String projectId, String structName){
		return this.dbStructDao.getStruct(projectId, structName);
	}
	
	@Override
	public DBStruct getStruct(String projectId, int structId){
		return this.dbStructDao.getStruct(projectId, structId);
	}
	
	@Override
	public void addStruct(DBStruct dbstruct) throws Exception{
		try{
			if(null != this.dbStructDao.getStruct(dbstruct.getProjectId(), dbstruct.getStructName()) 
					|| null != this.dbStructDao.getStruct(dbstruct.getProjectId(), dbstruct.getStructId())){
				throw new Exception("struct has exists.");
			}
			this.dbStructDao.addStruct(dbstruct);
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
	public void updateStruct(DBStruct dbstruct) throws Exception{
		try{
			this.dbStructDao.updateStruct(dbstruct);
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
	public void deleteStruct(String dbstructId) throws Exception{
		try{
			this.dbStructDao.deleteStruct(dbstructId);
		}catch(Throwable e){
			e.printStackTrace();

			String causeStr = e.getMessage();
			Throwable cause = e.getCause();
			while(cause != null){
				causeStr += ", " + cause.getMessage();
				cause = cause.getCause();
			}
			
			throw new Exception("Delete struct[" + dbstructId + "] failed, msg: " + causeStr);
		}
	}
	
	@Override
	public void deleteStruct(String projectId, String structName) throws Exception{
		DBStruct s = this.getStruct(projectId, structName);
		if(null == s){
			return;
		}
		
		this.deleteStruct(s.getDbstructId());
	}
	
	@Override
	public void clearStructs(String projectId){
		this.dbStructDao.clearStructs(projectId);
	}

	@Override
	public DBStructMember getStructMember(String dbstructmemberId){
		return this.dbStructDao.getStructMember(dbstructmemberId);
	}

	@Override
	public void addStructMember(DBStructMember member) throws Exception{
		try{
			if(null != this.dbStructDao.getStructMember(member.getDbstructmemberId())){
				throw new Exception("struct member has exists.");
			}
			
			this.dbStructDao.addStructMember(member);
		}catch(Throwable e){
			e.printStackTrace();
			throw new Exception("Add struct member failed, msg: " + e.getMessage());
		}
	}

	@Override
	public void deleteStructMember(DBStructMember member){
		this.dbStructDao.deleteStructMember(member);
	}

	@Override
	public void clearStructMembers(String dbstructId){
		this.dbStructDao.clearStructMembers(dbstructId);
	}
	
	//def instance
	@Override
	public DBStructDefInstance getDefInstance(String dbstructdefinstanceId){
		return this.dbStructDao.getDefInstance(dbstructdefinstanceId);
	}

	@Override
	public void addDefInstance(DBStructDefInstance instance) throws Exception{
		try{
			if(null != this.dbStructDao.getDefInstance(instance.getDbstructdefinstanceId())){
				throw new Exception("def instance has exists.");
			}
			
			this.dbStructDao.addDefInstance(instance);
		}catch(Throwable e){
			e.printStackTrace();
			throw new Exception("Add def instance failed, msg: " + e.getMessage());
		}
	}
	
	@Override
	public void addDefInstance(String dbstructmemberId, DBStructDefInstance instance) throws Exception{
		try{
			this.dbStructDao.addDefInstance(dbstructmemberId, instance);
		}catch(Throwable e){
			e.printStackTrace();
			throw new Exception("Add def instance failed, msg: " + e.getMessage());
		}
	}
	
	public void addDefInstance(String dbstructmemberId, List<DBStructDefInstance> instances) throws Exception{
		try{
			this.dbStructDao.addDefInstance(dbstructmemberId, instances);
		}catch(Throwable e){
			e.printStackTrace();
			throw new Exception("Add def instances failed, msg: " + e.getMessage());
		}
	}

	@Override
	public void deleteDefInstance(DBStructDefInstance instance){
		this.dbStructDao.deleteDefInstance(instance);
	}

	@Override
	public void clearDefInstancesOfMember(String dbstructmemberId){
		this.dbStructDao.clearDefInstancesOfMember(dbstructmemberId);
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

	private void importStructInfo(String ulTempDir, String projectId) throws Exception {
		String dbstructPath = FileUtil.findDir(ulTempDir, "dbstruct");
		System.out.println(dbstructPath);

		if (dbstructPath.isEmpty()) {
			throw new Exception("DIR [dbstruct] not found.");
		}

		List<String> files = new ArrayList<String>();
		FileUtil.listFiles(files, dbstructPath, "xlsx");
		for (String p : files) {
			
			System.out.println(p);

			ExcelHelper excelHelper = new ExcelHelper();
			List<Map<Integer, String>> rows = excelHelper.loadFile(new File(p));

			int rowCount = rows.size();
			if (rowCount <= 1)
				continue;

			Map<Integer, String> titleRow = rows.get(0);
			int colCount = titleRow.size();

			int StructIDCol = -1;
			int StructNameCol = -1;
			int IsTempTableCol = -1;
			int MemberIDCol = -1;
			int MemberNameCol = -1;
			int IsPrimaryKeyCol = -1;
			int ValueTypeCol = -1;
			int DefaultValueCol = -1;
			int ValueRegexCol = -1;
			int RefStructCol = -1;
			int RefMemberCol = -1;
			int UniqueFlagCol = -1;

			for (int col = 0; col < colCount; ++col) {
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
				case "istemptable": {
					IsTempTableCol = col;
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
				case "valuetype": {
					ValueTypeCol = col;
					break;
				}
				case "defaultvalue": {
					DefaultValueCol = col;
					break;
				}
				case "valueregex": {
					ValueRegexCol = col;
					break;
				}
				case "refstruct": {
					RefStructCol = col;
					break;
				}
				case "refmember": {
					RefMemberCol = col;
					break;
				}
				case "uniqueflag": {
					UniqueFlagCol = col;
					break;
				}
				}
			}

			if (-1 == StructIDCol || -1 == StructNameCol || -1 == IsTempTableCol || -1 == MemberIDCol
					|| -1 == MemberNameCol || -1 == IsPrimaryKeyCol || -1 == ValueTypeCol || -1 == DefaultValueCol
					|| -1 == ValueRegexCol || -1 == RefStructCol || -1 == RefMemberCol || -1 == UniqueFlagCol) {
				continue;
			}

			DBStruct struct = null;
			for (int i = 1; i < rowCount; ++i) {
				Map<Integer, String> row = rows.get(i);
				if(null == row) continue;

				int structId = (int) getRowValueInt(row, StructIDCol);
				String structName = getRowValueStr(row, StructNameCol);
				boolean isTempTable = (0 != getRowValueInt(row, IsTempTableCol));
				int memberId = (int) getRowValueInt(row, MemberIDCol);
				memberId %= 1000;
				String memberName = getRowValueStr(row, MemberNameCol);
				boolean isPrimaryKey = (0 != getRowValueInt(row, IsPrimaryKeyCol));
				String valueType = getRowValueStr(row, ValueTypeCol);
				String defaultValue = getRowValueStr(row, DefaultValueCol);
				String valueRegex = getRowValueStr(row, ValueRegexCol);
				String refStruct = getRowValueStr(row, RefStructCol);
				String refMember = getRowValueStr(row, RefMemberCol);
				boolean uniqueFlag = (0 != getRowValueInt(row, UniqueFlagCol));
				int valueSize = 0;

				String vtStr = valueType.toLowerCase().trim();
				if (0 == vtStr.indexOf("char[")) {
					String sizeStr = vtStr.substring(5, vtStr.length() - 1);
					valueType = "Char";
					valueSize = Integer.valueOf(sizeStr);
				} else {
					if (!defaultValue.isEmpty()) {
						if (-1 == defaultValue.trim().toLowerCase().indexOf("0x")) {
							Integer value = (int) Double.valueOf(defaultValue).doubleValue();
							defaultValue = value.toString();
						}
					}
					switch (vtStr) {
					case "uint64": {
						valueSize = 8;
						break;
					}
					case "uint32": {
						valueSize = 4;
						break;
					}
					case "uint16": {
						valueSize = 2;
						break;
					}
					case "uint8": {
						valueSize = 1;
						break;
					}
					case "int64": {
						valueSize = 8;
						break;
					}
					case "int32": {
						valueSize = 4;
						break;
					}
					case "int16": {
						valueSize = 2;
						break;
					}
					case "int8": {
						valueSize = 1;
						break;
					}
					case "boolean": {
						valueSize = 1;
						break;
					}
					}
				}

				if (null == struct || struct.getStructId() != structId) {
					if (null != struct) {
						if (struct.getStructId() > 0) {
							String str = "Adding struct[" + struct.getStructName() + "] members{";
							for(DBStructMember m : struct.getMembers()){
								str += m.getMemberName() + "[" + m.getMemberId() + "] ";
							}
							str += "}";
							System.out.println(str);
							
							struct.check();
							this.deleteStruct(projectId, struct.getStructName());
							this.addStruct(struct);
						}
					}

					struct = new DBStruct();
					struct.setStructId(structId);
					struct.setStructName(structName);
					struct.setProjectId(projectId);
					struct.setTempTable(isTempTable);

					DBStructMember member = new DBStructMember();
					member.setDefaultValue(defaultValue);
					member.setMemberId(memberId);
					member.setMemberName(memberName);
					member.setMemberSize(valueSize);
					member.setMemberType(valueType);
					member.setPrimaryKey(isPrimaryKey);
					member.setRefMember(refMember);
					member.setRefStruct(refStruct);
					member.setUnique(uniqueFlag);
					member.setValueRegex(valueRegex);

					struct.addMember(member);
				} else {
					DBStructMember member = new DBStructMember();
					member.setDefaultValue(defaultValue);
					member.setMemberId(memberId);
					member.setMemberName(memberName);
					member.setMemberSize(valueSize);
					member.setMemberType(valueType);
					member.setPrimaryKey(isPrimaryKey);
					member.setRefMember(refMember);
					member.setRefStruct(refStruct);
					member.setUnique(uniqueFlag);
					member.setValueRegex(valueRegex);

					struct.addMember(member);
				}
			}

			if (struct.getStructId() > 0) {
				String str = "Adding struct[" + struct.getStructName() + "] members{";
				for(DBStructMember m : struct.getMembers()){
					str += m.getMemberName() + "[" + m.getMemberId() + "] ";
				}
				str += "}";
				System.out.println(str);
				
				struct.check();
				this.deleteStruct(projectId, struct.getStructName());
				this.addStruct(struct);
			}
		}
	}

	private void importDefaultInstanceInfo(String ulTempDir, String projectId) throws Exception {
		String multiInstanceDefaultPath = FileUtil.findDir(ulTempDir, "Multi-Instance-Default");
		System.out.println(multiInstanceDefaultPath);
		List<String> multiInstanceDefaultFiles = new ArrayList<String>();
		FileUtil.listFiles(multiInstanceDefaultFiles, multiInstanceDefaultPath, "xlsx");
		for (String p : multiInstanceDefaultFiles) {
			File f = new File(p);

			if (f.getName().indexOf("-h") > 0) {
				ExcelHelper excelHelper = new ExcelHelper();
				List<Map<Integer, String>> rows = excelHelper.loadFile(f);

				int rowCount = rows.size();
				if (rowCount <= 2)
					continue;

				int colCount = rows.get(0).size();
				if (colCount <= 2)
					continue;

				String structName = getRowValueStr(rows.get(0), 0);
				DBStruct struct = this.getStruct(projectId, structName);
				if (null == struct) {
					continue;
				}

				Map<String, List<DBStructDefInstance>> instanceMap = new HashMap<String, List<DBStructDefInstance>>();
				for (int i = 0; i < colCount; ++i) {
					List<DBStructDefInstance> instances = new ArrayList<DBStructDefInstance>();
					String memberName = getRowValueStr(rows.get(1), i);
					if (memberName.isEmpty())
						continue;
					for (int r = 2; r < rowCount; ++r) {
						Map<Integer, String> row = rows.get(r);

						DBStructDefInstance ins = new DBStructDefInstance();
						ins.setDefValue(getRowValueStr(row, i));
						instances.add(ins);
					}

					instanceMap.put(memberName, instances);
				}

				while (true) {
					boolean allInsEmpty = true;
					for (String memberName : instanceMap.keySet()) {
						List<DBStructDefInstance> insList = instanceMap.get(memberName);
						if (!insList.get(insList.size() - 1).getDefValue().trim().isEmpty()) {
							allInsEmpty = false;
							break;
						}
					}

					if (allInsEmpty) {
						for (String memberName : instanceMap.keySet()) {
							List<DBStructDefInstance> insList = instanceMap.get(memberName);
							insList.remove(insList.size() - 1);
						}
					} else {
						break;
					}
				}

				for (String memberName : instanceMap.keySet()) {
					DBStructMember member = struct.getMember(memberName);
					if (null == member) {
						continue;
					}
					this.addDefInstance(member.getDbstructmemberId(), instanceMap.get(memberName));
				}

			} else if (f.getName().indexOf("-v") > 0) {
				ExcelHelper excelHelper = new ExcelHelper();
				List<Map<Integer, String>> rows = excelHelper.loadFile(f);

				int rowCount = rows.size();
				if (rowCount <= 0)
					continue;

				int colCount = rows.get(0).size();
				if (colCount <= 2)
					continue;

				String currentStructName = null;
				Map<String, List<DBStructDefInstance>> instanceMap = new HashMap<String, List<DBStructDefInstance>>();
				for (int r = 0; r < rowCount; ++r) {
					Map<Integer, String> row = rows.get(r);

					String structName = getRowValueStr(row, 0);
					if (null == currentStructName) {
						currentStructName = structName;
					} else if (!structName.equals(currentStructName)) {
						DBStruct struct = this.getStruct(projectId, currentStructName);
						if (null == struct) {
							continue;
						}

						for (int i = colCount - 3; i >= 0; --i) {
							boolean allInsEmpty = true;
							for (String memberName : instanceMap.keySet()) {
								List<DBStructDefInstance> instances = instanceMap.get(memberName);
								if (!instances.get(instances.size() - 1).getDefValue().trim().isEmpty()) {
									allInsEmpty = false;
									break;
								}
							}

							if (allInsEmpty) {
								for (String memberName : instanceMap.keySet()) {
									List<DBStructDefInstance> instances = instanceMap.get(memberName);
									instances.remove(instances.size() - 1);
								}
							} else {
								break;
							}
						}

						for (String memberName : instanceMap.keySet()) {
							List<DBStructDefInstance> instances = instanceMap.get(memberName);
							DBStructMember member = struct.getMember(memberName);
							if (null == member) {
								continue;
							}
							this.addDefInstance(member.getDbstructmemberId(), instances);
						}
						currentStructName = structName;
						instanceMap = new HashMap<String, List<DBStructDefInstance>>();
					}

					String memberName = getRowValueStr(row, 1);

					List<DBStructDefInstance> instances = new ArrayList<DBStructDefInstance>();
					for (int col = 2; col < colCount; ++col) {
						String val = getRowValueStr(row, col);
						DBStructDefInstance intance = new DBStructDefInstance();
						intance.setDefValue(val);
						instances.add(intance);
					}

					instanceMap.put(memberName, instances);
				}
			}
		}
	}

	private String getCellValueString(Row row, int cellIndex) {
		Cell cell = row.getCell(cellIndex);
		if (null == cell) {
			return "";
		}

		switch (cell.getCellTypeEnum()) {
		case NUMERIC: {
			double val = cell.getNumericCellValue();
			DecimalFormat df = new DecimalFormat("#########.######");
			return df.format(val);
		}
		default:
			break;
		}

		return cell.toString();
	}

	private double getCellValueNumeric(Row row, int cellIndex) {
		Cell cell = row.getCell(cellIndex);
		if (null == cell) {
			return 0;
		}
		
		switch (cell.getCellTypeEnum()) {
		case NUMERIC:{
			return cell.getNumericCellValue();
		}
		default:{
			String valStr = this.getCellValueString(row, cellIndex);
			if(!valStr.isEmpty()){
				return Double.parseDouble(valStr);
			}else return 0;
		}
		}
	}

	private void importData(String ulTempDir, String projectId) throws Exception {
		this.importStructInfo(ulTempDir, projectId);

		this.importDefaultInstanceInfo(ulTempDir, projectId);
	}
	
	@Override
	public void importStructs(String projectId, File zipFile) throws Exception{
		if (null == zipFile) {
			throw new Exception("zip file is null.");
		}

		System.out.println("Importing: " + zipFile.getPath());
		
		String ulTempDir = FileUtil.getTempDir() + File.separator + UUID.randomUUID().toString();
		File f_ulTempDir = new File(ulTempDir);
		if (!f_ulTempDir.exists()) {
			f_ulTempDir.mkdirs();
		}

		try {
			FileUtil.unzipFile(zipFile, ulTempDir);
			this.clearStructs(projectId);
			this.importData(ulTempDir, projectId);
		} catch (Exception e) {
			throw e;
		}finally{
			FileUtil.rm(f_ulTempDir);
		}
		
		System.out.println("Import done: " + zipFile.getPath());
	}
	
	@Override
	public String exportToDBFile(String projectId) throws Exception{
		System.out.println("exporting db file of project[" + projectId + "]");
		String fileName = UUID.randomUUID().toString() + ".db";
		String dbFile = FileUtil.getTempDir() + File.separator + fileName;
		SqliteService sqliteService = new SqliteService();
		sqliteService.createOamDB(dbFile, this.getAllStructs(projectId));
		System.out.println("exported db file of project[" + projectId + "] to " + dbFile);
		return dbFile;
	}
	
	private void exportDbstruct(String filePath, List<DBStruct> structs) throws Exception{
		FileOutputStream dbstructOutput = new FileOutputStream(filePath);

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
		row.createCell(2, CellType.STRING).setCellValue("IsTempTable");
		row.createCell(3, CellType.STRING).setCellValue("MemberID");
		row.createCell(4, CellType.STRING).setCellValue("MemberName");
		row.createCell(5, CellType.STRING).setCellValue("IsPrimaryKey");
		row.createCell(6, CellType.STRING).setCellValue("ValueType");
		row.createCell(7, CellType.STRING).setCellValue("DefaultValue");
		row.createCell(8, CellType.STRING).setCellValue("ValueRegex");
		row.createCell(9, CellType.STRING).setCellValue("RefStruct");
		row.createCell(10, CellType.STRING).setCellValue("RefMember");
		row.createCell(11, CellType.STRING).setCellValue("UniqueFlag");
		for(int i = row.getFirstCellNum(); i <= row.getLastCellNum(); ++i){
			Cell cell = row.getCell(i);
			if(null == cell) continue;
			cell.setCellStyle(titleStyle);
		}
		
		int structCount = 0;
		for(DBStruct struct : structs){
			for(DBStructMember member : struct.getMembers()){
				row = sheet.createRow(++rowNum);
				row.createCell(0, CellType.STRING).setCellValue(struct.getStructId());
				row.createCell(1, CellType.STRING).setCellValue(struct.getStructName());
				row.createCell(2, CellType.STRING).setCellValue(struct.isTempTable() ? 1 : 0);
				int memberId = struct.getStructId() * 1000 + member.getMemberId();
				row.createCell(3, CellType.STRING).setCellValue(memberId);
				row.createCell(4, CellType.STRING).setCellValue(member.getMemberName());
				row.createCell(5, CellType.STRING).setCellValue(member.isPrimaryKey() ? 1 : 0);
				String valueType = member.getMemberType();
				switch(valueType){
				case "Char":{
					valueType = "Char[" + member.getMemberSize() + "]";
					break;
				}
				default:{
					break;
				}
				}
				row.createCell(6, CellType.STRING).setCellValue(valueType);
				row.createCell(7, CellType.STRING).setCellValue(member.getDefaultValue());
				row.createCell(8, CellType.STRING).setCellValue(member.getValueRegex());
				row.createCell(9, CellType.STRING).setCellValue(member.getRefStruct());
				row.createCell(10, CellType.STRING).setCellValue(member.getRefMember());
				row.createCell(11, CellType.STRING).setCellValue(member.isUnique() ? 1 : 0);
				
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
		wbDbstruct.write(dbstructOutput);
		wbDbstruct.close();
		dbstructOutput.close();
	}

	private void createMultiInstanceH(String dir, DBStruct struct,
			Map<Integer, List<DBStructDefInstance>> instanceMap) throws Exception {
		FileOutputStream out = new FileOutputStream(dir + "/" + struct.getStructName() + "-h.xlsx");
		Workbook wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet();
		
		int rowNum = 0;
		sheet.createRow(rowNum++);
		sheet.createRow(rowNum++);

		CellStyle titleStyle = wb.createCellStyle();
		Font titleFont = wb.createFont();
		titleFont.setFontHeightInPoints((short) 10);
		titleFont.setFontName("新宋体");
		titleFont.setColor(Font.COLOR_RED);
		titleFont.setBold(true);
		titleStyle.setFont(titleFont);
		titleStyle.setWrapText(false);

		CellStyle cellStyle = wb.createCellStyle();
		Font cellFont = wb.createFont();
		cellFont.setFontHeightInPoints((short) 9);
		cellFont.setFontName("新宋体");
		cellStyle.setFont(cellFont);
		cellStyle.setWrapText(false);
		for(int i = 0; i < 12; ++i){
			sheet.setDefaultColumnStyle(i, cellStyle);
		}
		
		
		int colCount = instanceMap.get(instanceMap.keySet().iterator().next()).size();
		
		int colNum = 0;
		for(Integer memberId : instanceMap.keySet()){
			Row structNameRow = sheet.getRow(0);
			Row memberNameRow = sheet.getRow(1);
			String memberName = struct.getMember(memberId).getMemberName();
			
			Cell cell = structNameRow.createCell(colNum, CellType.STRING);
			cell.setCellValue(struct.getStructName());
			cell.setCellStyle(titleStyle);
			
			cell = memberNameRow.createCell(colNum, CellType.STRING);
			cell.setCellValue(memberName);
			cell.setCellStyle(titleStyle);
			++colNum;
		}
		sheet.createFreezePane(0, 2, 0, 2);

		for(int i = 0; i < colCount; ++i){
			Row row = sheet.createRow(rowNum++);
			
			colNum = 0;
			for(Integer memberId : instanceMap.keySet()){
				String value = instanceMap.get(memberId).get(i).getDefValue();
				Cell cell = row.createCell(colNum++, CellType.STRING);
				cell.setCellValue(value);
				cell.setCellStyle(cellStyle);
			}
		}

		for(int i = 0; i < 12; ++i){
			sheet.autoSizeColumn(i);
		}
		wb.write(out);
		wb.close();
		out.close();
	}
	
	public void exportToExcelFile(String projectId, File file) throws Exception{
		System.out.println("exporting excel files of project[" + projectId + "]");
		
		String uuid = UUID.randomUUID().toString();
		String tmpDir = FileUtil.getTempDir() + "/" + uuid;
		String tmpDbDir = tmpDir + "/db";
		File zipFile = file;
		
		try{
			List<DBStruct> structs = this.getAllStructs(projectId);
			
			String dbstructDir = tmpDbDir + "/dbstruct";
			String multiInstanceDefaultDir = tmpDbDir + "/Multi-Instance-Default";
			File f = new File(dbstructDir);
			f.mkdirs();
			f = new File(multiInstanceDefaultDir);
			f.mkdirs();
			
			exportDbstruct(dbstructDir + "/dbstruct.xlsx", structs);
			
			FileOutputStream multiInstanceDefaultOutput = new FileOutputStream(multiInstanceDefaultDir + "/MultiInstance-v.xlsx");
			
			//
			int rowNum = 0;
			Workbook wbMultiInstanceDefault = new XSSFWorkbook();
			Sheet sheet = wbMultiInstanceDefault.createSheet();

			CellStyle cellStyle = wbMultiInstanceDefault.createCellStyle();
			Font cellFont = wbMultiInstanceDefault.createFont();
			cellFont.setFontHeightInPoints((short) 9);
			cellFont.setFontName("新宋体");
			cellStyle.setFont(cellFont);
			cellStyle.setWrapText(false);
			for(int i = 0; i < 12; ++i){
				sheet.setDefaultColumnStyle(i, cellStyle);
			}
			
			CellStyle markCellStyle = wbMultiInstanceDefault.createCellStyle();
			markCellStyle.setFillForegroundColor(IndexedColors.LEMON_CHIFFON.getIndex());
			markCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			Font markCellFont = wbMultiInstanceDefault.createFont();
			markCellFont.setFontHeightInPoints((short) 9);
			markCellFont.setFontName("新宋体");
			markCellStyle.setFont(markCellFont);
			markCellStyle.setWrapText(false);

			if (!structs.isEmpty()) {
				int structCount = 0;
				for (DBStruct struct : structs) {
					if (struct.isSingleInstance())
						continue;

					int instanceCount = 0;
					Map<Integer, List<DBStructDefInstance>> instanceMap = new HashMap<Integer, List<DBStructDefInstance>>();
					for (DBStructMember member : struct.getMembers()) {
						if (!member.getDefInstances().isEmpty()) {
							instanceCount = member.getDefInstances().size();
							instanceMap.put(member.getMemberId(), member.getDefInstances());
						}
					}
					if (instanceMap.isEmpty())
						continue;

					if (instanceCount > 6) {
						createMultiInstanceH(multiInstanceDefaultDir, struct, instanceMap);
						continue;
					}

					CellStyle cs = cellStyle;
					if (0 != (structCount % 2)) {
						cs = markCellStyle;
					}

					for (Integer memberId : instanceMap.keySet()) {
						String memberName = struct.getMember(memberId).getMemberName();
						List<DBStructDefInstance> instances = instanceMap.get(memberId);

						Row row = sheet.createRow(rowNum++);
						int cellCount = 0;

						Cell cell = row.createCell(cellCount++, CellType.STRING);
						cell.setCellValue(struct.getStructName());
						cell.setCellStyle(cs);

						cell = row.createCell(cellCount++, CellType.STRING);
						cell.setCellValue(memberName);
						cell.setCellStyle(cs);

						for (DBStructDefInstance instance : instances) {
							cell = row.createCell(cellCount++, CellType.STRING);
							cell.setCellValue(instance.getDefValue());
							cell.setCellStyle(cs);
						}
					}

					++structCount;
				}

				for (int i = 0; i <= sheet.getRow(0).getLastCellNum(); ++i) {
					sheet.autoSizeColumn(i);
				}
			}

			wbMultiInstanceDefault.write(multiInstanceDefaultOutput);
			wbMultiInstanceDefault.close();
			multiInstanceDefaultOutput.close();
			
			FileUtil.zipFiles(tmpDbDir, zipFile);
			
			System.out.println("exported excel files of project[" + projectId + "] to " + zipFile.getPath());
		}catch(Exception e){
			throw e;
		}finally{
			FileUtil.rm(tmpDir);
		}
	}

	@Override
	public String exportToExcelFile(String projectId) throws Exception{
		String uuid = UUID.randomUUID().toString();
		File zipFile = new File(FileUtil.getTempDir() + "/" + uuid + ".zip");
		this.exportToExcelFile(projectId, zipFile);
		return zipFile.getPath();
	}

	@Override
	public CheckPoint createCheckPoint(String projectId, String description) throws Exception {
		System.out.println("Creating db checkpoint of project " + projectId);
		String uuid = UUID.randomUUID().toString();
		File objsFile = new File(FileUtil.getTempDir() + "/" + uuid + ".objs");
		File zipFile = new File(FileUtil.getTempDir() + "/" + uuid + ".zip");
		try{
			List<DBStruct> structs = this.getAllStructs(projectId);
			FileOutputStream out = new FileOutputStream(objsFile);
			ObjectOutputStream oo = new ObjectOutputStream(out);
			oo.writeObject(structs);
			oo.close();
			out.close();
			FileUtil.zipFiles(objsFile.getPath(), zipFile);
			
			CheckPoint cp = new CheckPoint();
			cp.setProjectId(projectId);
			cp.setDescription(description);
			cp.setType("db");
			FileInputStream fi = new FileInputStream(zipFile);
			byte[] checkPointData = new byte[fi.available()];
			fi.read(checkPointData);
			fi.close();
			cp.setCheckPointData(checkPointData);
			
			this.checkPointService.addCheckPoint(cp);
			
			System.out.println("Created db checkpoint of project " + projectId);
			
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
		System.out.println("Recovery db checkpoint " + checkpointId);
		
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
			List<DBStruct> structs = (List<DBStruct>) oi.readObject();
			oi.close();
			in.close();

			this.clearStructs(cp.getProjectId());
			for (DBStruct s : structs) {
				s.setDbstructId(null);
				for (DBStructMember m : s.getMembers()) {
					m.setDbstructmemberId(null);
					for (DBStructDefInstance i : m.getDefInstances()) {
						i.setDbstructdefinstanceId(null);
					}
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
