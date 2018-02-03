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
<title>${systemName } - WEB结构体数据概览</title>
<link rel="shortcut icon" type="image/x-icon" href="<%=basePath%>images/icon.ico" />
<link href="<%=basePath%>styles/common.css" rel="stylesheet" type="text/css" />
<link href="<%=basePath%>styles/dbProject.css" rel="stylesheet" type="text/css" />
<link href="<%=basePath%>scripts/jquery/ui/jquery-ui.css" rel="stylesheet" type="text/css" />
<link href="<%=basePath%>scripts/jquery/ui/jquery-ui.theme.css" rel="stylesheet" type="text/css" />
<style type="text/css">
.rowMark {
	background-color: #efefaa;
}

.pkMemberRow {
	background-color: #efafaa;
}

.tbCommon td {
	font-family: 宋体 !important;
	font-size: 12px !important;
}

.tbCommon td {
	height: 18px !important;
	line-height: 18px !important;
	white-space: nowrap;
}

.tbCommon tr {
	height: 18px !important;
}

#dbstructsDiv {
	height: 100%;
	width: 100%;
}

#waitingDiv {
	background-image: url('<%=basePath%>images/loading.gif');
	background-repeat: no-repeat;
	background-position: center;
	height: 100%;
	width: 100%;
}

.selectedRow {
	background-color: #ddf0ff;
	border: 1px solid black !important;
	color: red;
}

.titleDiv {
	position: fixed;
	left: 0px;
	top: 0px;
	display: none;
	margin: 0px;
	padding: 0px;
	box-shadow: 0px 3px 6px 0 rgba(0, 0, 0, 0.26);
}

</style>
<script type="text/javascript" src="<%=basePath%>scripts/jquery/jquery.js"></script>
<script type="text/javascript" src="<%=basePath%>scripts/jquery/ui/jquery-ui.js"></script>
<script type='text/javascript' src='<%=basePath%>dwr/engine.js'></script>
<script type='text/javascript' src='<%=basePath%>dwr/interface/projectService.js?version=<%=Math.random() %>'></script>
<script type='text/javascript' src='<%=basePath%>dwr/interface/webStructService.js?version=<%=Math.random() %>'></script>
<script type="text/javascript" src="<%=basePath%>scripts/common.js"></script>

<script type="text/javascript">
var $j = jQuery.noConflict();

function markRow(e){
	$j(".selectedRow", dbstructsDiv).removeClass("selectedRow");
	$j(e).addClass("selectedRow");
}

var fixHead = $j("<div class=\"titleDiv\"><table class=\"tbCommon\">" 
	+ "<thead><tr><th></th><th>StructID</th><th>StructName</th><th>ClassifyName</th><th>StructCnName</th><th>MemberID</th><th>MemberName</th><th>MemberCnName</th><th>IsPrimaryKey</th><th>DefaultValue</th><th>WebType</th><th>TypeDesc</th><th>WebTips</th><th>SetStatus</th></tr></thead></table></div>");
var titleShowed = false;
$j(function(){
	$j(document).on("scroll", function(){
		var st = $j(this).scrollTop();
		var sl = $j(this).scrollLeft();
		if(0 == st){
			fixHead.hide();
			titleShowed = false;
		}else{
			fixHead.css("width", $j("#dbstructsDiv table").outerWidth() + "px");
			fixHead.css({
				left: (0 - sl)
			});
			
			if(!titleShowed){
				titleShowed = true;
				var i = 0;
				var ths = $j("th", fixHead);
				$j("#dbstructsDiv th").each(function(){
					var th = $j(ths[i++]);
					//th.css("width", $j(this).innerWidth() - 1);
					th.innerWidth($j(this).innerWidth() + 1);
				});
				fixHead.show("fade");
			}
			
		}
	});
	
	webStructService.getAllStructs($j("#projectId").val(), function(structs){
		var tbStr = "<table class=\"tbCommon\">";
		tbStr += "<thead><tr><th></th><th>StructID</th><th>StructName</th><th>ClassifyName</th><th>StructCnName</th><th>MemberID</th><th>MemberName</th><th>MemberCnName</th><th>IsPrimaryKey</th><th>DefaultValue</th><th>WebType</th><th>TypeDesc</th><th>WebTips</th><th>SetStatus</th></tr></thead>";
		
		var row = 1;
		for(var i in structs){
			var struct = structs[i];
			var members = struct.members;
			var structInfoAdded = false;
			for(var j in members){
				var member = members[j];
				
				tbStr += "<tr onclick=\"markRow(this);\" class=\"";
				if(0 != (i % 2)){
					tbStr += " rowMark";
				}
				if(member.primaryKey){
					tbStr += " pkMemberRow";
				}
				tbStr += "\">";
				
				var classifyName = "";
				var structCnName = "";
				if(!structInfoAdded){
					structInfoAdded = true;
					classifyName = struct.classifyName;
					structCnName = struct.structCnName;
				}
				
				tbStr += "<td class=\"tbRowNum\">" + row + "</td>"
					+ "<td>" + struct.structId + "</td>"
					+ "<td>" + struct.structName + "</td>"
					+ "<td>" + classifyName + "</td>"
					+ "<td>" + structCnName + "</td>"
					+ "<td>" + (parseInt(struct.structId) * 1000 + parseInt(member.memberId)) + "</td>"
					+ "<td>" + member.memberName + "</td>"
					+ "<td>" + member.memberCnName + "</td>"
					+ "<td>" + (member.primaryKey ? 1 : 0) + "</td>"
					+ "<td>" + member.defaultValue + "</td>"
					+ "<td>" + member.webType + "</td>"
					+ "<td>" + member.typeDesc + "</td>"
					+ "<td>" + (member.unit 
							+ "|" + member.valueRangeCn 
							+ "|" + member.memberLevel 
							+ "|" + member.structLevel
							+ "|" + (member.canAddOrDelete ? 1 : 0)
							+ "|" + (member.unique ? 1 : 0)
							+ "|" + member.valueRange
							+ "|" + (member.rebootEffective ? 1 : 0)) + "</td>"
					+ "<td>" + member.setStatus + "</td>"
					+ "</tr>";
				++row;
			}
		}
		
		tbStr += "</table>";
		$j("#dbstructsDiv").empty();
		$j("#dbstructsDiv").append(tbStr);
		$j("body").append(fixHead);
	});
});

</script>

</head>
<body>
	<input type="hidden" value="${projectId }" id="projectId"/>
	<div id="dbstructsDiv">
		<div id="waitingDiv"></div>
	</div>
</body>
</html>