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
 * Query object class for Update statement.
 *
 * NOTE: Be careful when updating records in a table! Notice the WHERE clause in
 * the UPDATE statement. The WHERE clause specifies which record(s) that should
 * be updated. If you omit the WHERE clause, all records in the table will be
 * updated!
 *
 * @author Bohui Axelsson
 */
public class QueryObjectUpdate extends QueryObjectTableAbstract {
	private static final Logger LOGGER = Logger.getLogger(QueryObjectUpdate.class.getName());

	/*
	 * Update SQL statement specific operation settings
	 */
	private final @NonNull List<QueryObjectTableColumnAndValue> fUpdateItems = new ArrayList<>();

	/**
	 * Create an UPDATE query object with update values.
	 *
	 * Example:
	 *
	 * <example>
	 *  UPDATE table_name
	 *  SET column1 = value1, column2 = value2, ...;
	 * </example>
	 *
	 * @param jdbcDbConn
	 * 			JDBC database connection
	 * @param tables
	 * 			Names of table for selecting, should be only one
	 * @param updateItems
	 * 			Updating column and value lists
	 */
	public QueryObjectUpdate(@NonNull JdbcDatabaseConnection jdbcDbConn, @NonNull List<String> tables,
			@NonNull List<QueryObjectTableColumnAndValue> updateItems) {
		super(SqlQueryTypes.UPDATE, jdbcDbConn, tables, null, null);
		fUpdateItems.addAll(updateItems);
	}

	/**
	 * Create an UPDATE query object with WHERE conditions.
	 *
	 * Example:
	 *
	 * <example>
	 *  UPDATE table_name
	 *  SET column1 = value1, column2 = value2, ...
	 *  WHERE condition;
	 * </example>
	 *
	 * @param jdbcDbConn
	 * 			JDBC database connection
	 * @param tables
	 * 			Names of table for selecting, should be only one
	 * @param selectCriterias
	 * 			List that contains filtering selection criteria
	 * @param updateItems
	 * 			Updating column and value lists
	 */
	public QueryObjectUpdate(@NonNull JdbcDatabaseConnection jdbcDbConn, @NonNull List<String> tables,
			@NonNull List<QueryObjectTableCriteriaCondition> selectCriterias,
			@NonNull List<QueryObjectTableColumnAndValue> updateItems) {
		super(SqlQueryTypes.UPDATE, jdbcDbConn, tables, null, selectCriterias);
		fUpdateItems.addAll(updateItems);
	}

	/**
	 * Update values of all columns in table.
	 *
	 * NOTE: Only one table name should be added into object.
	 *
	 * Scenario:
	 *
	 * <example>
	 *  UPDATE table_name
	 *  SET column1 = value1, column2 = value2, ...;
	 * </example>
	 *
	 * @return ResultSet SQL execution results
	 */
	public ResultSet updateAllColumnsWithValues() {
		if (!validatUpdateAllColumnsWithValues()) {
			return null;
		}

		String sql = fQueryObjectType.sqlQueryType() + " " + fTables.get(0) + " " + SqlStatementStrings.SQL_TABLE_SET
				+ " " + buildSqlUpdateColumnsVaulesClause() + " " + ";";
		return fJdbcDbConn.executeQueryObject(sql);
	}

	/**
	 * Validate fTables list only contains one table. fUpdateItems is not empty.
	 *
	 * @return True if fTables only contains one table, fUpdateItems is not empty.
	 */
	private boolean validatUpdateAllColumnsWithValues() {
		if (fTables.size() != 1) {
			LOGGER.severe("Failed to update values in table, more than one table are provided.");
			return false;
		}
		if (fUpdateItems.isEmpty()) {
			LOGGER.severe("Failed to update values in table, update columns and values are missing.");
			return false;
		}
		return true;
	}

	/**
	 * Build SQL columns and update values string contains all target update fields.
	 *
	 * Example:
	 *
	 * <example>
	 * column1 = value1, column2 = value2, ...
	 * </example>
	 *
	 * @return SQL columns string
	 */
	private String buildSqlUpdateColumnsVaulesClause() {
		StringBuilder updateColumsAndValues = new StringBuilder();
		for (QueryObjectTableColumnAndValue updateItem : fUpdateItems) {
			if (updateItem.isStringUpdateValue()) {
				updateColumsAndValues.append(updateItem.getUpdateColumnName() + " = " + "'" + updateItem.getUpdateValue() + "'" + ",");
			} else {
				updateColumsAndValues.append(updateItem.getUpdateColumnName() + " = " + updateItem.getUpdateValue() + ",");
			}
		}
		updateColumsAndValues.deleteCharAt(updateColumsAndValues.length() - 1);
		return updateColumsAndValues.toString();
	}

	/**
	 * Update values of columns in table filtering by WHERE.
	 *
	 * NOTE: Only one table name should be added into object.
	 *
	 * Scenario:
	 *
	 * <example>
	 *  UPDATE table_name
	 *  SET column1 = value1, column2 = value2, ...
	 *  WHERE condition;
	 * </example>
	 *
	 * @return ResultSet SQL execution results
	 */
	public ResultSet updateColumnsWithValuesWhereConditions() {
		if (!validatUpdateColumnsWithValuesWhereConditions()) {
			return null;
		}

		String whereClause = buildSqlWhereClause();
		String sql = null;
		if (whereClause.equals("")) {
			sql = fQueryObjectType.sqlQueryType() + " " + fTables.get(0) + " " + SqlStatementStrings.SQL_TABLE_SET
					+ " " + buildSqlUpdateColumnsVaulesClause() + ";";
		} else {
			sql = fQueryObjectType.sqlQueryType() + " " + fTables.get(0) + " " + SqlStatementStrings.SQL_TABLE_SET
					+ " " + buildSqlUpdateColumnsVaulesClause() + " " + SqlStatementStrings.SQL_TABLE_WHERE + " " +
					 buildSqlWhereClause() + ";";
		}
		return fJdbcDbConn.executeQueryObject(sql);
	}

	/**
	 * Validate fTables list only contains one table. fColumns and fUpdateValues are
	 * not empty, meanwhile, the amounts of fColumns and fUpdateValues is same. At
	 * the same time, validating criteria conditions.
	 *
	 * @return True if fTables only contains one table, the amounts of columns and
	 *         updateValues are equal, as well as all criteria conditions are matching
	 *         validate rule.
	 */
	private boolean validatUpdateColumnsWithValuesWhereConditions() {
		if (!validatUpdateAllColumnsWithValues()) {
			return false;
		}
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
		StringBuilder whereClause = new StringBuilder("");
		for (QueryObjectTableCriteriaCondition sqlCriteria : fCriteriaConditions) {
			if (sqlCriteria.isStringCriteriaValue()) {
				whereClause.append(sqlCriteria.getConditionOperator() + " " + sqlCriteria.getFiled()
						+ sqlCriteria.getOperator() + "'" + sqlCriteria.getValue() + "' ");
			} else {
				whereClause.append(sqlCriteria.getConditionOperator() + " " + sqlCriteria.getFiled()
						+ sqlCriteria.getOperator() + sqlCriteria.getValue() + " ");
			}
		}
		return whereClause.toString();
	}
}
