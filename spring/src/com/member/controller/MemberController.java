package com.member.controller;

import java.io.File;     
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Enumeration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest; 
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.util.StreamUtils;

import com.board.VO.BoardVO;
import com.board.paging.Paging;
import com.cart.VO.CartVO;
import com.member.VO.MemberVO;
import com.member.dao.MemberDAO;
import com.order.VO.OrderProductVO;
import com.order.VO.OrderVO;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import com.product.VO.ProductOptionVO;
import com.product.VO.ProductVO;
import com.tobesoft.xplatform.data.ColumnHeader;
import com.tobesoft.xplatform.data.DataSet;
import com.tobesoft.xplatform.data.DataSetList;
import com.tobesoft.xplatform.data.DataTypes;
import com.tobesoft.xplatform.data.PlatformData;
import com.tobesoft.xplatform.data.VariableList;
import com.tobesoft.xplatform.tx.HttpPlatformRequest;
import com.tobesoft.xplatform.tx.HttpPlatformResponse;
import com.tobesoft.xplatform.tx.PlatformException;
import com.tobesoft.xplatform.tx.PlatformType;


@Controller
public class MemberController {
		
	@Autowired
	private MemberDAO memberDAO;
	
	// �ֹ� ����
	@RequestMapping({ "/member/productBuy.do" })
	public void productBuy(HttpServletRequest request, HttpServletResponse response) throws PlatformException {
		
		String userID = request.getParameter("userID");
		System.out.println("userID : " + userID);
		
	    System.out.println("�ֹ� ����");
	    
	    PlatformData pdata = new PlatformData();

		int nErrorCode = 0;
		String strErrorMsg = "START";

		int totalCount  = 0;
		
		try {
			// create HttpPlatformRequest for receive data from client
			HttpPlatformRequest req 
			     = new HttpPlatformRequest(request); 
			System.out.println("1");
			req.receiveData();							
			pdata = req.getData();
			DataSetList datasetlist = new DataSetList();
			DataSet ds1 = pdata.getDataSet("test");
			 System.out.println("2");
			int pageNum = Integer.parseInt(ds1.getString(0, "pageNum"));
			int contentPerPage = Integer.parseInt(ds1.getString(0, "contentPerPage"));
			int pageSize = Integer.parseInt(ds1.getString(0, "pageSize"));
			String keyField = ds1.getString(0, "keyField");
			String keyWord = ds1.getString(0, "keyWord");

			System.out.println("3");
			int startCount;
			int endCount;
					
			startCount = ((pageNum - 1) * pageSize + 1);
			endCount = (pageNum * pageSize);
			
			DataSet ds = new DataSet("orderProductVO");
			
			ds.addColumn("orderProduct_num", DataTypes.INT, 4);
			ds.addColumn("order_num", DataTypes.INT, 4);
			ds.addColumn("userID", DataTypes.STRING,  256);
			ds.addColumn("phone", DataTypes.STRING,  256);
			ds.addColumn("product_code", DataTypes.INT,  400);
			ds.addColumn("product_size", DataTypes.STRING,  400);
			ds.addColumn("quantity", DataTypes.INT, 256);
			ds.addColumn("product_name", DataTypes.STRING,  400);
			ds.addColumn("product_price", DataTypes.INT, 999999999);
			ds.addColumn("order_state", DataTypes.STRING,  400);
			ds.addColumn("delivery", DataTypes.STRING,  256);
			ds.addColumn("buyComplete", DataTypes.STRING,  256);
			ds.addColumn("refund", DataTypes.STRING,  256);
			ds.addColumn("order_date", DataTypes.DATE_TIME, 256);
			ds.addColumn("images2", DataTypes.STRING,  256);
			
			System.out.println("4");
			OrderProductVO orderProductVO;

			HashMap<String, Object> map = new HashMap<>();
			map.put("pageNum", pageNum);
			map.put("startCount", startCount);
			map.put("endCount", endCount);
			map.put("keyField", keyField);
			map.put("keyWord", keyWord);
			map.put("userID", userID);
			
			totalCount = this.memberDAO.getProductCount2(map);
			System.out.println("totalCount " +totalCount);
			
			List<OrderProductVO> list = null;
			if (totalCount > 0) {
				System.out.println("list : " + list);
				list = this.memberDAO.getBuyProduct(map);
			} else {
				list = Collections.emptyList();
			}
			
			for (int i=0; i<list.size(); i++) {
				
			    int row = ds.newRow(); // new Row feed          * x-platform���� addRow() �ڹٿ����� newRow()
			    
			    orderProductVO = list.get(i);
			    ds.set(row, "orderProduct_num", orderProductVO.getOrderProduct_num());
			    ds.set(row, "order_num", orderProductVO.getOrder_num());
			    ds.set(row, "userID", orderProductVO.getUserID());
			    ds.set(row, "phone", orderProductVO.getPhone());
			    ds.set(row, "product_code", orderProductVO.getProduct_code());
			    ds.set(row, "product_size", orderProductVO.getProduct_size());
			    ds.set(row, "quantity", orderProductVO.getQuantity());
			    ds.set(row, "product_name", orderProductVO.getProduct_name());
			    ds.set(row, "product_price", orderProductVO.getProduct_price());
			    ds.set(row, "order_state", orderProductVO.getOrder_state());
			    ds.set(row, "delivery", orderProductVO.getDelivery());
			    ds.set(row, "buyComplete", orderProductVO.getBuyComplete());
			    ds.set(row, "refund", orderProductVO.getRefund());
			    ds.set(row, "order_date", orderProductVO.getOrder_date());
			    ds.set(row, "images2", orderProductVO.getImages2());
			    
			}
			
			int totalPage = (int)Math.ceil((double)totalCount/pageSize);
			if (totalPage == 0) {
				totalPage = 1;
			}
			if (pageNum > totalPage) {
				pageNum = totalPage;
			}
		

			int startPage = (pageNum - 1) / contentPerPage * contentPerPage + 1;
			int endPage = startPage + contentPerPage - 1;
			if (endPage > totalPage) {
				endPage = totalPage;
			}
			System.out.println("�˻��� �� ���� = "+totalCount);
			System.out.println("���� ������ = "+pageNum);
			System.out.println("���������� �� ����  = "+pageSize);
			System.out.println("�Ѹ��� �� ������ ���� = "+contentPerPage);
			System.out.println("��ü ������ ������ = "+totalPage);
			System.out.println("���� ī��Ʈ  = "+startCount);
			System.out.println("������ ī��Ʈ = "+endCount);
			System.out.println("���� ������ = "+startPage);
			System.out.println("������ ������ = "+endPage);
			System.out.println("Ű�ʵ� = " + keyField);
			System.out.println("Ű���� = " + keyWord);
			
			DataSet dsPaging = new DataSet("test");			
			    
			// DataSet Column setting
			dsPaging.addColumn("pageNum", DataTypes.INT, 4);
			dsPaging.addColumn("startPage", DataTypes.INT, 4);
			dsPaging.addColumn("endPage", DataTypes.STRING,  256);
			dsPaging.addColumn("totalPage", DataTypes.INT, 4);
			dsPaging.addColumn("count", DataTypes.INT, 4);
			dsPaging.addColumn("contentPerPage", DataTypes.INT, 4);
			dsPaging.addColumn("pageSize", DataTypes.INT, 4);
			dsPaging.addColumn("keyField", DataTypes.STRING, 256);
			dsPaging.addColumn("keyWord", DataTypes.STRING, 256);
				
			dsPaging.addColumn("startCount", DataTypes.INT, 4);
			dsPaging.addColumn("endCount", DataTypes.INT, 4);
			dsPaging.newRow();
				
			dsPaging.set(0, "pageNum", pageNum); 
			dsPaging.set(0, "startPage", startPage);
			dsPaging.set(0, "endPage", endPage);
			dsPaging.set(0, "count", totalCount);
			dsPaging.set(0, "totalPage", totalPage);
			dsPaging.set(0, "contentPerPage", contentPerPage);
			dsPaging.set(0, "pageSize", pageSize);
			dsPaging.set(0, "startCount", startCount);
			dsPaging.set(0, "endCount", endCount);
			dsPaging.set(0, "keyField", keyField);
			dsPaging.set(0, "keyWord", keyWord);
			
			// pdata.addDataSet(ds);
			// pdata.addDataSet(dsPaging);
			datasetlist.add(ds);
			datasetlist.add(dsPaging);
		    pdata.setDataSetList(datasetlist);
		    // set the ErrorCode and ErrorMsg about success
		    nErrorCode = 0;
		    strErrorMsg = "SUCC";
		        
		} catch (Exception e) {
			// set the ErrorCode and ErrorMsg about fail
			nErrorCode = -1;
			//strErrorMsg = th.getMessage();
			strErrorMsg = e.getMessage();
			e.printStackTrace();
		} // try
		
		// save the ErrorCode and ErrorMsg for sending Client
		VariableList varList = pdata.getVariableList();
		
		
				
		varList.add("ErrorCode", nErrorCode);
		varList.add("ErrorMsg", strErrorMsg);
				
		// send the result data(XML) to Client
		HttpPlatformResponse res 
		    = new HttpPlatformResponse(response, PlatformType.CONTENT_TYPE_XML, "UTF-8");
		res.setData(pdata); 
		res.sendData();		// Send Data
	}
	
	
	// �ֹ� ���� ��
	@RequestMapping({ "/member/orderDetail.do" })
	public void orderDetail(@RequestParam("orderProduct_num") int orderProduct_num,
			@RequestParam("order_num") int order_num, @RequestParam("userID") String userID, HttpServletRequest request, HttpServletResponse response) throws PlatformException {
		
		
		System.out.println("�ֹ� ���� ��");
		System.out.println("userID: " + userID);
		System.out.println("order_num " + order_num);
		System.out.println("orderProduct_num " + orderProduct_num);
		PlatformData pdata = new PlatformData();

	    int nErrorCode = 0;
	    String strErrorMsg = "START";

	    try {
	    	System.out.println("1");
	    	
	    	HttpPlatformRequest req = new HttpPlatformRequest(request); 
	    	System.out.println("2");
	    	try {
	    	    req.receiveData();
	    	} catch (Exception e) {
	    	    e.printStackTrace();
	    	}    
			pdata = req.getData();
			System.out.println("3");
			DataSet ds = pdata.getDataSet("orderVO");
			for(int i=0; i<pdata.getDataSetList().size(); i++) {
				System.out.println(" alias >>> : " + pdata.getDataSetList().get(i).getAlias());
			}
			

			List<OrderVO> orderVO = this.memberDAO.getOrders(userID, order_num);
			OrderVO vo = orderVO.get(0);
			ds.set(0, "order_num", vo.getOrder_num());
			ds.set(0, "userID", vo.getUserID());
			ds.set(0, "phone", vo.getPhone());
			ds.set(0, "order_phone", vo.getOrder_phone());
			ds.set(0, "order_name", vo.getOrder_name());
			ds.set(0, "order_address1", vo.getOrder_address1());
			ds.set(0, "order_address2", vo.getOrder_address2());
			ds.set(0, "postnum", vo.getPostnum());
			ds.set(0, "order_memo", vo.getOrder_memo());
			ds.set(0, "paymethod", vo.getPaymethod());
			ds.set(0, "totalbill", vo.getTotalbill());
			System.out.println("order_num: " + order_num);
			
			DataSet ds1 = pdata.getDataSet("orderProductVO");
			
			List<OrderProductVO> orderProductVO = this.memberDAO.getOrderProducts(userID, order_num, orderProduct_num);
			
			ds1.clearData();
			
			for(int i = 0; i < orderProductVO.size(); i++) {
				
			OrderProductVO opvo = orderProductVO.get(i);
			ds1.newRow();
			
			ds1.set(i, "orderProduct_num", opvo.getOrderProduct_num());
		    ds1.set(i, "order_num", opvo.getOrder_num());
		    ds1.set(i, "userID", opvo.getUserID());
		    ds1.set(i, "phone", opvo.getPhone());
		    ds1.set(i, "product_code", opvo.getProduct_code());
		    ds1.set(i, "product_size", opvo.getProduct_size());
		    ds1.set(i, "quantity", opvo.getQuantity());
		    ds1.set(i, "product_name", opvo.getProduct_name());
		    ds1.set(i, "product_price", opvo.getProduct_price());
		    ds1.set(i, "delivery", opvo.getDelivery());
		    ds1.set(i, "order_date", opvo.getOrder_date());
		    ds1.set(i, "order_state", opvo.getOrder_state());
		    ds1.set(i, "buyComplete", opvo.getBuyComplete());
		    ds1.set(i, "refund", opvo.getRefund());
			
			System.out.println("orderProduct_num list" +opvo.getOrderProduct_num());
			}
			System.out.println("orderProduct_num " + orderProduct_num);
			
			
			nErrorCode = 0;
		    strErrorMsg = "SUCC";
		    
	    } catch (Exception th) {
			// set the ErrorCode and ErrorMsg about fail
			nErrorCode = -1;
			strErrorMsg = th.getMessage();
			th.printStackTrace();
			System.out.println("ERROR:" + strErrorMsg);
		} // try
		
		// save the ErrorCode and ErrorMsg for sending Client
		VariableList varList = pdata.getVariableList();
			
		varList.add("ErrorCode", nErrorCode);
		varList.add("ErrorMsg", strErrorMsg);
				
		// send the result data(XML) to Client
		HttpPlatformResponse res 
		    = new HttpPlatformResponse(response, PlatformType.CONTENT_TYPE_XML, "UTF-8");
		res.setData(pdata); 
		res.sendData();		// Send Data
	}
	
	
	@RequestMapping(value="/member/updateState.do", method=RequestMethod.POST)
	public void updateState(HttpServletRequest request, HttpServletResponse response) throws PlatformException {
		
		System.out.println("orderProduct_num update");
		
		PlatformData pdata = new PlatformData();

	    int nErrorCode = 0;
	    String strErrorMsg = "START";

	    try {
	    	System.out.println("1");
	    	
	    	HttpPlatformRequest req = new HttpPlatformRequest(request); 
	    	System.out.println("2");
	    	try {
	    	    req.receiveData();
	    	} catch (Exception e) {
	    	    e.printStackTrace();
	    	}    
			pdata = req.getData();
			System.out.println("3");
			DataSet ds = pdata.getDataSet("orderProductVO");
			
			for(int i=0; i<pdata.getDataSetList().size(); i++) {
				System.out.println(" alias >>> : " + pdata.getDataSetList().get(i).getAlias());
			}
			
			for(int i=0; i<ds.getRowCount(); i++) {
				String userID = ds.getString(i, "userID");
				int orderProduct_num = ds.getInt(i, "orderProduct_num");
				int order_num = ds.getInt(i, "order_num");
				String order_state = ds.getString(i, "order_state");
				System.out.println("userID " + ds.getString(i, "userID"));
				System.out.println("orderProduct_num" + ds.getString(i, "orderProduct_num"));
				System.out.println("order_num " + ds.getString(i, "order_num"));
				System.out.println("���� " + ds.getString(i, "order_state"));
				this.memberDAO.updateState(userID, order_num, orderProduct_num, order_state);
			}
			
		    
		    nErrorCode = 0;
		    strErrorMsg = "SUCC";
		    
	    } catch (Exception th) {
			// set the ErrorCode and ErrorMsg about fail
			nErrorCode = -1;
			strErrorMsg = th.getMessage();
			th.printStackTrace();
			System.out.println("ERROR:" + strErrorMsg);
		} // try
		
		// save the ErrorCode and ErrorMsg for sending Client
		VariableList varList = pdata.getVariableList();
			
		varList.add("ErrorCode", nErrorCode);
		varList.add("ErrorMsg", strErrorMsg);
				
		// send the result data(XML) to Client
		HttpPlatformResponse res 
		    = new HttpPlatformResponse(response, PlatformType.CONTENT_TYPE_XML, "UTF-8");
		res.setData(pdata); 
		res.sendData();		// Send Data
	}
		
