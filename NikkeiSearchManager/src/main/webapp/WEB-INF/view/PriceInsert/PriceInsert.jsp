<!DOCTYPE html>

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<html>
	<head>
		<meta charset="utf-8">
		<title>過去の日経株価検索</title>
		<style>
		 h1 { font-size:16pt; background-color:#C0C0C0; padding:3px; }
		 h4 { font-size:10pt; background-color:#99FF99; padding:5px; }
		 p { color: #000066 }
		 select { width: 120px; }
		</style>
	</head>
	<title>${title}</title>

	<script type="text/javascript">
	function insertDisp(){

		if(window.confilm("入力したデータを挿入します。\nよろしいですか？")){
			return true;
		}
		else{
			window.alert('キャンセルしました');
		}
	}

	</script>
	<body>
		<!-- データを入力したい日付と種類と株価をセットする -->
		<h1>${message}</h1>
		<table>
		<form:form modelAttribute="PriceInsertForm">
			<tr><h4>西暦を選んでください(2012～2014)</h4></tr>
			<tr><form:select path="YEAR"  name="YEAR" items="${YearList}" multiple="false" width="80" /></tr>
			<tr><h4>月を選んでください</h4></tr>
			<tr><form:select path="MONTH"  name="MONTH" items="${MonthList}" multiple="false" /></tr>
			<tr><h4>日付を選んでください</h4></tr>
			<tr><form:select path="DAY"  name="DAY" items="${DayList}" multiple="false" /></tr>
			<tr><h4>入力する日経平均株価の値を全て入力してください<font color="#ff0000">(削除する場合は未入力)</font></h4>
			<tr><h>始値:</h><form:input path="OPEN_INSERT" name="OPEN_INSERT" /></br></br>
			<h>高値:</h><form:input path="HIGH_INSERT" name="HIGH_INSERT" /></br></br>
			<h>低値:</h><form:input path="LOW_INSERT" name="LOW_INSERT" /></br></br>
			<h>終値:</h><form:input path="CLOSE_INSERT" name="CLOSE_INSERT" /></tr>
			<br/><br/>
			<hr>
			<tr><input type="submit" Value="入力する" name="Insert_Price"></tr>　　　
			<tr><input type="submit" Value="削除する" name="Delete_Price"></tr>
			</br></br>
			<tr><input type="submit" Value="トップメニューに戻る" name="return_top_menu"></tr>
		</form:form>
		</table>
	</body>
</html>