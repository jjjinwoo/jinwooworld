package member.action;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import member.model.MemberVO;

public class memberUpdate implements CommandAction {

	@Override
	public String requestPro(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		
		System.out.println("���������� ���� ȭ�� �ҷ�����");

		HttpSession session1 = request.getSession();
		MemberVO membervo = (MemberVO) session1.getAttribute("member");
		if (membervo == null) {
			// �α������� ���� ��� alert�� ���� �α��� �������� �����̷�Ʈ�մϴ�.
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println("<script>alert('�α����� �ʿ��մϴ�.'); location.href='/mvc2/member/loginForm.do';</script>");
			out.flush();
			return null;
		}
		return "/member/memberUpdate.jsp";
	}

}
