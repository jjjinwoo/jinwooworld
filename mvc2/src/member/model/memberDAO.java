package member.model;

import java.sql.Connection; 
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class memberDAO {
	
	private static memberDAO instance = null;
	private memberDAO(){}
	
	public static memberDAO getInstance(){
		if(instance == null){
			synchronized (memberDAO.class) {
				instance = new memberDAO();
				
			}
		}
		return instance;
	}
	// resultSet rs = null; �� ���� �ڵ�� �����ͺ��̽����� �����͸� �˻����� �ʴ� ������ ������ �� ���. ���� ���, INSERT, UPDATE, DELETE ���� ���� ������ ������ �� ���˴ϴ�.
	// PreparedStatement�� SQL ���� �����ͺ��̽��� �����ϴ� ��ü�Դϴ�. PreparedStatement ��ü�� Connection ��ü�� prepareStatement �޼ҵ带 ����Ͽ� ������ �� �ֽ��ϴ�.
	
	// ��ǰ ����Ʈ
	public List<ProductVO> getProducts(Map<String, Object> map) {
	    List<ProductVO> products = new ArrayList<>();
	    Connection conn = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;

	    try {
	        conn = ConnUtil.getConnection();
	        StringBuilder sql = new StringBuilder();
	        sql.append("SELECT * FROM ( SELECT rownum rnum, p.* FROM ( SELECT * FROM product WHERE DELETESTATE = 'N' ");
	        if (map.get("keyField") != null) {
	            if ("product_type".equals(map.get("keyField"))) {
	                sql.append("AND LOWER(product_type) LIKE LOWER(?) ");
	            } else if ("product_name".equals(map.get("keyField"))) {
	                sql.append("AND LOWER(product_name) LIKE LOWER(?) ");
	            } else if ("all".equals(map.get("keyField"))) {
	                sql.append("AND (LOWER(product_type) LIKE LOWER(?) OR LOWER(product_name) LIKE LOWER(?)) ");
	            }
	        }
	        sql.append(") p) WHERE RNUM >= ? AND RNUM <= ?");
	        pstmt = conn.prepareStatement(sql.toString());
	        int index = 1;
	        if (map.get("keyField") != null) {
	            if ("product_type".equals(map.get("keyField")) || "product_name".equals(map.get("keyField"))) {
	                pstmt.setString(index++, "%" + map.get("keyWord") + "%");
	            } else if ("all".equals(map.get("keyField"))) {
	                pstmt.setString(index++, "%" + map.get("keyWord") + "%");
	                pstmt.setString(index++, "%" + map.get("keyWord") + "%");
	            }
	        }
	        pstmt.setInt(index++, (Integer) map.get("start"));
	        pstmt.setInt(index++, (Integer) map.get("end"));
	        rs = pstmt.executeQuery();
	        while (rs.next()) {
	        	ProductVO product = new ProductVO();
	            product.setProduct_code(rs.getInt("product_code"));
	            product.setProduct_type(rs.getString("product_type"));
	            product.setProduct_name(rs.getString("product_name"));
	            product.setProduct_price(rs.getInt("product_price"));
	            product.setImages1(rs.getString("images1"));
	            product.setImages2(rs.getString("images2"));
	            products.add(product);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	    	if(rs!=null) try{ rs.close();} catch (SQLException e2) {}
			if(pstmt!=null) try{ pstmt.close();} catch (SQLException e2) {}
			if(conn!=null) try{ conn.close();} catch (SQLException e2) {}
	    }
	    return products;
	}
	
	// ��ǰ ���� �� ����¡ ó��
	public int getProductCount(Map<String, Object> map) {
	    int count = 0;
	    Connection conn = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;

	    try {
	        conn = ConnUtil.getConnection();
	        StringBuilder sql = new StringBuilder();
	        
	        sql.append("SELECT COUNT(*) FROM product WHERE DELETESTATE = 'N' ");
	        if (map.get("keyField") != null) {
	            if ("product_type".equals(map.get("keyField"))) {
	                sql.append("AND LOWER(product_type) LIKE LOWER(?) ");
	            } else if ("product_name".equals(map.get("keyField"))) {
	                sql.append("AND LOWER(product_name) LIKE LOWER(?) ");
	            } else if ("all".equals(map.get("keyField"))) {
	                sql.append("AND (LOWER(product_type) LIKE LOWER(?) OR LOWER(product_name) LIKE LOWER(?)) ");
	            }
	        }
	        pstmt = conn.prepareStatement(sql.toString());
	        int index = 1;
	        if (map.get("keyField") != null) {
	            if ("product_type".equals(map.get("keyField")) || "product_name".equals(map.get("keyField"))) {
	                pstmt.setString(index++, "%" + map.get("keyWord") + "%");
	            } else if ("all".equals(map.get("keyField"))) {
	                pstmt.setString(index++, "%" + map.get("keyWord") + "%");
	                pstmt.setString(index++, "%" + map.get("keyWord") + "%");
	            }
	        }
	        rs = pstmt.executeQuery();
	        if (rs.next()) {
	            count = rs.getInt(1);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	    	if(rs!=null) try{ rs.close();} catch (SQLException e2) {}
			if(pstmt!=null) try{ pstmt.close();} catch (SQLException e2) {}
			if(conn!=null) try{ conn.close();} catch (SQLException e2) {}
	    }
	    return count;
	}
	
	// ���̵� ã��
	public String findUser(String name, String phone) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String userID = null;
		
		try {
			conn = ConnUtil.getConnection();
			String sql = "select userID from member where phone = ? and name = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, phone);
			pstmt.setString(2, name);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				userID = rs.getString("userID");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs!=null) try{ rs.close();} catch (SQLException e2) {}
			if(pstmt!=null) try{ pstmt.close();} catch (SQLException e2) {}
			if(conn!=null) try{ conn.close();} catch (SQLException e2) {}
		}
		
		return userID;
	}
	
	// ��й�ȣ ã��
	public String findPwd(String name, String userID) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String pwd = null;
		
		try {
			conn = ConnUtil.getConnection();
			String sql = "select pwd from member where name = ? and userID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, name);
			pstmt.setString(2, userID);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				pwd = rs.getString("pwd");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs!=null) try{ rs.close();} catch (SQLException e2) {}
			if(pstmt!=null) try{ pstmt.close();} catch (SQLException e2) {}
			if(conn!=null) try{ conn.close();} catch (SQLException e2) {}
		}
		
		return pwd;
	}	
	
	// ȸ������ 
	public int addMember(MemberVO memberVO) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int result = 0;
		
		try {
			conn = ConnUtil.getConnection();
			String sql = "INSERT INTO MEMBER(userID, pwd, name, postnum, address1, address2, phone, grade) VALUES(?, ?, ?, ?, ?, ?, ?, 0)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memberVO.getUserID());
			pstmt.setString(2, memberVO.getPwd());
			pstmt.setString(3, memberVO.getName());
			pstmt.setString(4, memberVO.getPostnum());
			pstmt.setString(5, memberVO.getAddress1());
			pstmt.setString(6, memberVO.getAddress2());
			pstmt.setString(7, memberVO.getPhone());
			
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs!=null) try{ rs.close();} catch (SQLException e2) {}
			if(pstmt!=null) try{ pstmt.close();} catch (SQLException e2) {}
			if(conn!=null) try{ conn.close();} catch (SQLException e2) {}
		}
		
		return result;
	}
	
	// �α��� ó��
	public MemberVO login(String userID) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		MemberVO memberVO = null;
		
		try {
			conn = ConnUtil.getConnection();
			String sql = "select * from member where userID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userID);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				memberVO = new MemberVO();
				memberVO.setUserID(rs.getString("userID"));
				memberVO.setPwd(rs.getString("pwd"));
				memberVO.setName(rs.getString("name"));
				memberVO.setPostnum(rs.getString("postnum"));
				memberVO.setAddress1(rs.getString("address1"));
				memberVO.setAddress2(rs.getString("address2"));
				memberVO.setPhone(rs.getString("phone"));
				memberVO.setGrade(rs.getInt("grade"));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs!=null) try{ rs.close();} catch (SQLException e2) {}
			if(pstmt!=null) try{ pstmt.close();} catch (SQLException e2) {}
			if(conn!=null) try{ conn.close();} catch (SQLException e2) {}
		}
		
		return memberVO;
	}
	
	// ��й�ȣ Ȯ��
	public MemberVO getMember(String userID) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		MemberVO memberVO = null;
		
		try {
			conn = ConnUtil.getConnection();
			String sql = "select pwd from member where userID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userID);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				memberVO = new MemberVO();
				memberVO.setPwd(rs.getString("pwd"));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs!=null) try{ rs.close();} catch (SQLException e2) {}
			if(pstmt!=null) try{ pstmt.close();} catch (SQLException e2) {}
			if(conn!=null) try{ conn.close();} catch (SQLException e2) {}
		}
		
		return memberVO;
	}
	
	// ID �ߺ��˻�
	public MemberVO getUser(String userID) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		MemberVO memberVO = null;
		
		try {
			conn = ConnUtil.getConnection();
			String sql = "select * from member where userID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userID);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				memberVO = new MemberVO();
				memberVO.setUserID(rs.getString("userID"));
				memberVO.setPwd(rs.getString("pwd"));
				memberVO.setName(rs.getString("name"));
				memberVO.setPostnum(rs.getString("postnum"));
				memberVO.setAddress1(rs.getString("address1"));
				memberVO.setAddress2(rs.getString("address2"));
				memberVO.setPhone(rs.getString("phone"));
				memberVO.setGrade(rs.getInt("grade"));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs!=null) try{ rs.close();} catch (SQLException e2) {}
			if(pstmt!=null) try{ pstmt.close();} catch (SQLException e2) {}
			if(conn!=null) try{ conn.close();} catch (SQLException e2) {}
		}
		
		return memberVO;
	}
	
	// �޴��� �ߺ��˻�
	public MemberVO getPhone(String phone) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		MemberVO memberVO = null;
		
		try {
			conn = ConnUtil.getConnection();
			String sql = "select * from member where phone = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, phone);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				memberVO = new MemberVO();
				memberVO.setUserID(rs.getString("userID"));
				memberVO.setPwd(rs.getString("pwd"));
				memberVO.setName(rs.getString("name"));
				memberVO.setPostnum(rs.getString("postnum"));
				memberVO.setAddress1(rs.getString("address1"));
				memberVO.setAddress2(rs.getString("address2"));
				memberVO.setPhone(rs.getString("phone"));
				memberVO.setGrade(rs.getInt("grade"));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs!=null) try{ rs.close();} catch (SQLException e2) {}
			if(pstmt!=null) try{ pstmt.close();} catch (SQLException e2) {}
			if(conn!=null) try{ conn.close();} catch (SQLException e2) {}
		}
		
		return memberVO;
	}
	
	// �̹���(���� ���ε�)
	public ProductVO getProductImage(int product_code) {
	    Connection conn = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    ProductVO productVO = null;

	    try {
	        conn = ConnUtil.getConnection();
	        String sql = "SELECT * FROM product WHERE product_code = ?";
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setInt(1, product_code);
	        rs = pstmt.executeQuery();

	        if (rs.next()) {
	            productVO = new ProductVO();
	            productVO.setProduct_code(rs.getInt("product_code"));
	            productVO.setImages1(rs.getString("images1"));
	            // �ٸ� �ʵ�鵵 ����
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        if (rs != null) try { rs.close(); } catch (SQLException e2) {}
	        if (pstmt != null) try { pstmt.close(); } catch (SQLException e2) {}
	        if (conn != null) try { conn.close(); } catch (SQLException e2) {}
	    }

	    return productVO;
	}
	
	// productDetail ��ǰ
	public ProductVO productDetail(String product_code) {
	    Connection conn = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    ProductVO productVO = null;

	    try {
	        conn = ConnUtil.getConnection();
	        String sql = "SELECT * FROM product WHERE product_code = ?";
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, product_code);
	        rs = pstmt.executeQuery();

	        if (rs.next()) {
	            productVO = new ProductVO();
	            productVO.setProduct_code(rs.getInt("product_code"));
	            productVO.setProduct_name(rs.getString("product_name"));
	            productVO.setProduct_price(rs.getInt("product_price"));
	            productVO.setProduct_type(rs.getString("product_type"));
	            productVO.setImages1(rs.getString("images1"));
	            productVO.setImages2(rs.getString("images2"));
	            productVO.setDeleteState(rs.getString("deleteState"));
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        if (rs != null) try { rs.close(); } catch (SQLException e2) {}
	        if (pstmt != null) try { pstmt.close(); } catch (SQLException e2) {}
	        if (conn != null) try { conn.close(); } catch (SQLException e2) {}
	    }

	    return productVO;
	}
	
	// productDetail �ɼ�
	public List<ProductOptionVO> getproductOption(String product_code) {
	    Connection conn = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    List<ProductOptionVO> list = new ArrayList<ProductOptionVO>();
	    try {
	        conn = ConnUtil.getConnection();
	        String sql = "SELECT * FROM product_option WHERE product_code = ?";
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, product_code);
	        rs = pstmt.executeQuery();
	        while (rs.next()) {
	            ProductOptionVO vo = new ProductOptionVO();
	            
	            vo.setProduct_code(rs.getInt("product_code"));
	            vo.setProduct_size(rs.getString("product_size"));
	            vo.setQuantity(rs.getInt("quantity"));
	            list.add(vo);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        if (rs != null) try { rs.close(); } catch (SQLException e2) {}
	        if (pstmt != null) try { pstmt.close(); } catch (SQLException e2) {}
	        if (conn != null) try { conn.close(); } catch (SQLException e2) {}
	    }
	    return list;
	}
	
	// 2023. 06.25
	// mycartForm
	public List<CartVO> cartList(String userID) {
		
		Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<CartVO> list = new ArrayList<CartVO>();
        
        try {
        	
        	conn = ConnUtil.getConnection();
            String sql = "SELECT * FROM cart WHERE userID = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, userID);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                CartVO vo = new CartVO();
                
                vo.setUserID(rs.getString("userID"));
                vo.setPhone(rs.getString("phone"));
                vo.setProduct_code(rs.getInt("product_code"));
                vo.setProduct_size(rs.getString("product_size"));
                vo.setQuantity(rs.getInt("quantity"));
                vo.setProduct_name(rs.getString("product_name"));
                vo.setProduct_price(rs.getInt("product_price"));
                vo.setDelivery(rs.getString("delivery"));
                vo.setCartNum(rs.getInt("cartNum"));
                vo.setImages2(rs.getString("images2"));
                
                list.add(vo);
            }
        	
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e2) {}
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e2) {}
            if (conn != null) try { conn.close(); } catch (SQLException e2) {}
        }
        return list;
    }
	
	 // �������� ���� ��������
	public ProductVO getDeleteState2(int product_code) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ProductVO vo = new ProductVO();
        try {
            conn = ConnUtil.getConnection();
            String sql = "SELECT deleteState FROM product WHERE product_code = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, product_code);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                vo.setDeleteState(rs.getString("deleteState"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e2) {}
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e2) {}
            if (conn != null) try { conn.close(); } catch (SQLException e2) {}
        }
        return vo;
    }
	
	// ��ٱ��Ͽ��� ��� Ȯ���ϱ� 
	public ProductOptionVO getProductOption3(int product_code, String product_size) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ProductOptionVO vo = new ProductOptionVO();
        try {
            conn = ConnUtil.getConnection();
            String sql = "SELECT * FROM product_option WHERE product_code = ? AND product_size = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, product_code);
            pstmt.setString(2, product_size);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                vo.setProduct_code(rs.getInt("product_code"));
                vo.setProduct_size(rs.getString("product_size"));
                vo.setQuantity(rs.getInt("quantity"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e2) {}
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e2) {}
            if (conn != null) try { conn.close(); } catch (SQLException e2) {}
        }
        return vo;
    }
	
	// ��ٱ��Ͽ� �ִ� ��ǰ �� deleteState ���� 'Y'�� ��ǰ�� �ִ��� Ȯ���մϴ�.
	public String getDeleteState(int product_code) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String deleteState = null;
        try {
            conn = ConnUtil.getConnection();
            String sql = "SELECT deleteState FROM product WHERE product_code = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, product_code);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                deleteState = rs.getString("deleteState");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e2) {}
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e2) {}
            if (conn != null) try { conn.close(); } catch (SQLException e2) {}
        }
        return deleteState;
    }
	
	// 2023.06.26
	// ��ٱ��� �߰� 
	public CartVO addCart(CartVO cartVO) {
	    
	    Connection conn = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    try {
	        conn = ConnUtil.getConnection();
	        String sql = "INSERT INTO cart(userID, phone, product_size, quantity, product_price, product_code, product_name, cartNum, delivery, images2) VALUES(?, ?, ?, ?, ?, ?, ?, cart_num_seq.NEXTVAL, '������', ?)";
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, cartVO.getUserID());
	        pstmt.setString(2, cartVO.getPhone());
	        pstmt.setString(3, cartVO.getProduct_size());
	        pstmt.setInt(4, cartVO.getQuantity());
	        pstmt.setInt(5, cartVO.getProduct_price());
	        pstmt.setInt(6, cartVO.getProduct_code());
	        pstmt.setString(7, cartVO.getProduct_name());
	        pstmt.setString(8, cartVO.getImages2());
	        int result = pstmt.executeUpdate();
	        System.out.println("executeUpdate ���: " + result);
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        if (rs != null) try { rs.close(); } catch (SQLException e2) {}
	        if (pstmt != null) try { pstmt.close(); } catch (SQLException e2) {}
	        if (conn != null) try { conn.close(); } catch (SQLException e2) {}
	    }
	    return cartVO;
	}
	
	 // ��ٱ��Ͽ� ������ ��ǰ �ڵ�� ������ ��ǰ ũ���� ��ǰ�� �ִ��� Ȯ��
	public CartVO getCart(String userID, int product_code, String product_size) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        CartVO vo = new CartVO();
        try {
            conn = ConnUtil.getConnection();
            String sql = "SELECT * FROM cart WHERE userID=? AND product_code=? AND product_size=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, userID);
            pstmt.setInt(2, product_code);
            pstmt.setString(3, product_size);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                vo.setUserID(rs.getString("userID"));
                vo.setPhone(rs.getString("phone"));
                vo.setProduct_code(rs.getInt("product_code"));
                vo.setProduct_size(rs.getString("product_size"));
                vo.setQuantity(rs.getInt("quantity"));
                vo.setProduct_name(rs.getString("product_name"));
                vo.setProduct_price(rs.getInt("product_price"));
                vo.setDelivery(rs.getString("delivery"));
                vo.setCartNum(rs.getInt("cartNum"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e2) {}
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e2) {}
            if (conn != null) try { conn.close(); } catch (SQLException e2) {}
        }
        return vo;
    }
	
	// ������ ��ǰ�� ���� ��� ��ٱ��� ������ ������Ʈ 
	public void updateCart2(CartVO cartVO) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = ConnUtil.getConnection();
            String sql = "UPDATE cart SET quantity=? WHERE userID=? AND product_code=? AND product_size=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, cartVO.getQuantity());
            pstmt.setString(2, cartVO.getUserID());
            pstmt.setInt(3, cartVO.getProduct_code());
            pstmt.setString(4, cartVO.getProduct_size());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e2) {}
            if (conn != null) try { conn.close(); } catch (SQLException e2) {}
        }
    }
	
	// ��ٱ��� ��ü ����
	public void deleteAll(String userID) {
		 Connection conn = null;
	     PreparedStatement pstmt = null;
	     ResultSet rs = null;
	     
	     try {
	    	 conn = ConnUtil.getConnection();
	    	 String sql = "DELETE FROM cart WHERE userID = ?";
	         pstmt = conn.prepareStatement(sql);
	         pstmt.setString(1, userID);
	         pstmt.executeUpdate();
	         
	     } catch (SQLException e) {
	         e.printStackTrace();
	     } finally {
	         if (rs != null) try { rs.close(); } catch (SQLException e2) {}
	         if (pstmt != null) try { pstmt.close(); } catch (SQLException e2) {}
	         if (conn != null) try { conn.close(); } catch (SQLException e2) {}
	     }
	}
	
	// ��ٱ��� �κ� ����
	public void deletePart(String userID, int cartNum) {
		Connection conn = null;
	     PreparedStatement pstmt = null;
	     ResultSet rs = null;
	     
	     try {
	    	 
	    	 conn = ConnUtil.getConnection();
	    	 String sql = "DELETE FROM cart WHERE userID = ? and cartNum = ?";
	         pstmt = conn.prepareStatement(sql);
	         pstmt.setString(1, userID);
	         pstmt.setInt(2, cartNum);
	         pstmt.executeUpdate();
	    	 
	     } catch (SQLException e) {
	         e.printStackTrace();
	     } finally {
	         if (rs != null) try { rs.close(); } catch (SQLException e2) {}
	         if (pstmt != null) try { pstmt.close(); } catch (SQLException e2) {}
	         if (conn != null) try { conn.close(); } catch (SQLException e2) {}
	     }
		
	}
	
	// ��ٱ��� update �� orderForm���� �ѱ��
	public void updateCart(CartVO cartVO) {
		Connection conn = null;
	    PreparedStatement pstmt = null;
	     
	     try {
	    	 conn = ConnUtil.getConnection();
	    	 String sql = "UPDATE cart SET product_size = ?, quantity = ?, product_price = ?, product_code = ?, product_name = ? WHERE userID = ? AND cartNum = ? AND images2 = ?";
	            pstmt = conn.prepareStatement(sql);
	            pstmt.setString(1, cartVO.getProduct_size());
	            pstmt.setInt(2, cartVO.getQuantity());
	            pstmt.setInt(3, cartVO.getProduct_price());
	            pstmt.setInt(4, cartVO.getProduct_code());
	            pstmt.setString(5, cartVO.getProduct_name());
	            pstmt.setString(6, cartVO.getUserID());
	            pstmt.setInt(7, cartVO.getCartNum());
	            pstmt.setString(8, cartVO.getImages2());
	            pstmt.executeUpdate();
	    	 
	     } catch (SQLException e) {
	         e.printStackTrace();
	     } finally {
	         if (pstmt != null) try { pstmt.close(); } catch (SQLException e2) {}
	         if (conn != null) try { conn.close(); } catch (SQLException e2) {}
	     }
		
	}
	
	// ���� �κп��� �ֹ��� ���� �������� 
	public void insertOrder(OrderVO orderVO) {
		 Connection conn = null;
	     PreparedStatement pstmt = null;
	     ResultSet rs = null;
	     try {
	    	 conn = ConnUtil.getConnection();
	    	 String sql = "INSERT INTO orders(order_num, userID, phone, order_phone, order_name, order_address1, order_address2, postnum, order_memo, paymethod, totalbill) VALUES(order_num_seq.nextval, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"; 
	    	 //pstmt = conn.prepareStatement(sql); 
	    	 pstmt = conn.prepareStatement(sql, new String[] {"order_num"});
	    	 pstmt.setString(1, orderVO.getUserID());
	         pstmt.setString(2, orderVO.getPhone());
	         pstmt.setString(3, orderVO.getOrder_phone());
	         pstmt.setString(4, orderVO.getOrder_name());
	         pstmt.setString(5, orderVO.getOrder_address1());
	         pstmt.setString(6, orderVO.getOrder_address2());
	         pstmt.setString(7, orderVO.getPostnum());
	         pstmt.setString(8, orderVO.getOrder_memo());
	         pstmt.setString(9, orderVO.getPaymethod());
	         pstmt.setInt(10, orderVO.getTotalbill());
	         pstmt.executeUpdate();
	         
	         // ������ order_num ���� �����ɴϴ�.
	         rs = pstmt.getGeneratedKeys();
	         if (rs.next()) {
	             int orderNum = rs.getInt(1);
	             orderVO.setOrder_num(orderNum);
	         }
	         
	     } catch (SQLException e) {
	         e.printStackTrace();
	     } finally {
	         if (pstmt != null) try { pstmt.close(); } catch (SQLException e2) {}
	         if (conn != null) try { conn.close(); } catch (SQLException e2) {}
	         if (rs != null) try { rs.close(); } catch (SQLException e2) {}
	     }
	}
	
	// ���� �κп��� ���� ��ǰ ���� �������� 
	public void insertOrderProduct(OrderProductVO orderProductVO) {
		Connection conn = null;
	     PreparedStatement pstmt = null;
	     try {
	    	 conn = ConnUtil.getConnection();
	    	 String sql = "INSERT INTO order_product(orderProduct_num, order_num, userID, phone, product_size, quantity, product_price, product_code, product_name, delivery, images2, order_date, order_state, buyComplete, refund) VALUES(orderproduct_num_seq.nextval, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, SYSDATE, '��� �غ���', 'N','N')";
	    	 pstmt = conn.prepareStatement(sql);
	         pstmt.setInt(1, orderProductVO.getOrder_num());
	         pstmt.setString(2, orderProductVO.getUserID());
	         pstmt.setString(3, orderProductVO.getPhone());
	         pstmt.setString(4, orderProductVO.getProduct_size());
	         pstmt.setInt(5, orderProductVO.getQuantity());
	         pstmt.setInt(6, orderProductVO.getProduct_price());
	         pstmt.setInt(7, orderProductVO.getProduct_code());
	         pstmt.setString(8, orderProductVO.getProduct_name());
	         pstmt.setString(9, orderProductVO.getDelivery());
	         pstmt.setString(10, orderProductVO.getImages2());
	         pstmt.executeUpdate();
	     } catch (SQLException e) {
	         e.printStackTrace();
	     } finally {
	         if (pstmt != null) try { pstmt.close(); } catch (SQLException e2) {}
	         if (conn != null) try { conn.close(); } catch (SQLException e2) {}
	     }
	}
	
	// db�� �ش� ��ٱ��� �׸� ���� ����� 
	public int deleteCart(CartVO cartVO) {
	    Connection conn = null;
	    PreparedStatement pstmt = null;
	    int result = 0;
	    try {
	        conn = ConnUtil.getConnection();
	        String sql = "DELETE FROM cart WHERE userID = ? AND cartNum = ?";
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, cartVO.getUserID());
	        pstmt.setInt(2, cartVO.getCartNum());
	        result = pstmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        if (pstmt != null) try { pstmt.close(); } catch (SQLException e2) {}
	        if (conn != null) try { conn.close(); } catch (SQLException e2) {}
	    }
	    return result;
	}
	
	// db�� �ش� ��ٱ��� �׸� ���� �����
	public List<ProductOptionVO> getproductOption2(String product_code, String product_size) {
	    Connection conn = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    List<ProductOptionVO> list = new ArrayList<>();
	    try {
	        conn = ConnUtil.getConnection();
	        String sql = "SELECT * FROM product_option WHERE product_code = ? AND product_size = ?";
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, product_code);
	        pstmt.setString(2, product_size);
	        rs = pstmt.executeQuery();
	        while (rs.next()) {
	            ProductOptionVO vo = new ProductOptionVO();
	            vo.setProduct_code(rs.getInt("product_code"));
	            vo.setProduct_size(rs.getString("product_size"));
	            vo.setQuantity(rs.getInt("quantity"));
	            list.add(vo);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        if (rs != null) try { rs.close(); } catch (SQLException e2) {}
	        if (pstmt != null) try { pstmt.close(); } catch (SQLException e2) {}
	        if (conn != null) try { conn.close(); } catch (SQLException e2) {}
	    }
	    return list;
	}
	
	// productCode int�� ���� Ȯ�� 
	public void updateQuantity(ProductOptionVO productOptionVO) {
	    Connection conn = null;
	    PreparedStatement pstmt = null;
	    try {
	        conn = ConnUtil.getConnection();
	        String sql = "UPDATE product_option SET quantity = ? WHERE product_code = ? AND product_size = ?";
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setInt(1, productOptionVO.getQuantity());
	        pstmt.setInt(2, productOptionVO.getProduct_code());
	        pstmt.setString(3, productOptionVO.getProduct_size());
	        pstmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        if (pstmt != null) try { pstmt.close(); } catch (SQLException e2) {}
	        if (conn != null) try { conn.close(); } catch (SQLException e2) {}
	    }
	}
	
	// ��� Ȯ�� 1�� �̻�
	public ProductOptionVO getProductOption4(String product_code, String product_size) {
	    Connection conn = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    ProductOptionVO vo = null;
	    try {
	        conn = ConnUtil.getConnection();
	        String sql = "SELECT * FROM product_option WHERE product_code = ? AND product_size = ?";
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, product_code);
	        pstmt.setString(2, product_size);
	        rs = pstmt.executeQuery();
	        if (rs.next()) {
	            vo = new ProductOptionVO();
	            vo.setProduct_code(rs.getInt("product_code"));
	            vo.setProduct_size(rs.getString("product_size"));
	            vo.setQuantity(rs.getInt("quantity"));
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        if (rs != null) try { rs.close(); } catch (SQLException e2) {}
	        if (pstmt != null) try { pstmt.close(); } catch (SQLException e2) {}
	        if (conn != null) try { conn.close(); } catch (SQLException e2) {}
	    }
	    return vo;
	}
	
	//ȸ�� ���� ����
	public int memberUpdate1(MemberVO memberVO) {
		 
		Connection conn = null;
		PreparedStatement pstmt = null;
		int result = 0;
		try {
			conn = ConnUtil.getConnection();
			String sql = "update member set pwd = ?, name = ?, postnum = ?, address1 = ?, address2 = ? where userID = ? and phone = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memberVO.getPwd());
			pstmt.setString(2, memberVO.getName());
			pstmt.setString(3, memberVO.getPostnum());
			pstmt.setString(4, memberVO.getAddress1());
			pstmt.setString(5, memberVO.getAddress2());
			pstmt.setString(6, memberVO.getUserID());
			pstmt.setString(7, memberVO.getPhone());
			result = pstmt.executeUpdate();
		}catch (SQLException e) {
		     e.printStackTrace();
		} finally {
		    if (pstmt != null) try { pstmt.close(); } catch (SQLException e2) {}
		    if (conn != null) try { conn.close(); } catch (SQLException e2) {}
		}
		return result;
	}
	
	// ȸ�� Ż��
	public int memberDel(String userID) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int result = 0;
		try {
			conn = ConnUtil.getConnection();
			String sql = "delete from member where userID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userID);
			result = pstmt.executeUpdate();
		}catch (SQLException e) {
		     e.printStackTrace();
		} finally {
		    if (pstmt != null) try { pstmt.close(); } catch (SQLException e2) {}
		    if (conn != null) try { conn.close(); } catch (SQLException e2) {}
		}
		return result;
	}
	// ���ų��� ������ 
	public int getProductCount2(Map<String, Object> map) {
		ResultSet rs = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		int count = 0;
		try {
			conn = ConnUtil.getConnection();
			 StringBuilder sql = new StringBuilder();
		        
		        sql.append("SELECT COUNT(*) FROM order_product WHERE userID = ? ");
		        if (map.get("keyField") != null) {
		            if ("product_name".equals(map.get("keyField"))) {
		                sql.append("AND LOWER(product_name) LIKE LOWER(?) ");
		            } else if ("order_state".equals(map.get("keyField"))) {
		                sql.append("AND LOWER(order_state) LIKE LOWER(?) ");
		            }
		        }
		        pstmt = conn.prepareStatement(sql.toString());
		        int index = 1;
		        pstmt.setString(index++, (String) map.get("userID"));
		        if (map.get("keyField") != null) {
		            if ("product_name".equals(map.get("keyField"))) {
		                pstmt.setString(index++, "%" + map.get("keyWord") + "%");
		            } else if ("order_state".equals(map.get("keyField"))) {
		                pstmt.setString(index++, "%" + map.get("keyWord") + "%");
		            }
		        }
	        rs = pstmt.executeQuery();
	        if (rs.next()) {
	            count = rs.getInt(1);
	        }
		}catch (SQLException e) {
		     e.printStackTrace();
		} finally {
		    if (pstmt != null) try { pstmt.close(); } catch (SQLException e2) {}
		    if (conn != null) try { conn.close(); } catch (SQLException e2) {}
		    if (rs != null) try { rs.close(); } catch (SQLException e2) {}
		}
		return count;
	}
	
	// ���ų��� ������
	public List<OrderProductVO> getBuyProduct(Map<String, Object> map){
		ResultSet rs = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		List<OrderProductVO> orderProducts  = new ArrayList<>();
		try {
			conn = ConnUtil.getConnection();
			String sql = "SELECT * FROM (SELECT rownum rnum, p.* FROM (SELECT * FROM order_product WHERE userID = ?";
	        if (map.get("keyField") != null) {
	            if (map.get("keyField").equals("product_name")) {
	                sql += " AND LOWER(product_name) LIKE LOWER(?)";
	            } else if (map.get("keyField").equals("order_state")) {
	                sql += " AND LOWER(order_state) LIKE LOWER(?)";
	            }
	        }
	        sql += " ORDER BY order_date DESC) p) WHERE RNUM >= ? AND RNUM <= ?";
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, (String) map.get("userID"));
	        int index = 2;
	        if (map.get("keyField") != null) {
	            if (map.get("keyField").equals("product_name") || map.get("keyField").equals("order_state")) {
	                pstmt.setString(index++, "%" + map.get("keyWord") + "%");
	            }
	        }
	        pstmt.setInt(index++, (Integer) map.get("start"));
	        pstmt.setInt(index++, (Integer) map.get("end"));
	        rs = pstmt.executeQuery();
	        while (rs.next()) {
	        	
	            OrderProductVO orderProduct = new OrderProductVO();
	            
	            orderProduct.setUserID(rs.getString("userID"));
	            orderProduct.setBuyComplete(rs.getString("buyComplete"));
	            orderProduct.setDelivery(rs.getString("delivery"));
	            orderProduct.setImages2(rs.getString("images2"));
	            java.sql.Date sqlDate = rs.getDate("order_date");
	            java.util.Date utilDate = new java.util.Date(sqlDate.getTime());
	            orderProduct.setOrder_date(utilDate);
	            orderProduct.setOrder_num(rs.getInt("order_num"));
	            orderProduct.setOrder_state(rs.getString("order_state"));
	            orderProduct.setOrderProduct_num(rs.getInt("orderProduct_num"));
	            orderProduct.setPhone(rs.getString("phone"));
	            orderProduct.setProduct_code(rs.getInt("product_code"));
	            orderProduct.setProduct_name(rs.getString("product_name"));
	            orderProduct.setProduct_price(rs.getInt("product_price"));
	            orderProduct.setProduct_size(rs.getString("product_size"));
	            orderProduct.setQuantity(rs.getInt("quantity"));
	            orderProduct.setRefund(rs.getString("refund"));

	            orderProducts.add(orderProduct);
	        }
		}catch (SQLException e) {
		     e.printStackTrace();
		} finally {
		    if (pstmt != null) try { pstmt.close(); } catch (SQLException e2) {}
		    if (conn != null) try { conn.close(); } catch (SQLException e2) {}
		    if (rs != null) try { rs.close(); } catch (SQLException e2) {}
		}
		return orderProducts;
	}
	
	// �ֹ����� �� orderVO �κ�
	public List<OrderVO> getOrders(String userID, int order_num){
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<OrderVO> orderVO  = new ArrayList<>();
		try {
			conn = ConnUtil.getConnection();
	        String sql = "SELECT * FROM orders WHERE userID = ? AND order_num = ?";
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, userID);
	        pstmt.setInt(2, order_num);
	        rs = pstmt.executeQuery();

	        while (rs.next()) {
	            OrderVO order = new OrderVO();
	            order.setOrder_num(rs.getInt("order_num"));
	            order.setUserID(rs.getString("userID"));
	            order.setOrder_address1(rs.getString("order_address1"));
	            order.setOrder_address2(rs.getString("order_address2"));
	            order.setOrder_memo(rs.getString("order_memo"));
	            order.setOrder_name(rs.getString("order_name"));
	            order.setOrder_phone(rs.getString("order_phone"));
	            order.setPaymethod(rs.getString("paymethod"));
	            order.setPhone(rs.getString("phone"));
	            order.setPostnum(rs.getString("postnum"));
	            order.setTotalbill(rs.getInt("totalbill"));
	            orderVO.add(order);
	        }
		}catch (SQLException e) {
		     e.printStackTrace();
		} finally {
		    if (pstmt != null) try { pstmt.close(); } catch (SQLException e2) {}
		    if (conn != null) try { conn.close(); } catch (SQLException e2) {}
		    if (rs != null) try { rs.close(); } catch (SQLException e2) {}
		}
		return orderVO;
	}
	
	// �ֹ����� �� ProductOrderVO �κ�
	public List<OrderProductVO> getOrderProducts(String userID, int order_num){
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<OrderProductVO> orderProducts  = new ArrayList<>();
		try {
			conn = ConnUtil.getConnection();
			String sql = "SELECT * FROM order_product WHERE userID = ? AND order_num = ?";
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, userID);
	        pstmt.setInt(2, order_num);
	        rs = pstmt.executeQuery();
	        
	        while (rs.next()) {
	        	OrderProductVO orderProduct = new OrderProductVO();
	        	orderProduct.setBuyComplete(rs.getString("buyComplete"));
	        	orderProduct.setDelivery(rs.getString("delivery"));
	        	orderProduct.setImages2(rs.getString("images2"));
	        	orderProduct.setOrder_date(rs.getDate("order_date"));
	        	orderProduct.setOrder_num(rs.getInt("order_num"));
	        	orderProduct.setOrder_state(rs.getString("order_state"));
	        	orderProduct.setOrderProduct_num(rs.getInt("orderProduct_num"));
	        	orderProduct.setPhone(rs.getString("phone"));
	        	orderProduct.setProduct_code(rs.getInt("product_code"));
	        	orderProduct.setProduct_name(rs.getString("product_name"));
	        	orderProduct.setProduct_price(rs.getInt("product_price"));
	        	orderProduct.setProduct_size(rs.getString("product_size"));
	        	orderProduct.setQuantity(rs.getInt("quantity"));
	        	orderProduct.setRefund(rs.getString("refund"));
	        	orderProduct.setUserID(rs.getString("userID"));
	        	orderProducts.add(orderProduct);
	        }
		}catch (SQLException e) {
		     e.printStackTrace();
		} finally {
		    if (pstmt != null) try { pstmt.close(); } catch (SQLException e2) {}
		    if (conn != null) try { conn.close(); } catch (SQLException e2) {}
		    if (rs != null) try { rs.close(); } catch (SQLException e2) {}
		}
		return orderProducts;
	}
	
	// ���� Ȯ�� complete
	public void updateBuyComplete(String userID, int orderProduct_num) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = ConnUtil.getConnection();
			String sql = "UPDATE order_product SET buyComplete = 'Y' WHERE userID = ? AND orderProduct_num = ?";
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, userID);
	        pstmt.setInt(2, orderProduct_num);
	        pstmt.executeUpdate();
			
		}catch (SQLException e) {
		     e.printStackTrace();
		} finally {
		    if (pstmt != null) try { pstmt.close(); } catch (SQLException e2) {}
		    if (conn != null) try { conn.close(); } catch (SQLException e2) {}
		    
		}
		
	}
	
	// ȯ��
	public void updateRefundConfirm(String userID, int orderProduct_num, String order_state) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = ConnUtil.getConnection();
			String sql = "UPDATE order_product SET refund = 'Y', order_state = ? WHERE userID = ? AND orderProduct_num = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, order_state);
			pstmt.setString(2, userID);
			pstmt.setInt(3, orderProduct_num);
			pstmt.executeUpdate();
		} catch (SQLException e) {
		     e.printStackTrace();
		} finally {
		    if (pstmt != null) try { pstmt.close(); } catch (SQLException e2) {}
		    if (conn != null) try { conn.close(); } catch (SQLException e2) {}
		    
		}
	}
	
	
	
	
}
