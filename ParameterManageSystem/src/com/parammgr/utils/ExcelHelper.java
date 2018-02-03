package com.parammgr.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class ExcelHelper {
	class XSSFSheetHandler extends DefaultHandler{
		StylesTable styles;
		ReadOnlySharedStringsTable strings;
		boolean isOpen = false;
		StringBuffer value = new StringBuffer();
		String cellStyleStr = null;
		int formatIndex = -1;
		String formatStr = "";
		int currentColumn = -1;
		String currentCellType = "";
		List<Map<Integer, String>> datas = new ArrayList<Map<Integer, String>>();
		HashMap<Integer, String> currentRow;
		
		XSSFSheetHandler(StylesTable styles, ReadOnlySharedStringsTable strings){
			this.styles = styles;
			this.strings = strings;
		}
		
		List<Map<Integer, String>> getRows(){
			return this.datas;
		}
		
		@Override
		public void characters(char[] ch, int start, int length) throws SAXException {
			if(isOpen){
				this.value.append(ch, start, length);
			}
		}
		
		@Override
		public void endDocument() throws SAXException {
			// TODO Auto-generated method stub
			super.endDocument();
		}
		
		@Override
		public void startDocument() throws SAXException {
			// TODO Auto-generated method stub
			super.startDocument();
		}
		
		@Override
		public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
			switch(name){
			//c -> Cell
			case "c":{
				this.value.setLength(0);
				
				String r = attributes.getValue("r");
				currentCellType = attributes.getValue("t");
				cellStyleStr = attributes.getValue("s"); 
				//System.out.println(r);
				int firstDigit = -1;
				for(int c = 0; c < r.length(); ++c){
					if(Character.isDigit(r.charAt(c))){
						firstDigit = c;
						break;
					}
				}
				
				currentColumn = this.nameToColumn(r.substring(0, firstDigit));
				break;
			}
			//start of cell content
			case "inlineStr":
			case "v":{
				this.isOpen = true;
				break;
			}
			case "row":{
				currentRow = new HashMap<Integer, String>();
				break;
			}
			}
		}

		@Override
		public void endElement(String uri, String localName, String name) throws SAXException {
			//System.out.println("public void endElement(" + uri + ", " + localName + ", " + name + ")");
			
			if(null != cellStyleStr){
				int styleIndex = Integer.parseInt(this.cellStyleStr);
				XSSFCellStyle style = this.styles.getStyleAt(styleIndex);
				this.formatIndex = style.getDataFormat();
				this.formatStr = style.getDataFormatString();
				if(null == this.formatStr){
					this.formatStr = BuiltinFormats.getBuiltinFormat(formatIndex);
				}
			}
			
			switch(name){
			//end of cell content
			case "vs":{
				isOpen = false;
				break;
			}
			case "v":{
				isOpen = false;
				String cellStr = value.toString();
				if(null != currentCellType){
					switch(currentCellType){
					//boolean
					case "b":{
						break;
					}
					//error
					case "e":{
						break;
					}
					//inlineStr
					case "inlineStr":{
						break;
					}
					//ssindex
					case "s":{
						int idx = Integer.parseInt(value.toString());
						XSSFRichTextString rtss = new XSSFRichTextString(strings.getEntryAt(idx));
						cellStr = rtss.toString();
						break;
					}
					//formula
					case "str":{
						break;
					}
					default:{
						break;
					}
					}
					
				}
				 
				currentRow.put(currentColumn, cellStr);
				break;
			}
			//end of row
			case "row":{
				this.datas.add(currentRow);
				currentRow = null;
				break;
			}
			default:{
				//System.out.println("end element: " + name);
				break;
			}
			}
		}
		
		int nameToColumn(String name){
			int col = -1;
			for(int i = 0; i < name.length(); ++i){
				int c = name.charAt(i);
				col = (col + 1) * 26 + c - 'A';
			}
			return col;
		}
	}
	
	public List<Map<Integer, String>> loadFileXlsx(File file) throws Exception{
		OPCPackage p = OPCPackage.open(file);
		ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(p);
		XSSFReader reader = new XSSFReader(p);
		StylesTable styles = reader.getStylesTable();
		XSSFReader.SheetIterator it = (XSSFReader.SheetIterator)reader.getSheetsData();
		if(it.hasNext()){
			InputStream in = it.next();
			InputSource is = new InputSource(in);
			SAXParserFactory saxFactory = SAXParserFactory.newInstance();
			SAXParser saxParser = saxFactory.newSAXParser();
			XMLReader xmlReader = saxParser.getXMLReader();
			XSSFSheetHandler handler = new XSSFSheetHandler(styles, strings);
			xmlReader.setContentHandler(handler);
			xmlReader.parse(is);
			return handler.getRows();
		}
		return new ArrayList<Map<Integer, String>>();
	}
	
	public List<Map<Integer, String>> loadFileXls(File file) throws Exception{
		FileInputStream in = new FileInputStream(file);
		Workbook wb = new HSSFWorkbook(in);
		List<Map<Integer, String>> datas = new ArrayList<Map<Integer, String>>();
		try{
			Sheet sheet = wb.getSheetAt(0);
			
			int rowCount = sheet.getLastRowNum();
			if(rowCount > 0){
				Row row = sheet.getRow(0);
				int colCount = row.getLastCellNum();
				if(colCount > 0){
					for(int r = 0; r < rowCount; ++r){
						row = sheet.getRow(r);
						Map<Integer, String> currentRow = new HashMap<Integer, String>();
						for(int col = 0; col < colCount; ++col){
							currentRow.put(col, getCellValueString(row, col));
						}
						datas.add(currentRow);
					}
				}
			}
		}catch(Exception e){
			throw e;
		}finally{
			wb.close();
		}
		
		return datas;
	}
	
	public List<Map<Integer, String>> loadFile(File file) throws Exception{
		String suffix = FileUtil.getSuffix(file.getPath()).toLowerCase();
		if("xlsx".equals(suffix)){
			return this.loadFileXlsx(file);
		}else if("xls".equals(suffix)){
			return this.loadFileXls(file);
		}else{
			try{
				return this.loadFileXlsx(file);
			}catch(Exception e){
				return this.loadFileXls(file);
			}
		}
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
			String val = cell.toString();
			if(val.isEmpty()){
				return 0;
			}
			return Double.parseDouble(val);
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

}
