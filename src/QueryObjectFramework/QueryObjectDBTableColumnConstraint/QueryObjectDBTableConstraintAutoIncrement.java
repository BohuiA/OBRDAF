package QueryObjectFramework.QueryObjectDBTableColumnConstraint;

import java.util.logging.Logger;

import QueryObjectFramework.CommonClasses.SqlStatementStrings;

/**
 * AUTO_INCREMENT:
 *   Auto-increment allows a unique number to be generated automatically when a new
 *   record is inserted into a table.
 *
 *   Example1:
 *   <example>
 *   	CREATE TABLE Persons (
 *   		ID int NOT NULL AUTO_INCREMENT,
 *   		LastName varchar(255) NOT NULL,
 *   		FirstName varchar(255),
 *  		Age int,
 *   		PRIMARY KEY (ID)
 *		);
 *   </example>
 *
 *   NOTE: As default, Query Object pattern begins auto_increment from 1.
 *
 * @author Bohui Axelsson
 */
public class QueryObjectDBTableConstraintAutoIncrement implements QueryObjectDBTableConstraintDecorator {
	private static final Logger LOGGER = Logger.getLogger(QueryObjectDBTableConstraintAutoIncrement.class.getName());

	private QueryObjectDBTableConstraintDecorator fConstraintDecorator = null;
	private int fAutoIncrementBeginningValue = 1;

	/**
	 * Create a default AUTO INCREMENT instances.
	 */
	public QueryObjectDBTableConstraintAutoIncrement() {}

	/**
	 * Create a AUTO_INCREMENT constraint definitions.
	 *
	 * @param constraintDecorator
	 *            The other constraint decorator
	 * @param autoIncrementBeginningValue
	 *            AUTO INCREMENT beginning value
	 */
	public QueryObjectDBTableConstraintAutoIncrement(QueryObjectDBTableConstraintDecorator constraintDecorator,
			int autoIncrementBeginningValue) {
		fConstraintDecorator = constraintDecorator;
		if (autoIncrementBeginningValue < 1) {
			LOGGER.warning("AUTO_INCREMENT Beginning Value should be bigger than 1.");
			return;
		}
		fAutoIncrementBeginningValue = autoIncrementBeginningValue;
	}

	@Override
	public String createConstraintString() {
		String autoIncrementString = SqlStatementStrings.SQL_DATABASE_AUTO_INCREMENT + "="
				+ fAutoIncrementBeginningValue;
		return fConstraintDecorator == null ? autoIncrementString
				: fConstraintDecorator.createConstraintString() + " " + autoIncrementString;
	}
}
