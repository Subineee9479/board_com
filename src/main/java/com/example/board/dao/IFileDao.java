package com.example.board.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.board.dto.BoardDto;
import com.example.board.dto.FileDto;

@Mapper
public interface IFileDao {
	public List<FileDto> file_list(String board_idx);
	public FileDto searchFile(String board_idx);
	
	// 엑셀 다운로드
	public List<BoardDto> downBoardList(@Param("searchType") String searchType, @Param("searchKeyword") String searchKeyword);
}
