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
	height: 100px;
	position: absolute;
	left: 0px;
	top: 0px;
	right: 0px;
	border-bottom: 1px gray solid;
	overflow: hidden;
	background-color: #6faacf;
	background: -webkit-gradient(linear, 0 0, 0 bottom, from(#9fdfef), to(#6faacf));
	background: -moz-linear-gradient(top, #9fdfef, #6faacf);
	box-shadow: 0px 3px 5px 0 rgba(0, 0, 0, 0.26);
}

#div_header #loginInfoDiv{
	float: right;
}

#div_footer {
	height: 32px;
	position: absolute;
	left: 0px;
	bottom: 0px;
	right: 0px;
}

#div_main_container {
	position: absolute;
	top: 101px;
	left: 0px;
	bottom: 0px;
	right: 0px;
	overflow: hidden;
}

#div_main_container iframe {
	border: 0px;
	width: 100%;
	height: 100%;
}

.menuBarFrame {
	height: 32px;
	position: absolute;
	left: 0px;
	right: 0px;
	bottom: 0px;
	margin: 0px 5px;
}

.menuBarFrame ul {
	position: relative;
	margin: 0px;
	padding: 0px;
	list-style: none;
}

.menuBarFrame li {
	position: relative;
	float: left;
	text-align: center;
	display: block;
	border: 1px #EFEFEF solid;
}

.menuBarFrame li:HOVER {
	background-color: #aaddff;
	background: -webkit-gradient(linear, 0 0, 0 bottom, from(#daedff), to(#aaddff));
	background: -moz-linear-gradient(top, #daedff, #aaddff);
	position: relative;
	top: -4px;
	height: 36px;
}

.menuBarFrame li a {
	color: black;
	line-height: 32px;
	display: block;
	text-decoration: none;
	padding: 0px 5px;
}

.menuBarFrame .menuChecked {
	border: 1px gray solid;
	background-color: #55ccdd;
	background: -webkit-gradient(linear, 0 0, 0 bottom, from(#daedff), to(#8edfdf));
	background: -moz-linear-gradient(top, #daedff, #8edfdf);
	position: relative;
	top: -4px;
	height: 36px;
}

.menuBarFrame .menuChecked a{
	color: white;
}

.hideFrame{
	display: none;
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
var windowTitle = "";
$j(function(){
	windowTitle = $j("title").text();
	$j("#menuBarFrame li").click(function(){
		$j("#menuBarFrame li").removeClass("menuChecked");
		$j(this).addClass("menuChecked");
	});
	
	projectService.getProjectById("${projectId }", function(project){
		$j("#lbProjectName").text(project.projectName);
	});
});

function containerLoad(url, title){
	$j("title").text(windowTitle + " - " + title);
	//$j("#main_container_iframe").attr("src", url);
	$j("iframe[class!=hideFrame]").addClass("hideFrame");
	var id = "#main_container_iframe_" + title.toLowerCase();
	var iframe = $j(id);
	if(url != iframe.attr("src")){
		iframe.attr("src", url)
	}
	iframe.removeClass("hideFrame");
}
</script>
</head>
<body>
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
		<div><a href="<%=basePath%>projects">&lt;&lt;</a><label id="lbProjectName"></label></div>
		<div class="menuBarFrame" id="menuBarFrame">
			<ul>
				<li><a href="javascript:void(0)" onclick="containerLoad('<%=basePath%>db/project?projectId=${projectId }', 'DB')">DB</a></li>
				<li><a href="javascript:void(0)" onclick="containerLoad('<%=basePath%>web/project?projectId=${projectId }', 'WEB')">WEB</a></li>
				<li><a href="javascript:void(0)" onclick="containerLoad('<%=basePath%>snmp/project?projectId=${projectId }', 'SNMP')">SNMP</a></li>
				<!-- <li><a href="#" onclick="containerLoad('<%=basePath%>tr069/project?projectId=${projectId }', 'Tr069')">Tr069</a></li> -->
				<li><a href="javascript:void(0)" onclick="containerLoad('<%=basePath%>pages/not-ready.jsp', 'Tr069')">Tr069</a></li>
			</ul>
		</div>
	</div>
	
	<div id="div_main_container">
		<iframe id="main_container_iframe_gide" src="<%=basePath%>pages/projectPageHint.html"></iframe>
		<iframe id="main_container_iframe_db" class="hideFrame" src=""></iframe>
		<iframe id="main_container_iframe_web" class="hideFrame" src=""></iframe>
		<iframe id="main_container_iframe_snmp" class="hideFrame" src=""></iframe>
		<iframe id="main_container_iframe_tr069" class="hideFrame" src=""></iframe>
	</div>
	<!-- <div id="div_footer"></div> -->
	
</body>
</html>