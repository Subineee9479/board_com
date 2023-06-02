<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.oreilly.servlet.MultipartRequest" %>
<%@ page import="com.oreilly.servlet.multipart.DefaultFileRenamePolicy" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>글쓰기</title>
<!-- 	<script>
		let isEmpty = function(value){
			if( value == "" || value == null || value == indefined ||
					(value != null && typeof value == "object" && !Object.keys(value).length)) {
				return true;
			} else {
				return false;
			}
		};
		
		let alert_message = "${alert_message}";
		if(!isEmpty(alert_message)){
			alert(alert_message);
		}
	</script> -->
</head>
<body>
	<h2>글쓰기</h2>
	
	<script>
	/* 글자수 세기 */
	function countCharacters(element, limit) {
	    var value = element.value;
	    var characters = value.length;

	    if (characters > limit) {
	        element.value = value.substring(0, limit);
	        characters = limit;
	    }

	    if (element.tagName === 'INPUT') {
	        document.getElementById('counterTitle').innerHTML = "입력된 글자 수: " + characters + "자/30자";
	    } else if (element.tagName === 'TEXTAREA') {
	        var bytes = new TextEncoder().encode(value).length;
	        document.getElementById('counterContent').innerHTML = "입력된 바이트 수: " + bytes + "bytes/1000bytes";
	    }
	}
	</script>
	
		<%
     	String user_id = (String) session.getAttribute("user_id"); 
        out.println(user_id +"님 로그인 중"); %>

	<form action="writeAction" method="post" enctype="multipart/form-data">
		<table width="600" cellpadding="0" cellspacing="0" border="1">
		
			<tr>
				<td>글 종류</td>
				<td><select name="board_name">
					<option value="공지">공지</option>
					<option value="뉴스">뉴스</option>
					<option value="자유">자유</option>
				</select></td>
			</tr>
			<tr>
				<td>글 제목</td>
				<td><input type="text" name="board_title" value="" size="50" oninput="countCharacters(this, 30)"></td>
				<div id="counterTitle"></div>
			</tr>
			<tr>
				<td>글 내용</td>
				<td>
					<textarea rows="10" cols="50" name="board_content" oninput="countCharacters(this, 1000)"></textarea>
					<div id="counterContent"></div>
				</td>
			</tr>

			<tr>
				<td>작성자</td>
				<td><input type="text" name="user_id" size="50" value="${user_id }"></td>
			</tr>
			<tr>
			<td>파일</td>
			<td><input type="file" name="file"></td>	
			</tr>
		 	<tr>
				<td>비밀번호</td>
				<td><input type="password" name="non_user_pw" minlength="4" maxlength="4"></td>
			</tr> 
			<tr>
				<td colspan="2">
					&nbsp;&nbsp;<input type="submit" value="글쓰기">&nbsp;&nbsp;
					<a href="listForm"><input type="button" value="목록보기"></a>
				</td>	
			</tr>
		</table>
	</form>
</body>
</html>