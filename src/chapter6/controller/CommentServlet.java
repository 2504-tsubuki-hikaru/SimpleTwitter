package chapter6.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import chapter6.beans.Comment;
import chapter6.beans.User;
import chapter6.logging.InitApplication;
import chapter6.service.CommentService;

@WebServlet(urlPatterns = { "/comment" })
public class CommentServlet extends HttpServlet {

	// ロガーインスタンスの生成

	Logger log = Logger.getLogger("twitter");

	/**
	* デフォルトコンストラクタ
	* アプリケーションの初期化を実施する。
	*/
	public CommentServlet() {
		InitApplication application = InitApplication.getInstance();
		application.init();

	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());

		//セッションからユーザーの情報を取得（loginUserをセッションから取得）
		HttpSession session = request.getSession();
		//List<String> errorMessages = new ArrayList<String>();

		//入力フォームで送信されたつぶやきを取得し、text変数に格納(top.jspで設定している、getParameter()でtextを取得)
		String text = request.getParameter("text");
		String number = request.getParameter("messageid");

		List<String> errorMessages = new ArrayList<String>();

		if (!isValid(text, errorMessages)) {
			session.setAttribute("errorMessages", errorMessages);
			response.sendRedirect("./");
			return;
		}
		//isValidメソッドでエラーがあれば、エラーメッセージをセッションにセットしてtop.jspにリダイレクト
		/*if (!isValid(text, errorMessages)) {
			session.setAttribute("errorMessages", errorMessages);
			response.sendRedirect("./");
			return;
		}*/

		int messageId = Integer.parseInt(number);

		Comment comments = new Comment();
		//メッセージ型のsetTextメソッドを使って、textをセット
		comments.setText(text);
		comments.setMessageId(messageId);

		//セッションからログインユーザーの情報を取得し、Userにidを格納する。
		//これをすることでつぶやきがどのユーザ―の物か判断できる。
		User user = (User) session.getAttribute("loginUser");
		comments.setUserId(user.getId());

		new CommentService().insert(comments);

		/*top.jspに遷移*/
		response.sendRedirect("./");
	}

	private boolean isValid(String text, List<String> errorMessages) {

		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());

		if (StringUtils.isBlank(text)) {
			errorMessages.add("メッセージを入力してください");
		} else if (140 < text.length()) {
			errorMessages.add("140文字以下で入力してください");
		}
		if (errorMessages.size() != 0) {
			return false;
		}
		return true;
	}
}
