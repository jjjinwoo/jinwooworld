<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt"	uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var = "contextPath" value = "${pageContext.request.contextPath }"/>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>메인</title>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap-theme.min.css">
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>	
</head>
<style>
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
</style>
<body>
	<br/>
	<jsp:include page ="../common/topMenu.jsp"></jsp:include>
	<br/>
	<table width="500" align = "right" style = "margin-right: 125px;">
		<tr>
			<td align="right" style="font-family: Gulim; font-size: 15px;">총&nbsp;${count}건  ${currentPage}페이지</td>
		</tr>
	</table>
	<br/><br/>
	<form action="${contextPath}/member/main.do" name="search" method="post" onsubmit="return seachCheck()">
		<table width="500" align="center">
			<tr>
				<td align="center" width="90%">
				<select name="keyField" class = "pl">
						<option value="product_name">상품명</option>
				</select> 
				&nbsp;&nbsp;
				<input type="text" size="16" name="keyWord" class = "pl2">&nbsp;&nbsp;
				<input type="submit" value="검색" style="font-family: Gulim; font-size: 12px;">
				</td>
			</tr>
		</table>
	</form>
	<br/><br/>
	<c:if test="${count > 0}">
	<table align="center">
	    <c:forEach var="product" items="${products}" varStatus="status">
	        <c:if test="${status.index % 3 == 0}">
	            <tr class="table">
	        </c:if>
	        <td style = "width: 400px; height: 400px;">
			    <a href="${contextPath}/member/productDetail.do?product_code=${product.product_code}">
			    	<%-- <img src="${contextPath}/member/displayImage.do?product_code=${product.product_code}" alt="${product.product_name}" width="300"> --%>
			    	<img src="${contextPath}/image/uniform2/${product.images2}"  alt="${product.product_name}" width="300">
			    </a>
			    <br/>
			    <div><a href="${contextPath}/member/productDetail.do?product_code=${product.product_code}" style = "color: black"><strong>상 품 타 입 : ${product.product_type}</strong></a></div>
			    <div><a href="${contextPath}/member/productDetail.do?product_code=${product.product_code}" style = "color: black"><strong>상 품 명 : ${product.product_name}</strong></a></div>
			    <div><a href="${contextPath}/member/productDetail.do?product_code=${product.product_code}" style = "color: black"><strong>가 격 : <fmt:formatNumber value="${product.product_price}" pattern="#,###원"/></strong></a></div>
			</td>
	        <c:if test="${status.index % 3 == 2}">
	            </tr>
	        </c:if>
	    </c:forEach>
	</table>
	<br/>
	<table align="center" style = "font-size: 18px;">
		<c:if test="${count > 0}">
			<tr>
				<td align="center" style = "font-size: 18px;">${pagingHtml}</td>
			</tr>
		</c:if>
	</table>
	</c:if>
	<c:if test="${count == 0}">
		<div align="center">
	    <h2>검색어 &nbsp;&nbsp;'${keyWord}'&nbsp;에 해당하는 정보가 없습니다.</h2>
		</div>
	</c:if>
	<br/><br/>
</body>
</html>