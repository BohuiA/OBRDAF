package QueryObjectFramework.queryObjectBase;

import java.util.ArrayList;
import java.util.List;

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
	public final SqlQueryTypes fQueryObjectType;
	public final JdbcDatabaseConnection fJdbcDbConn;
	public final List<String> fTables = new ArrayList<>();
	public final List<String> fColumns = new ArrayList<>();
	public final List<String> fFields = new ArrayList<>();
	public final List<String> fValues = new ArrayList<>();
	public final List<String> fOperators = new ArrayList<>();

	public QueryObjectAbstract(SqlQueryTypes queryObjectType, JdbcDatabaseConnection jdbcDbConn, List<String> tables,
			List<String> columns, List<String> fileds, List<String> values, List<String> operators) {
		fQueryObjectType = queryObjectType;
		fJdbcDbConn = jdbcDbConn;
		fTables.addAll(tables);
		fColumns.addAll(columns);
		fFields.addAll(fileds);
		fValues.addAll(values);
		fOperators.addAll(operators);
	}

	public QueryObjectAbstract(SqlQueryTypes queryObjectType, JdbcDatabaseConnection jdbcDbConn) {
		fQueryObjectType = queryObjectType;
		fJdbcDbConn = jdbcDbConn;
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

	public void addValue(String value) {
		fValues.add(value);
	}

	public void setValues(List<String> values) {
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
}
