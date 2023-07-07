<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<c:set var = "contextPath" value = "${pageContext.request.contextPath }"/>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원가입</title>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap-theme.min.css">
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>	
<!-- 다음 API -->
<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>

</head>
<body>
	<br/><br/>
	<jsp:include page ="../common/topMenu.jsp"></jsp:include>
	
			<div id="mainTitle">
				<h1 align = "center">Join</h1>
			</div>
			
			<div class="container">
				<form class="form-horizontal" method="post" id="loginForm" name="memInsForm" action="${contextPath}/member/addMember.do">
					<div class="form-group">
						<label for="id" class="col-sm-3 control-label">아이디</label>
						<div class="col-sm-3">
							<input type="text" class="form-control" id="userID" name="userID" maxlength="20" placeholder="아이디 입력"/>
							<strong style = "color: red;">&nbsp;&nbsp;ID 중복 여부를 확인해주세요.</strong>
						</div>
						<div class = "col-sm-4">
							<!-- 아이디 중복검사 -->
							<button class="btn btn-light idCheck" type="button" id="idCheck" onClick="fn_idCheck();" value="N">아이디 중복 검사</button>
						</div>
					</div>
					
					<!-- 비밀번호 일치 여부 -->
					<div class="form-group">
						<label for="pwd" class="col-sm-3 control-label">비밀번호</label>
						<div class="col-sm-3">
							<input type="password" class="form-control" id="pwd" name="pwd" maxlength="20" placeholder="비밀번호 입력" onkeyup="passConfirm()"/>
						</div>
					</div>
					<div class="form-group">
						<label for="repwd" class="col-sm-3 control-label">비밀번호 확인</label>
						<div class="col-sm-3">
							<input type="password" class="form-control" id="repwd" name="repwd" maxlength="20" placeholder="비밀번호 확인" onkeyup="passConfirm()"> 
							<br>
							<strong><span id ="confirmMsg"></span></strong>
						</div>
					</div>
					<div class="form-group">
						<label for="name" class="col-sm-3 control-label">이  름</label>
						<div class="col-sm-2">
							<input type="text" class="form-control" id="name" name="name" maxlength="20" placeholder="이름 입력"/>
						</div>
					</div>
					<div class="form-group">
						<label for="postnum" class="col-sm-3 control-label">우편번호</label>
						<div class="col-sm-2">
							<input type = "text" class = "form-control" name = "postnum" id = "postnum" readonly/>
							<input type = "button" class = "form-control postBtn" onclick = "daumZipCode()" value = "우편번호 검색"/>
						</div>
					</div>
					<div class="form-group">
						<label for="address1" class="col-sm-3 control-label">주  소</label>
						<div class="col-sm-5">
							<input type = "text" class = "form-control" id = "address1" name = "address1" readonly/>
						</div>
					</div>
					<div class = "form-group">
						<label for="address2" class = "col-sm-3 control-label">상 세 주 소</label>
						<div class = "col-sm-5">
							<input type = "text" class = "form-control" id = "address2" name = "address2"/>
						</div> 
					</div>
					<div class="form-group">
						<label for="phone" class="col-sm-3 control-label">Phone</label>
						<div class="col-sm-4">
							<input type="text" class="form-control" id="phone" name="phone" placeholder="휴대폰 번호"
							oninput="this.value = this.value.replace(/[^0-9.]/g, '').replace(/(\..*)\./g, '$1');" onKeyup="inputPhoneNumber();" maxlength="13"/>
							<strong style = "color: red;">&nbsp;&nbsp;전화번호 중복 여부를 확인해주세요.</strong>
						</div>
						<div class = "col-sm-4">
							<!-- 전화번호 중복검사 -->
							<button class="btn btn-light idCheck" type="button" id="phoneCheck" onClick="fn_phoneCheck();" value="N">전화번호 중복 검사</button>
						</div>
					</div>
					<div class="form-group">
						<label for="name" class="col-sm-3 control-label">회원가입 동의</label>
						<div class="col-sm-2">
							<label class="radio-inline">
								<input type="radio" class="radio-value" id="register_Yn" name="register_Yn" value="yes" checked> 동의&nbsp;&nbsp;
							</label>
							<label class="radio-inline">
								<input type="radio" class="radio-value" id="register_Yn" name="register_Yn" value="no"> 동의 안함
							</label>
						</div>
					</div>
				</form>
				<div id="joinButtonBox">
					<button type="reset" form="loginForm"  class="btn btn-light">re-enter</button>
					<button type="button" class="btn btn-light cancel">cancel</button>
					<button type="submit" form="loginForm" id="memberFormsubmit" name="checkButton" class="btn btn-light"  >submit</button>
				</div>
			</div>
			
