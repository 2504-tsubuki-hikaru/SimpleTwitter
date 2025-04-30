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

import chapter6.beans.User;
import chapter6.exception.NoRowsUpdatedRuntimeException;
import chapter6.logging.InitApplication;
import chapter6.service.UserService;

@WebServlet(urlPatterns = { "/setting" })
public class SettingServlet extends HttpServlet {

	/**
	* ロガーインスタンスの生成
	*/
	Logger log = Logger.getLogger("twitter");

	/**
	* デフォルトコンストラクタ
	* アプリケーションの初期化を実施する。
	*/
	public SettingServlet() {
		InitApplication application = InitApplication.getInstance();
		application.init();

	}

	//doGetはページの取得を要求する際に使う。
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());

		HttpSession session = request.getSession();
		User loginUser = (User) session.getAttribute("loginUser");

		/*//UserServiceクラスのselectメソッドを使って、ログイン中のユーザー（loginUser）の
		IDを元にデータベースからユーザー情報を再取得している。
		loginUserのIDを使ってデータベースからユーザーの情報を取り出してる。*/
		User user = new UserService().select(loginUser.getId());

		request.setAttribute("user", user);
		request.getRequestDispatcher("setting.jsp").forward(request, response);
	}

	/*doPOST()は入力画面でよく使われる。method=“POST”を指定したフォームに入力したデータを
	 * サーバーに転送する際に使用されます。* */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());

		HttpSession session = request.getSession();
		List<String> errorMessages = new ArrayList<String>();

		//送られてきたデータをuserに入れる
		User user = getUser(request);
		//エラーチェックをしている
		if (isValid(user, errorMessages)) {
			try {
				new UserService().update(user);
			} catch (NoRowsUpdatedRuntimeException e) {
				log.warning("他の人によって更新されています。最新のデータを表示しました。データを確認してください。");
				errorMessages.add("他の人によって更新されています。最新のデータを表示しました。データを確認してください。");
			}
		}
		//エラーメッセ―ジ変数の要素数が0でないならjspにフォワードをしてエラーを返す。
		if (errorMessages.size() != 0) {
			request.setAttribute("errorMessages", errorMessages);
			request.setAttribute("user", user);
			request.getRequestDispatcher("setting.jsp").forward(request, response);
			return;
		}
		/* //session.setAttributeはサーバ―にデータを保存するメソッド。loginUserという名前で情報を保存する。
		キーloginUser、値がuserになる。*/
		session.setAttribute("loginUser", user);
		response.sendRedirect("./");
	}

	/*//このメソッドはHTTPから送られてきたユーザーを元にUserオブジェクトを作る処理
	HttpServletRequest request=HTPPから送られてきたデータを受け取ること。
	throwsは例外が起こる可能性があるということ。IOException, ServletException は例外の種類で、このコードを入れておけば
	再利用や、将来的な変更がしやすい。*/
	private User getUser(HttpServletRequest request) throws IOException, ServletException {

		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());

		User user = new User();
		user.setId(Integer.parseInt(request.getParameter("id")));
		user.setName(request.getParameter("name"));
		user.setAccount(request.getParameter("account"));
		user.setPassword(request.getParameter("password"));
		user.setEmail(request.getParameter("email"));
		user.setDescription(request.getParameter("description"));
		return user;
	}

	/*このメソッドはUserメソッドの入力値を検証するための処理である。
	 * エラーがあればerrorMessagesリストに追加する（false=エラー）*/
	private boolean isValid(User user, List<String> errorMessages) {

		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());

		String name = user.getName();
		int id = user.getId();
		String account = user.getAccount();
		String email = user.getEmail();

		//重複を確認する為の確認メソッドをここに呼び出したい。
		User confirmAccount = new UserService().select(account);

		int accountId = confirmAccount.getId();

		//重複していたらlistの0番目が帰ってくる、していなかったらnullが帰ってくる。
		//nullと自分のアカウント情報ではなかった時にエラーを表示する。
		// 入力したアカウント名を表示
		// accountId→データベースから参照したid
		// id→ログインしているユーザーのid
		// この二つをどういう条件をしたいか
		// 同じidかどうか確かめたい
		if (confirmAccount != null && accountId != id) {
			errorMessages.add("すでに存在するアカウントです");
		}
		//名前がnullや空でないときに、20文字を超えていたらエラーをadd
		if (!StringUtils.isEmpty(name) && (20 < name.length())) {
			errorMessages.add("名前は20文字以下で入力してください");
		}
		if (StringUtils.isEmpty(account)) {
			errorMessages.add("アカウント名を入力してください");
		} else if (20 < account.length()) {
			errorMessages.add("アカウント名は20文字以下で入力してください");
		}
		if (!StringUtils.isEmpty(email) && (50 < email.length())) {
			errorMessages.add("メールアドレスは50文字以下で入力してください");
		}
		if (errorMessages.size() != 0) {
			return false;
		}
		return true;
	}
}