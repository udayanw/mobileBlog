package mBlogPack;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.ImageItem;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.TextBox;
import javax.microedition.lcdui.TextField;
import javax.microedition.media.Manager;
import javax.microedition.media.Player;
import javax.microedition.media.control.VideoControl;
import javax.microedition.rms.InvalidRecordIDException;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotFoundException;
import javax.microedition.rms.RecordStoreNotOpenException;

public class MBlogApplicationManager extends Canvas implements CommandListener
{

    private MBlog midlet;

    private BlogData blogData;

    private Display display;

    private int screenID;

    private Image splashImage;

    private Timer splashTimer;

    private TextBox blogTitleTextBox;

    private TextBox blogTextBox;

    private TextBox blogImageCaptionTextBox;

    private TextField usernameField;

    private TextField passwordField;

    private TextField serverURLField;

    private ChoiceGroup snapperChoiceGroup;

    private ChoiceGroup blogListChoiceGroup;

    private String[] snapperArray = { "Snapper 1", "Snapper 2" };

    private String[] blogListArray = null;

    private int[] blogIDArray = null;

    private String username = "";

    private String password = "";

    private String serverURL = "";

    private int snapperChoice;

    private Form settingsForm;

    private Form captureForm;

    private Form blogPreviewForm;

    private Form showBlogListForm;

    private Form viewSelectedBlogForm;

    private Form showCapturedImageForm;

    private Form helpForm;

    private Item vidItem;

    private VideoControl vidControl;

    private ImageItem capturedImage = null;

    private Image finalImage;

    private byte[] imageByteArray = null;

    private Player player = null;

    private int mainMenuSelectedItemIndex = 0;

    private int createBlogMenuSelectedItemIndex = 0;

    private int selectedBlogId = -1;

    private Command mainMenuSelect;

    private Command createBlogMenuSelect;

    private Command backToMainMenu;

    private Command backToCreateBlogMenu;

    private Command saveBlogTitle;

    private Command saveBlogText;

    private Command saveImageCaption;

    private Command captureImage;

    private Command saveSettings;

    private Command resetSettings;

    private Command uploadBlog;

    private Command saveImage;

    private Command discardImage;

    private Command selectBlogListItem;

    private Command backToBlogList;

    private Alert serverConnectionResponseAlert;
    
    private Canvas canvas = this;

    /**
     * Parameterized constructor
     * 
     * @param midlet
     */
    public MBlogApplicationManager(MBlog midlet)
    {

        this.midlet = midlet;
        setCommandListener(this);
        display = Display.getDisplay(midlet);
        initSettings();
        setScreenId(MBlogConstants.SCREEN_SPLASH);
        display.setCurrent(this);
    }

    /**
     * This method is called to set a screen or change to different screen.
     * Parameter required is screenID, provided in MBlogConstants class fields.
     * 
     * @param screenID
     */
    private void setScreenId(int screen)
    {
        screenID = screen;
        removeAllCommands();
        switch (screen)
        {
        case MBlogConstants.SCREEN_SPLASH:
            initSplashScreen();
            break;
        case MBlogConstants.SCREEN_MAIN_MENU:
            initMainMenu();
            break;
        case MBlogConstants.SCREEN_CREATE_BLOG_MAIN:
            initCreateBlogMain();
            break;
        case MBlogConstants.SCREEN_BLOG_TITLE:
            initBlogTitle();
            break;
        case MBlogConstants.SCREEN_BLOG_TEXT:
            initBlogText();
            break;
        case MBlogConstants.SCREEN_BLOG_IMAGE_CAPTION:
            initBlogImageCaption();
            break;
        case MBlogConstants.SCREEN_BLOG_CAPTURE_IMAGE:
            initImageCapture();
            break;
        case MBlogConstants.SCREEN_SETTINGS:
            initSettings();
            break;
        case MBlogConstants.SCREEN_VIEW_LIST:
            initBlogViewList();
            break;
        case MBlogConstants.SCREEN_BLOG_PREVIEW:
            initBlogPreview();
            break;
        case MBlogConstants.SCREEN_HELP:
            initHelp();
            break;
        case MBlogConstants.SCREEN_VIEW_BLOG:
            initViewBlog();
            break;
        }
        repaint();
    }

