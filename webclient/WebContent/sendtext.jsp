<%@ page import="java.sql.*" language="java"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="webclient.*"%>
<%@page import="java.util.*"%><%@page import="org.json.*"%>


<div class="availableusers" style="color:white;"
	onclick="showvalue(<%=(Integer) request.getAttribute("to")%>,'<%=(String) request.getAttribute("toname")%>')"><%=(String) request.getAttribute("toname")%></div>

<div id="testing">


	<div id="displaymsg" rows="10" cols="100"
		onscroll="scrollingfunction(<%=((Integer) request.getAttribute("to"))%>,'<%=(String) request.getAttribute("toname")%>')">
		<div class="align" id="alignno">
			<div id="secondtest">
				<%

					List<Message> msglist = (ArrayList<Message>) request.getAttribute("msglist");

					
						for(Message msg: msglist)
						{

						if (msg.getUser().equals("0")) {
				%><span class="from"><span class="fromsub"><%=(msg.getText())%></span><span
					class="fromchattime"><%=msg.getTime()%></span></span><br>
				<%
					} else {
				%><span class="to"><span class="tosub"><%=msg.getText()%></span><span
					class="tochattime"><%=msg.getTime()%></span></span><br>
				<%
					}
					}
				%>
			</div>
		</div>
	</div>
	<input id="textID" type="text" onkeyup="sendclick(event)"
		style="width: 95%; height: 25px; background: #ccf5ff; left: 2%; position: relative;">
	<button id="sendu" onclick="wsSendMessage(textID.value)" value="Send"
		style="position: absolute; right: 2.5%; height: 30px; width: 51px; box-shadow: 0px 0px 0 1px #147de8;">send</button>


</div>
<p>


	<br>
</p>



