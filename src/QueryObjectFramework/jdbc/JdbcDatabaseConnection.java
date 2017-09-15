package QueryObjectFramework.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
	private Statement fStatement = null;
	
	/**
	 * Constructor of JDBC database connection class, 
	 * setting user and pass null if not required.
	 * 
	 * @param jdbcDriver
	 * 			JDBC driver name
	 * @param dbUrl
	 * 			Database URL
	 * @param user
	 * 			Database user name
	 * @param pass
	 * 			Database password
	 */
	public JdbcDatabaseConnection(String jdbcDriver, String dbUrl, String user, String pass) {
		fJdbcDriver = jdbcDriver;
		fDbUrl = dbUrl;
		fUser = user;
		fPass = pass;
	}
	
	/**
	 * Update JDBC database connection.
	 * 
	 * @param jdbcDriver
	 * 			JDBC driver name
	 * @param dbUrl
	 * 			Database URL
	 * @param user
	 * 			Database user name
	 * @param pass
	 * 			Database password
	 */
	public void setJdbcDatabaseConnection(String jdbcDriver, String dbUrl, String user, String pass) {
		fJdbcDriver = jdbcDriver;
		fDbUrl = dbUrl;
		fUser = user;
		fPass = pass;
	}
	
	/**
	 * Update JDBC database connection.
	 * 
	 * @param jdbcDriver
	 * 			JDBC driver name
	 * @param dbUrl
	 * 			Database URL
	 */
	public void setJdbcDatabaseConnection(String jdbcDriver, String dbUrl) {
		fJdbcDriver = jdbcDriver;
		fDbUrl = dbUrl;
		fUser = null;
		fPass = null;
	}
	/**
	 * Execute SQL statement.
	 * 
	 * @param exeSql
	 * 			SQL string
	 * @return ResultSet
	 * 			SQL execution results
	 */
	public ResultSet executeQueryObject(String exeSql) {
		getDatabaseStatement();
		
		ResultSet results = null;
		try {
			results = fStatement.executeQuery(exeSql);
		} catch (SQLException executeQueryObjectException) {
			LOGGER.severe("Failed to execute sql. Datails: " + executeQueryObjectException.getMessage());
		} finally {
			if (results != null) {
				try {
					results.close();
				} catch (SQLException closeResultSetException) {
					LOGGER.severe("Failed to close resultSets. Datails: " + closeResultSetException.getMessage());
				}
			}
			closeDatabaseConnection();
		}
		
		return results;
	}
	
	/**
	 * Get SQL statement of DB connection.
	 */
	private void getDatabaseStatement() {
		getDatabaseConnection();
		try {
			fStatement = fConn.createStatement();
		} catch (SQLException createStatementException) {
			LOGGER.severe("Failed to create statement. Datails: " + createStatementException.getMessage());
		} finally {
			closeDatabaseConnection();
		}
	}
	
	/**
	 * Get database connection.
	 */
	private void getDatabaseConnection() {
		if (fConn == null) {
			try {
				ExeState exeState = openDatabaseConn();
				if (exeState != ExeState.SUCESSFUL) {
					return;
				}
			} catch (ClassNotFoundException classNotFoundExeception) {
				LOGGER.severe("Unable to load driver class.");
			} catch (SQLException dbConnectionException) {
				LOGGER.severe("Failed to access database. Datails: " + dbConnectionException.getMessage());
			} finally {
				closeDatabaseConnection();
			}
		}
	}
	
	/**
	 * Close database connection.
	 */
	private void closeDatabaseConnection() {
		try {
			if (fConn != null) {
				fConn.close();
			}
			if (fStatement != null) {
				fStatement.close();
			}
		} catch (SQLException dbCloseException) {
			LOGGER.severe("Failed to close database. Datails: " + dbCloseException.getMessage());
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
		if (fUser == null && fPass == null) {
			fConn = DriverManager.getConnection(fDbUrl);
		} else {
			fConn = DriverManager.getConnection(fDbUrl, fUser, fPass);
		}
		
		return ExeState.SUCESSFUL;
	}
}
