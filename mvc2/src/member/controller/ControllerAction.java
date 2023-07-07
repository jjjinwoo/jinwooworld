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
		// Iterator媛?泥대?? Enumeration 媛?泥대?? ???μ???? 媛????? 媛뱀껜
		Iterator<Object> keylter = pr.keySet().iterator();
		// 媛?泥대?? ?????? 爰쇰?댁?? 洹? 媛?泥대??쇰? Properties媛?泥댁?? ???λ?? 媛?泥댁?? ??洹?
		while (keylter.hasNext()) {
			String command = (String) keylter.next();
			String className = pr.getProperty(command);
			try {// ?대?? 臾몄???댁?? ?대???ㅻ? 留?????
				@SuppressWarnings("rawtypes")
				Class commandClass = Class.forName(className);
				Object commandInstance = commandClass.newInstance();// ?대?뱁?대???ㅼ??
																	// 媛?泥대?? ????
				System.out.println(command + "         " + commandInstance);
				// Map媛?泥댁?? commandMap?? 媛?泥? ????
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
	
	public void doGet(// get諛⑹???? ??鍮??? 硫?????
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		requestPro(request, response);
	}

	protected void doPost(// post 諛⑹???? ??鍮??? 硫?????) {
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		requestPro(request, response);
	}
	
	// ?ъ?⑹???? ??泥??? 遺????댁?? ???? ?????? 泥?由?
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
		// include 사용시 로그인 접속 안됨
		/*
		forward는 현재 서블릿에서 처리가 완료된 후 다른 리소스로 요청을 전달합니다. 즉, 현재 서블릿의 처리가 완료되면 다른 리소스로 제어가 이동하고, 다른 리소스의 처리 결과가 클라이언트에게 전송됩니다.
		include는 현재 서블릿에서 처리가 완료되지 않은 상태에서 다른 리소스로 요청을 전달합니다. 즉, 다른 리소스의 처리 결과가 현재 서블릿의 출력 스트림에 추가되고, 현재 서블릿의 처리가 계속됩니다.
		위의 코드에서 forward 대신 include를 사용하면 오류가 발생했다고 합니다. 이 문제는 include를 사용하면 현재 서블릿의 출력 스트림에 이미 데이터가 기록되어 있기 때문일 수 있습니다. 이 경우, 
		IllegalStateException이 발생할 수 있습니다. 이 문제를 해결하려면 include를 호출하기 전에 출력 스트림을 비워야 합니다.
		*/
	}
	
}
