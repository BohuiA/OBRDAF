package QueryObjectFramework.QueryObjectAbstract;

import java.util.logging.Logger;

import org.eclipse.jdt.annotation.NonNull;

import QueryObjectFramework.CommonClasses.SqlQueryTypes;
import QueryObjectFramework.JdbcDatabaseConnection.JdbcDatabaseConnection;

/**
 * Abstract class for Query Object pattern. Contains basic SQL database
 * operations and interface for customized SQL operations.
 *
 * SQL statement tutorial website link:
 * https://www.w3schools.com/sql/sql_select.asp
 *
 * @author Bohui Axelsson
 */
public class QueryObjectDBAbstract {
	private static final Logger LOGGER = Logger.getLogger(QueryObjectDBAbstract.class.getName());

	public final SqlQueryTypes fQueryObjectType;
	public final @NonNull JdbcDatabaseConnection fJdbcDbConn;
	public String fDBName = null;

	public QueryObjectDBAbstract(SqlQueryTypes queryObjectType, @NonNull JdbcDatabaseConnection jdbcDbConn) {
		fQueryObjectType = queryObjectType;
		fJdbcDbConn = jdbcDbConn;
	}

	public QueryObjectDBAbstract(SqlQueryTypes queryObjectType, @NonNull JdbcDatabaseConnection jdbcDbConn,
			@NonNull String dbName) {
		fQueryObjectType = queryObjectType;
		fJdbcDbConn = jdbcDbConn;
		if (dbName != null) {
			fDBName = dbName;
		}
	}

	public void setDB(@NonNull String db) {
		fDBName = db;
	}

	/**
	 * Validate fDBName is not null.
	 *
	 * @return True if fDBName is null.
	 */
	public boolean validateDBNameNotNull() {
		if (fDBName == null) {
			LOGGER.severe("Failed to create database, database name is missing.");
			return false;
		}
		return true;
	}
}
