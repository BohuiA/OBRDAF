package QueryObjectFramework.statementClasses;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Logger;

import QueryObjectFramework.common.SqlQueryTypes;
import QueryObjectFramework.common.SqlStatementStrings;
import QueryObjectFramework.jdbc.JdbcDatabaseConnection;
import QueryObjectFramework.queryObjectBase.QueryObjectAbstract;

/**
 * Query object class for Select statement.
 * 
 * @author Bohui Axelsson
 */
public class QueryObjectSelect extends QueryObjectAbstract {
	private static final Logger LOGGER = Logger.getLogger(QueryObjectSelect.class.getName());
	
	public QueryObjectSelect(JdbcDatabaseConnection jdbcDbConn) {
		super(SqlQueryTypes.SELECT, jdbcDbConn);
	}
	
	public QueryObjectSelect(JdbcDatabaseConnection jdbcDbConn, List<String> tables, 
			List<String> fileds, List<String> values) {
		super(SqlQueryTypes.SELECT, jdbcDbConn, tables, fileds, values);
	}
	
	/**
	 * Select all columns from the initialized table.
	 * 
	 * @return ResultSet
	 * 			SQL execution results
	 */
	public ResultSet selectAll() {
		if (fTables.isEmpty()) {
			LOGGER.severe("Failed to select all fileds from table, table name is missing.");
			return null;
		}
		
		/*
		 * SQL statement: "SELECT * FROM table_name;"
		 */
		String sql = SqlQueryTypes.SELECT.sqlQueryType() 
				+ " * "
				+ SqlStatementStrings.SQL_TABLE_FROM + " " + fTables.get(0) + ";";
		return fJdbcDbConn.executeQueryObject(sql);
	}
	
	/**
	 * Select specific fields from the initialized table.
	 * 
	 * @return ResultSet
	 * 			SQL execution results
	 */
	public ResultSet selectFileds() {
		if (fTables.isEmpty()) {
			LOGGER.severe("Failed to select fileds from table, table name is missing.");
			return null;
		}
		if (fFileds.isEmpty()) {
			LOGGER.severe("Failed to select fileds from table, filed names are missing.");
			return null;
		}
		
		String colunms = "";
		for (String filedName : fFileds) {
			colunms += filedName + ", ";
		}
		colunms.substring(0, colunms.length() - 1);
		
		String sql = SqlQueryTypes.SELECT.sqlQueryType() 
				+ " " + colunms + " " 
				+ SqlStatementStrings.SQL_TABLE_FROM + " " + fTables.get(0) + ";";
		return fJdbcDbConn.executeQueryObject(sql);
	}
}
