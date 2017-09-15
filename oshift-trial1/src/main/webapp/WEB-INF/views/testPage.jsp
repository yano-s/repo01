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
<h1>
	内容確認ページ
</h1>

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

</body>
</html>
