package QueryObjectFramework.statementClasses;

import java.sql.ResultSet;
import java.util.List;
import java.util.logging.Logger;

import QueryObjectFramework.common.SqlQueryTypes;
import QueryObjectFramework.common.SqlStatementStrings;
import QueryObjectFramework.jdbc.JdbcDatabaseConnection;
import QueryObjectFramework.queryObjectBase.QueryObjectAbstract;

/**
 * Query object class for Select statement.
 *
 * @author Bohui Axelsson
 */
public class QueryObjectSelect extends QueryObjectAbstract {
	private static final Logger LOGGER = Logger.getLogger(QueryObjectSelect.class.getName());

	public QueryObjectSelect(JdbcDatabaseConnection jdbcDbConn) {
		super(SqlQueryTypes.SELECT, jdbcDbConn);
	}

	public QueryObjectSelect(JdbcDatabaseConnection jdbcDbConn, List<String> tables, List<String> columns,
			List<String> fields, List<Object> values, List<String> operators, List<String> conditions) {
		super(SqlQueryTypes.SELECT, jdbcDbConn, tables, columns, fields, values, operators, conditions);
	}

	/**
	 * Select all columns from the initialized table.
	 *
	 * Scenario:
	 *
	 * <pre>
	 *  SELECT * FROM table_name;
	 * </pre>
	 *
	 * @return ResultSet SQL execution results
	 */
	public ResultSet selectAll() {
		if (fTables.isEmpty()) {
			LOGGER.severe("Failed to select all fileds from table, table name is missing.");
			return null;
		}

		String sql = SqlQueryTypes.SELECT.sqlQueryType() + " * " + SqlStatementStrings.SQL_TABLE_FROM + " "
				+ fTables.get(0) + ";";
		return fJdbcDbConn.executeQueryObject(sql);
	}

	/**
	 * Select all distinct columns from the initialized table.
	 *
	 * Scenario:
	 *
	 * <pre>
	 *  SELECT DISTINCT * FROM table_name;
	 * </pre>
	 *
	 * @return ResultSet SQL execution results
	 */
	public ResultSet selectAllDistinct() {
		if (fTables.isEmpty()) {
			LOGGER.severe("Failed to select all fileds from table, table name is missing.");
			return null;
		}

		String sql = SqlQueryTypes.SELECT.sqlQueryType() + " " + SqlStatementStrings.SQL_TABLE_DISTINCT + " * "
				+ SqlStatementStrings.SQL_TABLE_FROM + " " + fTables.get(0) + ";";
		return fJdbcDbConn.executeQueryObject(sql);
	}

	/**
	 * Select specific columns from the initialized table.
	 *
	 * Scenario:
	 *
	 * <pre>
	 *  SELECT column1, column2, .. FROM table_name;
	 * </pre>
	 *
	 * @return ResultSet SQL execution results
	 */
	public ResultSet selectColumns() {
		if (checkEmptyTableAndColumns()) {
			return null;
		}

		String sql = SqlQueryTypes.SELECT.sqlQueryType() + " " + buildSqlColumnsString() + " "
				+ SqlStatementStrings.SQL_TABLE_FROM + " " + fTables.get(0) + ";";
		return fJdbcDbConn.executeQueryObject(sql);
	}

	/**
	 * Build SQL columns string contains all target column names.
	 *
	 * @return SQL columns string
	 */
	private String buildSqlColumnsString() {
		StringBuilder colunms = new StringBuilder();
		for (String filedName : fColumns) {
			colunms.append(filedName + ",");
		}
		colunms.deleteCharAt(colunms.length() - 1);
		return colunms.toString();
	}

	/**
	 * Select specific distinct columns from the initialized table.
	 *
	 * Scenario:
	 *
	 * <pre>
	 *  SELECT DISTINCT column1, column2, .. FROM table_name;
	 * </pre>
	 *
	 * @return ResultSet SQL execution results
	 */
	public ResultSet selectColumnsDistinct() {
		if (checkEmptyTableAndColumns()) {
			return null;
		}

		String sql = SqlQueryTypes.SELECT.sqlQueryType() + " " + SqlStatementStrings.SQL_TABLE_DISTINCT + " "
				+ buildSqlColumnsString() + " " + SqlStatementStrings.SQL_TABLE_FROM + " " + fTables.get(0) + ";";
		return fJdbcDbConn.executeQueryObject(sql);
	}

	/**
	 * Select all columns from the initialized table with WHERE fields filters.
	 *
	 * Scenario:
	 *
	 * <pre>
	 *  SELECT * FROM table_name WHERE condition 1 field1 operator1 value1 condition2 ...;
	 * </pre>
	 *
	 * If first condition is empty string "", then the first condition will be not
	 * inserted. Example, ... WHERE country='USA' AND name='Bohui';
	 *
	 * @return ResultSet SQL execution results
	 */
	public ResultSet selectAllByFileds() {
		if (!validateFieldsFiltering()) {
			return null;
		}

		String sql = SqlQueryTypes.SELECT.sqlQueryType() + " * " + SqlStatementStrings.SQL_TABLE_FROM + " "
				+ fTables.get(0) + SqlStatementStrings.SQL_TABLE_WHERE + " " + buildSqlWhereClause() + ";";
		return fJdbcDbConn.executeQueryObject(sql);
	}

