package QueryObjectFramework.QueryObjectTableStatements;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.eclipse.jdt.annotation.NonNull;

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
	private final @NonNull List<QueryObjectTableJoinType> fJoinTypes = new ArrayList<>();
	private final @NonNull List<QueryObjectTableOrderBy> fOrderByLists = new ArrayList<>();
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
	 * <example>
	 * 	SELECT column1, column2, ... FROM table_name;
	 * </example>
	 *
	 * @param jdbcDbConn
	 * 			JDBC database connection
	 * @param tables
	 * 			Names of table for selecting, should be only one
	 * @param columns
	 * 			Names of columns for selection, setting NULL for
	 * 			selecting all columns.
	 */
	public QueryObjectSelect(@NonNull JdbcDatabaseConnection jdbcDbConn, @NonNull List<String> tables,
			List<String> columns) {
		super(SqlQueryTypes.SELECT, jdbcDbConn, tables, columns, null);
	}

	/**
	 * Create a SELECT query object with WHERE filtering grammar.
	 *
	 * Example:
	 *
	 * <example>
	 *  SELECT column1, column2, ...
	 *  FROM table_name
	 *  WHERE NOT condition1 AND condition2 AND NOT condition3 ...;
	 * </example>
	 *
	 * Note: conditionOperator is set as "" or NULL means there is no condition operator,
	 * Available condition operators are NOT, OR, AND.
	 *
	 * @param jdbcDbConn
	 * 			JDBC database connection
	 * @param tables
	 * 			Names of table for selecting, should be only one
	 * @param columns
	 * 			Names of columns for selection, setting NULL for
	 * 			selecting all columns.
	 * @param selectCriterias
	 * 			List that contains filtering selection criteria.
	 */
	public QueryObjectSelect(@NonNull JdbcDatabaseConnection jdbcDbConn, @NonNull List<String> tables,
			List<String> columns, @NonNull List<QueryObjectTableCriteriaCondition> selectCriterias) {
		super(SqlQueryTypes.SELECT, jdbcDbConn, tables, columns, selectCriterias);
	}

	/**
	 * Create a SELECT query object with WHERE or/and ORDER BY grammar.
	 *
	 * Example:
	 *
	 * <example>
	 *  SELECT column1, column2, ...
	 *  FROM table_name
	 *  WHERE NOT condition1 AND condition2 AND NOT condition3 ...
	 *  ORDER BY column1, column2, ... ASC|DESC;
	 * </example>
	 *
	 * <example>
	 *  SELECT column1, column2, ...
	 *  FROM table_name
	 *  ORDER BY column1, column2, ... ASC|DESC;
	 *
	 *  NOTE: Pass NULL to selectCriterias parameter to ignore WHERE clause.
	 * </example>
	 *
	 * Note: Setting selectCriterias to NULL to ignore WHERE clause.
	 *
	 * @param jdbcDbConn
	 * 			JDBC database connection
	 * @param tables
	 * 			Names of table for selecting, should be only one
	 * @param columns
	 * 			Names of columns for selection, setting NULL for selecting all columns.
	 * @param selectCriterias
	 * 			List that contains filtering selection criteria.
	 * 			Setting to NULL for ignoring.
	 * @param orderByLists
	 * 			List that contains order by parameters
	 */
	public QueryObjectSelect(@NonNull JdbcDatabaseConnection jdbcDbConn, @NonNull List<String> tables,
			List<String> columns, List<QueryObjectTableCriteriaCondition> selectCriterias,
			@NonNull List<QueryObjectTableOrderBy> orderByLists) {
		super(SqlQueryTypes.SELECT, jdbcDbConn, tables, columns, selectCriterias);
		fOrderByLists.addAll(orderByLists);
	}

	/**
	 * Create a SELECT query object with WHERE, ODERBY and JOIN grammars.
	 *
	 * Example:
	 *  <example>
	 *  SELECT Customers.CustomerName, Orders.OrderID
	 *  FROM Customers
	 *  LEFT JOIN Orders ON Customers.CustomerID = Orders.CustomerID;
	 *
	 *  NOTE: Setting selectCriterias and orderByLists to NULL to ignore
	 *  WHRER and ORDER BY clauses.
	 * </example>
	 *
	 * <example>
	 *  SELECT Customers.CustomerName, Orders.OrderID
	 *  FROM Customers
	 *  LEFT JOIN Orders ON Customers.CustomerID = Orders.CustomerID
	 *  ORDER BY Customers.CustomerName ASC;
	 * </example>
	 *
	 * Note: conditionOperator is set as "" or NULL means there is no condition operator,
	 * Available condition operators are NOT, OR, AND.
	 *
	 * @param jdbcDbConn
	 * 			JDBC database connection
	 * @param tables
	 * 			Names of table for selecting, should be only one
	 * @param columns
	 * 			Names of columns for selection, setting NULL for
	 * 			selecting all columns.
	 * @param selectCriterias
	 * 			List that contains filtering selection criteria.
	 * 			Setting to NULL for ignoring.
	 * @param orderByLists
	 * 			List that contains order by parameters.
	 * 			Setting to NULL for ignoring
	 * @param joinTypes
	 * 			List that contains Join types defining in SqlJoinType class.
	 */
	public QueryObjectSelect(@NonNull JdbcDatabaseConnection jdbcDbConn, @NonNull List<String> tables,
			List<String> columns, List<QueryObjectTableCriteriaCondition> selectCriterias,
			List<QueryObjectTableOrderBy> orderByLists, @NonNull List<QueryObjectTableJoinType> joinTypes) {
		super(SqlQueryTypes.SELECT, jdbcDbConn, tables, columns, selectCriterias);
		if (orderByLists != null) {
			fOrderByLists.addAll(orderByLists);
		} else {
			fOrderByLists.clear();
		}
		fJoinTypes.addAll(joinTypes);

	}

	/**
	 * Select specific columns from the initialized table.
	 *
	 * NOTE: Setting fColumns field to null if SELECT * all columns.
	 *
	 * Scenario:
	 *
	 * <example>
	 *  SELECT <DISTINCT> column1, column2, .. FROM table_name;
	 * </example>
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
	 * <example>
	 *  SELECT <DISTINCT> column1, column2, .. FROM table_name WHERE conditionOperator1
	 *  field1 operator1 value1 ...;
	 * </example>
	 *
	 * <example>
	 * 	SELECT <DISTINCT> column1, column2, .. FROM Customers WHERE CustomerName LIKE '%or%';
	 * </example>
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
		for (QueryObjectTableCriteriaCondition criteria : fCriteriaConditions) {
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
		for (QueryObjectTableCriteriaCondition sqlCriteria : fCriteriaConditions) {
			if ((sqlCriteria.isStringCriteriaValue())
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
	 * <example>
	 *  SELECT COUNT(<DISTINCT> column) FROM table_name;
	 * </example>
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
	 * <example>
	 *  SELECT COUNT(<DISTINCT> column) FROM table_name WHERE conditionOperator1
	 *  field1 operator1 value1 ...;
	 * </example>
	 *
	 * <example>
	 * 	SELECT COUNT(<DISTINCT> *) FROM Customers WHERE CustomerName LIKE '%or%';
	 * </example>
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
	 * <example>
	 *  SELECT <DISTINCT> column1, column2, .. FROM table_name ORDER BY column1,
	 *  column2, ... ASC|DESC;
	 * </example>
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
	 * fOrderByOrderings can only be ASC or DESC.
	 *
	 * @return True if all lists are matching valid rules.
	 */
	private boolean validateOrderByColumnsAndOrderings() {
		for (QueryObjectTableOrderBy orderBy : fOrderByLists) {
			if (orderBy == null) {
				LOGGER.severe("Failed to select all fileds from table, Order by rule is null.");
				return false;
			}
			if (!orderBy.validateOrderByOrderings()) {
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
		for (QueryObjectTableOrderBy orderBy : fOrderByLists) {
			orderByClause.append(orderBy.getOrderByColumn() + " " + orderBy.getOrderByOrdering() + ",");
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
	 * <example>
	 *  SELECT <DISTINCT> column1, column2, .. FROM table_name WHERE conditionOperator1
	 *  field1 operator1 value1 ...
	 *  ORDER BY column1, column2, ... ASC|DESC;
	 * </example>
	 *
	 * <example>
	 * 	SELECT <DISTINCT> * FROM Customers WHERE CustomerName LIKE '%or%' ORDER BY column1,
	 * column2, ... ASC|DESC;
	 * </example>
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
	 * <example>
	 *  SELECT MIN(column) FROM table_name;
	 * </example>
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
	 * <example>
	 *  SELECT MAX(column) FROM table_name;
	 * </example>
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
	 * <example>
	 *  SELECT MIN(column) FROM table_name WHERE conditionOperator1 field1 operator1 value1 ...;
	 * </example>
	 *
	 * <example>
	 * 	SELECT MIN(<DISTINCT> *) FROM Customers WHERE CustomerName LIKE '%or%';
	 * </example>
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
	 * <example>
	 *  SELECT MAX(column) FROM table_name WHERE conditionOperator1 field1 operator1 value1 ...;
	 * </example>
	 *
	 * <example>
	 * 	SELECT MAX(<DISTINCT> *) FROM Customers WHERE CustomerName LIKE '%or%';
	 * </example>
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
	 * <example>
	 *  SELECT AVG(<DISTINCT> column) FROM table_name;
	 * </example>
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
	 * <example>
	 *  SELECT AVG(<DISTINCT> column) FROM table_name WHERE conditionOperator1
	 *  field1 operator1 value1 ...;
	 * </example>
	 *
	 * <example>
	 * 	SELECT AVG(<DISTINCT> *) FROM Customers WHERE CustomerName LIKE '%or%';
	 * </example>
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
	 * <example>
	 *  SELECT SUM(<DISTINCT> column) FROM table_name;
	 * </example>
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
	 * <example>
	 *  SELECT SUM(<DISTINCT> column) FROM table_name WHERE conditionOperator1
	 *  field1 operator1 value1 ...;
	 * </example>
	 *
	 * <example>
	 * 	SELECT SUM(<DISTINCT> *) FROM Customers WHERE CustomerName LIKE '%or%';
	 * </example>
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
	 * <example>
	 *  SELECT Orders.OrderID, Customers.CustomerName
	 *  FROM Orders
	 *  INNER JOIN Customers ON Orders.CustomerID = Customers.CustomerID;
	 * </example>
	 *
	 * <example>
	 * 	SELECT Orders.OrderID, Customers.CustomerName, Shippers.ShipperName
	 *  FROM ((Orders
	 *  INNER JOIN Customers ON Orders.CustomerID = Customers.CustomerID)
	 *  INNER JOIN Shippers ON Orders.ShipperID = Shippers.ShipperID);
	 * </example>
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
		for (QueryObjectTableCriteriaCondition criteria : fCriteriaConditions) {
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
