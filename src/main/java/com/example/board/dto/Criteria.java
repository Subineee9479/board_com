package com.example.board.dto;

import lombok.Data;

//페이징 위한
@Data
public class Criteria {
	private int pageNum;
	private int amount;
	
	// 검색에 필요한 키워드 선언
	private String searchType; // 작성자, 제목
	private String searchKeyword; // 검색 이름
	
	public Criteria() {
		this(1, 10);
	}

	public Criteria(int pageNum, int amount) {
		this.pageNum = pageNum;
		this.amount = amount;
	}

}
