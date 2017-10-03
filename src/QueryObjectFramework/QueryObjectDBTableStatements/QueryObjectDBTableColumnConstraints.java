package QueryObjectFramework.QueryObjectDBTableStatements;

import java.util.logging.Logger;

import QueryObjectFramework.CommonClasses.SqlStatementStrings;

/**
 * Class contains all constraints for one particular
 * column in one table.
 *
 * Valid Constraint Settings:
 *
 * - NOT NULL:
 * 	 The NOT NULL constraint enforces a column to NOT accept NULL values.
 *
 *   Example0:
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
 *   Example1:
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
 *   NOTE: As default, Query Object pattern begins auto_increment from 1.
 *
 * - UNIQUE:
 * 	 The UNIQUE constraint ensures that all values in a column are different.
 *   Both the UNIQUE and PRIMARY KEY constraints provide a guarantee for uniqueness for
 *   a column or set of columns.
 *   A PRIMARY KEY constraint automatically has a UNIQUE constraint.
 *   However, you can have many UNIQUE constraints per table, but only one PRIMARY KEY constraint
 *   per table.
 *
 *   Example2:
 *   <pre>
 *   	CREATE TABLE Persons (
 *   		ID int NOT NULL,
 *   		LastName varchar(255) NOT NULL,
 *   		FirstName varchar(255),
 *   		Age int,
 *   		UNIQUE (ID)
 *		);
 *   </pre>
 *   <pre>
 *   	CREATE TABLE Persons (
 *   		ID int NOT NULL,
 *   		LastName varchar(255) NOT NULL,
 *   		FirstName varchar(255),
 *   		Age int,
 *   		CONSTRAINT UC_Person UNIQUE (ID,LastName)
 *		);
 *   </pre>
 *
 *   NOTE: To set multiple columns as UNIQUE, just setting the UNIQUE vaule of each constraint instance of
 *   each column to TRUE. Query Object Pattern will do the rest work.
 *
 *   Example of Example2:
 *   <pre>
 *   	CREATE TABLE Persons (
 *   		ID int NOT NULL UNIQUE,
 *   		LastName varchar(255) NOT NULL UNIQUE,
 *   		FirstName varchar(255),
 *   		Age int
 *		);
 *   </pre>
 *
 * @author Bohui Axelsson
 */
public class QueryObjectDBTableColumnConstraints {
	private static final Logger LOGGER = Logger.getLogger(QueryObjectDBTableColumnConstraints.class.getName());

	/*
	 * NOT NULL constraint
	 */
	private boolean fNotNullConstraint = false;
	/*
	 * AUTO_INCREMENT constraint
	 */
	private boolean fAutoIncrementConstraint = false;
	private int fAutoIncrementBeginningValue = 1;
	/*
	 * UNIQUE constraint
	 */
	private boolean fUniqueConstraint = false;

	/**
	 * Create a default SqlDBTableConstraints instance without any
	 * constraint.
	 */
	public QueryObjectDBTableColumnConstraints() {}

	/**
	 * Create a SqlDBTableConstraints instance with NOT NULL and AUTO_INCREMENT
	 * constraint definitions.
	 *
	 * NOTE: create a constraint instance that AUTO_INCREMENT from default 1. If
	 * creating a constraint instance that AUTO_INCREMENT from a customized value,
	 * please using SqlDBTableConstraints(boolean, boolean, int) construct method or
	 * to set AUTO_INCREMENT beginning value by method
	 * setAutoIncrementBeginningValue().
	 *
	 * @param notNullConstraint
	 *            True if creating NOT NULL constraint
	 * @param autoIncrementConstraint
	 *            True if creating AUTO_INCREMENT constraint
	 */
	public QueryObjectDBTableColumnConstraints(boolean notNullConstraint, boolean autoIncrementConstraint) {
		fNotNullConstraint = notNullConstraint;
		fAutoIncrementConstraint = autoIncrementConstraint;
	}

	/**
	 * Create a SqlDBTableConstraints instance with NOT NULL, AUTO_INCREMENT and UNIQUE
	 * constraint definitions.
	 *
	 * NOTE: create a constraint instance that AUTO_INCREMENT from default 1. If
	 * creating a constraint instance that AUTO_INCREMENT from a customized value,
	 * please using SqlDBTableConstraints(boolean, boolean, int) construct method or
	 * to set AUTO_INCREMENT beginning value by method
	 * setAutoIncrementBeginningValue().
	 *
	 * @param notNullConstraint
	 *            True if creating NOT NULL constraint
	 * @param autoIncrementConstraint
	 *            True if creating AUTO_INCREMENT constraint
	 * @param uniqueConstraint
	 * 			  True if creating UNIQUE constraint
	 */
	public QueryObjectDBTableColumnConstraints(boolean notNullConstraint, boolean autoIncrementConstraint,
			boolean uniqueConstraint) {
		fNotNullConstraint = notNullConstraint;
		fAutoIncrementConstraint = autoIncrementConstraint;
		fUniqueConstraint = uniqueConstraint;
	}

	/**
	 * Create a SqlDBTableConstraints instance with NOT NULL, AUTO_INCREMENT and UNIQUE
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
	 * @param uniqueConstraint
	 * 			  True if creating UNIQUE constraint
	 */
	public QueryObjectDBTableColumnConstraints(boolean notNullConstraint, boolean autoIncrementConstraint,
			int autoIncrementBeginningValue, boolean uniqueConstraint) {
		fNotNullConstraint = notNullConstraint;
		fAutoIncrementConstraint = autoIncrementConstraint;
		fAutoIncrementBeginningValue = autoIncrementBeginningValue;
		fUniqueConstraint = uniqueConstraint;
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
	public QueryObjectDBTableColumnConstraints(boolean notNullConstraint, boolean autoIncrementConstraint,
			int autoIncrementBeginningValue) {
		fNotNullConstraint = notNullConstraint;
		fAutoIncrementConstraint = autoIncrementConstraint;
		fAutoIncrementBeginningValue = autoIncrementBeginningValue;
	}

	/**
	 * Get UNIQUE constraint of the column.
	 *
	 * @return True if UNIQUE constraint has been settled.
	 */
	protected boolean getUniqueState() {
		return fUniqueConstraint;
	}

	/**
	 * Update AUTO_INCREMENT beginning value. AUTO_INCREMENT Beginning Value should
	 * be bigger than 1.
	 *
	 * @param autoIncrementBeginningValue
	 *            Customized AUTO_INCREMENT beginning value.
	 */
	public void setAutoIncrementBeginningValue(int autoIncrementBeginningValue) {
		if (autoIncrementBeginningValue < 1) {
			LOGGER.warning("AUTO_INCREMENT Beginning Value should be bigger than 1.");
			return;
		}
		fAutoIncrementBeginningValue = autoIncrementBeginningValue;
	}

	/**
	 * Get full string of column constraints defined for this
	 * column.
	 *
	 * @return Full String of column constraints.
	 */
	protected String getColumnConstraintsString() {
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
