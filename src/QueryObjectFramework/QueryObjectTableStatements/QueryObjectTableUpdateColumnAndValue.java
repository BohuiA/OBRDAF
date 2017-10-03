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
public class QueryObjectTableUpdateColumnAndValue {
	private @NonNull String fUpdateColumnName = "";
	private @NonNull Object fUpdateValue = new Object();

	/**
	 * Create a UPDATE column instance with target column name and updating value.
	 *
	 * @param updateColumnName
	 *            Updating column name
	 * @param updateValue
	 *            Updating column value
	 */
	public QueryObjectTableUpdateColumnAndValue(@NonNull String updateColumnName,@NonNull Object updateValue) {
		fUpdateColumnName = updateColumnName;
		fUpdateValue = updateValue;
	}

	public String getUpdateColumnName() {
		return fUpdateColumnName;
	}

	public Object getUpdateValue() {
		return fUpdateValue;
	}
}
