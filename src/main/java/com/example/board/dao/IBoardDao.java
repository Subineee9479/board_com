package com.example.board.dao;


import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.board.dto.BoardDto;
import com.example.board.dto.Criteria;
import com.example.board.dto.FileDto;

@Mapper
public interface IBoardDao {
	
	// 게시물 목록
	public List<BoardDto> list();
	// 게시물 작성
	//public int write(String board_name, String board_title, String board_content, String user_id, MultipartFile file);
	public int write(String board_name, String board_title, String board_content, String user_id, String non_user_pw);
	// 첨부파일
	public int saveFile(String file_save_name, String file_real_name, long file_size, String file_path);
	// 파일 조회
	public FileDto searchFile(String board_idx);
	// 파일 갯수
	public List<FileDto> fileCnt(String board_idx);
	
	//public FileDto downloadFile(String board_idx);
	//public FileDto downloadFile(String file_no, String board_idx);
	// 게시물 조회
	public BoardDto viewDto(String board_idx);
	// 게시물 수정
	public int updateDto(String board_idx, String board_title, String board_content, String user_id);
	// 게시물 수정
	public int updateDto2(String board_idx, String board_title, String board_content, String non_user_pw);
	// 게시물 삭제(회원)
	public int deleteDto(String board_idx, String user_id);
	// 게시물 삭제(비회원)
	public int deleteDto2(String board_idx, String loginPw);
	public String getNonUserPw(String board_idx);
	// 조회수 증가
	public int hit(String board_idx);
	
	
	// 게시판 제목 검색
	public List<BoardDto> searchTitle(String board_title);
	// 게시판 내용 검색
	public List<BoardDto> searchContent(String board_content);
	// 작성자로 검색
	public List<BoardDto> searchUser(String user_id);
	
	
	// 게시판 페이징
	public List<BoardDto> getList(Criteria cri);
	public int getTotal(Criteria cri);
	
	// 게시물 삭제(회원)
	public int checkAuthor(String board_idx, String user_id);
	// 게시물 삭제(비회원)
	public int checkPassword(String board_idx, String non_user_pw);

	
}
