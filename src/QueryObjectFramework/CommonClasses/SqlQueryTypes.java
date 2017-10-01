package QueryObjectFramework.CommonClasses;

public enum SqlQueryTypes {
	/*
	 * Table SQL statements
	 */
	SELECT("SELECT"),
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
	TRUNCATE_TABLE("TRUNCATE TABLE"),
	ALTER_TABLE("ALTER TABLE"),
	CREATE_INDEX("CREATE INDEX");

	private String fSqlQueryType;

	SqlQueryTypes(String sqlQueryType) {
		this.fSqlQueryType = sqlQueryType;
	}

	public String sqlQueryType() {
		return fSqlQueryType;
	}
}
