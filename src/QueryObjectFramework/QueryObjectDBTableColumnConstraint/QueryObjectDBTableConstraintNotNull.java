package QueryObjectFramework.QueryObjectDBTableColumnConstraint;

import QueryObjectFramework.CommonClasses.SqlStatementStrings;

/**
 * NOT NULL:
 * 	 The NOT NULL constraint enforces a column to NOT accept NULL values.
 *
 *   Example0:
 *   <example>
 *   	CREATE TABLE Persons (
 *   		ID int NOT NULL,
 *   		LastName varchar(255) NOT NULL,
 *   		FirstName varchar(255) NOT NULL,
 *  	 	Age int
 *   	);
 *   </example>
 *
 *   NOTE: If the table has already been created, you can add a NOT NULL
 *   constraint to a column with the ALTER TABLE statement.
 *
 * @author Bohui Axelsson
 *
 */
public class QueryObjectDBTableConstraintNotNull implements QueryObjectDBTableConstraintDecorator {
	private QueryObjectDBTableConstraintDecorator fConstraintDecorator = null;

	/**
	 * Create a default NOT NULL instances.
	 */
	public QueryObjectDBTableConstraintNotNull() {}

	/**
	 *
	 * Create a NOT NULL constraint definitions.
	 *
	 * @param constraintDecorator
	 *            The other constraint decorator
	 */
	public QueryObjectDBTableConstraintNotNull(QueryObjectDBTableConstraintDecorator constraintDecorator) {
		fConstraintDecorator = constraintDecorator;
	}

	@Override
	public String createConstraintString() {
		return fConstraintDecorator == null ? SqlStatementStrings.SQL_DATABASE_NOT_NULL
				: fConstraintDecorator.createConstraintString() + " " + SqlStatementStrings.SQL_DATABASE_NOT_NULL;
	}

	@Override
	public QueryObjectDBTableConstraintDecorator getConstraintDecorator() {
		return fConstraintDecorator;
	}
}
