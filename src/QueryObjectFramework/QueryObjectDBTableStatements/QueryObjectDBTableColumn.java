package QueryObjectFramework.QueryObjectDBTableStatements;

import org.eclipse.jdt.annotation.NonNull;

import QueryObjectFramework.CommonClasses.SqlColumnDataType;
import QueryObjectFramework.CommonClasses.SqlDBTableConstraints;

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
	private @NonNull SqlColumnDataType fColumnDataType = new SqlColumnDataType();
	private @NonNull SqlDBTableConstraints fColumnConstraint = new SqlDBTableConstraints();

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
	public QueryObjectDBTableColumn(@NonNull String columnName, @NonNull SqlColumnDataType columnDataType,
			@NonNull SqlDBTableConstraints columnConstraint) {
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
	public QueryObjectDBTableColumn(@NonNull String columnName, @NonNull SqlColumnDataType columnDataType) {
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
	public QueryObjectDBTableColumn(@NonNull String columnName, @NonNull SqlDBTableConstraints columnConstraint) {
		fColumnName = columnName;
		fColumnConstraint = columnConstraint;
	}

	public String getColumnName() {
		return fColumnName;
	}

	public SqlColumnDataType getColumnDataType() {
		return fColumnDataType;
	}

	public SqlDBTableConstraints getColumnConstraint() {
		return fColumnConstraint;
	}
}
