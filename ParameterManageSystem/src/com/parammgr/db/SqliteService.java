package com.parammgr.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.parammgr.db.entity.DBStruct;
import com.parammgr.db.entity.DBStructDefInstance;
import com.parammgr.db.entity.DBStructMember;

public class SqliteService {
	static {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Connection createDBFile(String dbFilePath) throws Exception {
		Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbFilePath);
		Statement stmt = connection.createStatement();
		stmt.executeUpdate("PRAGMA foreign_keys = ON;");
		stmt.executeUpdate(
				"CREATE TABLE struct_dictionary([StructID] INTEGER NOT NULL, StructName TEXT NOT NULL, IsTempTable TEXT NOT NULL, MemberID INTEGER NOT NULL, MemberName TEXT NOT NULL, IsPrimaryKey INTEGER NOT NULL, RefStruct TEXT NOT NULL, RefMember TEXT NOT NULL, UniqueFlag TEXT NOT NULL, ValueType TEXT NOT NULL, DefaultValue TEXT NOT NULL, ValueRegex TEXT NOT NULL)");
		/*
		 * stmt.setQueryTimeout(30);
		 * stmt.executeUpdate("PRAGMA foreign_keys = ON;");
		 * stmt.executeUpdate("drop table if exists tb_test"); stmt.
		 * executeUpdate("create table tb_test(t_id int primary key, t_name varchar(50) not null)"
		 * ); stmt.executeUpdate("insert into tb_test values(1, 'test_name')");
		 * ResultSet rs = stmt.executeQuery("select * from tb_test");
		 * while(rs.next()){ System.out.println("id: " + rs.getString("t_id"));
		 * System.out.println("name: " + rs.getString("t_name")); }
		 */
		return connection;
	}

	private void createStructDictionary(Connection connection, List<DBStruct> structs) throws Exception {
		Statement stmt = connection.createStatement();
		stmt.executeUpdate(
				"CREATE TABLE struct_dictionary([StructID] INTEGER NOT NULL, StructName TEXT NOT NULL, IsTempTable TEXT NOT NULL, MemberID INTEGER NOT NULL, MemberName TEXT NOT NULL, IsPrimaryKey INTEGER NOT NULL, RefStruct TEXT NOT NULL, RefMember TEXT NOT NULL, UniqueFlag TEXT NOT NULL, ValueType TEXT NOT NULL, DefaultValue TEXT NOT NULL, ValueRegex TEXT NOT NULL)");
		for (DBStruct struct : structs) {
			for (DBStructMember member : struct.getMembers()) {
				PreparedStatement pstmt = connection
						.prepareStatement("INSERT INTO struct_dictionary VALUES(?,?,?,?,?,?,?,?,?,?,?,?)");
				pstmt.setInt(1, struct.getStructId());
				pstmt.setString(2, struct.getStructName());
				pstmt.setBoolean(3, struct.isTempTable());

				pstmt.setInt(4, (struct.getStructId() * 1000 + member.getMemberId()));
				pstmt.setString(5, member.getMemberName());
				pstmt.setBoolean(6, member.isPrimaryKey());
				pstmt.setString(7, member.getRefStruct());
				pstmt.setString(8, member.getRefMember());
				pstmt.setBoolean(9, member.isUnique());
				String valueType = "";
				switch (member.getMemberType()) {
				case "Char": {
					valueType = "Char[" + member.getMemberSize() + "]";
					break;
				}
				default: {
					valueType = member.getMemberType();
					break;
				}
				}
				pstmt.setString(10, valueType);
				pstmt.setString(11, member.getDefaultValue());
				pstmt.setString(12, member.getValueRegex());

				pstmt.executeUpdate();
			}
		}
	}

	/**
	 * 创建结构体的表
	 * @param connection
	 * @param structs
	 * @throws Exception
	 */
	private void createStructTable(Connection connection, List<DBStruct> structs) throws Exception {
		for (DBStruct struct : structs) {

			System.out.println("creating table: " + struct.getStructName());
			
			String tempTableStr = "";
			if(struct.isTempTable()){
				tempTableStr = " TEMP";
			}

			String sqlStr = "CREATE" + tempTableStr + " TABLE [" + struct.getStructName() + "](";
			String primaryKeyStr = "";
			String foreignKeyStr = "";
			for (DBStructMember member : struct.getMembers()) {
				if ('(' != sqlStr.charAt(sqlStr.length() - 1)) {
					sqlStr += ",";
				}
				sqlStr += "[" + member.getMemberName() + "]";

				String typeStr = "";
				switch (member.getMemberType()) {
				case "Char": {
					typeStr = "TEXT";
					break;
				}
				default: {
					typeStr = "INTEGER";
					break;
				}
				}

				sqlStr += " " + typeStr;
				if (member.isPrimaryKey()) {
					if (!primaryKeyStr.isEmpty()) {
						primaryKeyStr += ",";
					}
					primaryKeyStr += "[" + member.getMemberName() + "]";
				}
				
				if(member.isUnique()){
					sqlStr += " UNIQUE";
				}
				
				if(!member.getRefStruct().isEmpty() && !member.getRefMember().isEmpty()){
					foreignKeyStr += ", FOREIGN KEY ([" + member.getMemberName() + "]) REFERENCES [" + member.getRefStruct() + "]([" + member.getRefMember() + "])";
				}
				
				if("Char".equals(member.getMemberType()) || !member.getDefaultValue().isEmpty()){
					sqlStr += " DEFAULT '" + member.getDefaultValue() + "'";
				}
			}

			sqlStr += foreignKeyStr;
			if (!primaryKeyStr.isEmpty()) {
				sqlStr += ", PRIMARY KEY(" + primaryKeyStr + ")";
			}
			sqlStr += ")";

			System.out.println(sqlStr);

			try{
				if (struct.isTempTable()) {
					PreparedStatement pstmt = connection.prepareStatement("INSERT INTO temp_table_info VALUES(?)");
					pstmt.setString(1, sqlStr);
					pstmt.executeUpdate();
					
					if(struct.isSingleInstance()){
						pstmt = connection.prepareStatement("INSERT INTO temp_table_info VALUES(?)");
						pstmt.setString(1, "INSERT INTO [" + struct.getStructName() + "] DEFAULT VALUES");
						pstmt.executeUpdate();
					}
				} else {
					Statement stmt = connection.createStatement();
					stmt.executeUpdate(sqlStr);
				}
			}catch(Exception e){
				String errInfo = "creating table [" + struct.getStructName() + "] failed: " + e.getMessage();
				throw new Exception(errInfo);
			}
		}
	}

