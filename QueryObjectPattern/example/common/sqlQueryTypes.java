package example.common;

public enum sqlQueryTypes {
	/*
	 * Table SQL statements
	 */
	SELECT("SELECT"),
	SELECT_DISTINCT("SELECT DISTINCT"),
	INSERT("INSERT INTO"),
	UPDATE("UPDATE"),
	DELETE("DELETE"),
	
	/*
	 * Database SQL statements
	 */
	CREATE_DATABASE("CREATE DATABASE"),
	DROP_DATABASE("DROP DATABASE"),
	CREATE_TABLE("CREATE TABLE"),
	DROP_TABLE("DROP TABLE"),
	ALTER_TABLE("ALTER TABLE"),
	CREATE_INDEX("CREATE INDEX");
	
	private String fSqlQueryType;
	
	sqlQueryTypes(String sqlQueryType) {
		this.fSqlQueryType = sqlQueryType;
	}
	
	public String sqlQueryType() {
		return fSqlQueryType;
	}
}
