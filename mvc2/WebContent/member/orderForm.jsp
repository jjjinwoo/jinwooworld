<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt"	uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var = "contextPath" value = "${pageContext.request.contextPath }"/>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>결제 화면</title>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap-theme.min.css">
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>	
<!-- 다음 API -->
<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
</head>
<style>
th, td {
        text-align: center;
        
    }
thead tr {
        background-color: #6d99f2;
        color: white;
    } 
tbody tr {
    background-color: white;
   } 

.ordersub {
    display: inline-block;
	}
#orderDetail {
    display: flex;
    justify-content: center;
}	
</style>
<body>
	<br/>
	<jsp:include page ="../common/topMenu.jsp"></jsp:include>
	<br/>
	<div class="container" id="cartListbox"> 	
		<div class="ordersub" style="margin-right:300px; position:relative; bottom:0; width:200px;">
		</div>
		<div class="ordersub" style="border-radius:50px; padding:15px; margin-bottom:20px; margin-right:0;">
			<span class="glyphicon glyphicon-shopping-cart" style="font-size:3em;  color:black;"></span>
		</div>
		<div class="ordersub">
			<h6>STEP1</h6>
			<h3>장바구니</h3>
		</div>
		<div class="ordersub" style="border-radius:50px; padding:15px; margin-bottom:20px; margin-right:0; background-color: #97BBEB;">
			<span class="glyphicon glyphicon-list-alt" style="font-size:3em;"></span>
		</div>
		<div class="ordersub">
			<h6>STEP2</h6>
			<h3>주문서작성</h3>
		</div>
		<div class="ordersub" style="border-radius:50px; padding:15px; margin-bottom:20px; margin-right:0;">
			<span class="glyphicon glyphicon-ok" style="font-size:3em;"></span>
		</div>
		<div class="ordersub">
			<h6>STEP3</h6>
			<h3>결제/주문완료</h3>
		</div>
	</div>
	
	<div class="col-sm-6" id="orderDetail">
	<!-- 회원 배송 정보 -->
		<div class="col-sm-12" id="deliveryDetail">
			<table style="border-collapse: collapse; width: 50%; margin-left: 450px;">
				<tr>
					<th colspan="2">
						<h4><strong>주문자 정보</strong></h4>
					</th>
					</tr>
					<tr>
						<td>* 주문자명</td>
						<td style = "padding-top: 10px; padding-bottom: 10px;"><input type="text" class="form-control" id="name"  value="${member.name}" readonly/></td>
					</tr>	
					<tr></tr> <!-- 빈 요소를 추가하여 간격 조절 -->
					<tr>
						<td>* 휴대폰 번호</td>
						<td style = "padding-top: 10px; padding-bottom: 10px;"><input type="text" class="form-control" id="phone" value="${member.phone}" readonly/></td>
					</tr>
					<tr></tr> <!-- 빈 요소를 추가하여 간격 조절 -->								
					<tr>
						<th colspan="3">
							<h4><strong>배송지 정보 </strong></h4> <p>주문자 정보와 동일 </p><input type="checkbox" id="deliveryCheckbox" checked/>
						</th>
					</tr>
					<tr>
						<td>* 수령인</td>
						<td style = "padding-top: 10px; padding-bottom: 10px;"><input type="text" class="form-control" id="deliveryName" name="order_name" value="${member.name}"/></td>
					</tr>	
					<tr>
						<td>배송지 선택</td>
						<td style = "padding-top: 10px; padding-bottom: 10px;">
							<input type="radio" id="originAdress" name="deliveryAddress" value="originAdress" checked/> 기본 배송지
							<input type="radio" id="newAdress" name="deliveryAddress" value='newAdress' /> 새로운 배송지
						</td>
					</tr>	
					<tr>
						<td>* 휴대폰 번호</td>
						<td style = "padding-top: 10px; padding-bottom: 10px;">
						<input type="text" class="form-control" id="deliveryPhone" name="order_phone" value="${member.phone}" oninput="this.value = this.value.replace(/[^0-9.]/g, '').replace(/(\..*)\./g, '$1');" onKeyup="inputPhoneNumber();"/>
						</td>
					</tr>							
					<tr>
						<td>* 배송지</td>
						<td>
							<div>
								<div class="col-sm-7" style="padding: 0px;">
									<input type = "text" class="form-control postNumInline" id="deliveryPostnum" name="postnum" value="${member.postnum}" style = "width: 200px;"readonly/>
										
								</div>
								<div class="col-sm-3">
									<input type = "button" class=" form-control postNumInline" id="postBtn" onclick = "daumZipCode()" value = "우편번호 검색" style = "width: 120px;" disabled/>
										
								</div>
								</div>
									<input type = "text"  class="form-control" id="deliveryAddress1" name="order_address1"value="${member.address1}"  readonly/>
									<input type="text"  class="form-control" id="deliveryAddress2" name="order_address2" value="${member.address2}"/>
								</td>
							</tr>	
							<tr> 
								<td>* 배송 메모</td> 
								<td style = "padding-top: 10px; padding-bottom: 10px;"> 
									<select class="form-control" id="orderMemoSelect" name = "order_memo"> 
									<option disabled selected>배송 메모를 선택해주세요</option> 
									<option>배송 전에 미리 연락 바랍니다.</option> 
									<option>부재시 경비실에 맡겨주세요.</option> 
									<option>부재시 전화나 문자 주세요.</option> 
									<option>문 앞에 놔주세요.</option> 
									<option value="makeDeliveryMemo">직접 입력</option> 
									</select> 
									<input type="text" id="orderMemo" name = "order_memo" style="display: none; width: 100%" style = "padding-top: 10px; padding-bottom: 10px;"> 
								</td> 
							</tr>
						</table>
						<!-- 결제 정보 -->	
						<table style="border-collapse: collapse; width: 50%; margin-left: 450px;">
							<tr>
								<th colspan="2">
									<h4><strong>결제 수단</strong></h4>
								</th>
							</tr>
							<tr id="payMethod">
								<td colspan="2">
									<button type="button" class="form-control" id="payMethodCash">가상계좌이체</button>
									<button type="button" class="form-control" id="payMethodCard">카드결제</button>
									<button type="button" class="form-control" id="payMethodKakao">카카오페이</button>
									<button type="button" class="form-control" id="payMethodPhone">휴대폰 소액결제</button>
									<!-- 결제 수단 구분을 위한 hidden input -->
									<input type="hidden" id="payMethodInput" value="1"/>
								</td>
							</tr>
						</table>
					</div>
				</div>
				<!-- 상품 정보 -->	
				<div class="container">
				    <div class="row">
				        <div class="col-sm-6">
				            <table style="border-collapse: collapse; width: 120%; margin-top: 30px;">
				                <thead>
				                    <tr style = "height: 30px;">
				                        <th>상품명</th>
				                        <th style = " padding-right: 15px;">수량</th>
				                        <th>사이즈</th>
				                        <th>가격</th>
				                        <th>배송정보</th>
				                    </tr>
				                </thead>
				                <tbody>
				                   <c:set var="totalbill" value="0"/>
				                    <c:forEach var="cart" items="${cartList}">
				                        <tr>
				                            <td><img src="${contextPath}/image/uniform2/${cart.images2}"  alt="${cart.product_name}" width="100">&nbsp;&nbsp;${cart.product_name}</td>
				                            <td>${cart.quantity}</td>
				                            <td>${cart.product_size}</td>
				                            <td><fmt:formatNumber value="${cart.product_price}" pattern="#,###원"/></td>
				                            <td>${cart.delivery}</td>
				                        </tr>
				                        <c:set var="totalbill" value="${totalbill + (cart.product_price * cart.quantity)}"/>
				                    </c:forEach>
				                </tbody>
				                <tfoot>
				                    <tr>
				                        <td colspan="5"><h5><strong>최종 결제 금액: <fmt:formatNumber value="${totalbill}" pattern="#,###원"/></strong></h5></td>
				                    </tr>
				                </tfoot>
				            </table>
				            <br/></br/>
				            <!-- 버튼 -->
				            <form action="${contextPath}/member/payment.do" method="post" onsubmit="return submitForm()">
							    <input type="hidden" name="order_phone" value="${member.phone}">
								<input type="hidden" name="order_name" value="${member.name}">
								<input type="hidden" name="order_address1" value="${member.address1}">
								<input type="hidden" name="order_address2" value="${member.address2}">
								<input type="hidden" name="postnum" value="${member.postnum}">
								<input type="hidden" name="order_memo" value="${order_memo}">
								<input type="hidden" name="paymethod" value="${paymethod}">
							    <input type="hidden" name="totalbill" value="${totalbill}">
							    <!-- 장바구니를 거치지 않고 결제하는 경우 -->
    							<input type="hidden" name="fromCart" value="${fromCart}">
							    <c:forEach var="cart" items="${cartList}" varStatus="status">
								        <input type="hidden" name="cartNum" value="${cart.cartNum}">
								        <input type="hidden" name="product_code" value="${cart.product_code}">
								        <input type="hidden" name="product_size" value="${cart.product_size}">
								        <input type="hidden" name="quantity" value="${cart.quantity}">
								        <input type="hidden" name="product_name" value="${cart.product_name}">
								        <input type="hidden" name="product_price" value="${cart.product_price}">
								        <input type="hidden" name="delivery" value="${cart.delivery}">
								        <input type="hidden" name="images2" value="${cart.images2}">
								</c:forEach>
							    <button type=submit value="Submit" style="width: 150px; height: 60px; background-color:#97BBEB; color: white; font-size: 16px; position: absolute; right: 150px;">결제</button>
							</form>
				        </div>
					</div>
					
				</div>
				
							
