package mblog.server;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ShowBlog extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public ShowBlog() {
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

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<HTML>");
		out.println("<HEAD><TITLE>Show Blog</TITLE></HEAD>");
		out.println("<BODY>");
		out.print("Use POST method...");
		out.println("</BODY>");
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

		String blogNumberString = request.getParameter("blog_number");
		boolean flag = false;
		int blogId = -1;
		try
		{
			blogId = Integer.parseInt(blogNumberString);
		}
		catch(NumberFormatException ne)
		{
			System.out.println("Invalid blog number entered... "+ne);
			flag = true;
		}
		
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<HTML>");
		out.println("<HEAD><TITLE>Show Blog</TITLE></HEAD>");
		out.println("<BODY>");
		if (flag || blogNumberString == null || blogNumberString.equalsIgnoreCase(""))
		{
			// Perform validations on input data
			out.println("<h3>Enter valid blog number!</h3>");
		}
		else
		{
			if (blogId == -1) 
			{
				out.print("<h3>Enter valid blog number!</h3>");
			}
			else
			{
				DatabaseOperations dOps = new DatabaseOperations();
				BlogData blog = dOps.fetchBlogDataFromDatabase(blogId);
				
				if (blog != null)
				{
					out.println("<h3>Blog Title: "+blog.getBlogTitle()+"</h3>");
					out.println("<br>");
					out.println("<h3>Blog Text:</h3>"+blog.getBlogText());
					out.println("<br><br><br>");
					try
					{
						//TODO:	Try forming image
						out.println("Image:<br>(Image can't be formed)");
						out.println("<br>");
					}
					catch (Exception e)
					{
						out.println("Image:<br>(Error in forming image...)");
					}
					out.println("<br>");
					out.println("Image Time:"+blog.getImageTime());
					out.println("<br>");
					out.println("<h4>Image Caption: "+blog.getImageCaption()+"</h4>");
					out.println("<br><hr>");
				
				}
				else
				{
					out.print("<h3>Blog with Blog ID:"+blogId+" not found!</h3>");
				}
			}
		}
		
		out.println("  </BODY>");
		out.println("</HTML>");
		out.flush();
		out.close();
	}

}
