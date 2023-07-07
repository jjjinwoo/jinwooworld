package com.member.dao;

import java.util.List; 
import java.util.Map;

import com.cart.VO.CartVO;
import com.member.VO.MemberVO;
import com.order.VO.OrderProductVO;
import com.order.VO.OrderVO;

import com.product.VO.ProductOptionVO;
import com.product.VO.ProductVO;

public interface MemberDAO {
	
	// 회원가입
	public int addMember(MemberVO memberVO);
	
	// 로그인처리
	public MemberVO login(MemberVO memberVO);
	
	//아이디 찾기
	public String findUser(String name, String phone);
	
	// 비밀번호 찾기
	public String findPwd(String name, String userID);
	
	// 비밀번호 확인
	public MemberVO getMember(String userID);	
	
	// id중복체크
	public MemberVO getUser(String userID);
	
	// phone중복체크
	public MemberVO getPhone(String phone);
	
	// 회원 탈퇴
	public int memberDel(String userID);
	
	// 수정요청
	public int memberUpdate1(MemberVO memberVO);
	
	// 회원 목록
	public List<MemberVO> listMember(Map<String, Object> map);
	
	//회원목록 count
	public int listCount(Map<String, Object> map);
	
	// 관리자 삭제
	public int memberRemove(String userID);
	
	// 리스트
	public List<ProductVO> getProducts(Map<String, Object> map);
	
	// 상품개수
	public int getProductCount(Map<String, Object> map);
	
	// 상세화면
	public ProductVO productDetail(int product_code);
	
	// 상품 option
	public List<ProductOptionVO> getproductOption(int product_code);
	
	public ProductOptionVO getProductOption3(String product_code, String product_size);
	// 장바구니 추가
	public CartVO addCart(CartVO cartVO);
	
	// 장바구니 정보
	public List<CartVO> cartList(String userID);
	
	// 장바구니 전체 삭제
	public void deleteAll(String userID);
	
	// 장바구니 체크만 삭제
	public void deletePart(String userID, int cartNum);
	
	// 주문결제로 넘기는 부분에서 수량 변경이 있을 경우 update
	public CartVO updateCart(CartVO cartVO);
	
	public CartVO getCart(String userID, String product_code, String product_size);
	
    public void updateCart2(CartVO cartVO);
	
	// 상품 구매정보 등록
	public void insertOrderProduct(OrderProductVO orderProductVO);
	
	public void insertOrder(OrderVO orderVO);
	
	// 결제 완료 후 결제된 해당 장바구니 삭제 
	public int deleteCart(CartVO cartVO);
	
	 // db에서 상품 수량만큼 삭제 
	public void updateQuantity(ProductOptionVO productOptionVO);
	
	//product_code와 product_size에 대한 재고량(quantity)을 가져옴
	public List<ProductOptionVO> getproductOption2(String product_code, String product_size);
	
	public ProductOptionVO getProductOption4(String product_code, String product_size);
	
	public OrderVO getOrder(String userID);
	
	// 주문 내역
	public List<OrderProductVO> getBuyProduct(Map<String, Object> map);
	
	// 상품개수
	public int getProductCount2(Map<String, Object> map);
	
	// 주문 내역 상세
	List<OrderVO> getOrders(String userID, int order_num);
	
	// 주문 내역 상세
	List<OrderProductVO> getOrderProducts(String userID, int order_num, int orderProduct_num);
	
	// 배송 상태 업데이트
	public void updateState(String userID, int order_num, int orderProduct_num, String order_state);
	
	// buyComplete update
	public void updateBuyComplete(String userID, int orderProduct_num);
	
	// 환불 요청 update
	public void updateRefundConfirm(String userID, int orderProduct_num, String order_state);
	
	// product_code 받아오기
	public int getProductCodeSeq();
	
	public int getProductCodeCurrval();
	
	// 상품 등록
	public ProductVO productInsert(ProductVO productVO);
	
	// 상품 옵션 등록
	public List<ProductOptionVO> productOptionInsert(ProductOptionVO productOptionVO);
	
	// 상품 수정
	public ProductVO productUpdate(ProductVO productVO);
	
	// 상품 옵션 수정
	public void productOptionUpdate(ProductOptionVO productOptionVO);
	
	// 상품 옵션 전체 삭제
	public void productOptionDelete(int product_code);

	
	// 삭제하기 위한 deletestate update
	public void productDelete(ProductVO productVO);
	
	// 삭제하기 위한 product_code에 해당하는 option 0개로 변경
	public void productOptionUpdate2(ProductOptionVO productOptionVO);
	
	// 상품 등록 전에 product_code 중복 검사
	public  boolean isProductCodeExists(String product_code);
	
	public void imageUp(int product_code, String images2);
	
	public void imageUp2(int product_code, String images1);
}
