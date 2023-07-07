package member.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class unregisterForm implements CommandAction {

	@Override
	public String requestPro(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		
		System.out.println("Å»Åð ¿Ï·á È­¸é ºÒ·¯¿À±â ");
		
		return "/member/unregisterForm.jsp";
	}

}
