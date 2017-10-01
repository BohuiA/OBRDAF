package QueryObjectFramework.QueryObjectDBTableStatements;

import java.util.logging.Logger;

import org.eclipse.jdt.annotation.NonNull;

import QueryObjectFramework.CommonClasses.SqlQueryTypes;
import QueryObjectFramework.JdbcDatabaseConnection.JdbcDatabaseConnection;

/**
 * Query object class for DROP TABLE statement. The DROP TABLE statement is used
 * to delete a existing SQL table.
 *
 * <pre>
 * DROP TABLE table_name;
 * </pre>
 *
 * TIPS: Be careful before dropping a table. Deleting a table will result in
 * loss of complete information stored in the table!
 *
 * @author Bohui Axelsson
 */
public class QueryObjectDropTable extends QueryObjectDBTableAbstract {
	private static final Logger LOGGER = Logger.getLogger(QueryObjectDropTable.class.getName());

	/**
	 * Create an DROP TABLE query object with Table names.
	 *
	 * @param jdbcDbConn
	 * 			JDBC database connection
	 * @param tableName
	 * 			Table name
	 */
	public QueryObjectDropTable(@NonNull JdbcDatabaseConnection jdbcDbConn, @NonNull String tableName) {
		super(SqlQueryTypes.DROP_TABLE, jdbcDbConn, tableName);
	}

	/**
	 * Drop a table.
	 *
	 * Scenario:
	 *
	 * <pre>
	 *  DROP TABLE table_name;
	 * </pre>
	 *
	 * NOTE: Only one table name can be associated to one
	 * Query Object.
	 *
	 * NOTE: Be sure all data in the table have been considered, when
	 * a table is dropped, all data in the table will be lost.
	 */
	public void dropDatabase() {
		if (!validateTableNameNotNull()) {
			return;
		}

		LOGGER.info("Droping table " + fTableName);
		String sql = fQueryObjectType.sqlQueryType() + " " + fTableName + ";";
		fJdbcDbConn.executeQueryObject(sql);
		LOGGER.info("Table " + fTableName + "is deleted.");
	}
}
