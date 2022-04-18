package com.javaex.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.javaex.vo.UserVo;

public class UserDaoImpl implements UserDao{

	private Connection getConnection() throws SQLException{
		Connection conn =null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			String dburl = "jdbc:oracle:thin:@localhost:1521:xe";
			conn = DriverManager.getConnection(dburl, "webdb","webdb");
		}catch(ClassNotFoundException e) {
			System.err.println("JDBC 드라이버 로드 실패");
		}
		return conn;
	}
	
	@Override
	public int insert(UserVo vo) {
		// TODO Auto-generated method stub
		Connection conn=null;
		PreparedStatement pstmt =null;
		int insertedCount=0;
		
		try {
			conn=getConnection();
			
			String sql="insert into users values(seq_users_no.nextval,?,?,?,?)";
			pstmt=conn.prepareStatement(sql);
			
			pstmt.setString(1, vo.getName());
			pstmt.setString(2, vo.getEmail());
			pstmt.setString(3, vo.getPassword());
			pstmt.setString(4, vo.getGender());
			
			insertedCount=pstmt.executeUpdate();
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		System.out.println("insert success");
		return insertedCount;
	}

	@Override
	public int update(UserVo vo) {
		// TODO Auto-generated method stub
		Connection conn=null;
		PreparedStatement pstmt =null;
		int insertedCount=0;
		
		try {
			conn=getConnection();
			
			String sql="update into user values(seq_user_no.nextval,?,?,?,?)";
			pstmt=conn.prepareStatement(sql);
			
			pstmt.setString(1, vo.getName());
			pstmt.setString(2, vo.getEmail());
			pstmt.setString(3, vo.getPassword());
			pstmt.setString(4, vo.getGender());
			
			insertedCount=pstmt.executeUpdate();
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		System.out.println("update success");
		return insertedCount;
		
	}

	@Override
	public UserVo getUser(String email, String pw) {

		// 0. import java.sql.*;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		UserVo vo = null;

		try {
			conn = getConnection();

			// 3. SQL문 준비 / 바인딩 / 실행
			String query = "select no, name from users where email = ? and password = ?";
			pstmt = conn.prepareStatement(query);

			pstmt.setString(1, email);
			pstmt.setString(2, pw);

			rs = pstmt.executeQuery();

			// 4.결과처리
			if (rs.next()) {
				int no = rs.getInt("no");
				String name = rs.getString("name");

				vo = new UserVo();
				vo.setNo(no);
				vo.setName(name);
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

		System.out.println("user 데이터 조회 성공");
		return vo;

	}
	
	
	public UserVo getUser(int no) {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		UserVo vo = null;

		try {
			conn = getConnection();

			// 3. SQL문 준비 / 바인딩 / 실행
			String query = "select no, name, email, password, gender from users where no = ?";
			pstmt = conn.prepareStatement(query);

			pstmt.setInt(1, no);

			rs = pstmt.executeQuery();

			// 4.결과처리
			if (rs.next()) {

				vo = new UserVo();
				vo.setNo(rs.getInt("no"));
				vo.setName(rs.getString("name"));
				vo.setEmail(rs.getString("email"));
				vo.setPassword(rs.getString("password"));
				vo.setGender(rs.getString("gender"));
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

		return vo;
	}

	@Override
	public String idCheck(String email) {
		Connection conn = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    @SuppressWarnings("unused")
	    UserVo vo = null;
	    int cnt = 0;
	    
	    try {
	      conn = getConnection();

	      // 3. SQL문 준비 / 바인딩 / 실행
	      String sql = " select count(*) cnt " + 
	                   "   from users " + 
	                   "  where email = ?";

	      pstmt = conn.prepareStatement(sql);
	      pstmt.setString(1, email);
	      rs = pstmt.executeQuery();

	      // 4.결과처리
	      rs.next();
	      cnt = rs.getInt("cnt");  // 1      0
	      System.out.println("cnt:" + cnt);
	    } catch (SQLException e) {
	      System.out.println("error:" + e);
	    } finally {
	      // 5. 자원정리
	      try {
	        if(rs != null)    rs.close();
	        if(pstmt != null) pstmt.close();
	        if(conn != null)  conn.close();
	      } catch (SQLException e) {
	        System.out.println("error:" + e);
	      }
	    }
	    if(cnt == 1){
	      return "true";
	    }else{
	      return "false";
	    }
	}

	

}
