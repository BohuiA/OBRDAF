package QueryObjectFramework.QueryObjectDBTableStatements;

import java.sql.ResultSet;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

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
 * - Add column:
 * <pre>
 *  ALTER TABLE table_name
 *  ADD column_name datatype;
 * </pre>
 *
 * - Drop column:
 * <pre>
 *  ALTER TABLE table_name
 *  DROP COLUMN column_name;
 * </pre>
 *
 * - Modify column:
 * <pre>
 *  ALTER TABLE table_name
 *  MODIFY COLUMN column_name datatype;
 * </pre>
 *
 * - Add UNIQUE to existing columns
 * <pre>
 *  ALTER TABLE table_name
 *  ADD UNIQUE(column_name);
 * </pre>
 * <pre>
 *  ALTER TABLE table_name
 *  ADD CONSTRAINT UC_<table_name> UNIQUE(column_name1, column_name2, ..);
 *
 *  NOTE: Currently, the UNIQUE name of multiple columns is hard coupled to table
 *  name, as format "UC_<table_name>".
 * </pre>
 *
 * NOTE: At the moment, MYSQL server is fully supported as altering
 * tables.
 *
 * NOTE: At the moment, DROP UNIQUE constraints is not supported.
 *
 * @author Bohui Axelsson
 */
public class QueryObjectAlterTable extends QueryObjectDBTableAbstract {
	/**
	 * Create an ALTER TABLE query object with Table names, columns, and column data types.
	 *
	 * NOTE: For updating UNIQUE constraint on table, setting fUniqueConstraint to true in
	 * fColumnConstraint filed of fTableColumns. Be careful about fUniqueConstraint value, default
	 * value is false for not creating UNIQUE constraint.
	 *
	 * Example:
	 * <pre>
	 *  ALTER TABLE table_name
	 *  ADD UNIQUE (column_name);
	 * </pre>
	 *
	 * For setting multiple columns as UNIQUE, just set the fUniqueConstraints of target columns to TRUE,
	 * Query Object Pattern will take care of rest work.
	 *
	 * @param jdbcDbConn
	 * 			JDBC database connection
	 * @param tableName
	 * 			Table name
	 * @param tableColumns
	 * 			Table column instances
	 */
	public QueryObjectAlterTable(@NonNull JdbcDatabaseConnection jdbcDbConn, @NonNull String tableName,
			@NonNull List<QueryObjectDBTableColumn> tableColumns) {
		super(SqlQueryTypes.ALTER_TABLE, jdbcDbConn, tableName, tableColumns);
	}

	/**
	 * ALTER TABLE - ADD Column
	 * To add a column in a table.
	 *
	 * Scenario:
	 *
	 * <pre>
	 *  ALTER TABLE table_name
	 *  ADD column_name datatype constraint;
	 * </pre>
	 *
	 * <pre>
	 *  ALTER TABLE table_name
	 *  ADD column_name datatype constraint,
	 *  UNIQUE(column_name);
	 * </pre>
	 *
	 * @return ResultSet SQL execution results
	 */
	public ResultSet alterTableAddColumns() {
		if (!validateTableColumnsNotNull()) {
			return null;
		}

		String sql = fQueryObjectType.sqlQueryType() + " " + fTableName + SqlStatementStrings.SQL_DATABASE_ADD + " "
				+ buildColumnsAndColumnDataTypes() + createAppendConstraintForColumns() + ";";
		return fJdbcDbConn.executeQueryObject(sql);
	}

	/**
	 * ALTER TABLE - ADD UNIQUE constraints on existing columns
	 * To add UNIQUE constraints to columns already existing in a table.
	 *
	 * Scenario:
	 *
	 * <pre>
	 *  ALTER TABLE table_name
	 *  ADD UNIQUE (column_name);
	 * </pre>
	 *
	 * <pre>
	 *  ALTER TABLE table_name
	 *  ADD CONSTRAINT UC_<table_name> UNIQUE (column_name1, column_name2, ..);
	 * </pre>
	 *
	 * @return ResultSet SQL execution results
	 */
	public ResultSet alterTableAddUniqueConstraintsOnExistingColumns() {
		if (!validateTableColumnsNotNull()) {
			return null;
		}

		String sql = fQueryObjectType.sqlQueryType() + " " + fTableName + SqlStatementStrings.SQL_DATABASE_ADD + " "
				+ createAppendConstraintForColumns() + ";";
		return fJdbcDbConn.executeQueryObject(sql);
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
		if (!validateTableColumnsNotNull()) {
			return null;
		}

		String sql = fQueryObjectType.sqlQueryType() + " " + fTableName + SqlStatementStrings.SQL_DATABASE_DROP_COLUMN + " "
				+ buildDropColumnsFromTableClaues() + ";";
		return fJdbcDbConn.executeQueryObject(sql);
	}

	/**
	 * Build SQL ALTER TABLE DROP columns string.
	 *
	 * @return SQL ALTER TABLE DROP columns string
	 */
	private String buildDropColumnsFromTableClaues() {
		StringBuilder alterTableDropColumnsClause = new StringBuilder();
		for (QueryObjectDBTableColumn tableColumn : fTableColumns) {
			alterTableDropColumnsClause.append(tableColumn.getColumnName() + ",");
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
		if (!validateTableColumnsNotNull()) {
			return null;
		}

		String sql = fQueryObjectType.sqlQueryType() + " " + fTableName + SqlStatementStrings.SQL_DATABASE_MODIFY_COLUMN + " "
				+ buildColumnsAndColumnDataTypes() + ";";
		return fJdbcDbConn.executeQueryObject(sql);
	}
}
