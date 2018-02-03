<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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
<title>Parameter Manage System</title>
<link rel="shortcut icon" type="image/x-icon" href="<%=basePath%>images/icon.ico" />
<link href="<%=basePath%>styles/common.css" rel="stylesheet" type="text/css" />
<link href="<%=basePath%>styles/tr069Project.css" rel="stylesheet" type="text/css" />
<link href="<%=basePath%>scripts/jquery/ui/jquery-ui.css" rel="stylesheet" type="text/css" />
<link href="<%=basePath%>scripts/jquery/ui/jquery-ui.theme.css" rel="stylesheet" type="text/css" />
<style type="text/css">
#editRowDlg table{
width: 100%;
}

#editRowDlg table input{
width: 100%;
}

.editRowElmtLabel{
width: 50px;
font-weight: bold;
background-color: #88bbee;
}

</style>
<script type='text/javascript' src='<%=basePath%>dwr/engine.js'></script>
<script type='text/javascript' src='<%=basePath%>dwr/interface/projectService.js?version=<%=Math.random() %>'></script>
<script type='text/javascript' src='<%=basePath%>dwr/interface/dbStructService.js?version=<%=Math.random() %>'></script>
<script type='text/javascript' src='<%=basePath%>dwr/interface/tr069StructService.js?version=<%=Math.random() %>'></script>
<script type='text/javascript' src='<%=basePath%>dwr/interface/userService.js?version=<%=Math.random() %>'></script>
<script type="text/javascript" src="<%=basePath%>scripts/jquery/jquery.js"></script>
<script type="text/javascript" src="<%=basePath%>scripts/jquery/ui/jquery-ui.js"></script>
<script type="text/javascript" src="<%=basePath%>scripts/common.js"></script>
<script type="text/javascript" src="<%=basePath%>scripts/tr069Project.js"></script>
<script type="text/javascript" src="<%=basePath%>scripts/userMgmt.js"></script>

<script type="text/javascript">
var $j = jQuery.noConflict();

