package QueryObjectFramework.QueryObjectTableStatements;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.eclipse.jdt.annotation.NonNull;

import QueryObjectFramework.CommonClasses.SqlCriteriaCondition;
import QueryObjectFramework.CommonClasses.SqlJoinType;
import QueryObjectFramework.CommonClasses.SqlQueryTypes;
import QueryObjectFramework.CommonClasses.SqlStatementStrings;
import QueryObjectFramework.JdbcDatabaseConnection.JdbcDatabaseConnection;

/**
 * Query object class for Select statement.
 *
 * @author Bohui Axelsson
 */
public class QueryObjectSelect extends QueryObjectTableAbstract {
	private static final Logger LOGGER = Logger.getLogger(QueryObjectSelect.class.getName());

	/*
	 * Select SQL statement specific operation settings
	 */
	private final @NonNull List<String> fOrderByColumns = new ArrayList<>();
	private final @NonNull List<String> fOrderByOrderings = new ArrayList<>();
	private final @NonNull List<SqlJoinType> fJoinTypes = new ArrayList<>();

	/**
	 * Create a SELECT query object with only JDBC connection.
	 *
	 * @param jdbcDbConn
	 * 			JDBC database connection
	 */
	public QueryObjectSelect(@NonNull JdbcDatabaseConnection jdbcDbConn) {
		super(SqlQueryTypes.SELECT, jdbcDbConn);
	}

	/**
	 * Create a SELECT query object with normal select grammar.
	 *
	 * Example:
	 *
	 * <pre>
	 * 	SELECT column1, column2, ... FROM table_name;
	 * </pre>
	 *
	 * @param jdbcDbConn
	 * 			JDBC database connection
	 * @param tables
	 * 			Names of table for selecting, should be only one
	 * @param columns
	 * 			Names of columns for selection, setting null for
	 * 			selecting all columns.
	 */
	public QueryObjectSelect(@NonNull JdbcDatabaseConnection jdbcDbConn, @NonNull List<String> tables,
			@NonNull List<String> columns) {
		super(SqlQueryTypes.SELECT, jdbcDbConn, tables, columns, null);
	}

	/**
	 * Create a SELECT query object with WHERE filtering grammar.
	 *
	 * Example:
	 *
	 * <pre>
	 *  SELECT column1, column2, ...
	 *  FROM table_name
	 *  WHERE NOT condition1 AND condition2 AND NOT condition3 ...;
	 * </pre>
	 *
	 * Note: conditionOperator is set as "" or NULL means there is no condition operator,
	 * Available condition operators are NOT, OR, AND.
	 *
	 * @param jdbcDbConn
	 * 			JDBC database connection
	 * @param tables
	 * 			Names of table for selecting, should be only one
	 * @param columns
	 * 			Names of columns for selection, setting null for
	 * 			selecting all columns.
	 * @param selectCriterias
	 * 			List that contains filtering selection criteria.
	 * 			Setting to NULL for ignoring.
	 */
	public QueryObjectSelect(@NonNull JdbcDatabaseConnection jdbcDbConn, @NonNull List<String> tables,
			@NonNull List<String> columns, List<SqlCriteriaCondition> selectCriterias) {
		super(SqlQueryTypes.SELECT, jdbcDbConn, tables, columns, selectCriterias);
	}

