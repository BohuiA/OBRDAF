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
 * - AUTO_INCREMENT:
 *   Auto-increment allows a unique number to be generated automatically when a new
 *   record is inserted into a table.
 *
 *   Example:
 *   <pre>
 *   	CREATE TABLE Persons (
 *   		ID int NOT NULL AUTO_INCREMENT,
 *   		LastName varchar(255) NOT NULL,
 *   		FirstName varchar(255),
 *  		Age int,
 *   		PRIMARY KEY (ID)
 *		);
 *   </pre>
 *
 *   NOTE: At the moment, Query Object pattern only supports auto-increment from 1.
 *
 * @author Bohui Axelsson
 */
public class SqlDBTableConstraints {
	private boolean fNotNullConstraint = false;
	private boolean fAutoIncrementConstraint = false;
	private int fAutoIncrementBeginningValue = 1;

	/**
	 * Create a default SqlDBTableConstraints instance without any
	 * constraint.
	 */
	public SqlDBTableConstraints() {}

	/**
	 * Create a SqlDBTableConstraints instance with NOT NULL and AUTO_INCREMENT
	 * constraint definitions.
	 *
	 * NOTE: create a constraint instance that AUTO_INCREMENT from default 1. If
	 * creating a constraint instance that AUTO_INCREMENT from a customized value,
	 * please using SqlDBTableConstraints(boolean, boolean, int) construct method or
	 * to set AUTO_INCREMENT beginning vaule by method
	 * setAutoIncrementBeginningValue().
	 *
	 * @param notNullConstraint
	 *            True if creating NOT NULL constraint
	 * @param autoIncrementConstraint
	 *            True if creating AUTO_INCREMENT constraint
	 */
	public SqlDBTableConstraints(boolean notNullConstraint, boolean autoIncrementConstraint) {
		fNotNullConstraint = notNullConstraint;
		fAutoIncrementConstraint = autoIncrementConstraint;
	}

	/**
	 * Create a SqlDBTableConstraints instance with NOT NULL and AUTO_INCREMENT
	 * constraint definitions.
	 *
	 * Create a constraint instance with a customized AUTO_INCREMENT beginning value.
	 *
	 * @param notNullConstraint
	 *            True if creating NOT NULL constraint
	 * @param autoIncrementConstraint
	 *            True if creating AUTO_INCREMENT constraint
	 * @param autoIncrementBeginningValue
	 * 			  AUTO_INCREMENT beginning value
	 */
	public SqlDBTableConstraints(boolean notNullConstraint, boolean autoIncrementConstraint,
			int autoIncrementBeginningValue) {
		fNotNullConstraint = notNullConstraint;
		fAutoIncrementConstraint = autoIncrementConstraint;
		fAutoIncrementBeginningValue = autoIncrementBeginningValue;
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
			columnConstraintStr.append(SqlStatementStrings.SQL_DATABASE_NOT_NULL + " ");
		}
		if (fAutoIncrementConstraint) {
			columnConstraintStr.append(SqlStatementStrings.SQL_DATABASE_AUTO_INCREMENT + "=" + fAutoIncrementBeginningValue + " ");
		}
		return columnConstraintStr.toString();
	}
}
