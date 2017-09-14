package queryObjectSelect;

import java.util.List;

import common.*;

public class QueryObjectSelect extends QueryObjectAbstract{

	/*
	 * Scenario : 
	 * 	SELECT column1, column2, ... FROM table_name;
	 *  or
	 *  SELECT * FROM table_name;
	 */
	
	public QueryObjectSelect(List<String> fileds, List<String> values) {
		super(SqlQueryTypes.SELECT, fileds, values);
		
		/*
		 * Other fields ?
		 */
	}
}
