package member.action;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import member.model.CartVO;
import member.model.MemberVO;
import member.model.ProductOptionVO;
import member.model.ProductVO;
import member.model.memberDAO;

public class mycartForm implements CommandAction {

	@Override
	public String requestPro(HttpServletRequest request, HttpServletResponse response) throws Throwable {
			
		memberDAO dao = memberDAO.getInstance();
        
        System.out.println("��ٱ��� �̵�");
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        
        // HttpSession ��ü���� member �Ӽ��� �����ɴϴ�.
        HttpSession session = request.getSession();
        MemberVO memberVO = (MemberVO) session.getAttribute("member");
        if (memberVO == null) {
            // �α������� ���� ��� alert�� ���� �α��� �������� �����̷�Ʈ�մϴ�.
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<script>alert('�α����� �ʿ��մϴ�.'); location.href='/mvc2/member/loginForm.do';</script>");
            out.flush();
            return null;
        }
        // memberVO ��ü���� userID ������ �����ɴϴ�.
        String userID = memberVO.getUserID();
        
        // ��� �޽����� �̹� ǥ�õ� Ƚ���� Ȯ���մϴ�.
        HttpSession session1 = request.getSession();
        Integer alertCount = (Integer) session1.getAttribute("alertCount");
        if (alertCount == null) {
            alertCount = 0;
        }
        
        List<CartVO> cartList = dao.cartList(userID);
        
        // �������� ���� ��������
        List<ProductVO> productList = new ArrayList<ProductVO>();
        for (CartVO cart : cartList) {
            ProductVO productVO = dao.getDeleteState2(cart.getProduct_code());
            productList.add(productVO);
        }
        // ��ٱ��Ͽ��� ��� Ȯ���ϱ� 
        List<ProductOptionVO> productOptionList = new ArrayList<ProductOptionVO>();
        for (CartVO cart : cartList) {
            ProductOptionVO productOptionVO = dao.getProductOption3(cart.getProduct_code(), cart.getProduct_size());
            productOptionList.add(productOptionVO);
        }
        
        // ��ٱ��Ͽ� �ִ� ��ǰ �� deleteState ���� 'Y'�� ��ǰ�� �ִ��� Ȯ���մϴ�.
        boolean hasDeletedProduct = false;
        for (CartVO cart : cartList) {
            String deleteState = dao.getDeleteState(cart.getProduct_code());
            if (deleteState.equals("Y")) {
                hasDeletedProduct = true;
                break;
            }
        }
            
        if (hasDeletedProduct && alertCount < 1) {
            // deleteState ���� 'Y'�� ��ǰ�� �ִ� ��� ��� �޽����� ǥ���մϴ�. ���޼��� �ִ� 1��
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<script>alert('������� �������� ���� �Ǹŵ��� �ʴ� ��ǰ�� �ֽ��ϴ�. �ش� ��ǰ�� �����ϰ� �ٽ� �õ����ּ���.'); location.href='/mvc2/member/mycartForm.do';</script>");
            out.flush();
            
            // ��� �޽����� ǥ�õ� Ƚ���� ������Ű�� ���ǿ� �����մϴ�.
            alertCount++;
            session.setAttribute("alertCount", alertCount);
            
            // ��� �޽����� ǥ�õ� Ƚ���� �ֿܼ� ����մϴ�.
            System.out.println("��� �޽����� " + alertCount + "��°�� ǥ�õǾ����ϴ�.");
          
            return null;
        }else {
            // deleteState ���� 'Y'�� ��ǰ�� ���� ��� alertCount ������ ���� 0���� �ʱ�ȭ�ϰ� ���ǿ� �����մϴ�.
            session.setAttribute("alertCount", 0);
        }
        
        // ��ٱ��� ������ request ��ü�� �߰��մϴ�.
        request.setAttribute("cartList", cartList);
        
        request.setAttribute("productList", productList);
        
        request.setAttribute("productOptionList", productOptionList);
        
        return "/member/mycartForm.jsp";
    }

}
