<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<html>
<head>
	<title>Home</title>
</head>
<body>
<h1>
	初めてのOpenShift!
</h1>
<p>OpenShift起動確認！</p>
<P>  The time on the server is ${serverTime}. </P>
<P>  Session ID is ${sessionId}. </P>
<P>  Page Count is ${counter}. </P>

<form:form action="${pageContext.request.contextPath}/clearCount" method="POST">
	<button type="submit">カウンタークリア</button>
</form:form>

</body>
</html>
