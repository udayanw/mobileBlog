package mblog.server;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DatabaseOperations
{
    public int isValidUser(String userName, String pwd)
    {
        int userID = -1;
        ResultSet rSet;
        DatabaseConnector dConnector = new DatabaseConnector();
        if (dConnector.isDbConnected())
        {
            String query = "SELECT UserId FROM usermaster where Username='" + userName + "' AND Password='" + pwd + "'";
            try
            {
                rSet = dConnector.execQuerry(query);
                if (rSet != null)
                {
                    while (rSet.next())
                    {
                        userID = rSet.getInt("UserId");
                        System.out.println("userID :: " + userID);
                    }
                }
                rSet.close();
                dConnector.close();
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
        return userID;
    }

    public boolean insertBlog(int userID, BlogData blogData)
    {
        boolean retFlag = false;
        int blogID = -1;
        ResultSet rSet;
        System.out.println("Image data adding to DB:"+blogData.getImageDataString());
        DatabaseConnector dConnector = new DatabaseConnector();
        if (dConnector.isDbConnected())
        {
            String insertQuery = "INSERT into blogmaster (BlogTitle,BlogText,ImageCaption,ImageData,ImageTime,UserID) values ('"
                    + blogData.getBlogTitle()
                    + "','"
                    + blogData.getBlogText()
                    + "','"
                    + blogData.getImageCaption()
                    + "','" + blogData.getImageDataString() + "','" + blogData.getImageTime() + "'," + userID + ")";
            try
            {
                int check = dConnector.updateQuery(insertQuery);
                if (check > 0)
                {
                    String selectQuery = "SELECT max(BlogID) FROM blogmaster";
                    rSet = dConnector.execQuerry(selectQuery);
                    if (rSet != null)
                    {
                        while (rSet.next())
                        {
                            blogID = rSet.getInt("max(BlogID)");
                            System.out.println("BlogID :: " + blogID + " UserID :: " + userID);
                        }
                        String insertInBlogDataQuery = "INSERT into userblog (UserID,BlogID) values (" + userID + ","
                                + blogID + ")";
                        int chk = dConnector.updateQuery(insertInBlogDataQuery);
                        if (chk > 0)
                        {
                            retFlag = true;
                        }
                    }
                }
            }
            catch (SQLException e)
            {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }

        }
        return retFlag;
    }

    public boolean isUserPresent(String username)
    {
        boolean userPresent = false;

        ResultSet rSet;
        DatabaseConnector dConnector = new DatabaseConnector();
        if (dConnector.isDbConnected())
        {
            String queryString = "SELECT * FROM usermaster WHERE Username='" + username + "'";
            try
            {
                rSet = dConnector.execQuerry(queryString);

                if (rSet != null)
                {
                    int count = 0;
                    while (rSet.next())
                    {
                        count++;
                    }
                    if (count > 0)
                    {
                        userPresent = true;
                    }
                }
                rSet.close();

            }
            catch (SQLException e)
            {
                System.out.println("ERROR! checking user..");
                userPresent = true;
            }
            dConnector.close();
        }
        return userPresent;
    }

    public int addUser(String username, String pw, String firstName, String laseName, int year, String userType)
    {
        int retVal = -1;

        DatabaseConnector dConn = new DatabaseConnector();
        if (dConn.isDbConnected())
        {
            String insertQuery = "INSERT INTO usermaster (Username,Password,FirstName,LastName,Year,UserType)"
                    + " VALUES ('" + username + "'," + "'" + pw + "'," + "'" + firstName + "'," + "'" + laseName + "',"
                    + +year + "," + "'" + userType + "')";
            try
            {
                int check = dConn.updateQuery(insertQuery);
                retVal = check;
            }
            catch (SQLException e)
            {
                System.out.println("Error adding user to database...");
            }
            dConn.close();
        }
        return retVal;
    }

    public String getBlogTitlesString()
    {
        DatabaseConnector dConn = new DatabaseConnector();
        ResultSet rSet;
        String retString = "";
        String blogID = "";
        if (dConn.isDbConnected())
        {
            String queryString = "SELECT BlogTitle,BlogID FROM blogmaster";
            try
            {
                rSet = dConn.execQuerry(queryString);
                if (rSet != null)
                {
                    while (rSet.next())
                    {
                        blogID = "" + rSet.getInt("BlogID");
                        retString += blogID;
                        retString += ":";
                        retString += rSet.getString("BlogTitle");
                        retString += "~~~";
                    }
                    rSet.close();
                }

            }
            catch (SQLException e)
            {
                System.out.println("ERROR: " + e);
            }
            dConn.close();
        }

        return retString;
    }
    
    public String[] getBlogTitlesStringArray() throws NullPointerException
    {
        DatabaseConnector dConn = new DatabaseConnector();
        ResultSet rSet;
        String[] retStringArray = null;
        ArrayList<String> arrayList = null;
        String blogID = "";
        if (dConn.isDbConnected())
        {
            String queryString = "SELECT BlogTitle,BlogID FROM blogmaster";
            try
            {
                rSet = dConn.execQuerry(queryString);
                if (rSet != null)
                {
                	arrayList = new ArrayList<String>();
                    while (rSet.next())
                    {
                        blogID = "" + rSet.getInt("BlogID");
                        arrayList.add((blogID)+": "+rSet.getString("BlogTitle"));
                    }
                    rSet.close();
                }

            }
            catch (SQLException e)
            {
                System.out.println("ERROR: " + e);
            }
            finally 
            {
            	dConn.close();
            	if (arrayList != null) 
            	{
            		int size = arrayList.size();
            		retStringArray = new String[size];
                	for (int i = 0; i < size; i++)
                	{
                		retStringArray[i] = arrayList.get(i);
                	}
            	}
            }
        }
        return retStringArray;
    }

    /**
     * Returns the blog data corresponding the blogID provided. If no data
     * present or invalid blogID, returns empty string.
     * 
     * @param blogID
     * @return String containing complete blog data. Or returns empty string if
     *         blogID is invalid
     */
    public String getBlogDataToSend(int blogID)
    {
        String retStr = "";
        DatabaseConnector dConn = new DatabaseConnector();
        ResultSet rSet;
        if (dConn.isDbConnected())
        {
            String queryString = "SELECT * FROM blogmaster WHERE BlogID=" + blogID;
            try
            {
                rSet = dConn.execQuerry(queryString);
                if (rSet != null)
                {
                    BlogData blog = new BlogData();
                    while (rSet.next())
                    {
                        blog.setBlogTitle(rSet.getString("BlogTitle"));
                        blog.setBlogText(rSet.getString("BlogText"));
                        blog.setImageCaption(rSet.getString("ImageCaption"));
                        blog.setImageTime(rSet.getString("ImageTime"));
                        blog.setImageDataString(rSet.getString("ImageData"));
                    }
                    rSet.close();
                    System.out.println("Sending image data to mobile:" + blog.getImageDataString());
                    retStr = formatSendingBlogDataString(blog);
                    // Check for no data
                    if (retStr.equals("#~#~##~#~##~#~##~#~#null"))
                    {
                        retStr = "";
                    }
                }

            }
            catch (Exception e)
            {
                System.out.println("Error getting blog data with ID:" + blogID);
            }
            dConn.close();
        }

        return retStr;
    }

    private String formatSendingBlogDataString(BlogData blog)
    {
        StringBuffer ret = new StringBuffer("");
        if (blog != null)
        {
            ret.append(blog.getBlogTitle());
            ret.append("#~#~#");
            ret.append(blog.getBlogText());
            ret.append("#~#~#");
            ret.append(blog.getImageCaption());
            ret.append("#~#~#");
            ret.append(blog.getImageTime());
            ret.append("#~#~#");
            ret.append(blog.getImageDataString());

        }
        return ret.toString();
    }
    
    
    /**
     * Returns the blog data corresponding the blogID provided. If no data
     * present or invalid blogID, returns null.
     * 
     * @param blogID
     * @return BlogData BlogData object or null if no data found.
     */
    public BlogData fetchBlogDataFromDatabase(int blogID)
    {
        DatabaseConnector dConn = new DatabaseConnector();
        ResultSet rSet;
        int resultCount = 0;
        BlogData blog = null;
        if (dConn.isDbConnected())
        {
            String queryString = "SELECT * FROM blogmaster WHERE BlogID=" + blogID;
            try
            {
                rSet = dConn.execQuerry(queryString);
                if (rSet != null)
                {
                    blog = new BlogData();
                    while (rSet.next())
                    {
                    	resultCount++;
                        blog.setBlogTitle(rSet.getString("BlogTitle"));
                        blog.setBlogText(rSet.getString("BlogText"));
                        blog.setImageCaption(rSet.getString("ImageCaption"));
                        blog.setImageTime(rSet.getString("ImageTime"));
                        blog.setImageDataString(rSet.getString("ImageData"));
                    }
                    rSet.close();
                }
            }
            catch (Exception e)
            {
                System.out.println("Error fetching blog data with ID:" + blogID);
            }
            dConn.close();
        }

        return (resultCount == 0) ? null : blog;
    }
    
    
    
    /**
     * Used for unit testing
     * @param args
     */
    public static void main (String[] args) 
    {
    	String [] titleArray = null;
    	DatabaseOperations dOps = new DatabaseOperations();
    	try 
    	{
    		titleArray = dOps.getBlogTitlesStringArray();
    	}
    	catch (Exception e) 
    	{
    		System.out.println("Error getting titles array");
		}
    	if (titleArray != null) 
    	{
    		for(int i=0; i<titleArray.length; i++)
    		{
    			System.out.println("Blog-"+titleArray[i]);
    		}
    	}
    }

}
