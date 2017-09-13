<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<html>
<head>
	<title>ファイル確認ページ</title>
</head>
<body>
<h1>
	ファイル確認ページ
</h1>

<h3>configurationList</h3>
<ul>
<c:forEach var="configuration" items="${configurationList}" >
	<li><c:out value="${configuration}"/></li>
</c:forEach>
</ul>
<form:form action="${pageContext.request.contextPath}/dl-config" method="POST">
	<p>$JBOSS_HOME/standalone/configuration/<input type="text" name="fileName"></p>
	<button type="submit">ダウンロード</button>
</form:form>
<h3>eapHomeList</h3>
<ul>
<c:forEach var="entry" items="${eapHomeList}" >
	<li><c:out value="${entry}"/></li>
</c:forEach>
</ul>
<hr>

</body>
</html>
