package member.action;

import java.io.PrintWriter; 

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import member.model.MemberVO;
import member.model.memberDAO;

public class idCheck implements CommandAction {

	@Override
	public String requestPro(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		
		System.out.println("아이디 중복 조회");

		String inputUserID = request.getParameter("userID");
		System.out.println(inputUserID);
		memberDAO dao = memberDAO.getInstance();
		MemberVO member = dao.getUser(inputUserID);
		
		response.setContentType("text/html; charset=UTF-8");
	    PrintWriter out = response.getWriter();
	    
		if (member != null) {
			System.out.println("fail");
	        out.println("fail");
	      
		} else {
			System.out.println("success");
	        out.println("success");
	        
		}
		out.close();
		out.flush();
	    return null;
		
	}

}
