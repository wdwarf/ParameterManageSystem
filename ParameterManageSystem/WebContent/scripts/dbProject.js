/**
 * 
 */
var $j = jQuery.noConflict();
var structListItems = [];
var currentStruct = null;
var currentDefInstance = {};

function containerLoad(url) {
	$j("#main_container_iframe").attr("src", url);
}

function clearEditArea() {
	$j("#structDetailTable tbody tr[class!=tempRow]").remove();
	$j("#structNameEdit").val("");
	$j("#structIdEdit").val("");
	$j("#dbstructId").val("");
	$j("#cbIsTempTable").removeAttr("checked");
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
		var structId = parseInt($j("a", structListItems[i]).attr("structId"));
		if (structName.indexOf(searchStr) >= 0 
				|| structId == parseInt(searchStr)) {
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
	/*
	dbStructService.getAllStructNames($j("#projectId").val(), function(structs) {
		//console.log(structs);
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
	*/
	dbStructService.getAllStructIdsAndNames($j("#projectId").val(), function(structs) {
		//console.log(structs);
		$j("#spanStructCount").text("[" + structs.length + "]");
		structList = structs;
		for ( var i in structs) {
			var structName = structs[i].structName;
			var structId = structs[i].structId;
			var displayName = structName;
			if ("" == displayName) {
				displayName = "[empty]";
			}
			var item = $j("<li><a title=\"" + structName + "\" structId=\"" + structId + "\" href=\"javascript:void(0)\" onclick=\"showStruct('" + structName
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
	var tr = $j(e);
	while (tr && "TR" != tr[0].tagName.toUpperCase()) {
		tr = tr.parent();
	}

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
	case "Boolean": {
		size = 1;
		break;
	}
	case "Uint64": {
		size = 8;
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
	case "Int64": {
		size = 8;
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
	var parent = $j(e);
	while (parent && "TR" != parent[0].tagName.toUpperCase()) {
		parent = parent.parent();
	}
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
	try{
		checkProjectAuth($j("#projectId").val());
	}catch(msg){
		showErrMsg(msg);
		return;
	}
	
	var elmtDlg = $j("#editRowDlg");
	var e = {};
	var row = $j(obj);
	while (row && "TR" != row[0].tagName.toUpperCase()) {
		row = row.parent();
	}

	elmtDlg[0].row = row;

	e.memberId = $j("input[name=memberId]", row).val().trim();
	e.memberName = $j("input[name=memberName]", row).val().trim();
	e.primaryKey = $j("input[name=isPrimaryKey]", row).is(":checked");
	e.memberType = $j("select[name=valueType]", row).val().trim();
	e.memberSize = $j("input[name=valueSize]", row).val().trim();
	e.defaultValue = $j("input[name=defaultValue]", row).val().trim();
	e.valueRegex = $j("input[name=valueRegex]", row).val().trim();
	e.refStruct = $j("input[name=refStruct]", row).val().trim();
	e.refMember = $j("input[name=refMember]", row).val().trim();
	e.unique = $j("input[name=isUnique]", row).is(":checked");
	e.memo = $j("input[name=memo]", row).val();

	$j("input[name=memberId]", elmtDlg).val(e.memberId);
	$j("input[name=memberName]", elmtDlg).val(e.memberName);
	setCheckboxStatus($j("input[name=isPrimaryKey]", elmtDlg), e.primaryKey);
	$j("select[name=valueType]", elmtDlg).val(e.memberType);
	$j("select[name=valueType]", elmtDlg).trigger("change");
	$j("input[name=valueSize]", elmtDlg).val(e.memberSize);
	$j("input[name=defaultValue]", elmtDlg).val(e.defaultValue);
	$j("input[name=valueRegex]", elmtDlg).val(e.valueRegex);
	$j("input[name=refStruct]", elmtDlg).val(e.refStruct);
	$j("input[name=refMember]", elmtDlg).val(e.refMember);
	setCheckboxStatus($j("input[name=isUnique]", elmtDlg), e.unique);
	$j("textarea[name=memo]", elmtDlg).val(e.memo);

	elmtDlg.dialog("open");
}

function setRowValues(row, member){
	$j("input[name=memberId]", row).val(member.memberId);
	$j("input[name=memberName]", row).val(member.memberName);
	var primaryKeyItem = $j("input[name=isPrimaryKey]", row);
	setCheckboxStatus(primaryKeyItem, member.primaryKey);
	cbPrimaryKeyStateChange(primaryKeyItem);
	var valueTypeElmt = $j("select[name=valueType]", row);
	valueTypeElmt.val(member.memberType);
	valueTypeElmt.trigger("change");
	$j("input[name=valueSize]", row).val(member.memberSize);
	$j("input[name=defaultValue]", row).val(member.defaultValue);
	$j("input[name=valueRegex]", row).val(member.valueRegex);
	$j("input[name=refStruct]", row).val(member.refStruct);
	$j("input[name=refMember]", row).val(member.refMember);
	setCheckboxStatus($j("input[name=isUnique]", row), member.unique);
	$j("input[name=memo]", row).val(member.memo);
	
	var structId = parseInt($j("#structIdEdit").val());
	if(!structId){
		structId = 0;
	}
	var title = member.memberName + "[" + (structId * 1000 + parseInt(member.memberId)) + "]";
	if(member.memo){
		title += "\n" + member.memo;
	}
	row.attr("title", title);
}

/**
 * 确认编辑结果
 * 
 * @returns
 */
function confirmEditRow() {
	var elmtDlg = $j("#editRowDlg");
	var row = elmtDlg[0].row;

	var member = {};
	member.memberId = $j("input[name=memberId]", elmtDlg).val();
	member.memberName = $j("input[name=memberName]", elmtDlg).val();
	member.primaryKey = $j("input[name=isPrimaryKey]", elmtDlg).is(":checked");
	member.memberType = $j("select[name=valueType]", elmtDlg).val();
	member.memberSize = $j("input[name=valueSize]", elmtDlg).val();
	member.defaultValue = $j("input[name=defaultValue]", elmtDlg).val();
	member.valueRegex = $j("input[name=valueRegex]", elmtDlg).val();
	member.refStruct = $j("input[name=refStruct]", elmtDlg).val();
	member.refMember = $j("input[name=refMember]", elmtDlg).val();
	member.unique = $j("input[name=isUnique]", elmtDlg).is(":checked");
	member.memo = $j("textarea[name=memo]", elmtDlg).val();
	
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
	dbStructService.getStruct($j("#projectId").val(), structName, function(struct) {
		clearEditArea();
		currentDefInstance = {};

		currentStruct = struct;
		console.log(struct);
		
		$j("#structNameEdit").val(struct.structName);
		$j("#structIdEdit").val(struct.structId);
		$j("#dbstructId").val(struct.dbstructId);
		setCheckboxStatus($j("#cbIsTempTable"), struct.tempTable);

		for ( var i in struct.members) {
			var row = genNewRow();
			var e = struct.members[i];

			setRowValues(row, e);
			
			$j("#structDetailTable tbody").append(row);

			currentDefInstance[e.memberName] = e.defInstances;
		}
		
		checkStructMenuItem(structName);
		$j("a[title=" + struct.structName + "]").focus();
	});

}

function resetStructInfo(){
	if(null == currentStruct){
		clearEditArea();
		return;
	}
	showStruct(currentStruct.structName);
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
	if(isEmptyStr(struct.structId)) throw "invlaid value[structId]";
	if(isEmptyStr(struct.tempTable)) throw "invlaid value[tempTable]";
	if(isEmptyStr(struct.projectId)) throw "invlaid value[projectId]";
	
	var structId = parseInt(struct.structId);
	if(typeof structId === 'number' && (isNaN(structId) || structId <= 0)){
		throw "invlaid value[structId]";
	}
	
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
			
			if(isEmptyStr(e.memberId)) throw "invlaid value[memberId]";
			if(isEmptyStr(e.memberName)) throw "invlaid value[memberName]";
			if(isEmptyStr(e.primaryKey)) throw "invlaid value[primaryKey]";
			if(isEmptyStr(e.memberType)) throw "invlaid value[memberType]";
			
			var memberId = parseInt(e.memberId);
			if(typeof memberId === 'number' && (isNaN(memberId) || (memberId <= 0 || memberId > 999))){
				throw "invlaid value[memberId]: " + memberId;
			}
			
		}
	}
	
	return true;
}

function wrapStructData(){
	var s = {};
	var dbstructId = $j("#dbstructId").val().trim();
	s.structName = $j("#structNameEdit").val().trim();
	s.structId = $j("#structIdEdit").val().trim();
	s.tempTable = $j("#cbIsTempTable").is(":checked");
	s.projectId = $j("#projectId").val().trim();
	s.members = [];

	if(!isEmptyStr(dbstructId)){
		s.dbstructId = dbstructId;
	}
	
	var hasPrimaryKey = false;
	
	var rows = $j("#structDetailTable tbody tr[class!=tempRow]");
	
	//判断有没有主键成员
	rows.each(function() {
		var isPrimaryKey = $j("input[name=isPrimaryKey]", $j(this)).is(":checked");
		if(isPrimaryKey){
			hasPrimaryKey = true;
		}
	});
	
	rows.each(function() {
		var e = {};
		var thisElmt = $j(this);
		var dbstructmemberId = $j("input[name=dbstructmemberId]", thisElmt).val().trim();
		e.memberId = $j("input[name=memberId]", thisElmt).val().trim();
		e.memberName = $j("input[name=memberName]", thisElmt).val().trim();
		e.primaryKey = $j("input[name=isPrimaryKey]", thisElmt).is(":checked");
		e.memberType = $j("select[name=valueType]", thisElmt).val().trim();
		e.memberSize = $j("input[name=valueSize]", thisElmt).val().trim();
		e.defaultValue = $j("input[name=defaultValue]", thisElmt).val().trim();
		e.valueRegex = $j("input[name=valueRegex]", thisElmt).val().trim();
		e.refStruct = $j("input[name=refStruct]", thisElmt).val().trim();
		e.refMember = $j("input[name=refMember]", thisElmt).val().trim();
		e.unique = $j("input[name=isUnique]", thisElmt).is(":checked");
		e.memo = $j("input[name=memo]", thisElmt).val().trim();

		//非临时表且有主键，才能配置默认实例
		if(!s.tempTable && hasPrimaryKey){
			e.defInstances = new Array();
			var newDefIns = currentDefInstance[e.memberName];
			if(newDefIns){
				for(var i in newDefIns){
					var instance = newDefIns[i];
					var newInstance = {};
					newInstance.defValue = instance.defValue;
					e.defInstances.push(newInstance);
				}
			}
		}
		
		s.members.push(e);
	});
	
	return s;
}

/**
 * 新增结构体
 * 
 * @returns
 */
function addNewStruct() {
	try{
		checkProjectAuth($j("#projectId").val());
	}catch(msg){
		showErrMsg(msg);
		return;
	}
	
	var s = wrapStructData();
	
	try{
		structCheck(s);
	}catch(e){
		showErrMsg(e);
		return;
	}

	dbStructService.addStruct(s, function() {
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
	try{
		checkProjectAuth($j("#projectId").val());
	}catch(msg){
		showErrMsg(msg);
		return;
	}
	
	var s = wrapStructData();

	if(isEmptyStr(s.dbstructId)){
		showErrMsg("请选择要更新的结构体");
		return;
	}
	
	try{
		structCheck(s);
	}catch(e){
		showErrMsg(e);
		return;
	}

	dbStructService.updateStruct(s, function(e) {
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
	var dbstructId = $j("#dbstructId").val().trim();
	if("" == dbstructId){
		showErrMsg("请选择要删除的结构体");
		return;
	}
	
	try{
		checkProjectAuth($j("#projectId").val());
	}catch(msg){
		showErrMsg(msg);
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
					dbStructService.deleteStruct(dbstructId, function(e) {
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
	try{
		checkProjectAuth($j("#projectId").val());
	}catch(msg){
		showErrMsg(msg);
		return;
	}
	
	$j("#importDlg").dialog({
		title : "导入结构体数据",
		modal : true,
		show: window._dialogEffectName,
		hide : window._dialogEffectName,
		buttons : [ {
			text : "导入",
			click : function() {
				showWaitingDlg();
				if("" == $j("#importZipFile").val()){
					return;
				}
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
	var parent = $j(e);
	while (parent && "TR" != parent[0].tagName.toUpperCase()) {
		parent = parent.parent();
	}

	if ($j(e).is(":checked")) {
		parent.addClass("primaryKeyRow");
	} else {
		parent.removeClass("primaryKeyRow");
	}
}

///////////////////////////////////////////////////////////////////////
//默认实例相关
///////////////////////////////////////////////////////////////////////

function defaultValuesEdit(){
	var isTempTable = $j("#cbIsTempTable").is(":checked");
	if(isTempTable){
		showInfoMsg("非临时表才可编辑默认实例", "默认实例");
		return;
	}
	
	var tb = $j("#tbDefaultInstance");
	tb.empty();
	var thead = $j("<thead></thead>");
	var trHead = $j("<tr class=\"titleRow\"></tr>");
	thead.append(trHead);
	tb.append(thead);
	
	var hasPrimarykey = false;
	var hasEmptyMemberName = false;
	var structName = $j("#structNameEdit").val().trim();
	var newRow = "<tr>";
	var rows = $j("#structDetailTable tbody tr[class!=tempRow]");
	if(0 == rows.length){
		showInfoMsg("没有成员", "默认实例");
		return;
	}
	rows.each(function() {
		var thisElmt = $j(this);
		var memberName = $j("input[name=memberName]", thisElmt).val().trim();
		if("" == memberName){
			hasEmptyMemberName = true;
		}
		var primaryKey = $j("input[name=isPrimaryKey]", thisElmt).is(":checked");
		if(primaryKey){
			hasPrimarykey = true;
		}
		trHead.append($j("<th>" + memberName + "</th>"));
		
		newRow += "<td><input type=\"text\" name=\"" + memberName + "\"></td>";
	});
	
	if(hasEmptyMemberName){
		showInfoMsg("请先配置成员名称", "默认实例");
		return;
	}
	
	if(!hasPrimarykey){
		showInfoMsg("多实例参数(有主键)才可编辑默认实例", "默认实例");
		return;
	}
	
	trHead.append($j("<th></th>"));
	newRow += "<td style=\"text-align:center;\"><a class=\"button alert\" href=\"javascript:void(0)\" onclick=\"deleteInstance(this);\">X</a></td></tr>";
	tb[0].newRow = newRow;
	
	for(var memberName in currentDefInstance){
		var instances = currentDefInstance[memberName];
		if(instances){
			var rows = $j("tr[class!=titleRow]", tb);
			var addRowCount = instances.length - rows.length;
			for(var i = 0; i < addRowCount; ++i){
				tb.append($j(newRow));
			}
			
			var index = 0;
			$j("input[name=" + memberName + "]", tb).each(function(){
				if(index < instances.length){
					$j(this).val(instances[index].defValue);
				}
				++index;
			});
		}
	}
	
	$j("#defaultValueEditDlg").dialog({
		title: "结构体[" + structName + "]默认实例",
		modal: true,
		show: window._dialogEffectName,
		hide : window._dialogEffectName,
		height: ($j("body").height() * 0.95),
		width: ($j("body").width() * 0.95),
		buttons: [{
			text: "确定",
			click: function(){
				currentDefInstance = {};
				var tb = $j("#tbDefaultInstance");
				$j("th", $j("tr[class=titleRow]", tb)).each(function() {
					//alert($j(this).text());
					var memberName = $j(this).text();
					var values = new Array();
					$j("input[name=" + memberName + "]", tb).each(function(){
						var instance = {};
						instance.defValue = $j(this).val();
						values.push(instance);
					});
					currentDefInstance[memberName] = values;
				});
				$j(this).dialog("close");
			}
		}]
	});
}

function addNewInstance(){
	var tb = $j("#tbDefaultInstance");
	tb.append($j(tb[0].newRow));
}

function deleteInstance(e){
	var parent = $j(e);
	while (parent && "TR" != parent[0].tagName.toUpperCase()) {
		parent = parent.parent();
	}
	
	parent.remove();
}

///////////////////////////////////////////////////////////////////////
//还原点管理
///////////////////////////////////////////////////////////////////////

function wrapCheckPointRow(checkpoint){
	var d = localDateStr(checkpoint.createDate);
	var id = checkpoint.checkpointId;
	var description = checkpoint.description;

	var row = $j("<tr></tr>");
	var td = $j("<td class=\"no-wrap\"></td>");
	td.text(d);
	row.append(td);

	td = $j("<td><div style='max-height: 100px; width:100%; overflow:auto;'></div></td>");
	var pre = $j("<pre style='padding:0px;margin:0px;word-wrap: break-word;'></pre>");
	pre.text(description);
	$j("div", td).append(pre);
	row.append(td);
	
	row.append("<td class=\"no-wrap\"><a class=\"button\" href=\"javascript:void(0)\" onclick=\"recoverCheckPoint('" + id + "');\">还原</a>&nbsp;<a class=\"button alert\" href=\"javascript:void(0)\" onclick=\"deleteCheckPoint('" + id + "', this);\">删除</a></td>");
	
	return row;
}

function showCheckPointDlg(){
	var projectId = $j("#projectId").val();

	try{
		checkProjectAuth(projectId);
	}catch(msg){
		showErrMsg(msg);
		return;
	}
	
	checkPointService.getAllCheckPoints(projectId, "db", function(checkpoints){
		console.log(checkpoints);
		var tbody = $j("#checkPointTable tbody");
		tbody.empty();
		for(var i in checkpoints){
			tbody.append(wrapCheckPointRow(checkpoints[i]));
		}
		
		$j("#checkPointDlg").dialog({
			title: "还原点管理",
			modal: true,
			show: window._dialogEffectName,
			hide : window._dialogEffectName,
			height: ($j("body").height() * 0.95),
			width: ($j("body").width() * 0.95),
			position: {
				my: "center",
				at: "center",
				of: window,
				collision: "fit",
				// Ensure the titlebar is always visible
				using: function( pos ) {
					var topOffset = $j( this ).css( pos ).offset().top;
					if ( topOffset < 0 ) {
						$( this ).css( "top", pos.top - topOffset );
					}
				}
			}
		});
	});
	
}

/**
 * 创建还原点
 * @returns
 */
function createCheckPoint(){
	var projectId = $j("#projectId").val();
	var description = $j("#checkPointDescription").val().trim();
	if("" == description){
		showInfoMsg("请输入描述信息。");
		return;
	}
	var waittingDlg = showWaitingDlg();
	dbStructService.createCheckPoint(projectId, description, function(checkpoint){
		console.log(checkpoint);
		
		$j("#checkPointTable tbody").prepend(wrapCheckPointRow(checkpoint));
		$j("#checkPointDescription").val("");
		
		waittingDlg.dialog("close");
		
		showInfoMsg("还原点创建成功 ！", "创建还原点");
	});
}

/**
 * 还原点恢复
 * @param checkpointId
 * @returns
 */
function recoverCheckPoint(checkpointId){
	$j("<div style='color: red;'>确定要恢复到这个还原点？</div>").dialog({
		title: "还原点还原",
		modal : true,
		show: window._dialogEffectName,
		hide : window._dialogEffectName,
		buttons : [
			{
				text : "确定",
				click : function(){
					var waittingDlg = showWaitingDlg();
					dbStructService.checkPointRecovery(checkpointId, function(result){
						waittingDlg.dialog("close");
						
						if(result){
							loadStructList();
							showInfoMsg("还原点还原成功。");
						}else{
							showErrMsg("还原点还原失败。");
						}
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

/**
 * 删除还原点
 * @param checkpointId
 * @param obj
 * @returns
 */
function deleteCheckPoint(checkpointId, obj){
	$j("<div><div style='color: red;'>确定要删除这个还原点？</div><div style='color: orange;'>注：该操作将无法恢复。</div></div>").dialog({
		title: "删除还原点",
		modal : true,
		show: window._dialogEffectName,
		hide : window._dialogEffectName,
		buttons : [
			{
				text : "确定",
				click : function(){
					var waittingDlg = showWaitingDlg();
					checkPointService.deleteCheckPoint(checkpointId, function(){
						var row = findParent($j(obj), "TR");
						$j(row).remove();
						waittingDlg.dialog("close");
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

$j(function() {
	$j("#editRowDlg").dialog({
		title : "结构体成员编辑",
		autoOpen : false,
		modal : true,
		show: window._dialogEffectName,
		hide : window._dialogEffectName,
		width : 500,
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

	loadStructList();
});