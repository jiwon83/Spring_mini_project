package com.javaex.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.javaex.dao.BoardDao;
import com.javaex.dao.BoardDaoImpl;
import com.javaex.dao.GuestbookDao;
import com.javaex.dao.GuestbookDaoImpl;
import com.javaex.util.WebUtil;
import com.javaex.vo.BoardVo;
import com.javaex.vo.GuestbookVo;
import com.javaex.vo.UserVo;

@WebServlet("/gb")
public class GbServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		request.setCharacterEncoding("UTF-8");

		String actionName = request.getParameter("a");
		System.out.println("user:" + actionName);

		GuestbookDao dao = new GuestbookDaoImpl();
		
		
		if ("list".equals(actionName)) {
			
			List<GuestbookVo> list = dao.getList();
			request.setAttribute("list", list);

			System.out.println(list.toString());

			// 리스트 화면에 보내기
			WebUtil.forward(request, response, "/WEB-INF/views/guestbook/list.jsp");
			
			
		
		}else if ("guestbook_regist".equals(actionName)) {
			String name = request.getParameter("name");
			String password = request.getParameter("password");
			String content = request.getParameter("content");

			GuestbookVo vo = new GuestbookVo(name, password, content);
			dao.insert(vo);
			
			WebUtil.redirect(request, response, "/mysite/gb?a=list");

			//List<GuestbookVo> list2 = dao.getList();//list를 다시 받아서 list를 업데이트해서 전송
			//request.setAttribute("list", list2);

			//RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/guestbook/list.jsp");
			//rd.forward(request, response);

			// id(email) 중복검사 : api/emailCheck.jsp 대신 적용

		} else if("delete".equals(actionName)) { 
			
			//패스워드가 일치하면 삭제, 패스워드만 확인!
			String password = request.getParameter("password");
//			UserVo user = getAuthUser(request);
//			int no = user.getNo();
			dao.delete(password);
		
			WebUtil.redirect(request, response, "/mysite/gb?a=list");
			
		} else if("deleteform".equals(actionName)) {
			
			WebUtil.forward(request, response, "/WEB-INF/views/guestbook/deleteform.jsp");
		}
		/*
		else if("idcheck".equals(actionName)) {
      String email = request.getParameter("email");
      GuestbookDao dao = new GuestbookDaoImpl();
      
//      Enumeration e = request.getParameterNames();
//      while ( e.hasMoreElements() ){
//        String name = (String) e.nextElement();
//        String[] values = request.getParameterValues(name);   
//        for (String value : values) {
//          System.out.println(this.getClass().getName() + ".name=" + name + ",value=" + value);
//        }   
//      }
      
      response.setContentType("text/html; charset=UTF-8");

      // "true", "false" 문자열이 반환되므로 ajax에서 결과값으로 받아서 처리
      response.getWriter().write(dao.idCheck(email)); 
      
    } else if("modify".equals(actionName)){
			String name = request.getParameter("name");
			String password = request.getParameter("password");
			String gender = request.getParameter("gender");
			GuestbookVo vo = new GuestbookVo();
			vo.setName(name);
			vo.setPassword(password);
			vo.setGender(gender);
			
			HttpSession session = request.getSession();
			GuestbookVo authUser = (GuestbookVo)session.getAttribute("authUser");
			
			int no = authUser.getNo();
			vo.setNo(no);
			
			GuestbookDao dao = new GuestbookDaoImpl();
			dao.update(vo);
			
			authUser.setName(name);
			
			WebUtil.forward(request, response, "/WEB-INF/views/main/index.jsp");
			
		} else if("modifyform".equals(actionName)) {
			
			HttpSession session = request.getSession();//session 생성
			GuestbookVo authUser = (GuestbookVo)session.getAttribute("authUser");
			int no = authUser.getNo(); // 회원번호
			
			GuestbookDao dao = new GuestbookDaoImpl();
			GuestbookVo userVo = dao.getUser(no);//데이터베이스에서 가져온 정보
			System.out.println(userVo.toString());
			
			request.setAttribute("userVo", userVo);
			WebUtil.forward(request, response, "/WEB-INF/views/user/modifyform.jsp");
			
		} else if("loginform".equals(actionName)){
			
			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/user/loginform.jsp");
			rd.forward(request, response);
			
		} else if("login".equals(actionName)){
			String email = request.getParameter("email");
			String password =request.getParameter("password");
			
			GuestbookDao dao = new GuestbookDaoImpl();
			GuestbookVo vo = dao.getUser(email, password);
			
			if(vo==null) {
				System.out.println("실패");
				response.sendRedirect("/mysite/user?a=loginform&result=fail");
			} else {
				System.out.println("성공");
				HttpSession session = request.getSession(true);
				session.setAttribute("authUser", vo);//header.jsp에서 사용
				
				response.sendRedirect("/mysite/main");
				return;
			}
			
		} else if("logout".equals(actionName)){
			HttpSession session = request.getSession();
			session.removeAttribute("authUser");//세션의 authUser만 삭제
			session.invalidate();//세션정보 모두 삭제
			response.sendRedirect("/mysite/main");
			
		} 
		*/
		else {
			//action tag에 아무 값도 없을 때
			WebUtil.redirect(request, response, "/mysite/gb?a=list");
			//WebUtil.forward(request, response, "/mysite/gb?a=list");
			//WebUtil.redirect(request, response, "/mysite/main");
			/*response.sendRedirect("/mysite/main");*/
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

}
