package QueryObjectFramework.QueryObjectDBTableColumnConstraint;

import org.eclipse.jdt.annotation.NonNull;

/**
 * CHECK:
 * The CHECK constraint is used to limit the value range that can be placed in a column.
 * If you define a CHECK constraint on a single column it allows only certain values
 * for this column.
 * If you define a CHECK constraint on a table it can limit the values in certain columns
 * based on values in other columns in the row.
 *
 * <example>
 * 	CREATE TABLE Persons (
 * 		ID int NOT NULL,
 * 		LastName varchar(255) NOT NULL,
 * 		FirstName varchar(255),
 * 		Age int,
 * 		CHECK (Age>=18)
 * 	);
 * </example>
 *
 * <example>
 * 	CREATE TABLE Persons (
 * 		ID int NOT NULL,
 * 		LastName varchar(255) NOT NULL,
 * 		FirstName varchar(255),
 * 		Age int,
 * 		CONSTRAINT CHK_Person CHECK (Age>=18 AND City='Sandnes')
 * 	);
 * </example>
 *
 * <example>
 * 	ALTER TABLE Persons
 * 	ADD CHECK (Age>=18);
 * </example>
 *
 * <example>
 * 	ALTER TABLE Persons
 * 	DROP CONSTRAINT CHK_PersonAge;
 * </example>
 *
 * @author Bohui Axelsson
 */
public class QueryObjectDBTableConstraintCheck implements QueryObjectDBTableConstraintDecorator {
	private QueryObjectDBTableConstraintDecorator fConstraintDecorator = null;
	private @NonNull QueryObjectDBTableConstraintCriteriaCondition fCriteriaCondition = null;

	public QueryObjectDBTableConstraintCheck(QueryObjectDBTableConstraintDecorator constraintDecorator,
			@NonNull QueryObjectDBTableConstraintCriteriaCondition criteriaConditione) {
		fConstraintDecorator = constraintDecorator;
		fCriteriaCondition = criteriaConditione;
	}

	/**
	 * Create and return CHECK column string.
	 *
	 * <example>
	 *  Age>=18 ... City='Sandnes'
	 * </example>
	 *
	 * @return CHECK string or empty string.
	 */
	public String getCriteriaConditionClause() {
		if (fCriteriaCondition == null || !fCriteriaCondition.validateCriteriaCondition()) {
			return "";
		}
		return buildColumnCheckCriteriaConditionClause();
	}

	/**
	 * Build SQL CHECK clause string from fCriteriaCondition.
	 *
	 * @return SQL CHECK string
	 */
	private String buildColumnCheckCriteriaConditionClause() {
		StringBuilder checkCriteriaClause = new StringBuilder("");
		if ((fCriteriaCondition.isStringCriteriaValue())
				&& !fCriteriaCondition.getValue().equals("")) {
			checkCriteriaClause.append(fCriteriaCondition.getFiled()
					+ fCriteriaCondition.getOperator() + "'" + fCriteriaCondition.getValue() + "' ");
		} else {
			checkCriteriaClause.append(fCriteriaCondition.getFiled()
					+ fCriteriaCondition.getOperator() + fCriteriaCondition.getValue() + " ");
		}
		return checkCriteriaClause.toString();
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