	/**
	 * Create a SELECT query object with WHERE and ORDER BY grammar.
	 *
	 * Example:
	 *
	 * <pre>
	 *  SELECT column1, column2, ...
	 *  FROM table_name
	 *  ORDER BY column1, column2, ... ASC|DESC;
	 * </pre>
	 *
	 * Note: conditionOperator is set as "" or NULL means there is no condition operator,
	 * Available condition operators are NOT, OR, AND.
	 *
	 * @param jdbcDbConn
	 * 			JDBC database connection
	 * @param tables
	 * 			Names of table for selecting, should be only one
	 * @param columns
	 * 			Names of columns for selection, setting null for
	 * 			selecting all columns.
	 * @param selectCriterias
	 * 			List that contains filtering selection criteria
	 * @param orderByColumns
	 * 			Names of columns that need to order by.
	 * 			Setting to NULL for ignoring.
	 * @param orderByOrderings
	 * 			Names of Orderings that need to perform, should be
	 * 			'ASC' or 'DESC'.
	 * 			Setting to NULL for ignoring.
	 */
	public QueryObjectSelect(@NonNull JdbcDatabaseConnection jdbcDbConn, @NonNull List<String> tables,
			@NonNull List<String> columns, List<SqlCriteriaCondition> selectCriterias,
			List<String> orderByColumns, List<String> orderByOrderings) {
		super(SqlQueryTypes.SELECT, jdbcDbConn, tables, columns, selectCriterias);
		if (orderByColumns != null) {
			fOrderByColumns.addAll(orderByColumns);
		}
		if (orderByOrderings != null) {
			fOrderByOrderings.addAll(orderByOrderings);
		}
	}

	/**
	 * Create a SELECT query object with WHERE, ODERBY and JOIN grammars.
	 *
	 * Example:
	 * <pre>
	 *  SELECT Customers.CustomerName, Orders.OrderID
	 *  FROM Customers
	 *  LEFT JOIN Orders ON Customers.CustomerID = Orders.CustomerID
	 *  ORDER BY Customers.CustomerName ASC;
	 * </pre>
	 *
	 * Note: conditionOperator is set as "" or NULL means there is no condition operator,
	 * Available condition operators are NOT, OR, AND.
	 *
	 * @param jdbcDbConn
	 * 			JDBC database connection
	 * @param tables
	 * 			Names of table for selecting, should be only one
	 * @param columns
	 * 			Names of columns for selection, setting null for
	 * 			selecting all columns.
	 * @param selectCriterias
	 * 			List that contains filtering selection criteria.
	 * 			Setting to NULL for ignoring.
	 * @param orderByColumns
	 * 			Names of columns that need to order by.
	 * 			Setting to NULL for ignoring.
	 * @param orderByOrderings
	 * 			Names of Orderings that need to perform, should be
	 * 			'ASC' or 'DESC'.
	 * 			Setting to NULL for ignoring.
	 * @param joinTypes
	 * 			List that contains Join types defining in SqlJoinType class.
	 * 			Setting to NULL for ignoring.
	 */
	public QueryObjectSelect(@NonNull JdbcDatabaseConnection jdbcDbConn, @NonNull List<String> tables,
			@NonNull List<String> columns, List<SqlCriteriaCondition> selectCriterias,
			List<String> orderByColumns, List<String> orderByOrderings, List<SqlJoinType> joinTypes) {
		super(SqlQueryTypes.SELECT, jdbcDbConn, tables, columns, selectCriterias);
		if (orderByColumns != null) {
			fOrderByColumns.addAll(orderByColumns);
		}
		if (orderByOrderings != null) {
			fOrderByOrderings.addAll(orderByOrderings);
		}
		if (joinTypes!= null) {
			fJoinTypes.addAll(joinTypes);
		}
	}

