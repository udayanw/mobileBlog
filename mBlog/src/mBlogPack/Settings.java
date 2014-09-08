package mBlogPack;

public class Settings
{
    public static final int VIDEO_LOCATOR = 1;
    public static final int IMAGE_LOCATOR = 2;

    private String userName;
    private String password;
    private String serverAddress;
    private int cameraLocator;

    public static int getIMAGE_LOCATOR()
    {
	return IMAGE_LOCATOR;
    }

    public static int getVIDEO_LOCATOR()
    {
	return VIDEO_LOCATOR;
    }

    public int getCameraLocator()
    {
	return cameraLocator;
    }

    public String getPassword()
    {
	return password;
    }

    public String getServerAddress()
    {
	return serverAddress;
    }

    public String getUserName()
    {
	return userName;
    }

    public void setCameraLocator(int cameraLocator)
    {
	this.cameraLocator = cameraLocator;
    }

    public void setPassword(String password)
    {
	this.password = password;
    }

    public void setServerAddress(String serverAddress)
    {
	this.serverAddress = serverAddress;
    }

    public void setUserName(String userName)
    {
	this.userName = userName;
    }

}
