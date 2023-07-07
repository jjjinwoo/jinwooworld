package member.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import member.action.CommandAction;


public class ControllerAction extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private Map<String, Object> commandMap = new HashMap<String, Object>();
	

	public void init(ServletConfig config) throws ServletException {

		String props = config.getInitParameter("propertyConfig");

		Properties pr = new Properties();
		String path = config.getServletContext().getRealPath("/WEB-INF");
		FileInputStream f = null;
		try {

			f = new FileInputStream(new File(path, props));

			pr.load(f);
		} catch (IOException e) {
			throw new ServletException(e);
		} finally {
			if (f != null)
				try {
					f.close();
				} catch (IOException ex) {
				}
		}
		// Iterator�?체�?? Enumeration �?체�?? ???��???? �????? 갹체
		Iterator<Object> keylter = pr.keySet().iterator();
		// �?체�?? ?????? 꺼�?��?? �? �?체�??��? Properties�?체�?? ???��?? �?체�?? ??�?
		while (keylter.hasNext()) {
			String command = (String) keylter.next();
			String className = pr.getProperty(command);
			try {// ?��?? 문�???��?? ?��???��? �?????
				@SuppressWarnings("rawtypes")
				Class commandClass = Class.forName(className);
				Object commandInstance = commandClass.newInstance();// ?��?��?��???��??
																	// �?체�?? ????
				System.out.println(command + "         " + commandInstance);
				// Map�?체�?? commandMap?? �?�? ????
				commandMap.put(command, commandInstance);
			} catch (ClassNotFoundException e) {
				throw new ServletException(e);
			} catch (InstantiationException e) {
				throw new ServletException(e);
			} catch (IllegalAccessException e) {
				throw new ServletException(e);
			}

		}
	}
	
	public void doGet(// get방�???? ??�??? �?????
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		requestPro(request, response);
	}

	protected void doPost(// post 방�???? ??�??? �?????) {
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		requestPro(request, response);
	}
	
	// ?��?��???? ??�??? �????��?? ???? ?????? �?�?
	private void requestPro(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		//response.setContentType("text/html; charset=UTF-8");
		String view = null;
		CommandAction com = null;
		request.setCharacterEncoding("utf-8");
		try {
			String command = request.getRequestURI();
			if (command.indexOf(request.getContextPath()) == 0) {
				command = command.substring(request.getContextPath().length());
			}
			com = (CommandAction) commandMap.get(command);

			view = com.requestPro(request, response);
		} catch (Throwable e) {
			throw new ServletException(e);
		}
		if (!response.isCommitted()) {
	         RequestDispatcher dispatcher = request.getRequestDispatcher(view);
	         dispatcher.forward(request, response);
	    }
		// include ���� �α��� ���� �ȵ�
		/*
		forward�� ���� �������� ó���� �Ϸ�� �� �ٸ� ���ҽ��� ��û�� �����մϴ�. ��, ���� ������ ó���� �Ϸ�Ǹ� �ٸ� ���ҽ��� ��� �̵��ϰ�, �ٸ� ���ҽ��� ó�� ����� Ŭ���̾�Ʈ���� ���۵˴ϴ�.
		include�� ���� �������� ó���� �Ϸ���� ���� ���¿��� �ٸ� ���ҽ��� ��û�� �����մϴ�. ��, �ٸ� ���ҽ��� ó�� ����� ���� ������ ��� ��Ʈ���� �߰��ǰ�, ���� ������ ó���� ��ӵ˴ϴ�.
		���� �ڵ忡�� forward ��� include�� ����ϸ� ������ �߻��ߴٰ� �մϴ�. �� ������ include�� ����ϸ� ���� ������ ��� ��Ʈ���� �̹� �����Ͱ� ��ϵǾ� �ֱ� ������ �� �ֽ��ϴ�. �� ���, 
		IllegalStateException�� �߻��� �� �ֽ��ϴ�. �� ������ �ذ��Ϸ��� include�� ȣ���ϱ� ���� ��� ��Ʈ���� ����� �մϴ�.
		*/
	}
	
}
