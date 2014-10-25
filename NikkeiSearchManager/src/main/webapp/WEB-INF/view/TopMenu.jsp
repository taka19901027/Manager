<!DOCTYPE html>

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<html>
	<head>
		<meta charset="utf-8">
		<title>日経管理システムトップメニュー</title>
		<style>
		 h1 { font-size:16pt; background-color:#CCCCFF; padding:3px; }
		 p { color: #000066 }
		</style>
	</head>
	<body>
		<h1>${title}</h1>
		<P>${message}</P>
		<table>
		<form:form>
			<tr><input type="submit" value="データを入力する" name="insert_data"><tr>
			<br/><br/>
			<tr><input type="submit" value="過去の日経株価を見る" name="past_price_search"></tr>
			<br/></br>
			<tr><input type="submit" value="日付を指定して平均を見る" name="average_price_search"></tr>
			<br/></br>
			<tr><input type="submit" value="選択したデータを出力する" name="price_data_export"></tr>
			<br/><br/><br/><br/>
			<tr><input type="submit" value="ログアウト" name="logout"></tr>
		</form:form>
		</table>
	</body>
</html>