	// ��ǰ ����Ʈ
	@RequestMapping(value = "/member/goodsList.do", method={RequestMethod.GET, RequestMethod.POST})
	public void goodsList(HttpServletRequest request, HttpServletResponse response) throws PlatformException {
		
		System.out.println("��ǰ ����Ʈ");
		
		PlatformData pdata = new PlatformData();

		int nErrorCode = 0;
		String strErrorMsg = "START";

		int totalCount  = 0;
		
		try {
			// create HttpPlatformRequest for receive data from client
			HttpPlatformRequest req 
			     = new HttpPlatformRequest(request); 
			System.out.println("1");
			req.receiveData();							
			pdata = req.getData();
			DataSetList datasetlist = new DataSetList();
			DataSet ds1 = pdata.getDataSet("test");
			 System.out.println("2");
			int pageNum = Integer.parseInt(ds1.getString(0, "pageNum"));
			int contentPerPage = Integer.parseInt(ds1.getString(0, "contentPerPage"));
			int pageSize = Integer.parseInt(ds1.getString(0, "pageSize"));
			String keyField = ds1.getString(0, "keyField");
			String keyWord = ds1.getString(0, "keyWord");
			
			System.out.println("3");
			int startCount;
			int endCount;
					
			startCount = ((pageNum - 1) * pageSize + 1);
			endCount = (pageNum * pageSize);
			
			DataSet ds = new DataSet("productVO");
			
			ds.addColumn("product_code", DataTypes.INT,  256);
			ds.addColumn("product_type", DataTypes.STRING,  256);
			ds.addColumn("product_name", DataTypes.STRING,  256);
			ds.addColumn("images1", DataTypes.STRING,  256);
			ds.addColumn("images2", DataTypes.STRING,  256);
			ds.addColumn("product_price", DataTypes.INT, 256);
			ds.addColumn("deleteState", DataTypes.STRING,  256);
			ds.addColumn("product_date", DataTypes.DATE, 256);
			System.out.println("4");
			ProductVO productVO;

			HashMap<String, Object> map = new HashMap<>();
			map.put("pageNum", pageNum);
			map.put("startCount", startCount);
			map.put("endCount", endCount);
			map.put("keyField", keyField);
			map.put("keyWord", keyWord);

			totalCount = this.memberDAO.getProductCount(map);
			System.out.println("totalCount " +totalCount);
			
			List<ProductVO> list = null;
			if (totalCount > 0) {
				System.out.println("list : " + list);
				list = this.memberDAO.getProducts(map);
			} else {
				list = Collections.emptyList();
			}

			for (int i=0; i<list.size(); i++) {
				
			    int row = ds.newRow(); // new Row feed          * x-platform���� addRow() �ڹٿ����� newRow()
			    
			    productVO = list.get(i);
			    ds.set(row, "product_code", productVO.getProduct_code());
			    ds.set(row, "product_type", productVO.getProduct_type());
			    ds.set(row, "product_name", productVO.getProduct_name());
			    ds.set(row, "images1", productVO.getImages1());
			    ds.set(row, "images2", productVO.getImages2());
			    ds.set(row, "product_date", productVO.getProduct_date());
			    ds.set(row, "product_price", productVO.getProduct_price());
			    ds.set(row, "deleteState", productVO.getDeleteState());

			}
			int totalPage = (int)Math.ceil((double)totalCount/pageSize);
			if (totalPage == 0) {
				totalPage = 1;
			}
			if (pageNum > totalPage) {
				pageNum = totalPage;
			}
		

			int startPage = (pageNum - 1) / contentPerPage * contentPerPage + 1;
			int endPage = startPage + contentPerPage - 1;
			if (endPage > totalPage) {
				endPage = totalPage;
			}
			System.out.println("�˻��� �� ���� = "+totalCount);
			System.out.println("���� ������ = "+pageNum);
			System.out.println("���������� �� ����  = "+pageSize);
			System.out.println("�Ѹ��� �� ������ ���� = "+contentPerPage);
			System.out.println("��ü ������ ������ = "+totalPage);
			System.out.println("���� ī��Ʈ  = "+startCount);
			System.out.println("������ ī��Ʈ = "+endCount);
			System.out.println("���� ������ = "+startPage);
			System.out.println("������ ������ = "+endPage);
			System.out.println("Ű�ʵ� = " + keyField);
			System.out.println("Ű���� = " + keyWord);
			
			DataSet dsPaging = new DataSet("test");			
			    
			// DataSet Column setting
			dsPaging.addColumn("pageNum", DataTypes.INT, 4);
			dsPaging.addColumn("startPage", DataTypes.INT, 4);
			dsPaging.addColumn("endPage", DataTypes.STRING,  256);
			dsPaging.addColumn("totalPage", DataTypes.INT, 4);
			dsPaging.addColumn("count", DataTypes.INT, 4);
			dsPaging.addColumn("contentPerPage", DataTypes.INT, 4);
			dsPaging.addColumn("pageSize", DataTypes.INT, 4);
			dsPaging.addColumn("keyField", DataTypes.STRING, 256);
			dsPaging.addColumn("keyWord", DataTypes.STRING, 256);
				
			dsPaging.addColumn("startCount", DataTypes.INT, 4);
			dsPaging.addColumn("endCount", DataTypes.INT, 4);
			dsPaging.newRow();
				
			dsPaging.set(0, "pageNum", pageNum); 
			dsPaging.set(0, "startPage", startPage);
			dsPaging.set(0, "endPage", endPage);
			dsPaging.set(0, "count", totalCount);
			dsPaging.set(0, "totalPage", totalPage);
			dsPaging.set(0, "contentPerPage", contentPerPage);
			dsPaging.set(0, "pageSize", pageSize);
			dsPaging.set(0, "startCount", startCount);
			dsPaging.set(0, "endCount", endCount);
			dsPaging.set(0, "keyField", keyField);
			dsPaging.set(0, "keyWord", keyWord);
			
			// pdata.addDataSet(ds);
			// pdata.addDataSet(dsPaging);
			datasetlist.add(ds);
			datasetlist.add(dsPaging);
		    pdata.setDataSetList(datasetlist);
		    // set the ErrorCode and ErrorMsg about success
		    nErrorCode = 0;
		    strErrorMsg = "SUCC";
		        
		} catch (Throwable th) {
			// set the ErrorCode and ErrorMsg about fail
			nErrorCode = -1;
			strErrorMsg = th.getMessage();
			
		} // try
		
		// save the ErrorCode and ErrorMsg for sending Client
		VariableList varList = pdata.getVariableList();
		
		
				
		varList.add("ErrorCode", nErrorCode);
		varList.add("ErrorMsg", strErrorMsg);
				
		// send the result data(XML) to Client
		HttpPlatformResponse res 
		    = new HttpPlatformResponse(response, PlatformType.CONTENT_TYPE_XML, "UTF-8");
		res.setData(pdata); 
		res.sendData();		// Send Data
	}
	
