<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%> 
<% pageContext.setAttribute( "newLine", "\n" ); %>
<%@ page import= "com.javaex.dao.*"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<link href="/mysite/assets/css/board.css" rel="stylesheet" type="text/css">
<title>Mysite</title>
<script type="text/javascript">
	
	function down(filename){
		
		/*
		alert("down함수 실행");
		System.out.println("down함수 실행");
		BoardDao dao= new BoardDaoImpl();
		dao.downLoad(request, response, out, pageContext, filename);
		System.out.println("down함수 실행");
		
		*/
		//
		alert(filename);//ok
		 document.downFrm.filename.value=filename;
		 document.downFrm.action= "<%=application.getContextPath() %>/board?a=getFile";
		 document.downFrm.submit();
		 //Window.open('/mysite/board?a=getFile&no=');
		 
	}
	
</script>
<%
	//BoardDao dao= new BoardDaoImpl();
	//dao.downLoad(request, response, out, pageContext);
	//dao.downLoad(request, response, out, pageContext);
%>
</head>
<body>
	<div id="container">
		
		<c:import url="/WEB-INF/views/includes/header.jsp"></c:import>
		<c:import url="/WEB-INF/views/includes/navigation.jsp"></c:import>
		
		<div id="content">
			<div id="board" class="board-form">
				<table class="tbl-ex">
					<tr>
						<th colspan="2">글보기</th>
					</tr>
					<tr>
						<td class="label">제목</td>
						<td>${boardVo.title }</td>
					</tr>
					<tr>
						<td class="label">내용</td>
						<td>
							<div class="view-content">
								${fn:replace(boardVo.content, newLine, "<br>")}
							</div>
						</td>
					</tr>
					<tr>
						<td class="label">첨부파일</td>
						<td>
							<c:if test="${  boardVo.fileName ne null && (!empty boardVo.fileName ) }" >
								<a href="javascript:down('${boardVo.oriFileName }')">${boardVo.oriFileName }</a>&nbsp;&nbsp;
								<font color="blue">${boardVo.fileSize}KBytes</font>
							</c:if>
							<!-- null값이라면 -->
							<c:if test="${  boardVo.fileName eq null || (empty boardVo.fileName ) }" >
								등록된 파일이 없습니다.
		
							</c:if>
							
						</td>
					</tr>
				</table>
				<div class="bottom">
					<a href="/mysite/board">글목록</a>
					
					<c:if test="${authUser.no == boardVo.userNo }">
						<a href="/mysite/board?a=modifyform&no=${boardVo.no }">글수정</a>
					</c:if>
				</div>
			</div>
		</div>

		<c:import url="/WEB-INF/views/includes/footer.jsp"></c:import>
		
	</div><!-- /container -->
	<form name="downFrm" action="<%=application.getContextPath() %>/board?a=getFile" method="post">
	<input type="hidden" name="filename">
	
	
</form>
</body>
</html>		
		
