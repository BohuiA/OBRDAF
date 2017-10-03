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
 * Query object class for Insert statement.
 *
 * @author Bohui Axelsson
 */
public class QueryObjectInsertInto extends QueryObjectTableAbstract {
	private static final Logger LOGGER = Logger.getLogger(QueryObjectInsertInto.class.getName());

	/*
	 * Insert SQL statement specific operation settings
	 */
	private final @NonNull List<QueryObjectTableColumnAndValue> fInsertItems = new ArrayList<>();

	/**
	 * Create a INSERT INTO query object.
	 *
	 * Example:
	 *
	 * <pre>
	 * 	INSERT INTO table_name
	 *  VALUES (value1, value2, value3, ...);
	 * </pre>
	 *
	 * <pre>
	 * 	INSERT INTO table_name (column1, column2, column3, ...)
     * 	VALUES (value1, value2, value3, ...);
	 * </pre>
	 *
	 * @param jdbcDbConn
	 *            JDBC database connection
	 * @param tables
	 *            Names of table for inserting, should be only one
	 * @param insertItems
	 *            Inserting values for INSERT. Without columns set, the order of
	 *            insert values should be matching exactly with real database table.
	 *
	 *            TIP: Creating QueryObjectTableColumnAndValue item with only
	 *            updateValue for inserting values without column names.
	 */
	public QueryObjectInsertInto(@NonNull JdbcDatabaseConnection jdbcDbConn, @NonNull List<String> tables,
			@NonNull List<QueryObjectTableColumnAndValue> insertItems) {
		super(SqlQueryTypes.INSERT, jdbcDbConn, tables, null, null);
		fInsertItems.addAll(insertItems);
	}

	/**
	 * Insert target values into table.
	 *
	 * NOTE: Only one table name should be added into object.
	 *
	 * Scenario:
	 *
	 * <pre>
     *  INSERT INTO Customers (Street, Name, Country)
     *  VALUES ('Cardinal', 'Stavanger', 'Norway');
	 * </pre>
	 *
	 * <pre>
	 * 	INSERT INTO Customers
	 *  VALUES ('Cardinal', 'Tom B. Erichsen', 'Skagen 21', 'Stavanger', '4006', 'Norway');
	 * </pre>
	 *
	 * @return ResultSet SQL execution results
	 */
	public ResultSet insertIntoTableOnlyWithValues() {
		if (!validateTableAmountAndValuesNotEmpty()) {
			return null;
		}

		String sql = fQueryObjectType.sqlQueryType() + " " + fTables.get(0)
				 + " " + SqlStatementStrings.SQL_TABLE_VALUES + " (" + buildSqlInsertValuesClause() + " );";
		return fJdbcDbConn.executeQueryObject(sql);
	}

	/**
	 * Validate fTables list should only contain one table name, meanwhile,
	 * fInsertitems list should not be empty.
	 *
	 * @return True if fTables contains one table and fInsertItems is not empty.
	 */
	private boolean validateTableAmountAndValuesNotEmpty() {
		if (fTables.isEmpty()) {
			LOGGER.severe("Failed to insert values into table, table name is missing.");
			return false;
		}
		if (fTables.size() != 1) {
			LOGGER.severe("Failed to insert values into table, more than one table are provided.");
			return false;
		}
		if (fInsertItems.isEmpty()) {
			LOGGER.severe("Failed to insert values into table, inserting items are missing.");
			return false;
		}
		return true;
	}

	/**
	 * Build SQL insert values string contains all inserting values.
	 *
	 * @return SQL insert values string
	 */
	private String buildSqlInsertValuesClause() {
		StringBuilder insertValuesClause = new StringBuilder();
		for (QueryObjectTableColumnAndValue insertItem : fInsertItems) {
			if (insertItem.isStringUpdateValue()) {
				insertValuesClause.append("'" + insertItem.getUpdateValue() + "'" + ",");
			} else {
				insertValuesClause.append(insertItem.getUpdateValue() + ",");
			}
		}
		insertValuesClause.deleteCharAt(insertValuesClause.length() - 1);
		return insertValuesClause.toString();
	}

	/**
	 * Insert target values into table.
	 *
	 * NOTE: Only one table name should be added into object.
	 *
	 * Scenario:
	 *
	 * <pre>
	 *  INSERT INTO Customers (CustomerName, ContactName, Address, City, PostalCode, Country)
	 *  VALUES ('Cardinal', 'Tom B. Erichsen', 'Skagen 21', 'Stavanger', '4006', 'Norway');
	 * </pre>
	 *
	 * @return ResultSet SQL execution results
	 */
	public ResultSet insertIntoTableWithColumnsAndValues() {
		if (!validateTableAmountAndValuesNotEmpty() || !validateColumnsNotEmpty()) {
			return null;
		}

		String sql = fQueryObjectType.sqlQueryType() + " " + fTables.get(0) + " " + "( " + buildSqlInsertColumnsClause()
				+ " )" + " " + SqlStatementStrings.SQL_TABLE_VALUES + " (" + buildSqlInsertValuesClause() + " );";
		return fJdbcDbConn.executeQueryObject(sql);
	}

	/**
	 * Validate column names for insertItem should not be empty.
	 *
	 * @return True if column names for insertItem is not empty.
	 */
	private boolean validateColumnsNotEmpty() {
		for (QueryObjectTableColumnAndValue insertItem : fInsertItems) {
			if (insertItem.getUpdateColumnName().equals("")) {
				LOGGER.severe("Failed to insert values into table, column names should be set for all update values.");
				return false;
			}
		}
		return true;
	}

	/**
	 * Build SQL columns string contains all target column names.
	 *
	 * @return SQL columns string
	 */
	private String buildSqlInsertColumnsClause() {
		StringBuilder colunms = new StringBuilder();
		for (QueryObjectTableColumnAndValue insertItem : fInsertItems) {
			colunms.append(insertItem.getUpdateColumnName() + ",");
		}
		colunms.deleteCharAt(colunms.length() - 1);
		return colunms.toString();
	}
}
