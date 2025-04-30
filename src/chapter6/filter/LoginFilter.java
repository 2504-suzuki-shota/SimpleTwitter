//package chapter6.filter;
//
//import java.io.IOException;
//
//import javax.servlet.Filter;
//import javax.servlet.FilterChain;
//import javax.servlet.FilterConfig;
//import javax.servlet.ServletException;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.annotation.WebFilter;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//
//@WebFilter(urlPatterns = {"/setteing", "/edit"})
//public class LoginFilter implements Filter {
//
//	@Override
//	public void doFilter(ServletRequest request, ServletResponse response,
//			FilterChain chain) throws IOException, ServletException {
//
//		//ServletRequestをHttpServletRequestに型変更
//		HttpServletRequest httpRequest = (HttpServletRequest)request;
//		HttpServletResponse httpResponse = (HttpServletResponse)response;
//		HttpSession session = httpRequest.getSession();
//
//		if (session.getAttribute("loginUser") == null) {
//			httpResponse.sendRedirect("/login");
//		}
//
//	//問題なければサーブレットへ
//		chain.doFilter(httpRequest, httpResponse); // サーブレットを実行
//	}
//
//	@Override
//	public void init(FilterConfig config) {
//	}
//
//	@Override
//	public void destroy() {
//	}
//
//}
