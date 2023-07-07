package member.action;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import member.model.MemberVO;
import member.model.OrderProductVO;
import member.model.OrderVO;
import member.model.memberDAO;

public class orderDetail implements CommandAction {

	@Override
	public String requestPro(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		 System.out.println("�ֹ� ���� ��");

	        int order_num = Integer.parseInt(request.getParameter("order_num"));
	        System.out.println(order_num);

	        HttpSession session1 = request.getSession();
	        MemberVO membervo = (MemberVO) session1.getAttribute("member");
	        if (membervo == null) {
	            // �α������� ���� ��� alert�� ���� �α��� �������� �����̷�Ʈ�մϴ�.
	            response.setContentType("text/html; charset=UTF-8");
	            PrintWriter out = response.getWriter();
	            out.println("<script>alert('�α����� �ʿ��մϴ�.'); location.href='/project/member/loginForm.do';</script>");
	            out.flush();
	            return null;
	        }

	        String userID = membervo.getUserID();
	        System.out.println(userID);
	        memberDAO dao = memberDAO.getInstance();
	        List<OrderVO> orderVO = dao.getOrders(userID, order_num);
	        List<OrderProductVO> orderProducts = dao.getOrderProducts(userID, order_num);

	        request.setAttribute("orderVO", orderVO);
	        request.setAttribute("orderProducts", orderProducts);

	        return "/member/orderDetail.jsp";
	    }

}
