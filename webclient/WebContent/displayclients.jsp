<%@ page import="java.sql.*" language="java"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="webclient.*"%>
<%@page import="java.util.*"%>
<!DOCTYPE html>
<html>
<head>
<style>
p {
	background: #97cffb;
	margin: auto;
}

div {
	position: relative;
	left: 300px;
	background: #97cffb;
	width
	=50px;
}

button:hover {
	filter: brightness(85%);
}

.button:active {
	transform: translateY(4px);
}

}
body {
	color: #0436af;
	font-size: 20px;
}
</style>
<meta charset="UTF-8">
<title>available users</title>
</head>
<body bgcolor="#ccf5ff">
	<script>
//console.log("script executed!!!");
var id="<%=(String)request.getAttribute("username")%>";<% String id=(String)request.getAttribute("username");%>
<%Map<String,String> userlist=(HashMap<String,String>)request.getAttribute("list");%>
 var webSocket = new WebSocket("ws://"+window.location.hostname+":8080/webclient/websocket/"+id);
webSocket.onmessage = function(message){ wsGetMessage(message);};
var tempuser="5jhgfd";
window.user=tempuser;


function wsSendMessage(msg){
	var object='{"to":"'+window.user+'","text":"'+msg+'","msg":"0","from":"'+id+'"}';
	displaymsg.value+="\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+msg+"\n";
	webSocket.send(object);
	textID.value="";
	
	

}
function wsGetMessage(message){
	
	var a=message.data;

		var msg=JSON.parse(a);
		/* if(msg.id.localeCompare("@"))
			{
				if(window.showfriendrequest.localeCompare("0")==0)
				{
					displaymsg.value +=  msg.text + "\n"; 
				}
			}
			else
			{ */
				if((window.user.localeCompare(msg.from))==0)
				{
					displaymsg.value +=  msg.text + "\n"; 
				}
				else
				{
					var http;
		
					  http = new XMLHttpRequest();
					  http.onreadystatechange = function() {
					    if (this.readyState == 4 && this.status == 200) 
					    {
					      document.getElementById(msg.from).innerHTML=this.responseText;
					    }
					  };
					  http.open("POST","notif", true);
					  http.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
					  http.send("uniqueid="+msg.id);
				}
			
}
function wsSendFriendRequest(status,to)
{	
	var object='{"to":"'+to+'","from":"'+id+'","msg":"1","status":"'+status+'"}';
	webSocket.send(object);
}

</script>


	<p style="width: 200px; margin-left: 0px;">
		welcome
		<%=id %><br>available users are
	</p>
	<button id="showfriend" onclick="showFriendRequest()">show
		friendrequest</button>
	<br>
	<form name="vinform">
		<input type="text" name="name" onkeyup="searchInfo()"
			autocomplete="off">
	</form>
	<form name='clients'
		style="width: 200px; position: relative; right: 300px;">
		<%for (Map.Entry<String,String> entry : userlist.entrySet()) { %>
		<button type='button' name='clientlist' value='<%=entry.getKey()%>'
			onclick='sendinfo(this.value)'
			style="width: 216px; height: 50px; margin-left: 300px; background-color: #97cffb; box-shadow: 0px 0px 0 1px #147de8; margin-bottom: 10px; position: absolute; color: #0436af; font-size: 20px;"><%=entry.getKey()%></button>
		<div id='<%= entry.getKey()%>'
			style="width: 25px; height: 25px; left: 490px; right: 0px; position: relative; margin-bottom: 30px; bottom: -10px; background-color: #97cffb;"><%=(entry.getValue().equals("0"))?"": entry.getValue()%></div>
		<%} %>

	</form>
	<br>
	<a href='index.html'> logout</a>
	<div id="users" style="position: absolute; top: 0px; width: 1500px;">user
		will be displayed here</div>
	<script>
function showvalue(str)
{var xhttp; 

var a="user="+str+"&from=<%=id%>"; 

xhttp = new XMLHttpRequest();
xhttp.onreadystatechange = function() {
  if (this.readyState == 4 && this.status == 200) {
    document.getElementById("usersa").innerHTML = this.responseText;
  }
};
xhttp.open("POST", "displaydetails", true);
xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	xhttp.send(a);
	}function sendinfo(str) {
	 window.user=str;
  var xhttp; 
  
  var a="user="+str+"&from=<%=id%>"; 

  xhttp = new XMLHttpRequest();
  xhttp.onreadystatechange = function() {
    if (this.readyState == 4 && this.status == 200) {
      document.getElementById("users").innerHTML = this.responseText;
      document.getElementById(window.user).innerHTML="";
    }
  };
  xhttp.open("POST", "sendbox", true);
  xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	xhttp.send(a);}</script>
	<script>
	function searchInfo()
	{	var name=document.vinform.name.value;

			var http;
			if(name == "")
				{
				document.getElementById("displayall").style.display="none";
				}
			else
				{
				document.getElementById("displayall").style.display="block";
				}
			var senda="value="+name+"&from=<%=id%>";
			http=new XMLHttpRequest();
			http.onreadystatechange=function(){
				if(this.readyState==4&&this.status==200)
					{
					document.getElementById("displayall").innerHTML=this.responseText;
					
					}
			};
			
			http.open("POST","showalluser",true);
			http.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
			http.send(senda);
		
			
		
		}function showFriendRequest(){
			window.showfriendrequest="0";
			var xhttp; 

			var a="from=<%=id%>"; 

			xhttp = new XMLHttpRequest();
			xhttp.onreadystatechange = function() {
			  if (this.readyState == 4 && this.status == 200) {
			    document.getElementById("displayfriendrequest").innerHTML = this.responseText;
			  }
			};
			xhttp.open("POST", "displayfriendrequest", true);
			xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
				xhttp.send(a);

		}
		function acceptrequest(num,to)
		{

				var xhttp; 

				var a="from=<%=id%>&to="+to+"&num="+num;

				xhttp = new XMLHttpRequest();
			/* 	xhttp.onreadystatechange = function() {
				  if (this.readyState == 4 && this.status == 200) {
				    document.getElementById("displayfriendrequest").innerHTML = this.responseText;
				  }
				}; */
				xhttp.open("POST", "acceptfriend", true);
				xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
					xhttp.send(a);
				
			
		}
	</script>
	<div id="displayall"
		style="position: absolute; left: 8px; top: 77px; width: 130px;"></div>
	<div id="displayfriendrequest"></div>
</body>
</html>