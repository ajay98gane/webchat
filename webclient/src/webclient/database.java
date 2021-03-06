package webclient;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLSyntaxErrorException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

public class database  {
		public static void createTable() {
		try {

			Connection con = getConnection();
			PreparedStatement createmtable = con.prepareStatement(
					"CREATE TABLE IF NOT EXISTS userdetails(user_id INT PRIMARY KEY ,username TEXT ,mobileno TEXT ,name TEXT,emailid TEXT,address TEXT)");
			createmtable.executeUpdate();
			PreparedStatement createetable = con
					.prepareStatement("CREATE TABLE IF NOT EXISTS password(username TEXT,pass TEXT)");
			createetable.executeUpdate();
			PreparedStatement createmsgtable = con.prepareStatement(
					"CREATE TABLE IF NOT EXISTS messages(pair_id INT,msg TEXT,timeat TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
			createmsgtable.executeUpdate();
			PreparedStatement createmaptable = con.prepareStatement(
					"CREATE TABLE IF NOT EXISTS msgmap(fromuser INT,touser INT,notif INT DEFAULT '0',pair_id INT PRIMARY KEY AUTO_INCREMENT,group_verify boolean DEFAULT FALSE,admin_access boolean DEFAULT FALSE)");
			createmaptable.executeUpdate();
			PreparedStatement createfriendrequest = con
					.prepareStatement("CREATE TABLE IF NOT EXISTS friendrequest(fromuser INT,touser INT)");
			createfriendrequest.executeUpdate();
			PreparedStatement createtableid = con.prepareStatement(
					"CREATE TABLE IF NOT EXISTS group_id_holder(group_id INT PRIMARY KEY,groupname TEXT,timeat TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
			createtableid.executeUpdate();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Connection getConnection() throws Exception {
		Class driver_class = Class.forName("com.mysql.jdbc.Driver");
		Driver driver = (Driver) driver_class.newInstance();
		DriverManager.registerDriver(driver);
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/web", "root", "root");
		return con;

	}

	public static boolean usernameChecker(String username) throws Exception {
		boolean usernam = false;
		Connection con = getConnection();
		PreparedStatement userName = con
				.prepareStatement("select username from userdetails where username='" + username + "'");
		ResultSet un = userName.executeQuery();
		while (un.next()) {

			usernam = true;
		}
		con.close();
		return usernam;
	}

	public static String getUserId(String username) throws Exception {
		Connection con = getConnection();
		PreparedStatement getUserId = con
				.prepareStatement("SELECT user_id from userdetails where username='" + username + "'");
		ResultSet getId = getUserId.executeQuery();
		String userId = "";
		while (getId.next()) {
			userId = getId.getString("user_id");
		}
		con.close();
		return userId;

	}

	public static void addValuesToGroupTable(int uniqueId, String groupName) throws Exception {
		Connection con = getConnection();
		PreparedStatement addGroupIdTable = con.prepareStatement(
				"INSERT INTO group_id_holder(group_id,groupname) VALUES(" + uniqueId + ",'" + groupName + "')");
		addGroupIdTable.executeUpdate();
		con.close();

	}

	public static void addValueInfoTable(int uniqueUserId, String username, String pass, String name, String mobileno,
			String emailid, String address) throws Exception {
		Connection con = getConnection();
		PreparedStatement putinfotable = con.prepareStatement("INSERT INTO userdetails VALUES(" + uniqueUserId + ",'"
				+ username + "','" + mobileno + "','" + name + "','" + emailid + "','" + address + "')");
		PreparedStatement putpasstable = con
				.prepareStatement("INSERT INTO password VALUES('" + username + "','" + pass + "')");
		putpasstable.executeUpdate();
		putinfotable.executeUpdate();
		con.close();
	}

	public static boolean loginCheck(String user, String pass) throws Exception {
		Connection con = getConnection();
		boolean check = false;
		PreparedStatement checkLogin = con.prepareStatement("SELECT pass FROM password WHERE username='" + user + "'");
		ResultSet result = checkLogin.executeQuery();
		while (result.next()) {

			if (pass.equals(result.getString("pass"))) {

				check = true;
			}
		}
		con.close();
		return check;
	}

	public static void removeFriendRequest(int from, int to) throws Exception {
		Connection con = getConnection();
		PreparedStatement deletefriendrequest = con
				.prepareStatement("DELETE FROM friendrequest WHERE fromuser=" + to + " AND touser=" + from);
		deletefriendrequest.executeUpdate();
	}

	public static void addFriend(int from, int to) throws Exception {
		Connection con = getConnection();
		PreparedStatement addfriend = con
				.prepareStatement("INSERT INTO msgmap(fromuser,touser) VALUES(" + from + "," + to + ")");
		PreparedStatement addfriend2 = con
				.prepareStatement("INSERT INTO msgmap(fromuser,touser) VALUES(" + to + "," + from + ")");
		addfriend.executeUpdate();
		addfriend2.executeUpdate();
		con.close();

	}

	public static void addUsersToGroup(int from, int to, boolean group, boolean access) throws Exception {
		Connection con = getConnection();
		PreparedStatement addUsersToGroup = con
				.prepareStatement("INSERT INTO msgmap(fromuser,touser,group_verify,admin_access) VALUES(" + from + ","
						+ to + "," + group + "," + access + ")");
		addUsersToGroup.executeUpdate();
		con.close();

	}

	public static List<Pair<Users, String>> getFriends(int username) throws Exception {
		List<Pair<Users, String>> friendlist = new ArrayList<>();

		Connection con = database.getConnection();

		PreparedStatement checkLogin = con.prepareStatement(
				"SELECT msgmap.fromuser,msgmap.notif,msgmap.group_verify,userdetails.username FROM msgmap INNER JOIN userdetails on msgmap.fromuser=userdetails.user_id WHERE msgmap.touser= "
						+ username + " AND msgmap.group_verify=false");
		ResultSet result = checkLogin.executeQuery();
		while (result.next()) {

			Users user = new Users(Integer.parseInt(result.getString("fromuser")), result.getString("username"));
			Pair<Users, String> pair = new Pair<>(user, result.getString("notif"));
			friendlist.add(pair);
		}
		con.close();
		return friendlist;

	}

	public static List<Pair<Group, String>> getGroup(int username) throws Exception {
		List<Pair<Group, String>> grouplist = new ArrayList<>();

		Connection con = database.getConnection();

		PreparedStatement checkLogin = con.prepareStatement(
				"SELECT msgmap.fromuser,msgmap.notif,group_id_holder.groupname FROM msgmap INNER JOIN group_id_holder on msgmap.fromuser=group_id_holder.group_id WHERE msgmap.touser= "
						+ username + " and msgmap.group_verify=true");
		ResultSet result = checkLogin.executeQuery();
		while (result.next()) {
			Group user = new Group(Integer.parseInt(result.getString("fromuser")), result.getString("groupname"));
			Pair<Group, String> pair = new Pair<>(user, result.getString("notif"));
			grouplist.add(pair);
		}
		con.close();
		return grouplist;
	}

	public static List<Pair<Users,String>> friendsNotInGroup(int fromid, int groupid) throws Exception {
		List<Pair<Users, String>> friendlist = new ArrayList<>();

		Connection con = getConnection();
		PreparedStatement friendList = con.prepareStatement(
				"SELECT msgmap.fromuser,userdetails.username FROM msgmap INNER JOIN userdetails on msgmap.fromuser=userdetails.user_id WHERE msgmap.touser="
						+ fromid + " and msgmap.fromuser not in (select touser from msgmap where fromuser=" + groupid
						+ ")");
		ResultSet adduserstogroup = friendList.executeQuery();
		while (adduserstogroup.next()) {
			Users user = new Users(Integer.parseInt(adduserstogroup.getString("fromuser")), adduserstogroup.getString("username"));
			Pair<Users, String> pair = new Pair<>(user,"0");
			friendlist.add(pair);
			

		}
		return friendlist;
	}

	public static Map<Integer, String> getAllUsers(String users, int from) throws Exception {
		Connection con = getConnection();
		Map<Integer, String> availableusers = new HashMap<Integer, String>();
		con = database.getConnection();
		PreparedStatement showuser = con.prepareStatement(
				"SELECT username,user_id FROM userdetails WHERE username LIKE'" + users + "%' AND user_id <> " + from);
		ResultSet r = showuser.executeQuery();
		while (r.next()) {
			availableusers.put(Integer.parseInt(r.getString("user_id")), r.getString("username"));
		}
		con.close();
		return availableusers;

	}

	public static Map<Integer, String> getFriendRequest(int from) throws Exception {
		Connection con = getConnection();
		Map<Integer, String> friendrequest = new HashMap<Integer, String>();
		PreparedStatement friendreqget = con.prepareStatement(
				"SELECT friendrequest.fromuser,userdetails.username FROM friendrequest INNER JOIN userdetails ON friendrequest.fromuser=userdetails.user_id WHERE touser="
						+ from);
		ResultSet friendreqresult = friendreqget.executeQuery();

		while (friendreqresult.next()) {
			friendrequest.put(Integer.parseInt(friendreqresult.getString("fromuser")),
					friendreqresult.getString("username"));

		}
		con.close();
		return friendrequest;
	}

	public static String getNotif(int user) throws Exception {
		Connection con = getConnection();
		PreparedStatement notifget = con.prepareStatement("SELECT notif FROM msgmap WHERE pair_id=" + user);
		ResultSet notifresult = notifget.executeQuery();
		String temporary = "0";
		while (notifresult.next()) {
			temporary = notifresult.getString("notif");

		}
		con.close();
		return temporary;
	}

	public static void updateNotif(int count, int user) throws Exception {
		Connection con = getConnection();
		PreparedStatement notif = con
				.prepareStatement("UPDATE msgmap SET notif=" + (++count) + " WHERE pair_id=" + user);
		notif.executeUpdate();
		con.close();
	}

	public static String getId(int from, int to) throws Exception {
		Connection con = getConnection();
		PreparedStatement getmap = con.prepareStatement(
				"SELECT fromuser,touser,pair_id FROM msgmap WHERE fromuser=" + from + " AND touser=" + to);
		ResultSet mapresult = getmap.executeQuery();
		String num = "";
		while (mapresult.next()) {

			num = mapresult.getString("pair_id");

		}
		con.close();
		return num;
	}

	public static void insertMessages(int pair_id, String message) throws Exception {
		Connection con = getConnection();
		PreparedStatement storemessages = con
				.prepareStatement("INSERT INTO messages(pair_id,msg) VALUES(" + pair_id + ",'" + message + "')");
		storemessages.executeUpdate();
		con.close();
	}

	public static void removeFriend(int from, int to) throws Exception {
		Connection con = getConnection();
		PreparedStatement sendarequest = con.prepareStatement("DELETE FROM msgmap WHERE (fromuser=" + from
				+ " OR fromuser=" + to + ")AND(touser=" + from + " OR touser=" + to + ")");
		sendarequest.executeUpdate();
		con.close();

	}

	public static void sendFriendRequest(int from, int to) throws Exception {
		Connection con = getConnection();
		PreparedStatement sendrequest = con
				.prepareStatement("INSERT INTO friendrequest VALUES(" + from + "," + to + ")");
		sendrequest.executeUpdate();
		con.close();
	}

	public static Map<String, String> getUserDetails(int user) throws Exception {
		Connection con = getConnection();
		Map<String, String> userdetails = new HashMap<String, String>();

		PreparedStatement notifget = con.prepareStatement("SELECT * FROM userdetails WHERE username=" + user);
		ResultSet displayresult = notifget.executeQuery();

		while (displayresult.next()) {
			userdetails.put("username", displayresult.getString("username"));
			userdetails.put("name", displayresult.getString("name"));
			userdetails.put("mobileno", displayresult.getString("mobileno"));
			userdetails.put("emailid", displayresult.getString("emailid"));
			userdetails.put("address", displayresult.getString("address"));

		}
		con.close();
		return userdetails;
	}

	public static String friendsCheck(int from, int user) throws Exception {
		Connection con = getConnection();
		PreparedStatement friendcheck = con
				.prepareStatement("SELECT fromuser,touser FROM msgmap WHERE touser=" + from + " AND fromuser=" + user);
		ResultSet friendchecka = friendcheck.executeQuery();
		String bool = "add friend";
		while (friendchecka.next()) {
			bool = "remove friend";

		}
		PreparedStatement friendrequestcheck = con
				.prepareStatement("SELECT fromuser FROM friendrequest WHERE (fromuser=" + from + " and touser=" + user
						+ ") OR (fromuser=" + user + " and touser=" + from + ")");
		ResultSet friendrequest = friendrequestcheck.executeQuery();
		while (friendrequest.next()) {

			if ((friendrequest.getString("fromuser")).equals(user + "")) {
				bool = "accept friend";

			} else {
				bool = "cancel request";

			}

		}
		con.close();
		return bool;

	}

	public static List<Message> getGroupMessagesAndTime(int groupid, int fromid, String no) throws Exception {
		List<Message> messages = new ArrayList<>();
		Connection con = getConnection();
		PreparedStatement checkLogin;
		if (no == null) {
			checkLogin = con.prepareStatement(
					"SELECT userdetails.username,messages.msg,msgmap.touser,cast(messages.timeat as time) from messages inner join msgmap on messages.pair_id=msgmap.pair_id inner join userdetails on userdetails.user_id=msgmap.touser where msgmap.fromuser="
							+ groupid + " ORDER BY timeat DESC LIMIT 20");
		} else {
			checkLogin = con.prepareStatement(
					"SELECT userdetails.username,messages.msg,msgmap.touser,cast(messages.timeat as time) from messages inner join msgmap on messages.pair_id=msgmap.pair_id inner join userdetails on userdetails.user_id=msgmap.touser where msgmap.fromuser="
							+ groupid + " ORDER BY timeat DESC LIMIT 20 OFFSET " + no);

		}
		ResultSet result = checkLogin.executeQuery();
		while (result.next()) {
			if (result.getString("touser").equals(fromid + "")) {

				messages.add(new Message(result.getString("username"), result.getString("msg"),
						result.getString("cast(messages.timeat as time)"), "0"));
			} else {

				messages.add(new Message(result.getString("username"), result.getString("msg"),
						result.getString("cast(messages.timeat as time)"), "1"));

			}
		}
		con.close();
		return messages;

	}

	public static List<Message> getMessagesAndTime(int fromid, int toid, String no) throws Exception {
		List<Message> messages = new ArrayList<>();
		Connection con = getConnection();
		PreparedStatement checkLogin;
		if (no == null) {
			checkLogin = con.prepareStatement("SELECT msg,pair_id,cast(timeat as time) FROM messages WHERE pair_id="
					+ fromid + " OR pair_id=" + toid + " ORDER BY timeat DESC LIMIT 20");

		} else {
			checkLogin = con.prepareStatement("SELECT msg,pair_id,cast(timeat as time) FROM messages WHERE pair_id="
					+ fromid + " OR pair_id=" + toid + " ORDER BY timeat DESC LIMIT 20 OFFSET " + no);
		}
		ResultSet result = checkLogin.executeQuery();
		while (result.next()) {
			if (result.getString("pair_id").equals(fromid + "")) {

				messages.add(new Message("0", result.getString("msg"), result.getString("cast(timeat as time)")));
			} else {

				messages.add(new Message("1", result.getString("msg"), result.getString("cast(timeat as time)")));

			}
		}
		con.close();
		return messages;

	}

	public static boolean checkGroup(int groupid) throws Exception {
		Connection con = getConnection();
		PreparedStatement checkGroup = con.prepareStatement("select fromuser from msgmap where touser=" + groupid);
		ResultSet checkgroup = checkGroup.executeQuery();
		boolean bool = true;
		while (checkgroup.next()) {
			bool = false;

		}
		con.close();
		return bool;

	}


	public static List<GroupUser> getGroupUserDetails(int groupid, int fromid) throws Exception {
		Connection con = getConnection();
		List<GroupUser> groupuserdetails = new ArrayList<>();
		PreparedStatement userdetails = con.prepareStatement(
				"select msgmap.touser,userdetails.username,msgmap.admin_access from msgmap inner join userdetails on userdetails.user_id=msgmap.touser where msgmap.fromuser="
						+ groupid );
		ResultSet getUsers = userdetails.executeQuery();
		while (getUsers.next()) {
			
			Users user=new Users(Integer.parseInt(getUsers.getString("touser")),getUsers.getString("username"));
			GroupUser groupUsers=new GroupUser(getUsers.getString("admin_access"),user);
			groupuserdetails.add(groupUsers);
		}
		con.close();
		return groupuserdetails;
	}

	public static boolean checkAdminAccess(int fromid, int groupid) throws Exception {
		Connection con = getConnection();
		PreparedStatement userdetails = con.prepareStatement(
				"select admin_access from msgmap where fromuser = " + groupid + " and touser = " + fromid);
		ResultSet getAdminAccess = userdetails.executeQuery();
		boolean bool = false;
		while (getAdminAccess.next()) {
			if ((getAdminAccess.getString("admin_access")).equals("1")) {
				bool = true;
			}
		}
		con.close();
		return bool;
	}

	public static void removefromgroup(int fromid, int groupid) throws Exception {
		Connection con = getConnection();
		PreparedStatement removefromgroup = con
				.prepareStatement("delete from msgmap where fromuser=" + groupid + " and touser=" + fromid);
		removefromgroup.executeUpdate();
		con.close();
	}

	public static void changeAdminAccess(int fromid, int groupid, boolean bool) throws Exception {
		Connection con = getConnection();
		PreparedStatement changeadminaccess = con.prepareStatement(
				"update msgmap set admin_access=" + bool + " where fromuser=" + groupid + " and touser=" + fromid);
		changeadminaccess.executeUpdate();
		con.close();
	}

	public static void deletegroup(int groupid) throws Exception {
		Connection con = getConnection();
		PreparedStatement removegroup = con.prepareStatement("delete from msgmap where fromuser=" + groupid);
		PreparedStatement removegroupdetails = con
				.prepareStatement("delete from group_id_holder where group_id=" + groupid);
		removegroup.executeUpdate();
		removegroupdetails.executeUpdate();
		con.close();
	}

}
