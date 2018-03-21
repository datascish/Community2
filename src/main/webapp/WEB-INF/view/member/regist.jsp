<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet"
		type="text/css" href="<c:url value="/static/css/button.css"/>"/>
<link rel="stylesheet"
		type="text/css" href="<c:url value="/static/css/input.css"/>"/>
<script type="text/javascript" src="<c:url value="/static/js/jquery-3.3.1.min.js"/>" ></script>
<script type="text/javascript">
	$().ready(function() {
		$("#email").keyup(function() {
			var value = $(this).val();
			if (value != "") {
				 // Ajax Call (http://localhost:8080/Community/api/exists/email)
				 $.post("<c:url value="/api/exists/email"/>", { // 비동기 방식
					 email: value
				 }, function(response) {
					 console.log(response.response);
					 if (response.response) {
						 $("#email").removeClass("valid");
						 $("#email").addClass("invalid");
					 }
					 else {
						 $("#email").removeClass("invalid");
						 $("#email").addClass("valid");
					 }
				 });
			}
			else {
				$(this).addClass("invalid");
				$(this).removeClass("valid");
			}
		});
		
		$("#nickname").keyup(function() {
			var value = $(this).val();
			if (value != "") {
				$.post("<c:url value="/api/exists/nickname"/>", {
					nickname: value
				}, function(response) {
					console.log(response.response);
					if (response.response) {
						$("#nickname").removeClass("valid");
						$("#nickname").addClass("invalid");
					}
					else {
						$("#nickname").removeClass("invalid");
						$("#nickname").addClass("valid");
					}
				});
			}
			else {
				$(this).addClass("invalid");
				$(this).removeClass("valid");
			}
		});
		
		$("#password").keyup(function() {
			var value = $(this).val();
			if (value != "") {
				$(this).removeClass("invalid");
				$(this).addClass("valid");
			}
			else {
				$(this).addClass("invalid");
				$(this).removeClass("valid");
				
				var password = $("password-confirm").val();
				
				if (value != password) {
					$(this).removeClass("valid");
					$(this).addClass("invalid");
					$("#password-confirm").removeClass("valid");
					$("#password-confirm").addClass("invalid");
				}
				else {
					$(this).removeClass("invalid");
					$(this).addClass("valid");
					$("#password-confirm").removeClass("invalid");
					$("#password-confirm").addClass("valid");
				}
			}
			
		});
		
		$("#registBtn").click(function() {
			if ($("#email").val() == "") {
				alert("이메일을 입력하세요!");
				$("#email").focus();
				$("#email").addClass("invalid");
				return false;
			}
			
			if ($("#email").hasClass("invalid")) {
				alert("작성한 이메일은 사용할 수 없습니다.");
				$("#email").focus();
				return false;
			}
			else {
				// 한 번 더 체크 - 비동기 -> 동기
				 $.post("<c:url value="/api/exists/email"/>", {
					 email: $("#email").val()
				 }, function(response) {
					 if (response.response) {
						 alert("작성한 이메일은 사용할 수 없습니다.");
						 $("#email").focus();
						 return false;
					 }
					 if ($("#nickname").val() == "") {
							alert("닉네임을 입력하세요!");
							$("#nickname").focus();
							$("#nickname").addClass("invalid");
							return false;
						}
					 	if ($("#nickname").hasClass("invalid")) {
					 		alert("작성한 닉네임은 사용할 수 없습니다.");
					 		$("#nickname").focus();
					 		return false;
					 	}
					 	else {
					 		$.post("<c:url value="/api/exists/nickname"/>", {
								nickname: $("#nickname").val()
							}, function(response) {
								console.log(response.response);
								if (response.response) {
									$("#nickname").removeClass("valid");
									$("#nickname").addClass("invalid");
								}
								else {
									if ($("#password").val() == "") {
										alert("비밀번호를 입력하세요!");
										$("#password").focus();
										$("#password").addClass("invalid");
										return false;
									}
									$("#registForm").attr({
										"method": "post",
										"action": "<c:url value="/regist"/>"
								    }).submit();
								}
							});
					 	}
						
						
					
				 });

			}
		});
	});
</script>
</head>
<body>
	<div id="wrapper">
		<jsp:include page="/WEB-INF/view/template/menu.jsp"/>
		<form:form modelAttribute="registForm">
			<div>
				<%-- TODO Email 중복검사 하기 (ajax) --%>
				<input type="email" id="email" 
						name="email" placeholder="Email"
						value="${registForm.email}" />
				<div>
					<form:errors path="email" />
				</div>
			</div>
			
			<div>
				
				<input type="text" id="nickname" 
						name="nickname" placeholder="Nickname" 
						value="${registForm.nickname}"/>
				<div>
					<form:errors path="nickname" />
				</div>
			</div>
			<div>
				<input type="password" id="password" name="password" placeholder="Password" />
			</div>
			<div>
				<input type="password" id="password-confirm" placeholder="Password" />
			</div>
			<div style="text-align:center;">
				<div id="registBtn" class="button">
					회원가입
				</div>
			</div>
		</form:form>
	</div>
</body>
</html>