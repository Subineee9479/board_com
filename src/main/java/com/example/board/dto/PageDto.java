package com.example.board.dto;
import lombok.Data;
import lombok.ToString;

@Data @ToString
public class PageDto {
	private int startPage; // 첫 페이지 번호
	private int endPage; // 마지막 페이지 번호
	private boolean next; // 다음 버튼
	private boolean prev; // 이전 버튼
	private boolean first; // 맨앞
	private boolean last; // 맨뒤
	
	private int total; // 총 게시글
	private int pageNum; // 조회하는 페이지번호(cri에도 존재함)
	private int amount = 10; // 보여질 데이터 개수

	private Criteria cri;
	
	public PageDto(Criteria cri, int total) {
		//번호, 개수, 총 게시글 수 초기화
		this.pageNum = cri.getPageNum();
		this.amount = cri.getAmount();
		this.total = total;
		this.cri = cri;
		
		this.endPage = (int)Math.ceil(this.pageNum / 10.0) * 10;
		this.startPage = this.endPage - 10 + 1;
		int realEnd = (int)Math.ceil(this.total / (double)this.amount);
		
		if(this.endPage > realEnd) {
			this.endPage = realEnd; // 마지막에 도달했을 때 보여질 번호르 ㄹ변경
		}
		
		this.prev = this.startPage > 1;
		this.next = this.endPage < realEnd;
	}
	
}




