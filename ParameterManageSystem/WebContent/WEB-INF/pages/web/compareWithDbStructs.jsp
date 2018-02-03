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
<title>${systemName } - WEB-DB参数差异对比</title>
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

.tbCommon{
	width: auto;
	margin-bottom: 15px;
}

.tbCommon td {
	font-family: 宋体 !important;
	font-size: 12px !important;
}

.tbCommon td {
	height: 18px !important;
	line-height: 18px !important;
	white-space: nowrap;
	padding: 0px 3px;
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

.markItem{
	background-color: orange;
}

</style>
<script type="text/javascript" src="<%=basePath%>scripts/jquery/jquery.js"></script>
<script type="text/javascript" src="<%=basePath%>scripts/jquery/ui/jquery-ui.js"></script>
<script type='text/javascript' src='<%=basePath%>dwr/engine.js'></script>
<script type='text/javascript' src='<%=basePath%>dwr/interface/projectService.js?version=<%=Math.random() %>'></script>
<script type='text/javascript' src='<%=basePath%>dwr/interface/dbStructService.js?version=<%=Math.random() %>'></script>
<script type='text/javascript' src='<%=basePath%>dwr/interface/webStructService.js?version=<%=Math.random() %>'></script>
<script type="text/javascript" src="<%=basePath%>scripts/common.js"></script>

<script type="text/javascript">
var $j = jQuery.noConflict();

/**
 * 不存在于DB的参数
 */
var noInDBStructs = {
		table : $j("<table class='tbCommon'><thead><tr style='background-color: #ff3333;'><th colspan='2' style='color:white;'>不存在于DB中的结构体</th></tr><tr><th>StructName</th><th>StructID</th></tr></thead><tbody></tbody></table>"),
		addStruct : function(s){
			var row = $j("<tr></tr>");
			row.append($j("<td>" + s.structName + "</td>"));
			row.append($j("<td>" + s.structId + "</td>"));
			$j("tbody", this.table).append(row);
		},
		show : function(){
			var rowCount = $j("tbody tr", this.table).length;
			if(rowCount <= 0) return;
			
			$j("#dbstructsDiv").append("<div style='font-weight:bold; font-size: 18px; margin: 5px 0px;'>有<span style='color: red;'>" + rowCount + "</span>个结构体不存在于DB配置中</div><hr />");
			$j("#dbstructsDiv").append(this.table);
		}
};

/**
 * 未配置的参数
 */
var noConfigStructs = {
		table : $j("<table class='tbCommon'><thead><tr style='background-color: #ff3333;'><th colspan='2' style='color:white;'>WEB中未配置的结构体</th></tr><tr><th>StructName</th><th>StructID</th></tr></thead><tbody></tbody></table>"),
		addStruct : function(s){
			var row = $j("<tr></tr>");
			row.append($j("<td>" + s.structName + "</td>"));
			row.append($j("<td>" + s.structId + "</td>"));
			$j("tbody", this.table).append(row);
		},
		show : function(){
			var rowCount = $j("tbody tr", this.table).length;
			if(rowCount <= 0) return;
			$j("#dbstructsDiv").append("<div style='font-weight:bold; font-size: 18px; margin: 5px 0px;'>有<span style='color: red;'>" + rowCount + "</span>个结构体在WEB中未配置</div><hr />");
			$j("#dbstructsDiv").append(this.table);
		}
};

/**
 * ID与名称不比配的结构体
 */
var idNotMatchNameStructs = {
		table : $j("<table class='tbCommon'><thead><tr style='background-color: #ff4444;'><th colspan='6' style='color:white;'>结构体ID与名称不匹配的参数</th></tr>" 
			+ "<tr><th colspan='2'>DB参数</th><th colspan='2'>按ID匹配的参数</th><th colspan='2'>按名称匹配的参数</th></tr>"
			+ "<tr><th>StructName</th><th>StructID</th><th>StructName</th><th>StructID</th><th>StructName</th><th>StructID</th></tr>" 
			+ "</thead><tbody></tbody></table>"),
		addStruct : function(ds, wsId, wsName){
			if(null == wsId){
				wsId = {
						structName : "",
						structId : ""
				};
			}
			if(null == wsName){
				wsName = {
						structName : "",
						structId : ""
				};
			}
			var row = $j("<tr></tr>");
			row.append($j("<td>" + ds.structName + "</td><td>" + ds.structId + "</td>"));
			row.append($j("<td>" + wsId.structName + "</td><td>" + wsId.structId + "</td>"));
			row.append($j("<td>" + wsName.structName + "</td><td>" + wsName.structId + "</td>"));
			$j("tbody", this.table).append(row);
		},
		show : function(){
			var rowCount = $j("tbody tr", this.table).length;
			if(rowCount <= 0) return;
			
			$j("#dbstructsDiv").append("<div style='font-weight:bold; font-size: 18px; margin: 5px 0px;'>有<span style='color: red;'>" + rowCount + "</span>个结构体ID与名称不匹配</div><hr />");
			$j("#dbstructsDiv").append(this.table);
		}
};

/**
 * 成员不匹配的结构体
 */
var findMember = function(struct, memberId){
	for(var i in struct.members){
		var m = struct.members[i];
		if(m.memberId == memberId){
			return m;
		}
	}
	return null;
}
var memberUnmatchStructs = {
		div : $j("<div><div id='title' style='font-weight:bold; font-size: 18px; margin: 5px 0px;'></div><hr /></div"),
		addStruct : function(ds, ws){
			var tb = $j("<table class='tbCommon'><thead>" 
					+ "<tr style='background-color: #ff8855'><th colspan='9' style='color:white;'>" + ds.structName + "[" + ds.structId + "]的成员不匹配</th></tr>"
					+ "<tr><th colspan='4'>DB参数</th><th>&nbsp;</th><th colspan='4'>WEB参数</th></tr>"
					+ "<tr><th>MemberID</th><th>MemberName</th><th>IsPrimaryKey</th><th>DefaultValue</th><th></th><th>MemberID</th><th>MemberName</th><th>IsPrimaryKey</th><th>DefaultValue</th></tr>" 
					+ "</thead><tbody></tbody></table>");
			
			var idSet = new Array();
			for(var i in ds.members){
				if(idSet.indexOf(ds.members[i].memberId) < 0){
					idSet.push(ds.members[i].memberId);
				}
			}
			for(var i in ws.members){
				if(idSet.indexOf(ws.members[i].memberId) < 0){
					idSet.push(ws.members[i].memberId);
				}
			}
			for(var i in idSet){
				var id = idSet[i];
				
				var dm = findMember(ds, id);
				var wm = findMember(ws, id);
				
				var row = $j("<tr></tr>");
				if(dm){
					row.append("<td>" + dm.memberId + "</td>" 
							+ "<td>" + dm.memberName + "</td>"
							+ "<td>" + (dm.primaryKey ? "1" : "0") + "</td>"
							+ "<td>" + dm.defaultValue + "</td>");
				}else{
					row.append("<td></td><td></td><td></td><td></td>");
				}
				row.append("<td></td>");
				if(wm){
					var tdId = $j("<td>" + wm.memberId + "</td>");
					var tdName = $j("<td>" + wm.memberName + "</td>");
					var tdPk = $j("<td>" + (wm.primaryKey ? "1" : "0") + "</td>");
					var tdDefValue = $j("<td>" + wm.defaultValue + "</td>");
					if(null == dm){
						tdId.addClass("markItem");
						tdName.addClass("markItem");
						tdPk.addClass("markItem");
						tdDefValue.addClass("markItem");
					}else{
						if(dm.memberName != wm.memberName){
							tdName.addClass("markItem");
						}
						if(dm.primaryKey != wm.primaryKey){
							tdPk.addClass("markItem");
						}
						if(dm.defaultValue != wm.defaultValue){
							tdDefValue.addClass("markItem");
						}
					}
					
					row.append(tdId);
					row.append(tdName);
					row.append(tdPk);
					row.append(tdDefValue);
				}else{
					row.append("<td class='markItem'></td><td class='markItem'></td><td class='markItem'></td><td class='markItem'></td>");
				}
				tb.append(row);
			}
			
			
			this.div.append(tb);
		},
		show : function(){
			var tbCount = $j("table", this.div).length;
			if(tbCount <= 0) return;
			$j("#title", this.div).html("存在<span style='color: red;'>" + tbCount + "</span>个结构体的成员与DB不匹配");
			$j("#dbstructsDiv").append(this.div);
		}
};

function doCompare(webStructs, dbStructs){
	var dbstructsDiv = $j("#dbstructsDiv");
	dbstructsDiv.empty();
	
	//查找不在DB中的结构体
	for(var i in webStructs){
		var ws = webStructs[i];
		
		var ds = null;
		for(var j in dbStructs){
			var _ds = dbStructs[j];
			if(ws.structId == _ds.structId || ws.structName == _ds.structName){
				ds = _ds;
				break;
			}
		}
		
		if(null == ds){
			noInDBStructs.addStruct(ws);
		}
	}
	
	for(var i in dbStructs){
		var ds = dbStructs[i];
		
		var wsId = null;
		var wsName = null;
		for(var j in webStructs){
			var ws = webStructs[j];
			if(ws.structId == ds.structId){
				wsId = ws;
			}
			if(ws.structName == ds.structName){
				wsName = ws;
			}
		}
		
		//结构体不在WEB中
		if(null == wsId && null == wsName){
			noConfigStructs.addStruct(ds);
			continue;
		}
		
		//ID与名称不匹配
		if(wsId != wsName){
			idNotMatchNameStructs.addStruct(ds, wsId, wsName);
		}else{
			//成员数量不一致
			if(ds.members.length != wsId.members.length){
				memberUnmatchStructs.addStruct(ds, wsId);
			}else{
				//查找不一致的成员
				for(var i in ds.members){
					var m = ds.members[i];
					var wm = null;
					for(var j in wsId.members){
						var _wm = wsId.members[j];
						if(m.memberId == _wm.memberId){
							wm = _wm;
							break;
						}
					}
					
					if(null == wm 
							|| m.memberName != wm.memberName
							|| m.defaultValue != wm.defaultValue
							|| m.primaryKey != wm.primaryKey){
						memberUnmatchStructs.addStruct(ds, wsId);
						break;
					}
				}
			}
		}
	}
	
	noConfigStructs.show();
	noInDBStructs.show();
	idNotMatchNameStructs.show();
	memberUnmatchStructs.show();
}

$j(function(){
	//dwr.engine.setAsync(false);
	
	var dbStructs = null;
	var webStructs = null;
	var waitingDiv = $j("#waitingDiv");
	waitingDiv.text("正在加载WEB参数...");
	webStructService.getAllStructs($j("#projectId").val(), function(structs){
		webStructs = structs;
		
		waitingDiv.text("正在加载DB参数...");
		dbStructService.getAllStructs($j("#projectId").val(), function(structs){
			dbStructs = structs;
			
			if(null == dbStructs || null == webStructs){
				showErrMsg("数据读取失败。");
			}else{
				doCompare(webStructs, dbStructs);
			}
		});
	});
	
	
	//dwr.engine.setAsync(true);
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