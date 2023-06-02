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
	<title>게시판</title>
	
	<style>
		* {
			margin: 0 auto;
			padding: 0 auto;
			/*overflow: hidden; 스크롤 감춰놓는거*/ 
		}
		td, h2 {
			text-align: center;
		}
		.searchbox, .write  {
		text-align: left;
		margin-left: 370px;
		}
 		.excel {
/* 		text-align: right;
		margin: auto; */
		text-align: left;
		margin-left: -20px ;
		margin-bottom: -23px;
		} 
		
		
	li {
		list-style-type: none;
		padding: 0;
		margin: 0;
		display: flex;
	}

    .pagination {
	 	display: flex;
  		justify-content: center;
 	 	margin-top: 10px; 
 	 	
	}	
    .pagination a {
        color: #333;
        float: left;
        padding: 8px 16px;
        text-decoration: none;
        transition: background-color 0.3s;
        border: 1px solid #ddd;
        margin-right: 5px;    
    }

    .pagination a.active {
    	
        background-color: #4CAF50;
        color: white;
        border: 1px solid #4CAF50;
    }

    .pagination a:hover:not(.active) {
        background-color: #ddd;
    }
	
	.excel {
		display: flex;
		magin-right: 200px;
	}

    </style>

</head>
<body>

<%
     if(session.getAttribute("user_id") == null) {
       out.println("<a href='/login'>로그인</a>");
     } else{
        String user_id = (String)session.getAttribute("user_id");
        out.println(user_id+"님 반갑습니다.");
        out.println("<a href='logout'>로그아웃</a>");
     }
 %>
	<h2>게시판 글목록</h2>

	<table width="800" cellpadding="0" cellspacing="0" border="1">
		<tr>
			<th>글 번호</th>
			<th>글 종류</th>
			<th>글 제목</th>
			<!-- <th>글 내용</th> -->
			<th>첨부파일</th>
			<th>작성자</th>
			<th>작성일</th>
			<th>조회수</th>
		</tr>
		<tr>
		ID : test1
		PW : test1
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
		
		<%-- <c:if test="${user_id != null }"> --%>
		
 		<c:forEach var="dto" items="${ list }">
			<tr>
				<td>${dto.board_idx}</td>
				<td>${dto.board_name}</td>
				<td> 
					<a href="contentForm?board_idx=${dto.board_idx}">${dto.board_title}</a>
				 </td>
				<%-- <td>${dto.board_content}</td> --%>
				<td>${dto.file_count}</td>
				<td>${dto.user_id}</td>
				
				<td>
					<c:set var="dateVar" value="${dto.board_date}"/>
					<fmt:formatDate value="${dateVar}" pattern="yyyy-MM-dd"/>	
				</td>
				<td>${dto.board_hit}</td>
			</tr>
		</c:forEach> 
		
	<!-- 엑셀 다운로드 -->
	<form action="excelDown" >
		<div class="excel" >
			<input type="hidden" name="searchType" value="">
			<input type="hidden" name="searchKeyword" value="">
			<button type="submit" value="엑셀다운">엑셀다운</button>
		</div>
	</form>
		
	<!-- 검색  -->
	<form action="listForm" method="post">
		<div class="searchbox">		
				
			<select id="condition" name="searchType">
				<option value="title" ${pageDto.cri.searchType eq 'title' ? 'selected' : '' }>제목</option>
				<option value="user" ${pageDto.cri.searchType eq 'user' ? 'selected' : '' } >작성자</option>
				<option value="content" ${pageDto.cri.searchType eq 'content' ? 'selected' : '' }>내용</option>
<!-- 				<option value="title">제목</option>
				<option value="user">작성자</option>
				<option value="content">내용</option> -->
			</select>			
			<input type="text" name="searchKeyword" id="keyword" value=${pageDto.cri.searchKeyword }>
			<button type="submit" >검색</button>
			<a href="writeForm"><input type="button" value="글쓰기"></a>
		</div>
		<input type="hidden" name="pageNum" value="1">
		<input type="hidden" name="amount" value="10">			
	</form>	
	</table>

	<!-- 페이징 -->
	<form action="listForm" method="post" name="pageForm">
		<div class="pagination">
			
			<li><a href="listForm?pageNum=1&amount=${pageDto.amount}&searchType=${pageDto.cri.searchType}&searchKeyword=${pageDto.cri.searchKeyword}">&lt;&lt;</a></li>
			
		
		    <!-- 이전 페이지 활성화 -->
		    <c:if test="${pageDto.prev}">
		        <li><a href="listForm?pageNum=${pageDto.startPage - 1}&amount=${pageDto.amount}&searchType=${pageDto.cri.searchType}&searchKeyword=${pageDto.cri.searchKeyword}">&lt;</a></li>
		    </c:if>
		
			<!-- 페이징 -->
		    <c:forEach var="num" begin="${pageDto.startPage}" end="${pageDto.endPage}">
		        <li class="${pageDto.pageNum eq num ? 'active' : ''}">
		            <a href="listForm?pageNum=${num}&amount=${pageDto.amount}&searchType=${pageDto.cri.searchType}&searchKeyword=${pageDto.cri.searchKeyword}">
		                ${num}
		            </a>
		        </li>
		    </c:forEach>
		
		    <!-- 다음 페이지 활성화 -->
		    <c:if test="${pageDto.next}">
		        <li><a href="listForm?pageNum=${pageDto.endPage + 1}&amount=${pageDto.amount}&searchType=${pageDto.cri.searchType}&searchKeyword=${pageDto.cri.searchKeyword}">&gt;</a></li>
		    </c:if>
		    <li><a href="listForm?pageNum=${pageDto.endPage}&amount=${pageDto.amount}&searchType=${pageDto.cri.searchType}&searchKeyword=${pageDto.cri.searchKeyword}">&gt;&gt;</a></li>

				
 		<input type="hidden" name="pageNum" value="${pageDto.cri.pageNum }">
		<input type="hidden" name="amount" value="${pageDto.cri.amount }">
		<input type="hidden" name="searchType" value="${pageDto.cri.searchType }">
		<input type="hidden" name="searchKeyword" value="${pageDto.cri.searchKeyword }"> 
		</div>
	</form>
	



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