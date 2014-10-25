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
	<title>入力結果ページ</title>
	</script>
	<body>
		<!-- 「入力する」ボタン押下後の結果を表示する -->
		<table>
		<form:form modelAttribute="PriceInsertForm">
			<h4>入力結果</h4>
			<p><font color="#ff0000">${resultView}</font></p>
			<hr>
			<tr><input type="submit" Value="データ入力ページに戻る" name="return_insert_page"></tr>
		</form:form>
		</table>
	</body>
</html>