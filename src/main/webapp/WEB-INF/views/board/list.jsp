<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="java.util.Vector"%>
<%@page import="com.javaex.vo.PageDTO"%>
<%@ page import="com.javaex.vo.Criteria"%>
<%@ page import="com.javaex.vo.BoardVo"%>
<%@ page import="com.javaex.dao.*"%>
<!DOCTYPE html>
<%

	request.setCharacterEncoding("UTF-8");
	int listSize = 0; //현재 읽어온 게시물의 수
%>

<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<link href="/mysite/assets/css/board.css" rel="stylesheet" type="text/css">

<script type="text/javascript">

	function block(value){
		 document.readFrm.nowPage.value=${pagePerBlock}*(value-1)+1;//value가 2이라면,3*1+1=4
		 document.readFrm.action="/mysite/board?a=list";
		 document.readFrm.submit();//sumit하면 페이지가 변한다.

	}
	/*
	 function click(no){
		 window.open('/mysite/board?a=read&no='+no);
		 window.open('/mysite/board?a=updateHit&no='+no);
		 
	 }
	*/
</script>
<title>Mysite</title>
</head>
<body>
	<div id="container">
	
		<c:import url="/WEB-INF/views/includes/header.jsp"></c:import>
		<c:import url="/WEB-INF/views/includes/navigation.jsp"></c:import>
		
		<table align="center" width="600">
			<tr>
				<td>Total : <c:out value="${totalRecord }"/>Articles(<font color="red">
				${nowPage }/ <c:out value="${totalPage }"/>Pages</font>)</td>
				<td><c:out value="[nowPage="/>${nowPage}<c:out value="]"/></td>
			</tr>
		</table>
		
		<div id="content">
			<div id="board">
				
				<form name ="searchFrm" id="search_form" action="/mysite/board" method="post">
					<table width="600" cellpadding="4" cellspacing="0">
						<tr>
							<td align="center" valign="bottom">
	   							<select name="keyField" size="1" >
	    							<option value="title"> 제 목</option>
	    							<option value="userNo"> 이 름</option>
	    							<option value="content"> 내 용</option>
	    							<option value="ori_filename"> 파일명</option>
	    							
	   							</select>
	   							<input size="16" type="text" id="keyWord" name="keyWord" value="">
								<input type="submit" value="찾기">
	   							<input type="hidden" name="nowPage" value="1">
	   				
  							</td>
						</tr>
					</table>
					
				</form>
				<!-- 게시글 목록 -->
				
				<table class="tbl-ex">
					<tr>
						<th>번호</th>
						<th>제목</th>
						<th>글쓴이</th>
						<th>조회수</th>
						<th>작성일</th>
						<th>&nbsp;</th>
					</tr>
					
					<c:forEach items="${list }" var="vo">
						<tr>
							<td>${vo.no }</td>
							<td><a href="/mysite/board?a=updateHit&no=${vo.no }">${vo.title }</a></td>
							<td>${vo.userName }</td>
							<td>${vo.hit }</td>
							<td>${vo.regDate }</td>
							<td>
								<c:if test="${authUser.no == vo.userNo }">
									<a href="/mysite/board?a=delete&no=${vo.no }" class="del">삭제</a>
								</c:if>
							</td>
						</tr>
						
					</c:forEach>	
				
					<tr>
						<td colspan="2"><br /><br /></td>
					</tr>
					<tr>
						<td>
							
								<!-- 페이지 블록 (1,2,3,-> 4,5,6-> 7,8)을 생성하고 페이지번호를 누르면 servlet으로 보내주는 작업 -->
							<c:if test="${totalPage !=0 }">
							
								<c:if test="${nowBlock >1 }">
									<a href="javascript:block('${nowBlock-1 }')">prev...</a>&nbsp; 
									
								</c:if>
										
								<c:out value="start=${pageStart }"/>
								<c:out value="end=${pageEnd }"/>
								<c:out value="end=${nowPage }"/>
								<c:forEach var="i" begin="${pageStart }" end="${pageEnd-1 }" step="1">
										
										<a href="/mysite/board?a=list&nowPage=${i}">
											<c:if test="${i==nowPage }">
												<font color="blue">
													<c:out value="["/>${i}<c:out value="]"/>
												</font>
											</c:if>
											<c:if test="${i!=nowPage }">
												<c:out value="["/>${i}<c:out value="]"/>
											</c:if>
											
										</a>
								</c:forEach>
								
								<c:if test="${totalBlock > nowBlock}">
									<a href="javascript:block('${nowBlock+1 }')">.....next</a>
							
								</c:if>
								
							</c:if>
						</td>
					</tr>	
				</table>
				
				<div class="pager">
					
				</div>				
				<c:if test="${authUser != null }">
					<div class="bottom">
						<a href="/mysite/board?a=writeform" id="new-book">글쓰기</a>
					</div>
				</c:if>	
						
			</div>
		</div>
		
		<c:import url="/WEB-INF/views/includes/footer.jsp"></c:import>
		
		<form name="listFrm" method="post">
		<input type="hidden" name="reload" value="true"> <!-- 처음으로 기능 예상 -->
		<input type="hidden" name="nowPage" value="1">
		</form>
		<form name="readFrm" method="post">
		<input type="hidden" name="num"> 
		<input type="hidden" name="nowPage" value="${nowPage }">
		<input type="hidden" name="keyWord" value="${keyWord }">
			
	</form>
	</div><!-- /container -->
</body>
</html>		
		
