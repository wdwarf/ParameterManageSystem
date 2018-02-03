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
<title>${systemName } - 用户管理</title>
<link rel="shortcut icon" type="image/x-icon"
	href="<%=basePath%>images/icon.ico" />
<link href="<%=basePath%>styles/common.css" rel="stylesheet"
	type="text/css" />
<link href="<%=basePath%>scripts/jquery/ui/jquery-ui.css"
	rel="stylesheet" type="text/css" />
<script type="text/javascript"
	src="<%=basePath%>scripts/jquery/jquery.js"></script>
<script type="text/javascript"
	src="<%=basePath%>scripts/jquery/ui/jquery-ui.js"></script>
<script type='text/javascript' src='<%=basePath%>dwr/engine.js'></script>
<script type='text/javascript'
	src='<%=basePath%>dwr/interface/userService.js?version=<%=Math.random()%>'></script>
<script type="text/javascript" src="<%=basePath%>scripts/common.js"></script>
<style type="text/css">
#div_header {
	position: relative;
	height: 100px;
	width: 100%;
	border-bottom: 1px gray solid;
	overflow: hidden;
	background-color: #6faacf;
	background: -webkit-gradient(linear, 0 0, 0 bottom, from(#9fdfef),
		to(#6faacf));
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

#div_header #loginInfoDiv {
	float: right;
}

.tbCommon td{
	white-space: nowrap;
}
</style>

<script type='text/javascript'>
	var $j = jQuery.noConflict();
	
	function loadAllUsers(){
		$j("#userTable tbody").empty();
		
		userService.getAllUsers(function(users){
			for(var i in users){
				var user = users[i];
				var userName = user.userName;
				if("admin" == userName) continue;
				
				var row = $j("<tr></tr>");
				var tdUserName = $j("<td></td>");
				var tdNickName = $j("<td></td>");
				var tdCreateDate = $j("<td style='width:30px;'></td>");
				var tdOp = $j("<td style='width:30px;'></td>");
				var delBtn = $j("<a href='javascript:void(0)' onclick='updateUser(this); return false;' class='button'>编辑</a> <a href='javascript:void(0)' onclick='deleteUser(this); return false;' class='button alert'>删除</a>");
				
				tdUserName.text(user.userName);
				tdNickName.text(user.nickName);
				tdOp.append(delBtn);
				
				tdCreateDate.text(localDateStr(user.createDate));
				
				row.append(tdUserName);
				row.append(tdNickName);
				row.append(tdCreateDate);
				row.append(tdOp);
				row[0].user = user;
				
				$j("#userTable tbody").append(row);
			}
		});
	}

	function showAddNewUserDlg(){
		$j("#userEditDlg").dialog({
			title: "添加用户",
			modal: true,
			show: window._dialogEffectName,
			hide : window._dialogEffectName,
			buttons : [
				{
					text: "添加",
					click: function(){
						var userName = $j("#userName").val().trim();
						var nickName = $j("#nickName").val().trim();
						var password = $j("#password").val().trim();
						if("" == userName || "" == password){
							showErrMsg("请输入用户名和密码", "添加用户");
							return;
						}
						
						var user = {};
						user.userName = userName;
						user.password = password;
						user.nickName = nickName;
						userService.addUser(user, function(){
							loadAllUsers();
						});

						$j(this).dialog("close");
					}
				}
			],
			close: function(){
				$j("#userName").val("");
				$j("#nickName").val("");
				$j("#password").val("");
			}
		});
	}
	

	function updateUser(a){
		var row = findParent(a, "tr");
		var user = row[0].user;
		
		$j("#userName").val(user.userName);
		$j("#nickName").val(user.nickName);
		$j("#password").val(user.password);
		
		$j("#userEditDlg").dialog({
			title: "编辑用户",
			modal: true,
			show: window._dialogEffectName,
			hide : window._dialogEffectName,
			buttons : [
				{
					text: "确定修改",
					click: function(){
						var userName = $j("#userName").val().trim();
						var nickName = $j("#nickName").val().trim();
						var password = $j("#password").val().trim();
						if("" == userName || "" == password){
							showErrMsg("请输入用户名和密码", "编辑用户");
							return;
						}
						
						user.userName = userName;
						user.password = password;
						user.nickName = nickName;
						userService.updateUser(user, function(){
							loadAllUsers();
						});
						
						$j(this).dialog("close");
					}
				}
			],
			close: function(){
				$j("#userName").val("");
				$j("#nickName").val("");
				$j("#password").val("");
			}
		});
	}
	
	function deleteUser(a){
		var row = findParent(a, "tr");
		var user = row[0].user;
		
		$j("<div style='color: red;'>" + "确定要删除用户[ " + user.userName + " ]？" + "</div>").dialog({
			title : "删除用户",
			modal : true,
			show: window._dialogEffectName,
			hide : window._dialogEffectName,
			buttons : [
				{
					text : "确定",
					click : function(){
						userService.deleteUser(user.userId, function(){
							loadAllUsers();
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
		loadAllUsers();
	});
	
</script>
</head>

<body>
	<input type="hidden" id="userId" value="${sessionScope.user.userId }" />

	<div id="div_header">
		<a href="<%=basePath%>" style="float:left;">&lt;&lt;</a>
		<div id="loginInfoDiv">
			<s:if test="#isUserLogin == false">
				<a class="button" href="<%=basePath%>login">登录</a>
			</s:if>
			<s:else>
				<span>欢迎 </span><span style="color:orange;font-weight: bold;"><s:if test="null != #session.user.nickName && '' != #session.user.nickName"><s:property value="#session.user.nickName" /></s:if><s:else><s:property value="#session.user.userName" /></s:else> </span>， <a class="button" href="<%=basePath%>logout">登出</a>
			</s:else>
		</div>
		<span class="header_title">${systemName } - 用户管理</span>
	</div>

	<div style="text-align: right;">
		<button class="button" onclick="showAddNewUserDlg();">添加用户</button>
	</div>
	<div>
		<table class="tbCommon" id="userTable">
			<thead>
				<tr>
					<th>用户名</th>
					<th>真实姓名</th>
					<th>创建日期</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody></tbody>
		</table>
	</div>

	<div id="userEditDlg" style="display: none;">
		<table>
			<tr><td><label for="userName">用户名</label></td><td><input type="text" id="userName" /></td></tr>
			<tr><td><label for="nickName">真实姓名</label></td><td><input type="text" id="nickName" /></td></tr>
			<tr><td><label for="password">密码</label></td><td><input type="password" id="password" /></td></tr>
		</table>
	</div>
</body>
</html>