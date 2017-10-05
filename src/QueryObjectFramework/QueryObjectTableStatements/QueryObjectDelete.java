package QueryObjectFramework.QueryObjectTableStatements;

import java.sql.ResultSet;
import java.util.List;
import java.util.logging.Logger;

import org.eclipse.jdt.annotation.NonNull;

import QueryObjectFramework.CommonClasses.SqlQueryTypes;
import QueryObjectFramework.CommonClasses.SqlStatementStrings;
import QueryObjectFramework.JdbcDatabaseConnection.JdbcDatabaseConnection;

/**
 * Query object class for Delete statement.
 *
 * NOTE: Be careful when deleting records in a table! Notice the WHERE clause in
 * the DELETE statement. The WHERE clause specifies which record(s) that should
 * be deleted. If you omit the WHERE clause, all records in the table will be
 * deleted!
 *
 * @author Bohui Axelsson
 */
public class QueryObjectDelete extends QueryObjectTableAbstract {
	private static final Logger LOGGER = Logger.getLogger(QueryObjectDelete.class.getName());

	/**
	 * Create an DELETE query object with only JDBC connection.
	 *
	 * @param jdbcDbConn
	 * 			JDBC database connection
	 */
	public QueryObjectDelete(@NonNull JdbcDatabaseConnection jdbcDbConn) {
		super(SqlQueryTypes.DELETE, jdbcDbConn);
	}

	/**
	 * Create an DELETE query object with WHERE conditions.
	 *
	 * Example:
	 *
	 * <example>
	 *  UDELETE FROM table_name
	 *  WHERE condition;
	 * </example>
	 *
	 * @param jdbcDbConn
	 * 			JDBC database connection
	 * @param tables
	 * 			Names of table for selecting, should be only one
	 * @param selectCriterias
	 * 			List that contains filtering selection criteria
	 */
	public QueryObjectDelete(@NonNull JdbcDatabaseConnection jdbcDbConn, @NonNull List<String> tables,
			@NonNull List<QueryObjectTableCriteriaCondition> selectCriterias) {
		super(SqlQueryTypes.DELETE, jdbcDbConn, tables, null, selectCriterias);
	}

	/**
	 * Delete columns of a table.
	 *
	 * NOTE: Only one table name should be added into object.
	 *
	 * Scenario:
	 *
	 * <example>
	 *  DELETE FROM Customers
	 *  WHERE CustomerName='Alfreds Futterkiste';
	 * </example>
	 *
	 * <example>
	 * DELETE * FROM table_name;
	 * </example>
	 *
	 * NOTE: To delete all columns, just set deleteAll parameter to
	 * true, do not need to add/update columns list.
	 *
	 * @param deleteAll
	 * 			True to delete all columns from the table
	 * @return ResultSet SQL execution results
	 */
	public ResultSet deleteColumnsWhereConditions(boolean deleteAll) {
		if (!validatDeleteColumnsWhereConditions()) {
			return null;
		}

		String sql = null;
		if (deleteAll) {
			sql = fQueryObjectType.sqlQueryType() + " * " + SqlStatementStrings.SQL_TABLE_FROM
					+ fTables.get(0) + ";";
		} else {
			String whereClause = buildSqlWhereClause();
			if (whereClause.equals("")) {
				sql = fQueryObjectType.sqlQueryType() + " " + SqlStatementStrings.SQL_TABLE_FROM
						+ ";";
			} else {
				sql = fQueryObjectType.sqlQueryType() + " " + SqlStatementStrings.SQL_TABLE_FROM
						+ " " + SqlStatementStrings.SQL_TABLE_WHERE + buildSqlWhereClause() + ";";
			}
		}
		return fJdbcDbConn.executeQueryObject(sql);
	}

	/**
	 * Validate fTables list only contains one table. And all criteria
	 * conditions meet the valid requirement.
	 *
	 * @return True if fTables only contains one table, and all criteria
	 * conditions are meeting valid requirement.
	 */
	private boolean validatDeleteColumnsWhereConditions() {
		if (fTables.size() != 1) {
			LOGGER.severe("Failed to delete columns in table, more than one table are provided.");
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
