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
 * <example>
 *  ALTER TABLE table_name
 *  ADD column_name datatype;
 * </example>
 *
 * - Drop column:
 * <example>
 *  ALTER TABLE table_name
 *  DROP COLUMN column_name;
 * </example>
 *
 * - Modify column:
 * <example>
 *  ALTER TABLE table_name
 *  MODIFY COLUMN column_name datatype;
 * </example>
 *
 * - Add UNIQUE to existing columns
 * <example>
 *  ALTER TABLE table_name
 *  ADD UNIQUE(column_name);
 * </example>
 * <example>
 *  ALTER TABLE table_name
 *  ADD CONSTRAINT UC_<table_name> UNIQUE(column_name1, column_name2, ..);
 *
 *  NOTE: Currently, the UNIQUE name of multiple columns is hard coupled to table
 *  name, as format "UC_<table_name>".
 * </example>
 *
 * NOTE: At the moment, MYSQL server is fully supported as altering
 * tables.
 *
 * NOTE: At the moment, customized DROP UNIQUE constraints is not supported.
 * DROP UNIQUE constraints will only drops UC_<table_name> UNIQUE ID.
 * <example>
 *  ALTER TABLE Persons
 *  DROP INDEX UC_Person;
 * </example>
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
	 * <example>
	 *  ALTER TABLE table_name
	 *  ADD UNIQUE (column_name);
	 * </example>
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
	 * <example>
	 *  ALTER TABLE table_name
	 *  ADD column_name datatype constraint;
	 * </example>
	 *
	 * <example>
	 *  ALTER TABLE table_name
	 *  ADD column_name datatype constraint,
	 *  UNIQUE(column_name);
	 * </example>
	 *
	 * @return ResultSet SQL execution results
	 */
	public ResultSet alterTableAddColumns() {
		if (!validateTableColumnsNotNull()) {
			return null;
		}

		String sql = fQueryObjectType.sqlQueryType() + " " + fTableName + " " + SqlStatementStrings.SQL_DATABASE_ADD + " "
				+ buildColumnSettingString() + ";";
		return fJdbcDbConn.executeQueryObject(sql);
	}

	/**
	 * ALTER TABLE - ADD UNIQUE constraints on existing columns
	 * To add UNIQUE constraints to columns already existing in a table.
	 *
	 * Scenario:
	 *
	 * <example>
	 *  ALTER TABLE table_name
	 *  ADD UNIQUE (column_name);
	 * </example>
	 *
	 * <example>
	 *  ALTER TABLE table_name
	 *  ADD CONSTRAINT UC_<table_name> UNIQUE (column_name1, column_name2, ..);
	 * </example>
	 *
	 * @return ResultSet SQL execution results
	 */
	public ResultSet alterTableAddUniqueConstraintsOnExistingColumns() {
		if (!validateTableColumnsNotNull()) {
			return null;
		}

		String sql = fQueryObjectType.sqlQueryType() + " " + fTableName + " " + SqlStatementStrings.SQL_DATABASE_ADD + " "
				+ createUniqueConstraintsForColumns() + ";";
		return fJdbcDbConn.executeQueryObject(sql);
	}

	/**
	 * Create a UNIQUE constraint string.
	 *
	 * @return UNIQUE constraint string.
	 */
	private String createUniqueConstraintsForColumns() {
		StringBuilder uniqueCoulmnClause = new StringBuilder("");
		int uniqueColumnsAmount = 0;
		for (QueryObjectDBTableColumn tableColumn : fTableColumns) {
			String columnConstraints = tableColumn.getColumnConstraint().getColumnConstraintsString();
			if (columnConstraints.contains(SqlStatementStrings.SQL_DATABASE_UNIQUE)) {
				uniqueColumnsAmount ++;
				uniqueCoulmnClause.append(tableColumn.getColumnName() + ",");
			}
		}

		/*
		 * To create UNQIUE clause regarding to the amount of UNIQUE
		 * columns.
		 */
		if (uniqueColumnsAmount == 1) {
			uniqueCoulmnClause.deleteCharAt(uniqueCoulmnClause.length() - 1);
			uniqueCoulmnClause.insert(0, SqlStatementStrings.SQL_DATABASE_UNIQUE + "(");
			uniqueCoulmnClause.append(")");
		} else if (uniqueColumnsAmount > 1) {
			uniqueCoulmnClause.deleteCharAt(uniqueCoulmnClause.length() - 1);
			uniqueCoulmnClause.insert(0, SqlStatementStrings.SQL_DATABASE_CONSTRAINT
					+ SqlStatementStrings.SQL_DATABASE_MULTIPLE_UNIQUE_COLUMNS + this.fTableName + " "
					+ SqlStatementStrings.SQL_DATABASE_UNIQUE + "(");
			uniqueCoulmnClause.append(")");
		}

		return uniqueCoulmnClause.toString();
	}

	/**
	 * ALTER TABLE - DROP COLUMN
	 * To delete a column in a table.
	 *
	 * Scenario:
	 *
	 * <example>
	 *  ALTER TABLE table_name
	 *  DROP COLUMN column_name;
	 * </example>
	 *
	 * @return ResultSet SQL execution results
	 */
	public ResultSet alterTableDropColumns() {
		if (!validateTableColumnsNotNull()) {
			return null;
		}

		String sql = fQueryObjectType.sqlQueryType() + " " + fTableName + " " + SqlStatementStrings.SQL_DATABASE_DROP_COLUMN + " "
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
	 * ALTER TABLE - DROP UNIQUE constraints on existing table
	 * To drop UNIQUE constraints on an existing table.
	 *
	 * Scenario:
	 *
	 * <example>
	 *  ALTER TABLE Person
	 *  DROP INDEX UC_Person;
	 * </example>
	 *
	 * @return ResultSet SQL execution results
	 */
	public ResultSet alterTableDropUniqueConstraintsOnExistingTable() {
		String sql = fQueryObjectType.sqlQueryType() + " " + fTableName + " " + SqlStatementStrings.SQL_DATABASE_DROP_INDEX_UNIQUE
				+ " " + SqlStatementStrings.SQL_DATABASE_MULTIPLE_UNIQUE_COLUMNS + fTableName + ";";
		return fJdbcDbConn.executeQueryObject(sql);
	}

	/**
	 * ALTER TABLE - ALTER/MODIFY COLUMN
	 * To change the data type of a column in a table.
	 *
	 * Scenario:
	 *
	 * <example>
	 *  ALTER TABLE table_name
	 *  MODIFY COLUMN column_name datatype;
	 * </example>
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

	/**
	 * Build SQL TABLE columns and data types string.
	 *
	 * <example>
	 * 	column_name1 datatype1, column_name2 datatype2, ...
	 * </example>
	 *
	 * @return SQL columns and data types string
	 */
	private String buildColumnsAndColumnDataTypes() {
		StringBuilder columnAndDatatypeClause = new StringBuilder();
		for (QueryObjectDBTableColumn tableColumn : fTableColumns) {
			columnAndDatatypeClause.append(
					tableColumn.getColumnName() + " " + tableColumn.getColumnDataType().getSqlColumnDataType() + ",");
		}
		columnAndDatatypeClause.deleteCharAt(columnAndDatatypeClause.length() - 1);
		return columnAndDatatypeClause.toString();

	}
}
