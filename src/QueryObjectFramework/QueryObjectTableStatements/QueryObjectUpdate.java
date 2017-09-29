package QueryObjectFramework.QueryObjectTableStatements;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.eclipse.jdt.annotation.NonNull;

import QueryObjectFramework.CommonClasses.SqlCriteriaCondition;
import QueryObjectFramework.CommonClasses.SqlQueryTypes;
import QueryObjectFramework.CommonClasses.SqlStatementStrings;
import QueryObjectFramework.JdbcDatabaseConnection.JdbcDatabaseConnection;
import QueryObjectFramework.QueryObjectAbstract.QueryObjectTableAbstract;

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
	private final @NonNull List<Object> fUpdateValues = new ArrayList<>();

	/**
	 * Create an UPDATE query object with only JDBC connection.
	 *
	 * @param jdbcDbConn
	 * 			JDBC database connection
	 */
	public QueryObjectUpdate(@NonNull JdbcDatabaseConnection jdbcDbConn) {
		super(SqlQueryTypes.UPDATE, jdbcDbConn);
	}

	/**
	 * Create an UPDATE query object with update values.
	 *
	 * Example:
	 *
	 * <pre>
	 *  UPDATE table_name
	 *  SET column1 = value1, column2 = value2, ...;
	 * </pre>
	 *
	 * @param jdbcDbConn
	 * 			JDBC database connection
	 * @param tables
	 * 			Names of table for selecting, should be only one
	 * @param columns
	 * 			Names of columns for selection
	 * @param updateValues
	 * 			Updating values for UPDATE. The amount of updateVaules
	 * 			should be the same as the amount of columns.
	 */
	public QueryObjectUpdate(@NonNull JdbcDatabaseConnection jdbcDbConn, @NonNull List<String> tables,
			@NonNull List<String> columns, @NonNull List<Object> updateValues) {
		super(SqlQueryTypes.UPDATE, jdbcDbConn, tables, columns, null);
		if (updateValues != null) {
			fUpdateValues.addAll(updateValues);
		}
	}

	/**
	 * Create an UPDATE query object with WHERE conditions.
	 *
	 * Example:
	 *
	 * <pre>
	 *  UPDATE table_name
	 *  SET column1 = value1, column2 = value2, ...
	 *  WHERE condition;
	 * </pre>
	 *
	 * @param jdbcDbConn
	 * 			JDBC database connection
	 * @param tables
	 * 			Names of table for selecting, should be only one
	 * @param columns
	 * 			Names of columns for selection
	 * @param selectCriterias
	 * 			List that contains filtering selection criteria
	 * @param updateValues
	 * 			Updating values for UPDATE. The amount of updateVaules
	 * 			should be the same as the amount of columns.
	 */
	public QueryObjectUpdate(@NonNull JdbcDatabaseConnection jdbcDbConn, @NonNull List<String> tables,
			@NonNull List<String> columns, @NonNull List<SqlCriteriaCondition> selectCriterias, @NonNull List<Object> updateValues) {
		super(SqlQueryTypes.UPDATE, jdbcDbConn, tables, columns, selectCriterias);
		if (updateValues != null) {
			fUpdateValues.addAll(updateValues);
		}
	}

	public void addUpdateValue(@NonNull String updateValue) {
		fUpdateValues.add(updateValue);
	}

	public void setUpdateValues(List<String> updateValues) {
		clearUpdateValues();
		if (updateValues != null) {
			fUpdateValues.addAll(updateValues);
		}
	}

	public void clearUpdateValues() {
		fUpdateValues.clear();
	}

	/**
	 * Update values of all columns in table.
	 *
	 * NOTE: Only one table name should be added into object.
	 *
	 * Scenario:
	 *
	 * <pre>
	 *  UPDATE table_name
	 *  SET column1 = value1, column2 = value2, ...;
	 * </pre>
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
	 * Validate fTables list only contains one table. fColumns and fUpdateValues are
	 * not empty, meanwhile, the amounts of fColumns and fUpdateValues is same.
	 *
	 * @return True if fTables only contains one table, the amounts of columns and
	 *         updateValues are equal.
	 */
	private boolean validatUpdateAllColumnsWithValues() {
		if (fTables.size() != 1) {
			LOGGER.severe("Failed to update values in table, more than one table are provided.");
			return false;
		}
		if (fColumns.isEmpty()) {
			LOGGER.severe("Failed to update values in table, columns are missing.");
			return false;
		}
		if (fUpdateValues.isEmpty()) {
			LOGGER.severe("Failed to update values in table, update values are missing.");
			return false;
		}
		if (fUpdateValues.size() != fColumns.size()) {
			LOGGER.severe("Failed to update values in table, update values and columns are not matching.");
			return false;
		}
		return true;
	}

	/**
	 * Build SQL columns and update values string contains all target update fields.
	 *
	 * Example:
	 *
	 * <pre>
	 * column1 = value1, column2 = value2, ...
	 * </pre>
	 *
	 * @return SQL columns string
	 */
	private String buildSqlUpdateColumnsVaulesClause() {
		StringBuilder updateColumsAndValues = new StringBuilder();
		for (int i = 0; i < fColumns.size(); i ++) {
			if (fUpdateValues.get(i) instanceof String) {
				updateColumsAndValues.append(fColumns.get(i) + " = " + "'" + fUpdateValues.get(i) + "'" + ",");
			} else {
				updateColumsAndValues.append(fColumns.get(i) + " = " + fUpdateValues.get(i) + ",");
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
	 * <pre>
	 *  UPDATE table_name
	 *  SET column1 = value1, column2 = value2, ...
	 *  WHERE condition;
	 * </pre>
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
		StringBuilder whereClause = new StringBuilder("");
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
}
