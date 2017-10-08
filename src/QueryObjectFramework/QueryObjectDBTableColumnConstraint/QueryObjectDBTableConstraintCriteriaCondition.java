package QueryObjectFramework.QueryObjectDBTableColumnConstraint;

import java.util.logging.Logger;

/**
 * CHECK Criteria condition class includes pattern fields for each condition
 * definition.
 *
 * Scenario:
 *
 * field1 operator1 value1 ..
 *
 * <example>
 * country='USA', name='Bohui Axelsson'
 * </example>
 *
 * @author Bohui Axelsson
 */
public class QueryObjectDBTableConstraintCriteriaCondition {
	private static final Logger LOGGER = Logger.getLogger(QueryObjectDBTableConstraintCriteriaCondition.class.getName());

	/*
	 * Scenario:
	 *
	 * field operator value
	 *
	 * Example, country = 'USA' or CustomerName LIKE '%or%', LIKE keyword could be
	 * an operator.
	 */
	private String fField = null;
	private Object fValue = null;
	private String fOperator = null;

	/**
	 * Create SQL CHECK criteria condition class.
	 *
	 * <example>
	 * filed0 -> SalesDate
	 * operator0 -> "" or null
	 * value0 -> "" or null
	 *
	 * filed1 -> "" or null
	 * operator1 -> "" or null
	 * value1 -> '11/11/2010'

	 * filed2 -> "" or null
	 * operator2 -> "" or null
	 * value2 -> '12/11/2010'
	 * </example>
	 *
	 * @param field
	 *            Criteria condition field name, empty string is no field name
	 *            needed.
	 * @param value
	 *            Criteria condition value, empty string is no value needed.
	 * @param operator
	 *            Criteria condition operator name, empty string is no operator name
	 *            needed.
	 */
	public QueryObjectDBTableConstraintCriteriaCondition(String field, Object value, String operator) {
		fField = field;
		fValue = value;
		fOperator = operator;
	}

	protected String getFiled() {
		return fField;
	}

	protected Object getValue() {
		return fValue;
	}

	protected String getOperator() {
		return fOperator;
	}

	/**
	 * Validate SQL CHECK condition setups.
	 *
	 * @return True if all lists are matching valid rules.
	 */
	protected boolean validateCriteriaCondition() {
		if (fField == null) {
			LOGGER.config("filed name is missing, changing filed name to empty string.");
			fField = "";
		}
		if (fValue == null) {
			LOGGER.config("value is missing, changing value to empty string.");
			fValue = "";
		}
		if (fOperator == null) {
			LOGGER.config("Operator is missing, changing operator to empty string.");
			fOperator = "";
		}
		return true;
	}

	/**
	 * Check criteria value is String type.
	 *
	 * @return True if criteria value is String type.
	 */
	protected boolean isStringCriteriaValue() {
		return fValue instanceof String;
	}

}
