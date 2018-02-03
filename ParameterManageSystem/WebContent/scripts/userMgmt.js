var $j = jQuery.noConflict();

function getLoginUser(){
	var loginUser = null;
	dwr.engine.setAsync(false);
	userService.getLoginUser(function(user){
		loginUser = user;
	});
	dwr.engine.setAsync(true);
	
	return loginUser;
}

function hasUserLogin(){
	var loginUser = getLoginUser();
	
	if(null == loginUser){
		return false;
	}
	
	return true;
}

function checkProjectAuth(projectId){
	var loginUser = getLoginUser();
	var project = null;
	dwr.engine.setAsync(false);
	projectService.getProjectById(projectId, function(p){
		project = p;
	});
	dwr.engine.setAsync(true);

	if(null == loginUser){
		throw "您未登录 ，请先登录。";
	}
	
	if(null == project){
		throw "未找到工程";
	}
	
	if("admin" != loginUser.userName && (!project.user || (project.user && (project.user.userId != loginUser.userId)))){
		throw "无权限";
	}
}

var changeUserPasswordDlg = null;
function changeUserPassword(userId){
	if("undefined" === typeof userService){
		showErrMsg("没有加载用户服务模块。");
		return;
	}
	
	if(!hasUserLogin()){
		//showErrMsg("请登录。", "未登录");
		alert("您未登录 ，请先登录。");
		window.location = "login";
		return;
	}
	
	if(null == changeUserPasswordDlg){
		changeUserPasswordDlg = $j("<div><table>"
			+ "<tr><td><label>新密码:</label></td><td><input type='password' name='password1' /></td></tr>"
			+ "<tr><td><label>确认密码:</label></td><td><input type='password' name='password2' /></td></tr>"
			+ "</table></div>");
	}
	
	changeUserPasswordDlg.dialog({
		title: "修改密码",
		modal: true,
		show: window._dialogEffectName,
		hide : window._dialogEffectName,
		buttons : [
			{
				text: "确定",
				click: function(){
					var psw1 = $j("input[name=password1", changeUserPasswordDlg).val();
					var psw2 = $j("input[name=password2", changeUserPasswordDlg).val();

					if("" == psw1 || "" == psw2){
						showErrMsg("请输入密码", "输入密码");
						return;
					}
					
					if(psw1 != psw2){
						showErrMsg("两次输入的密码不一致", "密码不一致");
						return;
					}
					
					userService.changePassword(userId, psw1, function(re){
						if(re){
							showInfoMsg("密码修改成功。", "修改成功");
							dlg.dialog("close");
						}else{
							showErrMsg("密码修改失败。", "修改失败");
						}
					});
				}
			}
		]
	});
}

