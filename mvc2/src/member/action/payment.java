package member.action;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import member.model.CartVO;
import member.model.MemberVO;
import member.model.OrderProductVO;
import member.model.OrderVO;
import member.model.ProductOptionVO;
import member.model.memberDAO;

public class payment implements CommandAction {

	@Override
	public String requestPro(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		
		memberDAO dao = memberDAO.getInstance();
		
		System.out.println("결제 처리 입니다");
		
		HttpSession session1 = request.getSession();
	    MemberVO membervo = (MemberVO) session1.getAttribute("member");
	    if (membervo == null) {
	        // 로그인하지 않은 경우 alert을 띄우고 로그인 페이지로 리다이렉트합니다.
	        response.setContentType("text/html; charset=UTF-8");
	        PrintWriter out = response.getWriter();
	        out.println("<script>alert('로그인이 필요합니다.'); location.href='/mvc2/member/loginForm.do';</script>");
	        out.flush();
	        return null;
	    }
		
	    // OrderVO 객체를 생성합니다.
	    OrderVO orderVO = new OrderVO();
	    // HttpSession 객체에서 member 속성을 가져옵니다.
	    HttpSession session = request.getSession();
	    MemberVO memberVO = (MemberVO) session.getAttribute("member");
	   
	    // memberVO 객체에서 userID와 phone 정보를 가져옵니다.
	    orderVO.setUserID(memberVO.getUserID());
	    orderVO.setPhone(memberVO.getPhone());
	    
	    System.out.println("userID: " + memberVO.getUserID());
	    System.out.println("phone: " + memberVO.getPhone());
		
	    orderVO.setOrder_phone(request.getParameter("order_phone"));
	    orderVO.setOrder_name(request.getParameter("order_name"));
	    orderVO.setOrder_address1(request.getParameter("order_address1"));
	    orderVO.setOrder_address2(request.getParameter("order_address2"));
	    orderVO.setPostnum(request.getParameter("postnum"));
	    orderVO.setOrder_memo(request.getParameter("order_memo"));
	    orderVO.setPaymethod(request.getParameter("paymethod"));
	    orderVO.setTotalbill(Integer.parseInt(request.getParameter("totalbill")));
	    
	    // 데이터베이스에 OrderVO 객체를 저장합니다.
	    dao.insertOrder(orderVO);
	    
	    System.out.println("order_phone: " + request.getParameter("order_phone"));
	    System.out.println("order_name: " + request.getParameter("order_name"));
	    System.out.println("order_address1: " + request.getParameter("order_address1"));
	    System.out.println("order_address2: " + request.getParameter("order_address2"));
	    System.out.println("postnum: " + request.getParameter("postnum"));
	    System.out.println("order_memo: " + request.getParameter("order_memo"));
	    System.out.println("paymethod: " + request.getParameter("paymethod"));
	    System.out.println("totalbill: " + request.getParameter("totalbill"));
	    
	    // 생성된 order_num 값을 가져옵니다.
	    int orderNum = orderVO.getOrder_num();
		
	    System.out.println("orderNum: " +orderNum);
	    // ============================================================================
		// ================== 상품 구매 정보를 가져온다 ========================
		// OrderProductVO 객체의 배열을 생성합니다.
	    String[] productCodes = request.getParameterValues("product_code");
	    String[] productSizes = request.getParameterValues("product_size");
	    String[] quantities = request.getParameterValues("quantity");
	    String[] productNames = request.getParameterValues("product_name");
	    String[] productPrices = request.getParameterValues("product_price");
	    String[] deliveries = request.getParameterValues("delivery");
	    String[] images2 = request.getParameterValues("images2");
	    Date order_date = new Date();
	    
	    String[] orderstate = new String[productCodes.length];
	    Arrays.fill(orderstate, "배송 준비중");
	    
	    String[] buyComplete = new String[productCodes.length];
	    Arrays.fill(buyComplete, "N");
	    
	    String[] refund = new String[productCodes.length];
	    Arrays.fill(refund, "N");
	    
	    List<OrderProductVO> orderProducts = new ArrayList<>();
	    for (int i = 0; i < productCodes.length; i++) {
	        OrderProductVO orderProductVO = new OrderProductVO();
	        // OrderProductVO 객체의 값을 설정합니다.
	        orderProductVO.setOrder_num(orderNum); // order_num 값을 설정합니다.
	        orderProductVO.setUserID(memberVO.getUserID());
	        orderProductVO.setPhone(memberVO.getPhone());
	        orderProductVO.setProduct_code(Integer.parseInt(productCodes[i]));
	        orderProductVO.setProduct_size(productSizes[i]);
	        orderProductVO.setQuantity(Integer.parseInt(quantities[i]));
	        orderProductVO.setProduct_name(productNames[i]);
	        orderProductVO.setProduct_price(Integer.parseInt(productPrices[i]));
	        orderProductVO.setDelivery(deliveries[i]);
	        orderProductVO.setOrder_date(order_date);
	        orderProductVO.setOrder_state(orderstate[i]);
	        orderProductVO.setBuyComplete(buyComplete[i]);
	        orderProductVO.setRefund(refund[i]);
	        orderProductVO.setImages2(images2[i]);
	        
		    // 데이터베이스에 OrderProductVO 객체를 저장합니다.
		    dao.insertOrderProduct(orderProductVO);
		    orderProducts.add(orderProductVO);

	    }
	    
	    
	    // db에 해당 장바구니 항목 삭제 만들기 
	    if (!request.getParameter("fromCart").equals("false")) {
	        String[] cartNums = request.getParameterValues("cartNum");
	        for (int i = 0; i < cartNums.length; i++) {
	            int cartnum = Integer.parseInt(cartNums[i]);
	            System.out.println("cartnum : " + cartnum);
	            System.out.println("fromCart : " + request.getParameter("fromCart"));
	            CartVO cartVO = new CartVO();
	            cartVO.setUserID(memberVO.getUserID());
	            cartVO.setCartNum(cartnum);
	            int result = dao.deleteCart(cartVO);
	        }
	    }
	    
	    // productCode int로 변경 확인 
	    String[] productCode = request.getParameterValues("product_code");
	    String[] productSize = request.getParameterValues("product_size");
	    String[] productquantities = request.getParameterValues("quantity");

	    for (int i = 0; i < productCodes.length; i++) {
	        // 데이터베이스에서 product_code와 product_size에 해당하는 ProductOptionVO 객체를 가져옵니다.
	        List<ProductOptionVO> currentProductOptionVOs = dao.getproductOption2(productCode[i], productSize[i]);
	        
	        for (ProductOptionVO currentProductOptionVO : currentProductOptionVOs) {
	            if (currentProductOptionVO.getProduct_size().equals(productSize[i])) {
	            	System.out.println("Before: " + currentProductOptionVO.getQuantity());
	            	// 결제된 수량만큼 차감합니다.
	                currentProductOptionVO.setQuantity(currentProductOptionVO.getQuantity() - Integer.parseInt(productquantities[i]));
	                System.out.println("After: " + currentProductOptionVO.getQuantity());
	                // 데이터베이스에 업데이트합니다.
	                dao.updateQuantity(currentProductOptionVO);
	            }
	        }
	    }
	    
	    request.setAttribute("orderNum", orderNum);
        request.setAttribute("orderVO", orderVO);
        request.setAttribute("orderProducts", orderProducts);
        
		return "/member/paymentResult.jsp";
	}

}
