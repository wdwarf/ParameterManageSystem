<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Parameter Manage System</title>
<link rel="shortcut icon" type="image/x-icon"
	href="<%=basePath%>images/icon.ico" />
<link href="<%=basePath%>styles/common.css" rel="stylesheet"
	type="text/css" />
<link href="<%=basePath%>scripts/jquery/ui/jquery-ui.css"
	rel="stylesheet" type="text/css" />
<style type="text/css">
html, body {
	position: relative;
	height: 100%;
	padding: 0px;
	margin: 0px;
}

#titleBar {
	position: relative;
	width: 324px;
	height: 32px;
	left: -3px;
	line-height: 32px;
	text-align: center;
	font-weight: bold;
	color: white;
	border: #aaaaaa 1px solid;
	background-color: #0080ff;
	background: -webkit-gradient(linear, 0 0, 0 bottom, from(#dfefff),
		to(#0080ff));
	background: -moz-linear-gradient(top, #dfefff, #0080ff);
	background: -o-linear-gradient(top, #dfefff, #0080ff);
	background: -ms-linear-gradient(top, #dfefff, #0080ff);
	background: linear-gradient(top, #dfefff, #0080ff);
	-webkit-transition-duration: 0.3s; /* Safari */
	transition-duration: 0.3s;
	line-height: 32px;
}

#loginDivParentFloater {
	float: left;
	height: 50%;
	margin-bottom: -125px;
}

#loginDivParent {
	clear: both;
	border-radius: 0px 0px 10px 10px;
	width: 320px;
	height: 200px;
	margin: 0px auto;
	position: relative;
	background-color: #ddedff;
	box-shadow: 5px 8px 12px 0 rgba(0, 0, 0, 0.35);
}

#loginInfoTable {
	height: 80px;
	margin: 0px auto;
}

#loginInfoTable label {
	float: right;
}

#loginInfoTable input {
	float: left;
	width: 100%;
}

#errInfo {
	height: 30px;
	color: red;
}

#submitBtn {
	height: 55px;
	width: 55px;
	margin-left: 3px;
}
</style>
<script type="text/javascript"
	src="<%=basePath%>scripts/jquery/jquery.js"></script>
<script type="text/javascript"
	src="<%=basePath%>scripts/jquery/ui/jquery-ui.js"></script>
<script type="text/javascript" src="<%=basePath%>scripts/common.js"></script>
</head>
<body>
	<div id="loginDivParentFloater"></div>
	<div id="loginDivParent">
		<div id="titleBar"><a style="position:absolute;left:3px;color:white;font-weight:normal;" href="<%=basePath%>projects">&lt;&lt;</a>请登录</div>
		<div id="errInfo">
			<s:actionerror />
		</div>
		<form action="doLogin" method="post">
			<table id="loginInfoTable">
				<tr>
					<td><label for="userName"><img src="<%=basePath%>images/user.png" /></label></td>
					<td><input type="text" id="userName" name="user.userName" value="${user.userName }" tabindex="1" /></td>
					<td rowspan="2"><button id="submitBtn" type="submit" class="button" tabindex="3">登录</button></td>
				</tr>
				<tr>
					<td><label for="password"><img src="<%=basePath%>images/encrypted.png" /></label></td>
					<td><input type="password" id="password" name="user.password" tabindex="2" /></td>
				</tr>
			</table>
		</form>
	</div>

</body>
</html>