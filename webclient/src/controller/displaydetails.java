package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import webclient.*;

/**
 * Servlet implementation class displaydetails
 */
@WebServlet("/displaydetails")
public class displaydetails extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		int user = Integer.parseInt(request.getParameter("user"));
		String username = request.getParameter("username");
		String fromname = request.getParameter("fromname");
		int from = Integer.parseInt(request.getParameter("from"));
		Map<String, String> userdetails ;
		List<Pair<Users,String>> friends;
		List<Pair<Group,String>> group ;
		List<GroupUser> groupusers;

		try {
			boolean groupcheck = database.checkGroup(user);
			if (groupcheck == false) {
				userdetails = (HashMap<String, String>) database.getUserDetails(user);

				friends = (ArrayList<Pair<Users,String>>) database.getFriends(user);
				group=(ArrayList<Pair<Group,String>>)database.getGroup(user);
				String bool = database.friendsCheck(from, user);
				request.setAttribute("bool", bool);
				request.setAttribute("toname", username);
				request.setAttribute("userdetails", userdetails);
				request.setAttribute("to", user);
				request.setAttribute("group",group);
				request.setAttribute("friends", friends);
				RequestDispatcher rd;
				if (bool.equals("accept friend")) {
					request.setAttribute("request",true);
					rd = request.getRequestDispatcher("displaydetailscheck.jsp");

				} else if (bool.equals("cancel request")) {
					request.setAttribute("request",false);

					rd = request.getRequestDispatcher("displaydetailscheck.jsp");
				} else {
					request.setAttribute("request",false);

					rd = request.getRequestDispatcher("displaydetailscheck.jsp");
				}
				rd.forward(request, response);
			}
			else
			{
				groupusers=(ArrayList<GroupUser>)database.getGroupUserDetails(user,from);
				boolean adminAccess=database.checkAdminAccess(from,user);
				request.setAttribute("toname", username);
				request.setAttribute("userdetails", groupusers);
				request.setAttribute("to", user);
				request.setAttribute("from",from);
				request.setAttribute("fromname",fromname);
				RequestDispatcher rd;
				if(adminAccess==true)
				{	
					rd=request.getRequestDispatcher("displaygroupdetailsadmin.jsp");
				}
				else
				{
					rd=request.getRequestDispatcher("displaygroupdetails.jsp");
				}
				rd.forward(request,response);
						
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

}
