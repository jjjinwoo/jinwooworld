<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt"	uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var = "contextPath" value = "${pageContext.request.contextPath }"/>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>장바구니</title>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap-theme.min.css">
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>	
</head>
<style>
    table {
        border-collapse: collapse;
        width: 100%;
    }
    th, td {
        text-align: center;
        padding: 8px;
    }
    thead tr {
        background-color: #6d99f2;
        color: white;
    }
    tbody tr {
        background-color: white;
    }
    .btn-custom {
        background-color: #39558f;
        color: white;

    }
    .ordersub {
    display: inline-block;
	}
</style>
<body>
	<br/>
	<jsp:include page ="../common/topMenu.jsp"></jsp:include>
	<br/>
	
	<div class="container" id="cartListbox"> 
		<div class="ordersub" style="margin-right:300px; position:relative; bottom:0; width:200px;">
		</div>	
		<div class="ordersub" style="border-radius:50px; padding:15px; margin-bottom:20px; margin-right:0; background-color: #97BBEB;">
			<span class="glyphicon glyphicon-shopping-cart" style="font-size:3em;"></span>
		</div>
		<div class="ordersub">
			<h6>STEP1</h6>
			<h3>장바구니</h3>
		</div>
		<div class="ordersub" style="border-radius:50px; padding:15px; margin-bottom:20px; margin-right:0;">
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
	<br/>
	<div class="container">
	    <!-- 주문 상품 정보 -->
	    <table>
	        <thead>
	            <tr>
	                <th><input type="checkbox" onClick="check(this)"/>&nbsp;&nbsp;선택</th>
	                <th>상품명</th>
	                <th>수량</th>
	                <th>사이즈</th>
	                <th>가격</th>
	                <th>배송정보</th>
	                <th>합계</th>
	            </tr>
	        </thead>
	        <tbody>
	            <c:if test="${not empty cartList}">
		            <c:forEach var="cart" items="${cartList}">
		                <tr style = "border-bottom: 1px solid #ccd0d9;">
		                    <td><input type="checkbox" name="chk" value="${cart.cartNum}"></td>
		                    <td><img src="${contextPath}/image/uniform2/${cart.images2}"  alt="${cart.product_name}" width="100">&nbsp;&nbsp;${cart.product_name}</td>
		                    <td><button onclick="decreaseQuantity(this)">-</button>&nbsp;${cart.quantity}&nbsp;<button onclick="increaseQuantity(this)">+</button></td>
		                    <td>${cart.product_size}</td>
		                    <td><fmt:formatNumber value="${cart.product_price}" pattern="#,###원"/></td>
		                    <td>${cart.delivery}</td>
		                    <td><fmt:formatNumber value="${cart.product_price}" pattern="#,###원"/></td>
		                </tr>
		            </c:forEach>
		        </c:if>
		        <c:if test="${empty cartList}">
		        	<td colspan="7" style="text-align: center;">
				       <h5><strong>장바구니가 비어있습니다. 상품을 추가해주세요.</strong></h5>
				    </td>
		        </c:if>
		    </tbody>
		</table>
	    <br/>
		<!-- 장바구니 삭제 버튼 -->
	    <div id="cartDeleteBtn" style = "text-align:right;">
		    <button type="button" id="orderDeleteBtn" onclick="fn_cartDelete();" class="btn btn-custom">선택삭제</button>&nbsp;&nbsp;
		    <button type="button" id="orderAllDeleteBtn" onclick="fn_cartAllDelete();" class="btn btn-custom">전체삭제</button>
		</div>
		<br/>
	</div>
	
	<div class="container" style="border: 3px solid #39558f; height: 100px; width: 20%; margin-left: auto; margin-right: 400px;">
    <div class="row" style="border: 1px solid black; background-color: #39558f;">
        <div class="col-md-12 text-right">
            <h3 style="color: white;">결제 예정 금액 </h3>
        </div>
    </div>
    <div class="col-md-12 text-right">
        <h4></h4>
    </div>
    <form action="${contextPath}/member/orderForm.do" method="post">
        <c:forEach var="cart" items="${cartList}">
            <input type="hidden" name="product_code" value="${cart.product_code}">
            <input type="hidden" name="product_size" value="${cart.product_size}">
            <input type="hidden" name="quantity" value="${cart.quantity}">
            <input type="hidden" name="product_name" value="${cart.product_name}">
            <input type="hidden" name="product_price" value="${cart.product_price}">
            <input type="hidden" name="delivery" value="${cart.delivery}">
            <input type="hidden" name="userID" value="${cart.userID}">
            <input type="hidden" name="cartNum" value="${cart.cartNum}">
            <input type="hidden" name = "images2" value="${cart.images2}">
        </c:forEach>
        <!-- 장바구니에서 결제하는 경우 -->
        <input type="hidden" name="fromCart" value="true">
        <div class="row">
            <div class="col-md-12 text-right">
                <br/>
                <button type="submit" class="btn" style="width: 150px; height: 60px; background-color:#97BBEB; color: white; font-size: 16px;" onclick="return checkCart();"><strong>주문결제</strong></button>
            </div>
        </div>
    </form>
