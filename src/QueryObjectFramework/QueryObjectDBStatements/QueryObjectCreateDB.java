package QueryObjectFramework.QueryObjectDBStatements;

import java.util.logging.Logger;

import org.eclipse.jdt.annotation.NonNull;

import QueryObjectFramework.CommonClasses.SqlQueryTypes;
import QueryObjectFramework.JdbcDatabaseConnection.JdbcDatabaseConnection;

/**
 * Query object class for CREATE DATABASE statement.
 * The CREATE DATABASE statement is used to create a new SQL database.
 *
 * <pre>
 * CREATE DATABASE databasename;
 * </pre>
 *
 * TIPS: Make sure you have admin privilege before creating any database.
 * Once a database is created, you can check it in the list of databases
 * with SHOW DATABASE Query Object;
 *
 * @author Bohui Axelsson
 */
public class QueryObjectCreateDB extends QueryObjectDBAbstract {
	private static final Logger LOGGER = Logger.getLogger(QueryObjectCreateDB.class.getName());

	/**
	 * Create an CREATE DATABASE query object with only JDBC connection.
	 *
	 * @param jdbcDbConn
	 * 			JDBC database connection
	 */
	public QueryObjectCreateDB(@NonNull JdbcDatabaseConnection jdbcDbConn) {
		super(SqlQueryTypes.CREATE_DATABASE, jdbcDbConn);
	}

	/**
	 * Create an CREATE DATABSE query object with DB names.
	 *
	 * @param jdbcDbConn
	 * 			JDBC database connection
	 * @param dbName
	 * 			Database name
	 */
	public QueryObjectCreateDB(@NonNull JdbcDatabaseConnection jdbcDbConn, @NonNull String dbName) {
		super(SqlQueryTypes.CREATE_DATABASE, jdbcDbConn, dbName);
	}

	/**
	 * Create a database.
	 *
	 * Scenario:
	 *
	 * <pre>
	 *  CREATE DATABASE databasename;
	 * </pre>
	 *
	 * NOTE: Only one database can be associated to one
	 * Query Object.
	 *
	 * @return New DB URL for JDBC connection object or NULL
	 * 			if no database has been created.
	 *
	 * NOTE: Using new database URL to create a new JDBC connection
	 * object for further table operations.
	 */
	public String createDatabase() {
		if (!validateDBNameNotNull()) {
			return null;
		}

		/*
		 * Create separate char '/' between DB URL and database name.
		 */
		String separateChar = fJdbcDbConn.getDbUrl().endsWith("/") ? "" : "/";

		/*
		 * Create database via JDBC connection.
		 */
		LOGGER.info("Creating database " + fDBName);
		String sql = fQueryObjectType.sqlQueryType() + " " + fDBName + ";";
		fJdbcDbConn.executeQueryObject(sql);
		LOGGER.info("Database " + fDBName + "is created. Database URL is " + fJdbcDbConn.getDbUrl() + separateChar
				+ fDBName);
		LOGGER.info("Don't forget to create a new JDBC connection object to supply further table"
				+ " operations on this particular database.");
		return fJdbcDbConn.getDbUrl() + separateChar + fDBName;
	}
}
