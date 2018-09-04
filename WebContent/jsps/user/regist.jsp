<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path;
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%= basePath %>">
    <title>注册</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<meta http-equiv="content-type" content="text/html;charset=utf-8">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<link rel="stylesheet" type="text/css" href="<c:url value='/css/css.css'/>">
	<link rel="stylesheet" type="text/css" href="<c:url value='/css/user/regist.css'/>">
	<script type="text/javascript" src="<c:url value='/jquery/jquery-1.5.1.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/js/basePath.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/js/common.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/js/user/regist.js'/>"></script>
  </head>
  
  <body>
<div id="divBody">
  <div id="divTitle">
    <span id="spanTitle">新用户注册</span>
  </div>
  <div id="divCenter">
    <form action="<c:url value='/UserServlet'/>" method="post">
    <input type="hidden" name="method" value="regist"/>
    <table id="tableInfo">
      <tr>
        <td class="tdLabel">用户名：</td>
        <td class="tdInput">
          <input type="text" name="loginname" id="loginname" class="input" value="${registForm.loginname}"/>
        </td>
        <td class="tdError">
          <label class="labelError" id="loginnameError">${errors.loginname}</label>
        </td>
      </tr>
      <tr>
        <td class="tdLabel">登录密码：</td>
        <td class="tdInput">
          <input type="password" name="loginpass" id="loginpass" class="input" value="${registForm.loginpass}"/>
        </td>
        <td class="tdError">
          <label class="labelError" id="loginpassError">${errors.loginpass}</label>
        </td>
      </tr>
      <tr>
        <td class="tdLabel">确认密码：</td>
        <td class="tdInput">
          <input type="password" name="reloginpass" id="reloginpass" class="input" value="${registForm.reloginpass}"/>
        </td>
        <td class="tdError">
          <label class="labelError" id="reloginpassError">${errors.reloginpass}</label>
        </td>
      </tr>
      <tr>
        <td class="tdLabel">Email：</td>
        <td class="tdInput">
          <input type="text" name="email" id="email" class="input" value="${registForm.email}"/>
        </td>
        <td class="tdError">
          <label class="labelError" id="emailError">${errors.email}</label>
        </td>
      </tr>
      <tr>
        <td class="tdLabel">图形验证码：</td>
        <td class="tdInput">
          <input type="text" name="verifyCode" id="verifyCode" class="input" value=""/>
        </td>
        <td class="tdError">
          <label class="labelError" id="verifyCodeError">${errors.verifyCode}</label>
        </td>
      </tr>
      <tr>
        <td>&nbsp;</td>
        <td>
          <img id="vCode" width="100" src="<c:url value="/VerifyCodeServlet"/>" />
        </td>
        <td><a href="javascript: changeVerifyCode()">换一张</a></td>
      </tr>
      <tr>
        <td>&nbsp;</td>
        <td>
          <input id="submit" type="image" src="<c:url value='/images/regist1.jpg'/>"/>
        </td>
        <td>已有帐号--><a href="<c:url value='/jsps/user/login.jsp'/>">登陆</a></td>
      </tr>
    </table>
    </form>
  </div>
</div>
  </body>
</html>
	