package com.javaex.dao;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

import com.javaex.vo.BoardVo;




public class BoardDaoImpl implements BoardDao {
	
	
	private static final String  SAVEFOLDER = "C:\\Users\\jiwon\\eclipse-workspace\\mysite\\WebContent\\WEB-INF\\views\\filecontent";
	
	
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
  
	public List<BoardVo> getList() {

		// 0. import java.sql.*;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<BoardVo> list = new ArrayList<BoardVo>();

		try {
			conn = getConnection();

			// 3. SQL문 준비 / 바인딩 / 실행
			String query = "select b.no, b.title, b.hit, b.reg_date, b.user_no, u.name "
					     + " from board b, users u "
					     + " where b.user_no = u.no "
					     + " order by no desc";
			
			pstmt = conn.prepareStatement(query);

			rs = pstmt.executeQuery();
			// 4.결과처리
			while (rs.next()) {
				int no = rs.getInt("no");
				String title = rs.getString("title");
				int hit = rs.getInt("hit");
				String regDate = rs.getString("reg_date");
				int userNo = rs.getInt("user_no");
				String userName = rs.getString("name");
				
				BoardVo vo = new BoardVo(no, title, hit, regDate, userNo, userName);
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

	
	public BoardVo getBoard(int no) {

		// 0. import java.sql.*;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		BoardVo boardVo = null;
		
		try {
		  conn = getConnection();

		  	
			// 3. SQL문 준비 / 바인딩 / 실행
		  
			String query = "select b.no, b.title, b.content, b.hit, b.reg_date, b.user_no, u.name, b.filename, b.filesize, b.ori_filename "
					     + "from board b, users u "
					     + "where b.user_no = u.no "
					     + "and b.no = ?";
		  
		 
			
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, no);
			
			rs = pstmt.executeQuery();
			// 4.결과처리
			while (rs.next()) {
				String title = rs.getString("title");
				String content = rs.getString("content");
				int hit = rs.getInt("hit");
				String regDate = rs.getString("reg_date");
				int userNo = rs.getInt("user_no");
				String userName = rs.getString("name");
				String fileName=rs.getString("filename");
				int fileSize=rs.getInt("filesize");
				String oriFileName=rs.getString("ori_filename");
				boardVo = new BoardVo(no, title, content, hit, regDate, userNo, userName,fileName,fileSize,oriFileName);
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
		System.out.println(boardVo);
		return boardVo;

	}
	
	public int insert(BoardVo vo) {
		// 0. import java.sql.*;
		Connection conn = null;
		PreparedStatement pstmt = null;
		int count = 0;

		try {
		  conn = getConnection();
		  
		  System.out.println("vo.userNo : ["+vo.getUserNo()+"]");
      System.out.println("vo.title : ["+vo.getTitle()+"]");
      System.out.println("vo.content : ["+vo.getContent()+"]");
      
			// 3. SQL문 준비 / 바인딩 / 실행
			String query = "insert into board values (seq_board_no.nextval, ?, ?, 0, sysdate, ?, ?, 0, ?)";
			pstmt = conn.prepareStatement(query);

			pstmt.setString(1, vo.getTitle());
			pstmt.setString(2, vo.getContent());
			pstmt.setInt(3, vo.getUserNo());
			pstmt.setString(4, vo.getFileName());
			//pstmt.setInt(5, vo.getFileSize());
			pstmt.setString(5, vo.getOriFileName());
			
			
			
			
      
			count = pstmt.executeUpdate();

			// 4.결과처리
			System.out.println(count + "건 등록");

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

		return count;
	}
	
	
	public int delete(int no) {
		// 0. import java.sql.*;
		Connection conn = null;
		PreparedStatement pstmt = null;
		int count = 0;

		try {
		  conn = getConnection();

			// 3. SQL문 준비 / 바인딩 / 실행
			String query = "delete from board where no = ?";
			pstmt = conn.prepareStatement(query);

			pstmt.setInt(1, no);

			count = pstmt.executeUpdate();

			// 4.결과처리
			System.out.println(count + "건 삭제");

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

		return count;
	}
	
	
	public int update(BoardVo vo) {
		// 0. import java.sql.*;
		Connection conn = null;
		PreparedStatement pstmt = null;
		int count = 0;

		try {
		  conn = getConnection();

			// 3. SQL문 준비 / 바인딩 / 실행
			String query = "update board set title = ?, content = ? where no = ? ";
			pstmt = conn.prepareStatement(query);

			pstmt.setString(1, vo.getTitle());
			pstmt.setString(2, vo.getContent());
			pstmt.setInt(3, vo.getNo());

			count = pstmt.executeUpdate();

			// 4.결과처리
			System.out.println(count + "건 수정");

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

		return count;
	}
	//update hit
	public int updateHit(int no) {
		// 0. import java.sql.*;
		Connection conn = null;
		PreparedStatement pstmt = null;
		int count = 0;

		try {
		  conn = getConnection();

			// 3. SQL문 준비 / 바인딩 / 실행
		  //update board set hit=hit+1 where no = 3;
			String query = "update board set hit = hit+1 where no = ? ";
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, no);

			count = pstmt.executeUpdate();

			// 4.결과처리
			System.out.println(count + "조히수 업데이트 완료.");

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

		return count;
	}
	
	//paging 기능 구현
	// 게시판 리스트 
	public Vector<BoardVo> getBoardList(String keyfield, String keyword, int start, int end) {

		// 0. import java.sql.*;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		BoardVo boardVo = null;
		Vector<BoardVo> vlist= new Vector<BoardVo>();
		String query="";
		
		try {
		  conn = getConnection();
		  
		  //만약 검색어를 입력하지 않았다면 페이징 처리만 하도록
		  if( keyword==null || keyword.equals("null") || keyword.equals("")) {
			  
			  /*
			   * select *
				from(select rownum as RNUM, A.*
        				from (select * from board order by reg_date desc) A
        				where rownum <= 5)
				where RNUM >2;
			   * 
			   */
			  
			  //2. userName적용
			  /*
			  	 * select *
					from(select rownum as RNUM, A.*
	        			from (select b.*, u.name as username from board b, users u where b.user_no= u.no (+) order by reg_date desc) A
	        			where rownum <= 5)
					where RNUM >2;
			  	 */
			  query = "SELECT * \r\n" + 
				      "        FROM(\r\n" + 
				      "              SELECT ROWNUM AS RNUM, A.*\r\n" + 
				      "                  FROM ( select b.*, u.name as username from BOARD b, USERS u where b.user_no=u.no (+) order by reg_date desc) A\r\n" + 
				      "               WHERE ROWNUM <= ?+?\r\n" + 
				      "            )\r\n" + 
				      "       WHERE RNUM > ?";
				  
				         
				  pstmt = conn.prepareStatement(query);
				  pstmt.setInt(1, start);//0
				  pstmt.setInt(2, end);//3
				  pstmt.setInt(3, start);//0
		  }else {
			  /*
			   * select *
				from(select rownum as RNUM, A.*
        				from (select * from board where board.title like '%5%') A
        				where rownum <= 5) 
				where RNUM >=2;
			   */
			  query = " SELECT * \r\n" + 
		              " FROM(\r\n" + 
		              "   SELECT ROWNUM AS RNUM, A.*\r\n" + 
		              "   FROM ( select b.*, u.name as username from  board b, users u where " + keyfield + " like ? and b.user_no=u.no (+) order by reg_date desc) A\r\n" + 
		              "   WHERE ROWNUM <= ?+?\r\n" + 
		              "   )\r\n" + 
		              " WHERE RNUM > ?";
						
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, "%" + keyword + "%");
			pstmt.setInt(2, start);
			pstmt.setInt(3, end);
			pstmt.setInt(4, start);
			  
			  
		  }

		  rs = pstmt.executeQuery();
		  while (rs.next()) {
				BoardVo vo = new BoardVo();
				vo.setNo(rs.getInt("no"));
				vo.setTitle(rs.getString("title"));
				vo.setContent(rs.getString("content"));
				vo.setHit(rs.getInt("hit"));//조회수
				vo.setUserNo(rs.getInt("user_no"));
				vo.setUserName(rs.getString("username"));//유저명
				vo.setRegDate(rs.getString("reg_date"));
				
				
				vlist.add(vo);
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
		System.out.println(boardVo);
		
		return vlist;

	}
		
	//총 게시물수
	public int getTotalCount(String keyField, String keyWord) {
			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql = null;
			int totalCount = 0;
			try {
				conn = getConnection();
				
				if ( keyWord==null || keyWord.equals("") || keyWord.equals("null") ) {
					sql = "select count(no) from board";
					pstmt = conn.prepareStatement(sql);
					System.out.println("getTotalCount 실행");
				} else {
					sql = "select count(no) from  board where " + keyField + " like ? ";
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, "%" + keyWord + "%");
				}
				rs = pstmt.executeQuery();
				if (rs.next()) {
					totalCount = rs.getInt(1);
				}
			} catch (Exception e) {
				e.printStackTrace();
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
			return totalCount;
	}
	
	//파일 다운로드
		public void downLoad(HttpServletRequest req, HttpServletResponse res,
				JspWriter out, PageContext pageContext,String fn) {
			try {
				String filename = fn;
				File file = new File(con(SAVEFOLDER + File.separator+ filename));
				byte b[] = new byte[(int) file.length()];
				res.setHeader("Accept-Ranges", "bytes");
				String strClient = req.getHeader("User-Agent");
				//MSIE6.0 특별처리, 요즘엔 잘 안씀
				if (strClient.indexOf("MSIE6.0") != -1) {
					res.setContentType("application/smnet;charset=utf-8");
					res.setHeader("Content-Disposition", "filename=" + filename + ";");
				} else {
					//이게 진자 실행되는 코드 실제로 파일을 다운로드 : 파일을 실제로 open 해주는 방식 보안 위험성 존재
					res.setContentType("application/smnet;charset=utf-8");
					res.setHeader("Content-Disposition", "attachment;filename="+ filename + ";");
				}
				
				
				//JspWriter out,
				out.clear();
				out = pageContext.pushBody();
				if (file.isFile()) {
					BufferedInputStream fin = new BufferedInputStream(
							new FileInputStream(file));
					BufferedOutputStream outs = new BufferedOutputStream(
							res.getOutputStream());
					int read = 0;
					while ((read = fin.read(b)) != -1) {
						outs.write(b, 0, read);
					}
					outs.close();
					fin.close();
				}
				
				System.out.println("다운로드 DaoImpl 성공");
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("다운로드 DaoImpl 실패");
			}
		}
		
		//인코딩 변경
		public String con(String s) {
				String str = null;
				try {
					str = new String(s.getBytes("8859_1"), "ksc5601"); //한글처리
				} catch (Exception e) {
					e.printStackTrace();
				}
				return str;
			}
	

	
}
