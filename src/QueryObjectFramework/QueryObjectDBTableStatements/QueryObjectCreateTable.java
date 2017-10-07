package QueryObjectFramework.QueryObjectDBTableStatements;

import java.sql.ResultSet;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import QueryObjectFramework.CommonClasses.SqlQueryTypes;
import QueryObjectFramework.JdbcDatabaseConnection.JdbcDatabaseConnection;

/**
 * Query object class for CREATE TABLE statement. The CREATE TABLE statement is
 * used to create a new SQL table.
 *
 * <example>
 * 	CREATE TABLE table_name (
 *   	column1 datatype constraint,
 *   	column2 datatype constraint,
 *   	column3 datatype constraint,
 *  		....
 *	);
 * </example>
 *
 * <example>
 * 	CREATE TABLE table_name (
 *   	column1 datatype constraint,
 *   	column2 datatype constraint,
 *   	column3 datatype constraint,
 *  		....
 *      UNIQUE(column2)
 *	);
 * </example>
 *
 * The column parameters specify the names of the columns of the table.
 *
 * The data type parameter specifies the type of data the column can hold (e.g.
 * VARCHAR, INT, DATE, etc.).
 *
 * NOTE: At the moment, MYSQL server is fully supported as altering
 * tables.
 *
 * NOTE: At the moment, CREATE TABLE Query Object only supports pre-defined columns way.
 * Create table using another table is not supported right now.
 *
 * @author Bohui Axelsson
 */
public class QueryObjectCreateTable extends QueryObjectDBTableAbstract {
	/**
	 * Create an CREATE TABLE query object with Table names.
	 *
	 * @param jdbcDbConn
	 * 			JDBC database connection
	 * @param tableName
	 * 			Table name
	 */
	public QueryObjectCreateTable(@NonNull JdbcDatabaseConnection jdbcDbConn, @NonNull String tableName) {
		super(SqlQueryTypes.CREATE_TABLE, jdbcDbConn, tableName);
	}

	/**
	 * Create an CREATE TABLE query object with Table names and TableColumns.
	 *
	 * @param jdbcDbConn
	 * 			JDBC database connection
	 * @param tableName
	 * 			Table name
	 * @param tableColumns
	 * 			Table column instances
	 */
	public QueryObjectCreateTable(@NonNull JdbcDatabaseConnection jdbcDbConn, @NonNull String tableName,
			@NonNull List<QueryObjectDBTableColumn> tableColumns) {
		super(SqlQueryTypes.CREATE_TABLE, jdbcDbConn, tableName, tableColumns);
	}

	/**
	 * Create a table.
	 *
	 * Scenario:
	 *
	 * <example>
	 *  CREATE TABLE Persons (
	 *    	PersonID INT(2147),
	 *  	LastName VARCHAR(255),
	 *   	FirstName VARCHAR(255),
	 *    	Address VARCHAR(255),
	 *   	City VARCHAR(255)
	 *	);
	 * </example>
	 *
	 * <example>
	 *  CREATE TABLE Persons (
	 *    	PersonID INT(2147) NOT NULL AUTO_INCREMENT,
	 *  	LastName VARCHAR(255),
	 *   	FirstName VARCHAR(255),
	 *    	Address VARCHAR(255),
	 *   	City VARCHAR(255) NOT NULL,
	 *      UNIQUE(PersonID)
	 *	);
	 * </example>
	 *
	 * <example>
	 *  CREATE TABLE Persons (
	 *    	PersonID INT(2147) NOT NULL AUTO_INCREMENT,
	 *  	LastName VARCHAR(255),
	 *   	FirstName VARCHAR(255),
	 *    	Address VARCHAR(255),
	 *   	City VARCHAR(255) NOT NULL,
	 *      CONSTRAINT UC_Persons UNIQUE(PersonID, LastName, FirstName)
	 *	);
	 *
	 * NOTE: Currently, the UNIQUE name of multiple columns is hard coupled to table
	 * name, as format "UC_<table_name>".
	 * </example>
	 *
	 * NOTE: Only one table can be associated to one Query Object.
	 *
	 * NOTE: Column data type will be defined with QueryObejctDBTableColumn class,
	 * more details see QueryObejctDBTableColumn class.
	 *
	 * @return ResultSet SQL execution results
	 */
	public ResultSet createTable() {
		if (!validateTableColumnsNotNull()) {
			return null;
		}

		String sql = fQueryObjectType.sqlQueryType() + " " + fTableName + " (" + buildFullColumnSettingString() + ")" + ";";
		return fJdbcDbConn.executeQueryObject(sql);
	}
}
