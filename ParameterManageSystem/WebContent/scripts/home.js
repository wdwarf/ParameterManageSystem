/**
 * 
 */

var currentProjectInfo = {
	name : "",
	id : "",
	currentSubProject : {
		name : "",
		id : ""
	}
};

$(function() {
	$(".list-title").click(function() {
		var parent = $(this).parent();
		var l = $(".panel-struct-list", parent);
		// l.toggle();
		if ("none" != l.css("display")) {
			l.slideUp("fast");
		} else {
			l.slideDown("fast");
		}
	});

	$("#panel-projects ul a").click(function() {
		var projectName = $(this).text();
		
		if(structListMgr.projectName == projectName){
			return;
		}
		
		structListMgr.projectName = projectName;
		$("#project-name").text(projectName);
		$("#sub-project-name").empty();
		structListMgr.clear();
		structListMgr.cleraStructEditor();
	});

	$("#panel-sub-projects ul a").click(function() {
		var subProjectName = $(this).text();
		var span = $("<span></span>");
		span.append("panel-sub-projects " + subProjectName);
		span.attr("structId", "123");
		structListMgr.add(span);
		$("#sub-project-name").text(subProjectName);
	});

	structListMgr.init();
});

var structListMgr = {
	projectName : "",
	projectId : "",
	subProjectName : "",
	subProjectId : "",
	init : function() {
		structListMgr.listItem = $("#structList");
		structListMgr.structEdirot = $("#struct-editor");
		structListMgr.clear();
	},
	listItem : null,
	structEditor : null,
	clear : function() {
		structListMgr.listItem.empty();
	},

	// add a struct to list
	add : function(struct) {
		var a = $("<a></a>");
		a.click(structListMgr.onStructClick);
		a.append(struct);
		var li = $("<li></li>");
		li.append(a);
		structListMgr.listItem.append(li);
	},

	onStructClick : function() {
		var span = $("span", $(this));
		structListMgr.structEdirot.attr("src", "db/dbStructEditor?editType=edit&structName=" + span.text());
	},
	
	cleraStructEditor : function(){
		structListMgr.structEdirot.attr("src", "");
	}
}