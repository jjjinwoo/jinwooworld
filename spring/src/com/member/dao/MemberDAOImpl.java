package com.member.dao;

import java.util.ArrayList; 
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cart.VO.CartVO;
import com.member.VO.MemberVO;
import com.order.VO.OrderProductVO;
import com.order.VO.OrderVO;

import com.product.VO.ProductOptionVO;
import com.product.VO.ProductVO;

@Component
public class MemberDAOImpl implements MemberDAO {
	
	@Autowired
	private SqlSessionTemplate sqlSession;
	
	@Autowired
	private DataSource dataSource;
	
	// 회원가입처리
	@Override
	public int addMember(MemberVO memberVO) {
		
		System.out.println("회원가입 DAOImpl" + memberVO);
		System.out.println("userID: " + memberVO.getUserID());
	    System.out.println("pwd: " + memberVO.getPwd());
	    System.out.println("name: " + memberVO.getName());
	    System.out.println("postnum: " + memberVO.getPostnum());
	    System.out.println("address1: " + memberVO.getAddress1());
	    System.out.println("address2: " + memberVO.getAddress2());
	    System.out.println("phone: " + memberVO.getPhone());
		int result = sqlSession.insert("addMember", memberVO);
		
		return result;
	}
	
	// 로그인처리
	@Override
	public MemberVO login(MemberVO memberVO) {
		
		MemberVO memVO = sqlSession.selectOne("login", memberVO);
		return memVO;
	}
	
	// 아이디 찾기 
	@Override
	public String findUser(String name, String phone) {
		Map<String, Object> params = new HashMap<>();
		params.put("name", name);
		params.put("phone", phone);
		return sqlSession.selectOne("findUser", params);
	}
	
	// 비밀번호 찾기
	@Override
	public String findPwd(String name, String userID) {
		
		Map<String, Object> params = new HashMap<>();
		params.put("name", name);
		params.put("userID", userID);
		return sqlSession.selectOne("findPwd", params);
	}
	
	// 비밀번호 확인
	@Override
	public MemberVO getMember(String userID) {
		
		return sqlSession.selectOne("getMember", userID);
	}
	
	// id중복체크
	@Override
	public MemberVO getUser(String userID) {
		
		return sqlSession.selectOne("getUser", userID);
	}
	
	// phone중복체크
	@Override
	public MemberVO getPhone(String phone) {
		
		return sqlSession.selectOne("getPhone", phone);
	}
	
	// 회원 탈퇴
	@Override
	public int memberDel(String userID) {
		
		System.out.println(userID);
		int result = sqlSession.delete("memberDel", userID);
		return result;
	}
	
	// 수정요청
	@Override
	public int memberUpdate1(MemberVO memberVO) {
		
		System.out.println("수정요청 시작 DAOImpl");
		System.out.println("userID: " + memberVO.getUserID());
	    System.out.println("pwd: " + memberVO.getPwd());
	    System.out.println("name: " + memberVO.getName());
	    System.out.println("postnum: " + memberVO.getPostnum());
	    System.out.println("address1: " + memberVO.getAddress1());
	    System.out.println("address2: " + memberVO.getAddress2());
	    System.out.println("phone: " + memberVO.getPhone());
		return sqlSession.update("memberUpdate1", memberVO);
	}
	
	// 회원 목록
	@Override
	public List<MemberVO> listMember(Map<String, Object> map) {
		
		return sqlSession.selectList("listMember", map);
	}
	
	// 관리자 삭제
	@Override
	public int memberRemove(String userID) {
		
		int result = sqlSession.delete("memberRemove", userID);
		return result;
	}
	
	// 리스트 
	@Override
	public List<ProductVO> getProducts(Map<String, Object> map) {
		
		return sqlSession.selectList("getProducts", map);
	}
	
	// 상품 개수
	@Override
	public int getProductCount(Map<String, Object> map) {
		
		return ((Integer)sqlSession.selectOne("getProductCount", map)).intValue();
	}
	
	// 상세화면
	@Override
	public ProductVO productDetail(int product_code) {
		
		return sqlSession.selectOne("productDetail", product_code);
	}
	
	// 상품 option
	@Override
	public List<ProductOptionVO> getproductOption(int product_code) {
		
		return sqlSession.selectList("getproductOption", product_code);
	}
	
