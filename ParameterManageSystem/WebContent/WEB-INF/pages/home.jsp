<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	response.setHeader("Cache-Control", "no-cache");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="Cache-Control" content="no-cache" />
<title>Parameter Manage System</title>

<link href="<%=basePath%>styles/common.css" rel="stylesheet"
	type="text/css" />
<link href="<%=basePath%>styles/home.css" rel="stylesheet"
	type="text/css" />
<style type="text/css">
</style>
<script type="text/javascript" src="<%=basePath%>scripts/jquery/jquery.js"></script> 
<script type="text/javascript" src="<%=basePath%>scripts/home.js"></script>
<script type='text/javascript' src='<%=basePath%>dwr/engine.js'></script>  
<script type='text/javascript' src='<%=basePath%>dwr/interface/projectService.js'></script>
<script type='text/javascript' src='<%=basePath%>dwr/interface/dbStructService.js'></script>
<script type="text/javascript">

</script>
</head>
<body>

	<div class="panel-project-menu">
		<div id="panel-projects" class="panel-common">
			<div class="list-title">
				<span>Projects</span>
			</div>
			<div class="panel-struct-list">
				<ul class="memu-list" id="projectList">
				</ul>
			</div>
		</div>
		<div id="panel-sub-projects" class="panel-common">
			<div class="list-title">
				<span>Sub Projects</span>
			</div>
			<div class="panel-struct-list">
				<ul class="memu-list" id="subProjectList">
					<li><a>DB</a></li>
					<li><a>WEB</a></li>
					<li><a>SNMP</a></li>
					<li><a>Tr069</a></li>
				</ul>
			</div>
		</div>
	</div>

	<div class="panel-sub-project-detail">
		<div class="sub-project-title">
			<span id="project-name"></span>&nbsp;&nbsp;<span
				id="sub-project-name"></span>
		</div>
		<div class="panel-structs-info">
			<div class="panel-structs panel-common">
				<div class="list-title">
					<span>Structs</span>
				</div>
				<div class="panel-struct-list" id="panel-struct-list">
					<ul class="memu-list" id="structList">
						<li><a>DeviceInfo</a></li>
						<li><a>IrMode</a></li>
						<li><a>snmpTrapEntry</a></li>
					</ul>
				</div>
			</div>
			<div class="panel-struct-detail">
				<iframe id="struct-editor" src="" frameborder="0" scrolling="auto"></iframe>
			</div>
		</div>
	</div>

</body>
</html>