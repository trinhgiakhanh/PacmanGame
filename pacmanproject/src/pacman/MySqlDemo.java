
package pacman;

import java.sql.*;
import java.sql.DriverManager;
public class MySqlDemo {

static final String DRIVER_CLASS = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
static final String DB_URL = "jdbc:sqlserver://localhost:1433;DatabaseName=gamescore;encrypt=true;trustServerCertificate=true;";
static final String USER = "sa";//tài khoản
static final String PASS = "123123";//mật khẩu

public static void main(String[] args) throws SQLException, ClassNotFoundException {
		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName(DRIVER_CLASS);
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT name, score FROM highscore");
			
			while (rs.next()) {	
				String name = rs.getString("name");
				int score = rs.getInt("score");
				System.out.print("name: " + name);
				System.out.println(", score: " + score);
			}
			rs.close();
		} finally {
			if (stmt != null)
			stmt.close();
			if (conn != null)
			conn.close();
		} 
		System.out.println("Done!");
	}
}
