package member.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class findPass implements CommandAction {

	@Override
	public String requestPro(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		
		System.out.println(" ��й�ȣ ã�� ȭ�� �ҷ����� ");
		
		return "/member/findPass.jsp";
	}

}
