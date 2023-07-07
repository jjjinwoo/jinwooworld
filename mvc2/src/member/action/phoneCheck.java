package member.action;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import member.model.MemberVO;
import member.model.memberDAO;

public class phoneCheck implements CommandAction {

	@Override
	public String requestPro(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		System.out.println("전화번호 중복 조회");

		String inputPhone = request.getParameter("phone");
		System.out.println(inputPhone);
		memberDAO dao = memberDAO.getInstance();
		MemberVO member = dao.getPhone(inputPhone);
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
		
	    return null;
	}

}
