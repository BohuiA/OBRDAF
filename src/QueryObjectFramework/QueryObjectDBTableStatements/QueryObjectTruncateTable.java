package QueryObjectFramework.QueryObjectDBTableStatements;

import java.util.logging.Logger;

import org.eclipse.jdt.annotation.NonNull;

import QueryObjectFramework.CommonClasses.SqlQueryTypes;
import QueryObjectFramework.JdbcDatabaseConnection.JdbcDatabaseConnection;

/**
 * Query object class for TRUNCATE TABLE statement. The TRUNCATE TABLE statement
 * is used to delete the data inside a table, but not the table itself.
 *
 * <pre>
 *  TRUNCATE TABLE table_name;
 * </pre>
 *
 * @author Bohui Axelsson
 */
public class QueryObjectTruncateTable extends QueryObjectDBTableAbstract {
	private static final Logger LOGGER = Logger.getLogger(QueryObjectTruncateTable.class.getName());

	/**
	 * Create an DROP TABLE query object with Table names.
	 *
	 * @param jdbcDbConn
	 * 			JDBC database connection
	 * @param tableName
	 * 			Table name
	 */
	public QueryObjectTruncateTable(@NonNull JdbcDatabaseConnection jdbcDbConn, @NonNull String tableName) {
		super(SqlQueryTypes.TRUNCATE_TABLE, jdbcDbConn, tableName);
	}

	/**
	 * Truncate a table.
	 *
	 * Scenario:
	 *
	 * <pre>
	 *  TRUNCATE TABLE table_name;
	 * </pre>
	 *
	 * NOTE: Only one table name can be associated to one
	 * Query Object.
	 */
	public void truncateTable() {
		if (!validateTableNameNotNull()) {
			return;
		}

		LOGGER.info("Truncate table " + fTableName);
		String sql = fQueryObjectType.sqlQueryType() + " " + fTableName + ";";
		fJdbcDbConn.executeQueryObject(sql);
		LOGGER.info("Table " + fTableName + "is truncated.");
	}
}
