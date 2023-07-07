package member.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import member.model.MemberVO;
import member.model.memberDAO;

public class addMember implements CommandAction {

	@Override
	public String requestPro(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		
		System.out.println("회원가입 처리 시작 ");
		
		String userID = request.getParameter("userID");
	    String pwd = request.getParameter("pwd");
	    String name = request.getParameter("name");
	    String postnum = request.getParameter("postnum");
	    String address1 = request.getParameter("address1");
	    String address2 = request.getParameter("address2");
	    String phone = request.getParameter("phone");
	    
	    System.out.println("userID: " + userID);
	    System.out.println("pwd: " + pwd);
	    System.out.println("name: " + name);
	    System.out.println("postnum: " + postnum);
	    System.out.println("address1: " + address1);
	    System.out.println("address2: " + address2);
	    System.out.println("phone: " + phone);
	    
	    MemberVO vo = new MemberVO();
	    vo.setUserID(userID);
	    vo.setPwd(pwd);
	    vo.setPostnum(postnum);
	    vo.setAddress1(address1);
	    vo.setAddress2(address2);
	    vo.setName(name);
	    vo.setPhone(phone);
	    
	    System.out.println("MemberVO: " + vo);
	    
		response.setContentType("text/html;charset=UTF-8");
		
		HttpSession session = request.getSession();
		session.setAttribute("member", vo);
		session.setAttribute("isLogOn", true);
		
		int result = 0;
		memberDAO dao = memberDAO.getInstance();
		result = dao.addMember(vo);

		return "/member/main.do";
	    
	}

}
