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
	 */
	private String fConditionOperator = null;

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
		if (fField == null) {
			LOGGER.severe("Failed to select fileds from table, fileds name is missing.");
			return false;
		}
		if (fValue == null) {
			LOGGER.severe("Failed to select fileds from table, values names are missing.");
			return false;
		}
		if (fOperator == null) {
			LOGGER.severe("Failed to select fileds from table, operators are missing.");
			return false;
		}
		if (fConditionOperator == null) {
			fConditionOperator = "";
		}
		return true;
	}
}
