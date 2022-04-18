<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
test
<c:out value = "${pageStart}"/>
<h1>${param.pageStart}</h1>
<c:set var="filename" value="hello"/>
<%!
	String pageStart2= "${pageStart}";

%>
<script>
 function click(){
 
 window.open('/mysite/board?a=read&no=3');
 window.open('/mysite/board?a=read&no=4');
  }
 </script>
<a href="javascript:click()">여러링크버튼</a>



<c:forEach var="i" begin="1" end="4" step="1">
		<a href="/mysite/board?a=list&nowPage=${pageStart }">
			
				<font color="blue">
					<c:out value="["/>${i}<c:out value="]"/>
				</font>
				
			
		</a>
</c:forEach>

<c:forEach begin="1" end="4" step="1">
		<a href="/mysite/board?a=list&nowPage=${pageStart }">
			
				<font color="blue">
					<c:out value="["/>${pageStart}<c:out value="]"/>
				</font>
				
			
		</a>
</c:forEach>
<c:if test="${  filename ne null && (!empty filename ) }" >
	<c:out value="${filename }"/>
</c:if>
<c:if test="${  filename ne null }" >
	<c:out value="${filename }"/>
</c:if>

</body>
</html>