package QueryObjectFramework.queryObjectBase;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import QueryObjectFramework.common.SqlQueryTypes;
import QueryObjectFramework.jdbc.JdbcDatabaseConnection;

/**
 * Abstract class for Query Object pattern. Contains basic SQL database
 * operations and interface for customized SQL operations.
 *
 * SQL statement tutorial website link:
 * https://www.w3schools.com/sql/sql_select.asp
 *
 * @author Bohui Axelsson
 */
public class QueryObjectAbstract {
	private static final Logger LOGGER = Logger.getLogger(QueryObjectAbstract.class.getName());

	public final SqlQueryTypes fQueryObjectType;
	public final JdbcDatabaseConnection fJdbcDbConn;
	public final List<String> fTables = new ArrayList<>();
	public final List<String> fColumns = new ArrayList<>();
	/*
	 * Scenario: field operator value Example, country = 'USA'
	 */
	public final List<String> fFields = new ArrayList<>();
	public final List<Object> fValues = new ArrayList<>();
	public final List<String> fOperators = new ArrayList<>();
	/*
	 * Scenario: condition1 field1 operator1 value1 condition2 field2 operator2
	 * value2 ..
	 * Example, NOT country='USA' AND name='Bohui Axelsson'
	 */
	public final List<String> fConditions = new ArrayList<>();

	public QueryObjectAbstract(SqlQueryTypes queryObjectType, JdbcDatabaseConnection jdbcDbConn, List<String> tables,
			List<String> columns, List<String> fileds, List<Object> values, List<String> operators, List<String> conditions) {
		fQueryObjectType = queryObjectType;
		fJdbcDbConn = jdbcDbConn;
		fTables.addAll(tables);
		fColumns.addAll(columns);
		fFields.addAll(fileds);
		fValues.addAll(values);
		fOperators.addAll(operators);
		fConditions.addAll(conditions);
	}

	public QueryObjectAbstract(SqlQueryTypes queryObjectType, JdbcDatabaseConnection jdbcDbConn) {
		fQueryObjectType = queryObjectType;
		fJdbcDbConn = jdbcDbConn;
	}

	/**
	 * Check fTables is not empty, and update fColumns with '*' if iColumns is
	 * empty.
	 *
	 * @return True if fTables is empty.
	 */
	public boolean checkEmptyTableAndUpdateEmptyColumn() {
		if (fTables.isEmpty()) {
			LOGGER.severe("Failed to select columns from table, table name is missing.");
			return true;
		}
		/*
		 * If fColumns is empty, it means selecting all columns,
		 * updating fColumns with one '*'.
		 */
		if (fColumns.isEmpty()) {
			fColumns.add("*");
		}
		return false;
	}

	/**
	 * Validate SQL WHERE fields searching filtering setup.
	 *
	 * fTable, fFields, fOperators, fConditions, fValues should not be
	 * empty, and amount of these lists should be same.
	 *
	 * @return True if all lists are matching valid rules.
	 */
	public boolean validateFieldsFiltering() {
		if (fTables.isEmpty()) {
			LOGGER.severe("Failed to select fileds from table, table name is missing.");
			return false;
		}
		if (fFields.isEmpty()) {
			LOGGER.severe("Failed to select fileds from table, fileds name is missing.");
			return false;
		}
		if (fValues.isEmpty()) {
			LOGGER.severe("Failed to select fileds from table, values names are missing.");
			return false;
		}
		if (fOperators.isEmpty()) {
			LOGGER.severe("Failed to select fileds from table, operators are missing.");
			return false;
		}
		if (fConditions.isEmpty()) {
			LOGGER.severe("Failed to select fileds from table, conditions are missing.");
			return false;
		}
		if (fFields.size() != fValues.size() || fValues.size() != fOperators.size()
				|| fConditions.size() != fValues.size()) {
			LOGGER.severe("Failed to select fileds from table, fileds, values, operators and conditions are not matching.");
			return false;
		}
		return true;
	}

	public void addTable(String table) {
		fTables.add(table);
	}

	public void setTables(List<String> tables) {
		fTables.addAll(tables);
	}

	public void clearTables() {
		fTables.clear();
	}

	public void addColumn(String column) {
		fColumns.add(column);
	}

	public void setColumns(List<String> columns) {
		fColumns.addAll(columns);
	}

	public void clearColumns() {
		fColumns.clear();
	}

	public void addField(String field) {
		fFields.add(field);
	}

	public void setFields(List<String> fields) {
		fFields.addAll(fields);
	}

	public void clearFields() {
		fFields.clear();
	}

	public void addValue(Object value) {
		fValues.add(value);
	}

	public void setValues(List<Object> values) {
		fValues.addAll(values);
	}

	public void clearValues() {
		fValues.clear();
	}

	public void addOperator(String operator) {
		fOperators.add(operator);
	}

	public void setOperators(List<String> operators) {
		fOperators.addAll(operators);
	}

	public void clearOperators() {
		fOperators.clear();
	}

	public void addCondition(String condition) {
		fConditions.add(condition);
	}

	public void setConditions(List<String> conditions) {
		fConditions.addAll(conditions);
	}

	public void clearConditions() {
		fConditions.clear();
	}
}
