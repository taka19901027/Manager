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
		<form:form modelAttribute="DateExportForm">
			<!-- 検索条件で指定された日付の範囲を一覧表示する -->
			<h1>${message}</h1>
			<table border="1">
				<!-- column headers -->
				<tr>
				<c:forEach var="columnName" items="${headerList}">
					<th><c:out value="${columnName}"/></th>
				</c:forEach>
				</tr>
				<!-- column data -->
				<c:forEach var="row" items="${exportList}">
					<tr>
					<c:forEach var="column" items="${row}">
						<td><c:out value="${column}"/></td>
					</c:forEach>
					</tr>
				</c:forEach>
			</table>
			<hr><br/><br/>
			<input type="submit" Value="データ出力ページに戻る" name="return_export_menu">
		</form:form>
	</body>
</html>