package member.action;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import member.model.memberDAO;

public class memberDel implements CommandAction {

	@Override
	public String requestPro(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		
		memberDAO dao = memberDAO.getInstance();
		System.out.println("Ε»Επ Γ³Έ®");

		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();

		String userID = request.getParameter("userID");
		HttpSession session = request.getSession();
		session.removeAttribute("member");
		session.removeAttribute("isLogOn");
		int result = dao.memberDel(userID);
		if (result > 0) {
			out.print("success");
		} else {
			out.print("fail");
		}
		out.flush();
		return null;
	}

}
