<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>簡易Twitter</title>
<link href="./css/style.css" rel="stylesheet" type="text/css">
</head>
<body>
	<div class="main-contents">
		<div class="header">
			<c:if test="${ empty loginUser }">
				<a href="login">ログイン</a>
				<a href="signup">登録する</a>
			</c:if>
			<c:if test="${ not empty loginUser }">
				<a href="./">ホーム</a>
				<a href="setting">設定</a>
				<a href="logout">ログアウト</a>
			</c:if>
		</div>
		<c:if test="${ not empty loginUser }">
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
		</c:if>
		<c:if test="${ not empty errorMessages }">
			<div class="errorMessages">
				<ul>
					<c:forEach items="${errorMessages}" var="errorMessage">
						<li><c:out value="${errorMessage}" />
					</c:forEach>
				</ul>
			</div>
			<c:remove var="errorMessages" scope="session" />
		</c:if>

		<div class="form-area">
			<!-- isShowMessageFormが	trueの場合(ログインしている時)につぶやきのフォームを表示 -->
			<c:if test="${ isShowMessageForm }">
				<form action="message" method="post">
					いま、どうしてる？<br />
					<textarea name="text" cols="100" rows="5" class="tweet-box"></textarea>
					<!-- brタグは改行のこと -->
					<br /> <input type="submit" value="つぶやく">（140文字まで）
				</form>
			</c:if>
		</div>
		<div class="messages">
		<c:forEach items="${messages}" var="message">
			<div class="message">
				<div class="account-name">
					<span class="account"> <a
						href="./?user_id=<c:out value="${message.userId}"/> ">
						<c:out value="${message.account}" />
						</a>
					</span> <span class="name"><c:out value="${message.name}" /></span>
				</div>
					<div class="text">
						<pre>
							<c:out value="${message.text}" />
						</pre>
					</div>
					<div class="date">
						<fmt:formatDate value="${message.createdDate}"
							pattern="yyyy/MM/dd HH:mm:ss" />
					</div>
				<c:if test="${ isShowMessageForm }">
					<!-- deleteMessageはDeleteMessageServletクラスの16行目の/deleteMessage -->
					<!-- でデータを送る先を決めている。methodはなんのメソッドを使うか指定 -->
					<!-- ログインidとつぶやきのidが同じな場合のみボタンを表示する -->
					<c:if test="${ loginUser.id == message.userId }">
						<form action="deleteMessage" method="post">
							<input type="submit" value="削除">
							<input type="hidden" name="messageid" value="${message.id}">
						</form>
						<form action="edit" method="get">
							<input type="submit" value="編集">
							<input type="hidden"name="messageid" value="${message.id}">
						</form>
					</c:if>
				</c:if>
			</div>
		</c:forEach>
		</div>
	</div>
	<div class="copyright">Copyright(c)YourName</div>
</body>
</html>