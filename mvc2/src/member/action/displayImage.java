package member.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StreamUtils;

import member.model.ProductVO;
import member.model.memberDAO;


public class displayImage implements CommandAction {
	 
	// �̹��� ���ε� ��ǰ�κи�
	@Override
	public String requestPro(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		 
		System.out.println("�̹��� ���ε� Ȯ��");
		 
		int product_code = Integer.parseInt(request.getParameter("product_code"));
		System.out.println("product_code : " + product_code);
	    memberDAO dao = memberDAO.getInstance();
	    ProductVO productVO = dao.getProductImage(product_code);
		
		 //ProductImageVO productImVO = new ProductImageVO();
	    String filePath = "C:\\Users\\Gram\\Desktop\\mvcproject\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp0\\wtpwebapps\\mvc2\\image\\uniform\\" + productVO.getImages1();
	    System.out.println(filePath);
	   
	     
		return null;
	}

}
