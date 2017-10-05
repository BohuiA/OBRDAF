package QueryObjectFramework.QueryObjectDBTableColumnConstraint;

import java.util.logging.Logger;

/**
 * Class contains all constraints for one particular
 * column in one table.
 *
 * Valid Constraint Settings:
 * - NOT NULL
 * 		<class> QueryObjectDBTableConstraintNotNull </class>
 * - AUTO_INCREMENT
 * 		<class> QueryObjectDBTableConstraintAutoIncrement </class>
 * - UNIQUE
 * 		<class> QueryObjectDBTableConstraintUnique </class>
 *
 * TODO: Add customized BUILDER methods for users to create columns with
 * constraints easily.
 *
 * @author Bohui Axelsson
 */
public class QueryObjectDBTableColumnConstraints {
	private static final Logger LOGGER = Logger.getLogger(QueryObjectDBTableColumnConstraints.class.getName());

	private QueryObjectDBTableConstraintDecorator fColumnConstraint = null;

	/**
	 * Create a default SqlDBTableConstraints instance without any
	 * constraint.
	 */
	public QueryObjectDBTableColumnConstraints() {}

	/**
	 * Create a SqlDBTableConstraints instance with customized constraint decorator.
	 *
	 * <example>
	 * QueryObjectDBTableColumnConstraints(new
	 * 	QueryObjectDBTableConstraintNotNull(new
	 * 		QueryObjectDBTableConstraintAutoIncrement()));
	 * </example>
	 *
	 * @param constraintDecorator
	 */
	public QueryObjectDBTableColumnConstraints(QueryObjectDBTableConstraintDecorator constraintDecorator) {
		fColumnConstraint = constraintDecorator;
	}

	/**
	 * Get full string of column constraints defined for this column.
	 *
	 * @return Full String of column constraints or empty string if no constraints
	 *         have been settled.
	 */
	public String getColumnConstraintsString() {
		return fColumnConstraint == null ? "" : fColumnConstraint.createConstraintString();
	}
}