</body>

	<script>
	function fn_idCheck(){
		var inputUserID = document.getElementById("userID").value;
		
		if (inputUserID.trim() == "") {
	        alert("아이디를 입력하십시오.");
	        return;
	    }
	    $.ajax({
	        type: "post",
	        url : "${contextPath}/member/idCheck.do",
	        data: {userID : inputUserID},
	        success: function(data){
	            if(data.trim() == "fail"){ // 입력한 아이디가 이미 존재하는 경우
	                $("#idCheck").attr("value", "N"); //중복 확인 초기화
	                alert("이미 사용 중인 아이디입니다. \n\n 다른 아이디를 입력하십시오.");
	            } else { // 입력한 아이디가 존재하지 않는 경우
	                $("#idCheck").attr("value", "Y");
	                alert("사용 가능한 아이디입니다.");
	            }
	        },
	        error: function(xhr, status, error) {
	            alert("AJAX 요청 실패: " + error);
	        }
	    });
	}
	
	function fn_phoneCheck(){
		
		var inputPhone = document.getElementById("phone").value;
		
		if (inputPhone.trim() == "") {
	        alert("전화번호를 입력하십시오.");
	        $("#phone").focus();
	        return;
	    }
	    $.ajax({
	        type: "post",
	        url : "${contextPath}/member/phoneCheck.do",
	        data: {phone : inputPhone},
	        success: function(data){
	            if(data == "fail"){ // 입력한 아이디가 이미 존재하는 경우
	                $("#phoneCheck").attr("value", "N"); //중복 확인 초기화
	                alert("이미 사용 중인 번호입니다. \n\n 다른 번호를 입력하십시오.");
	            } else { // 입력한 아이디가 존재하지 않는 경우
	                $("#phoneCheck").attr("value", "Y");
	                alert("사용 가능한 번호입니다.");
	            }
	        }
	    });
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
	
	
	$(document).ready(function(){
		
		$(".cancel").on("click", function(){ //취소 버튼을 눌렀을 때
			location.href = "${contextPath}/member/main.do"; //메인페이지로 이동
		});
		
		//아이디 입력 시 한글입력 안되게 처리
		$("input[name=userID]").keyup(function(event){ 
			if (!(event.keyCode >=37 && event.keyCode<=40)) {
				var inputVal = $(this).val();
				$(this).val(inputVal.replace(/[^a-z0-9]/gi,''));
			}
		});
		
		//아이디 입력값 변화 후 아이디 중복 확인 알람 창 뜨기
		$( '#userID' ).change( function() {
	        $('#idCheck').attr("value", "N");
	    } );
		
		$( '#phone' ).change( function() {
	        $('#phoneCheck').attr("value", "N");
	    } );
		
		// 회원 가입 버튼을 눌렀을 경우(userID = "submit") - memberForm 에서
		$("#memberFormsubmit").on("click", function(){
		
			if($("#userID").val().trim() ==""){
				alert("아이디를 입력하셔야 합니다.");
				$("#userID").focus();
				return false;
			}
			if($("#userID").val().length < 4 || $("#userID").val().length > 20){
				alert("아이디는 최소 4자리 이상, 최대 20자리 이하로 입력하셔야 합니다.");
				$("#userID").focus();
				return false;
			}
			if($("#idCheck").val() == "N"){
				alert("아이디 중복검사가 필요합니다. 중복확인을 해주세요");
				$("#idCheck").focus();
				return false;
			}
			if($("#phoneCheck").val() == "N"){
				alert("전화번호 중복검사가 필요합니다. 중복확인을 해주세요");
				$("#phoneCheck").focus();
				return false;
			}
			
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
			
			if($("#phone").val().trim() ==""){
				alert("핸드폰번호를 입력하셔야 합니다.");
				$("#phone").focus();
				return false;
			}
			
			
			if($('input[name=register_Yn]:checked').val()=="no"){
				alert("동의버튼을 누르셔야 합니다.");
				$("#register_Yn").focus();
				return false;
			}
			

		});
		
		
		
	});
	</script>







</html>