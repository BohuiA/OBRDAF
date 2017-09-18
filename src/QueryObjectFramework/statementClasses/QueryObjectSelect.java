package QueryObjectFramework.statementClasses;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.sun.istack.internal.NotNull;

import QueryObjectFramework.common.SqlCriteriaCondition;
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
	private final @NotNull List<String> fOrderByColumns = new ArrayList<>();
	private final @NotNull List<String> fOrderByOrderings = new ArrayList<>();

	public QueryObjectSelect(@NotNull JdbcDatabaseConnection jdbcDbConn) {
		super(SqlQueryTypes.SELECT, jdbcDbConn);
	}

	public QueryObjectSelect(@NotNull JdbcDatabaseConnection jdbcDbConn, @NotNull List<String> tables,
			@NotNull List<String> columns, List<SqlCriteriaCondition> selectCriterias,
			List<String> orderByColumns, List<String> orderByOrderings) {
		super(SqlQueryTypes.SELECT, jdbcDbConn, tables, columns, selectCriterias);
		if (orderByColumns != null) {
			fOrderByColumns.addAll(orderByColumns);
		}
		if (orderByOrderings != null) {
			fOrderByOrderings.addAll(orderByOrderings);
		}
	}

	public void addOrderByColumn(@NotNull String orderByColumn) {
		fOrderByColumns.add(orderByColumn);
	}

	public void setOrderByColumns(List<String> orderByColumns) {
		clearOrderByColumns();
		if (orderByColumns != null) {
			fOrderByColumns.addAll(orderByColumns);
		}
	}

	public void clearOrderByColumns() {
		fOrderByColumns.clear();
	}

	public void addOrderByOrdering(@NotNull String orderByOrdering) {
		fOrderByOrderings.add(orderByOrdering);
	}

	public void setOrderByOrderings(List<String> orderByOrdering) {
		clearOrderByOrderings();
		if (orderByOrdering != null) {
			fOrderByOrderings.addAll(orderByOrdering);
		}
	}

	public void clearOrderByOrderings() {
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
	 *  SELECT <DISTINCT> column1, column2, .. FROM table_name WHERE conditionOperator1
	 *  field1 operator1 value1 ...;
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
	public ResultSet selectColumnsWhereConditions(boolean distinctSelection) {
		if (checkEmptyTableAndUpdateEmptyColumn() && !validateWhereConditions()) {
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
	 * Build SQL WHERE clause string from fCriteriaConditions lists.
	 *
	 * @return SQL WHERE string
	 */
	private String buildSqlWhereClause() {
		StringBuilder whereClause = new StringBuilder();
		for (SqlCriteriaCondition sqlCriteria : fCriteriaConditions) {
			if (sqlCriteria.getValue() instanceof String) {
				whereClause.append(sqlCriteria.getConditionOperator() + " " + sqlCriteria.getFiled()
						+ sqlCriteria.getOperator() + "'" + sqlCriteria.getValue() + "' ");
			} else {
				whereClause.append(sqlCriteria.getConditionOperator() + " " + sqlCriteria.getFiled()
						+ sqlCriteria.getOperator() + sqlCriteria.getValue() + " ");
			}
		}
		return whereClause.toString();
	}

	/**
	 * Select and COUNT specific column from the initialized table.
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
	public ResultSet selectAndCountColumns() {
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
	 *  SELECT COUNT(<DISTINCT> column) FROM table_name WHERE conditionOperator1
	 *  field1 operator1 value1 ...;
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
	public ResultSet selectAndCountColumnsWhereConditions(boolean distinctSelection) {
		if (checkEmptyTableAndUpdateEmptyColumn() && !validateWhereConditions()) {
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
	 *  SELECT <DISTINCT> column1, column2, .. FROM table_name ORDER BY column1,
	 *  column2, ... ASC|DESC;
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
	 *  SELECT <DISTINCT> column1, column2, .. FROM table_name WHERE conditionOperator1
	 *  field1 operator1 value1 ...
	 *  ORDER BY column1, column2, ... ASC|DESC;
	 * </pre>
	 *
	 * <pre>
	 * 	SELECT <DISTINCT> * FROM Customers WHERE CustomerName LIKE '%or%' ORDER BY column1,
	 * column2, ... ASC|DESC;
	 * </pre>
	 *
	 * @param distinctSelection
	 *            True if only select distinct lines
	 * @return ResultSet SQL execution results
	 */
	public ResultSet selectColumnsWhereConditionsOrderByColumns(boolean distinctSelection) {
		if (checkEmptyTableAndUpdateEmptyColumn()
				&& (!validateWhereConditions() || !validateOrderByColumnsAndOrderings())) {
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
	public ResultSet selectAndMinColumns() {
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
	public ResultSet selectAndMaxColumns() {
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
	 *  SELECT MIN(column) FROM table_name WHERE conditionOperator1 field1 operator1 value1 ...;
	 * </pre>
	 *
	 * <pre>
	 * 	SELECT MIN(<DISTINCT> *) FROM Customers WHERE CustomerName LIKE '%or%';
	 * </pre>
	 *
	 * @return ResultSet SQL execution results
	 */
	public ResultSet selectAndMinColumnsWhereConditions() {
		if (checkEmptyTableAndUpdateEmptyColumn() && !validateWhereConditions()) {
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
	 *  SELECT MAX(column) FROM table_name WHERE conditionOperator1 field1 operator1 value1 ...;
	 * </pre>
	 *
	 * <pre>
	 * 	SELECT MAX(<DISTINCT> *) FROM Customers WHERE CustomerName LIKE '%or%';
	 * </pre>
	 *
	 * @return ResultSet SQL execution results
	 */
	public ResultSet selectAndMaxColumnsWhereConditions() {
		if (checkEmptyTableAndUpdateEmptyColumn() && !validateWhereConditions()) {
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
	public ResultSet selectAndAvgColumns() {
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
	 *  SELECT AVG(<DISTINCT> column) FROM table_name WHERE conditionOperator1
	 *  field1 operator1 value1 ...;
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
	public ResultSet selectAndAvgColumnsWhereConditions(boolean distinctSelection) {
		if (checkEmptyTableAndUpdateEmptyColumn() && !validateWhereConditions()) {
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
	public ResultSet selectAndSumColumns() {
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
	 *  SELECT SUM(<DISTINCT> column) FROM table_name WHERE conditionOperator1
	 *  field1 operator1 value1 ...;
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
	public ResultSet selectAndSumColumnsWhereConditions(boolean distinctSelection) {
		if (checkEmptyTableAndUpdateEmptyColumn() && !validateWhereConditions()) {
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
