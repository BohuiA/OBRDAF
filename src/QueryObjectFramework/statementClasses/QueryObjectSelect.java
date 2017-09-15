package QueryObjectFramework.statementClasses;

import java.util.List;

import QueryObjectFramework.common.SqlQueryTypes;
import QueryObjectFramework.jdbc.JdbcDatabaseConnection;
import QueryObjectFramework.queryObjectBase.QueryObjectAbstract;

/**
 * Query object class for Select statement.
 * 
 * @author Bohui Axelsson
 */
public class QueryObjectSelect extends QueryObjectAbstract {

	public QueryObjectSelect(JdbcDatabaseConnection jdbcDbConn, List<String> tables, 
			List<String> fileds, List<String> values) {
		super(SqlQueryTypes.SELECT, jdbcDbConn, tables, fileds, values);
	}
	
	
}
