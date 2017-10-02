package QueryObjectFramework.QueryObjectDBTableStatements;

import java.sql.ResultSet;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import QueryObjectFramework.CommonClasses.SqlColumnDataType;
import QueryObjectFramework.CommonClasses.SqlDBTableConstraints;
import QueryObjectFramework.CommonClasses.SqlQueryTypes;
import QueryObjectFramework.JdbcDatabaseConnection.JdbcDatabaseConnection;

/**
 * Query object class for CREATE TABLE statement. The CREATE TABLE statement is
 * used to create a new SQL table.
 *
 * <pre>
 * 	CREATE TABLE table_name (
 *   	column1 datatype constraint,
 *   	column2 datatype constraint,
 *   	column3 datatype constraint,
 *  		....
 *	);
 * </pre>
 *
 * The column parameters specify the names of the columns of the table.
 *
 * The data type parameter specifies the type of data the column can hold (e.g.
 * VARCHAR, INT, DATE, etc.).
 *
 * NOTE: At the moment, CREATE TABLE Query Object only supports pre-defined columns way.
 * Create table using another table is not supported right now.
 *
 * @author Bohui Axelsson
 */
public class QueryObjectCreateTable extends QueryObjectDBTableAbstract {
	/**
	 * Create an CREATE TABLE query object with Table names.
	 *
	 * @param jdbcDbConn
	 * 			JDBC database connection
	 * @param tableName
	 * 			Table name
	 */
	public QueryObjectCreateTable(@NonNull JdbcDatabaseConnection jdbcDbConn, @NonNull String tableName) {
		super(SqlQueryTypes.CREATE_TABLE, jdbcDbConn, tableName);
	}

	/**
	 * Create an CREATE TABLE query object with Table names, columns, and column data types.
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
	public QueryObjectCreateTable(@NonNull JdbcDatabaseConnection jdbcDbConn, @NonNull String tableName,
			@NonNull List<String> columns, @NonNull List<SqlColumnDataType> columnDataTypes) {
		super(SqlQueryTypes.CREATE_TABLE, jdbcDbConn, tableName, columns, columnDataTypes);
	}

	/**
	 * Create an CREATE TABLE query object with Table names, columns, column data types and constraints.
	 *
	 * NOTE: The number of constraints should be the same as columns. For those columns that don't have constraint,
	 * placing NULL for placeholder in the list.
	 *
	 * @param jdbcDbConn
	 * 			JDBC database connection
	 * @param tableName
	 * 			Table name
	 * @param columns
	 * 			Table column names
	 * @param columnDataTypes
	 *          Table column data types
	 * @param columnConstraints
	 * 			Table column constraint.
	 */
	public QueryObjectCreateTable(@NonNull JdbcDatabaseConnection jdbcDbConn, @NonNull String tableName,
			@NonNull List<String> columns, @NonNull List<SqlColumnDataType> columnDataTypes,
			@NonNull List<SqlDBTableConstraints> columnConstraints) {
		super(SqlQueryTypes.CREATE_TABLE, jdbcDbConn, tableName, columns, columnDataTypes, columnConstraints);
	}

	/**
	 * Create a table.
	 *
	 * Scenario:
	 *
	 * <pre>
	 *  CREATE TABLE Persons (
	 *    	PersonID INT(2147),
	 *  	LastName VARCHAR(255),
	 *   	FirstName VARCHAR(255),
	 *    	Address VARCHAR(255),
	 *   	City VARCHAR(255)
	 *	);
	 * </pre>
	 *
	 * NOTE: Only one table can be associated to one Query Object.
	 *
	 * NOTE: Column data type will be defined with SqlColumnDataType class,
	 * more details see SqlColumnDataType class.
	 *
	 * @return ResultSet SQL execution results
	 */
	public ResultSet createTable() {
		if (!validateTableNameNotNull() || !validateColumnsSettingsMatching()) {
			return null;
		}

		String sql = fQueryObjectType.sqlQueryType() + " " + fTableName + " (" + buildColumnsAndColumnDataTypes() + checkAndCreateUniqueConstraintColumns() + ")" + ";";
		return fJdbcDbConn.executeQueryObject(sql);
	}

	/**
	 * Build SQL CREATE TABLE columns string.
	 *
	 * @return SQL CREATE TABLE columns string
	 */
	private String buildColumnsAndColumnDataTypes() {
		StringBuilder createTableColumnsClause = new StringBuilder();
		for (int i = 0; i < fColumns.size(); i ++) {
			createTableColumnsClause.append(fColumns.get(i) + " " + fColumnDataTypes.get(i).getSqlColumnDataType() + " "
					+ fColumnConstraints.get(i).getColumnConstraintsString() + ",");
		}
		createTableColumnsClause.deleteCharAt(createTableColumnsClause.length() - 1);
		return createTableColumnsClause.toString();
	}
}