	@Override
	public ProductOptionVO getProductOption3(String product_code, String product_size) {
	    Map<String, Object> map = new HashMap<String, Object>();
	    map.put("product_code", product_code);
	    map.put("product_size", product_size);
	    return sqlSession.selectOne("getProductOption3", map);
	}
	
	// 장바구니 추가
	@Override
	public CartVO addCart(CartVO cartVO) {
		
		System.out.println("userID: " + cartVO.getUserID());
	    System.out.println("phone: " + cartVO.getPhone());
	    System.out.println("product_size: " + cartVO.getProduct_size());
	    System.out.println("quantity: " + cartVO.getQuantity());
	    System.out.println("product_price: " + cartVO.getProduct_price());
	    System.out.println("product_code: " + cartVO.getProduct_code());
	    System.out.println("product_name: " + cartVO.getProduct_name());
	    sqlSession.insert("addCart", cartVO);
	    return cartVO;
	}
	
	// 장바구니 정보
	@Override
	public List<CartVO> cartList(String userID) {
		
		return sqlSession.selectList("cartList", userID);
	}
	
	// 장바구니 전체 삭제
	@Override
	public void deleteAll(String userID) {
		
		sqlSession.delete("deleteAll", userID);
		
	}
	
	// 장바구니 체크만 삭제
	@Override
	public void deletePart(String userID, int cartNum) {
		
		Map<String, Object> params = new HashMap<>();
		params.put("userID", userID);
		params.put("cartNum", cartNum);
		
		sqlSession.delete("deletePart", params);
		
	}
	
	// 주문결제로 넘기는 부분에서 수량 변경이 있을 경우 update
	@Override
	public CartVO updateCart(CartVO cartVO) {

	    sqlSession.update("updateCart", cartVO);
	    return cartVO;
	}

	@Override
	public void insertOrderProduct(OrderProductVO orderProductVO) {
		
		sqlSession.insert("insertOrderProduct", orderProductVO);
	}

	@Override
	public void insertOrder(OrderVO orderVO) {
		// TODO Auto-generated method stub
		 sqlSession.insert("insertOrder", orderVO);
		 
		 int orderNum = sqlSession.selectOne("selectOrderNum");
		 // OrderVO 객체의 order_num 값을 설정합니다.
		 orderVO.setOrder_num(orderNum);
	}
	
	// 결제 완료 후 결제된 해당 장바구니 삭제 
	@Override
	public int deleteCart(CartVO cartVO) {
		
		int result = sqlSession.delete("deleteCart", cartVO);
		return result;
	}
	
	// db에서 상품 수량만큼 삭제 
	@Override
	public void updateQuantity(ProductOptionVO productOptionVO) {
		
		sqlSession.update("updateQuantity", productOptionVO);
	}
	
	//product_code와 product_size에 대한 재고량(quantity)을 가져옴
	@Override
	public List<ProductOptionVO> getproductOption2(String product_code, String product_size) {
	    Map<String, Object> params = new HashMap<>();
	    params.put("product_code", product_code);
	    params.put("product_size", product_size);
	    return sqlSession.selectList("getproductOption2", params);
	}

	
	@Override
	public ProductOptionVO getProductOption4(String product_code, String product_size) {
	    Map<String, Object> map = new HashMap<String, Object>();
	    map.put("product_code", product_code);
	    map.put("product_size", product_size);
	    return sqlSession.selectOne("getProductOption3", map);
	}
	
	 @Override
	 public CartVO getCart(String userID, String product_code, String product_size) {
	       Map<String, Object> paramMap = new HashMap<String, Object>();
	        paramMap.put("userID", userID);
	        paramMap.put("product_code", product_code);
	        paramMap.put("product_size", product_size);
	        return sqlSession.selectOne("getCart", paramMap);
	  }

	  @Override
	  public void updateCart2(CartVO cartVO) {
	        sqlSession.update("updateCart2", cartVO);
	   }
	
	  @Override
	    public OrderVO getOrder(String userID) {
	        return sqlSession.selectOne("getOrder", userID);
	    }
	
	// 주문 내역
	@Override
	public List<OrderProductVO> getBuyProduct(Map<String, Object> map) {
		
		return sqlSession.selectList("getBuyProduct", map);
	}

	@Override
	public int getProductCount2(Map<String, Object> map) {
		
		return ((Integer)sqlSession.selectOne("getProductCount2", map)).intValue();
	}
	
