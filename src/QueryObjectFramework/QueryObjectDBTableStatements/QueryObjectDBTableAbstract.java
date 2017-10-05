package QueryObjectFramework.QueryObjectDBTableStatements;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.eclipse.jdt.annotation.NonNull;

import QueryObjectFramework.CommonClasses.SqlQueryTypes;
import QueryObjectFramework.CommonClasses.SqlStatementStrings;
import QueryObjectFramework.JdbcDatabaseConnection.JdbcDatabaseConnection;

/**
 * Abstract class for Query Object pattern. Contains basic SQL database
 * table operations and interface for customized SQL table operations.
 *
 * Including CREATE TABLE, DROP TABLE, ALTER TABLE operations.
 *
 * SQL statement tutorial website link:
 * https://www.w3schools.com/sql/sql_select.asp
 *
 * @author Bohui Axelsson
 */
public class QueryObjectDBTableAbstract {
	private static final Logger LOGGER = Logger.getLogger(QueryObjectDBTableAbstract.class.getName());

	protected final SqlQueryTypes fQueryObjectType;
	protected final @NonNull JdbcDatabaseConnection fJdbcDbConn;
	protected final @NonNull List<QueryObjectDBTableColumn> fTableColumns = new ArrayList<>();
	protected String fTableName = "";

	public QueryObjectDBTableAbstract(SqlQueryTypes queryObjectType, @NonNull JdbcDatabaseConnection jdbcDbConn) {
		fQueryObjectType = queryObjectType;
		fJdbcDbConn = jdbcDbConn;
	}

	public QueryObjectDBTableAbstract(SqlQueryTypes queryObjectType, @NonNull JdbcDatabaseConnection jdbcDbConn,
			@NonNull String tableName) {
		fQueryObjectType = queryObjectType;
		fJdbcDbConn = jdbcDbConn;
		if (tableName != null) {
			fTableName = tableName;
		}
	}

	public QueryObjectDBTableAbstract(SqlQueryTypes queryObjectType, @NonNull JdbcDatabaseConnection jdbcDbConn,
			@NonNull String tableName, @NonNull List<QueryObjectDBTableColumn> tableColumns) {
		fQueryObjectType = queryObjectType;
		fJdbcDbConn = jdbcDbConn;
		if (tableName != null) {
			fTableName = tableName;
		}
		if (tableColumns != null) {
			fTableColumns.addAll(tableColumns);
		}
	}

	/**
	 * Create columns setting string.
	 *
	 * @return A full columns setting string
	 */
	protected String buildColumnSettingString() {
		StringBuilder tableColumnsClause = new StringBuilder("");
		StringBuilder uniqueCoulmnNames = new StringBuilder("");
		int uniqueColumnNamesAmount = 0;

		for (QueryObjectDBTableColumn tableColumn : fTableColumns) {
			String columnConstraints = tableColumn.getColumnConstraint().getColumnConstraintsString();
			if (columnConstraints.contains(SqlStatementStrings.SQL_DATABASE_UNIQUE)) {
				uniqueColumnNamesAmount++;
				uniqueCoulmnNames.append(tableColumn.getColumnName() + ",");
				columnConstraints = columnConstraints.replace(SqlStatementStrings.SQL_DATABASE_UNIQUE, "");
			}

			tableColumnsClause.append(tableColumn.getColumnName() + " "
					+ tableColumn.getColumnDataType().getSqlColumnDataType() + " " + columnConstraints + ",");
		}

		/*
		 * Post process table column setting string on UNIQUE constraint.
		 *
		 * If only one column is UNIQUE, creating clause as "UNIQUE (ID)"; Otherwise,
		 * creating clause as CONSTRAINT UC_Person UNIQUE (ID,LastName).
		 *
		 * TODO: Introducing customized multiple UNIQUE columns name.
		 */
		if (uniqueColumnNamesAmount == 0) {
			tableColumnsClause.deleteCharAt(tableColumnsClause.length() - 1);
		} else if (uniqueColumnNamesAmount == 1) {
			uniqueCoulmnNames.deleteCharAt(uniqueCoulmnNames.length() - 1);
			tableColumnsClause
					.append(" " + SqlStatementStrings.SQL_DATABASE_UNIQUE + "(" + uniqueCoulmnNames.toString() + ")");
		} else if (uniqueColumnNamesAmount > 1) {
			uniqueCoulmnNames.deleteCharAt(uniqueCoulmnNames.length() - 1);
			tableColumnsClause.append(SqlStatementStrings.SQL_DATABASE_CONSTRAINT
					+ SqlStatementStrings.SQL_DATABASE_MULTIPLE_UNIQUE_COLUMNS + this.fTableName + " "
					+ SqlStatementStrings.SQL_DATABASE_UNIQUE + "(" + uniqueCoulmnNames.toString() + ")");
		}

		return tableColumnsClause.toString();
	}

	/**
	 * Validate there are no NULL item in the table column list.
	 *
	 * @return True if all items in fTableColumns are not NULL.
	 */
	protected boolean validateTableColumnsNotNull() {
		for (QueryObjectDBTableColumn tableColumn : fTableColumns) {
			if (tableColumn == null) {
				LOGGER.severe("Failed to operate table operation, table column item is null.");
				return false;
			}
		}
		return true;
	}
}
