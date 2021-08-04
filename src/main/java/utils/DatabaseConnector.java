package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import javax.sql.DataSource;
import com.mysql.cj.jdbc.MysqlDataSource;

public class DatabaseConnector {
	protected static Properties prop;

	public String connectToSQL(String selectQuery, int rowToReturn, String updateQuery)
			throws ClassNotFoundException, SQLException {

		// This will load the MySQL driver, each DB has its own driver
		//Class.forName("com.mysql.jdbc.Driver");
		ResultSet rs = null;
		Statement st = null;
		Connection con  = null;
		
		try {
			con = getMySQLDataSource().getConnection();
			st = con.createStatement();
			st.execute("SET FOREIGN_KEY_CHECKS = 0;");
			rs = st.executeQuery(selectQuery);
			for (int i = 0; i < rowToReturn; i++) {
				rs.next();
			}

			if (!updateQuery.equalsIgnoreCase("")) {
				int delete = st.executeUpdate(updateQuery);
				if (delete == 1) {
					System.out.println("Row is updated.");
				} else {
					System.out.println("Row NOT updated.");
				}
			} else if (updateQuery.equalsIgnoreCase("")) {
				// When returning value, must alias the column to be returned with "as
				// 'returnvalue'" in the select query
				return rs.getString("returnvalue");
			}
			st.execute("SET FOREIGN_KEY_CHECKS = 1;");

		} catch (SQLException ex) {
			// Logger lgr = Logger.getLogger(sql.class.getName());
			// lgr.log(Level.SEVERE, ex.getMessage(), ex);
			ex.printStackTrace();
		}

		finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (st != null) {
					st.close();
				}
				if (con != null) {
					con.close();
				}

			} catch (SQLException ex) {
				// Logger lgr = Logger.getLogger(sql.class.getName());
				// lgr.log(Level.WARNING, ex.getMessage(), ex);
			}
		}
		return "nothing";
	}

	public static DataSource getMySQLDataSource() {
		
		String epmsDBServerName, epmsDBPort, epmsDBUser, epmsDBName, epmsDBUserPassword;
		MysqlDataSource mysqlDS = null;
		try {
			prop = new Properties();
			prop.load(new FileInputStream(new File("./src/main/resources/config.properties")));
			epmsDBServerName = prop.getProperty("epmsDBServerName");
			epmsDBName = prop.getProperty("epmsDBName");
			epmsDBPort = prop.getProperty("epmsDBPort");
			epmsDBUser = prop.getProperty("epmsDBUser");
			epmsDBUserPassword = prop.getProperty("epmsDBUserPassword");
			mysqlDS = new MysqlDataSource();
			
			mysqlDS.setURL("jdbc:mysql://" + epmsDBServerName + ":" + epmsDBPort + "/" + epmsDBName);
			mysqlDS.setUser(epmsDBUser);
			mysqlDS.setPassword(epmsDBUserPassword);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return mysqlDS;
	}

}
