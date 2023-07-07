package member.action;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import member.model.MemberVO;
import member.model.memberDAO;

public class orderDirect implements CommandAction {

	@Override
	public String requestPro(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		
		memberDAO dao = memberDAO.getInstance();
		
		System.out.println("바로 결제");
		
		 HttpSession session = request.getSession();
		 MemberVO memberVO = (MemberVO) session.getAttribute("member");
		 if (memberVO == null) {
		        // 로그인하지 않은 경우 alert을 띄우고 로그인 페이지로 리다이렉트합니다.
		     response.setContentType("text/html; charset=UTF-8");
		     PrintWriter out = response.getWriter();
		     out.println("<script>alert('로그인이 필요합니다.'); location.href='/mvc2/member/loginForm.do';</script>");
		     out.flush();
		     return null;
		 }
		
		 // 상품 정보를 가져옵니다.
		 String product_name = request.getParameter("product_name");
		 String product_price = request.getParameter("product_price");
		 int product_code = Integer.parseInt(request.getParameter("product_code"));
		 String product_size = request.getParameter("product_size");
		 int quantity = Integer.parseInt(request.getParameter("quantity"));
		 String delivery = "무료배송";
		 String images2 = request.getParameter("images2");
		 
		 System.out.println("imgaes2 : " + images2);
		 
		 // 상품 정보와 배송 정보, 장바구니 목록을 orderForm.jsp로 전달합니다.
	     Map<String, Object> cart = new HashMap<>();
	     cart.put("product_code", product_code);
	     cart.put("product_name", product_name);
	     cart.put("product_price", Integer.parseInt(product_price));
	     cart.put("quantity", quantity);
	     cart.put("product_size", product_size);
	     cart.put("delivery", delivery);
	     cart.put("images2", images2);

	     request.setAttribute("cartList", Arrays.asList(cart));

	     request.setAttribute("fromCart", false);		 
		
		 return "/member/orderForm.jsp";
	}

}
