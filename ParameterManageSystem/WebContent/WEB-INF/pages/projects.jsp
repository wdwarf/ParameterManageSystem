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
<s:set var="isUserLogin" value="null!=#session.user"></s:set>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="Cache-Control" content="no-cache" />
<title>${systemName }</title>
<link rel="shortcut icon" type="image/x-icon" href="<%=basePath%>images/icon.ico" />
<link href="<%=basePath%>styles/common.css" rel="stylesheet" type="text/css" />
<link href="<%=basePath%>scripts/jquery/ui/jquery-ui.css" rel="stylesheet" type="text/css" />
<style type="text/css">
#div_header {
	position: relative;
	height: 100px;
	width: 100%;
	border-bottom: 1px gray solid;
	overflow: hidden;
	background-color: #6faacf;
	background: -webkit-gradient(linear, 0 0, 0 bottom, from(#9fdfef), to(#6faacf));
	background: -moz-linear-gradient(top, #9fdfef, #6faacf);
	margin-bottom: 3px;
	box-shadow: 0px 3px 5px 0 rgba(0, 0, 0, 0.26);
}

#div_header .header_title {
	font-weight: bold;
	font-size: 18px;
	line-height: 100px;
	display: block;
	margin-left: 10px;
	color: white;
}

#div_header #loginInfoDiv{
	float: right;
}

.projectLink {
	display: block;
	color: black;
	width: 100%;
	line-height: 24px;
	padding: 0px;
	margin: 0px;
}

#tbProjects td{
	white-space: nowrap;
}
</style>
<script type="text/javascript" src="<%=basePath%>scripts/jquery/jquery.js"></script>
<script type="text/javascript" src="<%=basePath%>scripts/jquery/ui/jquery-ui.js"></script>
<script type='text/javascript' src='<%=basePath%>dwr/engine.js'></script>  
<script type='text/javascript' src='<%=basePath%>dwr/interface/projectService.js?version=<%=Math.random() %>'></script>
<script type='text/javascript' src='<%=basePath%>dwr/interface/userService.js?version=<%=Math.random() %>'></script>
<script type="text/javascript" src="<%=basePath%>scripts/common.js"></script>
<script type="text/javascript" src="<%=basePath%>scripts/userMgmt.js"></script>
<script type="text/javascript">
var $j = jQuery.noConflict();

function openProject(projectId){
	$j("#projectId").val(projectId);
	$j("#projectForm").submit();
}

function loadProjects(){
	projectService.getAllProjects(function(projects){
		var loginUser = getLoginUser();
		var loginUserId = "";
		var loginUserName = "";
		if(loginUser){
			loginUserId = loginUser.userId;
			loginUserName = loginUser.userName;
		}
		
		var tb = $j("#tbProjects");
		$j("tbody", tb).empty();
		console.log(projects);
		for(var i in projects){
			var project = projects[i];
			console.log(project);
			var projectName = project.projectName;
			var projectId = project.projectId;
			var userName = "";
			var userId = null;
			var user = project.user;
			console.log(user);
			if(user){
				userName = user.userName;
				if(null != user.nickName && "" != user.nickName){
					userName = user.nickName;
				}
				userId = user.userId;
			}
			var createDate = localDateStr(project.createDate);
			var href = "<%=basePath%>project?projectId=" + projectId;
			
			var opStr = "<td></td>";
			if(("" != userId && loginUserId == userId) || "admin" == loginUserName){
				opStr = "<td>" 
					+ "<a class=\"button\" href=\"javascript:void(0)\" onclick=\"showEditProjectDlg('" + projectId + "', '" + projectName + "');\">编辑</a>"
					+ " <a class=\"button alert\" href=\"javascript:void(0)\" onclick=\"deleteProject('" + projectId + "', '" + projectName + "');\">删除</a>"
				+ "</td>";
			}
			
			tb.append($j("<tr userId='" + userId + "' ><td><a class=\"projectLink\" href=\"javascript:void(0)\" onclick=\"openProject('" + projectId + "','" + projectName + "');\">" + projectName + "</a></td>"
				+ "<td>" + userName + "</td>"
				+ "<td>" + createDate + "</td>"
				+ opStr
				+ "</tr>"));
		}
	});
}

function showAddNewProjectDlg(){
	if(!hasUserLogin()){
		alert("您未登录 ，请先登录。");
		window.location = "login";
		return;
	}
	
	$j("#divAddProject").dialog({
		title: "添加工程",
		modal: true,
		show: window._dialogEffectName,
		hide : window._dialogEffectName,
		width: 350,
		buttons: [
			{
				text: "确定",
				click: addNewProject
			}
		],
		close : function(){
			//
		}
	});
}

function addNewProject(){
	var userId = $j("#userId").val();
	var project = {};
	project.projectName = $j("#projectName").val();
	if("" == project.projectName){
		showErrMsg("请输入工程名称！", "添加工程");
		return;
	}
	
	if("" != userId){
		project.user = {};
		project.user.userId = userId;
	}
	
	projectService.addProject(project, function(e){
		//window.location.reload();
		$j("#divAddProject").dialog("close");
		loadProjects();
	});
}

