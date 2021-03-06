package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import webclient.database;

/**
 * Servlet implementation class deletefromgroup
 */
@WebServlet("/deletefromgroup")
public class deletefromgroup extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int groupid = Integer.parseInt(request.getParameter("groupid"));
		try {
			database.deletegroup(groupid);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
