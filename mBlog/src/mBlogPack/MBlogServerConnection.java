package mBlogPack;

import base64.Base64;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

/**
 * This class is used to connect to mBlog Server application and send/receive
 * data to the server
 * 
 */
public class MBlogServerConnection {
	private String mUsername;

	private String mPassword;

	private String mServerLoginString;

	private String mServerUrl;

	private HttpConnection mHttpConnection;

	private OutputStream mOutStream;

	private InputStream mInStream;

	public MBlogServerConnection(String username, String password,
			String serverUrl) {
		mUsername = username;
		mPassword = password;
		mServerUrl = serverUrl;

		if (mUsername != null && mPassword != null) {
			mServerLoginString = "uname=" + mUsername + "&pw=" + mPassword;
		}
	}

	public String sendBlogData(BlogData blogData) throws IOException,
			NullPointerException {
		mHttpConnection = (HttpConnection) Connector.open(mServerUrl
				+ "/SubmitBlog", Connector.READ_WRITE, false);
		mHttpConnection.setRequestMethod(HttpConnection.POST);
		
		String imageAttrib = "&image_data=";
		long dataLength = mServerLoginString.getBytes().length;
		dataLength += formatSendingData(blogData).length;
		String base64EncodedString = null;
		if (blogData.getImageByteArray() != null) {
			dataLength += imageAttrib.getBytes().length;
			// Encode the image data using Base64 algorithm
			base64EncodedString = Base64.encode(blogData.getImageByteArray());
			dataLength += base64EncodedString.getBytes().length;
		}

		mHttpConnection.setRequestProperty("Content-length", "" + dataLength);

		mOutStream = mHttpConnection.openOutputStream();

		// Write the login credentials to output stream
		mOutStream.write(mServerLoginString.getBytes());
		// Write all text data of blog to output stream
		mOutStream.write(formatSendingData(blogData));
		// Write image data attrib to output stream
		mOutStream.write(imageAttrib.getBytes());
		// Write image data to output stream only if image data is not null
		if (blogData.getImageByteArray() != null) {
			mOutStream.write(base64EncodedString.getBytes());
			System.out.println("Sending image string data:"
					+ base64EncodedString);
		}
		mOutStream.flush();

		mInStream = mHttpConnection.openInputStream();
		int size = mInStream.available();
		byte[] inArray = new byte[size];
		mInStream.read(inArray);
		new String(inArray);

		int serverResponseCode = mHttpConnection.getResponseCode();

		return "\nServer response code: " + serverResponseCode;
	}

	public void closeConnection() {
		try {
			mOutStream.close();
			mHttpConnection.close();
			System.out.println("Server connection closed!");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e);
		} catch (NullPointerException ne) {
			ne.printStackTrace();
		}

	}

	/**
	 * @param blogData
	 * @return
	 */
	private byte[] formatSendingData(BlogData blogData) {
		StringBuffer buff = new StringBuffer();

		buff.append("&blog_title=");
		buff.append(blogData.getBlogTitle());
		buff.append("&blog_text=");
		buff.append(blogData.getBlogText());
		buff.append("&image_caption=");
		buff.append(blogData.getImageCaption());
		buff.append("&image_time=");
		buff.append(blogData.getImageTime());

		return buff.toString().getBytes();
	}

	public String[] fetchBlogList() throws IOException, NullPointerException {
		mHttpConnection = (HttpConnection) Connector.open(mServerUrl
				+ "/BlogList", Connector.READ_WRITE, false);
		mHttpConnection.setRequestMethod(HttpConnection.POST);
		mHttpConnection.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");

		mOutStream = mHttpConnection.openOutputStream();
		// Write any data for sending...
		mOutStream.flush();

		mInStream = mHttpConnection.openInputStream();
		int size = mInStream.available();
		System.out.println("Recived blog list. Data size:"+size);
		byte[] inArray = new byte[size];
		mInStream.read(inArray);
		mInStream.close();
		String recivedString = new String(inArray);
		System.out.println("::" + recivedString);

		return seperateBlogList(recivedString);
	}

	private String[] seperateBlogList(String recivedString) {
		String[] retBlogList = null;
		Vector blogList = new Vector();
		int index = -1;

		while (recivedString.indexOf("~~~") != -1) {
			index = recivedString.indexOf("~~~");
			blogList.addElement(recivedString.substring(0, index));
			if (index < (recivedString.length() - 3)) {
				recivedString = recivedString.substring(index + 3);
			} else {
				break;
			}
		}
		retBlogList = new String[blogList.size()];
		for (int i = 0; i < blogList.size(); i++) {
			retBlogList[i] = (String) blogList.elementAt(i);
		}

		return retBlogList;
	}

	public BlogData fetchBlog(int blogID) throws IOException,
			NullPointerException {
		String sendStr = "blogid=" + blogID;

		mHttpConnection = (HttpConnection) Connector.open(mServerUrl
				+ "/BlogList", 1);
		mHttpConnection.setRequestMethod(HttpConnection.POST);
		mHttpConnection.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		mHttpConnection.setRequestProperty("Content-length", ""
				+ sendStr.getBytes().length);

		mOutStream = mHttpConnection.openOutputStream();
		mOutStream.write(sendStr.getBytes());
		mOutStream.flush();

		mInStream = mHttpConnection.openInputStream();
		int size = mInStream.available();
		byte[] inArray = new byte[size];
		mInStream.read(inArray);
		mInStream.close();
		String recivedString = new String(inArray);

		System.out.println("Blog Data recived from server::" + recivedString);

		return extractBlogData(recivedString);
	}

	private BlogData extractBlogData(String recivedString) {
		BlogData blog = new BlogData();
		String seperator = "#~#~#";
		blog.setBlogTitle(recivedString.substring(0, recivedString
				.indexOf(seperator)));
		recivedString = recivedString.substring(recivedString
				.indexOf(seperator) + 5);
		blog.setBlogText(recivedString.substring(0, recivedString
				.indexOf(seperator)));
		recivedString = recivedString.substring(recivedString
				.indexOf(seperator) + 5);
		blog.setImageCaption(recivedString.substring(0, recivedString
				.indexOf(seperator)));
		recivedString = recivedString.substring(recivedString
				.indexOf(seperator) + 5);
		blog.setImageTime(recivedString.substring(0, recivedString
				.indexOf(seperator)));
		recivedString = recivedString.substring(recivedString
				.indexOf(seperator) + 5);
		blog.setImageByteArray(Base64.decode(recivedString));

		System.out.println("Title:" + blog.getBlogTitle() + "\nText:"
				+ blog.getBlogText() + "\nCaption:" + blog.getImageCaption()
				+ "\nTime:" + blog.getImageTime() + "\nImage Data:"
				+ new String(blog.getImageByteArray()));

		return blog;
	}

}
