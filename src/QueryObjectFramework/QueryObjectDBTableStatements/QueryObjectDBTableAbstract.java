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
	 * Build SQL CREATE TABLE columns string.
	 *
	 * @return SQL CREATE TABLE columns string
	 */
	protected String buildColumnsAndColumnDataTypes() {
		StringBuilder createTableColumnsClause = new StringBuilder();
		for (QueryObjectDBTableColumn tableColumn : fTableColumns) {
			createTableColumnsClause.append(tableColumn.getColumnName() + " " + tableColumn.getColumnDataType().getSqlColumnDataType() + " "
					+ tableColumn.getColumnConstraint().getColumnConstraintsString() + ",");
		}
		createTableColumnsClause.deleteCharAt(createTableColumnsClause.length() - 1);
		return createTableColumnsClause.toString();
	}

	/**
	 * Create a UNIQUE constraint string.
	 *
	 * @return UNIQUE constraint string.
	 */
	protected String createAppendConstraintForColumns() {
		int uniqueColumnAmount = 0;
		StringBuilder uniqueTableColumnsClause = new StringBuilder("");
		for (QueryObjectDBTableColumn tableColumn : fTableColumns) {
			if (tableColumn.getColumnConstraint().getUniqueState()) {
				uniqueTableColumnsClause.append(tableColumn.getColumnName() + ",");
				uniqueColumnAmount ++;
			}
		}
		uniqueTableColumnsClause.deleteCharAt(uniqueTableColumnsClause.length() - 1);

		/*
		 * If only one column is UNIQUE, creating clause as "UNIQUE (ID)"; Otherwise,
		 * creating clause as CONSTRAINT UC_Person UNIQUE (ID,LastName).
		 *
		 * TODO: Introducing customized multiple UNIQUE columns name.
		 */
		if (uniqueColumnAmount == 1) {
			uniqueTableColumnsClause.insert(0, ", " + SqlStatementStrings.SQL_DATABASE_UNIQUE + "(");
			uniqueTableColumnsClause.append(")");
		} else if (uniqueColumnAmount > 1) {
			uniqueTableColumnsClause.insert(0,
					", " + SqlStatementStrings.SQL_DATABASE_CONSTRAINT
							+ SqlStatementStrings.SQL_DATABASE_MULTIPLE_UNIQUE_COLUMNS + this.fTableName + " "
							+ SqlStatementStrings.SQL_DATABASE_UNIQUE + "(");
			uniqueTableColumnsClause.append(")");
		}
		return uniqueTableColumnsClause.toString();
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