	/**
	 * 创建结构体的默认实例
	 * @param connection
	 * @param structs
	 * @throws Exception
	 */
	private void createStructDefaultInstance(Connection connection, List<DBStruct> structs) throws Exception {
		for (DBStruct struct : structs) {
			if (struct.isTempTable())
				continue;

			try{
				if (struct.isSingleInstance()) {
					/*
					String sqlStr = "INSERT INTO " + struct.getStructName() + "(";

					String fieldStr = "";
					String placeHolderStr = "";
					for (DBStructMember member : struct.getMembers()) {
						if (!fieldStr.isEmpty()) {
							fieldStr += ",";
						}
						fieldStr += "[" + member.getMemberName() + "]";

						if (!placeHolderStr.isEmpty()) {
							placeHolderStr += ",";
						}
						placeHolderStr += "?";
					}
					sqlStr += fieldStr + ") VALUES(" + placeHolderStr + ")";

					//System.out.println(sqlStr);
					PreparedStatement pstmt = connection.prepareStatement(sqlStr);
					int fieldIndex = 1;
					for (DBStructMember member : struct.getMembers()) {
						pstmt.setString(fieldIndex, member.getDefaultValue());
						++fieldIndex;
					}
					pstmt.executeUpdate();
					*/
					PreparedStatement pstmt = connection.prepareStatement("INSERT INTO [" + struct.getStructName() + "] DEFAULT VALUES");
					pstmt.executeUpdate();
				} else {
					int instanceCount = 0;
					Map<String, List<DBStructDefInstance>> instanceMap = new HashMap<String, List<DBStructDefInstance>>();
					for (DBStructMember member : struct.getMembers()) {
						if (!member.getDefInstances().isEmpty()) {
							instanceCount = member.getDefInstances().size();
							instanceMap.put(member.getMemberName(), member.getDefInstances());
						}
					}
					for (int i = 0; i < instanceCount; ++i) {
						String sqlStr = "INSERT INTO [" + struct.getStructName() + "](";
						Set<String> memberNames = instanceMap.keySet();
						String fieldStr = "";
						String placeHolderStr = "";
						for (String memberName : memberNames) {
							if (instanceMap.get(memberName).get(i).getDefValue().isEmpty())
								continue;
							if (!fieldStr.isEmpty()) {
								fieldStr += ",";
							}
							fieldStr += "[" + memberName + "]";

							if (!placeHolderStr.isEmpty()) {
								placeHolderStr += ",";
							}
							placeHolderStr += "?";
						}

						sqlStr += fieldStr + ") VALUES(" + placeHolderStr + ")";
						//System.out.println(sqlStr);
						
						PreparedStatement pstmt = connection.prepareStatement(sqlStr);
						int fieldIndex = 1;
						for (String memberName : memberNames) {
							String defValue = instanceMap.get(memberName).get(i).getDefValue();
							if (defValue.isEmpty())
								continue;
							pstmt.setString(fieldIndex, defValue);
							++fieldIndex;
						}
						pstmt.executeUpdate();
					}
				}
			}catch(Exception e){
				String errInfo = "creating default instance of [" + struct.getStructName() + "] failed: " + e.getMessage();
				throw new Exception(errInfo);
			}
		}
	}

	public void createOamDB(String dbFilePath, List<DBStruct> structs) throws Exception {
		Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbFilePath);
		
		try{
			Statement stmt = connection.createStatement();
			stmt.executeUpdate("PRAGMA foreign_keys = ON;");
			connection.setAutoCommit(false);

			stmt.executeUpdate("CREATE TABLE temp_table_info([SQL] TEXT NOT NULL)");
			createStructDictionary(connection, structs);
			createStructTable(connection, structs);
			createStructDefaultInstance(connection, structs);

			connection.commit();
		}catch(Exception e){
			connection.rollback();
			throw e;
		}
		
		connection.close();
	}
}
