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
	
	// ȸ������
	public int addMember(MemberVO memberVO);
	
	// �α���ó��
	public MemberVO login(MemberVO memberVO);
	
	//���̵� ã��
	public String findUser(String name, String phone);
	
	// ��й�ȣ ã��
	public String findPwd(String name, String userID);
	
	// ��й�ȣ Ȯ��
	public MemberVO getMember(String userID);	
	
	// id�ߺ�üũ
	public MemberVO getUser(String userID);
	
	// phone�ߺ�üũ
	public MemberVO getPhone(String phone);
	
	// ȸ�� Ż��
	public int memberDel(String userID);
	
	// ������û
	public int memberUpdate1(MemberVO memberVO);
	
	// ȸ�� ���
	public List<MemberVO> listMember(Map<String, Object> map);
	
	//ȸ����� count
	public int listCount(Map<String, Object> map);
	
	// ������ ����
	public int memberRemove(String userID);
	
	// ����Ʈ
	public List<ProductVO> getProducts(Map<String, Object> map);
	
	// ��ǰ����
	public int getProductCount(Map<String, Object> map);
	
	// ��ȭ��
	public ProductVO productDetail(int product_code);
	
	// ��ǰ option
	public List<ProductOptionVO> getproductOption(int product_code);
	
	public ProductOptionVO getProductOption3(String product_code, String product_size);
	// ��ٱ��� �߰�
	public CartVO addCart(CartVO cartVO);
	
	// ��ٱ��� ����
	public List<CartVO> cartList(String userID);
	
	// ��ٱ��� ��ü ����
	public void deleteAll(String userID);
	
	// ��ٱ��� üũ�� ����
	public void deletePart(String userID, int cartNum);
	
	// �ֹ������� �ѱ�� �κп��� ���� ������ ���� ��� update
	public CartVO updateCart(CartVO cartVO);
	
	public CartVO getCart(String userID, String product_code, String product_size);
	
    public void updateCart2(CartVO cartVO);
	
	// ��ǰ �������� ���
	public void insertOrderProduct(OrderProductVO orderProductVO);
	
	public void insertOrder(OrderVO orderVO);
	
	// ���� �Ϸ� �� ������ �ش� ��ٱ��� ���� 
	public int deleteCart(CartVO cartVO);
	
	 // db���� ��ǰ ������ŭ ���� 
	public void updateQuantity(ProductOptionVO productOptionVO);
	
	//product_code�� product_size�� ���� ���(quantity)�� ������
	public List<ProductOptionVO> getproductOption2(String product_code, String product_size);
	
	public ProductOptionVO getProductOption4(String product_code, String product_size);
	
	public OrderVO getOrder(String userID);
	
	// �ֹ� ����
	public List<OrderProductVO> getBuyProduct(Map<String, Object> map);
	
	// ��ǰ����
	public int getProductCount2(Map<String, Object> map);
	
	// �ֹ� ���� ��
	List<OrderVO> getOrders(String userID, int order_num);
	
	// �ֹ� ���� ��
	List<OrderProductVO> getOrderProducts(String userID, int order_num, int orderProduct_num);
	
	// ��� ���� ������Ʈ
	public void updateState(String userID, int order_num, int orderProduct_num, String order_state);
	
	// buyComplete update
	public void updateBuyComplete(String userID, int orderProduct_num);
	
	// ȯ�� ��û update
	public void updateRefundConfirm(String userID, int orderProduct_num, String order_state);
	
	// product_code �޾ƿ���
	public int getProductCodeSeq();
	
	public int getProductCodeCurrval();
	
	// ��ǰ ���
	public ProductVO productInsert(ProductVO productVO);
	
	// ��ǰ �ɼ� ���
	public List<ProductOptionVO> productOptionInsert(ProductOptionVO productOptionVO);
	
	// ��ǰ ����
	public ProductVO productUpdate(ProductVO productVO);
	
	// ��ǰ �ɼ� ����
	public void productOptionUpdate(ProductOptionVO productOptionVO);
	
	// ��ǰ �ɼ� ��ü ����
	public void productOptionDelete(int product_code);

	
	// �����ϱ� ���� deletestate update
	public void productDelete(ProductVO productVO);
	
	// �����ϱ� ���� product_code�� �ش��ϴ� option 0���� ����
	public void productOptionUpdate2(ProductOptionVO productOptionVO);
	
	// ��ǰ ��� ���� product_code �ߺ� �˻�
	public  boolean isProductCodeExists(String product_code);
	
	public void imageUp(int product_code, String images2);
	
	public void imageUp2(int product_code, String images1);
}
