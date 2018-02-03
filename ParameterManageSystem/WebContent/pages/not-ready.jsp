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
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="Cache-Control" content="no-cache" />
<title>${systemName }</title>
<link rel="shortcut icon" type="image/x-icon" href="<%=basePath%>images/icon.ico" />
<link href="<%=basePath%>styles/common.css" rel="stylesheet" type="text/css" />
<style type="text/css">
html, body{
	background-color: transparent !important;
}
</style>
<script type="text/javascript" src="<%=basePath%>scripts/jquery/jquery.js"></script>
<script type="text/javascript">
var $j = jQuery.noConflict();

</script>
</head>
<body>
	<table style="width: 100%; height: 100%;">
		<tr>
			<td style="text-align:center;vertical-align:middle;">
				<div><img src="<%=basePath %>images/yaoming_laughing.jpg" style="text-align:center;" /></div>
				<div><span>这个功能还没实现......</span></div>
			</td>
		</tr>
	</table>
</body>
</html>