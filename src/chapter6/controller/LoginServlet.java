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

import chapter6.beans.User;
import chapter6.logging.InitApplication;
import chapter6.service.UserService;

@WebServlet(urlPatterns = { "/login" })
public class LoginServlet extends HttpServlet {

    /**
    * ロガーインスタンスの生成
    */
    Logger log = Logger.getLogger("twitter");
    /**
     * デフォルトコンストラクタ
     * アプリケーションの初期化を実施する。
     */
     public LoginServlet() {
         InitApplication application = InitApplication.getInstance();
         application.init();

     }

     @Override
     protected void doGet(HttpServletRequest request, HttpServletResponse response)
             throws IOException, ServletException {

 	  log.info(new Object(){}.getClass().getEnclosingClass().getName() +
         " : " + new Object(){}.getClass().getEnclosingMethod().getName());

 	  		//ユーザーがログインページにアクセスしたときにtop.jspを表示
         request.getRequestDispatcher("login.jsp").forward(request, response);
     }

     @Override
     protected void doPost(HttpServletRequest request, HttpServletResponse response)
             throws IOException, ServletException {

 	  log.info(new Object(){}.getClass().getEnclosingClass().getName() +
         " : " + new Object(){}.getClass().getEnclosingMethod().getName());

 	  	//入力フォームから送信されたアカウントかメールアドレスとパスワードを取得し変数に格納
         String accountOrEmail = request.getParameter("accountOrEmail");
         String password = request.getParameter("password");

         //DBからログイン情報を取得し(SELECTでデータの参照)、User型のuser変数に格納
         User user = new UserService().select(accountOrEmail, password);
         //select結果がnullの場合、ログインに失敗しましたをセットしフォワード
         if (user == null) {
             List<String> errorMessages = new ArrayList<String>();
             errorMessages.add("ログインに失敗しました");
             request.setAttribute("errorMessages", errorMessages);
             request.getRequestDispatcher("login.jsp").forward(request, response);
             return;
         }
         //ログインユーザー情報をsessionに格納している。
         HttpSession session = request.getSession();
         /*セッションの無効化をするまでloginUserが使える、userの中にはログインした時に取得した
         ユーザー情報が入っている。*/
         
         //sessionにセットしている。userログインユーザーの情報を(accountOrEmail, password)
         session.setAttribute("loginUser", user);
         //("./")=TopServletに遷移
         response.sendRedirect("./");
     }
 }
