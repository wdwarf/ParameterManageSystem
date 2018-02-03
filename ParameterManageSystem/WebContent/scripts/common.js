var $j = jQuery.noConflict();

window._dialogEffectName = "fade";

if(!window.console){
	window.console = {};
}
if(!window.console.log){
	window.console.log = function(msg){};
}
if(!window.console.trace){
	window.console.trace = function(msg){};
}

function dwrErrHandler(msg, ex){
	console.log(ex);
	
	if(ex && ex.cause){
		var cause = ex.cause;
		while(cause){
			msg += "<br />" + cause.message;
			cause = cause.cause;
		}
		
	}
	
	showErrMsg(msg);
}
dwr.engine.setErrorHandler(dwrErrHandler);

String.prototype.trim = function(){
	return this.replace(/(^\s*)|(\s*$)/g, "");
}

String.prototype.ltrim = function(){
	return this.replace(/(^\s*)/g, "");
}

String.prototype.rtrim = function(){
	return this.replace(/(\s*$)/g, "");
}

function showInfoMsg(msg, title){
	var _title = "Info";
	if(title){
		_title = title;
	}
	
	var errDlg = $j("<div class=\"\"></div>");
	errDlg.text(msg);
	errDlg.dialog({
		title: _title,
		modal: true,
		show: window._dialogEffectName,
		hide : window._dialogEffectName,
		buttons: [
			{
				text: "OK",
				click: function(){
					$j(this).dialog("close");
				}
			}
		],
		close: function(){
			$j(this).remove();
		}
	});
}

function showErrMsg(msg, title){
	var _title = "Error";
	if(title){
		_title = title;
	}
	
	var errDlg = $j("<div class=\"alert\"></div>");
	errDlg.html(msg);
	errDlg.dialog({
		title: _title,
		modal: true,
		show: window._dialogEffectName,
		hide : window._dialogEffectName,
		buttons: [
			{
				text: "OK",
				click: function(){
					$j(this).dialog("close");
				}
			}
		],
		close: function(){
			$j(this).remove();
		}
	});
}

function showWarningMsg(msg, title){
	var _title = "Error";
	if(title){
		_title = title;
	}
	
	var errDlg = $j("<div class=\"warning\"></div>");
	errDlg.text(msg);
	errDlg.dialog({
		title: _title,
		modal: true,
		show: window._dialogEffectName,
		hide : window._dialogEffectName,
		buttons: [
			{
				text: "OK",
				click: function(){
					$j(this).dialog("close");
				}
			}
		],
		close: function(){
			$j(this).remove();
		}
	});
}

/**
 * 显示一个等待窗口
 * @returns
 */
function showWaitingDlg(){
	var dlg = $j("<div class=\"\" style=\"background-image:url('../images/loading.gif'); width:100%; height:100%; background-repeat:no-repeat; background-position:center;\"></div>");
	dlg.dialog({
		title: "",
		modal: true,
		draggable: false,
		closeOnEscape: false,
		resizable: false,
		show: window._dialogEffectName,
		hide : window._dialogEffectName,
		classes: {
			"ui-dialog": "ui-waiting-dlg",
			"ui-dialog-titlebar": "ui-hide"
		}
	});
	
	return dlg;
}

function findParent(obj, parentTagName){
	var parent = $j(obj);
	parentTagName = ("" + parentTagName).toUpperCase();
	while ((parent.length > 0) && parentTagName != parent[0].tagName.toUpperCase()) {
		parent = parent.parent();
	}
	
	return parent;
}

function fillZero(num, size){
	if(!size || size <= 0){
		size = 2;
	}
	
	var newNum = "" + num;
	while(newNum.length < size){
		newNum = "0" + newNum;
	}
	return newNum;
}

function localDateStr(d){
	return d.getFullYear() + "-" + fillZero(d.getMonth() + 1) 
	+ "-" + fillZero(d.getDate()) 
	+ " " 
	+ fillZero(d.getHours()) + ":" + fillZero(d.getMinutes()) + ":" + fillZero(d.getSeconds())
}
