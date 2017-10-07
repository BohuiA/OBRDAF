package QueryObjectFramework.QueryObjectDBTableStatements;

import java.util.List;
import java.util.logging.Logger;

import org.eclipse.jdt.annotation.NonNull;

import QueryObjectFramework.CommonClasses.SqlQueryTypes;
import QueryObjectFramework.JdbcDatabaseConnection.JdbcDatabaseConnection;

/**
 * Abstract class for Query Object pattern. Contains basic SQL database
 * table operations and interface for customized SQL table operations.
 *
 * Including CREATE TABLE, DROP TABLE, ALTER TABLE operations.
 *
 * SQL statement tutorial website link:
 * https://www.w3schools.com/sql/sql_select.asp
 *
 * @author Bohui Axelsson
 */
public class QueryObjectDBTableAbstract {
	private static final Logger LOGGER = Logger.getLogger(QueryObjectDBTableAbstract.class.getName());

	protected final SqlQueryTypes fQueryObjectType;
	protected final @NonNull JdbcDatabaseConnection fJdbcDbConn;
	protected final @NonNull QueryObjectDBTableColumnBuilder  fColumnIndex = new QueryObjectDBTableColumnBuilder();
	protected String fTableName = "";

	public QueryObjectDBTableAbstract(SqlQueryTypes queryObjectType, @NonNull JdbcDatabaseConnection jdbcDbConn) {
		fQueryObjectType = queryObjectType;
		fJdbcDbConn = jdbcDbConn;
	}

	public QueryObjectDBTableAbstract(SqlQueryTypes queryObjectType, @NonNull JdbcDatabaseConnection jdbcDbConn,
			@NonNull String tableName) {
		fQueryObjectType = queryObjectType;
		fJdbcDbConn = jdbcDbConn;
		fTableName = tableName;
	}

	public QueryObjectDBTableAbstract(SqlQueryTypes queryObjectType, @NonNull JdbcDatabaseConnection jdbcDbConn,
			@NonNull String tableName, @NonNull List<QueryObjectDBTableColumn> tableColumns) {
		fQueryObjectType = queryObjectType;
		fJdbcDbConn = jdbcDbConn;
		fTableName = tableName;
		fColumnIndex.buildDBTableColumnIndex(tableColumns);
	}

	/**
	 * Create columns setting string with table name.
	 *
	 * @return A full columns setting string
	 */
	protected String buildFullColumnSettingString() {
		return fColumnIndex.buildColumnWithNameAndDataTypeAndConstraints(fTableName);
	}

	/**
	 * Create columns setting string with appending constraints.
	 * - UNIQUE
	 * - PRIMARY KEY
	 *
	 * @return A full columns setting string
	 */
	protected String buildAppendingColumnSettingString() {
		return fColumnIndex.buildAppendConstraintsForColumns(fTableName);
	}


	/**
	 * Validate there are no NULL item in the table column list.
	 *
	 * @return True if all items in fTableColumns are not NULL.
	 */
	protected boolean validateTableColumnsNotNull() {
		return fColumnIndex.validateTableClolumsNotNull();
	}
}
