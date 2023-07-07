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
        
        System.out.println("장바구니 이동");
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        
        // HttpSession 객체에서 member 속성을 가져옵니다.
        HttpSession session = request.getSession();
        MemberVO memberVO = (MemberVO) session.getAttribute("member");
        if (memberVO == null) {
            // 로그인하지 않은 경우 alert을 띄우고 로그인 페이지로 리다이렉트합니다.
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<script>alert('로그인이 필요합니다.'); location.href='/mvc2/member/loginForm.do';</script>");
            out.flush();
            return null;
        }
        // memberVO 객체에서 userID 정보를 가져옵니다.
        String userID = memberVO.getUserID();
        
        // 경고 메시지가 이미 표시된 횟수를 확인합니다.
        HttpSession session1 = request.getSession();
        Integer alertCount = (Integer) session1.getAttribute("alertCount");
        if (alertCount == null) {
            alertCount = 0;
        }
        
        List<CartVO> cartList = dao.cartList(userID);
        
        // 삭제상태 정보 가져오기
        List<ProductVO> productList = new ArrayList<ProductVO>();
        for (CartVO cart : cartList) {
            ProductVO productVO = dao.getDeleteState2(cart.getProduct_code());
            productList.add(productVO);
        }
        // 장바구니에서 재고량 확인하기 
        List<ProductOptionVO> productOptionList = new ArrayList<ProductOptionVO>();
        for (CartVO cart : cartList) {
            ProductOptionVO productOptionVO = dao.getProductOption3(cart.getProduct_code(), cart.getProduct_size());
            productOptionList.add(productOptionVO);
        }
        
        // 장바구니에 있는 상품 중 deleteState 값이 'Y'인 상품이 있는지 확인합니다.
        boolean hasDeletedProduct = false;
        for (CartVO cart : cartList) {
            String deleteState = dao.getDeleteState(cart.getProduct_code());
            if (deleteState.equals("Y")) {
                hasDeletedProduct = true;
                break;
            }
        }
            
        if (hasDeletedProduct && alertCount < 1) {
            // deleteState 값이 'Y'인 상품이 있는 경우 경고 메시지를 표시합니다. 경고메세지 최대 1번
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<script>alert('사용자의 권한으로 인한 판매되지 않는 상품이 있습니다. 해당 상품을 삭제하고 다시 시도해주세요.'); location.href='/mvc2/member/mycartForm.do';</script>");
            out.flush();
            
            // 경고 메시지가 표시된 횟수를 증가시키고 세션에 저장합니다.
            alertCount++;
            session.setAttribute("alertCount", alertCount);
            
            // 경고 메시지가 표시된 횟수를 콘솔에 출력합니다.
            System.out.println("경고 메시지가 " + alertCount + "번째로 표시되었습니다.");
          
            return null;
        }else {
            // deleteState 값이 'Y'인 상품이 없는 경우 alertCount 변수의 값을 0으로 초기화하고 세션에 저장합니다.
            session.setAttribute("alertCount", 0);
        }
        
        // 장바구니 정보를 request 객체에 추가합니다.
        request.setAttribute("cartList", cartList);
        
        request.setAttribute("productList", productList);
        
        request.setAttribute("productOptionList", productOptionList);
        
        return "/member/mycartForm.jsp";
    }

}
