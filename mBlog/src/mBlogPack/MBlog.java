package mBlogPack;

import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;


public class MBlog extends MIDlet
{

    private MBlogApplicationManager mAppManager;

    public MBlog()
    {}

    protected void destroyApp(boolean arg0) throws MIDletStateChangeException
    {

    }

    protected void pauseApp()
    {

    }

    protected void startApp() throws MIDletStateChangeException
    {
	if (mAppManager == null)
	{
	    mAppManager = new MBlogApplicationManager(this);
	}
    }

}
