package QueryObjectFramework.QueryObjectDBTableColumnConstraint;

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
 * - PRIMARY KEY
 * 		<class> QueryObjectDBTableConstraintPrimaryKey </class>
 * - FOREIGN KEY
 * 		<class> QueryObjectDBTableConstraintForeignKey </class>
 *
 * @author Bohui Axelsson
 */
public class QueryObjectDBTableColumnConstraints {
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

	/**
	 * Check whether the column has an UNIQUE constraint.
	 *
	 * @return True if the column has an UNIQUE constraint.
	 */
	public boolean containUnqueConstraint() {
		QueryObjectDBTableConstraintDecorator columnConstraint = fColumnConstraint;
		while (columnConstraint != null) {
			if (columnConstraint instanceof QueryObjectDBTableConstraintUnique) {
				return true;
			}
			columnConstraint = columnConstraint.getConstraintDecorator();
		}
		return false;
	}

	/**
	 * Check whether the column has an PRIMARY KEY constraint.
	 *
	 * @return True if the column has an PRIMARY KEY constraint.
	 */
	public boolean containPrimaryKeyConstraint() {
		QueryObjectDBTableConstraintDecorator columnConstraint = fColumnConstraint;
		while (columnConstraint != null) {
			if (columnConstraint instanceof QueryObjectDBTableConstraintPrimaryKey) {
				return true;
			}
			columnConstraint = columnConstraint.getConstraintDecorator();
		}
		return false;
	}

	/**
	 * Check and return whether the column has a FOREIGN KEY constraint.
	 *
	 * @return FOREIGN KEY decorator instance.
	 */
	public QueryObjectDBTableConstraintForeignKey containAndGetForeignKeyConstraint() {
		QueryObjectDBTableConstraintDecorator columnConstraint = fColumnConstraint;
		while (columnConstraint != null) {
			if (columnConstraint instanceof QueryObjectDBTableConstraintForeignKey) {
				return (QueryObjectDBTableConstraintForeignKey) columnConstraint;
			}
			columnConstraint = columnConstraint.getConstraintDecorator();
		}
		return null;
	}
}
