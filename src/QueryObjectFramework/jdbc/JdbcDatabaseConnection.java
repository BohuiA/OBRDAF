package QueryObjectFramework.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

import QueryObjectFramework.common.ExeState;

/**
 * JDBC database connection class.
 * 
 * @author Bohui Axelsson
 */
public class JdbcDatabaseConnection {
	private static final Logger LOGGER = Logger.getLogger(JdbcDatabaseConnection.class.getName());
	
	/*
	 * JDBC driver name and database URL 
	 */
	private String fJdbcDriver = null;
	private String fDbUrl = null;
	
	/*
	 * Database credentials
	 */
	private String fUser = null;
	private String fPass = null;
	
	/*
	 * Database connection
	 */
	private Connection fConn = null;
	
	public JdbcDatabaseConnection(String jdbcDriver, String dbUrl, String user, String pass) {
		fJdbcDriver = jdbcDriver;
		fDbUrl = dbUrl;
		fUser = user;
		fPass = pass;
	}
	
	/**
	 * Get database connection.
	 * 
	 * @return Connection
	 * 			Database connection
	 */
	public Connection getDatabaseConnection() {
		if (fConn == null) {
			try {
				ExeState exeState = openDatabaseConn();
				if (exeState != ExeState.SUCESSFUL) {
					return null;
				}
			} catch (ClassNotFoundException classNotFoundExeception) {
				LOGGER.severe("Unable to load driver class.");
			} catch (SQLException sqlException) {
				LOGGER.severe("Failed to access database. Datails: " + sqlException.getMessage());
			} finally {
				closeDatabaseConnection();
			}
		}
		
		return fConn;
	}
	
	/**
	 * Close database connection.
	 */
	public void closeDatabaseConnection() {
		try {
			if (fConn != null) {
				fConn.close();
			}
		} catch (SQLException sqlException) {
			LOGGER.severe("Failed to close database. Datails: " + sqlException.getMessage());
		}
	}
	
	/**
	 * Open a database connection.
	 * 
	 * @return ExeState 
	 * 			State connection
	 * @throws ClassNotFoundException
	 * 			Unable to load driver class
	 * @throws SQLException
	 * 			Failed on accessing database
	 */
	private ExeState openDatabaseConn() throws ClassNotFoundException, SQLException {
		if (fJdbcDriver == null) {
			LOGGER.warning("JDBC driver of database configuration is missing.");
			return ExeState.ERROR;
		} else if (fDbUrl == null) {
			LOGGER.warning("JDBC database url configuration is missing.");
			return ExeState.ERROR;
		}
		
		/*
		 * Register JDBC driver
		 */
		Class.forName(fJdbcDriver);
		
		/*
		 * Open a connection
		 */
		fConn = DriverManager.getConnection(fDbUrl, fUser, fPass);
		
		return ExeState.SUCESSFUL;
	}
}
