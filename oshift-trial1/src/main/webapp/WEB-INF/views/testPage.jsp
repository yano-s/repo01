<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<html>
<head>
<title>内容確認ページ</title>
</head>
<body>
	<h1>内容確認ページ</h1>
	<h3>configurationList</h3>
	<ul>
		<c:forEach var="configuration" items="${configurationList}">
			<li><c:out value="${configuration}" /></li>
		</c:forEach>
		<h3>ファイル表示</h3>
		<form:form action="${pageContext.request.contextPath}/prev-file"
			method="POST">
			<p>
				ファイル：<input type="text" name="filePath">
			</p>
			<button type="submit">表示</button>
		</form:form>
<pre>
<c:out value="${prevText}" />
</pre>
		<hr>
<h3>ダウンロード</h3>
<form:form action="${pageContext.request.contextPath}/download" method="POST">
	<p>ファイル：<input type="text" name="filePath"></p>
	<button type="submit">ダウンロード</button>
</form:form>
<hr>
		<%--
<h3>実行</h3>
<form:form action="${pageContext.request.contextPath}/exec-run" method="POST">
	<p>コマンド：<input type="text" name="command"></p>
	<button type="submit">実行</button>
</form:form>
<hr>
<ul>
<c:forEach var="res" items="${resultList}" >
	<li><c:out value="${res}"/></li>
</c:forEach>
</ul>
--%>
</body>
</html>
