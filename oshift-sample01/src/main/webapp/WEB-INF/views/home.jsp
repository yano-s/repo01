<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<html>
<head>
	<title>Sample01</title>
<style>
.list{
border-collapse: collapse;
}
.list th,.list td{
	border:1px solid gray;
	text-align: left;
	padding:2px;
	word-break:break-all;
}
.list th{
width:150px;}

</style>
</head>
<body>
<h1>
	Sample01(${title})
</h1>
<p>OpenShift起動確認！</p>
<P>  The time on the server is ${serverTime}. </P>
<P>  Session ID is ${sessionId}. </P>
<P>  Page Count is ${counter}. </P>

<form:form action="${pageContext.request.contextPath}/clearCount" method="POST">
	<button type="submit">カウンタークリア</button>
</form:form>
<hr>
<table class="list">
<c:forEach var="address" items="${addressList}" >
<tr>
<td><c:out value="${address.hostName}"/></td>
<td><c:out value="${address.hostAddress}"/></td>
</tr>
</c:forEach>
</table>
<hr>

<table class="list">
<c:forEach var="systemProp" items="${systemProps}" >
<tr>
<th><c:out value="${systemProp.key}"/></th>
<td><c:out value="${systemProp.value}"/></td>
</tr>
</c:forEach>
</table>
</body>
</html>
