package member.action;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import member.model.MemberVO;

public class memberUp implements CommandAction {

	@Override
	public String requestPro(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		System.out.println("수정 전 비밀번호 입력 화면");

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
		return "/member/memberUp.jsp";
	}
}
