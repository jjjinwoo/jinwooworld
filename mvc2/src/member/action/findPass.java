package member.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class findPass implements CommandAction {

	@Override
	public String requestPro(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		
		System.out.println(" 비밀번호 찾기 화면 불러오기 ");
		
		return "/member/findPass.jsp";
	}

}
