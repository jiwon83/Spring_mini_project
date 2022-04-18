package com.javaex.vo;

public class Criteria {

	private int pageNum;//페이지 번호
	private int amount;// 한 페이지 당 보여줄 데이터의 양
	
	public Criteria() {
		this(1,10); //페이지번호 1번, 1페이지당 10개의 데이터 
	}
	public Criteria(int pageNum, int amount) {
		this.pageNum = pageNum;
		this.amount = amount;
	}
	public int getPageNum() {
		return pageNum;
	}
	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	
}