$j(function(){
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
<div id="actionErrorDiv" style="display:none;" class="alert">
<s:actionerror />
</div>

<div class="btnPanel">
	<!-- <a class="button" href="<%=basePath%>fileTrans?projectId=${projectId }" onclick="showExportDlg();">导出</a> -->
	<a class="button" href="<%=basePath%>tr069/tr069StructExport?projectId=${projectId }">导出</a>
	<a class="button" href="#" onclick="showImportDlg();">导入</a>
	<a class="button" href="<%=basePath%>tr069/tr069ProjectAllStructsPreview?projectId=${projectId }" target="blank">概览</a>
</div>

<div class="structsEditPanel">

	<!-- 结构体列表 -->
	<div id="structListPanel" class="structListPanel">
		<div class="structListPanelTitle">
			<span>结构体列表</span><span id="spanStructCount"></span>
		</div>
		<div id="div_structListSearch" style="position: relative;">
			<img src="<%=basePath %>images/search.png" style="position: absolute; right: 0px; height: 100%;" />
			<input type="text" id="structListSearchEdit" />
		</div>
		<div id="div_structList">
			<ul id="structList">
			</ul>
		</div>
	</div>
	<!-- end of 结构体列表 -->

	<div id="structEditPanel" class="structEditPanel">
		<div style="text-align: right;margin: 2px 0px;">
			<a href="#" onclick="showCompareResultDlg();" id="btnCompareResult" title="存在参数配置不一致问题,点击查看" style="float: left; display: none; height:100%; line-height: 100%;">
				<img style="border:none;" src="../images/alarm_red.gif" /><span style="color:red;"></span></a>
			<!-- <a href="#" class="button" onclick="matchWithDBStruct();">匹配DB参数</a> -->
			<a href="#" class="button" onclick="addNewStruct();">新增</a>
			<a href="#" class="button" onclick="updateStruct();">保存</a>
			<a href="#" class="button alert" onclick="deleteStruct();">删除</a>
		</div>
		<div id="divStructBaseInfo" style="margin-bottom: 2px;">
			<input type="hidden" id="tr069structId" />
			<label for="structNameEdit">MibStructName:</label><input id="structNameEdit" type="text" class="text ui-widget-content ui-corner-all" />
			<label for="oidEdit">OID:</label><input id="oidEdit" type="text" class="text ui-widget-content ui-corner-all" style="width: 18em" />
			<input id="isSingleTableCheckbox" type="checkbox" /><label for="isSingleTableCheckbox">SingleTable</label> 
		</div>
		<div>
				<table class="tbCommon" id="structDetailTable" style="white-space: nowrap;">
					<thead>
						<tr>
							<th></th>
							<th>OID</th>
							<th>MibMemberName</th>
							<th>IsWritable</th>
							<th>IsPrimaryKey</th>
							<th>DataType</th>
							<th>StructName</th>
							<th>MemberName</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody>
						<tr class="tempRow">
							<td class="tbRowNum">1</td>
							<td><input type="hidden" value="" name="tr069structmemberId" /><span field="oid"></span></td>
							<td><span field="elementName"></span></td>
							<td><input type="checkbox" name="isWritable" onclick="return false;" ondblclick="return false;" disabled="disabled" /></td>
							<td><input type="checkbox" name="isPrimaryKey" onclick="return false;" ondblclick="return false;" disabled="disabled" onchange="cbPrimaryKeyStateChange(this);" /></td>
							<td><span field="dataType"></span></td>
							<td><span field="structName"></span></td>
							<td><span field="memberName"></span></td>
							<td style="white-space: pre;"><a class="button" href="#"
								onclick="editRow(this);">编辑</a>&nbsp;<a class="button alert"
								href="#" onclick="deleteRow(this);">删除</a></td>
						</tr>
					</tbody>
					
				</table>
			</div>
			<div>
				<a href="#" id="btnAdd" class="button" onclick="addNewEmptyRow();">+新成员</a>
			</div>
	</div>
</div>

<!-- 编辑对话框 -->
<div id="editRowDlg" style="display: none;">
<table class="tbCommon">
	<tr><td class="editRowElmtLabel">OID</td><td><input type="text" name="oid" /></td></tr>
	<tr><td class="editRowElmtLabel">MibMemberName</td><td><input type="text" name="elementName" /></td></tr>
	<tr><td class="editRowElmtLabel">IsWritable</td><td><input type="checkbox" name="isWritable" /></td></tr>
	<tr><td class="editRowElmtLabel">IsPrimaryKey</td><td><input type="checkbox" name="isPrimaryKey" /></td></tr>
			<tr>
				<td class="editRowElmtLabel">DataType</td>
				<td><select name="dataType" id="dataTypeSelect">
					<option value="OCTETS">OCTETS</option>
					<option value="UINT32">UINT32</option>
					<option value="INT32">INT32</option>
				</select></td>
			</tr>
	<tr><td class="editRowElmtLabel">StructName</td><td><input type="text" name="structName" /></td></tr>
	<tr><td class="editRowElmtLabel">MemberName</td><td><input type="text" name="memberName" /></td></tr>
</table>
</div>

<!-- 导入对话框 -->
<div id="importDlg" style="display: none;width: 100%;">
<div><span style="color: orange;font-size:12px;">DataModel.xml</span></div>
<div><span style="color: red;font-size:12px;">注意：当前所有Tr069参数会被清空！</span></div>
<div>
	<form id="importStructForm" action="<%=basePath%>tr069/tr069StructImport" method="post" enctype="multipart/form-data">
		<input type="hidden" name="projectId" value="${projectId }" />
		<div><input type="file" name="file" id="importZipFile" style="width: 100%;" ></input></div>
	</form>
</div>
</div>

</body>
</html>