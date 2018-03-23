<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${community.title}</title>
<link rel="stylesheet" type="text/css" href="<c:url value="/static/css/alert.css" />" />
<script src="<c:url value="/static/js/jquery-3.3.1.min.js"/>" 
		type="text/javascript"></script>
<script src="<c:url value="/static/js/alert.js"/>" 
		type="text/javascript"></script>
<script type="text/javascript">
	$().ready(function() {
		loadReplies(0);
		function loadReplies(scrollTop) {
			$.get("<c:url value="/api/reply/${community.id}"/>", {}, 
					function(response) {
						console.log(response);
						for (var i in response) {
							appendReplies(response[i]); // 전체 댓글 목록 불러오기
					}
					$(window).scrollTop(scrollTop);
			});
		}
		
		$("#writeReplyBtn").click(function() {
			$.post("<c:url value="/api/reply/${community.id}" />",
					$("#writeReplyForm").serialize(), 
					function(response) {
						if (response.status) {
							show("댓글 등록 됨");
							
							$("#parentReplyId").val("0");
							$("#body").val("");
							
							$("#createReply").appendTo($("#createReplyDiv"));
							//appendReplies(response.reply); // 내가 작성한 하나의 댓글 불러오기
							var scrollTop = $(window).scrollTop();
							
							$("#replies").html("");
							loadReplies();
						}
						else {
							alert("등록에 실패했습니다. 잠시 후에 다시 시도하세요.");
						}
			});
		});
		
		// shadow-dom은 dom을 통해 접근해야 함
		// $("dom").on("click","shadow-dom", function() {});
		$("#replies").on("click", ".re-reply", function() {
			var parentReplyId = $(this).closest(".reply").data("id");
			$("#parentReplyId").val(parentReplyId);
			
			// $("dom").appendTo() : 현재 위치로 dom을 옮겨라
			$("#createReply").appendTo($(this).closest(".reply"));
		});
		
		function appendReplies(reply) {
			var replyDiv = $("<div class='reply' style='padding-left: " + ((reply.level-1)*20) + "px;' data-id='"+ reply.id + "'></div>");
			
			var nickname = reply.memberVO.nickname + "(" 
					+ reply.memberVO.email + ")";
			var top = $("<span class='writer'>"+ nickname + "</span><span class='regist-date'>"
					+ reply.registDate + "</span>");
			replyDiv.append(top);
			
			var body = $("<div class='body'>"+ reply.body + "</div>");
			replyDiv.append(body);
			
			var registReReply = $("<div class='re-reply'>댓글 달기</div>");
			replyDiv.append(registReReply);
			
			$("#replies").append(replyDiv);
			
		}
	});
</script>
</head>
<body>
	<div id="wrapper">
		<jsp:include page="/WEB-INF/view/template/menu.jsp" />
			<h1>${community.title}</h1>
			
			<h3>
				<c:choose>
					<c:when test="${not empty community.memberVO}">
						${community.memberVO.nickname}(${community.memberVO.email})  ${community.requestIp}
					</c:when>
					<c:otherwise>
						탈퇴한 회원 ${community.requestIp}
					</c:otherwise>
				</c:choose>
			</h3>
			
			<p>${community.viewCount}	| ${community.recommendCount} | ${community.writeDate}</p>
			<p></p>
			<c:if test="${not empty community.displayFilename}">
				<p><a href="<c:url value="/get/${community.id}"/>">${community.displayFilename}</a></p>
			</c:if>
			<p>
				${community.body}
			</p>
			<hr />
			<div id="replies"></div>
			<div id="createReplyDiv">
				<div id="createReply">
					<form id="writeReplyForm">
						<input type="hidden" id="parentReplyId" name="parentReplyId" value="0 " />
						<div>
							<textarea id="body" name="body"></textarea>
						</div>
						<div>
							<input type="button" id="writeReplyBtn" value="등록" />
						</div>
					</form>
				</div>
			</div>
			<a href="<c:url value="/"/>">뒤로</a>
			<a href="<c:url value="/recommend/${community.id}"/>">추천하기</a>
			<c:if test="${sessionScope.__USER__.id == community.memberVO.id}">
				<a href="<c:url value="/modify/${community.id}"/>">수정하기</a>
				<a href="<c:url value="/remove/${community.id}"/>">게시글 삭제</a>
			</c:if>
	</div>
</body>
</html>