	@RequestMapping(value = "/member/listMember.do", method={RequestMethod.GET, RequestMethod.POST})
	public void listMember(HttpServletRequest request, HttpServletResponse response) throws PlatformException {
		
		System.out.println("ȸ�� ��ü ���");
		
		PlatformData pdata = new PlatformData();

		int nErrorCode = 0;
		String strErrorMsg = "START";

		int totalCount  = 0;
		
		try {
			// create HttpPlatformRequest for receive data from client
			HttpPlatformRequest req 
			     = new HttpPlatformRequest(request); 
			System.out.println("1");
			req.receiveData();							
			pdata = req.getData();
			DataSetList datasetlist = new DataSetList();
			DataSet ds1 = pdata.getDataSet("test");
			 System.out.println("2");
			int pageNum = Integer.parseInt(ds1.getString(0, "pageNum"));
			int contentPerPage = Integer.parseInt(ds1.getString(0, "contentPerPage"));
			int pageSize = Integer.parseInt(ds1.getString(0, "pageSize"));
			String keyField = ds1.getString(0, "keyField");
			String keyWord = ds1.getString(0, "keyWord");
			
			System.out.println("3");
			int startCount;
			int endCount;
					
			startCount = ((pageNum - 1) * pageSize + 1);
			endCount = (pageNum * pageSize);
			
			DataSet ds = new DataSet("memberVO");
			
			ds.addColumn("userID", DataTypes.STRING,  256);
			ds.addColumn("phone", DataTypes.STRING,  256);
			ds.addColumn("postnum", DataTypes.STRING,  400);
			ds.addColumn("address1", DataTypes.STRING,  400);
			ds.addColumn("address2", DataTypes.STRING, 256);
			ds.addColumn("name", DataTypes.STRING,  400);
			ds.addColumn("pwd", DataTypes.STRING,  400);
	
			System.out.println("4");
			MemberVO memberVO;

			HashMap<String, Object> map = new HashMap<>();
			map.put("pageNum", pageNum);
			map.put("startCount", startCount);
			map.put("endCount", endCount);
			map.put("keyField", keyField);
			map.put("keyWord", keyWord);

			totalCount = this.memberDAO.listCount(map);
			System.out.println("totalCount " +totalCount);
			
			List<MemberVO> list = null;
			if (totalCount > 0) {
				System.out.println("list : " + list);
				list = this.memberDAO.listMember(map);
			} else {
				list = Collections.emptyList();
			}

			for (int i=0; i<list.size(); i++) {
				
			    int row = ds.newRow(); // new Row feed          * x-platform���� addRow() �ڹٿ����� newRow()
			    
			    memberVO = list.get(i);
			    ds.set(row, "userID", memberVO.getUserID());
			    ds.set(row, "phone", memberVO.getPhone());
			    ds.set(row, "postnum", memberVO.getPostnum());
			    ds.set(row, "name", memberVO.getName());
			    ds.set(row, "pwd", memberVO.getPwd());
			    ds.set(row, "address1", memberVO.getAddress1());
			    ds.set(row, "address2", memberVO.getAddress2());
			    System.out.println("address2: " + memberVO.getAddress2());
			}
			int totalPage = (int)Math.ceil((double)totalCount/pageSize);
			if (totalPage == 0) {
				totalPage = 1;
			}
			if (pageNum > totalPage) {
				pageNum = totalPage;
			}
		

			int startPage = (pageNum - 1) / contentPerPage * contentPerPage + 1;
			int endPage = startPage + contentPerPage - 1;
			if (endPage > totalPage) {
				endPage = totalPage;
			}
			System.out.println("�˻��� �� ���� = "+totalCount);
			System.out.println("���� ������ = "+pageNum);
			System.out.println("���������� �� ����  = "+pageSize);
			System.out.println("�Ѹ��� �� ������ ���� = "+contentPerPage);
			System.out.println("��ü ������ ������ = "+totalPage);
			System.out.println("���� ī��Ʈ  = "+startCount);
			System.out.println("������ ī��Ʈ = "+endCount);
			System.out.println("���� ������ = "+startPage);
			System.out.println("������ ������ = "+endPage);
			System.out.println("Ű�ʵ� = " + keyField);
			System.out.println("Ű���� = " + keyWord);
			
			DataSet dsPaging = new DataSet("test");			
			    
			// DataSet Column setting
			dsPaging.addColumn("pageNum", DataTypes.INT, 4);
			dsPaging.addColumn("startPage", DataTypes.INT, 4);
			dsPaging.addColumn("endPage", DataTypes.STRING,  256);
			dsPaging.addColumn("totalPage", DataTypes.INT, 4);
			dsPaging.addColumn("count", DataTypes.INT, 4);
			dsPaging.addColumn("contentPerPage", DataTypes.INT, 4);
			dsPaging.addColumn("pageSize", DataTypes.INT, 4);
			dsPaging.addColumn("keyField", DataTypes.STRING, 256);
			dsPaging.addColumn("keyWord", DataTypes.STRING, 256);
				
			dsPaging.addColumn("startCount", DataTypes.INT, 4);
			dsPaging.addColumn("endCount", DataTypes.INT, 4);
			dsPaging.newRow();
				
			dsPaging.set(0, "pageNum", pageNum); 
			dsPaging.set(0, "startPage", startPage);
			dsPaging.set(0, "endPage", endPage);
			dsPaging.set(0, "count", totalCount);
			dsPaging.set(0, "totalPage", totalPage);
			dsPaging.set(0, "contentPerPage", contentPerPage);
			dsPaging.set(0, "pageSize", pageSize);
			dsPaging.set(0, "startCount", startCount);
			dsPaging.set(0, "endCount", endCount);
			dsPaging.set(0, "keyField", keyField);
			dsPaging.set(0, "keyWord", keyWord);
			
			// pdata.addDataSet(ds);
			// pdata.addDataSet(dsPaging);
			datasetlist.add(ds);
			datasetlist.add(dsPaging);
		    pdata.setDataSetList(datasetlist);
		    // set the ErrorCode and ErrorMsg about success
		    nErrorCode = 0;
		    strErrorMsg = "SUCC";
		        
		} catch (Throwable th) {
			// set the ErrorCode and ErrorMsg about fail
			nErrorCode = -1;
			strErrorMsg = th.getMessage();
			
		} // try
		
		// save the ErrorCode and ErrorMsg for sending Client
		VariableList varList = pdata.getVariableList();
		
		
				
		varList.add("ErrorCode", nErrorCode);
		varList.add("ErrorMsg", strErrorMsg);
				
		// send the result data(XML) to Client
		HttpPlatformResponse res 
		    = new HttpPlatformResponse(response, PlatformType.CONTENT_TYPE_XML, "UTF-8");
		res.setData(pdata); 
		res.sendData();		// Send Data
	}
	
	
	// ��ȭ��
	@RequestMapping({ "/member/goodsDetail.do" })
	//@RequestMapping(value = "/member/goodsDetail.do", method=RequestMethod.GET)
	public void goodsDetail(@RequestParam("product_code") int product_code, HttpServletRequest request, HttpServletResponse response) throws PlatformException {
			
		System.out.println("��ǰ ��������");
		System.out.println("product_code : " + product_code);	
		PlatformData pdata = new PlatformData();

	    int nErrorCode = 0;
	    String strErrorMsg = "START";

	    try {
	    	System.out.println("1");
	    	
	    	HttpPlatformRequest req = new HttpPlatformRequest(request); 
	    	System.out.println("2");
	    	try {
	    	    req.receiveData();
	    	} catch (Exception e) {
	    	    e.printStackTrace();
	    	}    
			pdata = req.getData();
			System.out.println("3");
			DataSet ds = pdata.getDataSet("productVO");
			
			for(int i=0; i<pdata.getDataSetList().size(); i++) {
				System.out.println(" alias >>> : " + pdata.getDataSetList().get(i).getAlias());
			}
			
			
			ProductVO productVO = this.memberDAO.productDetail(product_code);
			
			ds.set(0, "product_code", productVO.getProduct_code());
			ds.set(0, "product_type", productVO.getProduct_type());
			ds.set(0, "product_name", productVO.getProduct_name());
			ds.set(0, "product_price", productVO.getProduct_price());
			ds.set(0, "deleteState", productVO.getDeleteState());
			ds.set(0, "product_date", productVO.getProduct_date());
			ds.set(0, "images1", productVO.getImages1());
			ds.set(0, "images2", productVO.getImages2());
			
			DataSet ds1 = pdata.getDataSet("productOptionVO");
			
			List<ProductOptionVO> productOptionVO = this.memberDAO.getproductOption(product_code);
			
			ds1.clearData();
			
			for(int i = 0; i < productOptionVO.size(); i++) {
				
			ProductOptionVO povo = productOptionVO.get(i);
			ds1.newRow();

		    ds1.set(i, "product_code", povo.getProduct_code());
		    ds1.set(i, "product_size", povo.getProduct_size());
		    ds1.set(i, "quantity", povo.getQuantity());

			}
			/*
			System.out.println("4");
			DataSet ds2 = pdata.getDataSet("file");
			System.out.println("5");
			ProductFileVO productFileVO = this.memberDAO.fileDetail(product_code);
			System.out.println("6");
			System.out.println("product_code : " + productFileVO.getProduct_code());
			System.out.println("fileFullPath : " + productFileVO.getFileFullPath());
			System.out.println("fileName : " + productFileVO.getFileName());
			System.out.println("imsiName : " + productFileVO.getImsiName());
			ds2.set(0, "product_code", productFileVO.getProduct_code());
			ds2.set(0, "fileFullPath", productFileVO.getFileFullPath());
			ds2.set(0, "fileName", productFileVO.getFileName());
			ds2.set(0, "imsiName", productFileVO.getImsiName());
			*/
			
			nErrorCode = 0;
		    strErrorMsg = "SUCC";
		        
		} catch (Throwable th) {
			// set the ErrorCode and ErrorMsg about fail
			nErrorCode = -1;
			strErrorMsg = th.getMessage();
			
		} // try
		
		// save the ErrorCode and ErrorMsg for sending Client
		VariableList varList = pdata.getVariableList();
		
		
				
		varList.add("ErrorCode", nErrorCode);
		varList.add("ErrorMsg", strErrorMsg);
				
		// send the result data(XML) to Client
		HttpPlatformResponse res 
		    = new HttpPlatformResponse(response, PlatformType.CONTENT_TYPE_XML, "UTF-8");
		res.setData(pdata); 
		res.sendData();		// Send Data
	}
	
