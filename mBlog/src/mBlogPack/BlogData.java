package mBlogPack;

import javax.microedition.lcdui.Image;


public class BlogData
{
    public String blogTitle = "";

    public String blogText = "";

    public String ImageCaption = "";

    public Image image;

    public String imageTime = "";

    public byte[] imageByteArray;

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
     * @return the image
     */
    public Image getImage()
    {
	return image;
    }

    /**
     * @param image
     *            the image to set
     */
    public void setImage(Image image)
    {
	this.image = image;
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

    /**
     * @return the imageByteArray
     */
    public byte[] getImageByteArray()
    {
	return imageByteArray;
    }

    /**
     * @param imageByteArray
     *            the imageByteArray to set
     */
    public void setImageByteArray(byte[] imageByteArray)
    {
	this.imageByteArray = imageByteArray;
    }

}
