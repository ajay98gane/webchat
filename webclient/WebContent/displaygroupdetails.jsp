<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@page import="java.util.*"%><%@page import="webclient.*" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body class="availableusers">
	<p style="background-color: #93ccff;"><%=request.getAttribute("toname")%>
		<br>users
	</p>

	<%
		int fromid = (Integer) request.getAttribute("from");
		int toid = (Integer) request.getAttribute("to");
		String fromname=(String) request.getAttribute("fromname");
		List<GroupUser> groupusers = (ArrayList<GroupUser>) request.getAttribute("userdetails");
			for(GroupUser entry:groupusers){
				if(entry.getUser().getId()==fromid)
				{%>
				<div class='availableusers'
		onclick="showvalue('<%=fromid%>','<%=fromname%>')"><%=fromname%>   -   <button  class='admin' >user </button>  <button class='availableusers' onclick="removefromgroup('<%=fromid%>','<%=toid%>')">exit group</button></div> 
	<%}else{%>
	<div class='availableusers'>
		<span onclick="showvalue('<%=entry.getUser().getId()%>','<%=entry.getUser().getName()%>')"><%=entry.getUser().getName()%>   -  </span><button class='admin' > <%=entry.getAdminStatus().equals("1") ? "admin" : "user" %></button></div>
	<%
		}}
	%>
	
</body>
</html>