	// ��ǰ ��� 
	@RequestMapping({ "/member/insertProduct.do" })
	public void insertProduct(HttpServletRequest request, HttpServletResponse response) throws PlatformException  {
		
		System.out.println("��ǰ  ���");
		PlatformData pdata = new PlatformData();

	    int nErrorCode = 0;
	    String strErrorMsg = "START";
	    try {
	    	
	    	HttpPlatformRequest req = new HttpPlatformRequest(request); 
	    	try {
	    	    req.receiveData();
	    	} catch (Exception e) {
	    	    e.printStackTrace();
	    	}    
			pdata = req.getData();
			
			DataSet ds = pdata.getDataSet("productVO");
			
			
			ProductVO productvo = new ProductVO();
			int productCode = this.memberDAO.getProductCodeSeq(); // ����
			
			productvo.setProduct_code(productCode);
			
			System.out.println("productCode: " + productCode);
			ds.set(0, "product_code", productCode);
			productvo.setImages1(ds.getString(0, "images1"));
			productvo.setImages2(ds.getString(0, "images2"));
			productvo.setProduct_type(ds.getString(0, "product_type"));
			productvo.setProduct_name(ds.getString(0, "product_name"));
			productvo.setProduct_price(ds.getInt(0, "product_price"));
			System.out.println("product_image1: " + ds.getString(0, "images1"));
			System.out.println("product_image2: " + ds.getString(0, "images2"));
			System.out.println("product_type: " + ds.getString(0, "product_type"));
			System.out.println("product_name: " + ds.getString(0, "product_name"));
			System.out.println("product_price: " + ds.getInt(0, "product_price"));
			//System.out.println("product_code : >>>>>>>>>>>>>>>>>>>>>>>" + product);
			this.memberDAO.productInsert(productvo);
			
			System.out.println("productvo.code >>> : " + productvo.getProduct_code());
			// �ɼ�
			DataSet ds1 = pdata.getDataSet("productOptionVO");
			
			
			for(int i = 0; i < ds1.getRowCount(); i++) {

			ProductOptionVO productOptionvo = new ProductOptionVO();
			ds1.set(i, "product_code", productCode);
			productOptionvo.setProduct_code(productCode);// ����
			 //productOptionvo.setProduct_code(productvo.getProduct_code());
			//productOptionvo.setProduct_code(ds1.getInt(i, "product_code"));
			 
			productOptionvo.setProduct_size(ds1.getString(i, "product_size"));
			productOptionvo.setQuantity(ds1.getInt(i, "quantity"));
			
			System.out.println("productCode: " + ds1.getString(i, "product_code"));
			System.out.println("product_size: " + ds1.getString(i, "product_size"));
			System.out.println("quantity: " + ds1.getInt(i, "quantity"));
			
			this.memberDAO.productOptionInsert(productOptionvo);
			
			}
			
			
		    nErrorCode = 0;
		    strErrorMsg = "SUCC";
	           
		} catch (Exception e) {
			// set the ErrorCode and ErrorMsg about fail
			nErrorCode = -1;
			strErrorMsg = e.getMessage();
			e.printStackTrace();
		} // try
		
		// save the ErrorCode and ErrorMsg for sending Client
		VariableList varList = pdata.getVariableList();
			
		varList.add("ErrorCode", nErrorCode);
		varList.add("ErrorMsg", strErrorMsg);		
		// send the result data(XML) to Client
		HttpPlatformResponse res 
		    = new HttpPlatformResponse(response, PlatformType.CONTENT_TYPE_XML, "UTF-8");
		res.setData(pdata); 
		res.sendData();		// Send Data
	}
	
	// ���� ���ε� 
	@RequestMapping({"/member/fileUpload.do"})
	public void fileUpload(HttpServletRequest request, HttpServletResponse response) throws PlatformException {
	    
		System.out.println("���Ͼ��ε� ��� ����");
	    
	    String sHeader = request.getHeader("Content-Type");
	    if (sHeader == null) {
	        return;
	    }
	    String sUpPath = "C:\\Users\\Gram\\Desktop\\mvcproject\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp0\\wtpwebapps\\mvc2\\image\\uniform2\\";
	    //String sRealPath = request.getSession().getServletContext().getRealPath("/upload/files");
	    //String sUpPath   = sRealPath; // + sPath;
	    int nMaxSize = 500 * 1024 * 1024;
	    
	    PlatformData resData = new PlatformData();
	    VariableList resVarList = resData.getVariableList();
	    
	    String sMsg = " A ";
	    try {
	        MultipartRequest multi = new MultipartRequest(request, sUpPath, nMaxSize, "utf-8", new DefaultFileRenamePolicy());
	        Enumeration files = multi.getFileNames();
	        
	        sMsg += "B ";
	        DataSet ds = new DataSet("file");
	        ds.addColumn(new ColumnHeader("fileName", DataTypes.STRING));
	        ds.addColumn(new ColumnHeader("fileFullPath", DataTypes.STRING));
	        ds.addColumn(new ColumnHeader("imsiName", DataTypes.STRING));
	        
	        sMsg += "C ";
	        
	        // product_code ���� ������
	        int product_code =  Integer.parseInt(request.getParameter("product_code"));
	        System.out.println("product_code : " + product_code);
	        //String sFName = product_code;
	        String imsiName = request.getParameter("imsiName");
	        String sFName = "";
	        
	        String sName = "";
	        
	        File f = null;
	        
	        while (files.hasMoreElements()) {
	            sMsg += "D ";
	            sName = (String)files.nextElement();
	            
	            // sFName ������ ���� ���
	            //sFName = multi.getFilesystemName(sName);
	            // imsiName ������ ���� ���
	            imsiName = multi.getFilesystemName(sName);
	            System.out.println("fileName : " + multi.getOriginalFileName(sName)); // fileName
	            System.out.println("imsiName : " + multi.getFilesystemName(sName)); // imsiName
	            
	            int nRow = ds.newRow();
	            ds.set(nRow, "fileName", multi.getOriginalFileName(sName));
	            
	            f = multi.getFile(sName);
	            if (f != null) {
	                ds.set(nRow, "fileFullPath", f.getAbsolutePath());
	                ds.set(nRow, "imsiName", imsiName);
	                
	                // ���� �̸� ����
	                String extension = "";
	                String originalFileName = multi.getOriginalFileName(sName);
	                int index = originalFileName.lastIndexOf('.');
	                if (index > 0) {
	                    extension = originalFileName.substring(index);
	                }
	                File newFile = new File(sUpPath + imsiName);
	                f.renameTo(newFile);
	                System.out.println("newFile : " + newFile);
	            }
	            sMsg += nRow +" ";
	        }
	        
	        resData.addDataSet(ds);
	        resVarList.add("ErrorCode", 200);
	        resVarList.add("ErrorMsg", sUpPath+"/"+sFName);
	        

	    	
	        product_code =  Integer.parseInt(request.getParameter("product_code"));
	        
	        
	        System.out.println("product_code : " + product_code);
	    	System.out.println("imsiName : " + imsiName);
	    	
	    	
	    	this.memberDAO.imageUp(product_code, imsiName);
	    	
	        
	    }
		catch (Exception e) 
		{
			resVarList.add("ErrorCode", 500);
			resVarList.add("ErrorMsg", sMsg + " " + e);
			e.printStackTrace();
		}

		HttpPlatformResponse res = new HttpPlatformResponse(response);
		res.setData(resData);
		res.sendData();	
		
	}	
	
