<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt"	uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var = "contextPath" value = "${pageContext.request.contextPath }"/>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title> 상세페이지</title>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap-theme.min.css">
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>	
</head>
<style>
    .pl {
        width: 220px;
        border: 1px solid #C4C4C4;
        box-sizing: border-box;
        border-radius: 10px;
        padding: 12px 13px;
        font-family: 'Roboto';
        font-style: normal;
        font-weight: 400;
        font-size: 14px;
        line-height: 16px;
        height: 45px;
    }

    .pl:focus {
        border: 1px solid #0e54e3;
        box-sizing: border-box;
        border-radius: 10px;
        outline: 3px solid #6dbff2;
        border-radius: 10px;
    }
</style>
<body>
	<br/>
	<jsp:include page ="../common/topMenu.jsp"></jsp:include>
	<br/>
        <h1 align="center">상품 상세 정보</h1>
	<div class="container" style="display: flex; justify-content: center;">
    <div>
       <img src="${contextPath}/image/uniform2/${product.images2}"  alt="${product.product_name}" width="400">
    </div>
    <div>
    	<br/><br/><br/>
        <h4><strong>상품 정보</strong></h4>
        <p><strong>상 품 타 입 : ${product.product_type}</strong></p>
        <p><strong>상 품 명 : ${product.product_name}</strong></p>
        <p><strong>가 격 : <fmt:formatNumber value="${product.product_price}" pattern="#,###원"/></strong></p>
        	<label for="quantity">수량&nbsp;&nbsp;:&nbsp;&nbsp;</label>
			<button onclick="decreaseValue()">-</button>&nbsp;&nbsp;
				<span id="quantity"><strong>1</strong></span>&nbsp;&nbsp;
			<button onclick="increaseValue()">+</button>
			<br/><br/>
            <label for="size">Option &nbsp;&nbsp;:&nbsp;&nbsp;</label>
            <select id="size" name="size" class="pl">
                <c:forEach var="option" items="${productOptionVO}">
				    <c:choose>
				        <c:when test="${option.quantity == 0}">
				            <option value="${option.product_size}" disabled>Size&nbsp;:&nbsp; ${option.product_size}&nbsp;,&nbsp;&nbsp;Stock&nbsp;:&nbsp;${option.quantity} (품절)</option>
				        </c:when>
				        <c:otherwise>
				            <option value="${option.product_size}">Size&nbsp;:&nbsp; ${option.product_size}&nbsp;,&nbsp;&nbsp;Stock&nbsp;:&nbsp;${option.quantity}</option>
				        </c:otherwise>
				    </c:choose>
				</c:forEach>
            </select>
            <c:if test="${product.deleteState == 'Y'}">
			    <script>
			        alert("사용자의 권한으로 인한 판매되지 않는 상품입니다.");
			    </script>
			</c:if>
            <br/><br/><br/>
			<button class="orderbtn btn btn-secondary" id="addcartBtn" style = "background-color: #d2d2d2; color: white; width : 500px;"><strong>ADD TO SHOPPING CART</strong></button>
            <button onclick="submitForm()" class="orderbtn btn btn-secondary" style="background-color: #787878; color: white; width : 500px;">
			    <strong>ORDER PRODUCT</strong>
			</button>
    </div>
</div>
<br/>
<br/>
	<h3 align="center">제품 상세 이미지</h3>
    <div class = "container" style="display: flex; justify-content: center;">
    <div>
		<img src="${contextPath}/image/uniform/${product.images1}"  alt="${product.product_name}"  width="500px">
    </div>
    </div>
    
</body>

<script>
function decreaseValue() {
  var value = parseInt(document.getElementById('quantity').innerHTML);
  value = isNaN(value) ? 0 : value;
  value < 2 ? value = 2 : '';
  value--;
  document.getElementById('quantity').innerHTML = value;
}

function increaseValue() {
  var value = parseInt(document.getElementById('quantity').innerHTML);
  value = isNaN(value) ? 0 : value;
  value++;
  document.getElementById('quantity').innerHTML = value;
}

