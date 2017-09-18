package QueryObjectFramework.queryObjectBase;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.sun.istack.internal.NotNull;

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
	private static final Logger LOGGER = Logger.getLogger(QueryObjectAbstract.class.getName());

	public final SqlQueryTypes fQueryObjectType;
	public final @NotNull JdbcDatabaseConnection fJdbcDbConn;
	public final @NotNull List<String> fTables = new ArrayList<>();
	public final @NotNull List<String> fColumns = new ArrayList<>();
	public final List<SqlCriteriaCondition> fCriteriaConditions = new ArrayList<>();

	public QueryObjectAbstract(SqlQueryTypes queryObjectType, @NotNull JdbcDatabaseConnection jdbcDbConn,
			@NotNull List<String> tables, @NotNull List<String> columns,
			List<SqlCriteriaCondition> selectCriterias) {
		fQueryObjectType = queryObjectType;
		fJdbcDbConn = jdbcDbConn;
		fTables.addAll(tables);
		fColumns.addAll(columns);
		if (selectCriterias != null) {
			fCriteriaConditions.addAll(selectCriterias);
		}
	}

	public QueryObjectAbstract(SqlQueryTypes queryObjectType, @NotNull JdbcDatabaseConnection jdbcDbConn) {
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
	 * Validate SQL WHERE condition setups.
	 *
	 * fTable, fFields, fOperators, fConditions, fValues should not be
	 * empty, and amount of these lists should be same.
	 *
	 * @return True if all lists are matching valid rules.
	 */
	public boolean validateWhereConditions() {
		if (fTables.isEmpty()) {
			LOGGER.severe("Failed to select fileds from table, table name is missing.");
			return false;
		}
		for (SqlCriteriaCondition criteria : fCriteriaConditions) {
			if (!criteria.validateCriteriaCondition()) {
				return false;
			}
		}
		return true;
	}

	public void addTable(@NotNull String table) {
		fTables.add(table);
	}

	public void setTables(@NotNull List<String> tables) {
		clearTables();
		fTables.addAll(tables);
	}

	public void clearTables() {
		fTables.clear();
	}

	public void addColumn(@NotNull String column) {
		fColumns.add(column);
	}

	public void setColumns(@NotNull List<String> columns) {
		clearColumns();
		fColumns.addAll(columns);
	}

	public void clearColumns() {
		fColumns.clear();
	}

	public void addCriteriaCondition(@NotNull SqlCriteriaCondition criteriaCondition) {
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
