package chapter6.controller;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import chapter6.logging.InitApplication;
import chapter6.service.MessageService;


@WebServlet(urlPatterns = { "/deleteMessage" })
public class DeleteMessageServlet extends HttpServlet {

	    /**
	    * ロガーインスタンスの生成
	    */
	    Logger log = Logger.getLogger("twitter");

	    /**
	    * デフォルトコンストラクタ
	    * アプリケーションの初期化を実施する。
	     * @return
	    */
	    public DeleteMessageServlet() {
	        InitApplication application = InitApplication.getInstance();
	        application.init();

	    }

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	            throws IOException, ServletException {

		log.info(new Object(){}.getClass().getEnclosingClass().getName() +
	      " : " + new Object(){}.getClass().getEnclosingMethod().getName());

		//ここでjspで表記した"massageid"をgetParameterを使って扱えるようにする。
		String number = request.getParameter("messageid");
		int messageid = Integer.parseInt(number);

		//messageidをMessageServiceのdeleteメソッドに引数として渡したい。
		new MessageService().delete(messageid);

		/*top.jspに遷移*/
		response.sendRedirect("./");
	}
}
