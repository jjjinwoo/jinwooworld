package member.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import member.model.ProductOptionVO;
import member.model.ProductVO;
import member.model.memberDAO;

public class productDetail implements CommandAction {

	@Override
	public String requestPro(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		
		System.out.println("상품 상세페이지");

        String product_code = request.getParameter("product_code");
        System.out.println("product_code : " + product_code);
        
        memberDAO dao = memberDAO.getInstance();
        ProductVO productVO = dao.productDetail(product_code);

        List<ProductOptionVO> productOptionVO = dao.getproductOption(product_code);

        request.setAttribute("product", productVO);
        request.setAttribute("productOptionVO", productOptionVO);

        return "/member/productDetail.jsp";
    }

}
