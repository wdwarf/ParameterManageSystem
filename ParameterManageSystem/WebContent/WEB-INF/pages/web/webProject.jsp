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
<title>${systemName } - WEB</title>
<link rel="shortcut icon" type="image/x-icon" href="<%=basePath%>images/icon.ico" />
<link href="<%=basePath%>styles/common.css" rel="stylesheet" type="text/css" />
<link href="<%=basePath%>styles/webProject.css" rel="stylesheet" type="text/css" />
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
<script type='text/javascript' src='<%=basePath%>dwr/interface/webStructService.js?version=<%=Math.random() %>'></script>
<script type='text/javascript' src='<%=basePath%>dwr/interface/checkPointService.js?version=<%=Math.random() %>'></script>
<script type='text/javascript' src='<%=basePath%>dwr/interface/userService.js?version=<%=Math.random() %>'></script>
<script type="text/javascript" src="<%=basePath%>scripts/jquery/jquery.js"></script>
<script type="text/javascript" src="<%=basePath%>scripts/jquery/ui/jquery-ui.js"></script>
<script type="text/javascript" src="<%=basePath%>scripts/common.js"></script>
<script type="text/javascript" src="<%=basePath%>scripts/webProject.js"></script>
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
	<a class="button" href="javascript:void(0)" onclick="showExportDlg();">导出</a>
	<s:if test="#isProjectOwer==true">
	<a class="button" href="javascript:void(0)" onclick="showImportDlg();">导入</a>
	</s:if>
	<a class="button" href="<%=basePath%>web/webProjectAllStructsPreview?projectId=${projectId }" target="blank">概览</a>
	<s:if test="#isProjectOwer==true">
	<a class="button" href="javascript:void(0);" onclick="showCheckPointDlg()">还原点</a>
	</s:if>
	<a class="button" href="<%=basePath%>web/compareWithDbStructs?projectId=${projectId }" target="blank">DB参数差异对比</a>
</div>

