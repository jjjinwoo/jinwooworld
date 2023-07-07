package member.action;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import member.model.MemberVO;
import member.model.memberDAO;

public class deletePart implements CommandAction {

	@Override
	public String requestPro(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		
		memberDAO dao = memberDAO.getInstance();
		
		System.out.println("장바구니 부분 삭제");
	    
	    String[] cartNums = request.getParameterValues("cartNum");
	    
	    HttpSession session = request.getSession();
	    MemberVO memberVO = (MemberVO) session.getAttribute("member");
	    String userID = memberVO.getUserID();
	    
	    for (String cartNum : cartNums) {
	        int cartNumInt = Integer.parseInt(cartNum);
	        dao.deletePart(userID, cartNumInt);
	    }
	    
	    String result = "success"; // 처리 결과

        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println(result);
        out.flush();
        return null;
	    //return "success";
	}

}
