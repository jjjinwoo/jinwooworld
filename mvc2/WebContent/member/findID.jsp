<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<c:set var = "contextPath" value = "${pageContext.request.contextPath }"/>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>아이디 찾기</title>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap-theme.min.css">
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>	
</head>
<body>
	<br/>
	<jsp:include page ="../common/topMenu.jsp"></jsp:include>
	<br/>
	<div class="container" id="loginFormBox" style="display: flex; justify-content: center; align-items: center;">
			<form class = "form-horizontal" method = "post" action = "${contextPath}/member/findUser.do">
				<div class = "form-group">
					<div class ="col-sm-8">
						<h2 align = "center">아이디 찾기</h2>
					</div>
				</div>
				<br/>
				<div class="form-group">
					<label for="name" class="col-sm-3 control-label">이  름</label>
						<div class="col-sm-8">
						<input type="text" class="form-control" id="name" name="name" maxlength="20" placeholder="이름 입력"/>
					</div>
				</div>
				<div class = "form-group">
					<label for="phone" class="col-sm-3 control-label">Phone</label>
						<div class="col-sm-8">
						<input type="text" class="form-control" id="phone" name="phone" placeholder="휴대폰 번호"
							oninput="this.value = this.value.replace(/[^0-9.]/g, '').replace(/(\..*)\./g, '$1');" onKeyup="inputPhoneNumber();" maxlength="13"/>
					</div>
				</div>
				<div class ="form-group" style="display: flex; justify-content: center; align-items: center;">
						<button type = "reset" class = "btn btn-light">Re-enter</button>&nbsp;&nbsp;
						<button type = "submit" class = "btn btn-light" id = "submit">submit</button>&nbsp;&nbsp;
						<button type = "button" class = "btn btn-light" onclick="location.href='${contextPath}/member/memberForm.do'">Join</button>
				</div>
				<div class ="form-group" style="display: flex; justify-content: center; align-items: center;">
					<button type = "button" class = "btn btn-light" onclick="location.href='${contextPath}/member/findID.do'" >아이디 찾기</button>&nbsp;&nbsp;&nbsp;
					<button type = "button" class = "btn btn-light" onclick="location.href='${contextPath}/member/findPass.do'" >비밀번호 찾기</button>
				</div>
			</form>
		</div>
</body>
<script>
//휴대폰 번호 자동으로 - 추가 해주는 함수
function inputPhoneNumber() {
	
	var phone = document.getElementById("phone");		   
    var number = phone.value.replace(/[^0-9]/g, "");
    var phone1 = "";
    
    if(number.length < 4) {
        return number;
    } else if(number.length < 7) {
        phone1 += number.substr(0, 3);
        phone1 += "-";
        phone1 += number.substr(3);
    } else if(number.length < 11) {
        phone1 += number.substr(0, 3);
        phone1 += "-";
        phone1 += number.substr(3, 3);
        phone1 += "-";
        phone1 += number.substr(6);
    } else {
        phone1 += number.substr(0, 3);
        phone1 += "-";
        phone1 += number.substr(3, 4);
        phone1 += "-";
        phone1 += number.substr(7);
    }
    phone.value = phone1;
}
</script>

</html>