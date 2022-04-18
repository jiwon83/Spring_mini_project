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
	
	int totalRecord = 0; //전체레코드수
	int numPerPage = 3; // 페이지당 레코드 수 
	int pagePerBlock = 3; //블럭당 페이지수 
	
	int totalPage = 0; //전체 페이지 수
	int totalBlock = 0; //전체 블럭수 
	
	int nowPage = 1; // 현재페이지
	int nowBlock = 1; //현재블럭
	
	int start = 0; //디비의 select 시작번호
	int end = 10; //시작번호로 부터 가져올 select 갯수
	
	int listSize = 0; //현재 읽어온 게시물의 수
	
	//검색기능
	String keyWord = "", keyField = "";
	Vector<BoardVo> vlist = null;
	if (request.getParameter("keyWord") != null) {
		keyWord = request.getParameter("keyWord");
		keyField = request.getParameter("keyField");
		System.out.println("keyword:"+keyWord+"keyfield"+keyField);
	}
	if (request.getParameter("reload") != null){
		if(request.getParameter("reload").equals("true")) {
			keyWord = "";
			keyField = "";
		}
	}
	
	//페이징 처리
	if (request.getParameter("nowPage") != null) {
		nowPage = Integer.parseInt(request.getParameter("nowPage"));
		System.out.println("선택한 현재 페이지:"+nowPage);
	}else{
		System.out.println("Request 에 nowPage 파라메터 없음 !!");
		System.out.println("nowPage:"+nowPage);
	}
	 start = (nowPage * numPerPage)-numPerPage;
	 end = numPerPage;
	 
	 BoardDao dao = new BoardDaoImpl();
	 
	totalRecord = dao.getTotalCount(keyField, keyWord);
	System.out.println("totalRecord: "+ totalRecord);
	totalPage = (int)Math.ceil((double)totalRecord / numPerPage);  //전체페이지수
	System.out.println("totalPage: "+ totalPage);
	nowBlock = (int)Math.ceil((double)nowPage/pagePerBlock); //현재블럭 계산 int(Math.ceil(1.0/3))=>0.3333 
	System.out.println("nowBlock:"+nowBlock);//nowBlock=1
	totalBlock = (int)Math.ceil((double)totalPage / pagePerBlock);  //전체블럭계산
%>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<link href="/mysite/assets/css/board.css" rel="stylesheet" type="text/css">

<script type="text/javascript">
	function list() {
		document.listFrm.action = "list.jsp";
		document.listFrm.submit();
	}
	
	function pageing(page) {
		document.readFrm.nowPage.value = page;
		document.readFrm.submit();
	}
	
	function block(value){
		 document.readFrm.nowPage.value=<%=pagePerBlock%>*(value-1)+1;
		 document.readFrm.submit();
	} 
	
	function read(num){
		document.readFrm.num.value=num;
		document.readFrm.action="read.jsp";
		document.readFrm.submit();
	}
	//키워드 검색 기능
	function check() {
	     if (document.searchFrm.keyWord.value == "") {
	   		alert("검색어를 입력하세요.");
	   		document.searchFrm.keyWord.focus();//focus(): input태그의 입력상태
	   		return;
	     }
	  	 document.searchFrm.submit();//
	 }
</script>


