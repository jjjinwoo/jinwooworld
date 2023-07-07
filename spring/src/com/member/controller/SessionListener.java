package com.member.controller;

import java.util.Map; 
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.member.VO.MemberVO;


@WebListener
public class SessionListener implements HttpSessionListener {
	
	private static final Map<String, HttpSession> sessions = new ConcurrentHashMap<>();
	
    //중복로그인 지우기
	public synchronized static String getSessionidCheck(String type, String compareId){
	    String result = "";
	    for(String key : sessions.keySet() ){
	    	HttpSession hs = sessions.get(key);
	    	MemberVO memberVO = new MemberVO();
	    	if(hs != null) {
	    		memberVO = (MemberVO) hs.getAttribute(type);
				if(memberVO != null && memberVO.getUserID().toString().equals(compareId)) {
					result = key.toString();
				}
			}
	    	
	    }
	    removeSessionForDoubleLogin(result);
	    return result;
	}	
	
	/*
	 * private static void removeSessionForDoubleLogin(String userID){
	 * System.out.println("remove userID : " + userID); if(userID != null &&
	 * userID.length() > 0){ sessions.get(userID).invalidate();
	 * sessions.remove(userID); } }
	 */
	
    private static void removeSessionForDoubleLogin(String sessionID){    	
        System.out.println("remove sessionID : " + sessionID);
        if(sessionID != null && sessionID.length() > 0){
            sessions.get(sessionID).invalidate();
            sessions.remove(sessionID);    		
        }
    }
    
	@Override
	public void sessionCreated(HttpSessionEvent http) {
		System.out.println(http);
		sessions.put(http.getSession().getId(), http.getSession());
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent http) {
		
		if(sessions.get(http.getSession().getId()) != null){
            sessions.get(http.getSession().getId()).invalidate();
            sessions.remove(http.getSession().getId());	
        }
	}

}
