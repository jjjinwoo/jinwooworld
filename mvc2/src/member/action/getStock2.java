package member.action;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import member.model.ProductOptionVO;
import member.model.memberDAO;

public class getStock2 implements CommandAction {

	@Override
	public String requestPro(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		
		//response.setContentType("application/json");
        memberDAO dao = memberDAO.getInstance();

        // 상품 정보를 가져옵니다.
        String[] productCodes = request.getParameterValues("product_code");
        String[] productSizes = request.getParameterValues("product_size");
        String[] quantities = request.getParameterValues("quantity");

        List<Map<String, Object>> outOfStockProducts = new ArrayList<>();
        // productOptionVO를 가져와서 재고량을 확인합니다.
        System.out.println("productCodes.length : " + productCodes.length);

        for (int i = 0; i < productCodes.length; i++) {
            ProductOptionVO productOptionVO = dao.getProductOption4(productCodes[i], productSizes[i]);

            System.out.println("productOptionVO.getQuantity() : " + productOptionVO.getQuantity());

            if (productOptionVO.getQuantity() <= 0) {
                System.out.println("재고량이 0인 상품: " + productCodes[i] + ", " + productSizes[i] + ", " + quantities[i]);
                Map<String, Object> outOfStockProduct = new HashMap<>();
                outOfStockProduct.put("productCode", productCodes[i]);
                outOfStockProduct.put("productSize", productSizes[i]);
                outOfStockProduct.put("quantity", quantities[i]);
                outOfStockProducts.add(outOfStockProduct);
                /*
                response.setContentType("text/html; charset=UTF-8");
                PrintWriter out = response.getWriter();
                out.println("out of stock");
                out.close();
                return null;
                */
            }
        }

        if (outOfStockProducts.size() > 0) {
            // 재고량이 부족한 상품이 있는 경우
        	/*
            Map<String, Object> result = new HashMap<>();
            result.put("result", "out of stock");
            result.put("outOfStockProducts", outOfStockProducts);
            return result;
            */
        	 response.setContentType("application/json");
             PrintWriter out = response.getWriter();
             out.println("{\"result\":\"out of stock\"}");
             out.close();
             return null;
        } else {
            // 재고량이 부족한 상품이 없는 경우
        	response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.println("{\"result\":\"success\"}");
            out.close();
            return null;
        }
        
    }

}
