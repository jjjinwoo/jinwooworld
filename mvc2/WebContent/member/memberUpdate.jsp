<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"	uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt"	uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="contextPath" value="${pageContext.request.contextPath }"/>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>수정 화면</title>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap-theme.min.css">
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>	
<!-- 다음 API -->
<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
</head>
<body>
<jsp:include page ="../common/topMenu.jsp"></jsp:include>
			<div id="mainTitle">
				<h1 align = "center">회원 정보 수정</h1>
			</div>
			<br/>
			<!-- 내용 -->
			<div class = "container">
				<div class="row">
					<nav class="col-sm-2">
						<ul class="nav nav-pills nav-stacked">
							<li><a href="${contextPath}/member/mypageForm.do">회원 정보 보기</a></li>
							<li class="active"><a href="${contextPath}/member/memberUp.do">회원 정보 수정</a></li>
							<li><a href="${contextPath}/member/memberDelete.do">회원 탈퇴</a></li>
							<li><a href="${contextPath}/member/productBuy.do">주문 내역</a></li>
						</ul>
					</nav>
					<div class="col-sm-offset-1 col-sm-9">
						<form class = "form-horizontal" id = "frm" method="post" action="${contextPath}/member/memberUpdate1.do">
							<div class = "form-group">
								<label for = "subject" class = "col-sm-4 control-label">아이디</label>
								<div class = "col-sm-4">
									<input type = "text" class = "form-control" id = "userID" name = "userID" maxlength = "200" value = "${member.userID }" readonly>
								</div>
							</div>
							
							<div class="form-group">
								<label for="pwd" class="col-sm-4 control-label">비밀번호</label>
								<div class="col-sm-4">
									<input type="password" class="form-control" id="pwd" name="pwd" maxlength="20" value = "${member.pwd }" onkeyup="passConfirm()"/>
								</div>
							</div>
							<div class="form-group">
								<label for="repwd" class="col-sm-4 control-label">비밀번호 확인</label>
								<div class="col-sm-4">
									<input type="password" class="form-control" id="repwd" name="repwd" maxlength="20" value = ""onkeyup="passConfirm()"> 
									<br>
									<strong><span id ="confirmMsg" style = "font-size: 15px; padding: 5px;"></span></strong>
								</div>
							</div>
							
							<div class = "form-group">
								<label for = "subject" class = "col-sm-4 control-label">이  름</label>
								<div class = "col-sm-4">
									<input type = "text" class = "form-control" id = "name" name = "name" maxlength = "200" value = "${member.name }">
								</div>
							</div>
							<div class = "form-group">
								<label for="postnum" class="col-sm-4 control-label">우편번호</label>
								<div class="col-sm-4">
									<input type = "text" class = "form-control" name = "postnum" id = "postnum"  value = "${member.postnum }"/>
									<input type = "button" class = "form-control postBtn" onclick = "daumZipCode()" value = "우편번호 검색"/>
								</div>
							</div>
							<div class = "form-group">
								<label for = "subject" class = "col-sm-4 control-label">주  소</label>
								<div class = "col-sm-4">
									<input type = "text" class = "form-control" id = "address1" name = "address1" maxlength = "200" value = "${member.address1 }">
								</div>
							</div>
							<div class = "form-group">
								<label for = "subject" class = "col-sm-4 control-label">상세주소</label>
								<div class = "col-sm-4">
									<input type = "text" class = "form-control" id = "address2" name = "address2" maxlength = "200" value = "${member.address2 }">
								</div>
							</div>
							<div class = "form-group">
								<label for = "subject" class = "col-sm-4 control-label">phone</label>
								<div class = "col-sm-4">
									<input type = "text" class = "form-control" id = "phone" name = "phone" maxlength = "200" value = "${member.phone }" readonly/>
								</div>
							</div>	
							<br/>
							<div class = "form-group" id="myPageBtn">
								<button type = "reset" class = "btn btn-light mypageBtn">
									<span class = "glyphicon glyphicon-erase"></span> 다시 입력
								</button>
								<button type = "submit" id="Updatesubmit" name="Updatesubmit" class="btn btn-light mypageBtn">
									<span class = "glyphicon glyphicon-pencil"></span> 수정 완료
								</button>
								<button type = "button" class = "btn btn-light mypageBtn" onclick="location.href='${contextPath}/member/main.do'">
									<span class = "glyphicon glyphicon-home"></span> Home
								</button>
							</div>
						</form>
					</div>
				</div>
			</div>
