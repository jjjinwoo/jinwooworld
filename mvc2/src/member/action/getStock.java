package member.action;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import member.model.ProductOptionVO;
import member.model.memberDAO;

public class getStock implements CommandAction {

	@Override
	public String requestPro(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		 
		memberDAO dao = memberDAO.getInstance();
		
		 System.out.println("getStock 재고량 확인");
		 
		 int product_code = Integer.parseInt(request.getParameter("product_code"));
		 String product_size = request.getParameter("product_size");
		 int quantity = Integer.parseInt(request.getParameter("quantity"));
		 
		 System.out.println("product_code : " + product_code);
		 System.out.println("product_size : " + product_size);
		 System.out.println("quantity : " + quantity);
		 
		 ProductOptionVO productOptionVO = dao.getProductOption3(product_code, product_size);
		 System.out.println("productOptionVO.getQuantity() : " + productOptionVO.getQuantity());
		    if (productOptionVO.getQuantity() < quantity) {
		    	 
		    	System.out.println("현재 재고 : " + productOptionVO.getQuantity() + " : " + quantity);
		    	response.setContentType("text/html; charset=UTF-8");
	            PrintWriter out = response.getWriter();
	            
	            out.println("out of stock");
	            out.close();
	            return null;
		 }
		 
		 
		 return "success";
	}

}
