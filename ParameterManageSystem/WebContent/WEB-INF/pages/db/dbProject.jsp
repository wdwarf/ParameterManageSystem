<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.parammgr.db.entity.User" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	response.setHeader("Cache-Control", "no-cache");
%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<s:set var="isProjectOwer" value="#session.user.userName=='admin' || ((null!=project.user && null!=#session.user) && #session.user.userName==project.user.userName)"></s:set>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="Cache-Control" content="no-cache" />
<title>${systemName } - DB</title>
<link rel="shortcut icon" type="image/x-icon" href="<%=basePath%>images/icon.ico" />
<link href="<%=basePath%>styles/common.css" rel="stylesheet" type="text/css" />
<link href="<%=basePath%>styles/dbProject.css" rel="stylesheet" type="text/css" />
<link href="<%=basePath%>scripts/jquery/ui/jquery-ui.css" rel="stylesheet" type="text/css" />
<link href="<%=basePath%>scripts/jquery/ui/jquery-ui.theme.css" rel="stylesheet" type="text/css" />
<style type="text/css">
#editRowDlg table {
	width: 100%;
}

#editRowDlg table input {
	width: 100%;
}

.editRowElmtLabel {
	width: 50px;
	font-weight: bold;
	background-color: #aee3fb;
	background: -webkit-gradient(linear, 0 0, 0 bottom, from(#daedff), to(#aee3fb));
	background: -moz-linear-gradient(top, #daedff, #aee3fb);
}

#checkPointTable td{
	padding: 0px 2px !important;
}
</style>
<script type='text/javascript' src='<%=basePath%>dwr/engine.js'></script>
<script type='text/javascript' src='<%=basePath%>dwr/interface/projectService.js?version=<%=Math.random() %>'></script>
<script type='text/javascript' src='<%=basePath%>dwr/interface/dbStructService.js?version=<%=Math.random() %>'></script>
<script type='text/javascript' src='<%=basePath%>dwr/interface/checkPointService.js?version=<%=Math.random() %>'></script>
<script type='text/javascript' src='<%=basePath%>dwr/interface/userService.js?version=<%=Math.random() %>'></script>
<script type="text/javascript" src="<%=basePath%>scripts/jquery/jquery.js"></script>
<script type="text/javascript" src="<%=basePath%>scripts/jquery/ui/jquery-ui.js"></script>
<script type="text/javascript" src="<%=basePath%>scripts/common.js"></script>
<script type="text/javascript" src="<%=basePath%>scripts/dbProject.js"></script>
<script type="text/javascript" src="<%=basePath%>scripts/userMgmt.js"></script>

<script type="text/javascript">
var $j = jQuery.noConflict();

$j(function(){
	var projectId = $j("#projectId").val();
	
	if("" != $j("#actionErrorDiv").text().trim()){
		$j("#actionErrorDiv").dialog({
			title: "Error",
			modal: true,
			buttons: [
				{
					text: "OK",
					click: function(){
						$j(this).dialog("close");
					}
				}
			]
		});
	}else{
		
	}
	
});

</script>

</head>
<body>

	<input type="hidden" id="projectId" value="${projectId }" />
	<div id="actionErrorDiv" style="display: none;" class="alert">
		<s:actionerror />
	</div>

	<div class="btnPanel">
	<!-- <a class="button" href="<%=basePath%>fileTrans?projectId=${projectId }" onclick="showExportDlg();">导出</a> -->
	<a class="button" href="javascript:void(0)" onclick="showExportDlg();">导出</a>
	<s:if test="#isProjectOwer==true">
	<a class="button" href="javascript:void(0)" onclick="showImportDlg();">导入</a>
	</s:if>
	<a class="button" href="<%=basePath%>db/dbProjectAllStructsPreview?projectId=${projectId }" target="blank">概览</a>
	<s:if test="#isProjectOwer==true">
	<a class="button" href="javascript:void(0);" onclick="showCheckPointDlg()">还原点</a>
	</s:if>
</div>

	<div class="structsEditPanel">

		<!-- 结构体列表 -->
		<div id="structListPanel" class="structListPanel">
			<div class="structListPanelTitle">
				<span>结构体列表</span><span id="spanStructCount"></span>
			</div>
			<div id="div_structListSearch" style="position: relative;"
				title="按ID或名称检索">
				<img src="<%=basePath %>images/search.png"
					style="position: absolute; right: 0px; height: 100%;" /> <input
					type="text" id="structListSearchEdit" />
			</div>
			<div id="div_structList">
				<ul id="structList">
				</ul>
			</div>
		</div>
		<!-- end of 结构体列表 -->

		<div id="structEditPanel" class="structEditPanel">
			<div style="text-align: right; margin: 2px 0px;">
				<s:if test="#isProjectOwer==true">
					<a href="javascript:void(0)" class="button"
						onclick="resetStructInfo();">复位</a>
				</s:if>
				<a href="javascript:void(0)" class="button"
					onclick="defaultValuesEdit();">默认实例</a>
				<s:if test="#isProjectOwer==true">
					<a href="javascript:void(0)" class="button"
						onclick="addNewStruct();">新增</a>
					<a href="javascript:void(0)" class="button"
						onclick="updateStruct();">保存</a>
					<a href="javascript:void(0)" class="button alert"
						onclick="deleteStruct();">删除</a>
				</s:if>
			</div>
			<div id="divStructBaseInfo" style="margin-bottom: 2px;">
				<input type="hidden" id="dbstructId" /> <label for="structNameEdit">Struct
					Name:</label><input id="structNameEdit" name="structName" type="text"
					class="text ui-widget-content ui-corner-all" /> <label
					for="structIdEdit">Struct ID:</label><input id="structIdEdit"
					name="structId" type="text"
					class="text ui-widget-content ui-corner-all" /> <input
					id="cbIsTempTable" name="tempTable" type="checkbox" /><label
					for="cbIsTempTable">Temp Table</label>
			</div>
			<div>
				<table class="tbCommon" id="structDetailTable">
					<thead>
						<tr>
							<th></th>
							<th style="width: 15px;">MemberId</th>
							<th>MemberName</th>
							<th style="width: 15px;">IsPrimaryKey</th>
							<th style="width: 15px;">ValueType</th>
							<th style="width: 15px;">ValueSize</th>
							<th style="width: 15px;">DefaultValue</th>
							<th>ValueRegex</th>
							<th style="width: 15px;">RefStruct</th>
							<th style="width: 15px;">RefMember</th>
							<th style="width: 15px;">IsUnique</th>
							<s:if test="#isProjectOwer==true">
							<th>操作</th>
							</s:if>
						</tr>
					</thead>
					<tbody>
						<tr class="tempRow">
							<td class="tbRowNum">1</td>
							<td>
								<!-- 隐藏字段 --> <input type="hidden" value=""
								name="dbstructmemberId" /> <input type="hidden" value=""
								name="memo" /> <!-- end of 隐藏字段 --> <input type="text" value=""
								name="memberId" />
							</td>
							<td><input type="text" value="" name="memberName" /></td>
							<td><input type="checkbox" name="isPrimaryKey"
								onchange="cbPrimaryKeyStateChange(this);" /></td>
							<td><select class="valueType" name="valueType"
								onchange="valueTypeChanged(this);">
									<option>Char</option>
									<option>Boolean</option>
									<option>Uint64</option>
									<option>Uint32</option>
									<option>Uint16</option>
									<option>Uint8</option>
									<option>Int32</option>
									<option>Int64</option>
									<option>Int16</option>
									<option>Int8</option>
							</select></td>
							<td><input type="text" value="1" name="valueSize" /></td>
							<td><input type="text" name="defaultValue" /></td>
							<td><input type="text" name="valueRegex" /></td>
							<td><input type="text" name="refStruct" /></td>
							<td><input type="text" name="refMember" /></td>
							<td><input type="checkbox" name="isUnique" /></td>
							<s:if test="#isProjectOwer==true">
							<td style="white-space: pre;"><a class="button"
								href="javascript:void(0)" onclick="editRow(this);">编辑</a>&nbsp;<a class="button alert" href="javascript:void(0)" onclick="deleteRow(this);">删除</a></td>
							</s:if>
						</tr>
					</tbody>
				</table>
			</div>
			<s:if test="#isProjectOwer==true">
			<div>
				<a href="javascript:void(0)" id="btnAdd" class="button"
					onclick="addNewEmptyRow();">+新成员</a>
			</div>
			</s:if>
			<div></div>
		</div>
	</div>
<s:if test="#isProjectOwer==true">
<!-- 编辑对话框 -->
<div id="editRowDlg" style="display: none;">
<table class="tbCommon"  style="width: 100%;">
	<tr><td class="editRowElmtLabel">memberId</td><td><input type="text" name="memberId" /></td></tr>
	<tr><td class="editRowElmtLabel">memberName</td><td><input type="text" name="memberName" /></td></tr>
	<tr><td class="editRowElmtLabel">IsPrimaryKey</td><td><input type="checkbox" name="isPrimaryKey" /></td></tr>
	<tr><td class="editRowElmtLabel">ValueType</td><td><select class="valueType" name="valueType" onchange="valueTypeChanged(this);">
								<option>Char</option>
								<option>Boolean</option>
								<option>Uint64</option>
								<option>Uint32</option>
								<option>Uint16</option>
								<option>Uint8</option>
								<option>Int32</option>
								<option>Int64</option>
								<option>Int16</option>
								<option>Int8</option>
						</select></td></tr>
	<tr><td class="editRowElmtLabel">ValueSize</td><td><input type="text" name="valueSize" /></td></tr>
	<tr><td class="editRowElmtLabel">DefaultValue</td><td><input type="text" name="defaultValue" /></td></tr>
	<tr><td class="editRowElmtLabel">ValueRegex</td><td><input type="text" name="valueRegex" /></td></tr>
	<tr><td class="editRowElmtLabel">RefStruct</td><td><input type="text" name="refStruct" /></td></tr>
	<tr><td class="editRowElmtLabel">RefMember</td><td><input type="text" name="refMember" /></td></tr>
	<tr><td class="editRowElmtLabel">IsUnique</td><td><input type="checkbox" name="isUnique" /></td></tr>
	<tr><td class="editRowElmtLabel">说明</td><td><textarea name="memo"></textarea> </td></tr>
</table>
</div>
</s:if>

	<!-- 导入对话框 -->
	<div id="importDlg" style="display: none; width: 100%;">
		<div>
			<span style="color: orange; font-size: 12px;">将参数的"db"目录打包为一个.zip文件上传即可。</span>
		</div>
		<div>
			<span style="color: red; font-size: 12px;">注意：当前所有DB参数会被清空！</span>
		</div>
		<div>
			<form id="importStructForm" action="<%=basePath%>db/dbStructImport"
				method="post" enctype="multipart/form-data">
				<input type="hidden" name="projectId" value="${projectId }" />
				<div>
					<input type="file" name="zipFile" id="importZipFile"
						style="width: 100%;"></input>
				</div>
			</form>
		</div>
	</div>

	<!-- 导出对话框 -->
	<div id="exportDlg"
		style="display: none; width: 100%; height: 100% !important; text-align: center"
		class="ui-widget-content">
		<a class="ui-button ui-corner-all"
			href="<%=basePath%>db/dbStructExportDB?projectId=${projectId }"
			onclick="">导出DB</a> <a class="ui-button ui-corner-all"
			href="<%=basePath%>db/dbStructExportExcel?projectId=${projectId }"
			onclick="">导出EXCEL</a>
	</div>

	<!-- 默认实例对话框 -->
	<div id="defaultValueEditDlg" style="display: none;">
		<div>
			<s:if test="#isProjectOwer==true">
			<a class="button" style="color: white;" href="javascript:void(0)"
				onclick="addNewInstance();">+实例</a>
			</s:if>
		</div>
		<div class="">
			<table id="tbDefaultInstance" class="tbCommon">
			</table>
		</div>
	</div>

	<!-- 还原点对话框 -->
	<div id="checkPointDlg" style="display: none; width:100%;">
		<fieldset style="background-color: #aaddff;">
			<legend>创建还原点</legend>
			<div>还原点描述：</div>
			<div>
				<textarea rows="3" name="checkPointDescription"
					id="checkPointDescription" style="width: 100%;"></textarea>
			</div>
			<div style="text-align: right;">
				<a class="button" href="javascript:void(0)"
					onclick="createCheckPoint();">创建一个还原点</a>
			</div>
		</fieldset>

		<fieldset style="background-color: white;">
			<legend>还原点</legend>
			<div>
				<table class="tbCommon" id="checkPointTable">
					<thead>
						<tr>
							<th style="width: 32px;">时间</th>
							<th>描述</th>
							<th style="width: 32px;">操作</th>
						</tr>
					</thead>
					<tbody></tbody>
				</table>
			</div>
		</fieldset>
	</div>

</body>
</html>