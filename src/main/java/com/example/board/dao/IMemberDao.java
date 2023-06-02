package com.example.board.dao;

import javax.servlet.http.HttpSession;

import org.apache.ibatis.annotations.Mapper;

import com.example.board.dto.MemberDto;

@Mapper
public interface IMemberDao {
	 MemberDto getUserId(String user_id);
	 MemberDto getUserIdAndPw(String user_id, String user_pw);
	 public void logout(HttpSession session);
}
