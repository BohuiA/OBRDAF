package example.common;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class for Query Object pattern. Contains basic SQL database
 * operations and interface for customized SQL operations.
 * 
 * @author Bohui Axelsson
 */
public final class queryObjectAbstract {
	public final sqlQueryTypes fQueryObjectType;
	public final List<String> fFileds = new ArrayList<String>();
	public final List<String> fValues = new ArrayList<String>();
	
	public queryObjectAbstract(sqlQueryTypes queryObjectType, List<String> fileds, List<String> values) {
		fQueryObjectType = queryObjectType;
		fFileds.addAll(fileds);
		fValues.addAll(values);
	}
}
