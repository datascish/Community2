<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<div id="nav">
<link rel="stylesheet" type="text/css" href="<c:url value="/static/css/link.css"/>" />
<style type="text/css">
	#nav > ul {
		padding: 0px;
		margin : 0px;
	}
</style>
	<ul style="display:inline-block; margin: 0px;">
		<c:if test="${empty sessionScope.__USER__}">
			<li style="display:inline-block;">
				<a href="<c:url value="/login" />">Regist/Login</a>
			</li>
		</c:if>
		
		<c:if test="${not empty sessionScope.__USER__}">
			<li>
				(${sessionScope.__USER__.nickname}님) 
				<a href="<c:url value="/logout" />"> Logout</a>
				<a href="<c:url value="/delete/process1"/>">탈퇴</a>
			</li>
		</c:if>
		
		<li style="display:inline-block;">
			<a href="<c:url value="/community/rest" />">Community</a>
		</li>
	</ul>
</div>