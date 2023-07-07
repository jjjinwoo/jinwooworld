<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt"	uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var = "contextPath" value = "${pageContext.request.contextPath }"/>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>마이페이지</title>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap-theme.min.css">
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>	
</head>
<style>
    #myPageBtn {
        text-align: center;
    }
    .pl {
        width: 100px;
        border: 1px solid #C4C4C4;
        box-sizing: border-box;
        border-radius: 10px;
        padding: 12px 13px;
        font-family: 'Roboto';
        font-style: normal;
        font-weight: 400;
        font-size: 14px;
        line-height: 16px;
    }

    .pl:focus {
        border: 1px solid #0e54e3;
        box-sizing: border-box;
        border-radius: 10px;
        outline: 3px solid #6dbff2;
        border-radius: 10px;
    }
    .pl2 {
        width: 300px;
        border: 1px solid #C4C4C4;
        box-sizing: border-box;
        border-radius: 10px;
        padding: 12px 13px;
        font-family: 'Roboto';
        font-style: normal;
        font-weight: 400;
        font-size: 14px;
        line-height: 16px;
    }

    .pl2:focus {
        border: 1px solid #0e54e3;
        box-sizing: border-box;
        border-radius: 10px;
        outline: 3px solid #6dbff2;
        border-radius: 10px;
    }
    input[type="submit"] {
    background-color: #6d99f2;
    color: white;
    padding: 12px 20px;
    border: none;
    border-radius: 4px;
    cursor: pointer;
	}

	input[type="submit"]:hover {
	    background-color: #1445a8;
	}

    table {
        border-collapse: collapse;
        display: flex;
    	justify-content: center;
    }
    th, td {
        padding: 8px;
        text-align: center;
        vertical-align: middle;
    }
  
