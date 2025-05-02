package chapter6.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter(urlPatterns = {"/setting", "/edit"})
public class LoginFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		//ServletRequestをHttpServletRequestに型変更
		HttpServletRequest httpRequest = (HttpServletRequest)request;
		HttpServletResponse httpResponse = (HttpServletResponse)response;

		//セッション獲得
		HttpSession session = httpRequest.getSession();

		//ログインしていない場合 ログイン情報のヌルチェック
		if (session.getAttribute("loginUser") == null) {
			//ここでエラーメッセージも用意する
			String errorMessage = "ログインしてください！！";
			session.setAttribute("errorMessages", errorMessage);
			//ログイン画面表示したい→ログインサーブレットのdoGetを実行すれば表示されるからリダイレクト
			httpResponse.sendRedirect("./login");
		} else {
			// サーブレットを実行
			chain.doFilter(request, response);
		}
	}

	@Override
	public void init(FilterConfig config) {
	}

	@Override
	public void destroy() {
	}
}