</body>
<script>
$(document).ready(function(){
	
	$("#Updatesubmit").on("click", function(){
	
	
		if($("#pwd").val().trim() ==""){
			alert("비밀번호를 입력하셔야 합니다.");
			$("#pwd").focus();
			return false;
		}
		if($("#pwd").val().length < 4 || $("#pwd").val().length > 20){
			alert("비밀번호 확인는 최소 4자리 이상, 최대 20자리 이하을 입력하셔야 합니다.");
			$("#pwd").focus();
			return false;
		}
		
		if($("#repwd").val().trim() ==""){
			alert("비밀번호 확인을 입력하셔야 합니다.");
			$("#repwd").focus();
			return false;
		}
		if($("#repwd").val().length < 4 || $("#repwd").val().length > 20){
			alert("비밀번호 확인은 최소 4자리 이상, 최대 20자리 이하을 입력하셔야 합니다.");
			$("#repwd").focus();
			return false;
		}
		if($("#pwd").val() != $("#repwd").val() ){
			alert("비밀번호와 비밀번호 확인이 맞지 않습니다. 다시입력해주세요");
			$("#pwd").focus();
			return false;	
		}
		
		if($("#name").val().trim() ==""){
			alert("이름을 입력하셔야 합니다.");
			$("#name").focus();
			return false;
		}
		
		if($("#name").val().length > 20){
			alert("이름은 최대 20자리 이하을 입력하셔야 합니다.");
			$("#name").focus();
			return false;
		}
		if($("#postnum").val().trim() ==""){
			alert("주소를 입력해야합니다.");
			$("#postnum").focus();
			return false;
		}
		if($("#address1").val().trim() ==""){
			alert("주소를 입력해야합니다.");
			$("#address1").focus();
			return false;
		}
		if($("#address2").val().trim() ==""){
			alert("주소를 입력해야합니다.");
			$("#address2").focus();
			return false;
		}
		
		if (confirm("회원 정보를 수정하시겠습니까?")) {
            $("#frm").submit();
            alert("수정이 완료되었습니다");
        }
		
	});
	
	
	
});


function passConfirm() {
	 
	/* 비밀번호, 비밀번호 확인 입력창에 입력된 값을 비교해서 같다면 비밀번호 일치, 그렇지 않으면 불일치 라는 텍스트 출력.*/
	/* document : 현재 문서를 의미함. 작성되고 있는 문서를 뜻함. */
	/* getElementByID('아이디') : 아이디에 적힌 값을 가진 id의 value를 get을 해서 password 변수 넣기 */
				
	var pwd = document.getElementById('pwd');					//비밀번호 
	var repwd = document.getElementById('repwd');				//비밀번호 확인 값
	var confrimMsg = document.getElementById('confirmMsg');		//확인 메세지
	var correctColor = "#0000FF";	//맞았을 때 출력되는 색깔.
	var wrongColor ="#ff0000";	//틀렸을 때 출력되는 색깔
					
	if(pwd.value == repwd.value){//password 변수의 값과 passwordConfirm 변수의 값과 동일하다.
		confirmMsg.style.color = correctColor;/* span 태그의 ID(confirmMsg) 사용  */
		confirmMsg.innerHTML ="비밀번호가 일치합니다";/* innerHTML : HTML 내부에 추가적인 내용을 넣을 때 사용하는 것. */
	}else{
		confirmMsg.style.color = wrongColor;
		confirmMsg.innerHTML ="비밀번호가 일치하지 않습니다.";
	}
}

function daumZipCode() {
	
	new daum.Postcode({
					
		oncomplete: function(data) {
			// 팝업창에서 검색한 결과에서 항목을 클릭하였을 경우에 실행할 코드를 이곳에 작성한다.
						
			// 각 주소의 노출 규칙에 따라서 주소를 조합해야 한다.
			// 내려오는 변수가 값이 없을 경우에는 공백('')값을 가지므로 이름을 참고하여 분기한다.
						
			var fullAddr = '';	// 최종 주소값을 저장할 변수
			var subAddr = '';	// 조합형 주소값을 저장할 변수
						
			// 사용자가 선택한 주소의 타입에 따라서 해당 주소 값을 가져온다.
			if(data.userSelectedType == 'R') {	// 도로명 주소를 선택한 경우	
			
				fullAddr		= data.roadAddress;
						
			}else {	// 지번 주소를 선택한 경우
			
				fullAddr		= data.jibunAddress;
				
			}	
						
			// 사용자가 선택한 주소가 도로명 타입일 때 조합한다.
			if(data.userSelectedType == 'R'){
				// 법정도명이 있을 경우에 추가한다.
				if(data.bname != ''){
					subAddr += data.bname;
				}
				// 건물명이 있을 경우에 추가한다.
				if(data.buildingName != ''){
					subAddr += (subAddr != '' ? ', ' + data.buildingName : data.buildingName);
				}
				// 조합형 주소의 유무에 따라서 양쪽에 괄호를 추가하여 최종 주소를 만든다.
				fullAddr += (subAddr != '' ? '(' + subAddr +' )' : ' ');
			}
						
			// 우편번호와 주소정보를 화면의 해당 필드에 출력시킨다.
			document.getElementById('postnum').value	= data.zonecode;	// 5자리의 새 우편번호
			document.getElementById('address1').value	= fullAddr;
						
			// 커서를 상세주소 입력란으로 이동시킨다.
			document.getElementById('address2').focus();
		}
					
	}).open({
		//open에 이름을 부여하여 우편번호 팝업창이 여러개 뜨는 것을 방지하기 위해서 popupName을 사용한다.
		popupName:	'postcodePopup'
	});		
}

</script>
</html>