// 장바구니로 넘기는 과정
document.getElementById("addcartBtn").onclick = function() {
    // 상품 정보를 가져옵니다.
    var product_code = "${product.product_code}";
    var product_name = "${product.product_name}";
    var product_price = "${product.product_price}";
    var quantity = document.getElementById("quantity").textContent;
    var product_size = document.getElementById("size").value;
    var images2 = "${product.images2}";
	
 	// 사이즈가 선택되지 않은 경우 경고 메시지를 표시합니다.
    if (product_size == null || product_size == "") {
        alert("사이즈를 선택해주세요.");
        return;
    }
    
    // addCart 컨트롤러를 호출합니다.
    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function() {
        if (xhr.readyState == XMLHttpRequest.DONE) {
        	console.log(xhr.responseText);
            if (xhr.responseText.trim() == "out of stock") {
                // 재고량이 부족한 경우 
            	alert('재고량이 구매량보다 많지 않습니다. 수량을 다시 확인하고 구매 부탁드립니다.');
            } else {
                if (confirm("장바구니로 이동하시겠습니까?")) {                  
                    window.location.href = "${contextPath}/member/mycartForm.do";
                }
            }
        }
    }
    xhr.open("GET", "${contextPath}/member/addCart.do?product_code=" + product_code + "&product_name=" + product_name + "&product_price=" + product_price + "&quantity=" + quantity + "&product_size=" + product_size + "&images2=" + images2);
    xhr.send();
}
// 바로 결제
function submitForm() {
	
    var quantity = document.getElementById("quantity").innerText;
    var size = document.getElementById("size").value;

 	// 사이즈가 선택되지 않은 경우 경고 메시지를 표시합니다.
    if (size == null || size == "") {
        alert("사이즈를 선택해주세요.");
        return;
    }
    
    if (!confirm("바로 결제를 진행하시겠습니까?")) {
        // [아니오] 버튼을 누른 경우
        return;
    }
    var form = document.createElement("form");
    form.setAttribute("method", "POST");
    form.setAttribute("action", "${contextPath}/member/orderDirect.do");

    var inputQuantity = document.createElement("input");
    inputQuantity.setAttribute("type", "hidden");
    inputQuantity.setAttribute("name", "quantity");
    inputQuantity.setAttribute("value", quantity);
    form.appendChild(inputQuantity);

    var inputSize = document.createElement("input");
    inputSize.setAttribute("type", "hidden");
    inputSize.setAttribute("name", "product_size");
    inputSize.setAttribute("value", size);
    form.appendChild(inputSize);

    var inputProductCode = document.createElement("input");
    inputProductCode.setAttribute("type", "hidden");
    inputProductCode.setAttribute("name", "product_code");
    inputProductCode.setAttribute("value", "${product.product_code}");
    form.appendChild(inputProductCode);

    var inputProductName = document.createElement("input");
    inputProductName.setAttribute("type", "hidden");
    inputProductName.setAttribute("name", "product_name");
    inputProductName.setAttribute("value", "${product.product_name}");
    form.appendChild(inputProductName);

    var inputProductPrice = document.createElement("input");
    inputProductPrice.setAttribute("type", "hidden");
    inputProductPrice.setAttribute("name", "product_price");
    inputProductPrice.setAttribute("value", "${product.product_price}");
    form.appendChild(inputProductPrice);

    var inputDelivery = document.createElement("input");
    inputDelivery.setAttribute("type", "hidden");
    inputDelivery.setAttribute("name", "delivery");
    inputDelivery.setAttribute("value", "${delivery}");
    form.appendChild(inputDelivery);
	
    var inputImages2 = document.createElement('input');
	inputImages2.setAttribute('type', 'hidden');
	inputImages2.setAttribute('name', 'images2');
	inputImages2.setAttribute('value', "${product.images2}");
	form.appendChild(inputImages2);
    
    // orderDirect.do에 POST 요청을 보내고 응답을 처리합니다.
    var xhr = new XMLHttpRequest();
    xhr.open('POST', '${contextPath}/member/getStock.do');
    
	// Accept 헤더를 추가합니다.
	xhr.setRequestHeader('Accept', 'application/json');
	
	xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    
	xhr.onload = function() {
        if (xhr.status === 200 && xhr.responseText.trim() === 'out of stock') {
            // 재고량이 부족한 경우 경고 메시지를 표시합니다.
            alert('재고량이 구매량보다 많지 않습니다. 수량을 다시 확인하고 구매 부탁드립니다.');
        } else {
            // 그렇지 않은 경우 폼을 제출합니다.
            alert('결제 페이지로 이동합니다.'); 
            document.body.appendChild(form);
            form.submit();
        }
    };
    
	xhr.send(new URLSearchParams(new FormData(form)).toString());
}

</script>



</html>