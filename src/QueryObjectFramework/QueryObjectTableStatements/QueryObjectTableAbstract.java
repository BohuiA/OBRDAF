package QueryObjectFramework.QueryObjectTableStatements;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import QueryObjectFramework.CommonClasses.SqlCriteriaCondition;
import QueryObjectFramework.CommonClasses.SqlQueryTypes;
import QueryObjectFramework.JdbcDatabaseConnection.JdbcDatabaseConnection;

/**
 * Abstract class for Query Object pattern. Contains basic SQL table
 * operations and interface for customized SQL operations.
 *
 * SQL statement tutorial website link:
 * https://www.w3schools.com/sql/sql_select.asp
 *
 * @author Bohui Axelsson
 */
public class QueryObjectTableAbstract {
	protected final SqlQueryTypes fQueryObjectType;
	protected final @NonNull JdbcDatabaseConnection fJdbcDbConn;
	protected final @NonNull List<String> fTables = new ArrayList<>();
	protected final @NonNull List<String> fColumns = new ArrayList<>();
	protected final List<SqlCriteriaCondition> fCriteriaConditions = new ArrayList<>();

	public QueryObjectTableAbstract(SqlQueryTypes queryObjectType, @NonNull JdbcDatabaseConnection jdbcDbConn,
			@NonNull List<String> tables, List<String> columns,
			List<SqlCriteriaCondition> selectCriterias) {
		fQueryObjectType = queryObjectType;
		fJdbcDbConn = jdbcDbConn;
		fTables.addAll(tables);
		if (columns != null) {
			fColumns.addAll(columns);
		} else {
			fColumns.clear();
		}
		if (selectCriterias != null) {
			fCriteriaConditions.addAll(selectCriterias);
		} else {
			fCriteriaConditions.clear();
		}
	}

	public QueryObjectTableAbstract(SqlQueryTypes queryObjectType, @NonNull JdbcDatabaseConnection jdbcDbConn) {
		fQueryObjectType = queryObjectType;
		fJdbcDbConn = jdbcDbConn;
	}
}
