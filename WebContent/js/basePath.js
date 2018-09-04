//得到以'/'开头的webroot路径相当于request.getContextPath
function getContextPath(){
	var pathname = window.location.pathname;
	var contextPath = "/" + pathname.split("/")[1];
	return contextPath;
}

//得到web应用的绝对路径
function getRealPath(){
	var origin = window.location.origin;
	var contextPath = getContextPath();
	return origin + contextPath;
}

