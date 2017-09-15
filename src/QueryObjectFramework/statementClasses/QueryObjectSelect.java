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
			List<String> fields, List<String> values, List<String> operators) {
		super(SqlQueryTypes.SELECT, jdbcDbConn, tables, columns, fields, values, operators);
	}

	/**
	 * Select all columns from the initialized table.
	 *
	 * Scenario:
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

		/*
		 * SQL statement: "SELECT * FROM table_name;"
		 */
		String sql = SqlQueryTypes.SELECT.sqlQueryType() + " * " + SqlStatementStrings.SQL_TABLE_FROM + " "
				+ fTables.get(0) + ";";
		return fJdbcDbConn.executeQueryObject(sql);
	}

	/**
	 * Select specific columns from the initialized table.
	 *
	 * Scenario:
	 * <pre>
	 *  SELECT column1, column2, .. FROM table_name;
	 * </pre>
	 *
	 * @return ResultSet SQL execution results
	 */
	public ResultSet selectColumns() {
		if (fTables.isEmpty()) {
			LOGGER.severe("Failed to select columns from table, table name is missing.");
			return null;
		}
		if (fColumns.isEmpty()) {
			LOGGER.severe("Failed to select columns from table, columns names are missing.");
			return null;
		}

		StringBuilder colunms = new StringBuilder();
		for (String filedName : fColumns) {
			colunms.append(filedName + ",");
		}
		colunms.deleteCharAt(colunms.length() - 1);

		String sql = SqlQueryTypes.SELECT.sqlQueryType() + " " + colunms.toString() + " "
				+ SqlStatementStrings.SQL_TABLE_FROM + " " + fTables.get(0) + ";";
		return fJdbcDbConn.executeQueryObject(sql);
	}

	/**
	 * Select all columns from the initialized table with
	 * WHERE fields filters.
	 *
	 * Scenario:
	 * <pre>
	 *  SELECT * FROM table_name WHERE field1 operator1 value1, ...;
	 * </pre>
	 *
	 * @return ResultSet SQL execution results
	 */
	public ResultSet selectAllByFileds() {
		if (fTables.isEmpty()) {
			LOGGER.severe("Failed to select fileds from table, table name is missing.");
			return null;
		}
		if (fFields.isEmpty()) {
			LOGGER.severe("Failed to select fileds from table, fileds name is missing.");
			return null;
		}
		if (fValues.isEmpty()) {
			LOGGER.severe("Failed to select fileds from table, values names are missing.");
			return null;
		}
		if (fOperators.isEmpty()) {
			LOGGER.severe("Failed to select fileds from table, operators are missing.");
			return null;
		}
		if (fFields.size() != fValues.size() || fValues.size() != fOperators.size()) {
			LOGGER.severe("Failed to select fileds from table, fileds, values and operators are not matching.");
			return null;
		}

		/*
		 * Build WHERE clause
		 */
		StringBuilder whereClause = new StringBuilder();
		for (int i = 0; i < fFields.size(); i ++) {
			whereClause.append(fFields.get(i) + " " + fOperators.get(i) + " " + fValues.get(i) + ",");
		}
		whereClause.deleteCharAt(whereClause.length() - 1);

		String sql = SqlQueryTypes.SELECT.sqlQueryType() + " * "
				+ SqlStatementStrings.SQL_TABLE_FROM + " " + fTables.get(0)
				+ SqlStatementStrings.SQL_TABLE_WHERE + " "
				+ whereClause + ";";
		return fJdbcDbConn.executeQueryObject(sql);
	}

	/**
	 * Select specific columns from the initialized table with
	 * WHERE fields filters.
	 *
	 * Scenario:
	 * <pre>
	 *  SELECT column1, column2, .. FROM table_name WHERE field1 operator1 value1, ...;
	 * </pre>
	 *
	 * @return ResultSet SQL execution results
	 */
	public ResultSet selectColumnsByFileds() {
		if (fTables.isEmpty()) {
			LOGGER.severe("Failed to select fileds from table, table name is missing.");
			return null;
		}
		if (fColumns.isEmpty()) {
			LOGGER.severe("Failed to select fileds from table, columns names are missing.");
		}
		if (fFields.isEmpty()) {
			LOGGER.severe("Failed to select fileds from table, fileds name is missing.");
			return null;
		}
		if (fValues.isEmpty()) {
			LOGGER.severe("Failed to select fileds from table, values names are missing.");
			return null;
		}
		if (fOperators.isEmpty()) {
			LOGGER.severe("Failed to select fileds from table, operators are missing.");
			return null;
		}
		if (fFields.size() != fValues.size() || fValues.size() != fOperators.size()) {
			LOGGER.severe("Failed to select fileds from table, fileds, values and operators are not matching.");
			return null;
		}

		/*
		 * Build columns strings
		 */
		StringBuilder columns = new StringBuilder();
		for (String filedName : fColumns) {
			columns.append(filedName + ",");
		}
		columns.deleteCharAt(columns.length() - 1);

		/*
		 * Build WHERE clause
		 */
		StringBuilder whereClause = new StringBuilder();
		for (int i = 0; i < fFields.size(); i ++) {
			whereClause.append(fFields.get(i) + " " + fOperators.get(i) + " " + fValues.get(i) + ",");
		}
		whereClause.deleteCharAt(whereClause.length() - 1);

		String sql = SqlQueryTypes.SELECT.sqlQueryType() + " " + columns.toString() + " "
				+ SqlStatementStrings.SQL_TABLE_FROM + " " + fTables.get(0)
				+ SqlStatementStrings.SQL_TABLE_WHERE + " "
				+ whereClause + ";";
		return fJdbcDbConn.executeQueryObject(sql);
	}
}
