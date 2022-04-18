package com.javaex.dao;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.javaex.vo.GuestbookVo;


public class GuestbookDaoImpl implements GuestbookDao{
  
    private Connection getConnection() throws SQLException {
    	Connection conn = null;
	    try {
	      Class.forName("oracle.jdbc.driver.OracleDriver");
	      String dburl = "jdbc:oracle:thin:@localhost:1521:xe";
	      conn = DriverManager.getConnection(dburl, "webdb", "webdb");
	    } catch (ClassNotFoundException e) {
	      System.err.println("JDBC 드라이버 로드 실패!");
	    }
	    return conn;
    }
  
  	//EmaillistVo 객체
	public List<GuestbookVo> getList() {

		// 0. import java.sql.*;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		// 이해안됨
		// ArrayList는 가변객체
		List<GuestbookVo> list = new ArrayList<GuestbookVo>();

		try {
			conn = getConnection();
			
			// 3. SQL문 준비 / 바인딩 / 실행
			String query = "select no, name, password, content, reg_date  "
						       + "from guestbook "
					         + "order by no desc" ;
			pstmt = conn.prepareStatement(query);
			
			rs = pstmt.executeQuery();
			// 4.결과처리
			while(rs.next()) {
				int no = rs.getInt("no");
				String name = rs.getString("name");
				String password = rs.getString("password");
				String content = rs.getString("content");
				String reg_date = rs.getString("reg_date");
				
				GuestbookVo vo = new GuestbookVo(no, name, password, content, reg_date);
				list.add(vo);
			}
			
			
		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			// 5. 자원정리
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("error:" + e);
			}

		}

		return list;
	}
	
	// 삽입
	public int insert(GuestbookVo vo) {
		Connection conn = null;
	    PreparedStatement pstmt = null;
	    int insertedCount = 0;
		
		try{
			conn = getConnection();
			
			String sql = "insert into guestbook values (seq_guestbook_no.nextval, ?, ?, ?, sysdate)";
			
			//미리 미완성의 sql을 등록시켜 놓겠다.
			pstmt = conn.prepareStatement(sql); 
			
			//바인딩 시키기
			//pstmt.setInt(1, Integer.parseInt(author_id)); //첫번째 물음표에 대입!
			pstmt.setString(1, vo.getName());
			pstmt.setString(2, vo.getPassword());
			pstmt.setString(3, vo.getContent());
			
			insertedCount = pstmt.executeUpdate(); // insert update delete (반영 건수 리턴)
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	  System.out.println("insert 완료");
	  return insertedCount;
	}
	
	// 삭제
	public int delete(GuestbookVo vo) {
    Connection conn = null;
    PreparedStatement pstmt = null;
    int count = 0 ;
    
	    try {
	      conn = getConnection();
	      
	      String query ="delete from guestbook where no = ? and password = ?";
	      pstmt = conn.prepareStatement(query); 
	      
	      pstmt.setInt(1, vo.getNo());
	      pstmt.setString(2, vo.getPassword());
	      
	      count = pstmt.executeUpdate();
	      
	      System.out.println(count + "건 삭제");
	      
	    } catch (SQLException e) {
	      System.out.println("error:" + e);
	    
	    } finally {
	      try {
	        if (pstmt != null) pstmt.close();
	        
	        if (conn != null) conn.close();
	      } catch (SQLException e) {
	        System.out.println("error:" + e);
	      }
	    }
	    return count;
	}
	// no로 삭제
	public int delete(int no) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int count = 0;

		try {
			conn = getConnection();

			String query = "delete from guestbook where no = ?";
			pstmt = conn.prepareStatement(query);

			pstmt.setInt(1, no);

			count = pstmt.executeUpdate();

			System.out.println("방명록 삭제");

		} catch (SQLException e) {
			System.out.println("error:" + e);

		} finally {
			try {
				if (pstmt != null)
					pstmt.close();

				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				System.out.println("error:" + e);
			}
		}
		return count;
	}
	public int delete(String password) {
	    Connection conn = null;
	    PreparedStatement pstmt = null;
	    int count = 0 ;
	    
		    try {
		      conn = getConnection();
		      
		      String query ="delete from guestbook where password =?";
		      pstmt = conn.prepareStatement(query); 
		      
		      //pstmt.setInt(1, no);
		      pstmt.setString(1, password);
		      
		      
		      count = pstmt.executeUpdate();
		      
		      System.out.println("방명록 삭제");
		      
		    } catch (SQLException e) {
		      System.out.println("error:" + e);
		    
		    } finally {
		      try {
		        if (pstmt != null) pstmt.close();
		        
		        if (conn != null) conn.close();
		      } catch (SQLException e) {
		        System.out.println("error:" + e);
		      }
		    }
		    return count;
		}

	
}
