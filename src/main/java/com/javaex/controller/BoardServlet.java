package com.javaex.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.javaex.dao.BoardDao;
import com.javaex.dao.BoardDaoImpl;
import com.javaex.util.WebUtil;
import com.javaex.vo.BoardVo;
import com.javaex.vo.UserVo;

@WebServlet("/board")
public class BoardServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static final String CHARSET = "utf-8";
	private static final int LIMIT_SIZE_BYTES = 1024 * 1024;
	
	//업로드하면 파일이 저장될 위치이자 다운로드할때 파일을 가져오는 위치
	//private static final String ATTACHES_DIR = "C://test";
	private static final String  ATTACHES_DIR = "C://Users//jiwon//eclipse-workspace//mysite//WebContent//WEB-INF//views//filecontent";
	
	String keyWord = "", keyField = "";//키워드와 키필드 초기화
	
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response )
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String actionName = request.getParameter("a");//action값 받기
		System.out.println("actionName:" + actionName);
		
		//고정값
		final int numPerPage = 3; // 페이지당 레코드 수 
		final int pagePerBlock = 3; //블럭당 페이지수 
		
		//동적값
		int totalPage = 0; //전체 페이지 수
		int totalBlock = 0; //전체 블럭수 
		int totalRecord = 0; //전체레코드수
		
		int nowPage = 1; // 현재페이지 
		int nowBlock = 1; //현재블럭 
		
		int start = 1; //디비의 select 시작번호 +1
		int end = 10; //시작번호로 부터 가져올 select 갯수
		
		int listSize = 0; //현재 읽어온 게시물의 수
		
		Vector<BoardVo> vlist = null;
		BoardDao dao = new BoardDaoImpl();
		

		if ("list".equals(actionName)) {
			
			//nowPage 처리
			if(request.getParameter("nowPage")==null) {
				nowPage=1;
			}else {
				nowPage = Integer.parseInt(request.getParameter("nowPage"));
				System.out.println("1이 아닌 현재 페이지:"+nowPage);
			}
			
		
			//필요한 값 계산
			totalRecord = dao.getTotalCount(keyField, keyWord); //전체 게시글 수 계산, keyFiled와 keyWord 반영
			start = (nowPage * numPerPage)-numPerPage; // 디비를 받아올 rownum 최소값 계산
			end = numPerPage; //그냥 페이지당 들어갈 게시글 수 
			totalPage = (int)Math.ceil((double)totalRecord / numPerPage);  //전체페이지수
			nowBlock = (int)Math.ceil((double)nowPage/pagePerBlock); //현재블럭 계산 int(Math.ceil(1.0/3))=>0.3333 
		    totalBlock = (int)Math.ceil((double)totalPage / pagePerBlock);  //전체블럭계산
			
		    vlist= dao.getBoardList(keyField, keyWord, start, end);
			
			int pageStart = (nowBlock -1)*pagePerBlock + 1 ; //하단의 페이지 번호 시작점
			//pageEnd는 페이지 번호를 찍는 함수에 쓰일 end 값. 
	  		int pageEnd = ((pageStart + pagePerBlock ) <= totalPage) ?  (pageStart + pagePerBlock): totalPage+1; 
			
			System.out.println(vlist.toString());
			//list4에서 el로 받을 변수들 setting
			request.setAttribute("list", vlist);
			request.setAttribute("totalPage", totalPage);
			request.setAttribute("nowBlock", nowBlock);
			request.setAttribute("totalRecord", totalRecord);
			request.setAttribute("totalBlock", totalBlock);
			request.setAttribute("numPerPage", numPerPage);
			request.setAttribute("pagePerBlock", pagePerBlock);
			request.setAttribute("start", start);
			request.setAttribute("end", end);
			request.setAttribute("pageStart", pageStart);
			request.setAttribute("pageEnd", pageEnd);
			request.setAttribute("nowPage", nowPage);
			
			/*
			System.out.println("/"+vlist);
			System.out.println("/"+totalPage);
			System.out.println("/"+nowBlock);
			System.out.println("/"+totalRecord);
			System.out.println("/"+numPerPage);
			System.out.println("/"+pagePerBlock);
			System.out.println("/"+start);
			System.out.println("/"+end);
			System.out.println("/"+pageStart);
			System.out.println("/"+pageEnd);
			*/
			
			WebUtil.forward(request, response, "/WEB-INF/views/board/list.jsp");
			
			
		} else if ("read".equals(actionName)) {
			
			int no = Integer.parseInt(request.getParameter("no"));
			BoardVo boardVo = dao.getBoard(no);
			System.out.println(boardVo.toString());

			// 게시물 화면에 보내기
			request.setAttribute("boardVo", boardVo);
			WebUtil.forward(request, response, "/WEB-INF/views/board/read.jsp");
			
			
		} else if ("modifyform".equals(actionName)) {
			// 게시물 가져오기
			int no = Integer.parseInt(request.getParameter("no"));
			BoardVo boardVo = dao.getBoard(no);

			// 게시물 화면에 보내기
			request.setAttribute("boardVo", boardVo);
			WebUtil.forward(request, response, "/WEB-INF/views/board/modifyform.jsp");
			
		} else if ("modify".equals(actionName)) {
			// 게시물 가져오기
			String title = request.getParameter("title");
			String content = request.getParameter("content");
			int no = Integer.parseInt(request.getParameter("no"));
			
			BoardVo vo = new BoardVo(no, title, content);
			
			dao.update(vo);
			
			WebUtil.redirect(request, response, "/mysite/board?a=list");
			
		} else if ("writeform".equals(actionName)) {
			// 로그인 여부체크
			UserVo authUser = getAuthUser(request);
			if (authUser != null) { // 로그인했으면 작성페이지로
				WebUtil.forward(request, response, "/WEB-INF/views/board/writeform.jsp");
			} else { // 로그인 안했으면 리스트로
				WebUtil.redirect(request, response, "/mysite/board?a=list");
			}

		} else if ("write".equals(actionName)) {
			
			UserVo authUser = getAuthUser(request);
			int userNo = authUser.getNo();
			System.out.println("userNo : ["+userNo+"]");
			
			String[] fileItem = fileUpload(request, response);
			String fileName= fileItem[0];
			String oriFileName= fileItem[1];
			String title =fileItem[2];
			String content =fileItem[3];
			
			System.out.println("fileName:"+fileName);
			System.out.println("oriFileName:"+oriFileName);
			
			BoardVo vo = new BoardVo(title, content, userNo, fileName, oriFileName);
			dao.insert(vo);

			WebUtil.redirect(request, response, "/mysite/board?a=list");
			

		} else if ("delete".equals(actionName)) {
			
			int no = Integer.parseInt(request.getParameter("no"));
			dao.delete(no);

			WebUtil.redirect(request, response, "/mysite/board?a=list");
			
		} else if ("updateHit".equals(actionName)) {
			
			int no = Integer.parseInt(request.getParameter("no"));
			dao.updateHit(no);
			
			WebUtil.redirect(request, response, "/mysite/board?a=read&no="+no);
			
		} 
		else if("fileUpload".equals(actionName)) {
			
			fileUpload(request, response);
			WebUtil.redirect(request, response, "/mysite/board?a=list");
			
		}else if("getFile".equals(actionName)) {
			
			try {
				downLoadFile(request, response);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			WebUtil.redirect(request, response, "/mysite/board?a=list");
			
			keyWord = request.getParameter("keyWord");
			System.out.println("keyWord get:"+keyWord);
			keyField = request.getParameter("keyField");//ori_filename ,title, content
			System.out.println("keyField get:"+keyField);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	// 로그인 되어 있는 정보를 가져온다.
	protected UserVo getAuthUser(HttpServletRequest request) {
		HttpSession session = request.getSession();
		UserVo authUser = (UserVo) session.getAttribute("authUser");

		return authUser;
	}
	
	public String[] fileUpload(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		
		String[] list=new String[4]; //파일 정보를 담을 리스트 객체
		
        PrintWriter out = response.getWriter();
 
        File attachesDir = new File(ATTACHES_DIR);
 
 
        DiskFileItemFactory fileItemFactory = new DiskFileItemFactory();
        fileItemFactory.setRepository(attachesDir);
        fileItemFactory.setSizeThreshold(LIMIT_SIZE_BYTES);//파일크기 초과 시 임시 파일로 저장
        
        //ServletFileUpload는 HTTP요청에 대한 HTTPServlet Request객체로부터
        //multipart/form-data형식으로 넘어온 HTTP body 부분을 다루기 쉽게 변환.
        ServletFileUpload fileUpload = new ServletFileUpload(fileItemFactory);
 
 
        try {
            List<FileItem> items = fileUpload.parseRequest(request);//FileItem객체로 변환
            int i=2;
            for (FileItem item : items) {
                if (item.isFormField()) {
                	//isFormField()의 리턴값이 false인 경우가 파일데이터로 , 이때 처리하면된다.
                	//파일이 아닌 것들을 getparameter할 수 있다.
                	list[i] =item.getString(CHARSET);
                	i++;
                	
             
                    System.out.printf("파라미터 명 : %s, 파라미터 값 :  %s \n", item.getFieldName(), item.getString(CHARSET));
                } else {
                	//따라서 이때부터 파일 처리 start!
                    System.out.printf("파라미터 명 : %s, 파일 명 : %s,  파일 크기 : %s bytes \n", item.getFieldName(),
                            item.getName(), item.getSize());
                    
                    
                    
                    //long fileSize=item.getSize();
                    	//getName()은 파일명 또는 파일 경로
                    	//getSize()는 파일 사이즈, byte단위
                    	//write(File file)throws Exception은 현재 데이터가 첨부파일일때 매개변수로 넘겨준 File객체의 경로로 출력(저장).
                    if (item.getSize() > 0) {
                    	
                    	//만약 파일명에 파일 경로(separator)가 포함되어 있을 경우, 
                        String separator = File.separator;
                      
                        int index =  item.getName().lastIndexOf(separator);//해당파일에 /가 존재하지 않으면 -1 return
                        
                        /*
                         * lastIndexOf 테스트
                        String str = "user/apple.jpg";
                        int index2 = str.lastIndexOf("/");
                        System.out.println(index2);//4
                        */
                        
                        //item.getName()한것에서 /의 앞의 문자는 제외하고 fileName에 저장.
                        String fileName = item.getName().substring(index  + 1);//0부터 ,  확장명을 포함한 파일명
                        
                        System.out.println("파일존재체크함수 작동전 : "+fileName);
                        String dbFileName = isCheckFileName(fileName);
                	    System.out.println("파일존재체크함수 작동: "+dbFileName);
                		
                        File uploadFile = new File(ATTACHES_DIR +  separator + dbFileName);//가져올 파일의 경로
                        item.write(uploadFile);//경로에 파일 write
                        
                        String oriFileName=item.getName();
                         
                        list[0]=dbFileName;
                        list[1]=oriFileName; 
         
                        return list;
                        
                    }
                }
            }
            System.out.println("<h1>파일 업로드 완료</h1>");
 
 
        } catch (Exception e) {
            // 파일 업로드 처리 중 오류가 발생하는 경우
            e.printStackTrace();
            out.println("<h1>파일 업로드 중 오류가  발생하였습니다.</h1>");
        }
        return null;
		
	}
	public String isCheckFileName(String filename) {
		
		String separator = File.separator;
	    String realName=filename;
		int count=1;
		File file = null;
		boolean bool =true;
		
		file = new File("C:\\Users\\jiwon\\eclipse-workspace\\mysite\\webContent\\WEB-INF\\views\\filecontent"+separator+filename);
		
		if(file.exists()) {
			
			while(bool==true) {	//파일이 경로에 존재하는 동안 반복. //처음에는 존재하지 않을 것임.
				
				realName= filename.substring(0, filename.lastIndexOf(".")) + "("+count+")" + filename.substring(filename.lastIndexOf("."));
				
				file = new File("C:\\Users\\jiwon\\eclipse-workspace\\mysite\\webContent\\WEB-INF\\views\\filecontent"+separator+realName);
				
				bool = file.exists();
				
				count += 1;
				
				if(bool==false) { //존재하지 않는다 => 이 파일명으로 반환해도 된다.
					System.out.println("조정된 파일명: "+realName);
					break;
				}
				
			}
		
		}else {
			//중복되는 파일이 없다면(존재하지 않는다면)
			realName=filename;
		}
		
		
		return realName;
		
	}
	
	public void downLoadFile(HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		Enumeration params = request.getParameterNames();
		while(params.hasMoreElements()) {
		  String name = (String) params.nextElement();
		  System.out.print(name + " : " + request.getParameter(name) + "     "); 
		}
		
		String fileName=request.getParameter("filename");
		
		String fileNameEx="Test";
		//SAVEFOLDER
		File file = new File(ATTACHES_DIR+"/"+fileName);
		
		String mimeType = request.getServletContext().getMimeType(file.toString());//??
		
		if(mimeType == null) {
			response.setContentType("application/octet-stream");
			
		}
		String downName = null;
		if(request.getHeader("user-agent").indexOf("Trident") == -1) {
			downName= new String(fileName.getBytes("UTF-8"), "8859_1");
			
		}else {
			downName = new String(fileName.getBytes("euc-kr"), "8859_1");
			
		}
		
		//전송 객체에 현재 파일을 붙여서 보내겠다.
		response.setHeader("Content-Disposition", "attachment;filename=\"" + downName + "\";");
		
		//
		FileInputStream fileInputStream = new FileInputStream(file);
		ServletOutputStream servletOutputStream = response.getOutputStream();
		
		byte b [] = new byte[1024];
		int data = 0;
		while( (data=(fileInputStream.read(b,0,b.length))) != -1 ) {
			servletOutputStream.write(b,0,data);
			
		}
		servletOutputStream.close();
		fileInputStream.close();
		System.out.println("파일 다운로드 완료");
		
	}
	

}