	@RequestMapping({"/member/fileUpload1.do"})
	public void fileUpload1(HttpServletRequest request, HttpServletResponse response) throws PlatformException {
	    
		System.out.println("���Ͼ��ε� ��� ����");
	    
	    String sHeader = request.getHeader("Content-Type");
	    if (sHeader == null) {
	        return;
	    }
	    String sUpPath = "C:\\Users\\Gram\\Desktop\\mvcproject\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp0\\wtpwebapps\\mvc2\\image\\uniform\\";
	    //String sUpPath = "C:\\Users\\Gram\\Desktop\\mvcproject\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp0\\wtpwebapps\\mvc2\\image\\uniform2\\";
	    //String sRealPath = request.getSession().getServletContext().getRealPath("/upload/files");
	    //String sUpPath   = sRealPath; // + sPath;
	    int nMaxSize = 500 * 1024 * 1024;
	    
	    PlatformData resData = new PlatformData();
	    VariableList resVarList = resData.getVariableList();
	    
	    String sMsg = " A ";
	    try {
	        MultipartRequest multi = new MultipartRequest(request, sUpPath, nMaxSize, "utf-8", new DefaultFileRenamePolicy());
	        Enumeration files = multi.getFileNames();
	        
	        sMsg += "B ";
	        DataSet ds = new DataSet("file");
	        ds.addColumn(new ColumnHeader("fileName", DataTypes.STRING));
	        ds.addColumn(new ColumnHeader("fileFullPath", DataTypes.STRING));
	        ds.addColumn(new ColumnHeader("imsiName", DataTypes.STRING));
	        
	        sMsg += "C ";
	        
	        // product_code ���� ������
	        int product_code = Integer.parseInt(request.getParameter("product_code"));
	        System.out.println("product_code : " + product_code);
	        //String sFName = product_code;
	        String imsiName = request.getParameter("imsiName");
	        String sFName = "";
	        
	        String sName = "";
	        
	        File f = null;
	        
	        while (files.hasMoreElements()) {
	            sMsg += "D ";
	            sName = (String)files.nextElement();
	            
	            // sFName ������ ���� ���
	            //sFName = multi.getFilesystemName(sName);
	            // imsiName ������ ���� ���
	            imsiName = multi.getFilesystemName(sName);
	            System.out.println("fileName : " + multi.getOriginalFileName(sName)); // fileName
	            System.out.println("imsiName : " + multi.getFilesystemName(sName)); // imsiName
	            
	            int nRow = ds.newRow();
	            ds.set(nRow, "fileName", multi.getOriginalFileName(sName));
	            
	            f = multi.getFile(sName);
	            if (f != null) {
	                ds.set(nRow, "fileFullPath", f.getAbsolutePath());
	                ds.set(nRow, "imsiName", imsiName);
	                
	                // ���� �̸� ����
	                String extension = "";
	                String originalFileName = multi.getOriginalFileName(sName);
	                int index = originalFileName.lastIndexOf('.');
	                if (index > 0) {
	                    extension = originalFileName.substring(index);
	                }
	                File newFile = new File(sUpPath + imsiName);
	                f.renameTo(newFile);
	                System.out.println("newFile : " + newFile);
	            }
	            sMsg += nRow +" ";
	        }
	        
	        resData.addDataSet(ds);
	        resVarList.add("ErrorCode", 200);
	        resVarList.add("ErrorMsg", sUpPath+"/"+sFName);
	        
	        product_code =  Integer.parseInt(request.getParameter("product_code"));
	        
	        
	        System.out.println("product_code : " + product_code);
	    	System.out.println("imsiName : " + imsiName);
	    	
	    	
	    	this.memberDAO.imageUp2(product_code, imsiName);
	    	
	    }
		catch (Exception e) 
		{
			resVarList.add("ErrorCode", 500);
			resVarList.add("ErrorMsg", sMsg + " " + e);
			e.printStackTrace();
		}

		HttpPlatformResponse res = new HttpPlatformResponse(response);
		res.setData(resData);
		res.sendData();	
		
	}	
	
	// ����
	@RequestMapping({"/member/productUp.do"})
	public void productUp(HttpServletRequest request, HttpServletResponse response) throws PlatformException {
		    
		System.out.println("�����մϴ�");
			
			
		PlatformData pdata = new PlatformData();

		int nErrorCode = 0;
		String strErrorMsg = "START";
			
		try {
				
			HttpPlatformRequest req = new HttpPlatformRequest(request); 

			req.receiveData();							
			pdata = req.getData();
			
			DataSet ds = pdata.getDataSet("productVO");
			    
			ProductVO productVO = new ProductVO();  
			// ������ ������ ����
			productVO.setProduct_code(ds.getInt(0, "product_code"));
			ProductVO vo = this.memberDAO.productDetail(ds.getInt(0, "product_code"));
			productVO.setImages1(ds.getString(0, "images1"));
			productVO.setImages2(ds.getString(0, "images2"));
			productVO.setDeleteState(ds.getString(0, "deleteState"));
			productVO.setProduct_name(ds.getString(0, "product_name"));
			productVO.setProduct_price(ds.getInt(0, "product_price"));
			productVO.setProduct_type(ds.getString(0, "product_type"));
			productVO.setProduct_date(ds.getDateTime(0, "product_date"));
			
			this.memberDAO.productUpdate(productVO);
			
			DataSet ds1 = pdata.getDataSet("productOptionVO");
			
			int product_code = ds.getInt(0, "product_code");
			System.out.println("product_code : " + product_code);
			this.memberDAO.productOptionDelete(product_code);
			
			for(int i = 0; i < ds1.getRowCount(); i++) {
				
			    ProductOptionVO productOptionVO = new ProductOptionVO();
			    productOptionVO.setProduct_code(product_code);
			    //productOptionVO.setProduct_code(ds1.getInt(i, "product_code"));
			    productOptionVO.setProduct_size(ds1.getString(i, "product_size"));
			    productOptionVO.setQuantity(ds1.getInt(i, "quantity"));
			    
			    this.memberDAO.productOptionUpdate(productOptionVO);
			}
			
			DataSet ds2 = pdata.getDataSet("file");
			if (ds2.getRowCount() > 0) {
			System.out.println("ds2.getRowCount() : " + ds2.getRowCount() );
			
			String images2 = vo.getImages2();

			System.out.println(images2);

			String filePath = "C:\\Users\\Gram\\Desktop\\mvcproject\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp0\\wtpwebapps\\mvc2\\image\\uniform\\" + images2;
			
				System.out.println("������ ���� : " + filePath);
				
				File file = new File(filePath);
				   if (file.exists()) {
				       file.delete();
				       System.out.println("���� �Ϸ�");
				}
			}   
			
			DataSet ds3 = pdata.getDataSet("file2");
			if (ds3.getRowCount() > 0) {
			System.out.println("ds3.getRowCount() : " + ds3.getRowCount() );
			
			String images1 = vo.getImages1();

			System.out.println(images1);

			String filePath = "C:\\Users\\Gram\\Desktop\\mvcproject\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp0\\wtpwebapps\\mvc2\\image\\uniform\\" + images1;
			
				System.out.println("������ ���� : " + filePath);
				
				File file = new File(filePath);
				   if (file.exists()) {
				       file.delete();
				       System.out.println("���� �Ϸ�");
				}
			}   
			
			
			} catch (Throwable th) {
				// set the ErrorCode and ErrorMsg about fail
				nErrorCode = -1;
				strErrorMsg = th.getMessage();
				System.out.println("ERROR:" + strErrorMsg);
				th.printStackTrace();
			} // try
			
			// save the ErrorCode and ErrorMsg for sending Client �۾��� �Ϸ��ߴٴ� �ڵ忡 ���ؼ� �׻� ������ �״�� ������Ѵ� 
			VariableList varList = pdata.getVariableList();
					
			varList.add("ErrorCode", nErrorCode);
			varList.add("ErrorMsg", strErrorMsg);
					
			// send the result data(XML) to Client
			HttpPlatformResponse res 
			    = new HttpPlatformResponse(response, 
				       									            PlatformType.CONTENT_TYPE_XML,  
				       									            "UTF-8");
			res.setData(pdata); 
			res.sendData();		// Send Data
			    
		}
	
