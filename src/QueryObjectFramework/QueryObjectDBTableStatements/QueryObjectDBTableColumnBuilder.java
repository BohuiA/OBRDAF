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
	 * @param tableName
	 *            Table name that needs to create column clause.
	 * @return A full columns setting string
	 */
	protected String buildColumnWithNameAndDataTypeAndConstraints(String tableName) {
		StringBuilder tableColumnsClause = new StringBuilder("");

		/*
		 * Build normal column string without appending constraints.
		 */
		tableColumnsClause.append(buildColumnsWithNameAndDataTypeAndNormalConstraints());
		tableColumnsClause.deleteCharAt(tableColumnsClause.length() - 1);

		/*
		 * Build appending constraints
		 */
		tableColumnsClause.append(buildAppendConstraintsForColumns(tableName));

		return tableColumnsClause.toString();
	}

	/**
	 * Go through all table columns one by one to create tableColumnsClause
	 * with normal constraints.
	 *
	 * @return Columns definition string
	 */
	private String buildColumnsWithNameAndDataTypeAndNormalConstraints() {
		StringBuilder normalColumnsString = new StringBuilder("");
		for (QueryObjectDBTableColumn tableColumn : fTableColumns) {
			normalColumnsString
					.append(tableColumn.getColumnName() + " " + tableColumn.getColumnDataType().getSqlColumnDataType()
							+ " " + tableColumn.getColumnConstraint().getColumnConstraintsString() + ",");
		}
		return normalColumnsString.toString();
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
	 * @param tableName
	 *            Table name that needs to create column clause.
	 * @return Appending constraint string.
	 */
	protected String buildAppendConstraintsForColumns(String tableName) {
		StringBuilder appendingClause = new StringBuilder("");
		/*
		 * Build UNIQUE appending constraint
		 */
		appendingClause.append(buildUnqiueAppendingConstraint(tableName));
		// Insert ',' if UNIQUE appending constraint string is not null
		if (!appendingClause.toString().equals("")) {
			appendingClause.append(",");
		}
		/*
		 * Build and append PRIMARY KEY constraint
		 */
		appendingClause.append(buildPrimaryKeyAppendingConstraint(tableName));

		return appendingClause.toString();
	}

	/**
	 * Build UNIQUE constraint appending string.
	 *
	 * @param tableName
	 * 			Table name
	 * @return UNIQUE constraint appending string
	 */
	private String buildUnqiueAppendingConstraint(String tableName) {
		/*
		 * UUNIQE constraints
		 */
		StringBuilder uniqueColumnNames = new StringBuilder("");
		int uniqueColumnNamesAmount = 0;

		/*
		 * Build constraint string for UNIQUE constraint
		 */
		for (QueryObjectDBTableColumn tableColumn : fTableColumns) {
			if (tableColumn.containUniqueConstraint()) {
				uniqueColumnNamesAmount++;
				uniqueColumnNames.append(tableColumn.getColumnName() + ",");
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
			uniqueColumnNames.deleteCharAt(uniqueColumnNames.length() - 1);
			uniqueColumnNames.insert(0, " " + SqlStatementStrings.SQL_DATABASE_UNIQUE + "(");
			uniqueColumnNames.append(")");
		} else if (uniqueColumnNamesAmount > 1) {
			uniqueColumnNames.deleteCharAt(uniqueColumnNames.length() - 1);
			uniqueColumnNames.insert(0, SqlStatementStrings.SQL_DATABASE_CONSTRAINT
					+ SqlStatementStrings.SQL_DATABASE_MULTIPLE_UNIQUE_COLUMNS + tableName + " "
					+ SqlStatementStrings.SQL_DATABASE_UNIQUE + "(");
			uniqueColumnNames.append(")");
		}
		return uniqueColumnNames.toString();
	}

	/**
	 * Build PRIMARY KEY constraint appending string.
	 *
	 * @param tableName
	 * 			Table name
	 * @return PRIMARY KEY constraint appending string
	 */
	private String buildPrimaryKeyAppendingConstraint(String tableName) {
		/*
		 * PRIMARY KEY constraints
		 */
		StringBuilder primaryKeyColumnNames = new StringBuilder("");
		int primaryKeyColumnNamesAmount = 0;

		/*
		 * Go through all table columns one by one and count amounts of
		 * PRIMARY KEY constraints.
		 */
		for (QueryObjectDBTableColumn tableColumn : fTableColumns) {
			if (tableColumn.containPrimaryKeyConstraint()) {
				primaryKeyColumnNamesAmount++;
				primaryKeyColumnNames.append(tableColumn.getColumnName() + " ");
			}
		}

		/*
		 * Post process table column setting string on PRIMARY KEY constraint.
		 *
		 * Primary keys must contain UNIQUE values, and cannot contain NULL values. A
		 * table can have only one primary key, which may consist of single or multiple
		 * fields.
		 */
		if (primaryKeyColumnNamesAmount == 1) {
			primaryKeyColumnNames.deleteCharAt(primaryKeyColumnNames.length() - 1);
			primaryKeyColumnNames.insert(0, " " + SqlStatementStrings.SQL_DATABASE_PRIMARY_KEY + "(");
			primaryKeyColumnNames.append(")");
		} else if (primaryKeyColumnNamesAmount > 1) {
			primaryKeyColumnNames.deleteCharAt(primaryKeyColumnNames.length() - 1);
			primaryKeyColumnNames.insert(0, " " + SqlStatementStrings.SQL_DATABASE_CONSTRAINT
					+ SqlStatementStrings.SQL_DATABASE_MULTIPLE_PRIMARY_KEY_COLUMNS + tableName + " "
					+ SqlStatementStrings.SQL_DATABASE_PRIMARY_KEY + "(");
			primaryKeyColumnNames.append(")");
		}
		return primaryKeyColumnNames.toString();
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
