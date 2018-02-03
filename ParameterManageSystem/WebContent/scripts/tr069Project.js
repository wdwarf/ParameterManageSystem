/**
 * 
 */
var $j = jQuery.noConflict();
var structListItems = [];
var currentStruct = null;

var webTypesMap = {
		0 : "文本框",
		1 : "下拉框",
		2 : "数组",
		3 : "密码",
		4 : "多选框",
		9 : "bitmap",
		10 : "只读文本框",
		11 : "只读下拉框",
		12 : "只读数组",
		13 : "只读密码",
		14 : "只读多选框"
};

function containerLoad(url) {
	$j("#main_container_iframe").attr("src", url);
}

function clearEditArea() {
	$j("#structDetailTable tbody tr[class!=tempRow]").remove();
	$j("#structNameEdit").val("");
	$j("#structIdEdit").val("");
	$j("#snmpstructId").val("");
	$j("#btnCompareResult").hide();
}

function listStructs() {
	var searchStr = $j("#structListSearchEdit").val().toLowerCase();
	$j("#structList").empty();
	for ( var i in structListItems) {
		if ("" == searchStr) {
			$j("#structList").append(structListItems[i]);
			continue;
		}
		var structName = $j("a", structListItems[i]).text().toLowerCase();
		if (structName.indexOf(searchStr) >= 0) {
			$j("#structList").append(structListItems[i]);
		}
	}
}

/**
 * 加载结构体列表
 * 
 * @returns
 */
function loadStructList() {
	clearEditArea();
	$j("#structList").empty();
	structListItems = [];
	snmpStructService.getAllStructNames($j("#projectId").val(), function(structs) {
		console.log(structs);
		$j("#spanStructCount").text("[" + structs.length + "]");
		structList = structs;
		for ( var i in structs) {
			var structName = structs[i];
			var displayName = structName;
			if ("" == displayName) {
				displayName = "[empty]";
			}
			var item = $j("<li><a title=\"" + structName + "\" href=\"javascript:void(0)\" onclick=\"showStruct('" + structName
					+ "')\">" + displayName + "</a></li>");
			structListItems.push(item);
		}
		listStructs();
	});
}

function setCheckboxStatus(obj, isChecked) {
	if (isChecked) {
		$j(obj).attr("checked", "checked");
	} else {
		$j(obj).removeAttr("checked");
	}
}