	// 주문 내역 상세
	@Override
	public List<OrderVO> getOrders(String userID, int order_num) {
		Map<String, Object> map = new HashMap<String, Object>();
	    map.put("userID", userID);
	    map.put("order_num", order_num);
	    return sqlSession.selectList("getOrders", map);
	}
	
	// 주문 내역 상세
	@Override
	public List<OrderProductVO> getOrderProducts(String userID, int order_num, int orderProduct_num) {
		Map<String, Object> map = new HashMap<String, Object>();
	    map.put("userID", userID);
	    map.put("order_num", order_num);
	    map.put("orderProduct_num", orderProduct_num);
	    return sqlSession.selectList("getOrderProducts", map);
	}
	
	// 배송 상태
	@Override
	public void updateState(String userID, int order_num, int orderProduct_num, String order_state) {
		Map<String, Object> map = new HashMap<String, Object>();
	    map.put("userID", userID);
	    map.put("order_num", order_num);
	    map.put("orderProduct_num", orderProduct_num);
	    map.put("order_state", order_state);
	    sqlSession.update("updateState", map);
	    
	}
	
	// buyComplete update
	@Override
	public void updateBuyComplete(String userID, int orderProduct_num) {
		
		Map<String, Object> map = new HashMap<String, Object>();
	    map.put("userID", userID);
	    map.put("orderProduct_num", orderProduct_num);
	    sqlSession.update("updateBuyComplete", map);
		
	}

	// 환불 요청 update
	@Override
	public void updateRefundConfirm(String userID, int orderProduct_num, String order_state) {
		
		Map<String, Object> params = new HashMap<>();
	    params.put("userID", userID);
	    params.put("orderProduct_num", orderProduct_num);
	    params.put("order_state", order_state);
	    sqlSession.update("updateRefundConfirm", params);
		
	}

	@Override
	public int listCount(Map<String, Object> map) {
		
		return ((Integer)sqlSession.selectOne("listCount", map)).intValue();
	}
	
	// product_code 받아오기
	@Override
	public int getProductCodeSeq() {
	    return sqlSession.selectOne("getProductCodeSeq");
	}
	
	public int getProductCodeCurrval() {
	    return sqlSession.selectOne("getProductCodeCurrval");
	}
	
	// 상품 등록
	@Override
	public ProductVO productInsert(ProductVO productVO) {
		
		sqlSession.insert("productInsert", productVO);
	    return productVO;
	}
	
	// 상품 옵션 등록
	@Override
	public List<ProductOptionVO> productOptionInsert(ProductOptionVO productOptionVO) {
	    sqlSession.insert("productOptionInsert", productOptionVO);
	    List<ProductOptionVO> result = new ArrayList<>();
	    result.add(productOptionVO);
	    return result;
	}
	
	// 상품 수정
	@Override
	public ProductVO productUpdate(ProductVO productVO) {
		sqlSession.update("productUpdate", productVO);
        return productVO;
	}
	
	//상품 옵션 수정
	@Override
	public void productOptionUpdate(ProductOptionVO productOptionVO) {
		
		sqlSession.insert("productOptionUpdate", productOptionVO);
	}
	
	// 상품 옵션 수정 전 모든 데이터 삭제
	@Override
	public void productOptionDelete(int product_code) {
		
		sqlSession.delete("productOptionDelete", product_code);
		
	}
	
	
	// 삭제하기 위한 deletestate update
	@Override
	public void productDelete(ProductVO productVO) {
		sqlSession.update("productDelete", productVO);
		
	}

	// 삭제하기 위한 product_code에 해당하는 option 0개로 변경
	@Override
	public void productOptionUpdate2(ProductOptionVO productOptionVO) {
		sqlSession.update("productOptionUpdate2", productOptionVO);
		
	}
	
	// 상품 등록 전에 product_code 중복 검사
	@Override
	public boolean isProductCodeExists(String product_code) {
		// TODO Auto-generated method stub
		return sqlSession.selectOne("isProductCodeExists", product_code);
	}
	
	
	// images2 업데이트
	@Override
	public void imageUp(int product_code, String images2) {
		 Map<String, Object> params = new HashMap<String, Object>();
		 params.put("product_code", product_code);
		 params.put("images2", images2);
		 sqlSession.update("imageUp", params);
		
	}
	
	@Override
	public void imageUp2(int product_code, String images1) {
		 Map<String, Object> params = new HashMap<String, Object>();
		 params.put("product_code", product_code);
		 params.put("images1", images1);
		 sqlSession.update("imageUp2", params);
		
	}
	


}
