<!DOCTYPE html>

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<html>
	<head>
		<meta charset="utf-8">
		<title>ログインメニュー</title>
	</head>
	<body>
		<h1>${title}</h1>
		<P>${message}</P>
		<table>
		<form:form modelAttribute="loginForm">
			<tr><td><form:input path="USER_ID" /></td></tr>
			<tr><td><form:password path="PASSWORD" /></td></tr>
			<tr><td><input type="submit" name="login" value="ログイン"></td></tr>
		</form:form>
		</table>
	</body>
</html>