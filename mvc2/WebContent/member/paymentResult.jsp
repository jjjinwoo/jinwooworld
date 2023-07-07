<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt"	uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var = "contextPath" value = "${pageContext.request.contextPath }"/>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>결제 완료</title>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap-theme.min.css">
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>	
</head>
<style>
.ordersub {
    display: inline-block;
	}
#orderSumaryTable td:first-child {
    width: 100px;
}
#orderSumaryTable {
    border-collapse: collapse;
}

#orderSumaryTable td {
    border: none;
    padding: 8px;
}

#orderSumaryTable tr:hover {
    background-color: #D7F1FA;
}

#orderSumaryTable th {
    padding-top: 12px;
    padding-bottom: 12px;
    text-align: left;
    background-color: #4CAF50;
    color: white;
}
#orderDetail {
    display: flex;
    justify-content: center;
}

#orderSumaryDiv {
    margin-left: 20px;
}
</style>
<body>
	<br/>
	<jsp:include page ="../common/topMenu.jsp"></jsp:include>
	<br/>
	
	<div class="container" id="cartListbox"> 
		<div class="ordersub" style="margin-right:300px; position:relative; bottom:0; width:200px;">
		</div>	
		<div class="ordersub" style="border-radius:50px; padding:15px; margin-bottom:20px; margin-right:0; ">
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
		<div class="ordersub" style="border-radius:50px; padding:15px; margin-bottom:20px; margin-right:0; background-color: #97BBEB;">
			<span class="glyphicon glyphicon-ok" style="font-size:3em;"></span>
		</div>
		<div class="ordersub">
			<h6>STEP3</h6>
			<h3>결제/주문완료</h3>
		</div>
	</div>
	
	<br/>
	<div class = "container">
				<div class="row" id="orderDetail">
					  <!-- 회원 배송 정보 -->
					  <div class="col-sm-7" id="deliveryDetail">
					    <table style = " border-collapse: separate; border-spacing: 0 20px;">
					      <tr>
					        <th colspan="3">
					          <h4><strong>주문자 정보</strong></h4>
					          <br/>
					        </th>
					      </tr>
					      <tr>
					        <td><strong>주문 번호</strong></td>
					        <td colspan="2" style="padding-left: 15px;">${orderVO.order_num}</td>
					      </tr>
					      <tr>
						    <td><strong>배송 정보</strong></td>
						    <td style="vertical-align: top;">
						        <div style="margin-left: 15px;">
						            <p>수령인 이름</p>
						            <p>연락처</p>
						            <p>주소</p>
						            <p>&nbsp;</p>
						            <p>&nbsp;</p>
						            <p>배송 메모</p>
						        </div>
						    </td>
						    <td style="vertical-align: top;">
						        <div style="margin-left: 15px;">
						            <p>${orderVO.order_name}</p>
						            <p>${orderVO.order_phone}</p>
						            <p>${orderVO.postnum}</p>
						            <p>${orderVO.order_address1}</p>
						            <p>${orderVO.order_address2}</p>
						            <c:if test="${not empty orderVO.order_memo}">
						                <p>${orderVO.order_memo}</p>
						            </c:if>
						        </div>
						    </td>
						</tr>
					      <tr>
					        <td><strong>결제 정보</strong></td>
					        <td style="vertical-align: top;">
					          <div style="margin-left: 15px;">
					            <p>회원 아이디</p>
					            <p>결제 수단</p>
					          </div>
					        </td>
					        <td style="vertical-align: top;">
					          <div style="margin-left: 15px;"> 
					            <p>${member.userID}</p>
					            <p>${orderVO.paymethod}</p>
					            <p><fmt:formatDate value="${orderDTO.order_date}" pattern="yyyy.MM.dd a hh: mm: ss"/></p>
					          </div>
					        </td>
					      </tr>
					    </table>
					    <br/>
					  </div>
					
					
					<!-- 결제 요약 -->
					<div class="col-sm-10" id="orderSumaryDiv">
						<table id="orderListTable" style = "width:725px;">
							<thead>
								<tr>
							        <th colspan="3">
							          <h4><strong>제품 정보</strong></h4>
							          <br/>
							        </th>
						      	</tr>
								<tr style = "background-color: #6d99f2; color: white; height: 40px;">
									<th id="orderProductNameTh"><strong>상품 이름</strong></th>
									<th><strong>상품 가격</strong></th>
									<th><strong>주문 날짜</strong></th>
									<th><strong>배송 상태</strong></th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${orderProducts}" var="orderProducts">
									<tr id="cartListTbodyTr">
										<td class="orderProductNameTd">
											<c:choose>
												<c:when test="${orderProducts.product_name.length() > 15}">
													<a href="${contextPath}/member/productDetail.do?product_code=${orderProducts.product_code}">
													<!-- 
													<img src="${contextPath}/member/displayImage.do?product_code=${orderProducts.product_code}" alt="${orderProducts.product_name}" width="100">
													 -->
													<img src="${contextPath}/image/uniform2/${orderProducts.images2}"  alt="${orderProducts.product_name}" width="100">
														${orderProducts.product_name.substring(0, 16)} ⋯
													</a>
												</c:when>
												<c:otherwise>
													<a href="/product/productDetail?product_code=${orderProducts.product_code}">
														${orderProducts.product_name} 
													</a>	
												</c:otherwise>
											</c:choose>
											<div style="display: inline-block;">
												<p>&nbsp;&nbsp;옵션 : ${orderProducts.product_size} / 수량 : ${orderProducts.quantity}</p>
											</div>
										</td>
										<td><fmt:formatNumber value="${orderProducts.product_price}" pattern="#,###원"/></td>
										<td><fmt:formatDate  value="${orderProducts.order_date}" pattern="yyyy년 MM월 dd일"/></td>
										<td>${orderProducts.order_state}</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>	
						<table id="orderSumaryTable" style = "width: 250px; float: right;">
							<tr class="orderSumaryTr">
								<td style="width: 150px;"><strong>총 상품 금액</strong></td>
								<td class="orderPrice" id="orderSum"><fmt:formatNumber value="${orderVO.totalbill}" pattern="#,###원"/></td>
							</tr>

							<tr class="orderSumaryTr">
								<td style="width: 150px;"><strong>총 배송비</strong></td>
								<td class="orderPrice" id="deliveryFee">무료 배송</td>
							</tr>
							<tr class="orderSumaryTr" id="totalBill">
								<td style="width: 150px;"><strong>결제 금액</strong></td>
								<td class="orderPrice" id="totalBillTd"><fmt:formatNumber value="${orderVO.totalbill}" pattern="#,###원"/></td>
							</tr>
						</table>
						
					</div>	
				</div>	
			</div>	
					

			<div id="buttonDiv" style = "text-align: center;">
			    <a href="${contextPath}/member/main.do" class="btn btn-light" style = "border: 1px solid gray;">
			        <span class="glyphicon glyphicon-home"> Home</span>
			    </a>
			</div>
			
</body>
</html>