	// ����
	@RequestMapping({"/member/productDel.do"})
	public void productDel(HttpServletRequest request, HttpServletResponse response) throws PlatformException {
		    
		System.out.println("�����մϴ�");
			
		PlatformData pdata = new PlatformData();

		int nErrorCode = 0;
		String strErrorMsg = "START";
			
		try {
				
			HttpPlatformRequest req = new HttpPlatformRequest(request); 

			req.receiveData();							
			pdata = req.getData();
			
			DataSet ds = pdata.getDataSet("productVO");
			    
			ProductVO productVO = new ProductVO();  
			// ������ ������ ����
			productVO.setProduct_code(ds.getInt(0, "product_code"));
			productVO.setImages1(ds.getString(0, "images1"));
			productVO.setImages2(ds.getString(0, "images2"));
			productVO.setProduct_name(ds.getString(0, "product_name"));
			productVO.setProduct_price(ds.getInt(0, "product_price"));
			productVO.setProduct_type(ds.getString(0, "product_type"));
			productVO.setProduct_date(ds.getDateTime(0, "product_date"));
			//productVO.setDeleteState(ds.getString(0, "deleteState"));
			productVO.setDeleteState("Y");
			
			System.out.println("product_code: " + productVO.getProduct_code());
			
			System.out.println("product_name: " + productVO.getProduct_name());
			System.out.println("product_price: " + productVO.getProduct_price());
			System.out.println("product_type: " + productVO.getProduct_type());
			System.out.println("deleteState: " + productVO.getDeleteState());
			
			this.memberDAO.productDelete(productVO);
			
			
			
			DataSet ds1 = pdata.getDataSet("productOptionVO");
			
			String product_code = ds.getString(0, "product_code");
			System.out.println("product_code : " + product_code);
			
			for(int i = 0; i < ds1.getRowCount(); i++) {
				
			    ProductOptionVO productOptionVO = new ProductOptionVO();
			    productOptionVO.setProduct_code(ds1.getInt(i, "product_code"));
			    productOptionVO.setProduct_size(ds1.getString(i, "product_size"));
			    //productOptionVO.setQuantity(ds1.getInt(i, "quantity"));
			    productOptionVO.setQuantity(0); // quantity�� 0���� ����
			    
			    System.out.println("product_code: " + productOptionVO.getProduct_code());
			    System.out.println("product_size: " + productOptionVO.getProduct_size());
			    System.out.println("quantity: " + productOptionVO.getQuantity());
			    this.memberDAO.productOptionUpdate2(productOptionVO);
			}
			
			
			    
			} catch (Throwable th) {
				// set the ErrorCode and ErrorMsg about fail
				nErrorCode = -1;
				strErrorMsg = th.getMessage();
				System.out.println("ERROR:" + strErrorMsg);
				th.printStackTrace();
			} // try
			
			// save the ErrorCode and ErrorMsg for sending Client �۾��� �Ϸ��ߴٴ� �ڵ忡 ���ؼ� �׻� ������ �״�� ������Ѵ� 
			VariableList varList = pdata.getVariableList();
					
			varList.add("ErrorCode", nErrorCode);
			varList.add("ErrorMsg", strErrorMsg);
					
			// send the result data(XML) to Client
			HttpPlatformResponse res 
			    = new HttpPlatformResponse(response, 
				       									            PlatformType.CONTENT_TYPE_XML,  
				       									            "UTF-8");
			res.setData(pdata); 
			res.sendData();		// Send Data
			    
		}
	
	
	
	@RequestMapping({ "/member/insertProduct2.do" })
	public void insertProduct2(HttpServletRequest request, HttpServletResponse response) throws PlatformException  {
	    System.out.println("��ǰ  ���");
	    PlatformData pdata = new PlatformData();

	    int nErrorCode = 0;
	    String strErrorMsg = "START";
	    try {
	        HttpPlatformRequest req = new HttpPlatformRequest(request); 
	        try {
	            req.receiveData();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }    
	        pdata = req.getData();
	        
	        DataSet ds = pdata.getDataSet("productVO");
	        
	        
	        
	        ProductVO productvo = new ProductVO();
	        
	        productvo.setImages1(ds.getString(0, "images1"));
	        productvo.setImages2(ds.getString(0, "images2"));
	        productvo.setProduct_type(ds.getString(0, "product_type"));
	        productvo.setProduct_name(ds.getString(0, "product_name"));
	        productvo.setProduct_price(ds.getInt(0, "product_price"));
	        System.out.println("product_image1: " + ds.getString(0, "images1"));
	        System.out.println("product_image2: " + ds.getString(0, "images2"));
	        System.out.println("product_type: " + ds.getString(0, "product_type"));
	        System.out.println("product_name: " + ds.getString(0, "product_name"));
	        System.out.println("product_price: " + ds.getInt(0, "product_price"));
	        
	        this.memberDAO.productInsert(productvo);
	        
	        
	        // ���� product_code ��������
	        int productCode = this.memberDAO.getProductCodeCurrval();
	        System.out.println("productCode : " + productCode);
	        ds.set(0, "product_code", productCode);
	        // �ɼ�
	        DataSet ds1 = pdata.getDataSet("productOptionVO");
	        
	        
	        for(int i = 0; i < ds1.getRowCount(); i++) {

	            ProductOptionVO productOptionvo = new ProductOptionVO();
	            ds1.set(i, "product_code", productCode);
	            
	            productOptionvo.setProduct_code(productCode);
	            productOptionvo.setProduct_size(ds1.getString(i, "product_size"));
	            productOptionvo.setQuantity(ds1.getInt(i, "quantity"));
	            
	            System.out.println("productCode: " + ds1.getString(i, "product_code"));
	            System.out.println("product_size: " + ds1.getString(i, "product_size"));
	            System.out.println("quantity: " + ds1.getInt(i, "quantity"));
	            
	            this.memberDAO.productOptionInsert(productOptionvo);
	            
	        }
	        
	        
	        nErrorCode = 0;
	        strErrorMsg = "SUCC";
	           
	    } catch (Exception e) {
	        // set the ErrorCode and ErrorMsg about fail
	        nErrorCode = -1;
	        strErrorMsg = e.getMessage();
	        e.printStackTrace();
	    } // try
	    
	    // save the ErrorCode and ErrorMsg for sending Client
	    VariableList varList = pdata.getVariableList();
	        
	    varList.add("ErrorCode", nErrorCode);
	    varList.add("ErrorMsg", strErrorMsg);     
	    // send the result data(XML) to Client
	    HttpPlatformResponse res 
	        = new HttpPlatformResponse(response, PlatformType.CONTENT_TYPE_XML, "UTF-8");
	    res.setData(pdata); 
	    res.sendData();     // Send Data
	}
	
	
	
	
	
	
	
	
	
	
	
	// ȸ�� Ż��
	@RequestMapping(value="/member/memberDelete.do", method=RequestMethod.GET)
	public ModelAndView memberDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {

		System.out.println("ȸ��Ż�� ȭ��");
		
		ModelAndView mav = new ModelAndView();
		
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
		
		mav.setViewName("memberDelete");
		return mav;
	}
	// pwd�� �̿��� ���� �� ���� 
	@RequestMapping(value="/member/checkPassword.do", method = RequestMethod.POST)
	@ResponseBody
	public String checkPassword(MemberVO memberVO, HttpServletRequest request, HttpServletResponse response) {
		
		System.out.println("��й�ȣ ��ȸ");
		
		String inputPwd = request.getParameter("pwd");
		System.out.println("�Է��� ��й�ȣ : " + inputPwd);
		String userID = request.getParameter("userID");
		System.out.println(userID);
		MemberVO member = this.memberDAO.getMember(userID);
		String dbPwd = member.getPwd();
		System.out.println("db���� ��й�ȣ : " + dbPwd);
		
		if (inputPwd.trim().equals(dbPwd.trim())) {
	        return "success";
	    } else {
	        return "fail";
	    }
	}
	// id�ߺ�üũ
	@RequestMapping(value="/member/idCheck.do", method = RequestMethod.POST)
	@ResponseBody
	public String idCheck(MemberVO memberVO, HttpServletRequest request, HttpServletResponse response) {
		
		System.out.println("���̵� �ߺ� ��ȸ");
		
		 String inputUserID = request.getParameter("userID");
		 System.out.println(inputUserID);
		 MemberVO member = this.memberDAO.getUser(inputUserID);
		 if (member != null) {
		      return "fail";
		 } else {
		      return "success";
		 }
		 
		 
	}
	
	// phone�ߺ�üũ
	@RequestMapping(value="/member/phoneCheck.do", method = RequestMethod.POST)
	@ResponseBody
	public String phoneCheck(MemberVO memberVO, HttpServletRequest request, HttpServletResponse response) {
			
		System.out.println("��ȭ��ȣ �ߺ� ��ȸ");
			
		String inputPhone = request.getParameter("phone");
		System.out.println(inputPhone);
		MemberVO member = this.memberDAO.getPhone(inputPhone);
		if (member != null) {
		     return "fail";
		} else {
			 return "success";
		}
			 	 
	}
	
	
	
	
	// ��ȭ��
	@RequestMapping(value = "/member/productDetail.do", method=RequestMethod.GET)
	public ModelAndView productDetail(String product_code, HttpServletRequest request, HttpServletResponse response) {
		
		System.out.println("��ǰ ��������");
		
		ModelAndView mav = new ModelAndView();
		
		ProductVO productVO = this.memberDAO.productDetail(product_code);
		List<ProductOptionVO> productOptionVO = this.memberDAO.getproductOption(product_code);
		
		mav.addObject("product", productVO);
		mav.addObject("productOptionVO", productOptionVO);
		
		mav.setViewName("productDetail");
		
		return mav;
	}
	
	// �α��� ó��
	@RequestMapping(value = "/member/checkLogin.do", method=RequestMethod.GET)
	@ResponseBody
	public String checkLogin(HttpServletRequest request, HttpServletResponse response) {
	    HttpSession session = request.getSession();
	    MemberVO memberVO = (MemberVO) session.getAttribute("member");
	    if (memberVO == null) {
	        // �α������� ���� ��� "not logged in" ���ڿ��� ��ȯ�մϴ�.
	        return "not logged in";
	    } else {
	        // �α����� ��� "logged in" ���ڿ��� ��ȯ�մϴ�.
	        return "logged in";
	    }
	}
	
