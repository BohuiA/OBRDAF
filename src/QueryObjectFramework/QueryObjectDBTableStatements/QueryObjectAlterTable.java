package QueryObjectFramework.QueryObjectDBTableStatements;

import java.sql.ResultSet;
import java.util.List;
import java.util.logging.Logger;

import org.eclipse.jdt.annotation.NonNull;

import QueryObjectFramework.CommonClasses.SqlColumnDataType;
import QueryObjectFramework.CommonClasses.SqlQueryTypes;
import QueryObjectFramework.CommonClasses.SqlStatementStrings;
import QueryObjectFramework.JdbcDatabaseConnection.JdbcDatabaseConnection;

/**
 * Query object class for ALTER TABLE statement.
 *
 * The ALTER TABLE statement is used to add, delete, or modify columns in an
 * existing table.
 *
 * The ALTER TABLE statement is also used to add and drop various constraints on
 * an existing table.
 *
 * Example:
 *
 * <pre>
 *  ALTER TABLE table_name
 *  ADD column_name datatype;
 * </pre>
 *
 * <pre>
 *  ALTER TABLE table_name
 *  DROP COLUMN column_name;
 * </pre>
 *
 * <pre>
 *  ALTER TABLE table_name
 *  MODIFY COLUMN column_name datatype;
 * </pre>
 *
 * NOTE: At the moment, MYSQL server is fully supported as altering
 * tables.
 *
 * @author Bohui Axelsson
 */
public class QueryObjectAlterTable extends QueryObjectDBTableAbstract {
	private static final Logger LOGGER = Logger.getLogger(QueryObjectAlterTable.class.getName());

	/**
	 * Create an ALTER TABLE query object with Table names, columns, and column data types.
	 *
	 * Query Object ALTER TABLE for adding/modifying columns into Table.
	 *
	 * @param jdbcDbConn
	 * 			JDBC database connection
	 * @param tableName
	 * 			Table name
	 * @param columns
	 * 			Table column names
	 * @param columnDataTypes
	 *          Table column data types
	 */
	public QueryObjectAlterTable(@NonNull JdbcDatabaseConnection jdbcDbConn, @NonNull String tableName,
			@NonNull List<String> columns, @NonNull List<SqlColumnDataType> columnDataTypes) {
		super(SqlQueryTypes.ALTER_TABLE, jdbcDbConn, tableName, columns, columnDataTypes);
	}

	/**
	 * Create an ALTER TABLE query object with Table names, columns, and column data types.
	 *
	 * Query Object ALTER TABLE for dropping columns into Table.
	 *
	 * @param jdbcDbConn
	 * 			JDBC database connection
	 * @param tableName
	 * 			Table name
	 * @param columns
	 * 			Table column names
	 */
	public QueryObjectAlterTable(@NonNull JdbcDatabaseConnection jdbcDbConn, @NonNull String tableName,
			@NonNull List<String> columns) {
		super(SqlQueryTypes.ALTER_TABLE, jdbcDbConn, tableName, columns);
	}

	/**
	 * ALTER TABLE - ADD Column
	 * To add a column in a table.
	 *
	 * Scenario:
	 *
	 * <pre>
	 *  CALTER TABLE table_name
	 *  ADD column_name datatype;
	 * </pre>
	 *
	 * @return ResultSet SQL execution results
	 */
	public ResultSet alterTableAddColumns() {
		if (!validateTableNameNotNull() || !validateColumnsAndDataTypesNotNullAndMatching()) {
			return null;
		}

		String sql = fQueryObjectType.sqlQueryType() + " " + fTableName + SqlStatementStrings.SQL_DATABASE_ADD + " "
				+ buildAddColumnsIntoTableClaues() + ";";
		return fJdbcDbConn.executeQueryObject(sql);
	}

	/**
	 * Build SQL ALTER TABLE ADD columns string.
	 *
	 * @return SQL ALTER TABLE ADD columns string
	 */
	private String buildAddColumnsIntoTableClaues() {
		StringBuilder alterTableAddColumnsClause = new StringBuilder();
		for (int i = 0; i < fColumns.size(); i ++) {
			alterTableAddColumnsClause.append(fColumns.get(i) + " " + fColumnDataTypes.get(i).getSqlColumnDataType() + ",");
		}
		alterTableAddColumnsClause.deleteCharAt(alterTableAddColumnsClause.length() - 1);
		return alterTableAddColumnsClause.toString();
	}

	/**
	 * ALTER TABLE - DROP COLUMN
	 * To delete a column in a table.
	 *
	 * Scenario:
	 *
	 * <pre>
	 *  ALTER TABLE table_name
	 *  DROP COLUMN column_name;
	 * </pre>
	 *
	 * @return ResultSet SQL execution results
	 */
	public ResultSet alterTableDropColumns() {
		if (!validateTableNameNotNull() || !validateColumnsNotNull()) {
			return null;
		}

		String sql = fQueryObjectType.sqlQueryType() + " " + fTableName + SqlStatementStrings.SQL_DATABASE_DROP_COLUMN + " "
				+ buildDropColumnsFromTableClaues() + ";";
		return fJdbcDbConn.executeQueryObject(sql);
	}

	/**
	 * Validate columns is not null.
	 *
	 * @return True if columns is not null.
	 */
	private boolean validateColumnsNotNull() {
		if (fColumns == null) {
			LOGGER.severe("Failed to operate table operation, column names are missing.");
			return false;
		}
		for (String column : fColumns) {
			if (column == null) {
				LOGGER.severe("Failed to operate table operation, column item is null.");
				return false;
			}
		}
		return true;
	}

	/**
	 * Build SQL ALTER TABLE DROP columns string.
	 *
	 * @return SQL ALTER TABLE DROP columns string
	 */
	private String buildDropColumnsFromTableClaues() {
		StringBuilder alterTableDropColumnsClause = new StringBuilder();
		for (int i = 0; i < fColumns.size(); i ++) {
			alterTableDropColumnsClause.append(fColumns.get(i) + ",");
		}
		alterTableDropColumnsClause.deleteCharAt(alterTableDropColumnsClause.length() - 1);
		return alterTableDropColumnsClause.toString();
	}

	/**
	 * ALTER TABLE - ALTER/MODIFY COLUMN
	 * To change the data type of a column in a table.
	 *
	 * Scenario:
	 *
	 * <pre>
	 *  ALTER TABLE table_name
	 *  MODIFY COLUMN column_name datatype;
	 * </pre>
	 *
	 * @return ResultSet SQL execution results
	 */
	public ResultSet alterTableModifyColumns() {
		if (!validateTableNameNotNull() || !validateColumnsAndDataTypesNotNullAndMatching()) {
			return null;
		}

		String sql = fQueryObjectType.sqlQueryType() + " " + fTableName + SqlStatementStrings.SQL_DATABASE_MODIFY_COLUMN + " "
				+ buildAddColumnsIntoTableClaues() + ";";
		return fJdbcDbConn.executeQueryObject(sql);
	}
}