	public void addOrderByColumn(@NonNull String orderByColumn) {
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

	public void addOrderByOrdering(@NonNull String orderByOrdering) {
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

	public void addJoinType(@NonNull SqlJoinType joinType) {
		fJoinTypes.add(joinType);
	}

	public void setJoinTypes(List<SqlJoinType> joinTypes) {
		clearJoinTypes();
		if (joinTypes != null) {
			fJoinTypes.addAll(joinTypes);
		}
	}

	public void clearJoinTypes() {
		fJoinTypes.clear();
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
		if (!validateEmptyTableAndUpdateEmptyColumn()) {
			return null;
		}

		String sql = null;
		if (distinctSelection) {
			sql = fQueryObjectType.sqlQueryType() + " " + SqlStatementStrings.SQL_TABLE_DISTINCT + " "
					+ buildSqlColumnsString() + " " + SqlStatementStrings.SQL_TABLE_FROM + " " + fTables.get(0) + ";";
		} else {
			sql = fQueryObjectType.sqlQueryType() + " " + buildSqlColumnsString() + " "
					+ SqlStatementStrings.SQL_TABLE_FROM + " " + fTables.get(0) + ";";
		}
		return fJdbcDbConn.executeQueryObject(sql);
	}

	/**
	 * Check fTables is not empty, and update fColumns with '*' if iColumns is
	 * empty.
	 *
	 * @return True if fTables is not empty.
	 */
	private boolean validateEmptyTableAndUpdateEmptyColumn() {
		if (fTables.isEmpty()) {
			LOGGER.severe("Failed to select columns from table, table name is missing.");
			return false;
		}
		/*
		 * If fColumns is empty, it means selecting all columns,
		 * updating fColumns with one '*'.
		 */
		if (fColumns.isEmpty()) {
			fColumns.add("*");
		}
		return true;
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
	 * Note: conditionOperator is set as "" or NULL means there is no condition operator,
	 * Available condition operators are NOT, OR, AND.
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
		if (!validateEmptyTableAndUpdateEmptyColumn() || !validateWhereConditions()) {
			return null;
		}

		String sql = null;
		if (distinctSelection) {
			sql = fQueryObjectType.sqlQueryType() + " " + SqlStatementStrings.SQL_TABLE_DISTINCT + " "
					+ buildSqlColumnsString() + " " + SqlStatementStrings.SQL_TABLE_FROM + " " + fTables.get(0)
					+ SqlStatementStrings.SQL_TABLE_WHERE + " " + buildSqlWhereClause() + ";";
		} else {
			sql = fQueryObjectType.sqlQueryType() + " " + buildSqlColumnsString() + " "
					+ SqlStatementStrings.SQL_TABLE_FROM + " " + fTables.get(0) + SqlStatementStrings.SQL_TABLE_WHERE + " "
					+ buildSqlWhereClause() + ";";
		}
		return fJdbcDbConn.executeQueryObject(sql);
	}

	/**
	 * Validate SQL WHERE condition setups.
	 *
	 * fTable, fFields, fOperators, fConditions, fValues should not be
	 * empty, and amount of these lists should be same.
	 *
	 * @return True if all lists are matching valid rules.
	 */
	private boolean validateWhereConditions() {
		for (SqlCriteriaCondition criteria : fCriteriaConditions) {
			if (!criteria.validateCriteriaCondition()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Build SQL WHERE clause string from fCriteriaConditions lists.
	 *
	 * @return SQL WHERE string
	 */
	private String buildSqlWhereClause() {
		StringBuilder whereClause = new StringBuilder();
		for (SqlCriteriaCondition sqlCriteria : fCriteriaConditions) {
			if ((sqlCriteria.getValue() instanceof String)
					&& !sqlCriteria.getValue().equals("")) {
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
		if(!validateEmptyTableAndUpdateEmptyColumn()) {
			return null;
		}

		String sql = fQueryObjectType.sqlQueryType() + " " + SqlStatementStrings.SQL_TABLE_COUNT + "("
				+ fColumns.get(0) + ") " + SqlStatementStrings.SQL_TABLE_FROM + " " + fTables.get(0) + ";";
		return fJdbcDbConn.executeQueryObject(sql);
	}

	/**
	 * Select and count specific column from the initialized table with WHERE fields
	 * filters.
	 *
	 * NOTE: Setting fColumns field to null if SELECT * all columns.
	 *
	 * Note: conditionOperator is set as "" or NULL means there is no condition operator,
	 * Available condition operators are NOT, OR, AND.
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
		if (!validateEmptyTableAndUpdateEmptyColumn() && !validateWhereConditions()) {
			return null;
		}

		String sql = null;
		if (distinctSelection) {
			sql = fQueryObjectType.sqlQueryType() + " " + SqlStatementStrings.SQL_TABLE_COUNT + "("
					+ SqlStatementStrings.SQL_TABLE_DISTINCT + " " + fColumns.get(0) + ") "
					+ SqlStatementStrings.SQL_TABLE_FROM + " " + fTables.get(0) + SqlStatementStrings.SQL_TABLE_WHERE + " "
					+ buildSqlWhereClause() + ";";
		} else {
			sql = fQueryObjectType.sqlQueryType() + " " + SqlStatementStrings.SQL_TABLE_COUNT + "("
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
		if (!validateEmptyTableAndUpdateEmptyColumn() || !validateOrderByColumnsAndOrderings()) {
			return null;
		}

		String sql = null;
		if (distinctSelection) {
			sql = fQueryObjectType.sqlQueryType() + " " + SqlStatementStrings.SQL_TABLE_DISTINCT + " "
					+ buildSqlColumnsString() + " " + SqlStatementStrings.SQL_TABLE_FROM + " " + fTables.get(0)
					+ " " + SqlStatementStrings.SQL_TABLE_ORDER_BY + " " + buildSqlOrderByClause() + ";";
		} else {
			sql = fQueryObjectType.sqlQueryType() + " " + buildSqlColumnsString() + " "
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
	 * Note: conditionOperator is set as "" or NULL means there is no condition operator,
	 * Available condition operators are NOT, OR, AND.
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
		if (!validateEmptyTableAndUpdateEmptyColumn()
				|| !validateWhereConditions() || !validateOrderByColumnsAndOrderings()) {
			return null;
		}

		String sql = null;
		if (distinctSelection) {
			sql = fQueryObjectType.sqlQueryType() + " " + SqlStatementStrings.SQL_TABLE_DISTINCT + " "
					+ buildSqlColumnsString() + " " + SqlStatementStrings.SQL_TABLE_FROM + " " + fTables.get(0)
					+ SqlStatementStrings.SQL_TABLE_WHERE + " " + buildSqlWhereClause()
					+ " " + SqlStatementStrings.SQL_TABLE_ORDER_BY + " " + buildSqlOrderByClause() + ";";
		} else {
			sql = fQueryObjectType.sqlQueryType() + " " + buildSqlColumnsString() + " "
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
		if(!validateEmptyTableAndUpdateEmptyColumn()) {
			return null;
		}

		String sql = fQueryObjectType.sqlQueryType() + " " + SqlStatementStrings.SQL_TABLE_MIN + "("
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
		if(!validateEmptyTableAndUpdateEmptyColumn()) {
			return null;
		}

		String sql = fQueryObjectType.sqlQueryType() + " " + SqlStatementStrings.SQL_TABLE_MAX + "("
				+ fColumns.get(0) + ") " + SqlStatementStrings.SQL_TABLE_FROM + " " + fTables.get(0) + ";";
		return fJdbcDbConn.executeQueryObject(sql);
	}

	/**
	 * Select and MIN specific column from the initialized table with WHERE fields
	 * filters.
	 *
	 * NOTE: Setting fColumns field to null if SELECT * all columns.
	 *
	 * Note: conditionOperator is set as "" or NULL means there is no condition operator,
	 * Available condition operators are NOT, OR, AND.
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
		if (!validateEmptyTableAndUpdateEmptyColumn() || !validateWhereConditions()) {
			return null;
		}

		String sql = fQueryObjectType.sqlQueryType() + " " + SqlStatementStrings.SQL_TABLE_MIN + "("
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
	 * Note: conditionOperator is set as "" or NULL means there is no condition operator,
	 * Available condition operators are NOT, OR, AND.
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
		if (!validateEmptyTableAndUpdateEmptyColumn() || !validateWhereConditions()) {
			return null;
		}

		String sql = fQueryObjectType.sqlQueryType() + " " + SqlStatementStrings.SQL_TABLE_MAX + "("
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
		if(!validateEmptyTableAndUpdateEmptyColumn()) {
			return null;
		}

		String sql = fQueryObjectType.sqlQueryType() + " " + SqlStatementStrings.SQL_TABLE_AVG + "("
				+ fColumns.get(0) + ") " + SqlStatementStrings.SQL_TABLE_FROM + " " + fTables.get(0) + ";";
		return fJdbcDbConn.executeQueryObject(sql);
	}

	/**
	 * Select and AVG specific column from the initialized table with WHERE fields
	 * filters.
	 *
	 * NOTE: Setting fColumns field to null if SELECT * all columns.
	 *
	 * Note: conditionOperator is set as "" or NULL means there is no condition operator,
	 * Available condition operators are NOT, OR, AND.
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
		if (!validateEmptyTableAndUpdateEmptyColumn() || !validateWhereConditions()) {
			return null;
		}

		String sql = null;
		if (distinctSelection) {
			sql = fQueryObjectType.sqlQueryType() + " " + SqlStatementStrings.SQL_TABLE_AVG + "("
					+ SqlStatementStrings.SQL_TABLE_DISTINCT + " " + fColumns.get(0) + ") "
					+ SqlStatementStrings.SQL_TABLE_FROM + " " + fTables.get(0) + SqlStatementStrings.SQL_TABLE_WHERE + " "
					+ buildSqlWhereClause() + ";";
		} else {
			sql = fQueryObjectType.sqlQueryType() + " " + SqlStatementStrings.SQL_TABLE_AVG + "("
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
		if(!validateEmptyTableAndUpdateEmptyColumn()) {
			return null;
		}

		String sql = fQueryObjectType.sqlQueryType() + " " + SqlStatementStrings.SQL_TABLE_SUM + "("
				+ fColumns.get(0) + ") " + SqlStatementStrings.SQL_TABLE_FROM + " " + fTables.get(0) + ";";
		return fJdbcDbConn.executeQueryObject(sql);
	}

	/**
	 * Select and SUM specific column from the initialized table with WHERE fields
	 * filters.
	 *
	 * NOTE: Setting fColumns field to null if SELECT * all columns.
	 *
	 * Note: conditionOperator is set as "" or NULL means there is no condition operator,
	 * Available condition operators are NOT, OR, AND.
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
		if (!validateEmptyTableAndUpdateEmptyColumn() || !validateWhereConditions()) {
			return null;
		}

		String sql = null;
		if (distinctSelection) {
			sql = fQueryObjectType.sqlQueryType() + " " + SqlStatementStrings.SQL_TABLE_SUM + "("
					+ SqlStatementStrings.SQL_TABLE_DISTINCT + " " + fColumns.get(0) + ") "
					+ SqlStatementStrings.SQL_TABLE_FROM + " " + fTables.get(0) + SqlStatementStrings.SQL_TABLE_WHERE + " "
					+ buildSqlWhereClause() + ";";
		} else {
			sql = fQueryObjectType.sqlQueryType() + " " + SqlStatementStrings.SQL_TABLE_SUM + "("
					+ fColumns.get(0) + ") " + SqlStatementStrings.SQL_TABLE_FROM + " " + fTables.get(0)
					+ SqlStatementStrings.SQL_TABLE_WHERE + " " + buildSqlWhereClause() + ";";
		}
		return fJdbcDbConn.executeQueryObject(sql);
	}

	/**
	 * Select target columns from tables by joining.
	 *
	 * NOTE: Setting fColumns field to null if SELECT * all columns.
	 *
	 * Note: conditionOperator is set as "" or NULL means there is no condition
	 * operator, Available condition operators are NOT, OR, AND.
	 *
	 * Scenario:
	 *
	 * <pre>
	 *  SELECT Orders.OrderID, Customers.CustomerName
	 *  FROM Orders
	 *  INNER JOIN Customers ON Orders.CustomerID = Customers.CustomerID;
	 * </pre>
	 *
	 * <pre>
	 * 	SELECT Orders.OrderID, Customers.CustomerName, Shippers.ShipperName
	 *  FROM ((Orders
	 *  INNER JOIN Customers ON Orders.CustomerID = Customers.CustomerID)
	 *  INNER JOIN Shippers ON Orders.ShipperID = Shippers.ShipperID);
	 * </pre>
	 *
	 * @param distinctSelection
	 *			True if only select distinct lines
	 * @return ResultSet SQL execution results
	 */
	public ResultSet selectColumnsJoinTablesOnConditions(boolean distinctSelection) {
		if (!validateEmptyTableAndUpdateEmptyColumn() || !validateJoinConditions()) {
			return null;
		}

		String sql = null;
		if (distinctSelection) {
			sql = fQueryObjectType.sqlQueryType() + " "
					+ SqlStatementStrings.SQL_TABLE_DISTINCT + " " + buildSqlColumnsString() + " "
					+ SqlStatementStrings.SQL_TABLE_FROM + buildSqlJoinClause() + ";";
		} else {
			sql = fQueryObjectType.sqlQueryType() + " " + buildSqlColumnsString() + " "
					+ SqlStatementStrings.SQL_TABLE_FROM + buildSqlJoinClause() + ";";
		}
		return fJdbcDbConn.executeQueryObject(sql);
	}

	/**
	 * Validate SQL JOIN condition setups.
	 *
	 * fTable, fCriteriaConditions, fJoinTypes should not be empty, and amount of
	 * fTable should be the amount of fCriteriaConditions + 1.
	 *
	 * @return True if all lists are matching valid rules.
	 */
	private boolean validateJoinConditions() {
		for (SqlCriteriaCondition criteria : fCriteriaConditions) {
			if (!criteria.validateCriteriaCondition()) {
				return false;
			}
		}
		if (fJoinTypes.isEmpty()) {
			LOGGER.severe("Failed to select fileds from table, join types are missing.");
			return false;
		}
		if (fTables.size() != fCriteriaConditions.size() + 1
				|| fCriteriaConditions.size() != fJoinTypes.size()) {
			LOGGER.severe("Failed to select fileds from table, join types, ceriteria conditions"
					+ ", and tables are not matching.");
			return false;
		}
		return true;
	}

	/**
	 * Build SQL JOIN clause string from fTable, fCriteriaConditions,
	 * fJoinTypes lists.
	 *
	 * @return SQL JOIN string
	 */
	private String buildSqlJoinClause() {
		StringBuilder joinClause = new StringBuilder();
		for (int i = 0; i < fJoinTypes.size(); i ++) {
			if (i == 0) {
				joinClause.append("(");
				joinClause.append(fTables.get(i) + " ");
				joinClause.append(fJoinTypes.get(i) + " ");
				joinClause.append(fTables.get(i + 1) + " ");
				joinClause.append(SqlStatementStrings.SQL_TABLE_ON + " ");
				joinClause.append(fCriteriaConditions.get(i).getConditionOperator()
						+ " " + fCriteriaConditions.get(i).getFiled() + fCriteriaConditions.get(i).getOperator()
						+ fCriteriaConditions.get(i).getValue() + ") ");
			} else {
				joinClause.insert(0, '(');
				joinClause.append(fJoinTypes.get(i) + " ");
				joinClause.append(fTables.get(i + 1) + " ");
				joinClause.append(SqlStatementStrings.SQL_TABLE_ON + " ");
				joinClause.append(fCriteriaConditions.get(i).getFiled() + fCriteriaConditions.get(i).getOperator()
						+ fCriteriaConditions.get(i).getValue() + ") ");
			}
		}
		return joinClause.toString();
	}
}