function editProject(projectId, projectName){
	//alert("editProject " + projectId + ", " + projectName);
	projectService.updateProjectName(projectId, projectName, function(){
		loadProjects();
	});
}

function showEditProjectDlg(projectId, projectName){
	var loginUser = getLoginUser();
	var project = null;
	dwr.engine.setAsync(false);
	projectService.getProjectById(projectId, function(p){
		project = p;
	});
	dwr.engine.setAsync(true);

	if(null == loginUser){
		//showErrMsg("请登录。", "未登录");
		alert("您未登录 ，请先登录。");
		window.location = "login";
		return;
	}
	
	if(null == project){
		showErrMsg("未找到工程");
		return;
	}
	
	if("admin" != loginUser.userName && (!project.user || (project.user && (project.user.userId != loginUser.userId)))){
		showErrMsg("无权限");
		return;
	}
	
	$j("#projectName").val(projectName);
	$j("#divAddProject").dialog({
		title: "编辑工程",
		modal: true,
		show: window._dialogEffectName,
		hide : window._dialogEffectName,
		width: 350,
		buttons: [
			{
				text: "确定",
				click: function(){
					var newProjectName = $j("#projectName").val().trim();
					if("" == newProjectName){
						showErrMsg("请输入工程名称！", "编辑工程");
						return;
					}
					
					if(newProjectName != projectName){
						editProject(projectId, newProjectName);
					}
					$j(this).dialog("close");
				}
			}
		],
		close : function(){
			$j("#projectName").val("");
		}
	});
}

function deleteProject(projectId, projectName){
	var loginUser = getLoginUser();
	var project = null;
	dwr.engine.setAsync(false);
	projectService.getProjectById(projectId, function(p){
		project = p;
	});
	dwr.engine.setAsync(true);

	if(null == loginUser){
		//showErrMsg("请登录。", "未登录");
		alert("您未登录 ，请先登录。");
		window.location = "login";
		return;
	}
	
	if(null == project){
		showErrMsg("未找到工程");
		return;
	}
	
	if("admin" != loginUser.userName && (!project.user || (project.user && (project.user.userId != loginUser.userId)))){
		showErrMsg("无权限");
		return;
	}
	
	$j("<div style='color: red;'>" + "确定要删除工程[ " + projectName + " ]？" + "</div>").dialog({
		title : "删除工程",
		modal : true,
		show: window._dialogEffectName,
		hide : window._dialogEffectName,
		buttons : [
			{
				text : "确定",
				click : function(){
					var project = {};
					project.projectId = projectId;
					projectService.deleteProject(project, function(e){
						loadProjects();
					});
					$j(this).dialog("close");
				}
			},
			{
				text : "取消",
				click : function(){
					$j(this).dialog("close");
				}
			}
		]
	});
	
	
}

$j(function(){
	loadProjects();
});

</script>
</head>
<body>
	<input type="hidden" id="userId" value="${sessionScope.user.userId }" />
	<input type="hidden" id="userName" value="${sessionScope.user.userName }" />

	<div id="div_header">
		<div id="loginInfoDiv">
		<s:if test="#isUserLogin == false">
			<a class="button" href="<%=basePath%>login">登录</a>
		</s:if>
		<s:else>
			<span>欢迎 </span><span style="color:orange;font-weight: bold;"><s:if test="null != #session.user.nickName && '' != #session.user.nickName"><s:property value="#session.user.nickName" /></s:if><s:else><s:property value="#session.user.userName" /></s:else> </span>， <a class="button" href="<%=basePath%>logout">登出</a>
			<a class="button" href="javascript:void(0)" onclick="changeUserPassword('${sessionScope.user.userId }');return false;">修改密码</a>
			<s:if test="#session.user.username == 'admin'">
			<a class="button" href="<%=basePath%>userMgmt">用户管理</a>
			</s:if>
		</s:else>
		</div>
		<span class="header_title">${systemName }</span>
	</div>
	<div style="margin: 0px 10px;">
		<s:if test="#isUserLogin==true">
		<div style="text-align: right;">
			<button class="button" onclick="showAddNewProjectDlg();">添加项目</button>
		</div>
		</s:if>
		<div>
			<form action="<%=basePath%>project" method="post" id="projectForm">
				<input type="hidden" name="projectId" id="projectId" />
				<table class="tbCommon" id="tbProjects">
					<thead>
						<tr>
							<th>项目名称</th>
							<th style="width: 50px;">创建者</th>
							<th style="width: 50px;">创建日期</th>
							<th style="width: 50px;">操作</th>
						</tr>
					</thead>
				</table>
			</form>
		</div>
	</div>
	<div style="display: none;height: 100%;width: 100%; text-align: center;" id="divAddProject">
		<table style="height: 100%; width: 100%;">
			<thead>
				<tr>
					<td><label for="projectName">工程名称</label> <input type="text" name="projectName" id="projectName" class="text ui-widget-content ui-coner-all"/></td>
				</tr>
			</thead>
		</table>
	</div>
</body>
</html>