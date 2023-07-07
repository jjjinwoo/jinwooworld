package member.action;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import member.model.MemberVO;
import member.model.memberDAO;

public class buyConfirm implements CommandAction {

	@Override
	public String requestPro(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		System.out.println("buyComplete update");

        int orderProduct_num = Integer.parseInt(request.getParameter("orderProduct_num"));

        HttpSession session1 = request.getSession();
        MemberVO membervo = (MemberVO) session1.getAttribute("member");
        if (membervo == null) {
            // �α������� ���� ��� alert�� ���� �α��� �������� �����̷�Ʈ�մϴ�.
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<script>alert('�α����� �ʿ��մϴ�.'); location.href='/mvc2/member/loginForm.do';</script>");
            out.flush();
            return null;
        }
        
        String userID = membervo.getUserID();
        memberDAO dao = memberDAO.getInstance();
        dao.updateBuyComplete(userID, orderProduct_num);
        
        response.setContentType("text/plain; charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.print("success");
        out.flush();

        return null;
	}

}
