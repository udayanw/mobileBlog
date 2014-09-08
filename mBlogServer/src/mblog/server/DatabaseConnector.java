package mblog.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnector {
	private Connection conn;
	private String db_url = "localhost";
	private int db_port = 3306;
	private String db_username = "alpha_mblog";
	private String db_password = "alpha";

	public boolean isDbConnected() {
		boolean checkFlag = false;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://" + db_url + ":"
					+ db_port + "/mobile_blog_db?" + "user=" + db_username
					+ "&password=" + db_password);
			if (conn != null) {
				System.out.println("Database connection established");
				checkFlag = true;
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		return checkFlag;
	}

	public void close() {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public int updateQuery(String sql) throws SQLException {
		Statement stmt = conn.createStatement();
		int result = stmt.executeUpdate(sql);
		return result;
	}

	public ResultSet execQuerry(String sql) throws SQLException {
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		return rs;
	}

}
