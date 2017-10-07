package QueryObjectFramework.QueryObjectDBTableStatements;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.eclipse.jdt.annotation.NonNull;

import QueryObjectFramework.CommonClasses.SqlStatementStrings;

/**
 * Builder class for column definations.
 * - buildColumnWithNameAndDataTypeAndConstraints()
 * - buildAppendConstraintsForColumns()
 * - buildColumnWithNames()
 * - buildColumnWithNameAndDataType()
 *
 * @author Bohui Axelsson
 */
public class QueryObjectDBTableColumnBuilder {
	private static final Logger LOGGER = Logger.getLogger(QueryObjectDBTableColumnBuilder.class.getName());

	protected final @NonNull List<QueryObjectDBTableColumn> fTableColumns = new ArrayList<>();

	/**
	 * Build and initialize DBTableColumnIndex instance with list of
	 * QueryObjectDBTableColumn instances.
	 *
	 * @param tableColumns
	 *            List of QueryObjectDBTableColumn instances.
	 */
	protected void buildDBTableColumnIndex(@NonNull List<QueryObjectDBTableColumn> tableColumns) {
		fTableColumns.addAll(tableColumns);
	}

	/**
	 * Validate there are no NULL item in the table column list.
	 *
	 * @return True if all items in fTableColumns are not NULL.
	 */
	protected boolean validateTableClolumsNotNull() {
		for (QueryObjectDBTableColumn tableColumn : fTableColumns) {
			if (tableColumn == null) {
				LOGGER.severe("Failed to operate table operation, table column item is null.");
				return false;
			}
		}
		return true;
	}

