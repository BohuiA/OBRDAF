package QueryObjectFramework.QueryObjectStatements;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.eclipse.jdt.annotation.NonNull;

import QueryObjectFramework.CommonClasses.SqlQueryTypes;
import QueryObjectFramework.CommonClasses.SqlStatementStrings;
import QueryObjectFramework.JdbcDatabaseConnection.JdbcDatabaseConnection;
import QueryObjectFramework.QueryObjectAbstract.QueryObjectAbstract;

/**
 * Query object class for Insert statement.
 *
 * @author Bohui Axelsson
 */
public class QueryObjectInsertInto extends QueryObjectAbstract {
	private static final Logger LOGGER = Logger.getLogger(QueryObjectInsertInto.class.getName());

	/*
	 * Insert SQL statement specific operation settings
	 */
	private final @NonNull List<Object> fInsertValues = new ArrayList<>();

	/**
	 * Create a INSERT INTO query object with only JDBC connection.
	 *
	 * @param jdbcDbConn
	 * 			JDBC database connection
	 */
	public QueryObjectInsertInto(@NonNull JdbcDatabaseConnection jdbcDbConn) {
		super(SqlQueryTypes.INSERT, jdbcDbConn);
	}

	/**
	 * Create a INSERT INTO query object.
	 *
	 * Example:
	 *
	 * <pre>
	 * 	INSERT INTO table_name
     *  VALUES (value1, value2, value3, ...);
	 * </pre>
	 *
	 * @param jdbcDbConn
	 * 			JDBC database connection
	 * @param tables
	 * 			Names of table for inserting, should be only one
	 * @param insertValues
	 * 			Inserting values for INSERT. Without columns set,
	 * 			the order of insert values should be matching excatly
	 * 			with real database table.
	 */
	public QueryObjectInsertInto(@NonNull JdbcDatabaseConnection jdbcDbConn, @NonNull List<String> tables,
			@NonNull List<Object> insertValues) {
		super(SqlQueryTypes.INSERT, jdbcDbConn, tables, null, null);
		if (insertValues != null) {
			fInsertValues.addAll(insertValues);
		}
	}

	/**
	 * Create a INSERT INTO query object.
	 *
	 * Example:
	 *
	 * <pre>
	 * INSERT INTO table_name (column1, column2, column3, ...)
     * VALUES (value1, value2, value3, ...);
	 * </pre>
	 *
	 * @param jdbcDbConn
	 * 			JDBC database connection
	 * @param tables
	 * 			Names of table for inserting, should be only one
	 * @param columns
	 * 			Names of columns for inserting.
	 * @param insertValues
	 * 			Inserting values for INSERT. Without columns set,
	 * 			the order of insert values should be matching exactly
	 * 			with real database table.
	 */
	public QueryObjectInsertInto(@NonNull JdbcDatabaseConnection jdbcDbConn, @NonNull List<String> tables,
			@NonNull List<String> columns, @NonNull List<Object> insertValues) {
		super(SqlQueryTypes.INSERT, jdbcDbConn, tables, columns, null);
		if (insertValues != null) {
			fInsertValues.addAll(insertValues);
		}
	}

	/**
	 *
	 * @return
	 */
	public ResultSet insertIntoTableWithValues() {
		if (!validateTableAmountAndValuesNotEmpty()) {
			return null;
		}

		String sql = fQueryObjectType.sqlQueryType() + " " + fTables.get(0)
				 + " " + SqlStatementStrings.SQL_TABLE_VALUES + " (" + buildSqlInsterValuesClause() + " );";
		return fJdbcDbConn.executeQueryObject(sql);
	}

	/**
	 * Build SQL insert values string contains all inserting values.
	 *
	 * @return SQL insert values string
	 */
	private String buildSqlInsterValuesClause() {
		StringBuilder insertValuesClause = new StringBuilder();
		for (Object insertValue : fInsertValues) {
			if (insertValue instanceof String) {
				insertValuesClause.append("'" + insertValue + "'" + ",");
			} else {
				insertValuesClause.append(insertValue + ",");
			}
		}
		insertValuesClause.deleteCharAt(insertValuesClause.length() - 1);
		return insertValuesClause.toString();
	}

	/**
	 * Validate fTables list should only contain one table name, meanwhile,
	 * fInsertValues list should not be empty.
	 *
	 * @return True if fTables contains one table and fInsertValues is not empty.
	 */
	public boolean validateTableAmountAndValuesNotEmpty() {
		if (fTables.isEmpty()) {
			LOGGER.severe("Failed to insert values into table, table name is missing.");
			return false;
		}
		if (fTables.size() != 1) {
			LOGGER.severe("Failed to insert values into table, more than one table are provided.");
			return false;
		}
		if (fInsertValues.isEmpty()) {
			LOGGER.severe("Failed to insert values into table, inserting values are missing.");
			return false;
		}
		return true;
	}
}
