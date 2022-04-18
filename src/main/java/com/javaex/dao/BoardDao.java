package com.javaex.dao;

import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

import com.javaex.vo.BoardVo;

public interface BoardDao {
	public List<BoardVo> getList();  // 게시물 전체 목록 조회
	public BoardVo getBoard(int no); // 게시물 상세 조회
	public int insert(BoardVo vo);   // 게시물 등록
	public int delete(int no);       // 게시물 삭제
	public int update(BoardVo vo);   // 게시물 수정
	public int getTotalCount(String keyField, String keyWord);
	public Vector<BoardVo> getBoardList(String keyfield, String keyword, int start, int end);
	
	public void downLoad(HttpServletRequest req, HttpServletResponse res,
			JspWriter out, PageContext pageContext,String fn);
	
	public String con(String s);
	
	public int updateHit(int no);
	
	//public int isCheckFileName(String filename);// 이미 db에 저장된 파일이 존재하는지 확인하기 위한 메서드
}
