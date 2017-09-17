package QueryObjectFramework.statementClasses;

import java.sql.ResultSet;
import java.util.ArrayList;
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

	/*
	 * Select SQL statement specific operation settings
	 */
	private static final List<String> fOrderByColumns = new ArrayList<>();
	private static final List<String> fOrderByOrderings = new ArrayList<>();

	public QueryObjectSelect(JdbcDatabaseConnection jdbcDbConn) {
		super(SqlQueryTypes.SELECT, jdbcDbConn);
	}

	public QueryObjectSelect(JdbcDatabaseConnection jdbcDbConn, List<String> tables, List<String> columns,
			List<String> fields, List<Object> values, List<String> operators, List<String> conditions,
			List<String> orderByColumns, List<String> orderByOrdings) {
		super(SqlQueryTypes.SELECT, jdbcDbConn, tables, columns, fields, values, operators, conditions);
		fOrderByColumns.addAll(orderByColumns);
		fOrderByOrderings.addAll(orderByOrdings);
	}

	public void addOrderByColumn(String orderByColumn) {
		fOrderByColumns.add(orderByColumn);
	}

	public void setOrderByColumns(List<String> orderByColumns) {
		fOrderByColumns.addAll(orderByColumns);
	}

	public void clearOrderByColumns() {
		fOrderByColumns.clear();
	}

	public void addOrderByOrding(String orderByOrding) {
		fOrderByOrderings.add(orderByOrding);
	}

	public void setOrderByOrdings(List<String> orderByOrding) {
		fOrderByOrderings.addAll(orderByOrding);
	}

	public void clearOrderByOrdings() {
		fOrderByOrderings.clear();
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
	 * @param distinctSelection
	 * 			True if only select distinct lines
	 * @return ResultSet SQL execution results
	 */
	public ResultSet selectAll(boolean distinctSelection) {
		if (fTables.isEmpty()) {
			LOGGER.severe("Failed to select all fileds from table, table name is missing.");
			return null;
		}

		String sql = null;
		if (distinctSelection) {
			sql = SqlQueryTypes.SELECT.sqlQueryType() + " " + SqlStatementStrings.SQL_TABLE_DISTINCT + " * "
					+ SqlStatementStrings.SQL_TABLE_FROM + " " + fTables.get(0) + ";";
		} else {
			sql = SqlQueryTypes.SELECT.sqlQueryType() + " * " + SqlStatementStrings.SQL_TABLE_FROM + " "
					+ fTables.get(0) + ";";
		}
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
	 * @param distinctSelection
	 * 			True if only select distinct lines
	 * @return ResultSet SQL execution results
	 */
	public ResultSet selectColumns(boolean distinctSelection) {
		if (checkEmptyTableAndColumns()) {
			return null;
		}

		String sql = null;
		if (distinctSelection) {
			sql = SqlQueryTypes.SELECT.sqlQueryType() + " " + SqlStatementStrings.SQL_TABLE_DISTINCT + " "
					+ buildSqlColumnsString() + " " + SqlStatementStrings.SQL_TABLE_FROM + " " + fTables.get(0) + ";";
		} else {
			sql = SqlQueryTypes.SELECT.sqlQueryType() + " " + buildSqlColumnsString() + " "
					+ SqlStatementStrings.SQL_TABLE_FROM + " " + fTables.get(0) + ";";
		}
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
	 * @param distinctSelection
	 *			True if only select distinct lines
	 * @return ResultSet SQL execution results
	 */
	public ResultSet selectAllByFileds(boolean distinctSelection) {
		if (!validateFieldsFiltering()) {
			return null;
		}

		String sql = null;
		if (distinctSelection) {
			sql = SqlQueryTypes.SELECT.sqlQueryType() + " " + SqlStatementStrings.SQL_TABLE_DISTINCT + " * "
					+ SqlStatementStrings.SQL_TABLE_FROM + " " + fTables.get(0) + SqlStatementStrings.SQL_TABLE_WHERE + " "
					+ buildSqlWhereClause() + ";";
		} else {
			sql = SqlQueryTypes.SELECT.sqlQueryType() + " * " + SqlStatementStrings.SQL_TABLE_FROM + " "
					+ fTables.get(0) + SqlStatementStrings.SQL_TABLE_WHERE + " " + buildSqlWhereClause() + ";";
		}
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
				whereClause.append(fConditions.get(i) + " " + fFields.get(i) + fOperators.get(i) + "'" + fValues.get(i) + "' ");
			} else {
				whereClause.append(fConditions.get(i) + " " + fFields.get(i) + fOperators.get(i) + fValues.get(i) + " ");
			}
		}
		return whereClause.toString();
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
	 * @param distinctSelection
	 *			True if only select distinct lines
	 * @return ResultSet SQL execution results
	 */
	public ResultSet selectColumnsByFileds(boolean distinctSelection) {
		if (!validateFieldsFiltering()) {
			return null;
		}

		String sql = null;
		if (distinctSelection) {
			sql = SqlQueryTypes.SELECT.sqlQueryType() + " " + SqlStatementStrings.SQL_TABLE_DISTINCT + " "
					+ buildSqlColumnsString() + " " + SqlStatementStrings.SQL_TABLE_FROM + " " + fTables.get(0)
					+ SqlStatementStrings.SQL_TABLE_WHERE + " " + buildSqlWhereClause() + ";";
		} else {
			sql = SqlQueryTypes.SELECT.sqlQueryType() + " " + buildSqlColumnsString() + " "
					+ SqlStatementStrings.SQL_TABLE_FROM + " " + fTables.get(0) + SqlStatementStrings.SQL_TABLE_WHERE + " "
					+ buildSqlWhereClause() + ";";
		}
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
	 * @param distinctSelection
	 *			True if only select distinct lines
	 * @return ResultSet SQL execution results
	 */
	public ResultSet selectCountAll(boolean distinctSelection) {
		if (fTables.isEmpty()) {
			LOGGER.severe("Failed to select all fileds from table, table name is missing.");
			return null;
		}

		String sql = null;
		if (distinctSelection) {
			sql = SqlQueryTypes.SELECT.sqlQueryType() + " " + SqlStatementStrings.SQL_TABLE_COUNT + "("
					+ SqlStatementStrings.SQL_TABLE_DISTINCT + " *" + ") " + SqlStatementStrings.SQL_TABLE_FROM + " "
					+ fTables.get(0) + ";";
		} else {
			sql = SqlQueryTypes.SELECT.sqlQueryType() + " " + SqlStatementStrings.SQL_TABLE_COUNT + "(" + "*" + ") "
					+ SqlStatementStrings.SQL_TABLE_FROM + " " + fTables.get(0) + ";";
		}
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

		String sql = SqlQueryTypes.SELECT.sqlQueryType() + " " + SqlStatementStrings.SQL_TABLE_COUNT + "("
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
	 * @param distinctSelection
	 *			True if only select distinct lines
	 * @return ResultSet SQL execution results
	 */
	public ResultSet selectCountAllByFileds(boolean distinctSelection) {
		if (!validateFieldsFiltering()) {
			return null;
		}

		String sql = null;
		if (distinctSelection) {
			sql = SqlQueryTypes.SELECT.sqlQueryType() + " " + SqlStatementStrings.SQL_TABLE_COUNT + "("
					+ SqlStatementStrings.SQL_TABLE_DISTINCT + " *) " + SqlStatementStrings.SQL_TABLE_FROM + " "
					+ fTables.get(0) + SqlStatementStrings.SQL_TABLE_WHERE + " " + buildSqlWhereClause() + ";";
		} else {
			sql = SqlQueryTypes.SELECT.sqlQueryType() + " " + SqlStatementStrings.SQL_TABLE_COUNT + "(" + "*" + ") "
					+ SqlStatementStrings.SQL_TABLE_FROM + " " + fTables.get(0) + SqlStatementStrings.SQL_TABLE_WHERE + " "
					+ buildSqlWhereClause() + ";";
		}
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
	 * @param distinctSelection
	 *			True if only select distinct lines
	 * @return ResultSet SQL execution results
	 */
	public ResultSet selectCountColumnByFileds(boolean distinctSelection) {
		if (!validateFieldsFiltering()) {
			return null;
		}

		String sql = null;
		if (distinctSelection) {
			sql = SqlQueryTypes.SELECT.sqlQueryType() + " " + SqlStatementStrings.SQL_TABLE_COUNT + "("
					+ SqlStatementStrings.SQL_TABLE_DISTINCT + " " + fColumns.get(0) + ") "
					+ SqlStatementStrings.SQL_TABLE_FROM + " " + fTables.get(0) + SqlStatementStrings.SQL_TABLE_WHERE + " "
					+ buildSqlWhereClause() + ";";
		} else {
			sql = SqlQueryTypes.SELECT.sqlQueryType() + " " + SqlStatementStrings.SQL_TABLE_COUNT + "("
					+ fColumns.get(0) + ") " + SqlStatementStrings.SQL_TABLE_FROM + " " + fTables.get(0)
					+ SqlStatementStrings.SQL_TABLE_WHERE + " " + buildSqlWhereClause() + ";";
		}
		return fJdbcDbConn.executeQueryObject(sql);
	}

	/**
	 * Select all columns from the initialized table ORDER BY
	 * columns.
	 *
	 * Scenario:
	 *
	 * <pre>
	 *  SELECT * FROM table_name ORDER BY column1, column2, ... ASC|DESC;
	 * </pre>
	 *
	 * @param distinctSelection
	 *			True if only select distinct lines
	 * @return ResultSet SQL execution results
	 */
	public ResultSet selectAllOrderByColumns(boolean distinctSelection) {
		if (fTables.isEmpty()) {
			LOGGER.severe("Failed to select all fileds from table, table name is missing.");
			return null;
		}
		if (!validateOrderByColumnsAndOrderings()) {
			return null;
		}

		String sql = null;
		if (distinctSelection) {
			sql = SqlQueryTypes.SELECT.sqlQueryType() + " " + SqlStatementStrings.SQL_TABLE_DISTINCT + " * "
					+ SqlStatementStrings.SQL_TABLE_FROM + " " + fTables.get(0) + " "
					+ SqlStatementStrings.SQL_TABLE_ORDER_BY + " " + buildSqlOrderByClause() + ";";
		} else {
			sql = SqlQueryTypes.SELECT.sqlQueryType() + " * " + SqlStatementStrings.SQL_TABLE_FROM + " "
					+ fTables.get(0) + " " + SqlStatementStrings.SQL_TABLE_ORDER_BY + " " + buildSqlOrderByClause() + ";";
		}
		return fJdbcDbConn.executeQueryObject(sql);
	}

	/**
	 * Validate SQL ORDER BY columns and orders settings.
	 *
	 * fOrderByColumns and fOrderByOrderings should not be
	 * empty, and amount of these lists should be same. Meanwhile,
	 * fOrderByOrderings can only be ASC or DESC.
	 *
	 * @return True if all lists are matching valid rules.
	 */
	private boolean validateOrderByColumnsAndOrderings() {
		if (fOrderByColumns.isEmpty()) {
			LOGGER.severe("Failed to select all fileds from table, Order by column names are missing.");
			return false;
		}
		if (fOrderByOrderings.isEmpty()) {
			LOGGER.severe("Failed to select all fileds from table, Order by orderings are missing.");
			return false;
		}
		if (fOrderByOrderings.size() != fOrderByColumns.size()) {
			LOGGER.severe("Failed to select all fileds from table, OrderBy columns and orders are missing.");
			return false;
		}
		for (String order : fOrderByOrderings) {
			if (!order.equalsIgnoreCase("ASC") && !order.equalsIgnoreCase("DESC")) {
					LOGGER.severe("Failed to select all fileds from table, OrderBy orders must by ASC or DESC.");
					return false;
			}
		}
		return true;
	}


	/**
	 * Build SQL ORDER BY clause string from fOrderByColumns and fOrderByOrderings
	 * lists.
	 *
	 * @return SQL ORDER BY string
	 */
	private String buildSqlOrderByClause() {
		StringBuilder orderByClause = new StringBuilder();
		for (int i = 0; i < fOrderByColumns.size(); i++) {
			orderByClause.append(fOrderByColumns.get(i) + " " + fOrderByOrderings.get(i) + ",");
		}
		orderByClause.deleteCharAt(orderByClause.length() - 1);
		return orderByClause.toString();
	}

	/**
	 * Select specific columns from the initialized table ORDER BY columns.
	 *
	 * Scenario:
	 *
	 * <pre>
	 *  SELECT column1, column2, .. FROM table_name ORDER BY column1, column2, ... ASC|DESC;
	 * </pre>
	 *
	 * @param distinctSelection
	 *			True if only select distinct lines
	 * @return ResultSet SQL execution results
	 */
	public ResultSet selectColumnsOrderByColumns(boolean distinctSelection) {
		if (checkEmptyTableAndColumns() || !validateOrderByColumnsAndOrderings()) {
			return null;
		}

		String sql = null;
		if (distinctSelection) {
			sql = SqlQueryTypes.SELECT.sqlQueryType() + " " + SqlStatementStrings.SQL_TABLE_DISTINCT + " "
					+ buildSqlColumnsString() + " " + SqlStatementStrings.SQL_TABLE_FROM + " " + fTables.get(0)
					+ " " + SqlStatementStrings.SQL_TABLE_ORDER_BY + " " + buildSqlOrderByClause() + ";";
		} else {
			sql = SqlQueryTypes.SELECT.sqlQueryType() + " " + buildSqlColumnsString() + " "
					+ SqlStatementStrings.SQL_TABLE_FROM + " " + fTables.get(0)
					+ " " +SqlStatementStrings.SQL_TABLE_ORDER_BY + " " + buildSqlOrderByClause() + ";";
		}
		return fJdbcDbConn.executeQueryObject(sql);
	}

	/**
	 * Select all columns from the initialized table with WHERE fields filters
	 * ORDER BY columns.
	 *
	 * Scenario:
	 *
	 * <pre>
	 *  SELECT * FROM table_name WHERE condition 1 field1 operator1 value1 condition2 ...
	 *  ORDER BY column1, column2, ... ASC|DESC;
	 * </pre>
	 *
	 * If first condition is empty string "", then the first condition will be not
	 * inserted. Example, ... WHERE country='USA' AND name='Bohui';
	 *
	 * @param distinctSelection
	 *			True if only select distinct lines
	 * @return ResultSet SQL execution results
	 */
	public ResultSet selectAllByFiledsOrderByColumns(boolean distinctSelection) {
		if (!validateFieldsFiltering() || !validateOrderByColumnsAndOrderings()) {
			return null;
		}

		String sql = null;
		if (distinctSelection) {
			sql = SqlQueryTypes.SELECT.sqlQueryType() + " " + SqlStatementStrings.SQL_TABLE_DISTINCT + " * "
					+ SqlStatementStrings.SQL_TABLE_FROM + " " + fTables.get(0) + SqlStatementStrings.SQL_TABLE_WHERE + " "
					+ buildSqlWhereClause()
					+ " " + SqlStatementStrings.SQL_TABLE_ORDER_BY + " " + buildSqlOrderByClause() + ";";
		} else {
			sql = SqlQueryTypes.SELECT.sqlQueryType() + " * " + SqlStatementStrings.SQL_TABLE_FROM + " "
					+ fTables.get(0) + SqlStatementStrings.SQL_TABLE_WHERE + " " + buildSqlWhereClause()
					+ " " + SqlStatementStrings.SQL_TABLE_ORDER_BY + " " + buildSqlOrderByClause() + ";";
		}
		return fJdbcDbConn.executeQueryObject(sql);
	}

	/**
	 * Select specific columns from the initialized table with WHERE fields filters
	 * ORDER BY columns.
	 *
	 * Scenario:
	 *
	 * <pre>
	 *  SELECT column1, column2, .. FROM table_name WHERE condition1 field1 operator1 value1 ...
	 *  ORDER BY column1, column2, ... ASC|DESC;
	 * </pre>
	 *
	 * @param distinctSelection
	 *			True if only select distinct lines
	 * @return ResultSet SQL execution results
	 */
	public ResultSet selectColumnsByFiledsOrderByColumns(boolean distinctSelection) {
		if (!validateFieldsFiltering() || !validateOrderByColumnsAndOrderings()) {
			return null;
		}

		String sql = null;
		if (distinctSelection) {
			sql = SqlQueryTypes.SELECT.sqlQueryType() + " " + SqlStatementStrings.SQL_TABLE_DISTINCT + " "
					+ buildSqlColumnsString() + " " + SqlStatementStrings.SQL_TABLE_FROM + " " + fTables.get(0)
					+ SqlStatementStrings.SQL_TABLE_WHERE + " " + buildSqlWhereClause()
					+ " " + SqlStatementStrings.SQL_TABLE_ORDER_BY + " " + buildSqlOrderByClause() + ";";
		} else {
			sql = SqlQueryTypes.SELECT.sqlQueryType() + " " + buildSqlColumnsString() + " "
					+ SqlStatementStrings.SQL_TABLE_FROM + " " + fTables.get(0) + SqlStatementStrings.SQL_TABLE_WHERE + " "
					+ buildSqlWhereClause()
					+ " " + SqlStatementStrings.SQL_TABLE_ORDER_BY + " " + buildSqlOrderByClause() + ";";
		}
		return fJdbcDbConn.executeQueryObject(sql);
	}

	/**
	 * Select and MIN specific column from the initialized table.
	 *
	 * Scenario:
	 *
	 * <pre>
	 *  SELECT MIN(column) FROM table_name;
	 * </pre>
	 *
	 * @return ResultSet SQL execution results
	 */
	public ResultSet selectMinColumn() {
		if(checkEmptyTableAndColumns()) {
			return null;
		}

		String sql = SqlQueryTypes.SELECT.sqlQueryType() + " " + SqlStatementStrings.SQL_TABLE_MIN + "("
				+ fColumns.get(0) + ") " + SqlStatementStrings.SQL_TABLE_FROM + " " + fTables.get(0) + ";";
		return fJdbcDbConn.executeQueryObject(sql);
	}

	/**
	 * Select and MAX specific column from the initialized table.
	 *
	 * Scenario:
	 *
	 * <pre>
	 *  SELECT MAX(column) FROM table_name;
	 * </pre>
	 *
	 * @return ResultSet SQL execution results
	 */
	public ResultSet selectMaxColumn() {
		if(checkEmptyTableAndColumns()) {
			return null;
		}

		String sql = SqlQueryTypes.SELECT.sqlQueryType() + " " + SqlStatementStrings.SQL_TABLE_MAX + "("
				+ fColumns.get(0) + ") " + SqlStatementStrings.SQL_TABLE_FROM + " " + fTables.get(0) + ";";
		return fJdbcDbConn.executeQueryObject(sql);
	}

	/**
	 * Select and MIN specific column from the initialized table with WHERE fields
	 * filters.
	 *
	 * Scenario:
	 *
	 * <pre>
	 *  SELECT MIN(column) FROM table_name WHERE condition1 field1 operator1 value1 ...;
	 * </pre>
	 *
	 * @return ResultSet SQL execution results
	 */
	public ResultSet selectMinColumnByFileds() {
		if (!validateFieldsFiltering()) {
			return null;
		}

		String sql = SqlQueryTypes.SELECT.sqlQueryType() + " " + SqlStatementStrings.SQL_TABLE_MIN + "("
				+ fColumns.get(0) + ") " + SqlStatementStrings.SQL_TABLE_FROM + " " + fTables.get(0)
				+ SqlStatementStrings.SQL_TABLE_WHERE + " " + buildSqlWhereClause() + ";";
		return fJdbcDbConn.executeQueryObject(sql);
	}

	/**
	 * Select and MAX specific column from the initialized table with WHERE fields
	 * filters.
	 *
	 * Scenario:
	 *
	 * <pre>
	 *  SELECT MAX(column) FROM table_name WHERE condition1 field1 operator1 value1 ...;
	 * </pre>
	 *
	 * @return ResultSet SQL execution results
	 */
	public ResultSet selectMaxColumnByFileds() {
		if (!validateFieldsFiltering()) {
			return null;
		}

		String sql = SqlQueryTypes.SELECT.sqlQueryType() + " " + SqlStatementStrings.SQL_TABLE_MAX + "("
				+ fColumns.get(0) + ") " + SqlStatementStrings.SQL_TABLE_FROM + " " + fTables.get(0)
				+ SqlStatementStrings.SQL_TABLE_WHERE + " " + buildSqlWhereClause() + ";";
		return fJdbcDbConn.executeQueryObject(sql);
	}
}
