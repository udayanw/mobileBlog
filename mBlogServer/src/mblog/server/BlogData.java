package mblog.server;

public class BlogData
{
    private String username = "";

    private String password = "";

    private String blogTitle = "";

    private String blogText = "";

    private String ImageCaption = "";

    private String imageTime = "";

    private String imageDataString;

    /**
     * @return the imageDataString
     */
    public String getImageDataString()
    {
        return imageDataString;
    }

    /**
     * @param imageDataString
     *            the imageDataString to set
     */
    public void setImageDataString(String imageDataString)
    {
        this.imageDataString = imageDataString;
    }

    /**
     * @return the username
     */
    public String getUsername()
    {
        return username;
    }

    /**
     * @param username
     *            the username to set
     */
    public void setUsername(String username)
    {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword()
    {
        return password;
    }

    /**
     * @param password
     *            the password to set
     */
    public void setPassword(String password)
    {
        this.password = password;
    }

    /**
     * @return the blogTitle
     */
    public String getBlogTitle()
    {
        return blogTitle;
    }

    /**
     * @param blogTitle
     *            the blogTitle to set
     */
    public void setBlogTitle(String blogTitle)
    {
        this.blogTitle = blogTitle;
    }

    /**
     * @return the blogText
     */
    public String getBlogText()
    {
        return blogText;
    }

    /**
     * @param blogText
     *            the blogText to set
     */
    public void setBlogText(String blogText)
    {
        this.blogText = blogText;
    }

    /**
     * @return the imageCaption
     */
    public String getImageCaption()
    {
        return ImageCaption;
    }

    /**
     * @param imageCaption
     *            the imageCaption to set
     */
    public void setImageCaption(String imageCaption)
    {
        ImageCaption = imageCaption;
    }

    /**
     * @return the imageTime
     */
    public String getImageTime()
    {
        return imageTime;
    }

    /**
     * @param imageTime
     *            the imageTime to set
     */
    public void setImageTime(String imageTime)
    {
        this.imageTime = imageTime;
    }

}
