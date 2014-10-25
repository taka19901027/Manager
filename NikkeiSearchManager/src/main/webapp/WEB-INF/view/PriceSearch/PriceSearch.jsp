<!DOCTYPE html>

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<html>
	<head>
		<meta charset="utf-8">
		<title>過去の日経株価検索</title>
		<style>
		 h1 { font-size:16pt; background-color:#CCCCFF; padding:3px; }
		 h4 { font-size:10pt; background-color:#99FF99; padding:5px; }
		 p { color: #000066 }
		 select { width: 120px; }
		</style>
	</head>
	<body>
		<!-- 検索条件である年月日と株価の種類をセットする -->
		<table>
		<form:form modelAttribute="PriceSearchForm">
			<tr><h4>検索したい西暦を選んでください(2012～2014)</h4></tr>
			<tr><form:select path="YEAR"  name="YEAR" items="${YearList}" multiple="false" width="80" /></tr>
			<tr><h4>検索したい月を選んでください</h4></tr>
			<tr><form:select path="MONTH"  name="MONTH" items="${MonthList}" multiple="false" /></tr>
			<tr><h4>検索したい日付を選んでください</h4></tr>
			<tr><form:select path="DAY"  name="DAY" items="${DayList}" multiple="false" /></tr>
			<tr><h4>検索したい株価の種類を選んでください</h4></tr>
			<tr><form:select path="PRICE_SEARCH"  name="PRICE_SEARCH" items="${PriceSearchList}" multiple="false" /></tr>
			<br/><br/><br/>
			<tr><input type="submit" Value="検索する" name="Price_Search"></tr>
			<hr>
			<tr><h2>${year}年${month}月${day}日の${price_search}は${rate}円です。</h2>
			</br></br>
			<tr><input type="submit" Value="トップメニューに戻る" name="return_top_menu"></tr>
		</form:form>
		</table>
	</body>
</html>