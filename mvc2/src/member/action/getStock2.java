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

        // ��ǰ ������ �����ɴϴ�.
        String[] productCodes = request.getParameterValues("product_code");
        String[] productSizes = request.getParameterValues("product_size");
        String[] quantities = request.getParameterValues("quantity");

        List<Map<String, Object>> outOfStockProducts = new ArrayList<>();
        // productOptionVO�� �����ͼ� ����� Ȯ���մϴ�.
        System.out.println("productCodes.length : " + productCodes.length);

        for (int i = 0; i < productCodes.length; i++) {
            ProductOptionVO productOptionVO = dao.getProductOption4(productCodes[i], productSizes[i]);

            System.out.println("productOptionVO.getQuantity() : " + productOptionVO.getQuantity());

            if (productOptionVO.getQuantity() <= 0) {
                System.out.println("����� 0�� ��ǰ: " + productCodes[i] + ", " + productSizes[i] + ", " + quantities[i]);
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
            // ����� ������ ��ǰ�� �ִ� ���
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
            // ����� ������ ��ǰ�� ���� ���
        	response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.println("{\"result\":\"success\"}");
            out.close();
            return null;
        }
        
    }

}
