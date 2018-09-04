$(function(){
	$(".labelError").each(function(){
		showError($(this));
	});
	
	$("#submit").hover(
		function(){
			$("#submit").attr("src",getContextPath() + "/images/regist2.jpg");
		},
		function(){
			$("#submit").attr("src",getContextPath() + "/images/regist1.jpg");
		}
	);
	
	$(".input").blur(function(){
		var inputid = $(this).attr("id");
		var method = "validate" + inputid.substring(0,1).toUpperCase() + inputid.substring(1) + "()";
		//eval(str)函数将字符串当做代码执行，例：eval("alert(hello)");
		eval(method);
	});
	
	$(".input").focus(function(){
		var errorid = $(this).attr("id") + "Error";
		$("#" + errorid).attr("hidden","ture");
	});
	
//	$("#submit").click(function(){
//		var bool = true;
//		if(!validateLoginname()){
//			bool = false;
//		}
//		if(!validateLoginpass()){
//			bool = false;
//		}
//		if(!validateReloginpass()){
//			bool = false;
//		}
//		if(!validateEmail()){
//			bool = false;
//		}
//		if(!validateVerifyCode()){
//			bool = false;
//		}
//		return bool;
//	});
});

//有错误信息则显示，ele参数为JQuery对象
function showError(ele){
	var text = ele.text();
	if(!text){
		ele.attr("hidden","true");
	}else{
		ele.attr("hidden","");
	}
}

//校验用户名
function validateLoginname(){
	var input = "loginname";
	var value = $("#" + input).val();
	//如果为空返回false
	if(value == null || value.trim() == ""){
		var label = input + "Error";
		$("#" + label).text("用户名不能为空！");
		showError($("#" + label));
		return false;
	}
	//如果名称不在5~20之间返回false，
	if(value.length < 3 || value.length > 20){
		var label = input + "Error";
		$("#" + label).text("用户名需在3~20个字符之间！");
		showError($("#" + label));
		return false;
	}
	//使用字母、数字、下划线，需以字母开头,否则返回false
	var reg = /^[a-zA-Z]+[\w_]*$/;
	if(!(reg.test(value))){
		var label = input + "Error";
		$("#" + label).text("可使用字母、数字、下划线，需以字母开头！");
		showError($("#" + label));
		return false;
	}
	//如果用户名已被注册返回false
	$.ajax({
		url: getContextPath() + "/UserServlet",
		type: "post",
		data: {method: "validateLoginname", loginname: value},
		async: false,
		cache: false,
		dataType: "json",
		success: function(ele){
			if(ele){
				$("#loginnameError").text("该用户名已被注册!");
				$("#loginnameError").attr("hidden","");
				return false;
			}
			return true;
		}
	});
	return true;
}

//校验密码
function validateLoginpass(){
	var input = "loginpass";
	var value = $("#" + input).val();
	//如果为空返回false
	if(value == null || value.trim() == ""){
		var label = input + "Error";
		$("#" + label).text("密码不能为空！");
		showError($("#" + label));
		return false;
	}
	//如果密码不在5~20之间返回false，
	if(value.length < 5 || value.length > 20){
		var label = input + "Error";
		$("#" + label).text("密码需在5~20个字符之间！");
		showError($("#" + label));
		return false;
	}
	return true;
}

//校验再次输入密码
function validateReloginpass(){
	var input = "reloginpass";
	var value = $("#" + input).val();
	//如果为空返回false
	if(value == null || value.trim() == ""){
		var label = input + "Error";
		$("#" + label).text("确认密码不能为空！");
		showError($("#" + label));
		return false;
	}
	//如果两次密码不同返回false，
	if(value != $("#loginpass").val()){
		var label = input + "Error";
		$("#" + label).text("两次密码不相同！");
		showError($("#" + label));
		return false;
	}
	return true;
}

//校验邮箱
function validateEmail(){
	var input = "email";
	var value = $("#" + input).val();
	//如果为空返回false
	if(value == null || value.trim() == ""){
		var label = input + "Error";
		$("#" + label).text("邮箱不能为空！");
		showError($("#" + label));
		return false;
	}
	var reg = /^[\w-_]+@[\w-_]+(\.[\w-_]+)+$/;
	if(!reg.test(value)){
		var label = input + "Error";
		$("#" + label).text("邮箱格式不正确！");
		showError($("#" + label));
		return false;
	}
	
	$.ajax({
		url: getContextPath() + "/UserServlet",
		type: "post",
		data: {method: "validateEmail", email: value},
		async: false,
		cache: false,
		dataType: "json",
		success: function(ele){
			if(ele){
				$("#emailError").text("该邮箱已被注册!");
				$("#emailError").attr("hidden","");
				return false;
			}
			return true;
		}
	});
	return true;
}

//校验验证码
function validateVerifyCode(){
	var input = "verifyCode";
	var value = $("#" + input).val();
	//如果为空返回false
	if(value == null || value.trim() == ""){
		var label = input + "Error";
		$("#" + label).text("验证码不能为空！");
		showError($("#" + label));
		return false;
	}
	if(value.length != 4){
		var label = input + "Error";
		$("#" + label).text("验证码不正确！");
		showError($("#" + label));
		return false;
	}
	
	$.ajax({
		url: getContextPath() + "/UserServlet",
		type: "post",
		data: {method: "validateVerifyCode", verifyCode: value},
		async: false,
		cache: false,
		dataType: "json",
		success: function(ele){
			if(!ele){
				$("#verifyCodeError").text("验证码错误!");
				$("#verifyCodeError").attr("hidden","");
				return false;
			}
			return true;
		}
	});
	
	return true;
}