    // Initialize Splash Screen
    private void initSplashScreen()
    {
        splashTimer = new Timer();
        splashTimer.schedule(new SplashTask(), MBlogConstants.SPLASH_TIMEOUT);
        try
        {
            splashImage = Image.createImage(MBlogConstants.IMAGE_SPLASH);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    // Initialize Main Menu Screen
    private void initMainMenu()
    {
        mainMenuSelect = new Command("Select", Command.OK, 1);
    }

    // Initialize Create Blog Main Menu Screen
    private void initCreateBlogMain()
    {
        createBlogMenuSelect = new Command("Select", Command.OK, 1);
        backToMainMenu = new Command("Back", Command.BACK, 2);
    }

    // Initialize Blog Text Screen
    private void initBlogText()
    {
        backToCreateBlogMenu = new Command("Back", Command.BACK, 2);
        saveBlogText = new Command("Save", Command.OK, 1);
        blogTextBox = new TextBox("Type Blog...", blogData.getBlogText(), 10000, TextField.ANY);
        blogTextBox.setCommandListener(this);
    }

    // Initialize Blog Title Screen
    private void initBlogTitle()
    {
        backToCreateBlogMenu = new Command("Back", Command.BACK, 2);
        saveBlogTitle = new Command("Save", Command.OK, 1);
        blogTitleTextBox = new TextBox("Enter Blog Title...", blogData.getBlogTitle(), 200, TextField.ANY);
        blogTitleTextBox.setCommandListener(this);
    }

    // Initialize Image Caption Screen
    private void initBlogImageCaption()
    {
        backToCreateBlogMenu = new Command("Back", Command.BACK, 2);
        saveImageCaption = new Command("Save", Command.OK, 1);
        blogImageCaptionTextBox = new TextBox("Add Image Caption...", blogData.getImageCaption(), 400, TextField.ANY);
        blogImageCaptionTextBox.setCommandListener(this);
    }

    // Initialize Image Capture Screen
    private void initImageCapture()
    {
        captureForm = new Form("Capture Image");
        backToCreateBlogMenu = new Command("Back", Command.BACK, 2);
        captureImage = new Command("Capture", Command.OK, 1);
        captureForm.setCommandListener(this);
        Thread th = new Thread()
        {
            public void run()
            {
                viewCamera(); // invokes method to start camera
            }
        };
        th.start();
    }

    // Initialize Settings Screen
    private void initSettings()
    {
        settingsForm = new Form("Settings");
        backToMainMenu = new Command("Back", Command.BACK, 3);
        saveSettings = new Command("Save", Command.OK, 1);
        resetSettings = new Command("Logout & Reset", Command.SCREEN, 2);
        RMSHandler rmsHandler = RMSHandler.getInstance();
        Settings appSettings = null;
        try
        {
            appSettings = rmsHandler.getSettings();
        }
        catch (RecordStoreNotOpenException e)
        {
            e.printStackTrace();
        }
        catch (InvalidRecordIDException e)
        {
            e.printStackTrace();
        }
        catch (RecordStoreException e)
        {
            e.printStackTrace();
        }

        snapperChoice = Settings.IMAGE_LOCATOR;
        if (appSettings != null)
        {
            username = appSettings.getUserName();
            password = appSettings.getPassword();
            serverURL = appSettings.getServerAddress();
            snapperChoice = appSettings.getCameraLocator();
        }
        usernameField = new TextField("Username", username, 20, TextField.ANY );
        passwordField = new TextField("Password", password, 15, TextField.ANY | TextField.PASSWORD);
        serverURLField = new TextField("Server URL", serverURL, 60, TextField.URL);
        snapperChoiceGroup = new ChoiceGroup("Advanced: Snapper Option", ChoiceGroup.EXCLUSIVE, snapperArray, null);
        snapperChoiceGroup.setSelectedIndex(snapperChoice - 1, true);
        settingsForm.setCommandListener(this);

    }

    // Initialize Blog View List Screen.
    private void initBlogViewList()
    {
        showBlogListForm = new Form("View Blogs");
        backToMainMenu = new Command("Back", Command.BACK, 2);
        selectBlogListItem = new Command("Show Blog", Command.ITEM, 1);
        showBlogListForm.setCommandListener(this);
    }

    // Initilize view blog screen
    private void initViewBlog()
    {
        viewSelectedBlogForm = new Form("Blog...");
        backToBlogList = new Command("Back", Command.BACK, 1);
        viewSelectedBlogForm.setCommandListener(this);
    }

    // Initialize Blog preview screen
    private void initBlogPreview()
    {
        blogPreviewForm = new Form("Blog Preview");
        uploadBlog = new Command("Upload", Command.OK, 1);
        backToCreateBlogMenu = new Command("Back", Command.BACK, 2);
        blogPreviewForm.setCommandListener(this);
    }

    // Initialize Help Screen
    private void initHelp()
    {
        helpForm = new Form("mBlog Application Help");
        backToMainMenu = new Command("Back", Command.OK, 1);
        helpForm.addCommand(backToMainMenu);
        helpForm.setCommandListener(this);
        StringItem temp;
        temp = new StringItem("About mBlog Application", "");
        temp.setText("mBlog Application helps user to write blogs," + ""
                + "add image to the blog and upload the blog to the server.\n");
        helpForm.append(temp);
        temp = new StringItem("Application Help", "");
        temp.setText("If Camera is not working," + "then go to Settings and change the snapper option.");
        helpForm.append(temp);
        temp = new StringItem("About mBlog", "");
        temp.setText("mBlob Application is developed as a final year BE project by:" + "\n1) Raj Trivedi "
                + "\n2) Atul Akare " + "\n3) Manmath Deshpande " + "\n4) Abhinay Potdar ");
        helpForm.append(temp);
        display.setCurrent(helpForm);
    }

    /**
     * SplashTask Inner class.
     */
    class SplashTask extends TimerTask
    {
        public void run()
        {
            setScreenId(MBlogConstants.SCREEN_MAIN_MENU);
        }
    }

    /**
     * paint() method -- Paints the screen as per the screenID
     */
    protected void paint(Graphics graphics)
    {
        switch (screenID)
        {

        case MBlogConstants.SCREEN_SPLASH:
            drawSplashScreen(graphics);
            break;
        case MBlogConstants.SCREEN_MAIN_MENU:
            drawMainMenu(graphics);
            break;
        case MBlogConstants.SCREEN_CREATE_BLOG_MAIN:
            drawCreateBlogMain(graphics);
            break;
        case MBlogConstants.SCREEN_BLOG_TITLE:
            drawBlogTitle(graphics);
            break;
        case MBlogConstants.SCREEN_BLOG_TEXT:
            drawBlogText(graphics);
            break;
        case MBlogConstants.SCREEN_BLOG_IMAGE_CAPTION:
            drawImageCaption(graphics);
            break;
        case MBlogConstants.SCREEN_BLOG_CAPTURE_IMAGE:
            drawCaptureImageForm(graphics);
            break;
        case MBlogConstants.SCREEN_SETTINGS:
            drawSettings(graphics);
            break;
        case MBlogConstants.SCREEN_VIEW_LIST:
            drawViewList(graphics);
            break;
        case MBlogConstants.SCREEN_BLOG_PREVIEW:
            drawBlogPreview(graphics);
            break;
        case MBlogConstants.SCREEN_VIEW_BLOG:
            drawViewBlog(graphics);
            break;

        }
    }

    // draw methods to add Commands, append components, and show selected
    // screen.

    private void drawSplashScreen(Graphics graphics)
    {
        graphics.drawImage(splashImage, getWidth() / 2, getHeight() / 2, Graphics.VCENTER | Graphics.HCENTER);
    }

    private void drawMainMenu(Graphics graphics)
    {
        this.addCommand(mainMenuSelect);
        int yPos = 0;
        clearScreen(graphics, 0xF0F8FF);
        graphics.setColor(MBlogConstants.COLOR_MAIN_MENU_TITLE);
        graphics.setFont(MBlogConstants.FONT_MAIN_MENU_TITLE);
        graphics.drawString("Main Menu", getWidth() / 2, MBlogConstants.TOP_MARGIN, Graphics.TOP | Graphics.HCENTER);
        drawMainMenuSelector(graphics);
        yPos = 2 * MBlogConstants.TOP_MARGIN + MBlogConstants.FONT_MAIN_MENU_TITLE.getHeight();
        graphics.setColor(MBlogConstants.COLOR_MAIN_MENU_ITEMS);
        for (int i = 0; i < MBlogConstants.MAIN_MENU_ITEMS.length; i++)
        {
            graphics.setFont(MBlogConstants.FONT_MAIN_MENU);
            graphics.drawString(MBlogConstants.MAIN_MENU_ITEMS[i], getWidth() / 2, yPos, Graphics.TOP
                    | Graphics.HCENTER);
            yPos += MBlogConstants.FONT_MAIN_MENU.getHeight() + MBlogConstants.MAIN_MENU_ITEM_SEPERATOR;
        }

    }

    private void drawMainMenuSelector(Graphics graphics)
    {

        int currentItemWidth = graphics.getFont()
                .stringWidth(MBlogConstants.MAIN_MENU_ITEMS[mainMenuSelectedItemIndex]);
        int xPos = (getWidth() - currentItemWidth) / 2;
        int yPos = 2 * MBlogConstants.TOP_MARGIN + MBlogConstants.FONT_MAIN_MENU_TITLE.getHeight()
                + mainMenuSelectedItemIndex * MBlogConstants.MAIN_MENU_ITEM_SEPERATOR + mainMenuSelectedItemIndex
                * MBlogConstants.FONT_MAIN_MENU.getHeight();
        graphics.setColor(MBlogConstants.COLOR_MENU_SELECTOR_BORDER);
        graphics.fillRoundRect(xPos - 16, yPos - 3, currentItemWidth + 35,
                MBlogConstants.FONT_MAIN_MENU.getHeight() + 13, 15, 15);
        graphics.setColor(MBlogConstants.COLOR_MAIN_MENU_SELECTOR);
        graphics.fillRoundRect(xPos - 15, yPos - 2, currentItemWidth + 30,
                MBlogConstants.FONT_MAIN_MENU.getHeight() + 8, 15, 15);

    }

    private void drawCreateBlogMain(Graphics graphics)
    {
        clearScreen(graphics, 0xF0F8FF);
        this.addCommand(createBlogMenuSelect);
        this.addCommand(backToMainMenu);
        int yPos = 0;
        graphics.setColor(MBlogConstants.COLOR_CREATE_BLOG_MENU_TITLE);
        graphics.setFont(MBlogConstants.FONT_CREATE_BLOG_MENU_TITLE);
        graphics.drawString("Create Blog Menu", getWidth() / 2, MBlogConstants.TOP_MARGIN, Graphics.TOP
                | Graphics.HCENTER);
        drawCreateBlogMenuSelector(graphics);
        yPos = 2 * MBlogConstants.TOP_MARGIN + MBlogConstants.FONT_CREATE_BLOG_MENU_TITLE.getHeight();
        graphics.setColor(MBlogConstants.COLOR_CREATE_BLOG_MENU_ITEMS);
        for (int i = 0; i < MBlogConstants.CREATE_BLOG_MENU_ITEMS.length; i++)
        {
            graphics.setFont(MBlogConstants.FONT_CREATE_BLOG_MENU);
            graphics.drawString(MBlogConstants.CREATE_BLOG_MENU_ITEMS[i], getWidth() / 2, yPos, Graphics.TOP
                    | Graphics.HCENTER);
            yPos += MBlogConstants.FONT_CREATE_BLOG_MENU.getHeight() + MBlogConstants.CREATE_BLOG_MENU_ITEM_SEPERATOR;
        }
    }

    private void drawCreateBlogMenuSelector(Graphics graphics)
    {
        int currentItemWidth = graphics.getFont().stringWidth(
                MBlogConstants.CREATE_BLOG_MENU_ITEMS[createBlogMenuSelectedItemIndex]);
        int xPos = (getWidth() - currentItemWidth) / 2;
        int yPos = 2 * MBlogConstants.TOP_MARGIN + MBlogConstants.FONT_CREATE_BLOG_MENU_TITLE.getHeight()
                + createBlogMenuSelectedItemIndex * MBlogConstants.CREATE_BLOG_MENU_ITEM_SEPERATOR
                + createBlogMenuSelectedItemIndex * MBlogConstants.FONT_CREATE_BLOG_MENU.getHeight();
        graphics.setColor(MBlogConstants.COLOR_MENU_SELECTOR_BORDER);
        graphics.fillRoundRect(xPos - 16, yPos - 3, currentItemWidth + 35, MBlogConstants.FONT_CREATE_BLOG_MENU
                .getHeight() + 13, 15, 15);
        graphics.setColor(MBlogConstants.COLOR_CREATE_BLOG_MENU_SELECTOR);
        graphics.fillRoundRect(xPos - 15, yPos - 2, currentItemWidth + 30, MBlogConstants.FONT_CREATE_BLOG_MENU
                .getHeight() + 8, 15, 15);
    }

    private void drawBlogText(Graphics graphics)
    {
        blogTextBox.addCommand(saveBlogText);
        blogTextBox.addCommand(backToCreateBlogMenu);
        display.setCurrent(blogTextBox);
    }

    private void drawBlogTitle(Graphics graphics)
    {
        blogTitleTextBox.addCommand(backToCreateBlogMenu);
        blogTitleTextBox.addCommand(saveBlogTitle);
        display.setCurrent(blogTitleTextBox);
    }

    private void drawImageCaption(Graphics graphics)
    {
        blogImageCaptionTextBox.addCommand(backToCreateBlogMenu);
        blogImageCaptionTextBox.addCommand(saveImageCaption);
        display.setCurrent(blogImageCaptionTextBox);
    }

    private void drawCaptureImageForm(Graphics graphics)
    {
        captureForm.addCommand(backToCreateBlogMenu);
        captureForm.addCommand(captureImage);

    }

    private void drawSettings(Graphics graphics)
    {
        settingsForm.append(usernameField);
        settingsForm.append(passwordField);
        settingsForm.append(serverURLField);
        settingsForm.append(snapperChoiceGroup);
        settingsForm.addCommand(saveSettings);
        settingsForm.addCommand(resetSettings);
        settingsForm.addCommand(backToMainMenu);
        display.setCurrent(settingsForm);

    }

    private void drawViewList(Graphics graphics)
    {
        showBlogListForm.addCommand(backToMainMenu);
        showBlogListForm.addCommand(selectBlogListItem);

        display.setCurrent(showBlogListForm);
        getBlogListFromServer();
    }

    private void drawViewBlog(Graphics graphics)
    {
        viewSelectedBlogForm.addCommand(backToBlogList);
        getSelectedBlogFromServer();
        display.setCurrent(viewSelectedBlogForm);

    }

    private void drawBlogPreview(Graphics graphics)
    {
        blogPreviewForm.addCommand(backToCreateBlogMenu);
        blogPreviewForm.addCommand(uploadBlog);
        StringItem temp;
        temp = new StringItem("Blog Title", blogData.getBlogTitle());
        temp.setFont(MBlogConstants.FONT_BLOG_PREVIEW_TITLE);
        blogPreviewForm.append(temp);
        temp = new StringItem("Blog Text", blogData.getBlogText());
        temp.setFont(MBlogConstants.FONT_BLOG_PREVIEW_TEXT);
        temp.setLayout(Item.LAYOUT_NEWLINE_AFTER);
        blogPreviewForm.append(temp);
        if (blogData.getImage() != null)
        {
            ImageItem iItem = new ImageItem("", blogData.getImage(), Item.LAYOUT_NEWLINE_BEFORE
                    | Item.LAYOUT_NEWLINE_AFTER, null);
            blogPreviewForm.append(iItem);
        }
        temp = new StringItem("Image Time", blogData.getImageTime());
        temp.setFont(MBlogConstants.FONT_BLOG_PREVIEW_IMAGE_CAPTION);
        temp.setLayout(Item.LAYOUT_NEWLINE_AFTER | Item.LAYOUT_NEWLINE_BEFORE);
        blogPreviewForm.append(temp);
        temp = new StringItem("", blogData.getImageCaption()); // Image caption
        temp.setLayout(Item.LAYOUT_NEWLINE_AFTER | Item.LAYOUT_NEWLINE_BEFORE);
        blogPreviewForm.append(temp);
        display.setCurrent(blogPreviewForm);

    }

    // viewCamera() method initializes camera
    private void viewCamera()
    {
        try
        {
            if (snapperChoice == Settings.VIDEO_LOCATOR)
            {
                player = Manager.createPlayer("capture://video");
            }
            else
            {
                player = Manager.createPlayer("capture://image");
            }

            player.realize();
            if ((vidControl = (VideoControl) player.getControl("VideoControl")) != null)
            {
                vidItem = (Item) vidControl.initDisplayMode(VideoControl.USE_GUI_PRIMITIVE, null);
                int preDefinedLayout = Item.LAYOUT_2 | Item.LAYOUT_CENTER | Item.LAYOUT_NEWLINE_AFTER;
                vidItem.setLayout(preDefinedLayout);
                capturedImage = new ImageItem("", null, preDefinedLayout, "");
                if (capturedImage != null)
                {
                    captureForm.append(vidItem);
                }
            }
            player.start();
            display.setCurrent(captureForm);
        }
        catch (Exception e)
        {
            showException(e);
            return;
        }
    }

    // To show exceptions occurred during camera operations on screen as Alert
    private void showException(Exception e)
    {
        Alert cameraAlert = new Alert("Camera Problem!", "Camera Problem: " + e.toString(), null, null);
        cameraAlert.setTimeout(Alert.FOREVER);
        display.setCurrent(cameraAlert, captureForm);
    }

    /**
     * This method removes all commands in the screen before changing to another
     * screen.
     */
    private void removeAllCommands()
    {
        this.removeCommand(backToMainMenu);
        this.removeCommand(mainMenuSelect);
        this.removeCommand(createBlogMenuSelect);
        this.removeCommand(backToCreateBlogMenu);
        this.removeCommand(saveBlogTitle);
        this.removeCommand(saveBlogText);
        this.removeCommand(saveImageCaption);
        this.removeCommand(captureImage);
        this.removeCommand(discardImage);
        this.removeCommand(resetSettings);
        this.removeCommand(saveImage);
        this.removeCommand(saveSettings);
        this.removeCommand(uploadBlog);
        this.removeCommand(selectBlogListItem);
        this.removeCommand(backToBlogList);
    }

    /**
     * Get Blog list from the server
     */
    private void getBlogListFromServer()
    {
        final MBlogServerConnection serverConn = new MBlogServerConnection(username, password, serverURL);
        Thread th = new Thread(new Runnable()
        {

            public void run()
            {
                try
                {
                    blogListArray = serverConn.fetchBlogList();
                }
                catch (NullPointerException e)
                {
                    e.printStackTrace();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                finally
                {
                    serverConn.closeConnection();
                    for (int i = 0; i < blogListArray.length; i++)
                    {
                        System.out.println("Blog " + i + "::" + blogListArray[i]);
                    }
                    if (blogListArray != null)
                    {
                    	
                        blogListChoiceGroup = new ChoiceGroup("Blog List", ChoiceGroup.EXCLUSIVE, blogListArray, null);
                        showBlogListForm.append(blogListChoiceGroup);
                        display.setCurrent(showBlogListForm);
                        blogIDArray = getBlogIDArrayFromList(blogListArray);
                        for (int i = 0; i < blogIDArray.length; i++)
                        {
                            System.out.println("BlogIDArray-" + i + "-" + blogIDArray[i]);
                        }
                    }
                }
            }
        });
        th.start();
    }

    private void getSelectedBlogFromServer()
    {
        final MBlogServerConnection serverConnection = new MBlogServerConnection(username, password, serverURL);
        Thread thread = new Thread(new Runnable()
        {
            BlogData recivedBlog = new BlogData();

            public void run()
            {
                try
                {
                    recivedBlog = serverConnection.fetchBlog(selectedBlogId);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                finally
                {

                    serverConnection.closeConnection();
                    if (recivedBlog != null)
                    {
                        StringItem temp;

                        temp = new StringItem("Blog Title:", "");
                        temp.setText(recivedBlog.getBlogTitle());
                        viewSelectedBlogForm.append(temp);

                        temp = new StringItem("Blog Text:", "");
                        temp.setText(recivedBlog.getBlogText());
                        viewSelectedBlogForm.append(temp);

                        try
                        {
                            // Try to form image from the data received from the
                            // server
                            Image img = Image.createImage(recivedBlog.getImageByteArray(), 0, recivedBlog
                                    .getImageByteArray().length);
                            ImageItem imageItem = new ImageItem("Image", img, Item.LAYOUT_NEWLINE_BEFORE
                                    | Item.LAYOUT_NEWLINE_AFTER, null);
                            viewSelectedBlogForm.append(imageItem);
                        }
                        catch (Exception e)
                        {
                            System.out.println("Error forming image...");
                            e.printStackTrace();
                            temp = new StringItem("Image", "");
                            temp.setText("Image data is corrupt!");
                            viewSelectedBlogForm.append(temp);
                        }

                        temp = new StringItem("Image Time:", "");
                        temp.setText(recivedBlog.getImageTime());
                        viewSelectedBlogForm.append(temp);

                        temp = new StringItem("Image Caption:", "");
                        temp.setText(recivedBlog.getImageCaption());
                        viewSelectedBlogForm.append(temp);
                    }

                    System.out.println("Blog with id:" + selectedBlogId + " fetched from the server.");
                }
            }
        });
        thread.start();
    }

    private int[] getBlogIDArrayFromList(String[] blogListArray)
    {
        int[] retArray = new int[blogListArray.length];
        for (int i = 0; i < blogListArray.length; i++)
        {
            retArray[i] = Integer.parseInt(blogListArray[i].substring(0, (blogListArray[i].indexOf(':'))));
        }
        return retArray;

    }

    /**
     * This method is called whenever user presser any command on screen
     * 
     * @param Command
     *            , displayable
     */
    public void commandAction(Command cmd, Displayable displayable)
    {
        if (cmd == mainMenuSelect)
        {
            // Go to selected item index ..
            selectScreen(mainMenuSelectedItemIndex);
        }
        else
            if (cmd == backToMainMenu)
            {
                display.setCurrent(this);
                setScreenId(MBlogConstants.SCREEN_MAIN_MENU);
            }
            else
                if (cmd == createBlogMenuSelect)
                {
                    display.setCurrent(this);
                    selectBlogScreen(createBlogMenuSelectedItemIndex);
                }
                else
                    if (cmd == backToCreateBlogMenu)
                    {
                        if (player != null)
                        {
                            player.close();
                        }
                        display.setCurrent(this);
                        setScreenId(MBlogConstants.SCREEN_CREATE_BLOG_MAIN);
                    }
                    else
                        if (cmd == saveBlogTitle)
                        {
                            blogData.setBlogTitle(blogTitleTextBox.getString());
                            display.setCurrent(this);
                            setScreenId(MBlogConstants.SCREEN_CREATE_BLOG_MAIN);
                        }
                        else
                            if (cmd == saveBlogText)
                            {
                                blogData.setBlogText(blogTextBox.getString());
                                display.setCurrent(this);
                                setScreenId(MBlogConstants.SCREEN_CREATE_BLOG_MAIN);
                            }
                            else
                                if (cmd == saveImageCaption)
                                {
                                    blogData.setImageCaption(blogImageCaptionTextBox.getString());
                                    display.setCurrent(this);
                                    setScreenId(MBlogConstants.SCREEN_CREATE_BLOG_MAIN);
                                }
                                else
                                    if (cmd == captureImage)
                                    {
                                        showCapturedImageForm = new Form("Captured Image");
                                        showCapturedImageForm.setCommandListener(this);
                                        Thread th1 = new Thread()
                                        {
                                            public void run()
                                            {
                                                try
                                                {
                                                    imageByteArray = vidControl.getSnapshot(null);
                                                    // System.getProperty("video.snapshot.encodings"));
                                                    finalImage = Image.createImage(imageByteArray, 0,
                                                            imageByteArray.length);
                                                    capturedImage.setImage(finalImage);
                                                    player.close();
                                                }
                                                catch (Exception me)
                                                {
                                                    showException(me);
                                                }
                                                removeAllCommands();
                                                showCapturedImageForm.append(finalImage);
                                                saveImage = new Command("Save", Command.OK, 1);
                                                discardImage = new Command("Discard", Command.CANCEL, 2);
                                                showCapturedImageForm.addCommand(saveImage);
                                                showCapturedImageForm.addCommand(discardImage);
                                                display.setCurrent(showCapturedImageForm);
                                            }
                                        };
                                        th1.start();

                                    }
                                    else
                                        if (cmd == saveSettings)
                                        {
                                            username = usernameField.getString();
                                            password = passwordField.getString();
                                            serverURL = serverURLField.getString();
                                            if (snapperChoiceGroup.getSelectedIndex() == 0)
                                            {
                                                snapperChoice = Settings.VIDEO_LOCATOR;
                                            }
                                            else
                                                if (snapperChoiceGroup.getSelectedIndex() == 1)
                                                {
                                                    snapperChoice = Settings.IMAGE_LOCATOR;
                                                }
                                            try
                                            {
                                                RMSHandler.getInstance().setSettings(username, password, serverURL,
                                                        snapperChoice);
                                            }
                                            catch (RecordStoreFullException e)
                                            {
                                                e.printStackTrace();
                                            }
                                            catch (RecordStoreNotFoundException e)
                                            {
                                                e.printStackTrace();
                                            }
                                            catch (RecordStoreException e)
                                            {
                                                e.printStackTrace();
                                            }
                                            display.setCurrent(this);
                                            setScreenId(MBlogConstants.SCREEN_MAIN_MENU);
                                        }
                                        else
                                            if (cmd == resetSettings)
                                            {
                                                try
                                                {
                                                    RMSHandler.getInstance().setSettings("", "", "",
                                                            Settings.IMAGE_LOCATOR);
                                                }
                                                catch (RecordStoreFullException e)
                                                {
                                                    e.printStackTrace();
                                                }
                                                catch (RecordStoreNotFoundException e)
                                                {
                                                    e.printStackTrace();
                                                }
                                                catch (RecordStoreException e)
                                                {
                                                    e.printStackTrace();
                                                }
                                                display.setCurrent(this);
                                                setScreenId(MBlogConstants.SCREEN_MAIN_MENU);
                                            }
                                            else
                                                if (cmd == uploadBlog)
                                                {
                                                    System.out.println("Username:" + username + " :: ServerUrl="
                                                            + serverURL);
                                                    final MBlogServerConnection serverConn = new MBlogServerConnection(
                                                            username, password, serverURL);

                                                    Thread th = new Thread(new Runnable()
                                                    {

                                                        public void run()
                                                        {
                                                            try
                                                            {
                                                                String serverResponse = serverConn
                                                                        .sendBlogData(blogData);
                                                                serverConnectionResponseAlert = new Alert("Server Connected",
                                                                        "Server Connected: " + serverResponse, null,
                                                                        AlertType.INFO);
                                                            }
                                                            catch (Exception e)
                                                            {
                                                                e.printStackTrace();
                                                                System.out.println(e);
                                                                serverConnectionResponseAlert = new Alert("ERROR",
                                                                        "Error connecting server: " + e.getMessage(),
                                                                        null, AlertType.ERROR);
                                                                serverConnectionResponseAlert.setTimeout(Alert.FOREVER);
                                                            }
                                                            finally
                                                            {
                                                                serverConn.closeConnection();
                                                                
                                                                serverConnectionResponseAlert.setTimeout(2000);
                                                                display.setCurrent(serverConnectionResponseAlert, canvas);
                                                                setScreenId(MBlogConstants.SCREEN_BLOG_PREVIEW);
                                                            }
                                                        }
                                                    });
                                                    th.start();

                                                    
                                                }
                                                else
                                                    if (cmd == Alert.DISMISS_COMMAND)
                                                    {
                                                        setScreenId(MBlogConstants.SCREEN_MAIN_MENU);
                                                        display.setCurrent(this);
                                                    }
                                                    else
                                                        if (cmd == saveImage)
                                                        {
                                                            blogData.setImage(finalImage);
                                                            blogData.setImageByteArray(imageByteArray);
                                                            blogData.setImageTime(getCurrentTime());
                                                            setScreenId(MBlogConstants.SCREEN_CREATE_BLOG_MAIN);
                                                            display.setCurrent(this);
                                                        }
                                                        else
                                                            if (cmd == discardImage)
                                                            {
                                                                finalImage = null;
                                                                imageByteArray = null;
                                                                blogData.setImageTime(null);
                                                                blogData.setImage(finalImage);
                                                                blogData.setImageByteArray(imageByteArray);
                                                                setScreenId(MBlogConstants.SCREEN_BLOG_CAPTURE_IMAGE);
                                                                display.setCurrent(this);
                                                            }
                                                            else
                                                                if (cmd == selectBlogListItem)
                                                                {
                                                                    int selectedIndex = blogListChoiceGroup
                                                                            .getSelectedIndex();
                                                                    selectedBlogId = blogIDArray[selectedIndex];
                                                                    System.out.println("Selected Blog ID: "
                                                                            + selectedBlogId);
                                                                    setScreenId(MBlogConstants.SCREEN_VIEW_BLOG);
                                                                    display.setCurrent(this);

                                                                }
                                                                else
                                                                    if (cmd == backToBlogList)
                                                                    {
                                                                        setScreenId(MBlogConstants.SCREEN_VIEW_LIST);
                                                                        display.setCurrent(this);
                                                                    }
    }

    /**
     * Clears the Canvas screen by drawing colored filled rectangle *
     * 
     * @param graphics
     *            , color
     */
    private void clearScreen(Graphics graphics, int fillColor)
    {
        graphics.setColor(fillColor);
        graphics.fillRect(0, 0, getWidth(), getHeight());
    }

    /**
     * To handle keyPressed in custom components. Other than standard Commands
     * 
     * @param keyCode
     */
    public void keyPressed(int keyCode)
    {
        keyCode = getGameAction(keyCode);
        switch (screenID)
        {
        case MBlogConstants.SCREEN_MAIN_MENU:
            handleMainMenuKeyPressed(keyCode);
            break;
        case MBlogConstants.SCREEN_CREATE_BLOG_MAIN:
            handleCreateBlogMenuKeyPressed(keyCode);
            break;
        }
        repaint();
    }

    private void handleMainMenuKeyPressed(int keyCode)
    {
        if (keyCode == Canvas.DOWN && mainMenuSelectedItemIndex < MBlogConstants.MAIN_MENU_ITEMS.length - 1)
        {
            mainMenuSelectedItemIndex++;
        }
        else
            if (keyCode == Canvas.UP && mainMenuSelectedItemIndex > 0)
            {
                mainMenuSelectedItemIndex--;
            }
            else
                if (keyCode == Canvas.FIRE)
                {
                    // Get the selected item index .. and change screen
                    // accordingly
                    selectScreen(mainMenuSelectedItemIndex);
                }
    }

    private void handleCreateBlogMenuKeyPressed(int keyCode)
    {
        if (keyCode == Canvas.DOWN
                && createBlogMenuSelectedItemIndex < MBlogConstants.CREATE_BLOG_MENU_ITEMS.length - 1)
        {
            createBlogMenuSelectedItemIndex++;
        }
        else
            if (keyCode == Canvas.UP && createBlogMenuSelectedItemIndex > 0)
            {
                createBlogMenuSelectedItemIndex--;
            }
            else
                if (keyCode == Canvas.FIRE)
                {
                    // Get the selected item index .. and change screen
                    // accordingly
                    selectBlogScreen(createBlogMenuSelectedItemIndex);
                }
    }

    private void selectScreen(int mainMenuSelectedItemIndex)
    {
        if (mainMenuSelectedItemIndex == 0)
        {
            // Initialize BlogData object to start new blog
            blogData = new BlogData();
            setScreenId(MBlogConstants.SCREEN_CREATE_BLOG_MAIN);
        }
        else
            if (mainMenuSelectedItemIndex == 1)
            {
                setScreenId(MBlogConstants.SCREEN_VIEW_LIST);
            }
            else
                if (mainMenuSelectedItemIndex == 2)
                {
                    setScreenId(MBlogConstants.SCREEN_SETTINGS);
                }
                else
                    if (mainMenuSelectedItemIndex == 3)
                    {
                        setScreenId(MBlogConstants.SCREEN_HELP);
                    }
                    else
                        if (mainMenuSelectedItemIndex == 4)
                        {
                            midlet.notifyDestroyed(); // exit the application
                        }
    }

    private void selectBlogScreen(int createBlogMenuSelectedItemIndex)
    {
        if (createBlogMenuSelectedItemIndex == 0)
        {
            setScreenId(MBlogConstants.SCREEN_BLOG_TITLE);
        }
        else
            if (createBlogMenuSelectedItemIndex == 1)
            {
                setScreenId(MBlogConstants.SCREEN_BLOG_TEXT);
            }
            else
                if (createBlogMenuSelectedItemIndex == 2)
                {
                    setScreenId(MBlogConstants.SCREEN_BLOG_CAPTURE_IMAGE);
                }
                else
                    if (createBlogMenuSelectedItemIndex == 3)
                    {
                        setScreenId(MBlogConstants.SCREEN_BLOG_IMAGE_CAPTION);
                    }
                    else
                        if (createBlogMenuSelectedItemIndex == 4)
                        {
                            setScreenId(MBlogConstants.SCREEN_BLOG_PREVIEW);
                        }
    }

    /**
     * This method returns current date and time as per IST
     * 
     * @return String containing current date and time (Format:
     *         dd/mm/yyyy-hh:mm:ss)
     */
    private String getCurrentTime()
    {

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+05:30"));
        cal.setTime(new Date());

        StringBuffer time = new StringBuffer("");
        time.append(cal.get(Calendar.DATE));
        time.append("/");
        time.append(cal.get(Calendar.MONTH));
        time.append("/");
        time.append(cal.get(Calendar.YEAR));
        time.append("-");
        time.append((cal.get(Calendar.HOUR)) < 10 ? "0" : "");
        time.append(cal.get(Calendar.HOUR));
        time.append(":");
        time.append((cal.get(Calendar.MINUTE)) < 10 ? "0" : "");
        time.append(cal.get(Calendar.MINUTE));
        time.append(":");
        time.append((cal.get(Calendar.SECOND)) < 10 ? "0" : "");
        time.append(cal.get(Calendar.SECOND));
        if (cal.get(Calendar.AM_PM) == 0)
            time.append(" AM");
        else
            if (cal.get(Calendar.AM_PM) == 1)
                time.append(" PM");
        return time.toString();
    }

}