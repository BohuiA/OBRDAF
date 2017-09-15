package QueryObjectFramework.queryObjectBase;

import java.util.ArrayList;
import java.util.List;

import QueryObjectFramework.common.SqlQueryTypes;
import QueryObjectFramework.jdbc.JdbcDatabaseConnection;

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
	public final JdbcDatabaseConnection fJdbcDbConn;
	public final List<String> fTables = new ArrayList<>();
	public final List<String> fFileds = new ArrayList<>();
	public final List<String> fValues = new ArrayList<>();
	
	public QueryObjectAbstract(SqlQueryTypes queryObjectType, JdbcDatabaseConnection jdbcDbConn, List<String> tables, 
			List<String> fileds, List<String> values) {
		fQueryObjectType = queryObjectType;
		fJdbcDbConn = jdbcDbConn;
		fTables.addAll(tables);
		fFileds.addAll(fileds);
		fValues.addAll(values);
	}
}
