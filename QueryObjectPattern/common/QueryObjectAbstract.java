package common;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class for Query Object pattern. Contains basic SQL database
 * operations and interface for customized SQL operations.
 * 
 * SQL statement tutorial website link:
 * https://www.w3schools.com/sql/sql_select.asp
 * 
 * @author Bohui Axelsson
 */
public class QueryObjectAbstract {
	public final SqlQueryTypes fQueryObjectType;
	public final List<String> fFileds = new ArrayList<String>();
	public final List<String> fValues = new ArrayList<String>();
	
	public QueryObjectAbstract(SqlQueryTypes queryObjectType, List<String> fileds, List<String> values) {
		fQueryObjectType = queryObjectType;
		fFileds.addAll(fileds);
		fValues.addAll(values);
	}
}
