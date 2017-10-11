package QueryObjectFramework.QueryObjectTableStatements;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

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
	protected final @NonNull List<QueryObjectTableCriteriaCondition> fCriteriaConditions = new ArrayList<>();

	public QueryObjectTableAbstract(SqlQueryTypes queryObjectType, @NonNull JdbcDatabaseConnection jdbcDbConn,
			@NonNull List<String> tables, @NonNull List<String> columns) {
		fQueryObjectType = queryObjectType;
		fJdbcDbConn = jdbcDbConn;
		fTables.addAll(tables);
		fColumns.addAll(columns);
		fCriteriaConditions.clear();
	}

	public QueryObjectTableAbstract(SqlQueryTypes queryObjectType, @NonNull JdbcDatabaseConnection jdbcDbConn,
			@NonNull List<String> tables, @NonNull List<String> columns,
			@NonNull List<QueryObjectTableCriteriaCondition> selectCriterias) {
		fQueryObjectType = queryObjectType;
		fJdbcDbConn = jdbcDbConn;
		fTables.addAll(tables);
		fColumns.addAll(columns);
		fCriteriaConditions.addAll(selectCriterias);
	}

	public QueryObjectTableAbstract(SqlQueryTypes queryObjectType, @NonNull JdbcDatabaseConnection jdbcDbConn) {
		fQueryObjectType = queryObjectType;
		fJdbcDbConn = jdbcDbConn;
		fTables.clear();
		fColumns.clear();
		fCriteriaConditions.clear();
	}

	public QueryObjectTableAbstract(SqlQueryTypes queryObjectType, @NonNull JdbcDatabaseConnection jdbcDbConn,
			@NonNull List<String> tables) {
		fQueryObjectType = queryObjectType;
		fJdbcDbConn = jdbcDbConn;
		fTables.addAll(tables);
		fColumns.clear();
		fCriteriaConditions.clear();
	}
}
