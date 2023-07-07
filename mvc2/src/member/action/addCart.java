package member.action;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import member.model.CartVO;
import member.model.MemberVO;
import member.model.ProductOptionVO;
import member.model.memberDAO;

public class addCart implements CommandAction {

	@Override
	public String requestPro(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		
		memberDAO dao = memberDAO.getInstance();
        
        System.out.println("장바구니 추가");
        CartVO cartVO = new CartVO();
        // HttpSession 객체에서 member 속성을 가져옵니다.
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
        // memberVO 객체에서 userID와 phone 정보를 가져옵니다.
        cartVO.setUserID(memberVO.getUserID());
        cartVO.setPhone(memberVO.getPhone());
        System.out.println(memberVO.getUserID());
        
        int product_code = Integer.parseInt(request.getParameter("product_code"));
        String product_size = request.getParameter("product_size");
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        
        System.out.println("product_code : " + product_code);
        System.out.println("product_size : " + product_size);
        System.out.println("quantity : " + quantity);
       
        // productOptionVO를 가져와서 재고량을 확인합니다.
        ProductOptionVO productOptionVO = dao.getProductOption3(product_code, product_size);
        System.out.println("productOptionVO.getQuantity() : " + productOptionVO.getQuantity());
        System.out.println("quantity : " + quantity);
        
        if (productOptionVO.getQuantity() < quantity) {
            // 재고량이 부족한 경우 "out of stock"을 반환합니다.
        	System.out.println("재고 부족");
        	response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("out of stock");
            out.close();
            return null;
        }

        // 장바구니에 동일한 상품 코드와 동일한 상품 크기의 상품이 있는지 확인합니다.
        CartVO existingCartVO = dao.getCart(memberVO.getUserID(), product_code, product_size);
        System.out.println("existingCartVO : " + existingCartVO.getCartNum());
        System.out.println("userID : " + existingCartVO.getUserID());
        if (existingCartVO.getUserID() != null) {
        	System.out.println("장바구니 이미 존재 ");
            // 이미 존재하는 경우 수량을 업데이트합니다.
            existingCartVO.setQuantity(existingCartVO.getQuantity() + quantity);
            dao.updateCart2(existingCartVO);
        } else {
            // 존재하지 않는 경우 새로운 상품을 추가합니다.
        	System.out.println("장바구니 존재 x");
            cartVO.setProduct_code(product_code);
            cartVO.setProduct_size(product_size);
            cartVO.setQuantity(quantity);
            cartVO.setProduct_name(request.getParameter("product_name"));
            cartVO.setProduct_price(Integer.parseInt(request.getParameter("product_price")));
            cartVO.setDelivery(request.getParameter("delivery"));
            cartVO.setImages2(request.getParameter("images2"));
            System.out.println("product_name : " + request.getParameter("product_name"));
            System.out.println("images : " + request.getParameter("images2"));
            dao.addCart(cartVO);
        }

        // 정상적으로 처리된 경우 "success"를 반환합니다.
        return "success";
    }

}