</div>

</body>
<script>

function checkCart() {
	
    if (${fn:length(cartList)} == 0) {
        alert("상품이 없습니다. 상품을 장바구니에 담아주세요.");
        return false;
    }
    var hasCheckedProduct = false;
    var checkboxes = document.querySelectorAll('input[type="checkbox"]');
    for (var i = 0; i < checkboxes.length; i++) {
        if (checkboxes[i].checked) {
            hasCheckedProduct = true;
            break;
        }
    }
	// 항목이 없을 때 전체 체크가 풀리게 변경해야함
    var checkboxes = document.querySelectorAll('.cart-checkbox');
    for (var i = 0; i < checkboxes.length; i++) {
        if (checkboxes[i].checked) {
            hasCheckedProduct = true;
            break;
        }
    }
   
    if (!hasCheckedProduct) {
        alert('체크된 상품이 없습니다. 상품을 선택해주세요.');
        return false;
    }
    // deleteState 값이 'Y'인 상품이 있는지 확인합니다.
    var hasDeletedProduct = false;
    <c:forEach var="product" items="${productList}">
        <c:if test="${product.deleteState == 'Y'}">
            hasDeletedProduct = true;
        </c:if>
    </c:forEach>
    
    if (hasDeletedProduct) {
        alert('사용자의 권한으로 인한 판매되지 않는 상품이 있습니다. 해당 상품을 삭제하고 다시 시도해주세요.');
        return false;
    }
    
 	// productOptionList에서 각 상품의 재고량을 가져옵니다.
    var stockList = [];
    <c:forEach var="productOption" items="${productOptionList}">
        stockList.push(${productOption.quantity});
    </c:forEach>

 // 장바구니에 있는 각 상품의 수량과 재고량을 비교합니다.
    var isStockLack = false;
    var quantityInputs = document.getElementsByName("quantity");
    for (var i = 0; i < quantityInputs.length; i++) {
        var quantity = parseInt(quantityInputs[i].value);
        var stock = stockList[i];
        if (quantity > stock) {
            isStockLack = true;
            break;
        }
    }

    if (isStockLack) {
        var outOfStockCount = 0;
        for (var i = 0; i < quantityInputs.length; i++) {
            var quantity = parseInt(quantityInputs[i].value);
            var stock = stockList[i];
            if (quantity > stock) {
                outOfStockCount++;
            }
        }

        if (outOfStockCount == quantityInputs.length) {
            alert('재고가 부족합니다.');
            return false;
        } else if (confirm("재고가 부족한 상품이 있습니다. 재고가 부족한 상품을 제외하고 결제 페이지로 이동하시겠습니까?")) {
            // 폼의 값을 수정하여 재고가 부족한 상품을 제외합니다.
            for (var i = quantityInputs.length - 1; i >= 0; i--) {
                var quantity = parseInt(quantityInputs[i].value);
                var stock = stockList[i];
                if (quantity > stock) {
                    document.querySelectorAll('input[name="product_code"]')[i].remove();
                    document.querySelectorAll('input[name="product_size"]')[i].remove();
                    document.querySelectorAll('input[name="quantity"]')[i].remove();
                    document.querySelectorAll('input[name="product_name"]')[i].remove();
                    document.querySelectorAll('input[name="product_price"]')[i].remove();
                    document.querySelectorAll('input[name="delivery"]')[i].remove();
                    document.querySelectorAll('input[name="userID"]')[i].remove();
                    document.querySelectorAll('input[name="cartNum"]')[i].remove();
                    document.querySelectorAll('input[name="images2"]')[i].remove();
                }
            }
            return true;
        } else {
            alert('품절된 상품입니다. 재입고 후 구매 부탁드립니다.');
            return false;
        }
    }
    
    return true;
 	
}


document.querySelector('form').addEventListener('submit', function(event) {
    // 체크되지 않은 행을 찾습니다.
    var rows = document.querySelectorAll('tbody tr');
    for (var i = rows.length - 1; i >= 0; i--) {
        var row = rows[i];
        var checked = row.querySelector('input[type="checkbox"]').checked;
        if (!checked) {
            // 폼의 해당 인덱스의 값을 제거합니다.
            document.querySelectorAll('input[name="product_code"]')[i].remove();
            document.querySelectorAll('input[name="product_size"]')[i].remove();
            document.querySelectorAll('input[name="quantity"]')[i].remove();
            document.querySelectorAll('input[name="product_name"]')[i].remove();
            document.querySelectorAll('input[name="product_price"]')[i].remove();
            document.querySelectorAll('input[name="delivery"]')[i].remove();
            document.querySelectorAll('input[name="userID"]')[i].remove();
            document.querySelectorAll('input[name="cartNum"]')[i].remove();
            document.querySelectorAll('input[name="images2"]')[i].remove();
        }
    }
});

