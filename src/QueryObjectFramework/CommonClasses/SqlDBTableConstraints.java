package QueryObjectFramework.CommonClasses;

/**
 * Class contains all constraints for one particular
 * column in one table.
 *
 * Valid Constraint Settings:
 *
 * - NOT NULL:
 * 	 The NOT NULL constraint enforces a column to NOT accept NULL values.
 *
 *   Example:
 *   <pre>
 *   	CREATE TABLE Persons (
 *   		ID int NOT NULL,
 *   		LastName varchar(255) NOT NULL,
 *   		FirstName varchar(255) NOT NULL,
 *  	 	Age int
 *   	);
 *   </pre>
 *
 *   NOTE: If the table has already been created, you can add a NOT NULL
 *   constraint to a column with the ALTER TABLE statement.
 *
 * @author Bohui Axelsson
 */
public class SqlDBTableConstraints {
	private boolean fNotNullConstraint = false;

	/**
	 * Create a default SqlDBTableConstraints instance without any
	 * constraint.
	 */
	public SqlDBTableConstraints() {}

	/**
	 * Create a SqlDBTableConstraints instance with NOT NULL
	 * constraint definition.
	 *
	 * @param notNullConstraint
	 * 			True if creating NOT NULL constraint.
	 */
	public SqlDBTableConstraints(boolean notNullConstraint) {
		fNotNullConstraint = notNullConstraint;
	}

	/**
	 * Get full string of column constraints defined for this
	 * column.
	 *
	 * @return Full String of column constraints.
	 */
	public String getColumnConstraintsString() {
		StringBuilder columnConstraintStr = new StringBuilder("");
		if (fNotNullConstraint) {
			columnConstraintStr.append(SqlStatementStrings.SQL_TABLE_IS_NOT_NULL + " ");
		}
		return columnConstraintStr.toString();
	}
}