</body>
<script>

//결제 수단 버튼을 클릭했을 때의 이벤트 핸들러를 정의합니다.
function onPayMethodButtonClick(event) {
    // 결제 수단 버튼의 id 값을 payMethodInput 요소의 값으로 설정합니다.
    document.querySelector("#payMethodInput").value = event.target.id;
}
document.querySelector("#payMethodCash").addEventListener("click", onPayMethodButtonClick);
document.querySelector("#payMethodCard").addEventListener("click", onPayMethodButtonClick);
document.querySelector("#payMethodKakao").addEventListener("click", onPayMethodButtonClick);
document.querySelector("#payMethodPhone").addEventListener("click", onPayMethodButtonClick);

function submitForm() {
    // 배송 정보 값을 가져옵니다.
    var orderName = document.querySelector("input[name='order_name']").value.trim();
    var orderPhone = document.querySelector("input[name='order_phone']").value.trim();
    var orderAddress1 = document.querySelector("input[name='order_address1']").value.trim();
    var orderAddress2 = document.querySelector("input[name='order_address2']").value.trim();
    var postnum = document.querySelector("input[name='postnum']").value.trim();
    
    // 배송 정보 값이 입력되었는지 확인합니다.
    if (orderName == "" || orderPhone == "" || orderAddress1 == "" || orderAddress2 == "" || postnum == "") {

        alert("필수 입력 항목을 입력해주세요.");
        return false;
    } 
    
    // 배송 메모 값을 가져옵니다.
    var orderMemoSelect = document.querySelector("#orderMemoSelect").value;
    var orderMemo = document.querySelector("#orderMemo").value.trim();
    
    // 배송 메모 값이 입력되었는지 확인합니다.
    if (orderMemoSelect === "배송 메모를 선택해주세요" && orderMemo === "") {
        alert("배송 메모를 선택해주세요.");
        return false;
    }
    
    // 결제 수단 값을 가져옵니다.
    var payMethodInput = document.querySelector("#payMethodInput").value;
    
    // 결제 수단 값이 입력되었는지 확인합니다.
    if (payMethodInput === "1") {
        alert("결제 수단을 선택해주세요.");
        return false;
    }
	
 	// getStock.do에 POST 요청을 보내고 응답을 처리합니다.
 	 //var cartListSize = ${fn:length(cartList)};
		var xhr = new XMLHttpRequest();
		xhr.open('POST', '${contextPath}/member/getStock2.do');
		xhr.setRequestHeader('Accept', 'application/json');
		xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
		xhr.onload = function() {
		    if (xhr.status === 200) {
		        var response = JSON.parse(xhr.responseText);
		        if (response.result.trim() === 'out of stock') {
		            // 재고량이 부족한 경우 경고 메시지를 표시합니다.
		            alert('해당 제품 중 품절된 상품이 있습니다. 빠른 시일 내에 입고 처리하겠습니다.');
		            return false;
		        } else {
		            // 그렇지 않은 경우 폼을 제출합니다.
		            alert("결제가 완료되었습니다.")
		            document.forms[0].submit();
		            //return true;
		        }
		    }
		};

		// POST 요청을 보냅니다.
		xhr.send(new URLSearchParams(new FormData(document.forms[0])).toString());

		// 폼 제출을 중단합니다.
		return false;
}

