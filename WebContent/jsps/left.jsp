<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>left</title>
    <base target="body"/>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<meta http-equiv="content-type" content="text/html;charset=utf-8">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<script type="text/javascript" src="<c:url value='/jquery/jquery-1.5.1.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/menu/mymenu.js'/>"></script>
	<link rel="stylesheet" href="<c:url value='/menu/mymenu.css'/>" type="text/css" media="all">
	<link rel="stylesheet" type="text/css" href="<c:url value='/css/left.css'/>">
<script language="javascript">
/*
 * Q6MenuBar的add(参数1,参数2,参数3,参数4)方法:
	参数1: 一级分类名称
	参数2: 二级分类名称
	参数3: 该二级分类所指向的url,点击则跳转
	参数4: 如<a></a>标签中的target指在哪个视图显示
 */
var bar = new Q6MenuBar("bar", "勇尚网上书城");
$(function() {
	bar.colorStyle = 4;
	bar.config.imgDir = "<c:url value='/menu/img/'/>";
	bar.config.radioButton=true;

	<c:forEach items="${requestScope.categories}" var="parent">
		<c:forEach items="${parent.children}" var="child">
			bar.add("${parent.cname}", "${child.cname}", "<c:url value='/BookServlet?method=findByCategory&cid=${child.cid}'/>", "body");
		</c:forEach>
	</c:forEach>
	
	$("#menu").html(bar.toString());
});
</script>
</head>
  
<body>  
  <div id="menu"></div>
</body>
</html>
