package member.action;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import member.model.MemberVO;
import member.model.OrderProductVO;
import member.model.Paging;
import member.model.memberDAO;

public class productBuy implements CommandAction {
	
	private int pageSize1 = 8;
	private int blockCount1 = 10;
	
	@Override
	public String requestPro(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		
		memberDAO dao = memberDAO.getInstance();
		 
		String keyField = request.getParameter("keyField") == null ? "" : request.getParameter("keyField");
        String keyWord = request.getParameter("keyWord") == null ? "" : request.getParameter("keyWord");
        int currentPage = request.getParameter("pageNum") == null ? 1 : Integer.parseInt(request.getParameter("pageNum"));
		
		System.out.println("주문 내역");
		
		HttpSession session1 = request.getSession();
	    MemberVO membervo = (MemberVO) session1.getAttribute("member");
	    if (membervo == null) {
	        // 로그인하지 않은 경우 alert을 띄우고 로그인 페이지로 리다이렉트합니다.
	        response.setContentType("text/html; charset=UTF-8");
	        PrintWriter out = response.getWriter();
	        out.println("<script>alert('로그인이 필요합니다.'); location.href='/mvc2/member/loginForm.do';</script>");
	        out.flush();
	        return null;
	    }
		
	    String pagingHtml = "";
	    HashMap<String, Object> map = new HashMap<>();
	    map.put("keyField", keyField);
	    map.put("keyWord", keyWord);
	    map.put("userID", membervo.getUserID());
	    int count = dao.getProductCount2(map);
		
	    Paging page = new Paging(keyField, keyWord, currentPage, count, this.pageSize1, this.blockCount1, "productBuy.do");
	    pagingHtml = page.getPagingHtml().toString();
	    map.put("start", Integer.valueOf(page.getStartCount()));
	    map.put("end", Integer.valueOf(page.getEndCount()));
	    map.put("userID", membervo.getUserID());
	    
	    List<OrderProductVO> orderProducts = dao.getBuyProduct(map);
	    
	    request.setAttribute("count", Integer.valueOf(count));
	    request.setAttribute("currentPage", Integer.valueOf(currentPage));
	    request.setAttribute("pagingHtml", pagingHtml);
	    request.setAttribute("orderProducts", orderProducts);
	    request.setAttribute("keyWord", keyWord);
	    
	    
		return "/member/productBuy.jsp";
	}

}
