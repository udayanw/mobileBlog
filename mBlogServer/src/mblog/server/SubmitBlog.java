package mblog.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SubmitBlog extends HttpServlet
{

    /**
     * Default Serial Version ID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor of the object.
     */
    public SubmitBlog()
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

        System.out.println("SubmitBlog.. in doGet");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("SubmitBlog - Use POST method");
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
        System.out.println("SubmitBlog.. in doPost");

        System.out.println("request.getContentLength(): " + request.getContentLength());
        System.out.println("request.getContentType(): " + request.getContentType());
        System.out.println("request.getMethod(): " + request.getMethod());
        System.out.println("request.getRemoteAddr(): " + request.getRemoteAddr());

        // BufferedReader reqReader = request.getReader();
        ServletInputStream inStream = request.getInputStream();
        BufferedReader buffRead = new BufferedReader(new InputStreamReader(inStream));
        StringBuffer inStrBuff = new StringBuffer();
        String line;
        while ((line = buffRead.readLine()) != null)
        {
            inStrBuff.append(line);
        }

        // Do the processing on data
        String totalRecivedString = inStrBuff.toString();
        int index = totalRecivedString.indexOf("&image_data=");
        String imageString = null;
        if (index != -1)
        {
            imageString = totalRecivedString.substring(index + "&image_data=".length());
        }

        System.out.println("Recived Data Stream:" + totalRecivedString);
        System.out.println("Image Data Stream:" + imageString);

        DataOperations dOps = new DataOperations();
        final BlogData recivedBlogData = dOps.seperateBlogData(totalRecivedString);

        boolean isBlogInserted = false;

        DatabaseOperations dbOperations = new DatabaseOperations();

        int userID = dbOperations.isValidUser(recivedBlogData.getUsername(), recivedBlogData.getPassword());
        if (userID != -1)
        {
            isBlogInserted = dbOperations.insertBlog(userID, recivedBlogData);
            System.out.println(isBlogInserted);
        }

        // Send response back to mobile
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        String status = isBlogInserted ? "Blog Submitted!" : "Blog Upload Failed!";
        out.println(status);
        out.flush();
        out.close();

    }

}