	/**
	 * Create columns setting string.
	 *
	 * <example>
	 *  column1 datatype constraint,
	 *  column2 datatype constraint,
	 *  column3 datatype constraint, ....
	 *  UNIQUE(column2),
	 *  PRIMARY KEY (column1)
	 * </example>
	 *
	 * TODO: Refactor function to remove duplicate code.
	 *
	 * @param tableName
	 *            Table name that needs to create column clause.
	 * @return A full columns setting string
	 */
	protected String buildColumnWithNameAndDataTypeAndConstraints(String tableName) {
		StringBuilder tableColumnsClause = new StringBuilder("");
		/*
		 * UUNIQE constraints
		 */
		StringBuilder uniqueCoulmnNames = new StringBuilder("");
		int uniqueColumnNamesAmount = 0;
		/*
		 * PRIMARY KEY constraints
		 */
		StringBuilder primaryKeyCoulmnNames = new StringBuilder("");
		int primaryKeyColumnNamesAmount = 0;

		/*
		 * Go through all table columns one by one and count amounts of
		 * UNIQUE and PRIMARY KEY constraints. Meanwhile, create tableColumnsClause
		 * with normal constraints.
		 */
		for (QueryObjectDBTableColumn tableColumn : fTableColumns) {
			String columnConstraints = tableColumn.getColumnConstraint().getColumnConstraintsString();
			if (columnConstraints.contains(SqlStatementStrings.SQL_DATABASE_UNIQUE)) {
				uniqueColumnNamesAmount++;
				uniqueCoulmnNames.append(tableColumn.getColumnName() + ",");
				columnConstraints = columnConstraints.replace(SqlStatementStrings.SQL_DATABASE_UNIQUE, "");
			} else if (columnConstraints.contains(SqlStatementStrings.SQL_DATABASE_PRIMARY_KEY)) {
				primaryKeyColumnNamesAmount++;
				primaryKeyCoulmnNames.append(tableColumn.getColumnName() + " ");
				columnConstraints = columnConstraints.replace(SqlStatementStrings.SQL_DATABASE_PRIMARY_KEY, "");
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
		if (uniqueColumnNamesAmount == 1) {
			uniqueCoulmnNames.deleteCharAt(uniqueCoulmnNames.length() - 1);
			tableColumnsClause
					.append(" " + SqlStatementStrings.SQL_DATABASE_UNIQUE + "(" + uniqueCoulmnNames.toString() + "),");
		} else if (uniqueColumnNamesAmount > 1) {
			uniqueCoulmnNames.deleteCharAt(uniqueCoulmnNames.length() - 1);
			tableColumnsClause.append(SqlStatementStrings.SQL_DATABASE_CONSTRAINT
					+ SqlStatementStrings.SQL_DATABASE_MULTIPLE_UNIQUE_COLUMNS + tableName + " "
					+ SqlStatementStrings.SQL_DATABASE_UNIQUE + "(" + uniqueCoulmnNames.toString() + "),");
		}

		/*
		 * Post process table column setting string on PRIMARY KEY constraint.
		 *
		 * Primary keys must contain UNIQUE values, and cannot contain NULL values. A
		 * table can have only one primary key, which may consist of single or multiple
		 * fields.
		 */
		if (primaryKeyColumnNamesAmount == 1) {
			primaryKeyCoulmnNames.deleteCharAt(primaryKeyCoulmnNames.length() - 1);
			tableColumnsClause
					.append(" " + SqlStatementStrings.SQL_DATABASE_PRIMARY_KEY + "(" + primaryKeyCoulmnNames.toString() + "),");
		} else if (primaryKeyColumnNamesAmount > 1) {
			primaryKeyCoulmnNames.deleteCharAt(primaryKeyCoulmnNames.length() - 1);
			tableColumnsClause.append(SqlStatementStrings.SQL_DATABASE_CONSTRAINT
					+ SqlStatementStrings.SQL_DATABASE_MULTIPLE_PRIMARY_KEY_COLUMNS + tableName + " "
					+ SqlStatementStrings.SQL_DATABASE_PRIMARY_KEY + "(" + primaryKeyCoulmnNames.toString() + "),");
		}

		/*
		 * Remove the last ',' char of clause.
		 */
		tableColumnsClause.deleteCharAt(tableColumnsClause.length() - 1);
		return tableColumnsClause.toString();
	}

	/**
	 * Create an appending constraint string.
	 *
	 * An appending constraint includes,
	 * - UNIQUE
	 * - PRIMARY KEY
	 *
	 * <example>
	 * 	UNQIUE (column_name0, column_name2, ..),
	 *  PRIMARY KEY(column_name0, column_name2, ..)
	 * </example>
	 *
	 * <example>
	 * 	CONSTRAINT UC_<table_name> UNQIUE (column_name0, column_name2, ..),
	 *  CONSTRAINT PK_<table_name> PRIMARY KEY (column_name0, column_name2, ..)
	 * </example>
	 *
	 * TODO: Refactor function to remove duplicate code.
	 *
	 * @param tableName
	 *            Table name that needs to create column clause.
	 * @return Appending constraint string.
	 */
	protected String buildAppendConstraintsForColumns(String tableName) {
		StringBuilder appendingClause = new StringBuilder("");
		/*
		 * UUNIQE constraints
		 */
		StringBuilder uniqueCoulmnNames = new StringBuilder("");
		int uniqueColumnNamesAmount = 0;
		/*
		 * PRIMARY KEY constraints
		 */
		StringBuilder primaryKeyCoulmnNames = new StringBuilder("");
		int primaryKeyColumnNamesAmount = 0;

		/*
		 * Go through all table columns one by one and count amounts of UNIQUE and
		 * PRIMARY KEY constraints.
		 */
		for (QueryObjectDBTableColumn tableColumn : fTableColumns) {
			String columnConstraints = tableColumn.getColumnConstraint().getColumnConstraintsString();
			if (columnConstraints.contains(SqlStatementStrings.SQL_DATABASE_UNIQUE)) {
				uniqueColumnNamesAmount++;
				uniqueCoulmnNames.append(tableColumn.getColumnName() + ",");
			} else if (columnConstraints.contains(SqlStatementStrings.SQL_DATABASE_PRIMARY_KEY)) {
				primaryKeyColumnNamesAmount++;
				primaryKeyCoulmnNames.append(tableColumn.getColumnName() + " ");
			}
		}

		/*
		 * Post process table column setting string on UNIQUE constraint.
		 *
		 * If only one column is UNIQUE, creating clause as "UNIQUE (ID)"; Otherwise,
		 * creating clause as CONSTRAINT UC_Person UNIQUE (ID,LastName).
		 *
		 * TODO: Introducing customized multiple UNIQUE columns name.
		 */
		if (uniqueColumnNamesAmount == 1) {
			uniqueCoulmnNames.deleteCharAt(uniqueCoulmnNames.length() - 1);
			appendingClause
					.append(" " + SqlStatementStrings.SQL_DATABASE_UNIQUE + "(" + uniqueCoulmnNames.toString() + "),");
		} else if (uniqueColumnNamesAmount > 1) {
			uniqueCoulmnNames.deleteCharAt(uniqueCoulmnNames.length() - 1);
			appendingClause.append(SqlStatementStrings.SQL_DATABASE_CONSTRAINT
					+ SqlStatementStrings.SQL_DATABASE_MULTIPLE_UNIQUE_COLUMNS + tableName + " "
					+ SqlStatementStrings.SQL_DATABASE_UNIQUE + "(" + uniqueCoulmnNames.toString() + "),");
		}

		/*
		 * Post process table column setting string on PRIMARY KEY constraint.
		 *
		 * Primary keys must contain UNIQUE values, and cannot contain NULL values. A
		 * table can have only one primary key, which may consist of single or multiple
		 * fields.
		 */
		if (primaryKeyColumnNamesAmount == 1) {
			primaryKeyCoulmnNames.deleteCharAt(primaryKeyCoulmnNames.length() - 1);
			appendingClause.append(
					" " + SqlStatementStrings.SQL_DATABASE_PRIMARY_KEY + "(" + primaryKeyCoulmnNames.toString() + "),");
		} else if (primaryKeyColumnNamesAmount > 1) {
			primaryKeyCoulmnNames.deleteCharAt(primaryKeyCoulmnNames.length() - 1);
			appendingClause.append(SqlStatementStrings.SQL_DATABASE_CONSTRAINT
					+ SqlStatementStrings.SQL_DATABASE_MULTIPLE_PRIMARY_KEY_COLUMNS + tableName + " "
					+ SqlStatementStrings.SQL_DATABASE_PRIMARY_KEY + "(" + primaryKeyCoulmnNames.toString() + "),");
		}

		/*
		 * Remove the last ',' char of clause.
		 */
		appendingClause.deleteCharAt(appendingClause.length() - 1);
		return appendingClause.toString();
	}

	/**
	 * Create a string that combining all column names with split
	 * ','.
	 *
	 * <example>
	 * 	column_name0, column_name1, column_name2, ....
	 * </example>
	 *
	 * @return Combination of all column names
	 */
	protected String buildColumnWithNames() {
		StringBuilder comninationOfColumnNames = new StringBuilder("");
		for (QueryObjectDBTableColumn tableColumn : fTableColumns) {
			comninationOfColumnNames.append(tableColumn.getColumnName() + ",");
		}
		comninationOfColumnNames.deleteCharAt(comninationOfColumnNames.length() - 1);
		return comninationOfColumnNames.toString();
	}

	/**
	 * Create a string that contains all columns with columnName and
	 * data types.
	 *
	 * <example>
	 * 	column_name0 data_type0, column_name1 data_type1, ...
	 * </example>
	 *
	 * @return Combination of column definitions
	 */
	protected String buildColumnWithNameAndDataType() {
		StringBuilder columnAndDatatypeClause = new StringBuilder();
		for (QueryObjectDBTableColumn tableColumn : fTableColumns) {
			columnAndDatatypeClause.append(
					tableColumn.getColumnName() + " " + tableColumn.getColumnDataType().getSqlColumnDataType() + ",");
		}
		columnAndDatatypeClause.deleteCharAt(columnAndDatatypeClause.length() - 1);
		return columnAndDatatypeClause.toString();
	}

}