	/**
	 * Build SQL WHERE clause string from fConditions, fFields,
	 * fOperators, and fValues lists.
	 *
	 * @return SQL WHERE string
	 */
	private String buildSqlWhereClause() {
		StringBuilder whereClause = new StringBuilder();
		for (int i = 0; i < fFields.size(); i++) {
			if (fValues.get(i) instanceof String) {
				whereClause.append(fConditions.get(i) + " " + fFields.get(i) + fOperators.get(i) + "'" + fValues.get(i) + "'");
			} else {
				whereClause.append(fConditions.get(i) + " " + fFields.get(i) + fOperators.get(i) + fValues.get(i));
			}
		}
		return whereClause.toString();
	}

	/**
	 * Select all columns from the initialized table with WHERE fields filters.
	 *
	 * Scenario:
	 *
	 * <pre>
	 *  SELECT DISTINCT * FROM table_name WHERE condition1 field1 operator1 value1 condition2 field2 operator2 value2...;
	 * </pre>
	 *
	 * @return ResultSet SQL execution results
	 */
	public ResultSet selectAllDistinctByFileds() {
		if (!validateFieldsFiltering()) {
			return null;
		}

		String sql = SqlQueryTypes.SELECT.sqlQueryType() + " " + SqlStatementStrings.SQL_TABLE_DISTINCT + " * "
				+ SqlStatementStrings.SQL_TABLE_FROM + " " + fTables.get(0) + SqlStatementStrings.SQL_TABLE_WHERE + " "
				+ buildSqlWhereClause() + ";";
		return fJdbcDbConn.executeQueryObject(sql);
	}

	/**
	 * Select specific columns from the initialized table with WHERE fields filters.
	 *
	 * Scenario:
	 *
	 * <pre>
	 *  SELECT column1, column2, .. FROM table_name WHERE condition1 field1 operator1 value1 ...;
	 * </pre>
	 *
	 * @return ResultSet SQL execution results
	 */
	public ResultSet selectColumnsByFileds() {
		if (!validateFieldsFiltering()) {
			return null;
		}

		String sql = SqlQueryTypes.SELECT.sqlQueryType() + " " + buildSqlColumnsString() + " "
				+ SqlStatementStrings.SQL_TABLE_FROM + " " + fTables.get(0) + SqlStatementStrings.SQL_TABLE_WHERE + " "
				+ buildSqlWhereClause() + ";";
		return fJdbcDbConn.executeQueryObject(sql);
	}

	/**
	 * Select specific columns from the initialized table with WHERE fields filters.
	 *
	 * Scenario:
	 *
	 * <pre>
	 *  SELECT DISTINCT column1, column2, .. FROM table_name WHERE Condition1 field1 operator1 value1 ...;
	 * </pre>
	 *
	 * @return ResultSet SQL execution results
	 */
	public ResultSet selectColumnsDistinctByFileds() {
		if (!validateFieldsFiltering()) {
			return null;
		}

		String sql = SqlQueryTypes.SELECT.sqlQueryType() + " " + SqlStatementStrings.SQL_TABLE_DISTINCT + " "
				+ buildSqlColumnsString() + " " + SqlStatementStrings.SQL_TABLE_FROM + " " + fTables.get(0)
				+ SqlStatementStrings.SQL_TABLE_WHERE + " " + buildSqlWhereClause() + ";";
		return fJdbcDbConn.executeQueryObject(sql);
	}

	/**
	 * Select and count all columns from the initialized table.
	 *
	 * Scenario:
	 *
	 * <pre>
	 *  SELECT COUNT(*) FROM table_name;
	 * </pre>
	 *
	 * @return ResultSet SQL execution results
	 */
	public ResultSet selectCountAll() {
		if (fTables.isEmpty()) {
			LOGGER.severe("Failed to select all fileds from table, table name is missing.");
			return null;
		}

		String sql = SqlQueryTypes.SELECT.sqlQueryType() + " " + SqlStatementStrings.SQL_TABLE_COUNT + "(" + "*" + ") "
				+ SqlStatementStrings.SQL_TABLE_FROM + " " + fTables.get(0) + ";";
		return fJdbcDbConn.executeQueryObject(sql);
	}

	/**
	 * Select and count all distinct columns from the initialized table.
	 *
	 * Scenario:
	 *
	 * <pre>
	 *  SELECT COUNT(DISTINCT *) FROM table_name;
	 * </pre>
	 *
	 * @return ResultSet SQL execution results
	 */
	public ResultSet selectCountDistinctAll() {
		if (fTables.isEmpty()) {
			LOGGER.severe("Failed to select all fileds from table, table name is missing.");
			return null;
		}

		String sql = SqlQueryTypes.SELECT.sqlQueryType() + " " + SqlStatementStrings.SQL_TABLE_COUNT + "("
				+ SqlStatementStrings.SQL_TABLE_DISTINCT + " *" + ") " + SqlStatementStrings.SQL_TABLE_FROM + " "
				+ fTables.get(0) + ";";
		return fJdbcDbConn.executeQueryObject(sql);
	}

