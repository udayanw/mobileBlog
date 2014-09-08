package mblog.server;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BlogList extends HttpServlet
{

    /**
     * Default Serial Version ID
     */
    private static final long serialVersionUID = 2L;

    /**
     * Constructor of the object.
     */
    public BlogList()
    {
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
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("Use POST method");
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
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        int blogID = -1;
        String blogIDStr = request.getParameter("blogid");
        System.out.println("Parameter blogid value recived: " + blogIDStr);

        DatabaseOperations dOps = new DatabaseOperations();
        if (blogIDStr == null)
        {
            // Send blog list
            String sendingStr = dOps.getBlogTitlesString();

            response.setContentType("text/plain");
            if (sendingStr != null) 
            {
            	response.setContentLength(sendingStr.getBytes().length);
            	System.out.println("Sending blog list. Content Length:"
            			+sendingStr.getBytes().length);
            }
            
            PrintWriter out = response.getWriter();
            
            if (sendingStr == null || sendingStr.compareTo("") == 0) 
            {
            	out.println("NULL");
            }
            else 
            {
            	out.println(sendingStr);
			}
            out.flush();
            out.close();

        }
        else
        {
            // Send individual blog data as per the blog id
            blogID = Integer.parseInt(blogIDStr);
            String sendingStr = dOps.getBlogDataToSend(blogID);
            response.setContentType("text/plain");
            if (sendingStr != null)
            {
            	response.setContentLength(sendingStr.getBytes().length);
            }
            PrintWriter out = response.getWriter();
            
            if (sendingStr == null || sendingStr.compareTo("") == 0)
            {
            	out.println("NULL");
            }
            else 
            {
                out.println(sendingStr);
			}
            
            out.flush();
            out.close();
            System.out.println("@Sending string to mobile:"+sendingStr);

        }

        System.out.println("BlogList.. POST method: BlogID recived= " + blogID);

    }

}