function updatePrice() {
    // 결제 예정 금액을 계산합니다.
    var total = 0;
    document.querySelectorAll('tbody tr').forEach(function(row) {
        // 체크박스의 상태를 확인합니다.
        var checked = row.querySelector('input[type="checkbox"]').checked;
        if (checked) {
            // 수량을 가져옵니다.
            var quantity = parseInt(row.querySelector('button[onclick="decreaseQuantity(this)"]').nextSibling.nodeValue);
            // 단가를 가져옵니다.
            var price = parseInt(row.querySelector('td:nth-child(5)').textContent.replace(/[^0-9]/g, ''));
            // 총 가격을 계산합니다.
            var subtotal = quantity * price;
            total += subtotal;
            // 총 가격을 업데이트합니다.
            row.querySelector('td:nth-child(7)').textContent = subtotal.toLocaleString() + '원';
        } else {
            // 체크박스가 해제된 경우 총 가격을 0으로 설정합니다.
            row.querySelector('td:nth-child(7)').textContent = '0원';
        }
    });
    // 결제 예정 금액을 업데이트합니다.
    document.querySelector('.container h4').textContent = ' ' + total.toLocaleString() + '원';
}

document.querySelectorAll('button[onclick="increaseQuantity(this)"], button[onclick="decreaseQuantity(this)"], input[type="checkbox"]').forEach(function(element) {
    element.addEventListener('click', function() {
        updatePrice();
    });
});

// 페이지가 로드될 때 결제 예정 금액을 초기화합니다.
window.addEventListener('load', function() {
    updatePrice();
});

function decreaseQuantity(button) {
    var quantity = parseInt(button.nextSibling.nodeValue);
    if (quantity > 1) {
        button.nextSibling.nodeValue = '  ' + (quantity - 1) + '  ';
        var row = button.parentNode.parentNode;
        updatePrice(row);
        // 폼의 quantity 필드 값을 업데이트합니다.
        var index = Array.from(row.parentNode.children).indexOf(row);
        document.querySelectorAll('input[name="quantity"]')[index].value = quantity - 1;
    }
}

function increaseQuantity(button) {
    var quantity = parseInt(button.previousSibling.nodeValue);
    button.previousSibling.nodeValue = '  ' + (quantity + 1) + '  ';
    var row = button.parentNode.parentNode;
    updatePrice(row);
    // 폼의 quantity 필드 값을 업데이트합니다.
    var index = Array.from(row.parentNode.children).indexOf(row);
    document.querySelectorAll('input[name="quantity"]')[index].value = quantity + 1;
}

window.onload = function() {
    var checkboxes = document.querySelectorAll('input[type="checkbox"]');
    for (var i = 0; i < checkboxes.length; i++) {
        checkboxes[i].checked = true;
    }
    updatePrice();
}
function check(source) {
    var checkboxes = document.querySelectorAll('input[type="checkbox"]');
    for (var i = 0; i < checkboxes.length; i++) {
        if (checkboxes[i] != source)
            checkboxes[i].checked = source.checked;
    }
}

function fn_cartAllDelete() {
    if(!confirm("\n전체 상품을 삭제하시겠습니까?\n\n삭제하려면 [확인]버튼을 누르시고, 아니면 [취소]버튼을 누르십시오.")) {
        alert("상품 삭제를 취소하셨습니다.");
    } else {
        // [확인]버튼을 눌렀을 경우
        $.ajax({
            url: '${contextPath}/member/deleteAll.do',
            type: 'post',
            success: function(result) {
                // 성공적으로 삭제되었을 경우
                console.log(result); // result 값을 확인
                if (result.trim() === "success") {
                    alert("장바구니의 모든 상품이 삭제되었습니다.");
                    location.reload();
                } else {
                    // 삭제에 실패했을 경우
                    alert("장바구니의 상품 삭제에 실패하였습니다.");
                }
            },
            error: function(error) {
                // 삭제에 실패했을 경우
                alert("장바구니의 상품 삭제에 실패하였습니다." + error);
            }
        });
    }
}

function fn_cartDelete() {
    var checked = document.querySelectorAll('input[name="chk"]:checked');
    if (checked.length === 0) {
        alert("삭제할 상품을 선택해주세요.");
        return;
    }
    
    if(!confirm("\n선택한 상품을 삭제하시겠습니까?\n\n삭제하려면 [확인]버튼을 누르시고, 아니면 [취소]버튼을 누르십시오.")) {
        alert("상품 삭제를 취소하셨습니다.");
    } else {
        // [확인]버튼을 눌렀을 경우
        var cartNums = [];
        for (var i = 0; i < checked.length; i++) {
            cartNums.push(checked[i].value);
        }
        
        $.ajax({
            url: '${contextPath}/member/deletePart.do',
            type: 'post',
            data: {userID: '${userID}', cartNum: cartNums},
            traditional: true,
            success: function(result) {
                // 성공적으로 삭제되었을 경우
                 console.log(result); // result 값을 확인
                if (result.trim() === "success") {
                    alert("장바구니의 선택한 상품이 삭제되었습니다.");
                    location.reload();
                } else {
                    // 삭제에 실패했을 경우
                    alert("장바구니의 상품 삭제에 실패하였습니다.");
                }
            },
            error: function(error) {
                // 삭제에 실패했을 경우
                alert("장바구니의 상품 삭제에 실패하였습니다.");
            }
        });
    }
}
</script>
</html>