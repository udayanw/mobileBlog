package mBlogPack;

import javax.microedition.lcdui.Font;


class MBlogConstants
{

    public static final int SCREEN_SPLASH = 0;

    public static final int SCREEN_MAIN_MENU = 1;

    public static final int SCREEN_CREATE_BLOG_MAIN = 2;

    public static final int SCREEN_BLOG_TITLE = 3;

    public static final int SCREEN_BLOG_TEXT = 4;

    public static final int SCREEN_BLOG_CAPTURE_IMAGE = 5;

    public static final int SCREEN_BLOG_PREVIEW_IMAGE = 6;

    public static final int SCREEN_BLOG_IMAGE_CAPTION = 7;

    public static final int SCREEN_BLOG_PREVIEW = 8;

    public static final int SCREEN_VIEW_LIST = 9;

    public static final int SCREEN_VIEW_BLOG = 10;

    public static final int SCREEN_VIEW_IMAGE = 11;

    public static final int SCREEN_SETTINGS = 12;

    public static final int SCREEN_HELP = 13;

    public static final int SPLASH_TIMEOUT = 2000;

    public static final String[] MAIN_MENU_ITEMS =
	{ "Create Blog", "Show Blogs", "Settings", "Help", "Exit" };

    public static final String[] CREATE_BLOG_MENU_ITEMS =
	{ "Blog Title", "Blog Text", "Capture Image", "Image Caption", "Preview & Upload" };

    public static final Font FONT_MAIN_MENU_TITLE = Font.getFont(Font.FACE_SYSTEM,
	    Font.STYLE_BOLD | Font.STYLE_UNDERLINED, Font.SIZE_MEDIUM);

    public static final Font FONT_CREATE_BLOG_MENU_TITLE = Font.getFont(Font.FACE_SYSTEM,
	    Font.STYLE_BOLD | Font.STYLE_UNDERLINED, Font.SIZE_MEDIUM);

    public static final Font FONT_MAIN_MENU = Font.getDefaultFont();

    public static final Font FONT_CREATE_BLOG_MENU = Font.getDefaultFont();

    public static final Font FONT_BLOG_PREVIEW_TITLE = Font.getFont(Font.FACE_SYSTEM,
	    Font.STYLE_BOLD, Font.SIZE_MEDIUM);

    public static final Font FONT_BLOG_PREVIEW_TEXT = Font.getDefaultFont();

    public static final Font FONT_BLOG_PREVIEW_IMAGE_CAPTION = Font.getFont(
	    Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_MEDIUM);

    public static final String IMAGE_SPLASH = "/splash_image.png";

    public static final int TOP_MARGIN = 12;

    public static final int MAIN_MENU_ITEM_SEPERATOR = 10;

    public static final int CREATE_BLOG_MENU_ITEM_SEPERATOR = 10;

    public static final int COLOR_MAIN_MENU_TITLE = 0x660000;

    public static final int COLOR_MAIN_MENU_ITEMS = 0x000099;

    public static final int COLOR_MAIN_MENU_SELECTOR = 0xE6E6EF;

    public static final int COLOR_CREATE_BLOG_MENU_TITLE = 0x660000;

    public static final int COLOR_CREATE_BLOG_MENU_ITEMS = 0x000099;

    public static final int COLOR_CREATE_BLOG_MENU_SELECTOR = 0xE6E6EF;

    public static final int COLOR_MENU_SELECTOR_BORDER = 0x9090CF;

    public static final int COLOR_BLOG_PREVIEW_TITLE = 0x550000;

    public static final int COLOR_BLOG_PREVIEW_TEXT = 0x444455;

    public static final int COLOR_BLOG_PREVIEW_IMAGE_CAPTION = 0x000033;

}