	/**
	 * Select and count specific column from the initialized table.
	 *
	 * Scenario:
	 *
	 * <pre>
	 *  SELECT COUNT(column) FROM table_name;
	 * </pre>
	 *
	 * @return ResultSet SQL execution results
	 */
	public ResultSet selectCountColumn() {
		if(checkEmptyTableAndColumns()) {
			return null;
		}

		String sql = SqlQueryTypes.SELECT.sqlQueryType() + " " + SqlStatementStrings.SQL_TABLE_DISTINCT + "("
				+ fColumns.get(0) + ") " + SqlStatementStrings.SQL_TABLE_FROM + " " + fTables.get(0) + ";";
		return fJdbcDbConn.executeQueryObject(sql);
	}

	/**
	 * Select and count all column from the initialized table with WHERE fields
	 * filters.
	 *
	 * Scenario:
	 *
	 * <pre>
	 *  SELECT COUNT(*) FROM table_name WHERE condition1 field1 operator1 value1 ...;
	 * </pre>
	 *
	 * @return ResultSet SQL execution results
	 */
	public ResultSet selectCountAllByFileds() {
		if (!validateFieldsFiltering()) {
			return null;
		}

		String sql = SqlQueryTypes.SELECT.sqlQueryType() + " " + SqlStatementStrings.SQL_TABLE_COUNT + "(" + "*" + ") "
				+ SqlStatementStrings.SQL_TABLE_FROM + " " + fTables.get(0) + SqlStatementStrings.SQL_TABLE_WHERE + " "
				+ buildSqlWhereClause() + ";";
		return fJdbcDbConn.executeQueryObject(sql);
	}

	/**
	 * Select and count all columns from the initialized table with WHERE fields
	 * filters.
	 *
	 * Scenario:
	 *
	 * <pre>
	 *  SELECT COUNT(DISTINCT *) FROM table_name WHERE condition1 field1 operator1 value1 ...;
	 * </pre>
	 *
	 * @return ResultSet SQL execution results
	 */
	public ResultSet selectCountAllDistinctByFileds() {
		if (!validateFieldsFiltering()) {
			return null;
		}

		String sql = SqlQueryTypes.SELECT.sqlQueryType() + " " + SqlStatementStrings.SQL_TABLE_COUNT + "("
				+ SqlStatementStrings.SQL_TABLE_DISTINCT + " *) " + SqlStatementStrings.SQL_TABLE_FROM + " "
				+ fTables.get(0) + SqlStatementStrings.SQL_TABLE_WHERE + " " + buildSqlWhereClause() + ";";
		return fJdbcDbConn.executeQueryObject(sql);
	}

	/**
	 * Select and count specific column from the initialized table with WHERE fields
	 * filters.
	 *
	 * Scenario:
	 *
	 * <pre>
	 *  SELECT COUNT(column) FROM table_name WHERE condition1 field1 operator1 value1 ...;
	 * </pre>
	 *
	 * @return ResultSet SQL execution results
	 */
	public ResultSet selectCountColumnByFileds() {
		if (!validateFieldsFiltering()) {
			return null;
		}

		String sql = SqlQueryTypes.SELECT.sqlQueryType() + " " + SqlStatementStrings.SQL_TABLE_COUNT + "("
				+ fColumns.get(0) + ") " + SqlStatementStrings.SQL_TABLE_FROM + " " + fTables.get(0)
				+ SqlStatementStrings.SQL_TABLE_WHERE + " " + buildSqlWhereClause() + ";";
		return fJdbcDbConn.executeQueryObject(sql);
	}

	/**
	 * Select and count specific column from the initialized table with WHERE fields
	 * filters.
	 *
	 * Scenario:
	 *
	 * <pre>
	 *  SELECT COUNT(DISTINCT column) FROM table_name WHERE condition1 field1 operator1 value1 ...;
	 * </pre>
	 *
	 * @return ResultSet SQL execution results
	 */
	public ResultSet selectCountColumnDistinctByFileds() {
		if (!validateFieldsFiltering()) {
			return null;
		}

		String sql = SqlQueryTypes.SELECT.sqlQueryType() + " " + SqlStatementStrings.SQL_TABLE_COUNT + "("
				+ SqlStatementStrings.SQL_TABLE_DISTINCT + " " + fColumns.get(0) + ") "
				+ SqlStatementStrings.SQL_TABLE_FROM + " " + fTables.get(0) + SqlStatementStrings.SQL_TABLE_WHERE + " "
				+ buildSqlWhereClause() + ";";
		return fJdbcDbConn.executeQueryObject(sql);
	}
}
