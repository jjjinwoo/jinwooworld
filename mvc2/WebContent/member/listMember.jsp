<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"	uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt"	uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="contextPath" value="${pageContext.request.contextPath }"/>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원 목록 화면</title>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap-theme.min.css">
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>	
</head>
<body>
<br/>
<jsp:include page ="../common/topMenu.jsp"></jsp:include>
	<div id="mainTitle">
	<h1 align = "center">회원 목록</h1>
	</div>
	<br/>
	<div class="container">
			<table class="table table-hover" id="memberListTable">
				<thead>
					<tr>
						<th><b>아이디</b></th>
						<th><b>비밀번호</b></th>
						<th><b>이  름</b></th>
						<th><b>우편번호</b></th>
						<th><b>주소 </b></th>
						<th><b>상세주소</b></th>
						<th><b>휴대폰번호</b></th>
						<th><b>삭제 (Yes/No)</b></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="member1" items="${memberList}">
					 <c:if test="${member1.grade != 7}">
					<tr>
						<td>${member1.userID}</td>
						<td>${member1.pwd}</td>
						<td>${member1.name}</td>
						<td>${member1.postnum}</td>
						<td>${member1.address1}</td>
						<td>${member1.address2}</td>
						<td>${member1.phone}</td>
						<td><button type="button" id="listRemoveBtn" class="btn btn-sm" onclick="fn_removeMember('${member1.userID}');" >삭제 (Yes/No)</button></td>
					</tr>
					</c:if>
					</c:forEach>	
				</tbody>
			</table>
		</div>	
</body>

<script>
function fn_removeMember(userID) {
    if (confirm("해당 아이디를 삭제하시겠습니까?")) {
        $.ajax({
            type: "POST",
            url: "${contextPath}/member/memberRemove.do",
            data: {userID: userID},
            success: function (data) {
                alert("회원이 삭제되었습니다.");
                location.reload();
            }
        });
    } else {
        alert("회원 삭제가 취소되었습니다.");
    }
}
</script>
</html>