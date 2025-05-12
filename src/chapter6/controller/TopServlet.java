package chapter6.controller;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import chapter6.beans.User;
import chapter6.beans.UserMessage;
import chapter6.logging.InitApplication;
import chapter6.service.MessageService;

@WebServlet(urlPatterns = { "/index.jsp" })
public class TopServlet extends HttpServlet {

	/**
	* ロガーインスタンスの生成
	*/
	Logger log = Logger.getLogger("twitter");

	/**
	* デフォルトコンストラクタ
	* アプリケーションの初期化を実施する。
	*/
	public TopServlet() {
		InitApplication application = InitApplication.getInstance();
		application.init();

	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		log.info(new Object(){}.getClass().getEnclosingClass().getName() +
		        " : " + new Object(){}.getClass().getEnclosingMethod().getName());

		/*//セッションからログインユーザーを取得し、ログインユーザーのオブジェクトが取得できた
		 * 場合(nullではなかった場合)には変数isShowMessageFormにtrueを設定するというコード。*/
		//変数の宣言、falseをセットしている。
		boolean isShowMessageForm = false;
		//ログイン情報を取得、LoginServletでログイン情報をセッションに格納しているので、

		User user = (User) request.getSession().getAttribute("loginUser");
		//ログインしていたらteueになり、つぶやきフォームを表示（top.jsp)
		if (user != null) {
			isShowMessageForm = true;
		}
		//リンクが押された時にIDでつぶやきを絞る為に、top.jspからuser_idを取得
		String userId = request.getParameter("user_id");
		/*セレクトメソッドを呼び出し（引数userId）、Daoから帰ってきた情報を
		List<UserMessage> messagesに格納している*/
		List<UserMessage> messages = new MessageService().select(userId);

		//requestにセットしてtop.jspにforward(request, response)している。
		request.setAttribute("messages", messages);
		request.setAttribute("isShowMessageForm", isShowMessageForm);
		request.getRequestDispatcher("/top.jsp").forward(request, response);
	}
}
