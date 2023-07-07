package member.action;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import member.model.CartVO;
import member.model.MemberVO;
import member.model.memberDAO;

public class deleteAll implements CommandAction {

	@Override
	public String requestPro(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		
		memberDAO dao = memberDAO.getInstance();
		
		System.out.println("장바구니 전체 삭제");

        CartVO cartVO = new CartVO();
        HttpSession session = request.getSession();
        MemberVO memberVO = (MemberVO) session.getAttribute("member");

        cartVO.setUserID(memberVO.getUserID());
        cartVO.setPhone(memberVO.getPhone());
        
        String product_code = request.getParameter("product_code");
        if (product_code != null) {
            cartVO.setProduct_code(Integer.parseInt(product_code));
        }
        cartVO.setProduct_size(request.getParameter("product_size"));

        String quantity = request.getParameter("quantity");
        if (quantity != null) {
            cartVO.setQuantity(Integer.parseInt(quantity));
        }

        cartVO.setProduct_name(request.getParameter("product_name"));

        String product_price = request.getParameter("product_price");
        if (product_price != null) {
            cartVO.setProduct_price(Integer.parseInt(product_price));
        }

        cartVO.setDelivery(request.getParameter("delivery"));

        dao.deleteAll(memberVO.getUserID());
       
        // return "success";
        String result = "success"; // 처리 결과

        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println(result);
        out.flush();
        return null;
    }
	
}
