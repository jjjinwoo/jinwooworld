package member.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class memberForm implements CommandAction {

	@Override
	public String requestPro(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		
		System.out.println("ȸ������  ȭ�� �ҷ����� ");
		
		return "/member/memberForm.jsp";
	}

}
