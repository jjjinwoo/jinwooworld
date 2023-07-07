package member.action;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest; 
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import member.model.MemberVO;
import member.model.memberDAO;

public class login implements CommandAction {

	@Override
	public String requestPro(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		
		memberDAO dao = memberDAO.getInstance();
        
        System.out.println("로그인 처리 시작");
        
        String userID = request.getParameter("userID");
        String inputPwd = request.getParameter("pwd");
        
        if (userID == null || userID.equals("")) {
            request.setAttribute("result", "loginIdEmpty");
            return "/member/loginForm.do?result=loginIdEmpty";
        }
        
        MemberVO memberVO = dao.login(userID);
        
        if (memberVO != null) {
            if (memberVO.getPwd().equals(inputPwd)) {
                HttpSession session = request.getSession();
                if (!session.isNew()) {
                    SessionListener.getSessionidCheck("member", memberVO.getUserID());
                    session.setAttribute("member", memberVO);
                    session.setAttribute("isLogOn", true);
                }
                return "/member/main.do";
            } else {
                request.setAttribute("result", "PasswordFailed");
                return "/member/loginForm.do?result=PasswordFailed";
            }
        } else {
            request.setAttribute("result", "loginFailed");
            return "/member/loginForm.do?result=loginFailed";
        }
    }
}