// 버튼 CSS
document.getElementById("payMethodCash").addEventListener("click", function() {
	  var button = this;
	  var isClicked = button.getAttribute("data-clicked");
	  
	  if (isClicked === "true") {
	    button.style.backgroundColor = "";
	    button.setAttribute("data-clicked", "false");
	  } else {
	    button.style.backgroundColor = "#AEC6CF";
	    button.setAttribute("data-clicked", "true");
	  }
	});

	document.getElementById("payMethodCard").addEventListener("click", function() {
	  var button = this;
	  var isClicked = button.getAttribute("data-clicked");
	  
	  if (isClicked === "true") {
	    button.style.backgroundColor = "";
	    button.setAttribute("data-clicked", "false");
	  } else {
	    button.style.backgroundColor = "#AEC6CF";
	    button.setAttribute("data-clicked", "true");
	  }
	});

	document.getElementById("payMethodKakao").addEventListener("click", function() {
	  var button = this;
	  var isClicked = button.getAttribute("data-clicked");
	  
	  if (isClicked === "true") {
	    button.style.backgroundColor = "";
	    button.setAttribute("data-clicked", "false");
	  } else {
	    button.style.backgroundColor = "#AEC6CF";
	    button.setAttribute("data-clicked", "true");
	  }
	});

	document.getElementById("payMethodPhone").addEventListener("click", function() {
	  var button = this;
	  var isClicked = button.getAttribute("data-clicked");
	  
	  if (isClicked === "true") {
	    button.style.backgroundColor = "";
	    button.setAttribute("data-clicked", "false");
	  } else {
	    button.style.backgroundColor = "#AEC6CF";
	    button.setAttribute("data-clicked", "true");
	  }
	});


