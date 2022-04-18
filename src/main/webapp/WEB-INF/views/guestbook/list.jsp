<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.javaex.dao.GuestbookDao"%>
<%@ page import="com.javaex.dao.GuestbookDaoImpl"%>
<%@ page import="com.javaex.vo.GuestbookVo"%>
<%@ page import="java.util.List"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
	List<GuestbookVo> list= (List<GuestbookVo>)request.getAttribute("list");
%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link href="/mysite/assets/css/guestbook.css" rel="stylesheet" type="text/css">
	<title>Insert title here</title>
</head>
<body>

	<div id="container">
	
		<c:import url="/WEB-INF/views/includes/header.jsp"></c:import>
		<c:import url="/WEB-INF/views/includes/navigation.jsp"></c:import>
		
		
		<div id="wrapper">
			<div id="content">
				<div id="guestbook">
					
					<form id="guestbook_form" action="/mysite/gb" method="post">
						<input type="hidden" name="a" value="guestbook_regist" />
						<table>
							<tr>
								<td>이름</td><td><input type="text" name="name" /></td>
								<td>비밀번호</td><td><input type="password" name="password" /></td>
							</tr>
							<tr>
								<td colspan=4><textarea name="content" id="content"></textarea></td>
							</tr>
							<tr>
								<td colspan=4 align=right><input type="submit" VALUE=" 확인 " /></td>
							</tr>
						</table>
					</form>
					<%
					//데이터를 가져와서
					//뿌린다.
					//GuestbookDao dao = new GuestbookDaoImpl();
					//List<GuestbookVo> list = dao.getList();
					//List<GuestbookVo> list= request.getAttribute("list");
					for(GuestbookVo vo : list){
					%>
					<ul>
						<li>
							<table>
								<tr>
									<td>[1]</td>
									<td><%= vo.getName() %></td>
									<td><%= vo.getReg_date() %></td>
									<td><a href="/mysite/gb?a=deleteform&no=<%=vo.getNo()%>">삭제</a></td>

								</tr>
								<tr>
									<td colspan=4>
									<%=vo.getContent() %>
									</td>
								</tr>
							</table>
							<br>
						</li>
					</ul>
					
					
					<%
					}
					
					%>
					
					
				</div><!-- /guestbook -->
			</div><!-- /content -->
		</div><!-- /wrapper -->
		
		<div id="footer">
			<p>(c)opyright 2015,2016,2017</p>
		</div> <!-- /footer -->
		
	</div> <!-- /container -->

</body>
</html>