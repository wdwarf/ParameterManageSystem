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
<link href="<%=basePath%>styles/snmpProject.css" rel="stylesheet" type="text/css" />
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
<script type='text/javascript' src='<%=basePath%>dwr/interface/snmpStructService.js?version=<%=Math.random() %>'></script>
<script type='text/javascript' src='<%=basePath%>dwr/interface/checkPointService.js?version=<%=Math.random() %>'></script>
<script type='text/javascript' src='<%=basePath%>dwr/interface/userService.js?version=<%=Math.random() %>'></script>
<script type="text/javascript" src="<%=basePath%>scripts/jquery/jquery.js"></script>
<script type="text/javascript" src="<%=basePath%>scripts/jquery/ui/jquery-ui.js"></script>
<script type="text/javascript" src="<%=basePath%>scripts/common.js"></script>
<script type="text/javascript" src="<%=basePath%>scripts/snmpProject.js"></script>
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
	<a class="button" href="<%=basePath%>snmp/snmpStructExport?projectId=${projectId }">导出</a>
	<s:if test="#isProjectOwer==true">
	<a class="button" href="javascript:void(0)" onclick="showImportDlg();">导入</a>
	</s:if>
	<a class="button" href="<%=basePath%>snmp/snmpProjectAllStructsPreview?projectId=${projectId }" target="blank">概览</a>
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
		<div id="div_structListSearch" style="position: relative;" title="按OID或名称检索">
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
			<a href="javascript:void(0)" onclick="showCompareResultDlg();" id="btnCompareResult" title="存在参数配置不一致问题,点击查看" style="float: left; display: none; height:100%; line-height: 100%;">
				<img style="border:none;" src="../images/alarm_red.gif" /><span style="color:red;"></span></a>
			<s:if test="#isProjectOwer==true">
			<a href="javascript:void(0)" class="button" onclick="resetStructInfo();">复位</a>
			<!-- <a href="javascript:void(0)" class="button" onclick="matchWithDBStruct();">匹配DB参数</a> -->
			<a href="javascript:void(0)" class="button" onclick="addNewStruct();">新增</a>
			<a href="javascript:void(0)" class="button" onclick="updateStruct();">保存</a>
			<a href="javascript:void(0)" class="button alert" onclick="deleteStruct();">删除</a>
			</s:if>
		</div>
		<div id="divStructBaseInfo" style="margin-bottom: 2px;">
			<input type="hidden" id="snmpstructId" />
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
							<s:if test="#isProjectOwer==true">
							<th>操作</th>
							</s:if>
						</tr>
					</thead>
					<tbody>
						<tr class="tempRow">
							<td class="tbRowNum">1</td>
							<td><input type="hidden" value="" name="snmpstructmemberId" /><span field="oid"></span></td>
							<td><span field="elementName"></span></td>
							<td><input type="checkbox" name="isWritable" onclick="return false;" ondblclick="return false;" disabled="disabled" /></td>
							<td><input type="checkbox" name="isPrimaryKey" onclick="return false;" ondblclick="return false;" disabled="disabled" onchange="cbPrimaryKeyStateChange(this);" /></td>
							<td><span field="dataType"></span></td>
							<td><span field="structName"></span></td>
							<td><span field="memberName"></span></td>
							<s:if test="#isProjectOwer==true">
							<td style="white-space: pre;"><a class="button" href="javascript:void(0)"
								onclick="editRow(this);">编辑</a>&nbsp;<a class="button alert"
								href="javascript:void(0)" onclick="deleteRow(this);">删除</a></td>
							</s:if>
						</tr>
					</tbody>
					
				</table>
			</div>
			<s:if test="#isProjectOwer==true">
			<div>
				<a href="javascript:void(0)" id="btnAdd" class="button" onclick="addNewEmptyRow();">+新成员</a>
			</div>
			</s:if>
	</div>
</div>

<s:if test="#isProjectOwer==true">
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
</s:if>

<!-- 导入对话框 -->
<div id="importDlg" style="display: none;width: 100%;">
<div><span style="color: orange;font-size:12px;">SnmpDataModel.xml</span></div>
<div><span style="color: red;font-size:12px;">注意：当前所有SNMP参数会被清空！</span></div>
<div>
	<form id="importStructForm" action="<%=basePath%>snmp/snmpStructImport" method="post" enctype="multipart/form-data">
		<input type="hidden" name="projectId" value="${projectId }" />
		<div><input type="file" name="file" id="importZipFile" style="width: 100%;" ></input></div>
	</form>
</div>
</div>

<!-- 导出对话框 -->
<div id="exportDlg" style="display: none;width: 100%; height: 100% !important; text-align:center" class="ui-widget-content">
	<table style="width: 100% !important; height: 100%; text-align:center; vertical-align: middle;">
		<tr><td>
		<a class="ui-button ui-corner-all" href="<%=basePath%>db/dbStructExportDB?projectId=${projectId }" onclick="">导出DB</a>
		<a class="ui-button ui-corner-all" href="<%=basePath%>db/dbStructExportExcel?projectId=${projectId }" onclick="">导出EXCEL</a>
		</td></tr>
	</table>
</div>

<!-- 还原点对话框 -->
	<div id="checkPointDlg" style="display: none;">
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