function valueTypeChanged(e) {
	var tr = findParent(e, "TR");

	var size = 0;
	var valueSizeElmt = $j("input[name=valueSize]", tr);
	if (valueSizeElmt.length <= 0) {
		valueSizeElmt = $j("input[name=valueSize]", tr.parent());
	}
	valueSizeElmt.attr("readonly", "readonly");
	valueSizeElmt.parent().addClass("noEdit");
	switch ($j(e).children('option:selected').val()) {
	case "Char": {
		size = 1;
		valueSizeElmt.removeAttr("readonly");
		valueSizeElmt.parent().removeClass("noEdit");
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
	valueSizeElmt.val(size);
}

function genNewRow() {
	var tr = $j(".tempRow").clone();
	tr.removeClass("tempRow");
	tr.css("display", "");
	tr.on("dblclick", function(){ editRow(tr[0]); });
	$j(".tbRowNum", tr).text($j("#structDetailTable tbody tr[class!=tempRow]").length + 1);

	return tr;
}

function addNewEmptyRow() {
	$j("#structDetailTable tbody").append(genNewRow());
	updateRowNum();
}

function updateRowNum() {
	var i = 1;
	$j("#structDetailTable tbody tr[class!=tempRow]").each(function() {
		$j(".tbRowNum", $j(this)).text(i);
		i += 1;
	});
}

function deleteRow(e) {
	var parent = findParent(e, "TR");
	parent.remove();
	updateRowNum();
}

/**
 * 行编辑
 * 
 * @param obj
 * @returns
 */
function editRow(obj) {
	var elmtDlg = $j("#editRowDlg");
	var e = {};
	var row = findParent(obj, "TR");
	
	elmtDlg[0].row = row;
	
	if(row[0].member){
		var member = row[0].member;
		$j("input[name=oid]", elmtDlg).val(member.oid);
		$j("input[name=elementName]", elmtDlg).val(member.elementName);
		setCheckboxStatus($j("input[name=isWritable]", elmtDlg), member.writable);
		setCheckboxStatus($j("input[name=isPrimaryKey]", elmtDlg), member.primaryKey);
		$j("select[name=dataType]", elmtDlg).val(member.dataType);
		$j("input[name=structName]", elmtDlg).val(member.structName);
		$j("input[name=memberName]", elmtDlg).val(member.memberName);
	}else{
		$j("input[name=oid]", elmtDlg).val("");
		$j("input[name=elementName]", elmtDlg).val("");
		setCheckboxStatus($j("input[name=isWritable]", elmtDlg), false);
		setCheckboxStatus($j("input[name=isPrimaryKey]", elmtDlg), false);
		$j("select[name=dataType]", elmtDlg).val("");
		$j("input[name=structName]", elmtDlg).val("");
		$j("input[name=memberName]", elmtDlg).val("");
	}
	
	elmtDlg.dialog({
		height: ($j("body").height() * 0.8),
		width: ($j("body").width() * 0.65)
	});
	elmtDlg.dialog("open");
}

function setRowValues(row, member){
	$j("span[field=oid]", row).text(member.oid);
	$j("span[field=elementName]", row).text(member.elementName);
	setCheckboxStatus($j("input[name=isWritable]", row), member.writable);
	setCheckboxStatus($j("input[name=isPrimaryKey]", row), member.primaryKey);
	$j("span[field=dataType]", row).text(member.dataType);
	$j("span[field=structName]", row).text(member.structName);
	$j("span[field=memberName]", row).text(member.memberName);
	
	$j("input[name=isPrimaryKey]", row).trigger("change");
}

/**
 * 确认编辑结果
 * 
 * @returns
 */
function confirmEditRow() {
	var elmtDlg = $j("#editRowDlg");
	var row = elmtDlg[0].row;

	row[0].member = {};
	var member = row[0].member;
	member.oid = $j("input[name=oid]", elmtDlg).val();
	member.elementName = $j("input[name=elementName]", elmtDlg).val();
	member.writable = $j("input[name=isWritable]", elmtDlg).is(":checked");
	member.primaryKey = $j("input[name=isPrimaryKey]", elmtDlg).is(":checked");
	member.dataType = $j("select[name=dataType] option:selected", elmtDlg).val();
	member.structName = $j("input[name=structName]", elmtDlg).val();
	member.memberName = $j("input[name=memberName]", elmtDlg).val();
	
	setRowValues(row, member);
}

function checkStructMenuItem(structName) {
	$j("li[class=structItemChecked]", $j("#structList")).removeClass("structItemChecked");
	for ( var i in structListItems) {
		if (structName == $j("a", structListItems[i]).text()) {
			structListItems[i].addClass("structItemChecked");
		}
	}
}

/**
 * 显示结构体
 * 
 * @param structName
 * @returns
 */
function showStruct(structName) {
	currentStruct = null;
	snmpStructService.getStructByStructName($j("#projectId").val(), structName, function(struct) {
		clearEditArea();
		currentStruct = struct;
		currentDefInstance = {};

		console.log(struct);
		
		$j("#structNameEdit").val(struct.structName);
		$j("#oidEdit").val(struct.oid);
		setCheckboxStatus($j("#isSingleTableCheckbox"), struct.singleTable);
		$j("#snmpstructId").val(struct.snmpstructId);

		for ( var i in struct.members) {
			var row = genNewRow();
			var member = struct.members[i];
			row[0].member = member;

			setRowValues(row, member);
			$j("#structDetailTable tbody").append(row);
		}
		
		checkStructMenuItem(structName);
		$j("a[title=" + struct.structName + "]").focus();
		//compareWithDBStruct();
	});

}

function showCompareResultDlg(){
	var dlg = $j("#compareResultDlg");
	dlg.dialog({
		title: "存在参数配置不一致问题",
		modal: true,
		show: window._dialogEffectName,
		hide : window._dialogEffectName,
		height: ($j("body").height() * 0.75),
		width: ($j("body").width() * 0.75)
	});
}

function compareWithDBStruct(){
	var projectId = $j("#projectId").val();
	dbStructService.getStruct(projectId, parseInt(currentStruct.structId), function(struct){
		var dlgInfo = "";
		
		if(null == struct){
			dlgInfo += "<li>结构体" + currentStruct.structName + "[" + currentStruct.structId + "] 不存在于DB配置里</li>";
		}else{
			if(struct.structId != currentStruct.structId && struct.structName != currentStruct.structName){
				return;
			}
		
			if(struct.structName != currentStruct.structName){
				dlgInfo += "<li>StructName[" + currentStruct.structName + "] 与DB参数[" + struct.structName + "]不一致</li>";
			}
		
			for(var i in currentStruct.members){
				var webMember = currentStruct.members[i];
				var memberFound = false;
			
				for ( var j in struct.members) {
					var dbMember = struct.members[j];
				
					if(dbMember.memberId == webMember.memberId || dbMember.memberName == webMember.memberName){
						memberFound = true;
				
						if(dbMember.memberId != webMember.memberId){
							dlgInfo += "<li>成员MemberID[" + webMember.memberId + "] 与DB参数[" + dbMember.memberId + "]不一致</li>";
						}
					
						if(dbMember.memberName != webMember.memberName){
							dlgInfo += "<li>成员MemberName[" + webMember.memberName + "] 与DB参数[" + dbMember.memberName + "]不一致</li>";
						}

						if(dbMember.defaultValue != webMember.defaultValue){
							dlgInfo += "<li>成员" + webMember.memberName + ".DefaultValue[" + webMember.defaultValue + "] 与DB参数[" + dbMember.defaultValue + "]不一致</li>";
						}

						if(dbMember.primaryKey != webMember.primaryKey){
							dlgInfo += "<li>成员" + webMember.memberName + ".IsPrimaryKey[" + webMember.primaryKey + "] 与DB参数[" + dbMember.primaryKey + "]不一致</li>";
						}
						break;
					}
				}

				if(!memberFound){
					dlgInfo += "<li>多余的成员" + webMember.memberName + "[" + webMember.memberId + "]</li>";
				}
			}
		
			for ( var j in struct.members) {
				var dbMember = struct.members[j];
				var memberFound = false;
			
				for(var i in currentStruct.members){
					var webMember = currentStruct.members[i];
				
					if(dbMember.memberId == webMember.memberId || dbMember.memberName == webMember.memberName){
						memberFound = true;
						break;
					}
				}

				if(!memberFound){
					dlgInfo += "<li>DB成员" + dbMember.memberName + "[" + dbMember.memberId + "]未配置</li>";
				}
			}
		}
		
		if("" != dlgInfo){
			var dlg = $j("#compareResultDlg");
			dlg.empty();
			dlg.append("<ul>" + dlgInfo + "</ul></div>");
			$j("#btnCompareResult img").attr("src", "../images/alarm_red.gif");
			$j("#btnCompareResult span").text($j("li", dlg).length);
			$j("#btnCompareResult").show();
		}
	});
}

function matchWithDBStruct(){
	var projectId = $j("#projectId").val();
	var structName = $j("#structNameEdit").val();
	var structId = $j("#structIdEdit").val();
	var snmpstructId = $j("#snmpstructId").val();
	if("" == structName && "" == structId){
		return;
	}
	
	dbStructService.getStruct(projectId, parseInt(structId), function(struct){
		if(null == struct){
			return;
		}
		
		clearEditArea();
		$j("#snmpstructId").val(snmpstructId);
		$j("#structNameEdit").val(struct.structName);
		$j("#structIdEdit").val(struct.structId);
		$j("#structCnNameEdit").val(currentStruct.structCnName);
		$j("#classifyNameEdit").val(currentStruct.classifyName);
		
		for ( var i in struct.members) {
			var row = genNewRow();
			var member = struct.members[i];
			
			var webStructMember = {};
			webStructMember.memberId = member.memberId;
			webStructMember.memberName = member.memberName;
			webStructMember.primaryKey = member.primaryKey;
			webStructMember.defaultValue = member.defaultValue;
			webStructMember.memberCnName = "";
			webStructMember.webType = "0";
			webStructMember.typeDesc = "";
			webStructMember.unit = "";
			webStructMember.valueRangeCn = "";
			webStructMember.memberLevel = "0";
			webStructMember.structLevel = "0";
			webStructMember.canAddOrDelete = false;
			webStructMember.unique = false;
			webStructMember.valueRange = "";
			webStructMember.rebootEffective = false;
			webStructMember.setStatus = "";
			
			for(var mi in currentStruct.members){
				var wm = currentStruct.members[mi];

				if(wm.memberId == webStructMember.memberId || wm.memberName == webStructMember.memberName){
					webStructMember.memberCnName = wm.memberCnName;
					webStructMember.webType = wm.webType;
					webStructMember.typeDesc = wm.typeDesc;
					webStructMember.unit = wm.unit;
					webStructMember.valueRangeCn = wm.valueRangeCn;
					webStructMember.memberLevel = wm.memberLevel;
					webStructMember.structLevel = wm.structLevel;
					webStructMember.canAddOrDelete = wm.canAddOrDelete;
					webStructMember.unique = wm.unique;
					webStructMember.valueRange = wm.valueRange;
					webStructMember.rebootEffective = wm.rebootEffective;
					webStructMember.setStatus = wm.setStatus;
					break;
				}
			}
			
			row[0].member = webStructMember;
			setRowValues(row, webStructMember);
			
			$j("input[name=isPrimaryKey]", row).trigger("change");
			$j("#structDetailTable tbody").append(row);
		}
	});
}

function isEmptyStr(str){
	if(null == str){
		return true;
	}
	
	if(typeof str === "undefined"){
		return true;
	}
	
	if(typeof str === "string"){
		if("" == str){
			return true;
		}
	}
	
	
	
	return false;
}

function structCheck(struct){
	if(null == struct || "undefined" == struct){
		throw "null object";
	}
	
	if(isEmptyStr(struct.structName)) throw "invlaid value[structName]";
	if(isEmptyStr(struct.oid)) throw "invlaid value[oid]";
	if(isEmptyStr(struct.projectId)) throw "invlaid value[projectId]";
	
	/*
	if(typeof struct.members === 'undefined'){
		return false;
	}
	*/
	
	if(typeof struct.members === 'object'){
		if(struct.members.length <= 0){
			throw "no members";
		}
		
		for(var i in struct.members){
			var e = struct.members[i];
			
			if(isEmptyStr(e.elementName)) throw "invlaid member value[elementName]";
			if(isEmptyStr(e.oid)) throw "invlaid member value[oid]";
			if("RowStatus" != e.elementName){
				if(isEmptyStr(e.structName)) throw "invlaid member value[structName]";
				if(isEmptyStr(e.memberName)) throw "invlaid member value[memberName]";
			}
			
			if(isEmptyStr(e.primaryKey)) throw "invlaid member value[primaryKey]";
		}
	}
	
	return true;
}

function wrapStructData(){
	var s = {};
	var snmpstructId = $j("#snmpstructId").val().trim();
	s.projectId = $j("#projectId").val().trim();
	s.structName = $j("#structNameEdit").val().trim();
	s.oid = $j("#oidEdit").val().trim();
	s.singleTable = $j("#isSingleTableCheckbox").is(":checked");

	if(!isEmptyStr(snmpstructId)){
		s.snmpstructId = snmpstructId;
	}
	
	s.members = new Array();

	$j("#structDetailTable tbody tr[class!=tempRow]").each(function() {
		var row = $j(this);
		if(row[0].member){
			s.members.push(row[0].member);
		}
	});
	
	return s;
}

/**
 * 新增结构体
 * 
 * @returns
 */
function addNewStruct() {
	var s = wrapStructData();
	delete s.snmpstructId;
	
	try{
		structCheck(s);
	}catch(e){
		showErrMsg(e);
		return;
	}

	snmpStructService.addStruct(s, function() {
		dwr.engine.setAsync(false);
		loadStructList();
		showStruct(s.structName);
		dwr.engine.setAsync(true);
		showInfoMsg("参数保存成功。", "新增");
	});
}

/**
 * 更新结构体
 * 
 * @returns
 */
function updateStruct() {
	var s = wrapStructData();
	
	try{
		structCheck(s);
	}catch(e){
		showErrMsg(e);
		return;
	}
	
	snmpStructService.updateStruct(s, function(e) {
		dwr.engine.setAsync(false);
		loadStructList();
		showStruct(s.structName);
		dwr.engine.setAsync(true);
		showInfoMsg("参数保存成功。", "保存");
	});
}

/**
 * 删除结构体
 * 
 * @returns
 */
function deleteStruct() {
	var snmpstructId = $j("#snmpstructId").val().trim();
	if("" == snmpstructId){
		showErrMsg("请选择要删除的结构体");
		return;
	}
	
	$j("<div style='color: red;'>确定要删除结构体[ " + currentStruct.structName + " ] ?</div>").dialog({
		title: "删除结构体",
		modal : true,
		show: window._dialogEffectName,
		hide : window._dialogEffectName,
		buttons : [
			{
				text : "确定",
				click : function(){
					snmpStructService.deleteStruct(snmpstructId, function(e) {
						loadStructList();
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

function trimQuoter(val) {
	if ('\"' == val.charAt(0) && '\"' == val.charAt(val.length - 1)) {
		val = val.substr(1, val.length - 2);
	}
	return val;
}

function parseBoolean(val) {
	if (null == val || "" == val || "undefined" == val) {
		return false;
	}

	if ("true" == String(val).toLowerCase() || 1 == parseInt(val)) {
		return true;
	}

	return false;
}

function showImportDlg() {
	$j("#importDlg").dialog({
		title : "导入结构体数据",
		modal : true,
		show: window._dialogEffectName,
		hide : window._dialogEffectName,
		buttons : [ {
			text : "导入",
			click : function() {
				if("" == $j("#importZipFile").val()){
					return;
				}
				showWaitingDlg();
				$j("#importStructForm").submit();
			}
		} ]
	});
}

function showExportDlg(){
	$j("#exportDlg").dialog({
		title : "导出结构体数据",
		modal : true,
		show: window._dialogEffectName,
		hide : window._dialogEffectName,
		resizable: false
	});
}

function cbPrimaryKeyStateChange(e) {
	var parent = findParent(e, "TR");

	if ($j(e).is(":checked")) {
		parent.addClass("primaryKeyRow");
	} else {
		parent.removeClass("primaryKeyRow");
	}
}

$j(function() {
	$j("#editRowDlg").dialog({
		title : "成员编辑",
		autoOpen : false,
		modal : true,
		show: window._dialogEffectName,
		hide : window._dialogEffectName,
		buttons : [ {
			text : "OK",
			click : function() {
				confirmEditRow();
				$j(this).dialog("close");
			}
		}, {
			text : "Cancel",
			click : function() {
				$j(this).dialog("close");
			}
		} ]
	});

	$j("#structListSearchEdit").keyup(function() {
		listStructs();
	});
	
	for(var key in webTypesMap){
		$j("#webTypeSelect").append("<option value=\"" + key + "\">" + webTypesMap[key] + "</option>");
	}

	loadStructList();
});