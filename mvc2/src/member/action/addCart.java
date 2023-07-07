package member.action;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import member.model.CartVO;
import member.model.MemberVO;
import member.model.ProductOptionVO;
import member.model.memberDAO;

public class addCart implements CommandAction {

	@Override
	public String requestPro(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		
		memberDAO dao = memberDAO.getInstance();
        
        System.out.println("��ٱ��� �߰�");
        CartVO cartVO = new CartVO();
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
        // memberVO ��ü���� userID�� phone ������ �����ɴϴ�.
        cartVO.setUserID(memberVO.getUserID());
        cartVO.setPhone(memberVO.getPhone());
        System.out.println(memberVO.getUserID());
        
        int product_code = Integer.parseInt(request.getParameter("product_code"));
        String product_size = request.getParameter("product_size");
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        
        System.out.println("product_code : " + product_code);
        System.out.println("product_size : " + product_size);
        System.out.println("quantity : " + quantity);
       
        // productOptionVO�� �����ͼ� ����� Ȯ���մϴ�.
        ProductOptionVO productOptionVO = dao.getProductOption3(product_code, product_size);
        System.out.println("productOptionVO.getQuantity() : " + productOptionVO.getQuantity());
        System.out.println("quantity : " + quantity);
        
        if (productOptionVO.getQuantity() < quantity) {
            // ����� ������ ��� "out of stock"�� ��ȯ�մϴ�.
        	System.out.println("��� ����");
        	response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("out of stock");
            out.close();
            return null;
        }

        // ��ٱ��Ͽ� ������ ��ǰ �ڵ�� ������ ��ǰ ũ���� ��ǰ�� �ִ��� Ȯ���մϴ�.
        CartVO existingCartVO = dao.getCart(memberVO.getUserID(), product_code, product_size);
        System.out.println("existingCartVO : " + existingCartVO.getCartNum());
        System.out.println("userID : " + existingCartVO.getUserID());
        if (existingCartVO.getUserID() != null) {
        	System.out.println("��ٱ��� �̹� ���� ");
            // �̹� �����ϴ� ��� ������ ������Ʈ�մϴ�.
            existingCartVO.setQuantity(existingCartVO.getQuantity() + quantity);
            dao.updateCart2(existingCartVO);
        } else {
            // �������� �ʴ� ��� ���ο� ��ǰ�� �߰��մϴ�.
        	System.out.println("��ٱ��� ���� x");
            cartVO.setProduct_code(product_code);
            cartVO.setProduct_size(product_size);
            cartVO.setQuantity(quantity);
            cartVO.setProduct_name(request.getParameter("product_name"));
            cartVO.setProduct_price(Integer.parseInt(request.getParameter("product_price")));
            cartVO.setDelivery(request.getParameter("delivery"));
            cartVO.setImages2(request.getParameter("images2"));
            System.out.println("product_name : " + request.getParameter("product_name"));
            System.out.println("images : " + request.getParameter("images2"));
            dao.addCart(cartVO);
        }

        // ���������� ó���� ��� "success"�� ��ȯ�մϴ�.
        return "success";
    }

}
