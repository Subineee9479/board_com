<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%--    <% if ("nonExistId".equals(request.getAttribute("loginFailed"))) { %>
      <script>
         console.log("아이디가 존재하지 않습니다2");
         alert('아이디가 존재하지 않습니다!');
         window.location.href = '/login';
      </script>
   <% } else if ("wrongPw".equals(request.getAttribute("loginFailed"))) { %>
      <script>
         alert('비밀번호가 틀렸습니다!');
         window.location.href = '/login';
      </script>
   <% } %>  --%>
   
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>로그인 페이지</title>

</head>
<body>
	<h2>로그인</h2>
	<form action="login" method="post">
		<table border="1" width="400">
			<tr>
				<td>아이디</td>
				<td><input type="text" name="user_id"></td>
			</tr>
			<tr>
				<td>비밀번호</td>
				<td><input type="password" name="user_pw"></td>
			</tr>
			<tr>
				<td colspan="2" align="center">
					<input type="submit" value="로그인"></input>
				</td>
			</tr>
		</table>	

	</form>

</body>
</html> 