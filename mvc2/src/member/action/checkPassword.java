package member.action;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import member.model.MemberVO;
import member.model.memberDAO;

public class checkPassword implements CommandAction {

	@Override
	public String requestPro(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		System.out.println("��й�ȣ ��ȸ");

		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();

		String inputPwd = request.getParameter("pwd");
		System.out.println("�Է��� ��й�ȣ : " + inputPwd);
		String userID = request.getParameter("userID");
		System.out.println(userID);

		memberDAO dao = memberDAO.getInstance();
		MemberVO member = dao.getMember(userID);
		String dbPwd = member.getPwd();
		System.out.println("db���� ��й�ȣ : " + dbPwd);

		if (inputPwd.trim().equals(dbPwd.trim())) {
			out.print("success");
		} else {
			out.print("fail");
		}
		out.flush();
		return null;
	}
}
