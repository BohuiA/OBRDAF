package QueryObjectFramework.QueryObjectDBTableStatements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import org.eclipse.jdt.annotation.NonNull;

import QueryObjectFramework.CommonClasses.SqlColumnDataType;
import QueryObjectFramework.CommonClasses.SqlDBTableConstraints;
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
	protected final @NonNull List<String> fColumns = new ArrayList<>();
	protected final @NonNull List<SqlColumnDataType> fColumnDataTypes = new ArrayList<>();
	protected final @NonNull List<SqlDBTableConstraints> fColumnConstraints = new ArrayList<>();

	protected String fTableName = null;

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
			@NonNull String tableName, @NonNull List<String> columns) {
		fQueryObjectType = queryObjectType;
		fJdbcDbConn = jdbcDbConn;
		if (tableName != null) {
			fTableName = tableName;
		}
		if (columns != null) {
			fColumns.addAll(columns);
		}
	}

	public QueryObjectDBTableAbstract(SqlQueryTypes queryObjectType, @NonNull JdbcDatabaseConnection jdbcDbConn,
			@NonNull String tableName, @NonNull List<String> columns, @NonNull List<SqlColumnDataType> columnDataTypes) {
		fQueryObjectType = queryObjectType;
		fJdbcDbConn = jdbcDbConn;
		if (tableName != null) {
			fTableName = tableName;
		}
		if (columns != null) {
			fColumns.addAll(columns);
		}
		if (columnDataTypes != null) {
			fColumnDataTypes.addAll(columnDataTypes);
		}
	}

	public QueryObjectDBTableAbstract(SqlQueryTypes queryObjectType, @NonNull JdbcDatabaseConnection jdbcDbConn,
			@NonNull String tableName, @NonNull List<String> columns, @NonNull List<SqlColumnDataType> columnDataTypes,
			@NonNull List<SqlDBTableConstraints> columnConstraints) {
		fQueryObjectType = queryObjectType;
		fJdbcDbConn = jdbcDbConn;
		if (tableName != null) {
			fTableName = tableName;
		}
		if (columns != null) {
			fColumns.addAll(columns);
		}
		if (columnDataTypes != null) {
			fColumnDataTypes.addAll(columnDataTypes);
		}
		if (columnConstraints != null) {
			fColumnConstraints.addAll(columnConstraints);
		}
	}

	public void setTableName(@NonNull String tableName) {
		fTableName = tableName;
	}

	public void addColumnName(@NonNull String columnName) {
		fColumns.add(columnName);
	}

	public void addColumnDataType(@NonNull SqlColumnDataType columnDataType) {
		fColumnDataTypes.add(columnDataType);
	}

	public void addColumnConstraints(SqlDBTableConstraints columnConstraint) {
		fColumnConstraints.add(columnConstraint);
	}

	/**
	 * Validate fTableName is not null.
	 *
	 * @return True if fDBName is null.
	 */
	protected boolean validateTableNameNotNull() {
		if (fTableName == null) {
			LOGGER.severe("Failed to operate table operation, table name is missing.");
			return false;
		}
		return true;
	}

	/**
	 * Check UNIQUE columns and create a UNIQUE constraint string.
	 *
	 * @return UNIQUE constraint string.
	 */
	protected String checkAndCreateUniqueConstraintColumns() {
		int uniqueColumnAmount = 0;
		StringBuilder uniqueTableColumnsClause = new StringBuilder("");
		for (int i = 0; i < fColumnConstraints.size(); i ++) {
			if (fColumnConstraints.get(i).getUniqueState()) {
				 uniqueTableColumnsClause.append(fColumns.get(i) + ",");
				 uniqueColumnAmount ++;
			}
		}
		uniqueTableColumnsClause.deleteCharAt(uniqueTableColumnsClause.length() - 1);

		/*
		 * If only one column is UNIQUE, creating clause as "UNIQUE (ID)"; Otherwise,
		 * creating clause as CONSTRAINT UC_Person UNIQUE (ID,LastName).
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
	 * Validate columns and columns data types are not null and matching.
	 * Meanwhile, ColumnConstraints amount must be matched to columns list amount or empty.
	 *
	 * @return True if above validate rules are matching.
	 */
	protected boolean validateColumnsSettingsMatching() {
		if (fColumns.size() != fColumnDataTypes.size()) {
			LOGGER.severe("Failed to operate table operation, column data types and columns are not matching.");
			return false;
		}
		for (String column : fColumns) {
			if (column == null) {
				LOGGER.severe("Failed to operate table operation, column item is null.");
				return false;
			}
		}
		for (SqlColumnDataType columnDataType : fColumnDataTypes) {
			if (columnDataType == null) {
				LOGGER.severe("Failed to operate table operation, columnDataType item is null.");
				return false;
			}
		}
		if (!fColumnConstraints.isEmpty() && fColumnConstraints.size() != fColumns.size()) {
			LOGGER.severe("Failed to operate table operation, ColumnConstraints should be empty or as same amount of columns.");
			return false;
		}
		/*
		 * Replace all null constraint to a default SqlDBTableConstraints instance.
		 */
		Collections.replaceAll(fColumnConstraints, null, new SqlDBTableConstraints());

		return true;
	}
}
