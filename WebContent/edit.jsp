<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!-- EL式を使うかどうかの判断falseならEL式を使う -->
<%@page isELIgnored="false"%>
<!-- taglibはインポートに近い感じ、ここで宣言してあげて"c""fmt"の指定した値が使えるようになる。 -->
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!-- パッケージ・エクスプローラーの右側の開いたファイルの名前 -->
<title>つぶやきの編集</title>
<!-- スタイルシートの適応 -->
<link href="css/style.css" rel="stylesheet" type="text/css">
</head>
<body>
	<!-- class="main-contents"はCSSのスタイルシートの中にあるやつを読んでいる。 -->
	<div class="main-contents">
		<div class="header">
			<a href="./">ホーム</a> <a href="setting">設定</a> <a href="logout">ログアウト</a>
		</div>
		<div class="profile">
			<div class="name">
				<h2>
					<c:out value="${loginUser.name}" />
				</h2>
			</div>
			<div class="account">
				@
				<c:out value="${loginUser.account}" />
			</div>
			<div class="description">
				<c:out value="${loginUser.description}" />
			</div>
		</div>
		<c:if test="${ not empty errorMessages }">
			<div class="errorMessages">
				<ul>
					<!-- ループ処理を行っている。varは変数の名前を指定 -->
					<c:forEach items="${errorMessages}" var="errorMessage">
						<!-- out valueでlistの中から１つずつ順番に値を参照している。 -->
						<li><c:out value="${errorMessage}" />
					</c:forEach>
				</ul>
			</div>
			<!-- remove変数を削除するタグ、削除しないと他のjspでもエラーめっさーじの表示がされているのでずっとエラーが表示される -->
			<c:remove var="errorMessages" scope="session" />
		</c:if>
		<div class="form-area">
			<form action="edit" method="post">
				<br />
				<!-- 出力する（request.setAttributeしたmessageからtextを出す） -->
				<pre>
					<textarea name="text" cols="100" rows="5" class="tweet-box">
						<c:out value ="${message.text}" />
					</textarea>
				</pre>
				<br /> <input type="submit" value="更新">（140文字まで）
				 <input type="hidden" name="messageid" value="${message.id}">
			</form>
		</div>
	</div>
	<div class="copyright">Copyright(c)YourName</div>
</body>
</html>
