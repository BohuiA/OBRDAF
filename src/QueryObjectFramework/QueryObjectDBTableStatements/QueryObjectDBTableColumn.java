package QueryObjectFramework.QueryObjectDBTableStatements;

import org.eclipse.jdt.annotation.NonNull;

import QueryObjectFramework.QueryObjectDBTableColumnConstraint.QueryObjectDBTableColumnConstraints;

/**
 * Table column class contains column relative data for
 * database table operations.
 *
 * Column relative data:
 * - Column Name
 * - Data Type
 * - Column Constraint
 *
 * @author Bohui Axelsson
 */
public class QueryObjectDBTableColumn {
	private @NonNull String fColumnName = "";
	private @NonNull QueryObjectDBTableColumnDataType fColumnDataType = new QueryObjectDBTableColumnDataType();
	private @NonNull QueryObjectDBTableColumnConstraints fColumnConstraint = new QueryObjectDBTableColumnConstraints();

	/**
	 * Create a default table column instance.
	 */
	public QueryObjectDBTableColumn() {}

	/**
	 * Create a table column instance with column name, column data type, and column
	 * constraint.
	 *
	 * @param columnName
	 *            Column name
	 * @param columnDataType
	 *            Column data type
	 * @param columnConstraint
	 *            Column constraints
	 */
	public QueryObjectDBTableColumn(@NonNull String columnName, @NonNull QueryObjectDBTableColumnDataType columnDataType,
			@NonNull QueryObjectDBTableColumnConstraints columnConstraint) {
		fColumnName = columnName;
		fColumnDataType = columnDataType;
		fColumnConstraint = columnConstraint;
	}

	/**
	 * Create a table column instance with column name and column data type.
	 *
	 * @param columnName
	 *            Column name
	 * @param columnDataType
	 *            Column data type
	 */
	public QueryObjectDBTableColumn(@NonNull String columnName, @NonNull QueryObjectDBTableColumnDataType columnDataType) {
		fColumnName = columnName;
		fColumnDataType = columnDataType;
	}

	/**
	 * Create a table column instance with column name and column constraint.
	 *
	 * @param columnName
	 *            Column name
	 * @param columnConstraint
	 *            Column constraints
	 */
	public QueryObjectDBTableColumn(@NonNull String columnName, @NonNull QueryObjectDBTableColumnConstraints columnConstraint) {
		fColumnName = columnName;
		fColumnConstraint = columnConstraint;
	}

	protected boolean containUniqueConstraint() {
		return fColumnConstraint.containUnqueConstraint();
	}

	protected boolean containPrimaryKeyConstraint() {
		return fColumnConstraint.containPrimaryKeyConstraint();
	}

	protected String getColumnName() {
		return fColumnName;
	}

	protected QueryObjectDBTableColumnDataType getColumnDataType() {
		return fColumnDataType;
	}

	protected QueryObjectDBTableColumnConstraints getColumnConstraint() {
		return fColumnConstraint;
	}
}