	// ��ٱ��� ��ȭ�� 
	@RequestMapping(value = "/member/mycartForm.do", method=RequestMethod.GET)
	public ModelAndView mycartForm(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		System.out.println("��ٱ��� �̵�");
		
		ModelAndView mav = new ModelAndView();
		
		// HttpSession ��ü���� member �Ӽ��� �����ɴϴ�.
	    HttpSession session = request.getSession();
	    MemberVO memberVO = (MemberVO) session.getAttribute("member");
	    if (memberVO == null) {
	        // �α������� ���� ��� alert�� ���� �α��� �������� �����̷�Ʈ�մϴ�.
	        response.setContentType("text/html; charset=UTF-8");
	        PrintWriter out = response.getWriter();
	        out.println("<script>alert('�α����� �ʿ��մϴ�.'); location.href='/project/member/loginForm.do';</script>");
	        out.flush();
	        return null;
	    }
	    // memberVO ��ü���� userID ������ �����ɴϴ�.
	    String userID = memberVO.getUserID();
	    
	    List<CartVO> cartList = this.memberDAO.cartList(userID);
	    
	    // ��ٱ��� ������ ModelAndView ��ü�� �߰��մϴ�.
	    mav.addObject("cartList", cartList);
		
		mav.setViewName("mycartForm");
		
		return mav;
	}
	
	// ��ٱ��� �߰�
	@RequestMapping(value = "/member/addCart.do", method=RequestMethod.GET)
	@ResponseBody
	public String addCart(HttpServletRequest request, HttpServletResponse response) {
	    System.out.println("��ٱ��� �߰�");
	    CartVO cartVO = new CartVO();
	    // HttpSession ��ü���� member �Ӽ��� �����ɴϴ�.
	    HttpSession session = request.getSession();
	    MemberVO memberVO = (MemberVO) session.getAttribute("member");
	   
	    // memberVO ��ü���� userID�� phone ������ �����ɴϴ�.
	    cartVO.setUserID(memberVO.getUserID());
	    cartVO.setPhone(memberVO.getPhone());

	    String product_code = request.getParameter("product_code");
	    String product_size = request.getParameter("product_size");
	    int quantity = Integer.parseInt(request.getParameter("quantity"));

	    // productOptionVO�� �����ͼ� ����� Ȯ���մϴ�.
	    ProductOptionVO productOptionVO = this.memberDAO.getProductOption3(product_code, product_size);
	    if (productOptionVO.getQuantity() < quantity) {
	        // ����� ������ ��� "out of stock"�� ��ȯ�մϴ�.
	        return "out of stock";
	    }

	    // ��ٱ��Ͽ� ������ ��ǰ �ڵ�� ������ ��ǰ ũ���� ��ǰ�� �ִ��� Ȯ���մϴ�.
	    CartVO existingCartVO = this.memberDAO.getCart(memberVO.getUserID(), product_code, product_size);
	    if (existingCartVO != null) {
	        // �̹� �����ϴ� ��� ������ ������Ʈ�մϴ�.
	        existingCartVO.setQuantity(existingCartVO.getQuantity() + quantity);
	        this.memberDAO.updateCart2(existingCartVO);
	    } else {
	        // �������� �ʴ� ��� ���ο� ��ǰ�� �߰��մϴ�.
	        cartVO.setProduct_code(product_code);
	        cartVO.setProduct_size(product_size);
	        cartVO.setQuantity(quantity);
	        cartVO.setProduct_name(request.getParameter("product_name"));
	        cartVO.setProduct_price(Integer.parseInt(request.getParameter("product_price")));
	        cartVO.setDelivery(request.getParameter("delivery"));

	        this.memberDAO.addCart(cartVO);
	    }
	    
	    // ���������� ó���� ��� "success"�� ��ȯ�մϴ�.
	    return "success";
	}
	// ��ٱ��� ��ü ����
	@ResponseBody
	@RequestMapping(value = "/member/deleteAll.do", method=RequestMethod.POST)
	public String deleteAll(HttpServletRequest request, HttpServletResponse response) {
	    
	    System.out.println("��ٱ��� ��ü ����");
	    
	    CartVO cartVO = new CartVO();
	    HttpSession session = request.getSession();
	    MemberVO memberVO = (MemberVO) session.getAttribute("member");
	    
	    cartVO.setUserID(memberVO.getUserID());
	    cartVO.setPhone(memberVO.getPhone());
	    
	    cartVO.setProduct_code(request.getParameter("product_code"));
	    cartVO.setProduct_size(request.getParameter("product_size"));
	    
	    String quantity = request.getParameter("quantity");
	    if (quantity != null) {
	        cartVO.setQuantity(Integer.parseInt(quantity));
	    }
	    
	    cartVO.setProduct_name(request.getParameter("product_name"));
	    
	    String product_price = request.getParameter("product_price");
	    if (product_price != null) {
	        cartVO.setProduct_price(Integer.parseInt(product_price));
	    }
	    
	    cartVO.setDelivery(request.getParameter("delivery"));
	    
	    this.memberDAO.deleteAll(memberVO.getUserID());
	    
	    return "success";
	}
	// ��ٱ��� üũ�� ����
	@ResponseBody
	@RequestMapping(value = "/member/deletePart.do", method=RequestMethod.POST)
	public String deletePart(HttpServletRequest request, HttpServletResponse response) {
	    
	    System.out.println("��ٱ��� �κ� ����");
	    
	    String[] cartNums = request.getParameterValues("cartNum");
	    
	    HttpSession session = request.getSession();
	    MemberVO memberVO = (MemberVO) session.getAttribute("member");
	    String userID = memberVO.getUserID();
	    
	    for (String cartNum : cartNums) {
	        int cartNumInt = Integer.parseInt(cartNum);
	        this.memberDAO.deletePart(userID, cartNumInt);
	    }
	    
	    return "success";
	}
	// ��ٱ��� update �� ���������� ���� 
	@RequestMapping(value = "/member/orderForm.do", method=RequestMethod.POST)
	public ModelAndView orderForm(HttpServletRequest request, HttpServletResponse response) {
	    System.out.println("������ �Դϴ�");

	    ModelAndView mav = new ModelAndView();

	    // HttpSession ��ü���� member �Ӽ��� �����ɴϴ�.
	    HttpSession session = request.getSession();
	    MemberVO memberVO = (MemberVO) session.getAttribute("member");

	    String[] productCodes = request.getParameterValues("product_code");
	    String[] productSizes = request.getParameterValues("product_size");
	    String[] quantities = request.getParameterValues("quantity");
	    String[] productNames = request.getParameterValues("product_name");
	    String[] productPrices = request.getParameterValues("product_price");
	    String[] deliveries = request.getParameterValues("delivery");
	    String[] cartNums = request.getParameterValues("cartNum");

	    // üũ�� �κи� ���� �������� ���� 
	    List<CartVO> cartList = new ArrayList<CartVO>();

	    for (int i = 0; i < cartNums.length; i++) {
	        CartVO cartVO = new CartVO();
	        cartVO.setUserID(memberVO.getUserID());
	        cartVO.setPhone(memberVO.getPhone());
	        cartVO.setProduct_code(productCodes[i]);
	        cartVO.setProduct_size(productSizes[i]);
	        cartVO.setQuantity(Integer.parseInt(quantities[i]));
	        cartVO.setProduct_name(productNames[i]);
	        cartVO.setProduct_price(Integer.parseInt(productPrices[i]));
	        cartVO.setDelivery(deliveries[i]);
	        cartVO.setCartNum(Integer.parseInt(cartNums[i]));

	        System.out.println("product_code: " + productCodes[i]);
	        System.out.println("product_size: " + productSizes[i]);
	        System.out.println("quantity: " + quantities[i]);
	        System.out.println("product_name: " + productNames[i]);
	        System.out.println("product_price: " + productPrices[i]);
	        System.out.println("delivery: " + deliveries[i]);
	        System.out.println("cartNum: " + cartNums[i]);

	        this.memberDAO.updateCart(cartVO);

	        cartList.add(cartVO);
	    }

	    mav.addObject("cartList", cartList);

	    mav.setViewName("orderForm");

	    return mav;
	}
	
