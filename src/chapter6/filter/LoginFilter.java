package chapter6.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

import chapter6.beans.User;

@WebFilter(urlPatterns = { "/setting", "/edit" })
public class LoginFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		//ServletRequestからHttpServletRequestにキャストする必要がある。
		//変数でのキャスト（型変換）をする
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;

		HttpSession session = req.getSession();
		User loginUser = (User) session.getAttribute("loginUser");

		//ログインしていなければログイン画面に遷移
		if (loginUser != null) {
			//サーブレットを実行
			chain.doFilter(request, response);
		} else {

			List<String> errorMessages = new ArrayList<String>();
			errorMessages.add("ログインしてください");
			//セッションにエラーメッセージを詰める
			session.setAttribute("errorMessages", errorMessages);
			//リダイレクト
			res.sendRedirect("./login");
		}
	}

	@Override
	public void init(FilterConfig config) {
	}

	@Override
	public void destroy() {
	}
}
