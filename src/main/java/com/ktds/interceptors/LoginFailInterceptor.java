package com.ktds.interceptors;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class LoginFailInterceptor extends HandlerInterceptorAdapter{
	private Map<String, Integer> failIdMap; 	
	
	public LoginFailInterceptor() {
		failIdMap = new HashMap<String, Integer>();
	}
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object controller)
			throws Exception {
		if (request.getMethod().equalsIgnoreCase("post")) { // 이 코드가 없으면 null로 전달됨
			String userId = request.getParameter("email");
			String password = request.getParameter("password");
			
//			if (userId.equals("admin")) {
//				if (!failIdMap.containsKey(userId)) {
//					failIdMap.put(userId, 0);
//				}
//				if (!password.equals("1234")) {
//					int failCount = failIdMap.get(userId);
//					failIdMap.put(userId, ++failCount);
//				}
//				if (failIdMap.get(userId) > 3) {
//					RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/view/error/403.jsp");
//					rd.forward(request, response);
//					return false;
//				}
//			}
		}
		return true;
	}

	
}
