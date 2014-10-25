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
		<h1>${title}</h1>
		<table>
		<form:form modelAttribute="AverageSearchForm">
			<tr><h4>${startDate}</h4></tr>
			<tr><form:select path="startYEAR"  name="startYEAR" items="${YearList}" multiple="false" width="80" />年　</tr>
			<tr><form:select path="startMONTH"  name="startMONTH" items="${MonthList}" multiple="false" />月　</tr>
			<tr><form:select path="startDAY"  name="startDAY" items="${DayList}" multiple="false" />日</tr>
			<br/>
			<tr><h4>${endDate}</h4></tr>
			<tr><form:select path="endYEAR"  name="endYEAR" items="${YearList}" multiple="false" width="80" />年　</tr>
			<tr><form:select path="endMONTH"  name="endMONTH" items="${MonthList}" multiple="false" />月　</tr>
			<tr><form:select path="endDAY"  name="endDAY" items="${DayList}" multiple="false" />日</tr>
			<tr><h4>${rateKind}</h4></tr>
			<tr><form:select path="PRICE_SEARCH"  name="PRICE_SEARCH" items="${PriceSearchList}" multiple="false" /></tr>
			<br/><br/><br/>
			<tr><input type="submit" Value="平均を算出する" name="average_price_culculate"></tr>
			<hr>
			<tr><h2>${startYear}年${startMonth}月${startDay}日～</h2>
			<tr><h2>${endYear}年${endMonth}月${endDay}日の</h2>
			<tr><h2>${search_kind}の平均は${resultDate}円です。</h2>
			</br></br>
			<tr><input type="submit" Value="トップメニューに戻る" name="return_top_menu"></tr>
			</form:form>
		</table>
	</body>
</html>