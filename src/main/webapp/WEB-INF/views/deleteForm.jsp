<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>

	<style>
	.submit {
		 display: flex;
  		align-items: center;
	}
	</style>
<meta charset="UTF-8">
<title>글 내용</title>
</head>
<body>
	<h2>글 수정!</h2>
		<%
     	String user_id = (String) session.getAttribute("user_id"); 
        out.println(user_id +"님 로그인 중"); %>
	
	<form action="deleteAction" method="post">
		<table width="500" cellpadding="0" cellspacing="0" border="1">
			<input type="hidden" name="board_idx" value="${dto.board_idx}"/>
			<input type="hidden" name="user_id" value="${sessionScope.user_id}"/>
			
			<tr>
				<td>글 번호</td>
				<td>${dto.board_idx }</td>
			</tr>
			<tr>
				<td>조회수</td>
				<td>${ dto.board_hit }</td>
			</tr>
			<tr>
				<td>글 종류</td>
				<td><input type="text" name="board_name" value="${dto.board_name }" readonly size="50"></td>
			</tr>
			<tr>
				<td>글 제목</td>
				<td><input type="text" name="board_title" value="${dto.board_title }" size="50"></td>
			</tr>
			<tr>
				<td>글 내용</td>
				<td>
					<textarea rows="10" cols="50"  name="board_content" >${dto.board_content }</textarea> 
					
				</td>
			</tr>
			<c:if test="${not empty fdto.file_real_name }">
				<tr>
					<td>첨부파일</td>
 					<td>					
						<a href="downloadFile?file_save_name=${fdto.file_save_name}"> ${fdto.file_real_name }</a>
							
					</td> 
				</tr>
			</c:if>	
			<tr>
				<td>작성자</td>
				<td><input type="text" name="user_id" value="${dto.user_id }" readonly size="50"></td>
			</tr>
			<tr>
				<td>비밀번호</td>
				<td><input type="password" name="non_user_pw" value=""></td>
			</tr> 
			<tr>
				<td colspan="2">
					<div class=submit>
					<!-- &nbsp;&nbsp;<input type="submit" value="수정하기">&nbsp;&nbsp; -->
					
					 <a href="deleteAction?board_idx=${dto.board_idx}&user_id=${dto.user_id}&non_user_pw=${dto.non_user_pw}">
					<%--  <td><a href="deleteReplyAction?reply_idx=${reply_dto.reply_idx }&board_idx=${ dto.board_idx}"><button>삭제</button></a></td>	 --%>
					<button>삭제</button></a>&nbsp;&nbsp; 
					
				
					<a href="listForm"><input type="button" value="목록보기"></a>&nbsp;&nbsp;</div>
					</td>							
			</tr>
		</table>
	 </form> 	
	<br>
	

	
<%-- 	<form action="writeReplyAction" method="post">
		<table width="500" cellpadding="0" cellspacing="0" border="1">
			<tr>
				<td colspan="2">
					<input type="hidden" name="reply_board_index" value="${dto.board_idx }"> <!-- 어느게시글인지 알기위해서 hidden으로 숨겨서 넣어줌 -->
					<label>댓글</label><textarea rows="2" cols="50" name="reply_content"></textarea><br>
					<label>작성자</label><input type="text" name="reply_name" value="${user_id}" readonly>
					<input type="submit" value="댓글달기">
				</td>
			</tr>
		</table>
	</form>
	
	<br> --%>

	
<%-- 	<table width="500" cellpadding="0" cellspacing="0" border="1">
		<tr>
			<th>작성자</th>
			<th>내용</th>
			<th>날짜</th>
			<th>삭제</th>	
		</tr>
		<c:forEach var="reply_dto" items="${ reply_list }">
		<tr>
			<td>${reply_dto.reply_name }</td>
			<td>${reply_dto.reply_content }</td>
			<td>
				<c:set var="dateVar" value="${ reply_dto.reply_date }"/>
				<fmt:formatDate value="${dateVar}" pattern="yyyy-MM-dd"/>	
			</td>
			
			<c:if test="${sessionScope.user_id eq reply_dto.reply_name }">
			<td><a href="deleteReplyAction?reply_idx=${reply_dto.reply_idx }&board_idx=${ dto.board_idx}"><button>삭제</button></a></td>	
			</c:if>
		</tr>
		</c:forEach>
	</table> --%>
	

</body>
</html>