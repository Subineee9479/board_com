package com.example.board.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.ToString;

@ToString
public class FileDto {
	private int file_no; // 파일 번호
	private int board_idx; // 게시글 번호
	//private String uuid; // 파일 이름을 만들기 위한 속성 
	private String file_save_name; // 저장된 파일 이름
	private String file_real_name;
	private Long file_size;
	private String file_path;
	private MultipartFile file; 
	
	public FileDto() {
		
	}

	public int getFile_no() {
		return file_no;
	}

	public void setFile_no(int file_no) {
		this.file_no = file_no;
	}

	public int getBoard_idx() {
		return board_idx;
	}

	public void setBoard_idx(int board_idx) {
		this.board_idx = board_idx;
	}

	public String getFile_save_name() {
		return file_save_name;
	}

	public void setFile_save_name(String file_save_name) {
		this.file_save_name = file_save_name;
	}

	public String getFile_real_name() {
		return file_real_name;
	}

	public void setFile_real_name(String file_real_name) {
		this.file_real_name = file_real_name;
	}

	public Long getFile_size() {
		return file_size;
	}

	public void setFile_size(Long file_size) {
		this.file_size = file_size;
	}

	public String getFile_path() {
		return file_path;
	}

	public void setFile_path(String file_path) {
		this.file_path = file_path;
	}

	public MultipartFile getUploadFile() {
		return file;
	}

	public void setUploadFile(MultipartFile uploadFile) {
		this.file = uploadFile;
	}

	public FileDto(int file_no, int board_idx, String file_save_name, String file_real_name, Long file_size,
			String file_path, MultipartFile file) {
		super();
		this.file_no = file_no;
		this.board_idx = board_idx;
		this.file_save_name = file_save_name;
		this.file_real_name = file_real_name;
		this.file_size = file_size;
		this.file_path = file_path;
		this.file = file;
	}


	

	
}
