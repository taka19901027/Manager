<!DOCTYPE html>

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<html>
	<head>
		<meta charset="utf-8">
		<title>ファイル管理エラー</title>
		<style>
		 h1 { font-size:16pt; background-color:#FF0000; padding:3px; }
		 p { color: #000066 }
		</style>
	</head>
	<body>
		<h1>${subject}</h1>
		<br/><br/><hr>
		<P>${message}</P>
		<form:form>
			<tr><input type="submit" value="ログアウト" name="logout"><tr>
		</form:form>
	</body>
</html>