<title>Mysite</title>
</head>
<body>
	<div id="container">
		
		
		<c:import url="/WEB-INF/views/includes/header.jsp"></c:import>
		<c:import url="/WEB-INF/views/includes/navigation.jsp"></c:import>
		
		<table align="center" width="600">
			<tr>
				<td>Total : <%=totalRecord%>Articles(<font color="red">
				<%=nowPage%>/<%=totalPage%>Pages</font>)</td>
			</tr>
		</table>
		
		<div id="content">
			<div id="board">
				
				<form name ="searchFrm" id="search_form" action="list2.jsp" method="post">
					<table width="600" cellpadding="4" cellspacing="0">
						<tr>
							<td align="center" valign="bottom">
	   							<select name="keyField" size="1" >
	    							<option value="name"> 이 름</option>
	    							<option value="subject"> 제 목</option>
	    							<option value="content"> 내 용</option>
	   							</select>
	   							<input size="16" type="text" id="keyWord" name="keyWord" value="">
								<input type="submit" value="찾기" onClick="javascript:check()">
	   							<input type="hidden" name="nowPage" value="1">
	   				
  							</td>
						</tr>
					</table>
					
				</form>
				<!-- 게시글 목록 -->
				<%
				  vlist = dao.getBoardList(keyField, keyWord, start, end);
				  listSize = vlist.size();//브라우저 화면에 보여질 게시물갯수
				  if (vlist.isEmpty()) {
					out.println("등록된 게시물이 없습니다.");
				  } else { 
				%>
				<table class="tbl-ex">
					<tr>
						<th>번호</th>
						<th>제목</th>
						<th>글쓴이</th>
						<th>조회수</th>
						<th>작성일</th>
						<th>&nbsp;</th>
					</tr>	
					<%
						  for (int i = 0;i<numPerPage; i++) {
							if (i == listSize) break;
							BoardVo vo = vlist.get(i);
							int no = vo.getNo();
							String title = vo.getTitle();
							int hit = vo.getHit();
							String regDate = vo.getRegDate();
							
					%>			
					
					<tr>
						<td><%=no %></td>
						<td><a href="/mysite/board?a=read&no=${vo.no }">${vo.title }</a></td>
						<td><%=title %></td>
						<td><%=hit %></td>
						<td><%=regDate %></td>
						<td>
								
							<a href="/mysite/board?a=delete&no=<%=no %>" class="del">삭제</a>
								
						</td>
					</tr>
					
					<%}//for%>
					<%
					}//if
					%>
			
					
					<tr>
						<td colspan="2"><br /><br /></td>
					</tr>
					
					<tr>
							<td>
							 
							<%
   				  			int pageStart = (nowBlock -1)*pagePerBlock + 1 ; //하단 페이지 시작번호
   				  			int pageEnd = ((pageStart + pagePerBlock ) <= totalPage) ?  (pageStart + pagePerBlock): totalPage+1; 
   				  			//하단 페이지 끝번호
   				  			if(totalPage !=0){
    			  				if (nowBlock > 1) { %>
    			  					<a href="javascript:block('<%=nowBlock-1%>')">prev...</a><%}%>&nbsp; 
    			  					<%for ( ; pageStart < pageEnd; pageStart++){%>
     			     				<a href="javascript:pageing('<%=pageStart %>')"> 
     								<%if(pageStart==nowPage) {%><font color="blue"> <%}%>
     								[<%=pageStart %>] 
     								<%if(pageStart==nowPage) {%></font> <%}%></a> 
    								<%}//for%>&nbsp; 
			    					<%if (totalBlock > nowBlock ) {%>
			    					<a href="javascript:block('<%=nowBlock+1%>')">.....next</a>
			    				<%}%>&nbsp;  
			   				<%}%>
   				
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
				
				<!-- 책보고 end -->			
			</div>
		</div>
		
		<c:import url="/WEB-INF/views/includes/footer.jsp"></c:import>
		<form name="listFrm" method="post">
		<input type="hidden" name="reload" value="true"> <!-- 처음으로 기능 예상 -->
		<input type="hidden" name="nowPage" value="1">
		</form>
		<form name="readFrm" method="get" action="/mysite/gb?a=pageing">
		<input type="hidden" name="num"> 
		<input type="hidden" name="nowPage" value="<%=nowPage%>">
		<input type="hidden" name="keyField" value="<%=keyField%>"> 
		<input type="hidden" name="keyWord" value="<%=keyWord%>"> 
		
		
	</form>
	</div><!-- /container -->
</body>
</html>		
		
