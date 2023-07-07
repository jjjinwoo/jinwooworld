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
		
		System.out.println("���� ó�� �Դϴ�");
		
		HttpSession session1 = request.getSession();
	    MemberVO membervo = (MemberVO) session1.getAttribute("member");
	    if (membervo == null) {
	        // �α������� ���� ��� alert�� ���� �α��� �������� �����̷�Ʈ�մϴ�.
	        response.setContentType("text/html; charset=UTF-8");
	        PrintWriter out = response.getWriter();
	        out.println("<script>alert('�α����� �ʿ��մϴ�.'); location.href='/mvc2/member/loginForm.do';</script>");
	        out.flush();
	        return null;
	    }
		
	    // OrderVO ��ü�� �����մϴ�.
	    OrderVO orderVO = new OrderVO();
	    // HttpSession ��ü���� member �Ӽ��� �����ɴϴ�.
	    HttpSession session = request.getSession();
	    MemberVO memberVO = (MemberVO) session.getAttribute("member");
	   
	    // memberVO ��ü���� userID�� phone ������ �����ɴϴ�.
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
	    
	    // �����ͺ��̽��� OrderVO ��ü�� �����մϴ�.
	    dao.insertOrder(orderVO);
	    
	    System.out.println("order_phone: " + request.getParameter("order_phone"));
	    System.out.println("order_name: " + request.getParameter("order_name"));
	    System.out.println("order_address1: " + request.getParameter("order_address1"));
	    System.out.println("order_address2: " + request.getParameter("order_address2"));
	    System.out.println("postnum: " + request.getParameter("postnum"));
	    System.out.println("order_memo: " + request.getParameter("order_memo"));
	    System.out.println("paymethod: " + request.getParameter("paymethod"));
	    System.out.println("totalbill: " + request.getParameter("totalbill"));
	    
	    // ������ order_num ���� �����ɴϴ�.
	    int orderNum = orderVO.getOrder_num();
		
	    System.out.println("orderNum: " +orderNum);
	    // ============================================================================
		// ================== ��ǰ ���� ������ �����´� ========================
		// OrderProductVO ��ü�� �迭�� �����մϴ�.
	    String[] productCodes = request.getParameterValues("product_code");
	    String[] productSizes = request.getParameterValues("product_size");
	    String[] quantities = request.getParameterValues("quantity");
	    String[] productNames = request.getParameterValues("product_name");
	    String[] productPrices = request.getParameterValues("product_price");
	    String[] deliveries = request.getParameterValues("delivery");
	    String[] images2 = request.getParameterValues("images2");
	    Date order_date = new Date();
	    
	    String[] orderstate = new String[productCodes.length];
	    Arrays.fill(orderstate, "��� �غ���");
	    
	    String[] buyComplete = new String[productCodes.length];
	    Arrays.fill(buyComplete, "N");
	    
	    String[] refund = new String[productCodes.length];
	    Arrays.fill(refund, "N");
	    
	    List<OrderProductVO> orderProducts = new ArrayList<>();
	    for (int i = 0; i < productCodes.length; i++) {
	        OrderProductVO orderProductVO = new OrderProductVO();
	        // OrderProductVO ��ü�� ���� �����մϴ�.
	        orderProductVO.setOrder_num(orderNum); // order_num ���� �����մϴ�.
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
	        
		    // �����ͺ��̽��� OrderProductVO ��ü�� �����մϴ�.
		    dao.insertOrderProduct(orderProductVO);
		    orderProducts.add(orderProductVO);

	    }
	    
	    
	    // db�� �ش� ��ٱ��� �׸� ���� ����� 
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
	    
	    // productCode int�� ���� Ȯ�� 
	    String[] productCode = request.getParameterValues("product_code");
	    String[] productSize = request.getParameterValues("product_size");
	    String[] productquantities = request.getParameterValues("quantity");

	    for (int i = 0; i < productCodes.length; i++) {
	        // �����ͺ��̽����� product_code�� product_size�� �ش��ϴ� ProductOptionVO ��ü�� �����ɴϴ�.
	        List<ProductOptionVO> currentProductOptionVOs = dao.getproductOption2(productCode[i], productSize[i]);
	        
	        for (ProductOptionVO currentProductOptionVO : currentProductOptionVOs) {
	            if (currentProductOptionVO.getProduct_size().equals(productSize[i])) {
	            	System.out.println("Before: " + currentProductOptionVO.getQuantity());
	            	// ������ ������ŭ �����մϴ�.
	                currentProductOptionVO.setQuantity(currentProductOptionVO.getQuantity() - Integer.parseInt(productquantities[i]));
	                System.out.println("After: " + currentProductOptionVO.getQuantity());
	                // �����ͺ��̽��� ������Ʈ�մϴ�.
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
