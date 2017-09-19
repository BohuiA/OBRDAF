package QueryObjectFramework.queryObjectBase;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import QueryObjectFramework.common.SqlCriteriaCondition;
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
	public final @NonNull JdbcDatabaseConnection fJdbcDbConn;
	public final @NonNull List<String> fTables = new ArrayList<>();
	public final @NonNull List<String> fColumns = new ArrayList<>();
	public final List<SqlCriteriaCondition> fCriteriaConditions = new ArrayList<>();

	public QueryObjectAbstract(SqlQueryTypes queryObjectType, @NonNull JdbcDatabaseConnection jdbcDbConn,
			@NonNull List<String> tables, @NonNull List<String> columns,
			List<SqlCriteriaCondition> selectCriterias) {
		fQueryObjectType = queryObjectType;
		fJdbcDbConn = jdbcDbConn;
		fTables.addAll(tables);
		fColumns.addAll(columns);
		if (selectCriterias != null) {
			fCriteriaConditions.addAll(selectCriterias);
		}
	}

	public QueryObjectAbstract(SqlQueryTypes queryObjectType, @NonNull JdbcDatabaseConnection jdbcDbConn) {
		fQueryObjectType = queryObjectType;
		fJdbcDbConn = jdbcDbConn;
	}

	/**
	 * Check fTables is not empty, and update fColumns with '*' if iColumns is
	 * empty.
	 *
	 * @return True if fTables is not empty.
	 */
	public boolean validateEmptyTableAndUpdateEmptyColumn() {
		return true;
	}

	/**
	 * Validate SQL WHERE condition setups.
	 *
	 * fTable, fFields, fOperators, fConditions, fValues should not be
	 * empty, and amount of these lists should be same.
	 *
	 * @return True if all lists are matching valid rules.
	 */
	public boolean validateWhereConditions() {
		return false;
	}

	public void addTable(@NonNull String table) {
		fTables.add(table);
	}

	public void setTables(@NonNull List<String> tables) {
		clearTables();
		fTables.addAll(tables);
	}

	public void clearTables() {
		fTables.clear();
	}

	public void addColumn(@NonNull String column) {
		fColumns.add(column);
	}

	public void setColumns(@NonNull List<String> columns) {
		clearColumns();
		fColumns.addAll(columns);
	}

	public void clearColumns() {
		fColumns.clear();
	}

	public void addCriteriaCondition(@NonNull SqlCriteriaCondition criteriaCondition) {
		fCriteriaConditions.add(criteriaCondition);
	}

	public void setCriteriaConditions(List<SqlCriteriaCondition> criteriaConditions) {
		clearCriteriaConditions();
		if (criteriaConditions != null) {
			fCriteriaConditions.addAll(criteriaConditions);
		}
	}

	public void clearCriteriaConditions() {
		fCriteriaConditions.clear();
	}
}
