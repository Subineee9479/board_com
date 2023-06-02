package com.example.board.dto;

import java.util.Date;
import lombok.Data;
@Data
public class ReplyDto {
	private int reply_idx; // 댓글 번호
	private String reply_name; // 댓글 작성자
	private String reply_content; // 댓글 내용
	private Date reply_date; // 댓글 작성일
	private int reply_board_idx; // 게시글
	private int reply_level; // 댓글 깊이(댓글0, 대댓글1)
	private int is_deleted; // 댓글 삭제 여부
	private int parent_id; // 부모댓글

}
