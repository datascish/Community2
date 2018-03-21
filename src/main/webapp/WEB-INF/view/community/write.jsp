<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script src="<c:url value="/static/js/jquery-3.3.1.min.js"/>" 
		type="text/javascript"></script>
<script type="text/javascript">
	$().ready(function() {
		// 제목, 내용, 날짜 유효성 검증
		<c:if test="${mode == 'modify' && not empty communityVO.displayFilename}">
			$("#file").closest("div").hide();
		</c:if>
		
		$("#displayFilename").change(function() {
			var isChecked = $(this).prop("checked");
			if (isChecked) {
				$("label[for=displayFilename]").css({
					"text-decoration-line": "line-through",
					"text-decoration-style": "double",
					"text-decoration-color": "#F00"
				});
				$("#file").closest("div").show();
			}
			else {
				$("label[for=displayFilename]").css({
					"text-decoration": "none"
				});
				$("#file").closest("div").hide();
			}
		});
		
		$("#writeBtn").click(function() {
			var mode = "${mode}";
			if (mode == "modify") {
				var url = "<c:url value="/modify/${communityVO.id}"/>";
			}
			else {
				var url = "<c:url value="/write"/>";
			}
			var writeForm = $("#writeForm");
			// submit 직전에 제목, 내용, 날짜 유효성 검증
			/* if($("#title").val() == "") {
				$("#errorTitle").slideDown(300);
				$("#title").focus();
				return false;
			}
			else {
				$("#errorTitle").slideUp();
			}
			if($("#body").val() == "") {
				$("#errorBody").slideDown(300);
				$("#body").focus();
				return false;
			}
			else {
				$("#errorBody").slideUp();
			}
			if($("#date").val() == "") {
				$("#errorDate").slideDown(300);
				$("#date").focus();
				return false;
			}
			else {
				$("#errorDate").slideUp();
			}*/
			writeForm.attr({
				"method" : "post",
				"action" : url
			});
			writeForm.submit();
		});
	});
</script>
</head>
<body>
	<div id="wrapper">
		<jsp:include page="/WEB-INF/view/template/menu.jsp" />
			<form:form modelAttribute="writeForm" enctype="multipart/form-data">
				<div>
					제목 <input type="text" id="title" name="title" value="${communityVO.title}" />
				</div>
				<div>
					<form:errors path="title" />
				</div>
				<div>
					<textarea rows="10" cols="50" id="body" name="body" >${communityVO.body}</textarea>
				</div>
				<!-- /modify로 접근했을 때 -->
				<c:if test="${mode == 'modify' && not empty communityVO.displayFilename}">
					<div>
						<input type="checkbox" id="displayFilename" name="displayFilename"
								value="${communityVO.displayFilename}"/>
						<label for="displayFilename" >
							${communityVO.displayFilename}
						</label>
					</div>
				</c:if>
				<div>
					<form:errors path="body" />
				</div>
				<div>
					<input type="hidden" id="userId" name="userId"
						value="${sessionScope.__USER__.id}" />
				</div>
				
				<div>
					<form:errors path="writeDate" />
				</div>
			
				<div>
					<input type="file" id="file" name="file" />
				</div>
			
				<div>
					<input type="button" id="writeBtn" value="등록" />
				</div>
			</form:form>
	</div>
	
	<!-- <div>
		<c:if test="${empty communityList}">
				<tr>
					<td colspan="5">등록된 게시글이 없습니다.</td>
				</tr>
		</c:if>
	</div> -->
	
</body>
</html>