<div class="structsEditPanel">

	<!-- 结构体列表 -->
	<div id="structListPanel" class="structListPanel">
		<div class="structListPanelTitle">
			<span>结构体列表</span><span id="spanStructCount"></span>
		</div>
		<div id="div_structListSearch" style="position: relative;" title="按ID或名称检索">
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
			<a href="javascript:void(0)" class="button" onclick="matchWithDBStructById();">按<span style="color:orange;">ID</span>匹配DB参数</a>
			<a href="javascript:void(0)" class="button" onclick="matchWithDBStructByName();">按<span style="color:orange;">名称</span>匹配DB参数</a>
			<a href="javascript:void(0)" class="button" onclick="addNewStruct();">新增</a>
			<a href="javascript:void(0)" class="button" onclick="updateStruct();">保存</a>
			<a href="javascript:void(0)" class="button alert" onclick="deleteStruct();">删除</a>
			</s:if>
		</div>
		<div id="divStructBaseInfo" style="margin-bottom: 2px;">
			<input type="hidden" id="webstructId" />
			<label for="structNameEdit">StructName:</label><input id="structNameEdit" type="text" class="text ui-widget-content ui-corner-all" />
			<label for="structIdEdit">StructID:</label><input id="structIdEdit" type="text" class="text ui-widget-content ui-corner-all" />
			<label for="structCnNameEdit">StructCnName:</label><input id="structCnNameEdit" type="text" class="text ui-widget-content ui-corner-all" />
			<label for="classifyNameEdit">ClassifyName:</label><input id="classifyNameEdit" type="text" class="text ui-widget-content ui-corner-all" /> 
		</div>
		<div>
				<table class="tbCommon" id="structDetailTable" style="white-space: nowrap;">
					<thead>
						<tr>
							<th></th>
							<th>MemberId</th>
							<th>MemberName</th>
							<th>MemberCnName</th>
							<th>IsPrimaryKey</th>
							<th>DefaultValue</th>
							<th>WebType</th>
							<th>TypeDesc</th>
							<th>WebTips</th>
							<th>SetStatus</th>
							<s:if test="#isProjectOwer==true">
							<th>操作</th>
							</s:if>
						</tr>
					</thead>
					<tbody>
						<tr class="tempRow">
							<td class="tbRowNum">1</td>
							<td><input type="hidden" value="" name="webstructmemberId" /><span field="memberId"></span></td>
							<td><span field="memberName"></span></td>
							<td><span field="memberCnName"></span></td>
							<td><input type="checkbox" name="isPrimaryKey" onclick="return false;" ondblclick="return false;" disabled="disabled" onchange="cbPrimaryKeyStateChange(this);" /></td>
							<td><span field="defaultValue"></span></td>
							<td><span field="webType"></span></td>
							<td><span field="typeDesc"></span></td>
							<td><span field="webTips"></span></td>
							<td><span field="setStatus"></span></td>
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
<div id="editRowDlg" style="display: none;">
<table class="tbCommon"  style="width: 100%;">
	<tr><td class="editRowElmtLabel">MemberId</td><td><input type="text" name="memberId" /></td></tr>
	<tr><td class="editRowElmtLabel">MemberName</td><td><input type="text" name="memberName" /></td></tr>
	<tr><td class="editRowElmtLabel">MemberCnName</td><td><input type="text" name="memberCnName" /></td></tr>
	<tr><td class="editRowElmtLabel">IsPrimaryKey</td><td><input type="checkbox" name="isPrimaryKey" /></td></tr>
	<tr><td class="editRowElmtLabel">DefaultValue</td><td><input type="text" name="defaultValue" /></td></tr>
			<tr>
				<td class="editRowElmtLabel">WebType</td>
				<td><select name="webType" id="webTypeSelect">
				</select></td>
			</tr>
	<tr><td class="editRowElmtLabel">TypeDesc</td><td><input type="text" name="typeDesc" /></td></tr>
	<tr><td class="editRowElmtLabel">单位</td><td><input type="text" name="unit" /></td></tr>
	<tr><td class="editRowElmtLabel">中文取值范围提示</td><td><input type="text" name="valueRangeCn" /></td></tr>
	<tr><td class="editRowElmtLabel">英文取值范围提示</td><td><input type="text" name="valueRange" /></td></tr>
	<tr>
		<td class="editRowElmtLabel">成员level</td>
		<td>
			<input type="text" name="memberLevel" title="隐藏该成员:100，默认:0" />
			<!--<select id="memberLevel">
				<option value="" title="空白"></option>
				<option value="0" title="">0</option>
				<option value="1" title="">1</option>
				<option value="3" title="">3 (User、Market、Root)</option>
				<option value="4" title="">4</option>
				<option value="10" title="">10</option>
				<option value="11" title="">11</option>
				<option value="12" title="">12</option>
				<option value="13" title="">13</option>
				<option value="14" title="">14</option>
				<option value="15" title="">15</option>
				<option value="16" title="">16 (User、Market、Root)</option>
				<option value="100" title="不显示">100 (不显示)</option>
			</select>-->
		</td>
	</tr>
	<tr><td class="editRowElmtLabel">结构体level</td><td><input type="text" name="structLevel" title="隐藏该结构体:100，默认:0" /></td></tr>
	<tr><td class="editRowElmtLabel">可增删</td><td><input type="checkbox" name="isCanAddOrDelete"
								onchange="" /></td></tr>
	<tr><td class="editRowElmtLabel">唯一</td><td><input type="checkbox" name="isUnique"
								onchange="" /></td></tr>
	<tr><td class="editRowElmtLabel">重启生效</td><td><input type="checkbox" name="isRebootEffective"
								onchange="" /></td></tr>
	<tr><td class="editRowElmtLabel">SetStatus</td><td><input type="text" name="setStatus" /></td></tr>
</table>
</div>
</s:if>

<!-- 导入对话框 -->
<div id="importDlg" style="display: none;width: 100%;">
<div><span style="color: orange;font-size:12px;">XML、Excel或zip</span></div>
<div><span style="color: red;font-size:12px;">注意：当前所有WEB参数会被清空！</span></div>
<div>
	<form id="importStructForm" action="<%=basePath%>web/webStructImport" method="post" enctype="multipart/form-data">
		<input type="hidden" name="projectId" value="${projectId }" />
		<div><input type="file" name="file" id="importFile" style="width: 100%;" ></input></div>
	</form>
</div>
</div>

<!-- 导出对话框 -->
<div id="exportDlg" style="display: none;width: 100%; height: 100% !important; text-align:center" class="ui-widget-content">
	<a class="ui-button ui-corner-all" href="<%=basePath%>web/webStructExportXml?projectId=${projectId }" onclick="">导出XML</a>
		<a class="ui-button ui-corner-all" href="<%=basePath%>web/webStructExportExcel?projectId=${projectId }" onclick="">导出EXCEL</a>
</div>

<div id="compareResultDlg" style="display:none; color: red;">
<div>
<ul id="compareResultList"></ul>
</div>
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