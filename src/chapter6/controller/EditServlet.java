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

import chapter6.beans.Message;
import chapter6.logging.InitApplication;
import chapter6.service.MessageService;


@WebServlet(urlPatterns = { "/edit" })
public class EditServlet extends HttpServlet {

	    /**
	    * ロガーインスタンスの生成
	    */
	    Logger log = Logger.getLogger("twitter");

	    /**
	    * デフォルトコンストラクタ
	    * アプリケーションの初期化を実施する。
	     * @return
	    */
	    public EditServlet() {
	        InitApplication application = InitApplication.getInstance();
	        application.init();

	    }

	 @Override
	 protected void doPost(HttpServletRequest request, HttpServletResponse response)
	            throws IOException, ServletException {

		log.info(new Object(){}.getClass().getEnclosingClass().getName() +
			" : " + new Object(){}.getClass().getEnclosingMethod().getName());

		//ここでjspで表記した"text"をgetParameterを使って扱えるようにする。
		String text = request.getParameter("text");
		String number = request.getParameter("messageid");
		//エラーメッセージの宣言とセッションに格納
		List<String> errorMessages = new ArrayList<String>();

		int messageId = Integer.parseInt(number);

		//idとtextが入ったmessage変数をupdateの引数に入れてあげる。
		//message型に値を入れる時は.setで入れる。取り出す時はgetを使う。
		Message message = new Message();
		message.setText(text);
		message.setId(messageId);

		//エラーのif文をisvalidのように別で用意
		if (!isValid(text, errorMessages)) {
			//
			request.setAttribute("errorMessages", errorMessages);
			request.setAttribute("message", message);
			request.getRequestDispatcher("/edit.jsp").forward(request, response);
			return;
		}

		new MessageService().update(message);

		//リダイレクトは外部サーバー上のページに移動したいときに使う。
		/*top.jspに遷移*/
		response.sendRedirect("./");
	}

	 //doGetはページの取得を要求する際に使う。
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {

		log.info(new Object(){}.getClass().getEnclosingClass().getName() +
			" : " + new Object(){}.getClass().getEnclosingMethod().getName());

		String number = request.getParameter("messageid");
		List<String> errorMessages = new ArrayList<String>();
		HttpSession session = request.getSession();

		//エラーチェックif
		//「^…先頭」「[0-9]の間の数字は通す」「{11}」桁数を指定 $…末尾」
		//URLの末尾が空白と数字以外の時はtrueにしたい。
		if (StringUtils.isBlank(number) || !number.matches("^[0-9]+$")) {
			errorMessages.add("不正なパラメータが入力されました。");
			session.setAttribute("errorMessages", errorMessages);
			response.sendRedirect("./");
			return;
		}

		int messageId = Integer.parseInt(number);

		//UserServiceクラスのselectメソッドを使って、ログイン中のユーザー（loginUser）の
		//IDを元にデータベースからユーザー情報を再取得している。
		//loginUserのIDを使ってデータベースからユーザーの情報を取り出してる。
		Message message = new MessageService().select(messageId);

		if (message == null) {
			errorMessages.add("不正なパラメータが入力されました。");
			session.setAttribute("errorMessages", errorMessages);
			response.sendRedirect("./");
			return;
		}

		request.setAttribute("message", message);
		//フォワードはサーバ―内の転送のみ使える。リクエストやパラメーター属性を共有できる。
		//forward内の引数の(request, response)はこう表記するルールできまっている。
		request.getRequestDispatcher("edit.jsp").forward(request, response);
	}

	private boolean isValid(String text, List<String> errorMessages) {

		log.info(new Object(){}.getClass().getEnclosingClass().getName() +
		 " : " + new Object(){}.getClass().getEnclosingMethod().getName());

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