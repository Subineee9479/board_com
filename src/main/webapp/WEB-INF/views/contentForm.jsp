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
	  .button-con{
    display: flex;
    justify-content: center;
    margin-top: 10px;
  }

  .button-con button,
  .button-con input[type="button"] {
    margin-right: 10px;

  }
	</style>
	
	<script>
	
	var whoLoginNow = '<%= session.getAttribute("user_id") %>';
	
	// 댓글
	function checkLogin(){
		// 로그인 체크
		console.log("whoLoginNow : " + whoLoginNow);
		
		if(whoLoginNow !== 'null') {
			console.log("로그인함 : " + whoLoginNow);
		} else {
			console.log("로그인해라 : " + whoLoginNow);
			alert("로그인을 하세요.");
			
		}
	}
	// 수정과 삭제를 한페이지에서 진행하도록
		function setFormAction(action) {
			//form 변수에 actionForm이라는 ID를 가진 요소를 할당 => form 태그 id!
			var form = document.getElementById("actionForm");
			//formAction 변수에 formAction이라는 ID를 가진 요소를 할당 => input태그(hidden)
			var formAction = document.getElementById("formAction");
			// formAction 요소의 valueth속성에 action값을 할당
			formAction.value = action;
			form.action = (action === 'updateAction') ? 'updateAction' : 'deleteAction';
			form.submit();			
		}
	// 대댓글
	function toggleReplyFormForReply(button, reply_idx, parentID) {
	  var container = document.getElementById("replyFormContainer-" + reply_idx);
	  container.style.display = container.style.display === "none" ? "block" : "none";
	  var parentInput = document.getElementById("parentID-" + reply_idx);
	  parentInput.value = parentID;
	}
	</script>
	
<meta charset="UTF-8">
<title>글 내용</title>
</head>

<body>
	<h2>글 내용</h2>
		<%
     	String user_id = (String) session.getAttribute("user_id"); 
        out.println(user_id +"님 로그인 중"); %>
	
	<div>
 	
		<table width="500" cellpadding="0" cellspacing="0" border="1">
<%-- 			<input type="hidden" name="board_idx" value="${dto.board_idx}"/>
			<input type="hidden" name="user_id" value="${sessionScope.user_id}"/> --%>
			
			
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
					<textarea rows="10" cols="50"  name="board_content" value="">${dto.board_content }</textarea> 
					
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
				<!-- <td>비회원 비밀번호</td> -->
				<%-- <td>${dto.non_user_pw }</td> --%>
				<!-- <td><input type="password" name="non_user_pw" value=""></td> -->
			</tr> 
			
		<!-- 수정,삭제  -->
		<tr>
			<td colspan="2">
				<form id="actionForm" method="post" action="" class="button-con">
					<input type="hidden" name="board_idx" value="${dto.board_idx }">
					<input type="hidden" name="board_title" value="${dto.board_title }">
					<input type="hidden" name="board_content" value="${dto.board_content }">
					<input type="hidden" name="user_id" value="${dto.user_id}">
					<input type="hidden" name="whoLoginNow" value="${user_id}">
					<input type="hidden" name="action" id="formAction">
					
					<c:if test="${dto.non_user_pw ne 0}">
						<input type="password" name="non_user_pw" minlength="4" maxlength="4" placeholder="비밀번호 4자리">
					</c:if>
					
					  <c:if test="${dto.non_user_pw eq 0 }">
						<input type="hidden" name="user_id" value="${dto.user_id }">
					</c:if>  
					
					<input type="submit" value="수정" onclick="setFormAction('updateAction')">
					<input type="submit" value="삭제" onclick="setFormAction('deleteAction')">
					<a href="listForm"><input type="button" value="목록보기"></a>
				</form>
			</td>
		</tr>
		
