package QueryObjectFramework.QueryObjectDBTableColumnConstraint;

/**
 * UNIQUE:
 * 	 The UNIQUE constraint ensures that all values in a column are different.
 *   Both the UNIQUE and PRIMARY KEY constraints provide a guarantee for uniqueness for
 *   a column or set of columns.
 *   A PRIMARY KEY constraint automatically has a UNIQUE constraint.
 *   However, you can have many UNIQUE constraints per table, but only one PRIMARY KEY constraint
 *   per table.
 *
 *   Example2:
 *   <example>
 *   	CREATE TABLE Persons (
 *   		ID int NOT NULL,
 *   		LastName varchar(255) NOT NULL,
 *   		FirstName varchar(255),
 *   		Age int,
 *   		UNIQUE (ID)
 *		);
 *   </example>
 *   <example>
 *   	CREATE TABLE Persons (
 *   		ID int NOT NULL,
 *   		LastName varchar(255) NOT NULL,
 *   		FirstName varchar(255),
 *   		Age int,
 *   		CONSTRAINT UC_Person UNIQUE (ID,LastName)
 *		);
 *   </example>
 *
 *   NOTE: To set multiple columns as UNIQUE, just setting the UNIQUE vaule of each constraint instance of
 *   each column to TRUE. Query Object Pattern will do the rest work.
 *
 *   Example of Example2:
 *   <example>
 *   	CREATE TABLE Persons (
 *   		ID int NOT NULL UNIQUE,
 *   		LastName varchar(255) NOT NULL UNIQUE,
 *   		FirstName varchar(255),
 *   		Age int
 *		);
 *   </example>

 * @author Bohui Axelsson
 *
 */
public class QueryObjectDBTableConstraintUnique implements QueryObjectDBTableConstraintDecorator {
	private QueryObjectDBTableConstraintDecorator fConstraintDecorator = null;

	/**
	 * Create a default UNIQUE instances.
	 */
	public QueryObjectDBTableConstraintUnique() {}

	/**
	 * Create a UNIQUE constraint definitions.
	 *
	 * @param constraintDecorator
	 *            The other constraint decorator
	 */
	public QueryObjectDBTableConstraintUnique(QueryObjectDBTableConstraintDecorator constraintDecorator) {
		fConstraintDecorator = constraintDecorator;
	}

	@Override
	public String createConstraintString() {
		return fConstraintDecorator == null ? ""
				: fConstraintDecorator.createConstraintString() + " ";
	}

	@Override
	public QueryObjectDBTableConstraintDecorator getConstraintDecorator() {
		return fConstraintDecorator;
	}
}
