package servers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataManager {
	public static DataManager dm;
	Connection conn = null;

	public static DataManager getInstance() throws SQLException {
		if (dm == null)
			dm = new DataManager();

		return dm;
	}

	private DataManager() throws SQLException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		conn = DriverManager.getConnection(
				"jdbc:mysql://pluto.pwrweb.it/uplink", "root", "vogedrukut");
	}

	public void closeConnection() {
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean checkUser(String username, String password)
			throws SQLException {

		java.sql.Statement st = conn.createStatement();
		ResultSet res = st
				.executeQuery("SELECT * FROM users WHERE username = '"
						+ username + "' AND password = '" + password + "' ");
		while (res.next()) {
			return true;
		}

		return false;
	}
}