var payMethod = "";

document.getElementById("payMethodCash").addEventListener("click", function() {
    payMethod = "가상계좌이체";
    document.querySelector("[name=paymethod]").value = payMethod;
});

document.getElementById("payMethodCard").addEventListener("click", function() {
    payMethod = "카드결제";
    document.querySelector("[name=paymethod]").value = payMethod;
});

document.getElementById("payMethodKakao").addEventListener("click", function() {
    payMethod = "카카오페이";
    document.querySelector("[name=paymethod]").value = payMethod;
});

document.getElementById("payMethodPhone").addEventListener("click", function() {
    payMethod = "휴대폰 소액결제";
    document.querySelector("[name=paymethod]").value = payMethod;
});

// 배송메모 
document.getElementById("orderMemoSelect").addEventListener("change", function() {
    var orderMemoInput = document.getElementById("orderMemo");
    var formOrderMemoInput = document.querySelector("form [name=order_memo]");
    if (this.value === "makeDeliveryMemo") {
        orderMemoInput.style.display = "block";
    } else {
        orderMemoInput.style.display = "none";
        var selectedText = this.options[this.selectedIndex].text;
        orderMemoInput.value = selectedText;
        formOrderMemoInput.value = selectedText;
    }
});

document.getElementById("orderMemo").addEventListener("input", function() {
    var formOrderMemoInput = document.querySelector("form [name=order_memo]");
    formOrderMemoInput.value = this.value;
});

// 배송지 관련
var deliveryName = $("#deliveryName").val();
var deliveryPhone = $("#deliveryPhone").val();
var deliveryPostnum = $("#deliveryPostnum").val();
var deliveryAddress1 = $("#deliveryAddress1").val();
var deliveryAddress2 = $("#deliveryAddress2").val();

$('#deliveryCheckbox').click(function() {
    // 체크 박스를 클릭 했을 때
    if ($('#deliveryCheckbox').is(':checked')) {
        // 주문자 정보와 동일 할 때
        // input 박스에 원래 값을 넣는다.
        $("#deliveryName").val(deliveryName);
        $("#deliveryPhone").val(deliveryPhone);
        $("#deliveryPostnum").val(deliveryPostnum);
        $("#deliveryAddress1").val(deliveryAddress1);
        $("#deliveryAddress2").val(deliveryAddress2);

        // 배송지 선택 checked를 바꾼다.
        $("#originAdress").prop("checked", true);
        $("#newAdress").prop("checked", false);
        // 우편번호 버튼에 disabled 속성을 추가한다.
        $("#postBtn").attr("disabled", true);

    } else {
        // 주문자 정보와 동일하지 않을 때
        // input 박스 안의 값을 초기화 한다.
        $("#deliveryName").val('');
        $("#deliveryPhone").val('');
        $("#deliveryPostnum").val('');
        $("#deliveryAddress1").val('');
        $("#deliveryAddress2").val('');

        // 배송지 선택 checked를 바꾼다.
        $("#newAdress").prop("checked", true);
        $("#originAdress").prop("checked", false);

        // 우편번호 버튼의 disabled 속성을 제거한다.
        $("#postBtn").attr("disabled", false);
    }
});

//폼 전송 전에 hidden input 태그의 value 속성을 업데이트한다.
$('form').submit(function() {
    $('input[name="order_name"]').val($('#deliveryName').val());
    $('input[name="order_phone"]').val($('#deliveryPhone').val());
    $('input[name="postnum"]').val($('#deliveryPostnum').val());
    $('input[name="order_address1"]').val($('#deliveryAddress1').val());
    $('input[name="order_address2"]').val($('#deliveryAddress2').val());
});

// 배송지
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
			document.getElementById('deliveryPostnum').value	= data.zonecode;	// 5자리의 새 우편번호
			document.getElementById('deliveryAddress1').value	= fullAddr;
						
			// 커서를 상세주소 입력란으로 이동시킨다.
			document.getElementById('deliveryAddress2').focus();
		}
					
	}).open({
		//open에 이름을 부여하여 우편번호 팝업창이 여러개 뜨는 것을 방지하기 위해서 popupName을 사용한다.
		popupName:	'postcodePopup'
	});		
}

function inputPhoneNumber() {
	
	var phone = document.getElementById("deliveryPhone");		   
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