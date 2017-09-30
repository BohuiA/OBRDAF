package QueryObjectFramework.CommonClasses;

import java.util.logging.Logger;

/**
 * SQL Criteria condition class includes pattern fields for each condition
 * definition.
 *
 * Scenario:
 *
 * conditionOperator1 field1 operator1 value1 conditionOperator2 field2
 * operator2 value2 ..
 *
 * Example, NOT country='USA' AND name='Bohui Axelsson'
 *
 * @author Bohui Axelsson
 */
public class SqlCriteriaCondition {
	private static final Logger LOGGER = Logger.getLogger(SqlCriteriaCondition.class.getName());
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
	/*
	 * Scenario:
	 *
	 * conditionOperator1 field1 operator1 value1 conditionOperator2 field2 operator2
	 * value2 ..
	 *
	 * Example, NOT country='USA' AND name='Bohui Axelsson'
	 *
	 * Example, "" SalesDate BETWEEN '11/11/2010' AND '12/11/2010'
	 *
	 * conditonOperation0 -> ""
	 * filed0 -> SalesDate
	 * operator0 -> ""
	 * value0 -> ""
	 *
	 * conditonOperation1 -> BETWEEN
	 * filed1 -> ""
	 * operator1 -> ""
	 * value1 -> '11/11/2010'
	 *
	 * conditonOperation2 -> AND
	 * filed2 -> ""
	 * operator2 -> ""
	 * value2 -> '12/11/2010'
	 */
	private String fConditionOperator = null;

	/**
	 * Create SQL WHERE criteria condition class.
	 *
	 * <pre>
	 * Example:
	 * "" SalesDate BETWEEN '11/11/2010' AND '12/11/2010'
	 *
	 * conditonOperation0 -> ""
	 * filed0 -> SalesDate
	 * operator0 -> "" or null
	 * value0 -> "" or null
	 *
	 * conditonOperation1 -> BETWEEN
	 * filed1 -> "" or null
	 * operator1 -> "" or null
	 * value1 -> '11/11/2010'
	 *
	 * conditonOperation2 -> AND
	 * filed2 -> "" or null
	 * operator2 -> "" or null
	 * value2 -> '12/11/2010'
	 * </pre>
	 *
	 * @param field
	 *            Criteria condition field name, empty string is no field name
	 *            needed.
	 * @param value
	 *            Criteria condition value, empty string is no value needed.
	 * @param operator
	 *            Criteria condition operator name, empty string is no operator name
	 *            needed.
	 * @param conditionOperator
	 *            Criteria condition conditionOperator name, empty string is no
	 *            conditionOperator name needed.
	 */
	public SqlCriteriaCondition(String field, Object value, String operator, String conditionOperator) {
		fField = field;
		fValue = value;
		fOperator = operator;
		fConditionOperator = conditionOperator;
	}

	public void setField(String field) {
		fField = field;
	}

	public String getFiled() {
		return fField;
	}

	public void setValue(String value) {
		fValue = value;
	}

	public Object getValue() {
		return fValue;
	}

	public void setOperator(String operator) {
		fOperator = operator;
	}

	public String getOperator() {
		return fOperator;
	}

	public void setConditionOperator(String conditionOperator) {
		fConditionOperator = conditionOperator;
	}

	public String getConditionOperator() {
		return fConditionOperator;
	}

	/**
	 * Validate SQL WHERE condition setups.
	 *
	 * fFields, fOperators, fValues should not be
	 * empty, and amount of these lists should be same.
	 *
	 * If fConditionOperator is null, then changing fConditionOperator
	 * to "", in order to achieve empty condition operation.
	 *
	 * @return True if all lists are matching valid rules.
	 */
	public boolean validateCriteriaCondition() {
		if (fField == null ) {
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
		if (fConditionOperator == null) {
			LOGGER.config("ConditionOperator is missing, changing ConditionOperator to empty string.");
			fConditionOperator = "";
		}
		return true;
	}
}
