package member.action;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest; 
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import member.model.MemberVO;

public class checkLogin implements CommandAction {

	@Override
	public String requestPro(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		HttpSession session = request.getSession();
		MemberVO memberVO = (MemberVO) session.getAttribute("member");
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		if (memberVO == null) {
	        // 로그인하지 않은 경우 "not logged in" 문자열을 반환합니다.
			System.out.println("not logged in");
	        out.println("not logged in");
	        //return "not logged in";
	    } else {
	        // 로그인한 경우 "logged in" 문자열을 반환합니다.
	    	System.out.println("logged in");
	        out.println("logged in");
	    	//return "logged in";
	    } 
		out.close();
		return null;
	}

}
