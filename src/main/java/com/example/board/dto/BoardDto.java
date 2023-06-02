package com.example.board.dto;

import java.util.ArrayList;
import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

import lombok.ToString;

@ToString 
public class BoardDto {
	private int board_idx;
	private String board_name;
	private String board_title;
	private String board_content;
	//private MultipartFile file;
	private String user_id;
	private Date board_date;
	private int board_hit;	
	private int file_count;
	private String non_user_pw;
	
	private ArrayList<FileDto> fileList; // DB에 전달하기 위해 Board 내에 생성
	
	// 기본 생성자
	public BoardDto() {
	}

	// Getter, Setter
	public int getBoard_idx() {
		return board_idx;
	}

	public void setBoard_idx(int board_idx) {
		this.board_idx = board_idx;
	}

	public String getBoard_name() {
		return board_name;
	}

	public void setBoard_name(String board_name) {
		this.board_name = board_name;
	}

	public String getBoard_title() {
		return board_title;
	}

	public void setBoard_title(String board_title) {
		this.board_title = board_title;
	}

	public String getBoard_content() {
		return board_content;
	}

	public void setBoard_content(String board_content) {
		this.board_content = board_content;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public Date getBoard_date() {
		return board_date;
	}

	public void setBoard_date(Date board_date) {
		this.board_date = board_date;
	}

	public int getBoard_hit() {
		return board_hit;
	}

	public void setBoard_hit(int board_hit) {
		this.board_hit = board_hit;
	}

	public int getFile_count() {
		return file_count;
	}

	public void setFile_count(int file_count) {
		this.file_count = file_count;
	}

	public String getNon_user_pw() {
		return non_user_pw;
	}

	public void setNon_user_pw(String non_user_pw) {
		this.non_user_pw = non_user_pw;
	}

//	public MultipartFile getFile() {
//		return file;
//	}
//
//	public void setFile(MultipartFile file) {
//		this.file = file;
//	}
	
	

}
