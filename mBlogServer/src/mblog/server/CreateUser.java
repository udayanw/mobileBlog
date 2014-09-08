package mblog.server;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CreateUser extends HttpServlet {

	private String username;
	private String password;
	private String re_password;
	private String first_name;
	private String last_name;
	private int year;
	private String user_type;

	/**
	 * Constructor of the object.
	 */
	public CreateUser() {
		super();
	}

	/**
	 * The doGet method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out
				.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<HTML>");
		out.println("<HEAD><TITLE>A Servlet</TITLE></HEAD>");
		out.println("<BODY><H3>");
		out.print("Use the POST method !");
		out.println("</H3></BODY>");
		out.println("</HTML>");
		out.flush();
		out.close();
	}

	/**
	 * The doPost method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to
	 * post.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String responseStr = "Problem creating user! Please retry...";
		boolean proceedFlag = true;
		int retVal = 0;

		username = (String) request.getParameter("username_field");
		password = (String) request.getParameter("password_field");
		re_password = (String) request.getParameter("re_password_field");
		first_name = (String) request.getParameter("firstname_field");
		last_name = (String) request.getParameter("lastname_field");
		String year_str = (String) request.getParameter("year_field");
		user_type = (String) request.getParameter("user_type_list");

		System.out.println("::" + username + ":" + password + ":" + re_password
				+ ":" + first_name + ":" + last_name + ":" + year_str + ":"
				+ user_type);
		if (username == null || (username.compareTo("") == 0)
				|| password == null || (password.compareTo("") == 0)
				|| re_password == null || (re_password.compareTo("") == 0)
				|| first_name == null || (first_name.compareTo("") == 0)
				|| last_name == null || (last_name.compareTo("") == 0)
				|| user_type == null || year_str == null
				|| (year_str.compareTo("") == 0)) {
			proceedFlag = false;
			responseStr = "All fields are mandatory!!!";

		} else {
			year = Integer.parseInt(year_str);
		}
		if (proceedFlag && password.compareTo(re_password) != 0) {
			proceedFlag = false;
			responseStr = "Both passwords do not match! Please retry...";
		}

		if (proceedFlag) {
			DatabaseOperations dOperations = new DatabaseOperations();
			boolean userPresent = dOperations.isUserPresent(username);
			if (!userPresent) {
				retVal = dOperations.addUser(username, password, first_name,
						last_name, year, user_type);
			}
		}

		if (retVal == 1) {
			responseStr = "User created succefully!";
		}
		
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out
				.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<HTML>");
		out.println("<HEAD><TITLE>A Servlet</TITLE></HEAD>");
		out.println("<BODY><H3>");
		out.print(responseStr);
		out.println("</H3></BODY>");
		out.println("</HTML>");
		out.flush();
		out.close();
	}
}
