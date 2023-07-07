package member.action;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import member.model.Paging;
import member.model.ProductVO;
import member.model.memberDAO;

public class ListAction implements CommandAction {

	    private int pageSize = 6;
	    private int blockCount = 10;

	    @Override
	    public String requestPro(HttpServletRequest request, HttpServletResponse response) throws Throwable {
	        
	    	System.out.println("메인 화면 불러오기 ");

	        String keyField = request.getParameter("keyField") == null ? "" : request.getParameter("keyField");
	        String keyWord = request.getParameter("keyWord") == null ? "" : request.getParameter("keyWord");
	        int currentPage = request.getParameter("pageNum") == null ? 1 : Integer.parseInt(request.getParameter("pageNum"));

	        String pagingHtml = "";
	        HashMap<String, Object> map = new HashMap<>();
	        map.put("keyField", keyField);
	        map.put("keyWord", keyWord);
	        
	        memberDAO dao = memberDAO.getInstance();
	        
	        int count = dao.getProductCount(map);
	        Paging page = new Paging(keyField, keyWord, currentPage, count,
	                this.pageSize, this.blockCount, "main.do");
	        pagingHtml = page.getPagingHtml().toString();

	        map.put("start", Integer.valueOf(page.getStartCount()));
	        map.put("end", Integer.valueOf(page.getEndCount()));

	        List<ProductVO> products = dao.getProducts(map);
	        
	        for(ProductVO productVO : products) {
	        	System.out.println(productVO.getProduct_code());
	        	System.out.println(productVO.getProduct_name());
	        	System.out.println(productVO.getImages1());
	        }
	        
	        System.out.println("products.size() : " + products.size());
	        request.setAttribute("count", Integer.valueOf(count));
	        request.setAttribute("currentPage", Integer.valueOf(currentPage));
	        request.setAttribute("products", products);
	        request.setAttribute("pagingHtml", pagingHtml);
	        request.setAttribute("keyWord", keyWord);
	        
	        
	        return "/member/main.jsp";
	    }
	    
	    
	    

}
