package QueryObjectFramework.QueryObjectDBTableStatements;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.eclipse.jdt.annotation.NonNull;

import QueryObjectFramework.CommonClasses.SqlColumnDataType;
import QueryObjectFramework.CommonClasses.SqlQueryTypes;
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

	public final SqlQueryTypes fQueryObjectType;
	public final @NonNull JdbcDatabaseConnection fJdbcDbConn;
	public final @NonNull List<String> fColumns = new ArrayList<>();
	public final @NonNull List<SqlColumnDataType> fColumnDataTypes = new ArrayList<>();

	public String fTableName = null;

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

	public void setTableName(@NonNull String tableName) {
		fTableName = tableName;
	}

	public void addColumnName(@NonNull String columnName) {
		fColumns.add(columnName);
	}

	public void addColumnDataType(@NonNull SqlColumnDataType columnDataType) {
		fColumnDataTypes.add(columnDataType);
	}

	/**
	 * Validate fTableName is not null.
	 *
	 * @return True if fDBName is null.
	 */
	public boolean validateTableNameNotNull() {
		if (fTableName == null) {
			LOGGER.severe("Failed to operate table operation, table name is missing.");
			return false;
		}
		return true;
	}

	/**
	 * Validate columns and columns data types are not null and matching.
	 *
	 * @return True if columns and columns data types are not null and matching.
	 */
	public boolean validateColumnsAndDataTypesNotNullAndMatching() {
		if (fColumns == null) {
			LOGGER.severe("Failed to operate table operation, column names are missing.");
			return false;
		}
		if (fColumnDataTypes == null) {
			LOGGER.severe("Failed to operate table operation, column data types are missing.");
			return false;
		}
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
		return true;
	}
}
