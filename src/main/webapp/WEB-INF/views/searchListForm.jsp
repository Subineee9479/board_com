
<%@ page import="java.io.File" %>
<%@ page import="com.oreilly.servlet.multipart.DefaultFileRenamePolicy" %>
<%@ page import="com.oreilly.servlet.MultipartRequest" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>게시판 목록보기</title>
	<style>
		* {
			margin: 0 auto;
			padding: 0 auto;
			overflow: hidden;
		}
		td, h2 {
			text-align: center;
		}
		.searchbox {
		text-align: left;
		margin-left: 370px;
		}
	</style>

</head>
<body>
	<!-- String sessionUser = session.getAttribute("userId"); -->
 	<!-- <a href="listForm">게시판</a> -->
	<br>
	<h2>게시판 글목록</h2>
	
	<table width="800" cellpadding="0" cellspacing="0" border="1">
		<tr>
			<th>글 번호</th>
			<th>글 종류</th>
			<th>글 제목</th>
			<th>첨부파일</th>
			<th>작성자</th>
			<th>작성일</th>
			<th>조회수</th>
		</tr>
<!-- 		<tr>
			<td>1</td>
			<td>뉴스</td>
			<td>글 제목1</td>
			<td>글 내용1</td>
			<td>jks2024</td>
			<td>2023.04.27</td>
			<td>3</td>
		</tr>	 -->
		
		<c:forEach var="dto" items="${ searchList }">
			<tr>
				<td>${dto.board_idx}</td>
				<td>${dto.board_name}</td>
				<td> 
					<a href="contentForm?board_idx=${dto.board_idx}">${dto.board_title}</a>
				 </td>
				<td>${dto.board_content}</td>
				<td>${dto.user_id}</td>
				
				<td>
					<c:set var="dateVar" value="${dto.board_date}"/>
					<fmt:formatDate value="${dateVar}" pattern="yyyy-MM-dd"/>	
				</td>
				<td>${dto.board_hit}</td>
			</tr>
		</c:forEach> 

		
	<!-- 검색  -->
	<div class="searchbox">
	<form action="searchAction" method="get">
		<select id="condition" name="searchType">
			<option value="title">제목</option>
			<option value="user">작성자</option>
			<option value="content">내용</option>
		</select>
		<input type="text" name="searchKeyword" id="keyword">
		<button type="submit" >검색</button>
	</form>
	</div>
	</table>
	



	

		<script>
	/* 웹브라우저 back키 누를때, 페이지 재로딩 */
	window.onpageshow = function(event) {
		if(event.persisted) {
			document.location.reload();
		}
	};
	</script>
	

</body>
</html>