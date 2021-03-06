package controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import webclient.database;

/**
 * Servlet implementation class displayfriendrequest
 */
@WebServlet("/displayfriendrequest")
public class displayfriendrequest extends HttpServlet {
	private static final long serialVersionUID = 1L;
   
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	try {	
			int from=Integer.parseInt(request.getParameter("from"));
			Map<Integer,String> friendrequest=new HashMap<Integer,String>();
			friendrequest=(HashMap<Integer,String>)database.getFriendRequest(from);
			if(friendrequest!=null)
			{
				request.setAttribute("friendrequest",friendrequest);
			}
			 request.setAttribute("from",request.getParameter("from"));
		     RequestDispatcher rd=request.getRequestDispatcher("displayfriendrequest.jsp");  
		     rd.forward(request, response); 
		} catch (Exception e) {
				
				e.printStackTrace();
		}
	}

}
