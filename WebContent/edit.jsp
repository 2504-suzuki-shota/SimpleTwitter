<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>つぶやき編集画面</title>
	</head>

	<body>
		<div class="main-contents">
			<!-- エラー処理 -->
			<c:if test="${ not empty errorMessages }">
				<div class="errorMessages">
					<ul>
						<c:forEach items="${errorMessages}" var="errorMessage">
							<li><c:out value="${errorMessage}" />
						</c:forEach>
					</ul>
				</div>
			</c:if>

			<!-- action先がある@持ちのサーブレットに移動してmethodのdoPostが呼び出される -->
			<form action="edit" method="post"><br />
				つぶやき<br />
				<textarea name="text" cols="100" rows="5" class="tweet-box"><c:out value="${edit.text}" /></textarea>
				<input type="hidden" name="id" value="${edit.id}"><br />
				<input type="submit" value="更新" /><br />
			</form>
			<a href="./">戻る</a>
			<div class="copyright">Copyright(c)Suzuki Shota</div>
		</div>
	</body>
</html>