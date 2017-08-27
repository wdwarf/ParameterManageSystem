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


function listProjects(projects){
	var projectList = $("#projectList");
	projectList.empty();
	for(var i = 0; i < projects.length; ++i){
		var li = $("<li></li>");
		var a = $("<a></a>");
		a.text(projects[i].projectName);
		a.attr("projectId", projects[i].projectId);
		a.click(function() {
			var projectName = $(this).text();
			var projectId = $(this).attr("projectId");
			
			if(structListMgr.projectName == projectName){
				return;
			}
			
			structListMgr.projectName = projectName;
			structListMgr.projectId = projectId;
			$("#project-name").text(projectName);
			$("#sub-project-name").empty();
			structListMgr.clear();
			structListMgr.cleraStructEditor();
		});
		li.append(a);
		projectList.append(li);
	}
}

$(function(){
	projectService.getAllProjects(listProjects);
});

$(function() {
	/*
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

	*/

	$("#panel-sub-projects ul a").click(function() {
		var subProjectName = $(this).text();
		
		structListMgr.loadStructs(subProjectName);
		
		$("#sub-project-name").text(subProjectName);
	});

	structListMgr.init();
	projectService.getAllProjects(listProjects);
});

var structListMgr = {
	projectName : "",
	projectId : "",
	subProjectName : "",
	subProjectId : "",
	structName : "",
	structId : "",
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
	
	loadStructs : function(subProjectName){
		structListMgr.clear();
		structListMgr.cleraStructEditor();
		
		structListMgr.subProjectName = subProjectName;
		switch(subProjectName){
		case "DB":{
			dbStructService.getAllStructs(structListMgr.projectId, function(structs){
				for(var i = 0; i < structs.length; ++i){
					var span = $("<span></span>");
					span.append(structs[i].structName);
					span.attr("structId", structs[i].structId);
					structListMgr.add(span);
				}
			});
			break;
		}
		}
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
		var structName = span.text();
		var structId = span.attr("structId");
		structListMgr.structName = structName;
		structListMgr.structId = structId;
		
		switch(structListMgr.subProjectName){
		case "DB":{
			structListMgr.structEdirot.attr("src", "db/dbStructEditor?editType=edit&structName=" + structName + "&structId=" + structId);
			break;
		}
		}
	},
	
	cleraStructEditor : function(){
		structListMgr.structEdirot.attr("src", "");
	}
}