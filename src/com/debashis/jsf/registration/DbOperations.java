package com.debashis.jsf.registration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


public abstract class DbOperations {
	private static final String dbUrl = "jdbc:oracle:thin:@localhost:1521:ORCL";
		
	private static Properties getProps() {
		Properties props = new Properties();
		props.setProperty("user", "WMAPP");
		props.setProperty("password", "WMAPP");
		return props;
	}
	
	public static Connection getDbConn() throws ClassNotFoundException, SQLException{
		Properties props = getProps();
		Connection dbConn=null;
		Class.forName("oracle.jdbc.driver.OracleDriver");
	    dbConn = DriverManager.getConnection(dbUrl, props);			
		return dbConn;
	}
		
}
