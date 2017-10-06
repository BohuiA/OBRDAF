package QueryObjectFramework.QueryObjectDBTableColumnConstraint;

import QueryObjectFramework.CommonClasses.SqlStatementStrings;

/**
 * PRIMARY KEY:
 * The PRIMARY KEY constraint uniquely identifies each record in a
 * database table. Primary keys must contain UNIQUE values, and cannot contain
 * NULL values. A table can have only one primary key, which may consist of
 * single or multiple fields.
 *
 * <example>
 * 	CREATE TABLE Persons (
 * 		ID int NOT NULL,
 * 		LastName varchar(255) NOT NULL, F
 *  		FirstName varchar(255),
 *  		Age int,
 *  		PRIMARY KEY (ID) );
 *  </example>
 *
 * <example>
 * 	CREATE TABLE Persons (
 * 		ID int NOT NULL,
 * 		LastName varchar(255) NOT NULL,
 * 		FirstName varchar(255),
 * 		Age int,
 * 		CONSTRAINT PK_Person PRIMARY KEY (ID,LastName) );
 *
 * Note: In the example above there is only ONE PRIMARY KEY (PK_Person).
 * However, the VALUE of the primary key is made up of TWO COLUMNS (ID +
 * LastName).
 * </example>
 *
 * To create a PRIMARY KEY constraint on the "ID" column when the table is
 * already created, use the following SQL
 * <example>
 * 	ALTER TABLE Persons
 * 	ADD PRIMARY KEY (ID);
 * </example>
 *
 * <example>
 * 	ALTER TABLE Persons
 * 	ADD CONSTRAINT PK_Person PRIMARY KEY (ID,LastName);
 *
 * Note: If you use the ALTER TABLE statement to add a primary key, the primary
 * key column(s) must already have been declared to not contain NULL values
 * (when the table was first created).
 * </example>
 *
 * To drop a PRIMARY KEY constraint, use the following SQL:
 *
 * <example> ALTER TABLE Persons DROP PRIMARY KEY; </example>
 *
 * @author Bohui Axelsson
 */
public class QueryObjectDBTableConstraintPrimaryKey implements QueryObjectDBTableConstraintDecorator {
	private QueryObjectDBTableConstraintDecorator fConstraintDecorator = null;

	/**
	 * Create a default PRIMARY KEY instances.
	 */
	public QueryObjectDBTableConstraintPrimaryKey() {}

	/**
	 * Create a PRIMARY KEY constraint definitions.
	 *
	 * @param constraintDecorator
	 *            The other constraint decorator
	 */
	public QueryObjectDBTableConstraintPrimaryKey(QueryObjectDBTableConstraintDecorator constraintDecorator) {
		fConstraintDecorator = constraintDecorator;
	}

	@Override
	public String createConstraintString() {
		return fConstraintDecorator == null ? SqlStatementStrings.SQL_DATABASE_PRIMARY_KEY
				: fConstraintDecorator.createConstraintString() + " " + SqlStatementStrings.SQL_DATABASE_PRIMARY_KEY;
	}

}
