package member.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import member.model.memberDAO;

public class findUser implements CommandAction {

	@Override
	public String requestPro(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		
		System.out.println("아이디 찾기 ");
		
		String name = request.getParameter("name");
		String phone = request.getParameter("phone");
		memberDAO dao = memberDAO.getInstance();
		 
		String userID = dao.findUser(name, phone);
		request.setAttribute("userID", userID);
		return "/member/findUserResult.jsp";
	}

}
