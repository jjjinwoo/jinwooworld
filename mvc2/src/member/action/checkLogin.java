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
	        // �α������� ���� ��� "not logged in" ���ڿ��� ��ȯ�մϴ�.
			System.out.println("not logged in");
	        out.println("not logged in");
	        //return "not logged in";
	    } else {
	        // �α����� ��� "logged in" ���ڿ��� ��ȯ�մϴ�.
	    	System.out.println("logged in");
	        out.println("logged in");
	    	//return "logged in";
	    } 
		out.close();
		return null;
	}

}
