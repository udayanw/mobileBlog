package mblog.server;

public class DataOperations
{
    public BlogData seperateBlogData(String totalData)
    {

        String username = seperateString(totalData, "uname=", "&pw=");
        String password = seperateString(totalData, "&pw=", "&blog_title=");
        String blogTitle = seperateString(totalData, "&blog_title=", "&blog_text=");
        String blogText = seperateString(totalData, "&blog_text=", "&image_caption=");
        String imageCaption = seperateString(totalData, "&image_caption=", "&image_time=");
        String imageTime = seperateString(totalData, "&image_time=", "&image_data=");
        String imageData = totalData.substring(totalData.indexOf("&image_data=") + ("&image_data=").length());

        System.out.println("#" + username + ":" + password + ":" + blogTitle + ":" + blogText + ":" + imageCaption
                + ":" + imageTime + ":" + imageData);

        BlogData blogData = new BlogData();
        blogData.setUsername(username);
        blogData.setPassword(password);
        blogData.setBlogTitle(blogTitle);
        blogData.setBlogText(blogText);
        blogData.setImageCaption(imageCaption);
        blogData.setImageTime(imageTime);
        blogData.setImageDataString(imageData);

        return blogData;
    }

    public String seperateString(String totalData, String stringA, String stringB)
    {
        int indexA = -1;
        int indexB = -1;
        String tempString = null;

        indexA = totalData.indexOf(stringA);
        indexB = totalData.indexOf(stringB);
        if (indexA != -1 && indexB != -1)
        {
            tempString = totalData.substring(indexA + stringA.length(), indexB);
        }

        return tempString;
    }
    
}
