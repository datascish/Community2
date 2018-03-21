<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript" src="<c:url value="/static/js/jquery-3.3.1.min.js"/>"></script>
<script type="text/javascript">
	$().ready(function() {
		$("#loginBtn").click(function() {
			// submit 직전에 validation check 할 것
			/*if ($("#id").val() == "") {
				// id를 입력하지 않았다면 alert 
				$("#errorId").slideDown(300);
				$("#id").focus(); 
				return false;
			}
			else {
				$("#errorId").slideUp();
			}
			if ($("#password").val() == "") {
				$("#errorPassword").slideDown(300);
				$("#password").focus();
				return false;
			}
			else {
				$("#errorPassword").slideUp();
			}*/
			$("#loginForm").attr({
				"action": "<c:url value="/login" />",
				"method": "post"
			}).submit();
		});
	});
</script>
</head>
<body>
	<form:form modelAttribute="loginForm">
		<c:if test ="${sessionScope.status eq 'fail'}">
			<div id="invalidIdAndPassword">
				<div>아이디 혹은 비밀번호가 잘못되었습니다.</div>
				<div>한 번 더 확인 후 시도해 주세요.</div>
			</div>
		</c:if>
		<div>
			<input type="email" id="email" name="email" placeholder="Email" />
		</div>
		
		<div>
			<form:errors path="email" />
		</div>
		
		<div>
			<input type="text" id="password" name="password" placeholder="Password" />
		</div>
		
		<div>
			<form:errors path="password" />
		</div>
		
		<div>
			<input type="button" id="loginBtn" value="Login" />
		</div>
	</form:form>
		<div>
			<a href="<c:url value="/regist"/>">아직 회원이 아니신가요?</a>
		</div>
</body>
</html>