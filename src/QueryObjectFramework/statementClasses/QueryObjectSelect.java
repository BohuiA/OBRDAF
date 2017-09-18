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
	private final List<String> fOrderByColumns = new ArrayList<>();
	private final List<String> fOrderByOrderings = new ArrayList<>();

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
	 * Select specific columns from the initialized table.
	 *
	 * NOTE: Setting fColumns field to null if SELECT * all columns.
	 *
	 * Scenario:
	 *
	 * <pre>
	 *  SELECT <DISTINCT> column1, column2, .. FROM table_name;
	 * </pre>
	 *
	 * @param distinctSelection
	 * 			True if only select distinct lines
	 * @return ResultSet SQL execution results
	 */
	public ResultSet selectColumns(boolean distinctSelection) {
		if (checkEmptyTableAndUpdateEmptyColumn()) {
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
	 * Select specific columns from the initialized table with WHERE fields filters.
	 *
	 * NOTE: Setting fColumns field to null if SELECT * all columns.
	 *
	 * Scenario:
	 *
	 * <pre>
	 *  SELECT <DISTINCT> column1, column2, .. FROM table_name WHERE condition1 field1 operator1 value1 ...;
	 * </pre>
	 * 
	 * <pre>
	 * 	SELECT <DISTINCT> column1, column2, .. FROM Customers WHERE CustomerName LIKE '%or%';
	 * </pre>
	 *
	 * @param distinctSelection
	 *            True if only select distinct lines
	 * @return ResultSet SQL execution results
	 */
	public ResultSet selectColumnsByFileds(boolean distinctSelection) {
		if (checkEmptyTableAndUpdateEmptyColumn() && !validateFieldsFiltering()) {
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
	 * Select and count specific column from the initialized table.
	 *
	 * NOTE: Setting fColumns field to null if SELECT * all columns.
	 *
	 * Scenario:
	 *
	 * <pre>
	 *  SELECT COUNT(<DISTINCT> column) FROM table_name;
	 * </pre>
	 *
	 * @return ResultSet SQL execution results
	 */
	public ResultSet selectCountColumn() {
		if(checkEmptyTableAndUpdateEmptyColumn()) {
			return null;
		}

		String sql = SqlQueryTypes.SELECT.sqlQueryType() + " " + SqlStatementStrings.SQL_TABLE_COUNT + "("
				+ fColumns.get(0) + ") " + SqlStatementStrings.SQL_TABLE_FROM + " " + fTables.get(0) + ";";
		return fJdbcDbConn.executeQueryObject(sql);
	}

	/**
	 * Select and count specific column from the initialized table with WHERE fields
	 * filters.
	 *
	 * NOTE: Setting fColumns field to null if SELECT * all columns.
	 *
	 * Scenario:
	 *
	 * <pre>
	 *  SELECT COUNT(<DISTINCT> column) FROM table_name WHERE condition1 field1 operator1 value1 ...;
	 * </pre>
	 * 
	 * <pre>
	 * 	SELECT COUNT(<DISTINCT> *) FROM Customers WHERE CustomerName LIKE '%or%';
	 * </pre>
	 *
	 * @param distinctSelection
	 *			True if only select distinct lines
	 * @return ResultSet SQL execution results
	 */
	public ResultSet selectCountColumnByFileds(boolean distinctSelection) {
		if (checkEmptyTableAndUpdateEmptyColumn() && !validateFieldsFiltering()) {
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
	 * Select specific columns from the initialized table ORDER BY columns.
	 *
	 * NOTE: Setting fColumns field to null if SELECT * all columns.
	 *
	 * Scenario:
	 *
	 * <pre>
	 *  SELECT <DISTINCT> column1, column2, .. FROM table_name ORDER BY column1, column2, ... ASC|DESC;
	 * </pre>
	 *
	 * @param distinctSelection
	 *			True if only select distinct lines
	 * @return ResultSet SQL execution results
	 */
	public ResultSet selectColumnsOrderByColumns(boolean distinctSelection) {
		if (checkEmptyTableAndUpdateEmptyColumn() || !validateOrderByColumnsAndOrderings()) {
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
	 * Select specific columns from the initialized table with WHERE fields filters
	 * ORDER BY columns.
	 *
	 * NOTE: Setting fColumns field to null if SELECT * all columns.
	 *
	 * Scenario:
	 *
	 * <pre>
	 *  SELECT <DISTINCT> column1, column2, .. FROM table_name WHERE condition1 field1 operator1 value1 ...
	 *  ORDER BY column1, column2, ... ASC|DESC;
	 * </pre>
	 * 
	 * <pre>
	 * 	SELECT <DISTINCT> * FROM Customers WHERE CustomerName LIKE '%or%' ORDER BY column1, column2, ... ASC|DESC;
	 * </pre>
	 *
	 * @param distinctSelection
	 *            True if only select distinct lines
	 * @return ResultSet SQL execution results
	 */
	public ResultSet selectColumnsByFiledsOrderByColumns(boolean distinctSelection) {
		if (checkEmptyTableAndUpdateEmptyColumn()
				&& (!validateFieldsFiltering() || !validateOrderByColumnsAndOrderings())) {
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
		if(checkEmptyTableAndUpdateEmptyColumn()) {
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
		if(checkEmptyTableAndUpdateEmptyColumn()) {
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
	 * NOTE: Setting fColumns field to null if SELECT * all columns.
	 *
	 * Scenario:
	 *
	 * <pre>
	 *  SELECT MIN(column) FROM table_name WHERE condition1 field1 operator1 value1 ...;
	 * </pre>
	 * 
	 * <pre>
	 * 	SELECT MIN(<DISTINCT> *) FROM Customers WHERE CustomerName LIKE '%or%';
	 * </pre>
	 *
	 * @return ResultSet SQL execution results
	 */
	public ResultSet selectMinColumnByFileds() {
		if (checkEmptyTableAndUpdateEmptyColumn() && !validateFieldsFiltering()) {
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
	 * NOTE: Setting fColumns field to null if SELECT * all columns.
	 *
	 * Scenario:
	 *
	 * <pre>
	 *  SELECT MAX(column) FROM table_name WHERE condition1 field1 operator1 value1 ...;
	 * </pre>
	 * 
	 * <pre>
	 * 	SELECT MAX(<DISTINCT> *) FROM Customers WHERE CustomerName LIKE '%or%';
	 * </pre>
	 *
	 * @return ResultSet SQL execution results
	 */
	public ResultSet selectMaxColumnByFileds() {
		if (checkEmptyTableAndUpdateEmptyColumn() && !validateFieldsFiltering()) {
			return null;
		}

		String sql = SqlQueryTypes.SELECT.sqlQueryType() + " " + SqlStatementStrings.SQL_TABLE_MAX + "("
				+ fColumns.get(0) + ") " + SqlStatementStrings.SQL_TABLE_FROM + " " + fTables.get(0)
				+ SqlStatementStrings.SQL_TABLE_WHERE + " " + buildSqlWhereClause() + ";";
		return fJdbcDbConn.executeQueryObject(sql);
	}

	/**
	 * Select and AVG specific column from the initialized table.
	 *
	 * NOTE: Setting fColumns field to null if SELECT * all columns.
	 *
	 * Scenario:
	 *
	 * <pre>
	 *  SELECT AVG(<DISTINCT> column) FROM table_name;
	 * </pre>
	 *
	 * @return ResultSet SQL execution results
	 */
	public ResultSet selectAvgColumn() {
		if(checkEmptyTableAndUpdateEmptyColumn()) {
			return null;
		}

		String sql = SqlQueryTypes.SELECT.sqlQueryType() + " " + SqlStatementStrings.SQL_TABLE_AVG + "("
				+ fColumns.get(0) + ") " + SqlStatementStrings.SQL_TABLE_FROM + " " + fTables.get(0) + ";";
		return fJdbcDbConn.executeQueryObject(sql);
	}

	/**
	 * Select and AVG specific column from the initialized table with WHERE fields
	 * filters.
	 *
	 * NOTE: Setting fColumns field to null if SELECT * all columns.
	 *
	 * Scenario:
	 *
	 * <pre>
	 *  SELECT AVG(<DISTINCT> column) FROM table_name WHERE condition1 field1 operator1 value1 ...;
	 * </pre>
	 * 
	 * <pre>
	 * 	SELECT AVG(<DISTINCT> *) FROM Customers WHERE CustomerName LIKE '%or%';
	 * </pre>
	 *
	 * @param distinctSelection
	 *			True if only select distinct lines
	 * @return ResultSet SQL execution results
	 */
	public ResultSet selectAvgColumnByFileds(boolean distinctSelection) {
		if (checkEmptyTableAndUpdateEmptyColumn() && !validateFieldsFiltering()) {
			return null;
		}

		String sql = null;
		if (distinctSelection) {
			sql = SqlQueryTypes.SELECT.sqlQueryType() + " " + SqlStatementStrings.SQL_TABLE_AVG + "("
					+ SqlStatementStrings.SQL_TABLE_DISTINCT + " " + fColumns.get(0) + ") "
					+ SqlStatementStrings.SQL_TABLE_FROM + " " + fTables.get(0) + SqlStatementStrings.SQL_TABLE_WHERE + " "
					+ buildSqlWhereClause() + ";";
		} else {
			sql = SqlQueryTypes.SELECT.sqlQueryType() + " " + SqlStatementStrings.SQL_TABLE_AVG + "("
					+ fColumns.get(0) + ") " + SqlStatementStrings.SQL_TABLE_FROM + " " + fTables.get(0)
					+ SqlStatementStrings.SQL_TABLE_WHERE + " " + buildSqlWhereClause() + ";";
		}
		return fJdbcDbConn.executeQueryObject(sql);
	}

	/**
	 * Select and SUM specific column from the initialized table.
	 *
	 * NOTE: Setting fColumns field to null if SELECT * all columns.
	 *
	 * Scenario:
	 *
	 * <pre>
	 *  SELECT SUM(<DISTINCT> column) FROM table_name;
	 * </pre>
	 *
	 * @return ResultSet SQL execution results
	 */
	public ResultSet selectSumColumn() {
		if(checkEmptyTableAndUpdateEmptyColumn()) {
			return null;
		}

		String sql = SqlQueryTypes.SELECT.sqlQueryType() + " " + SqlStatementStrings.SQL_TABLE_SUM + "("
				+ fColumns.get(0) + ") " + SqlStatementStrings.SQL_TABLE_FROM + " " + fTables.get(0) + ";";
		return fJdbcDbConn.executeQueryObject(sql);
	}

	/**
	 * Select and SUM specific column from the initialized table with WHERE fields
	 * filters.
	 *
	 * NOTE: Setting fColumns field to null if SELECT * all columns.
	 *
	 * Scenario:
	 *
	 * <pre>
	 *  SELECT SUM(<DISTINCT> column) FROM table_name WHERE condition1 field1 operator1 value1 ...;
	 * </pre>
	 * 
	 * <pre>
	 * 	SELECT SUM(<DISTINCT> *) FROM Customers WHERE CustomerName LIKE '%or%';
	 * </pre>
	 *
	 * @param distinctSelection
	 *			True if only select distinct lines
	 * @return ResultSet SQL execution results
	 */
	public ResultSet selectSumColumnByFileds(boolean distinctSelection) {
		if (checkEmptyTableAndUpdateEmptyColumn() && !validateFieldsFiltering()) {
			return null;
		}

		String sql = null;
		if (distinctSelection) {
			sql = SqlQueryTypes.SELECT.sqlQueryType() + " " + SqlStatementStrings.SQL_TABLE_SUM + "("
					+ SqlStatementStrings.SQL_TABLE_DISTINCT + " " + fColumns.get(0) + ") "
					+ SqlStatementStrings.SQL_TABLE_FROM + " " + fTables.get(0) + SqlStatementStrings.SQL_TABLE_WHERE + " "
					+ buildSqlWhereClause() + ";";
		} else {
			sql = SqlQueryTypes.SELECT.sqlQueryType() + " " + SqlStatementStrings.SQL_TABLE_SUM + "("
					+ fColumns.get(0) + ") " + SqlStatementStrings.SQL_TABLE_FROM + " " + fTables.get(0)
					+ SqlStatementStrings.SQL_TABLE_WHERE + " " + buildSqlWhereClause() + ";";
		}
		return fJdbcDbConn.executeQueryObject(sql);
	}
}
