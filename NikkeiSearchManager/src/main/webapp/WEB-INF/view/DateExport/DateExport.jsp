<!DOCTYPE html>

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<html>
	<head>
		<meta charset="utf-8">
		<title>過去の日経株価検索</title>
		<style>
		 h1 { font-size:16pt; background-color:#FFFF77; padding:3px; }
		 h3 { color: #ff0000; }
		 h4 { font-size:10pt; background-color:#99FF99; padding:5px; }
		 p { color: #000066 }
		 select { width: 120px; }
		</style>
	</head>
	<body>
		<!-- 検索条件で指定された日付の範囲を一覧表示する -->
		<h1>${title}</h1>
		<table>
		<form:form modelAttribute="DateExportForm">
			<tr><h4>${startDate}</h4></tr>
			<tr><form:select path="startYEAR"  name="startYEAR" items="${YearList}" multiple="false" width="80" />年　</tr>
			<tr><form:select path="startMONTH"  name="startMONTH" items="${MonthList}" multiple="false" />月　</tr>
			<tr><form:select path="startDAY"  name="startDAY" items="${DayList}" multiple="false" />日</tr>
			<br/>
			<tr><h4>${endDate}</h4></tr>
			<tr><form:select path="endYEAR"  name="endYEAR" items="${YearList}" multiple="false" width="80" />年　</tr>
			<tr><form:select path="endMONTH"  name="endMONTH" items="${MonthList}" multiple="false" />月　</tr>
			<tr><form:select path="endDAY"  name="endDAY" items="${DayList}" multiple="false" />日</tr>
			<br/><br/><br/>
			<tr><input type="submit" Value="選択した日付で出力" name="date_export"></tr>
			<hr>
			<h3>${DateChoiceError}</h3>
			</br></br>
			<input type="submit" Value="トップメニューに戻る" name="return_top_menu">
			</form:form>
		</table>
	</body>
</html>