<%-- 		<tr>		
		<td colspan="2">
		<div class="button-con">			
		 <form action="updateAction" method="post">
		 	<input type="hidden" name="board_idx" value="${dto.board_idx }">
		 	<input type="hidden" name="board_name" value="${dto.board_name }">
		 	<input type="hidden" name="board_content" value="${dto.board_content }">
		 	<input type="hidden" name="board_title" value="${dto.board_title }">
		 	<input type="hidden" name="user_id" value="${dto.user_id }">
		 	<input type="hidden" name="non_user_pw" value="${dto.non_user_pw }">
				<input type="submit" value="수정">&nbsp;&nbsp;
			</form>	
				
			<form action="deleteAction" method="post">
				<c:if test="${sessionScope.user_id eq user_id }">
					<input type="hidden" name="board_idx" value="${dto.board_idx }">
					<input type="hidden" name="user_id" value="${dto.user_id }">
					<input name="non_user_pw">
					<a href="deleteAction?board_idx=${dto.board_idx}&user_id=${dto.user_id}&non_user_pw=${non_user_pw }">
					<input type="submit" value="삭제"></a>&nbsp;&nbsp;
				</c:if>
			</form>
					<a href="listForm"><input type="button" value="목록보기"></a>&nbsp;&nbsp;			
			</div>	
				</td>				
		</table>
		</div>	 --%>
	<br>
	
 	<form action="writeReplyAction" method="post">
		<table width="500" cellpadding="0" cellspacing="0" border="1">
			<tr>
				<td colspan="2">
					<input type="hidden" name="reply_board_idx" value="${dto.board_idx }"> <!-- 어느게시글인지 알기위해서 hidden으로 숨겨서 넣어줌 -->
					<label>작성자</label><input type="text" name="reply_name" value="${user_id }"><br>
					<label>댓글</label>
					<textarea rows="2" cols="50" name="reply_content" ></textarea><br>
					<!-- <textarea rows="2" cols="50" name="reply_content" onclick="checkLogin()"></textarea><br> -->					
					<input type="submit" value="댓글달기" >
				</td>
			</tr>
		</table>
	</form> 

	<br>
	
	<div class="reply_container">
	    <c:forEach var="reply_dto" items="${reply_list}">
	        <br>
	
	        <c:if test="${reply_dto.is_deleted eq 1}">
	        	<!-- 댓글 삭제된 경우 -->
	            <div class="reply_name">${reply_dto.reply_name}</div>
	            <div class="reply_deleted_content">삭제된 댓글입니다.</div>
	        </c:if> 
	
	        <c:if test="${reply_dto.is_deleted eq 0}">
	        	<!-- 댓글 삭제되지 않은 경우  -->
	            <div class="reply_name">${reply_dto.reply_name}</div>
	            <div class="reply_content">	     
 	           	 	<c:if test="${reply_dto.reply_level == 1 }">&rdca;</c:if>	
					<c:if test="${reply_dto.reply_level != 1 }">${user_id}</c:if>        		       
	               	             		
	                ${reply_dto.reply_content}&nbsp;<fmt:formatDate value="${reply_dto.reply_date}" pattern="yyyy-MM-dd" />      
	                  		
	                <a href="deleteReplyAction?reply_idx=${reply_dto.reply_idx}&reply_board_idx=${reply_dto.reply_board_idx}
	                	&reply_name=${reply_dto.reply_name}&whoLoginNow=${user_id}"><button>삭제</button></a>
	
	                <c:if test="${reply_dto.reply_level eq 0}">
	                    <button onclick="toggleReplyFormForReply(this, ${reply_dto.reply_idx}, ${reply_dto.reply_idx})">대댓글 쓰기</button>
	                    <div id="replyFormContainer-${reply_dto.reply_idx}" style="display: none;">
	                        <form action="writeReplyAction" method="post">
	                            <input type="hidden" name="reply_board_idx" value="${reply_dto.reply_board_idx}">
	                            <input type="hidden" name="reply_name" value="${user_id}">
	                            <input type="hidden" name="parent_id" id="parentID-${reply_dto.reply_idx}">
	                            <input type="hidden" name="user_id" value="${dto.user_id}">
								<input type="hidden" name="whoLoginNow" value="${user_id}">
	                             <textarea name="reply_content" rows="3" cols="30" ></textarea> 
	                            <!-- <textarea name="reply_content" rows="3" cols="30" onclick="checkLogin()"></textarea> -->
	                            <button type="submit">대댓글 작성</button>
	                        </form>
	                    </div>
	                </c:if>
	            </div>
	            
	        </c:if>
	    </c:forEach>
	</div>


</body>
</html>