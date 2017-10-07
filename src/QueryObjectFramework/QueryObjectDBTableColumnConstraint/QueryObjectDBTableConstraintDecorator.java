package QueryObjectFramework.QueryObjectDBTableColumnConstraint;

/**
 * Decorator class for add constraints to columns dynamically.
 *
 * All SQL constraint classes must implement from this interface.
 *
 * @author Bohui Axelsson
 */
public interface QueryObjectDBTableConstraintDecorator {
	public String createConstraintString();
	public QueryObjectDBTableConstraintDecorator getConstraintDecorator();
}
