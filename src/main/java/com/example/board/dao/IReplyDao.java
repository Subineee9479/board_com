package com.example.board.dao;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.example.board.dto.ReplyDto;

@Mapper
public interface IReplyDao {
	// 댓글 조회
	public List<ReplyDto> reply_list(String reply_board_index);
	// 댓글 작성
	public int reply_write(String reply_name, String reply_content, String reply_board_idx, Integer reply_level, Integer is_deleted, Integer parent_id);
	// 댓글 삭제
	//public int reply_deleteDto(String reply_idx);
	public int reply_deleteDto(String reply_idx, String reply_board_idx, String reply_name);

	//public int getReplyLevel(Integer parent_id);
	//public int writeReply(String reply_name, String reply_content, String reply_board_index);
	
}
