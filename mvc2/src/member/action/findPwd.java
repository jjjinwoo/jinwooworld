package member.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import member.model.memberDAO;

public class findPwd implements CommandAction {

	@Override
	public String requestPro(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		
		System.out.println("비밀번호 찾기 ");
		
		String name = request.getParameter("name");
		String userID = request.getParameter("userID");
		memberDAO dao = memberDAO.getInstance();
		 
		String pwd = dao.findPwd(name, userID);
		request.setAttribute("pwd", pwd);
		return "/member/findPwdResult.jsp";
	}

}
