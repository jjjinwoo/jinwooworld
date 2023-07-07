package member.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import member.model.CartVO;
import member.model.MemberVO;
import member.model.memberDAO;

public class orderForm implements CommandAction {

	@Override
	public String requestPro(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		
		memberDAO dao = memberDAO.getInstance();
		
		System.out.println("결제폼 입니다");
		
		HttpSession session = request.getSession();
	    MemberVO memberVO = (MemberVO) session.getAttribute("member");

	    String[] productCodes = request.getParameterValues("product_code");
	    String[] productSizes = request.getParameterValues("product_size");
	    String[] quantities = request.getParameterValues("quantity");
	    String[] productNames = request.getParameterValues("product_name");
	    String[] productPrices = request.getParameterValues("product_price");
	    String[] deliveries = request.getParameterValues("delivery");
	    String[] cartNums = request.getParameterValues("cartNum");
	    String[] images2 = request.getParameterValues("images2");
		
	    List<CartVO> cartList = new ArrayList<CartVO>();
	    
	    for (int i = 0; i < cartNums.length; i++) {
            
	    	CartVO cartVO = new CartVO();
            
            cartVO.setUserID(memberVO.getUserID());
            cartVO.setPhone(memberVO.getPhone());
            cartVO.setProduct_code(Integer.parseInt(productCodes[i]));
            cartVO.setProduct_size(productSizes[i]);
            cartVO.setQuantity(Integer.parseInt(quantities[i]));
            cartVO.setProduct_name(productNames[i]);
            cartVO.setProduct_price(Integer.parseInt(productPrices[i]));
            cartVO.setDelivery(deliveries[i]);
            cartVO.setCartNum(Integer.parseInt(cartNums[i]));
            cartVO.setImages2(images2[i]);
            
            System.out.println("product_code: " + productCodes[i]);
            System.out.println("product_size: " + productSizes[i]);
            System.out.println("quantity: " + quantities[i]);
            System.out.println("product_name: " + productNames[i]);
            System.out.println("product_price: " + productPrices[i]);
            System.out.println("cartNum: " + cartNums[i]);
            System.out.println("images2: " + images2[i]);
            
            dao.updateCart(cartVO);

            cartList.add(cartVO);
        }

        request.setAttribute("cartList", cartList);
	    
		return "/member/orderForm.jsp";
	}

}
