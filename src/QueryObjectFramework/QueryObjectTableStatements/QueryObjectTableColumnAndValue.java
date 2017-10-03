package QueryObjectFramework.QueryObjectTableStatements;

import org.eclipse.jdt.annotation.NonNull;

/**
 * Data class contains UPDATE relative information.
 *
 * UPDATE relative information:
 * - Update column name.
 * - Update column values
 *
 * @author Bohui Axelsson
 */
public class QueryObjectTableColumnAndValue {
	private @NonNull String fUpdateColumnName = "";
	private @NonNull Object fUpdateValue = new Object();

	/**
	 * Create a UPDATE column instance with target updating value. The
	 * QueryObjectTableColumnAndValue item is for only contains updateValue for
	 * inserting/updating column.
	 *
	 * Example:
	 *
	 * <pre>
	 * 	INSERT INTO table_name
	 *  VALUES (value1, value2, value3, ...);
	 * </pre>
	 *
	 * @param updateValue
	 *            Updating column value
	 */
	public QueryObjectTableColumnAndValue(@NonNull Object updateValue) {
		fUpdateColumnName = "";
		fUpdateValue = updateValue;
	}

	/**
	 * Create a UPDATE column instance with target column name and updating value.
	 *
	 * @param updateColumnName
	 *            Updating column name
	 * @param updateValue
	 *            Updating column value
	 */
	public QueryObjectTableColumnAndValue(@NonNull String updateColumnName, @NonNull Object updateValue) {
		fUpdateColumnName = updateColumnName;
		fUpdateValue = updateValue;
	}

	protected String getUpdateColumnName() {
		return fUpdateColumnName;
	}

	protected Object getUpdateValue() {
		return fUpdateValue;
	}

	/**
	 * Check update value is String type.
	 *
	 * @return True if update value is String type.
	 */
	protected boolean isStringUpdateValue() {
		return fUpdateValue instanceof String;
	}
}