</style>
<body>
	<br/>
	<jsp:include page ="../common/topMenu.jsp"></jsp:include>
	<br/>
	<div id="mainTitle">
		<h1 align = "center">주문 내역</h1>
	</div>
	<br/>
	<div class = "container">
				<div class="row">
					<nav class="col-sm-2">
						<ul class="nav nav-pills nav-stacked">
							<li><a href="${contextPath}/member/mypageForm.do">회원 정보 보기</a></li>
							<li><a href="${contextPath}/member/memberUp.do">회원 정보 수정</a></li>
							<li><a href="${contextPath}/member/memberDelete.do">회원 탈퇴</a></li>
							<li class="active"><a href="${contextPath}/member/productBuy.do">주문 내역</a></li>
						</ul>
					</nav>
					<!-- 내용 -->
					<table width="500" align = "right" style = "margin-right: 0px;">
						<tr>
							<td align="right" style="font-family: Gulim; font-size: 15px;">총&nbsp;${count}건  ${currentPage}페이지</td>
						</tr>
					</table>
					<form action="productBuy.do" name="search" method="get" onsubmit="return seachCheck()">
						<table width="700px;" align="center">
							<tr id="productList">
								<td align="center" width="100%">
								<select name="keyField" class = "pl">
										<option value="product_name">상품명</option>
										<option value="order_state">배송 상태</option>
								</select> 
								&nbsp;&nbsp;
								<input type="text" size="16" name="keyWord" class = "pl2">&nbsp;&nbsp;
								<input type="submit" value="검색" style="font-family: Gulim; font-size: 12px;">
								</td>
							</tr>
						</table>
					</form>
					<br/><br/>
					<!-- 상품 테이블 -->
					<table id = "productTable"style = "width: auto;">
					    <tr style ="background-color: #6d99f2; height: 30px;">
					        <th>날짜</th>
					        <th>상품 정보</th>
					        <th>상태</th>
					        <th>확인/신청</th>
					    </tr>
					    <c:if test="${not empty orderProducts}">
					    <c:forEach items="${orderProducts}" var="orderProduct" varStatus="status">
					        <c:if test="${status.index == 0 || orderProduct.order_num != orderProducts[status.index - 1].order_num}">
					            <tr>
					                <td rowspan="${orderNumCount[orderProduct.order_num]}">
					                    <fmt:formatDate value="${orderProduct.order_date}" pattern="yyyy년 MM월 dd일"/>
					                </td>
					                <td>
							<a href="${contextPath}/member/orderDetail.do?order_num=${orderProduct.order_num}">
					                    <img src="${contextPath}/image/uniform2/${orderProduct.images2}"  alt="${orderProduct.product_name}" width="100"><br>
					                    ${orderProduct.product_name}
							</a>
					                </td>
					                <td>${orderProduct.order_state}</td>
					                <td>
					                    <c:if test="${orderProduct.order_state ne '배송 완료'}">
					                    <button onclick="refundOrder(${orderProduct.orderProduct_num})" id="refundButton${orderProduct.orderProduct_num}" style = "background-color: white;" <c:if test="${orderProduct.refund eq 'Y'}">disabled</c:if>><strong>환불요청</strong></button>
					                    </c:if>
					                    <c:if test="${orderProduct.order_state eq '배송 완료'}">
					                        <button onclick="confirmOrder(${orderProduct.orderProduct_num})" id="confirmButton${orderProduct.orderProduct_num}" style = "background-color: #AAB9FF;" <c:if test="${orderProduct.buyComplete eq 'Y'}">disabled</c:if>><strong>수취확인</strong></button>
					                    </c:if>
					                </td>
					            </tr>
					        </c:if>
					        <c:if test="${status.index != 0 && orderProduct.order_num == orderProducts[status.index - 1].order_num}">
					            <tr>
					            	<td></td>
					                <td>
							<a href="${contextPath}/member/orderDetail.do?order_num=${orderProduct.order_num}">
					                    <img src="${contextPath}/image/uniform2/${orderProduct.images2}"  alt="${orderProduct.product_name}" width="100"><br>
					                    ${orderProduct.product_name}
							</a>
					                </td>
					                <td>${orderProduct.order_state}</td>
					                <td>
					                	<c:if test="${orderProduct.order_state ne '배송 완료'}">
					                    <button onclick="refundOrder(${orderProduct.orderProduct_num})" id="refundButton${orderProduct.orderProduct_num}" style = "background-color: white;" <c:if test="${orderProduct.refund eq 'Y'}">disabled</c:if>><strong>환불요청</strong></button>
					                    </c:if>
					                    <c:if test="${orderProduct.order_state eq '배송 완료'}">
					                        <button onclick="confirmOrder(${orderProduct.orderProduct_num})" id="confirmButton${orderProduct.orderProduct_num}" style = "background-color: #AAB9FF;" <c:if test="${orderProduct.buyComplete eq 'Y'}">disabled</c:if>><strong>수취확인</strong></button>
					                    </c:if>
					                </td>
					            </tr>
					        </c:if>
					    </c:forEach>
					    </c:if>
					    <c:if test="${empty orderProducts}">
						    <td colspan="7" style="text-align: center;">
						        <c:choose>
						            <c:when test="${not empty keyWord}">
						                <h5><strong>검색어 '${keyWord}'에 해당하는 정보가 없습니다.</strong></h5>
						            </c:when>
						            <c:otherwise>
						                <h5><strong>구매 및 주문하신 목록이 없습니다.</strong></h5>
						            </c:otherwise>
						        </c:choose>
						    </td>
						</c:if>
					</table>
					
					<br/><br/>
					<table align="center" style = "font-size: 18px;">
						<c:if test="${count > 0}">
							<tr>
								<td align="center" style = "font-size: 18px;">${pagingHtml}</td>
							</tr>
						</c:if>
					</table>
					<br/>
					<div class = "form-group" id="myPageBtn">
						<button type = "button" class = "btn btn-light mypageBtn" onclick="location.href='${contextPath}/member/main.do'">Home</button>
					</div>		
			</div>
		</div>
</body>
<script>
	// 수취확인
    function confirmOrder(orderProduct_num) {
		
    	if (confirm('주문하신 상품을 받으셨습니까? 구매확정 관련해서 잘 받으셨으면 [예] 아니면 [아니오]를 눌러주세요')) {

        $.ajax({
            url: '${contextPath}/member/buyConfirm.do',
            type: 'POST',
            data: { orderProduct_num: orderProduct_num },
            success: function(response) {
                // 요청이 성공한 경우 버튼 상태를 변경합니다.
                alert('구매확정이 완료되었습니다.');
                $('#confirmButton' + orderProduct_num).prop('disabled', true);
                $('#refundButton' + orderProduct_num).css('background-color', 'gray');
            }
        });
    	} else {
            alert('구매확정을 취소하셨습니다.');
        }
    }
    // 환불요청
    function refundOrder(orderProduct_num) {
    	
	    if (confirm('환불 요청을 하시겠습니까?')) {
	        
	        $.ajax({
	            url: '${contextPath}/member/refundConfirm.do',
	            type: 'POST',
	            data: { orderProduct_num: orderProduct_num, order_state: '환불 요청' },
	            success: function(response) {
	            	console.log(response);
	                // 요청이 성공한 경우 버튼 상태를 변경합니다.
	                alert('환불 요청이 완료되었습니다.');
	                $('#refundButton' + orderProduct_num).prop('disabled', true);
	                $('#refundButton' + orderProduct_num).css('background-color', 'gray');
	                location.reload();
	            }
	        });
	    } else {
	        alert('환불 요청을 취소하셨습니다.');
	    }
	}
</script>
</html>