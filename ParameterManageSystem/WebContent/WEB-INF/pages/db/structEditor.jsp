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
<title>DB Parameter Editor</title>

<link href="<%=basePath%>styles/common.css" rel="stylesheet"
	type="text/css" />
<style type="text/css">
body {
	padding: 0px 1px 0px 1px;
	overflow: auto;
}

#title {
	font-size: 18px;
	color: black;
	line-height: 38px;
	border-bottom: 1px dashed blue;
	padding-left: 10px;
	padding-right: 10px;
}

#btnDel {
	background: url('<%=basePath%>images/delete_24px.png') no-repeat center;
	position: absolute;
	display: none;
	cursor: pointer;
	border: 0px;
	height: 24px;
	width: 24px;
}

#btnDel:hover {
	background: url('<%=basePath%>images/delete_24px_light.png') no-repeat
		center;
}

.tbCommon input[type=text] {
	border: 0px;
	padding: 0px;
	margin: 0px;
	width: 100%;
	height: 100%;
	background-color: transparent;
}

.tbCommon input[type=checkbox] {
	border: 0px;
	padding: 0px;
	margin: 0px;
	width: 100%;
	background-color: transparent;
}

.tbCommon select {
	width: 100%;
	height: 100%;
}

.tbCommon option {
	background-color: transparent;
}

.structRowNum {
	background-color: #88bbee;
}

.noEdit {
	background-color: #dfdfdf;
}

#
#btnAdd {
	border: none;
	background: url('<%=basePath%>images/add_24px.png') no-repeat center;
	width: 24px;
	height: 24px;
	margin: 2px;
	padding: 0px;
	cursor: pointer;
}

#
#btnAdd:hover {
	background: url('<%=basePath%>images/add_24px_light.png') no-repeat
		center;
}
</style>

<script type="text/javascript"
	src="<%=basePath%>scripts/jquery/jquery.js"></script>

<script type="text/javascript">
	var currentRow = null;
	var tempRow = null;
	$(function() {
		//$("table tr:nth-child(even)").addClass("striped");
		tempRow = $(".tempRow").clone();
		$(".tempRow").remove();

		$("tbody").hover(function() {
			$("#btnDel").show();
		}, function() {
			$("#btnDel").hide();
		});

		$("#btnDel").hover(function() {
			$(this).show();
			return true;
		}, function() {
			$(this).hide();
			return true;
		}).click(function() {
			currentRow.remove();
			currentRow = null;
			$(this).hide();

			updateRowNum();
		});

		$("#btnAdd").click(function() {
			var row = genNewRow();
		});
	});

	function genNewRow() {
		var tr = tempRow.clone();
		tr.css("display", "");
		$(".structRowNum", tr)
				.text($("#structDetailTable tbody tr").length + 1);
		tr.hover(function() {
			var btnDel = $("#btnDel");
			var tr = $(this);
			currentRow = tr;
			var top = tr.position().top
					- (btnDel.outerHeight() - tr.outerHeight()) / 2;
			var left = tr.position().left + tr.outerWidth()
					- btnDel.outerWidth();

			btnDel.css({
				"left" : left,
				"top" : top
			});
		}, function() {
		});

		$(".valueType", tr).change(function() {
			var size = 0;
			$(".valueSize", tr).attr("readonly", "readonly");
			$(".valueSize", tr).parent().addClass("noEdit");
			switch ($(this).children('option:selected').val()) {
			case "Char": {
				size = 1;
				$(".valueSize", tr).removeAttr("readonly");
				$(".valueSize", tr).parent().removeClass("noEdit");
				break;
			}
			case "Uint32": {
				size = 4;
				break;
			}
			case "Uint16": {
				size = 2;
				break;
			}
			case "Uint8": {
				size = 1;
				break;
			}
			case "Int32": {
				size = 4;
				break;
			}
			case "Int16": {
				size = 2;
				break;
			}
			case "Int8": {
				size = 1;
				break;
			}
			}
			$(".valueSize", tr).val(size);
		});

		$("#structDetailTable tbody").append(tr);

	}

	function updateRowNum() {
		var i = 1;
		$("#structDetailTable tbody tr").each(function() {
			$(".structRowNum", $(this)).text(i);
			i += 1;
		});
	}
</script>

</head>
<body>
	<div id="title">
		<span style="float: left;">${structName }</span>
		<div style="float: right;">
			<%
				String editType = (String) request.getParameter("editType");
				if(null == editType) editType = "";
				if (editType.equals("new")) {
			%>
			<button class="button button_img"
				style="background-image: url('<%=basePath%>images/resizeApi 35.png');">Save</button>
			<%
				} else if (editType.equals("edit")) {
			%>
			<button class="button button_img"
				style="background-image: url('<%=basePath%>images/resizeApi 35.png');">Save</button>
			<button class="button button_img"
				style="background-image: url('<%=basePath%>images/delete_24px_2.png');">Delete</button>
			<%
				}
			%>
		</div>
		<div style="clear: both;"></div>
	</div>
	<div>
		<div>
			<span>StructName:&nbsp;</span><input type="text" id="structId"
				value="${structName }" /> <span>StructId:&nbsp;</span><input
				type="text" id="structId" value="${structId }" />
		</div>
		<div>
			<div>
				<table class="tbCommon" id="structDetailTable">
					<thead>
						<tr>
							<th></th>
							<th>ElementId</th>
							<th>ElementName</th>
							<th>IsPrimaryKey</th>
							<th>ValueType</th>
							<th>ValueSize</th>
						</tr>
					</thead>
					<tbody>
						<tr style="display: none;" class="tempRow">
							<td class="structRowNum">1</td>
							<td><input type="text" value="" class="elementId" /></td>
							<td><input type="text" value="" class="elementName" /></td>
							<td><input type="checkbox" class="isPK" /></td>
							<td><select class="valueType">
									<option>Char</option>
									<option>Uint32</option>
									<option>Uint16</option>
									<option>Uint8</option>
									<option>Int32</option>
									<option>Int16</option>
									<option>Int8</option>
							</select></td>
							<td><input type="text" value="1" class="valueSize" /></td>
						</tr>
					</tbody>
				</table>
			</div>
			<div>
				<%
					/*
				%><button id="btnAdd"></button>
				<%
					*/
				%>

				<%
					if (editType.equals("new") || editType.equals("edit")) {
				%>
				<button id="btnAdd" class="button button_img"
					style="background-image: url('<%=basePath%>images/add-mini.png');">New
					Element</button>
				<%
					}
				%>
			</div>

			<button id="btnDel"></button>

		</div>
	</div>
</body>
</html>