	// ���� ó�� ���� ��
	@RequestMapping(value = "/member/payment.do", method=RequestMethod.POST)
	public ModelAndView payment(HttpServletRequest request, HttpServletResponse response) throws IOException {
	    
		System.out.println("���� ó�� �Դϴ�");
		
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
		
	    ModelAndView mav = new ModelAndView();

	    // OrderVO ��ü�� �����մϴ�.
	    OrderVO orderVO = new OrderVO();
	    // HttpSession ��ü���� member �Ӽ��� �����ɴϴ�.
	    HttpSession session = request.getSession();
	    MemberVO memberVO = (MemberVO) session.getAttribute("member");
	   
	    // memberVO ��ü���� userID�� phone ������ �����ɴϴ�.
	    orderVO.setUserID(memberVO.getUserID());
	    orderVO.setPhone(memberVO.getPhone());
	    
	    System.out.println("userID: " + memberVO.getUserID());
	    System.out.println("phone: " + memberVO.getPhone());
	   	    
	    // request ��ü���� ������ ������ �����ɴϴ�.
	    orderVO.setOrder_phone(request.getParameter("order_phone"));
	    orderVO.setOrder_name(request.getParameter("order_name"));
	    orderVO.setOrder_address1(request.getParameter("order_address1"));
	    orderVO.setOrder_address2(request.getParameter("order_address2"));
	    orderVO.setPostnum(request.getParameter("postnum"));
	    orderVO.setOrder_memo(request.getParameter("order_memo"));
	    orderVO.setOrder_memo(request.getParameter("order_memo"));
	    orderVO.setPaymethod(request.getParameter("paymethod"));
	    orderVO.setTotalbill(Integer.parseInt(request.getParameter("totalbill")));
	    
	   
	    // �����ͺ��̽��� OrderVO ��ü�� �����մϴ�.
	    this.memberDAO.insertOrder(orderVO);
	    
	    System.out.println("order_phone: " + request.getParameter("order_phone"));
	    System.out.println("order_name: " + request.getParameter("order_name"));
	    System.out.println("order_address1: " + request.getParameter("order_address1"));
	    System.out.println("order_address2: " + request.getParameter("order_address2"));
	    System.out.println("postnum: " + request.getParameter("postnum"));
	    System.out.println("order_memo: " + request.getParameter("order_memo"));
	    System.out.println("paymethod: " + request.getParameter("paymethod"));
	    System.out.println("totalbill: " + request.getParameter("totalbill"));
	    
	    // ������ order_num ���� �����ɴϴ�.
	    int orderNum = orderVO.getOrder_num();
		
	    System.out.println("orderNum: " +orderNum);
	    // ============================================================================
		// ================== ��ǰ ���� ������ �����´� ========================
		// OrderProductVO ��ü�� �迭�� �����մϴ�.
	    String[] productCodes = request.getParameterValues("product_code");
	    String[] productSizes = request.getParameterValues("product_size");
	    String[] quantities = request.getParameterValues("quantity");
	    String[] productNames = request.getParameterValues("product_name");
	    String[] productPrices = request.getParameterValues("product_price");
	    String[] deliveries = request.getParameterValues("delivery");
	    Date order_date = new Date();
	    
	    String[] orderstate = new String[productCodes.length];
	    Arrays.fill(orderstate, "��� �غ���");
	    
	    String[] buyComplete = new String[productCodes.length];
	    Arrays.fill(buyComplete, "N");
	    
	    String[] refund = new String[productCodes.length];
	    Arrays.fill(refund, "N");
	    
	    List<OrderProductVO> orderProducts = new ArrayList<>();
	    for (int i = 0; i < productCodes.length; i++) {
	        OrderProductVO orderProductVO = new OrderProductVO();
	        // OrderProductVO ��ü�� ���� �����մϴ�.
	        orderProductVO.setOrder_num(orderNum); // order_num ���� �����մϴ�.
	        orderProductVO.setUserID(memberVO.getUserID());
	        orderProductVO.setPhone(memberVO.getPhone());
	        orderProductVO.setProduct_code(productCodes[i]);
	        orderProductVO.setProduct_size(productSizes[i]);
	        orderProductVO.setQuantity(Integer.parseInt(quantities[i]));
	        orderProductVO.setProduct_name(productNames[i]);
	        orderProductVO.setProduct_price(Integer.parseInt(productPrices[i]));
	        orderProductVO.setDelivery(deliveries[i]);
	        orderProductVO.setOrder_date(order_date);
	        orderProductVO.setOrder_state(orderstate[i]);
	        orderProductVO.setBuyComplete(buyComplete[i]);
	        orderProductVO.setRefund(refund[i]);
	        
		    // �����ͺ��̽��� OrderProductVO ��ü�� �����մϴ�.
		    this.memberDAO.insertOrderProduct(orderProductVO);
		    orderProducts.add(orderProductVO);

	    }

	    // db�� �ش� ��ٱ��� �׸� ���� ����� 
	    if (!request.getParameter("fromCart").equals("false")) {
	        String[] cartNums = request.getParameterValues("cartNum");
	        for (int i = 0; i < cartNums.length; i++) {
	            int cartnum = Integer.parseInt(cartNums[i]);
	            System.out.println("cartnum : " + cartnum);
	            System.out.println("fromCart : " + request.getParameter("fromCart"));
	            CartVO cartVO = new CartVO();
	            cartVO.setUserID(memberVO.getUserID());
	            cartVO.setCartNum(cartnum);
	            int result = this.memberDAO.deleteCart(cartVO);
	        }
	    }

	    // db���� ��ǰ ������ŭ ���� 
	    String[] productCode = request.getParameterValues("product_code");
	    String[] productSize = request.getParameterValues("product_size");
	    String[] productquantities = request.getParameterValues("quantity");
	    
	    for (int i = 0; i < productCodes.length; i++) {
	        // �����ͺ��̽����� product_code�� product_size�� �ش��ϴ� ProductOptionVO ��ü�� �����ɴϴ�.
	        List<ProductOptionVO> currentProductOptionVOs = this.memberDAO.getproductOption2(productCode[i], productSize[i]);
	        for (ProductOptionVO currentProductOptionVO : currentProductOptionVOs) {
	            if (currentProductOptionVO.getProduct_size().equals(productSize[i])) {
	            	System.out.println("Before: " + currentProductOptionVO.getQuantity());
	            	// ������ ������ŭ �����մϴ�.
	                currentProductOptionVO.setQuantity(currentProductOptionVO.getQuantity() - Integer.parseInt(productquantities[i]));
	                System.out.println("After: " + currentProductOptionVO.getQuantity());
	                // �����ͺ��̽��� ������Ʈ�մϴ�.
	                this.memberDAO.updateQuantity(currentProductOptionVO);
	            }
	        }
	    }

	    mav.setViewName("paymentResult");
	    mav.addObject("orderVO", orderVO);
	    mav.addObject("orderProducts", orderProducts);
	    return mav;
	}
	
	
	// ��ٱ��� ��ġ�� �ʰ� �ٷ� ���� 
	@RequestMapping(value="/member/orderDirect.do", method = RequestMethod.POST)
	public ModelAndView orderDirect(HttpServletRequest request, HttpServletResponse response) throws IOException {
	    
	    System.out.println("�ٷ� ����");
	    
	    HttpSession session = request.getSession();
	    MemberVO memberVO = (MemberVO) session.getAttribute("member");
	    if (memberVO == null) {
	        // �α������� ���� ��� alert�� ���� �α��� �������� �����̷�Ʈ�մϴ�.
	        response.setContentType("text/html; charset=UTF-8");
	        PrintWriter out = response.getWriter();
	        out.println("<script>alert('�α����� �ʿ��մϴ�.'); location.href='/project/member/loginForm.do';</script>");
	        out.flush();
	        return null;
	    }

	    ModelAndView mav = new ModelAndView();

	    // ��ǰ ������ �����ɴϴ�.
	    String product_name = request.getParameter("product_name");
	    String product_price = request.getParameter("product_price");
	    String product_code = request.getParameter("product_code");
	    String product_size = request.getParameter("product_size");
	    int quantity = Integer.parseInt(request.getParameter("quantity"));
	    
	    // ��� ������ �����մϴ�.
	    String delivery = "������";

	    // ��ǰ ������ ��� ����, ��ٱ��� ����� orderForm.jsp�� �����մϴ�.
	    Map<String, Object> cart = new HashMap<>();
	    cart.put("product_code", product_code);
	    cart.put("product_name", product_name);
	    cart.put("product_price", Integer.parseInt(product_price));
	    cart.put("quantity", quantity);
	    cart.put("product_size", product_size);
	    cart.put("delivery", delivery);

	    mav.addObject("cartList", Arrays.asList(cart));

	    mav.addObject("fromCart", false);

	    mav.setViewName("orderForm");

	    return mav;
	}
	
	// ��� Ȯ�� 1�� 
	@RequestMapping(value="/member/getStock.do", method = RequestMethod.POST)
	@ResponseBody
	public Object getStock(HttpServletRequest request, HttpServletResponse response) {
		
		response.setContentType("application/json");
	    
	    System.out.println("getStock ��� Ȯ��");
	    
	    // ��ǰ ������ �����ɴϴ�.
	    String product_code = request.getParameter("product_code");
	    String product_size = request.getParameter("product_size");
	    int quantity = Integer.parseInt(request.getParameter("quantity"));

	    // productOptionVO�� �����ͼ� ����� Ȯ���մϴ�.
	    ProductOptionVO productOptionVO = this.memberDAO.getProductOption3(product_code, product_size);
	    if (productOptionVO.getQuantity() <= 0) {
	    	 
	    	System.out.println("���� ��� : " + productOptionVO.getQuantity());
	        return "out of stock";
	    }
	    
	    return "success";
	}


	// ��� Ȯ�� ������
	@RequestMapping(value="/member/getStock2.do", method = RequestMethod.POST)
	@ResponseBody
	public Object getStock2(HttpServletRequest request, HttpServletResponse response) {
	    response.setContentType("application/json");

	    // ��ǰ ������ �����ɴϴ�.
	    String[] productCodes = request.getParameterValues("product_code");
	    String[] productSizes = request.getParameterValues("product_size");
	    String[] quantities = request.getParameterValues("quantity");

	    List<Map<String, Object>> outOfStockProducts = new ArrayList<>();
	    // productOptionVO�� �����ͼ� ����� Ȯ���մϴ�.
	    for (int i = 0; i < productCodes.length; i++) {
	        ProductOptionVO productOptionVO = this.memberDAO.getProductOption4(productCodes[i], productSizes[i]);
	        if (productOptionVO.getQuantity() <= 0) {
	            System.out.println("����� 0�� ��ǰ: " + productCodes[i] + ", " + productSizes[i] + ", " + quantities[i]);
	            Map<String, Object> outOfStockProduct = new HashMap<>();
	            outOfStockProduct.put("productCode", productCodes[i]);
	            outOfStockProduct.put("productSize", productSizes[i]);
	            outOfStockProduct.put("quantity", quantities[i]);
	            outOfStockProducts.add(outOfStockProduct);
	            return "{\"result\":\"out of stock\"}";
	        }
	    }

	    if (outOfStockProducts.size() > 0) {
	        // ����� ������ ��ǰ�� �ִ� ���
	        Map<String, Object> result = new HashMap<>();
	        result.put("result", "out of stock");
	        result.put("outOfStockProducts", outOfStockProducts);
	        return result;
	    } else {
	        // ����� ������ ��ǰ�� ���� ���
	        return "{\"result\":\"success\"}";
	    }
	}
	
	
	
	
	// buyComplete update
	@RequestMapping(value="/member/buyConfirm.do", method=RequestMethod.POST)
	@ResponseBody
	public String buyConfirm(@RequestParam("orderProduct_num") int orderProduct_num,HttpServletRequest request, HttpServletResponse response) throws IOException {
	    
		System.out.println("buyComplete update");
		
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
	    this.memberDAO.updateBuyComplete(userID, orderProduct_num);
	    return "success";
	}
	
	// ȯ�� ��û update
	@RequestMapping(value="/member/refundConfirm.do", method=RequestMethod.POST)
	@ResponseBody
	public String refundConfirm(@RequestParam("orderProduct_num") int orderProduct_num, @RequestParam("order_state") String order_state,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
	    
		System.out.println("refundConfirm update");
		
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
	    this.memberDAO.updateRefundConfirm(userID, orderProduct_num, order_state);
	    return "success";
	}
	
	
}
