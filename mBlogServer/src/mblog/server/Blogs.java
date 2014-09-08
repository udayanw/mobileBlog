package mblog.server;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Blogs extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public Blogs() {
		super();
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		
		DatabaseOperations dOps = new DatabaseOperations();
		String[] blogList = null;
		try
		{
			blogList = dOps.getBlogTitlesStringArray();
		}
		catch (Exception e) 
		{
			System.out.println("Error fetching blogs list from database...");
		}
		
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<HTML>");
		out.println("<HEAD><TITLE>Blog List</TITLE></HEAD>");
		out.println("<BODY>");
		out.println("<DIV align='center'>");
		
		if (blogList != null )
		{
			out.print("<H2>Blogs List</H2>");
			out.print("<br><H4>");
			for (int i=0; i < blogList.length; i++)
			{
				out.println(blogList[i]);
				out.println("<br>");
			}
			out.print("</H4>");
			out.println("<br><br>");
			out.println("<form action='ShowBlog' method='POST'>");
			out.print("Enter Blog Number: <input type='text' Name='blog_number'");
			out.println("<input type='submit' name='Show_Blog' value='Show Blog'>");
			out.println("</form>");
		}
		else
		{
			out.print("<h3>Error fetching blogs list from server!</h3>");
		}
		out.println("</DIV></BODY>");
		out.println("</HTML>");
		out.flush();
		out.close();
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
