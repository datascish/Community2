package com.ktds.interceptors;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class IpInterceptor extends HandlerInterceptorAdapter{
	
		private List<String> blackList;
		
		public IpInterceptor() {
			blackList = new ArrayList<String>();
			blackList.add("192.168.201.27");
			//blackList.add("192.168.201.23");
		}
		
		@Override
		public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object controller)
				throws Exception {
		String requestorIp = request.getRemoteAddr();
		
		if (blackList.contains(requestorIp)) {
			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/view/error/403.jsp");
			rd.forward(request, response);
			return false;
		}
		return true;
	}
}
