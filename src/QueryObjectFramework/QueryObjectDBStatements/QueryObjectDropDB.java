package QueryObjectFramework.QueryObjectDBStatements;

import java.util.logging.Logger;

import org.eclipse.jdt.annotation.NonNull;

import QueryObjectFramework.CommonClasses.SqlQueryTypes;
import QueryObjectFramework.JdbcDatabaseConnection.JdbcDatabaseConnection;

/**
 * Query object class for DROP DATABASE statement.
 * The DROP DATABASE statement is used to delete a existing SQL database.
 *
 * <pre>
 * DROP DATABASE testDB;
 * </pre>
 *
 * TIPS: Make sure you have admin privilege before dropping any database.
 * Once a database is created, you can check it in the list of databases
 * with SHOW DATABASE Query Object;
 *
 * @author Bohui Axelsson
 */
public class QueryObjectDropDB extends QueryObjectDBAbstract {
	private static final Logger LOGGER = Logger.getLogger(QueryObjectDropDB.class.getName());

	/**
	 * Create an DROP DATABSE query object with DB names.
	 *
	 * @param jdbcDbConn
	 * 			JDBC database connection
	 * @param dbName
	 * 			Database name
	 */
	public QueryObjectDropDB(@NonNull JdbcDatabaseConnection jdbcDbConn, @NonNull String dbName) {
		super(SqlQueryTypes.DROP_DATABASE, jdbcDbConn, dbName);
	}

	/**
	 * Drop a database.
	 *
	 * Scenario:
	 *
	 * <pre>
	 *  DROP DATABASE databasename;
	 * </pre>
	 *
	 * NOTE: Only one database can be associated to one
	 * Query Object.
	 *
	 * NOTE: Be sure all data in the database have been considered, when
	 * a database is dropped, all data in the database will be lost.
	 */
	public void dropDatabase() {
		if (!validateDBNameNotNull()) {
			return;
		}

		LOGGER.info("Droping database " + fDBName);
		String sql = fQueryObjectType.sqlQueryType() + " " + fDBName + ";";
		fJdbcDbConn.executeQueryObject(sql);
		LOGGER.info("Database " + fDBName + "is deleted.");
	}
}
