package QueryObjectFramework.CommonClasses;

/**
 * Enumeration class contains available SQL table data types.
 *
 * NOTE: Note: Data types might have different names in different database. And
 * even if the name is the same, the size and other details may be different!
 * Always check the documentation!
 *
 * NOTE: Currently only support MYSQL database server.
 *
 * @author Bohui Axelsson
 */
public enum SqlDataTypes {
	/*
	 * Default data type
	 */
	EMPTYDATATYPE("EMPTY DATA TYPE"),
	/*
	 * Text data types
	 */
	CHAR("CHAR"),
	VARCHAR("VARCHAR"),
	TINYTEXT("TINYTEXT"),
	TEXT("TEXT"),
	BLOB("BLOB"),
	MEDIUMTEXT("MEDIUMTEXT"),
	MEDIUMBLOB("MEDIUMBLOB"),
	LONGTEXT("LONGTEXT"),
	LONGBLOB("LONGBLOB"),
	/*
	 * Number data types
	 *
	 * NOTE: The integer types have an extra option called UNSIGNED. Normally, the
	 * integer goes from an negative to positive value. Adding the UNSIGNED
	 * attribute will move that range up so it starts at zero instead of a negative
	 * number.
	 */
	TINYINT("TINYINT"),
	SMALLINT("SMALLINT"),
	MEDIUMINT("MEDIUMINT"),
	INT("INT"),
	BIGINT("BIGINT"),
	FLOAT("FLOAT"),
	DOUBLE("DOUBLE"),
	DECIMAL("DECIMAL"),
	/*
	 * Date data types
	 *
	 * NOTE:Even if DATETIME and TIMESTAMP return the same format, they work very
	 * differently. In an INSERT or UPDATE query, the TIMESTAMP automatically set
	 * itself to the current date and time. TIMESTAMP also accepts various formats,
	 * like YYYYMMDDHHMISS, YYMMDDHHMISS, YYYYMMDD, or YYMMDD.
	 */
	DATE("DATE()"),
	DATETIME("DATETIME()"),
	TIMESTAMP("TIMESTAMP()"),
	TIME("TIME()"),
	YEAR("YEAR()");

	private String fDataType;

	SqlDataTypes(String dataType) {
		this.fDataType = dataType;
	}

	public String dataTypeString() {
		return fDataType;
	}
}
