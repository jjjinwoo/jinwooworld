package member.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import member.model.MemberVO;
import member.model.memberDAO;

public class memberUpdate1 implements CommandAction {

	@Override
	public String requestPro(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		
		memberDAO dao = memberDAO.getInstance();
		MemberVO memberVO = new MemberVO();
		memberVO.setUserID(request.getParameter("userID"));
		memberVO.setPwd(request.getParameter("pwd"));
		memberVO.setName(request.getParameter("name"));
		memberVO.setPhone(request.getParameter("phone"));
		memberVO.setPostnum(request.getParameter("postnum"));
		memberVO.setAddress1(request.getParameter("address1"));
		memberVO.setAddress2(request.getParameter("address2"));

		HttpSession session = request.getSession();
		session.setAttribute("member", memberVO);
		session.setAttribute("isLogOn", true);

		System.out.println("아이디에 해당하는 회원정보 수정");

		int result = dao.memberUpdate1(memberVO);

		return "/member/mypageForm.jsp";
	}

}
