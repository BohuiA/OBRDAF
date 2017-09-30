package QueryObjectFramework.CommonClasses;

/**
 * class for creating a SQL column table data type.
 *
 * NOTE: For creating a SqlColumnDataType, users needs to pre-choose
 * a SqlDataTypes type. More details see SqlDataTypes class.
 *
 * @author Bohui Axelsson
 */
public class SqlColumnDataType {
	private SqlDataTypes fDataType = SqlDataTypes.EMPTYDATATYPE;
	private int fDataTypeRange;
	private int fDataTypeDecimal;

	/**
	 * Create a SQL data type without data range and data decimal.
	 *
	 * @param dataType
	 *            Data type that need to be created for the column.
	 */
	public SqlColumnDataType(SqlDataTypes dataType) {
		fDataType = dataType;
		fDataTypeRange = Integer.MIN_VALUE;
		fDataTypeDecimal = Integer.MIN_VALUE;
	}

	/**
	 * Create a SQL data type with data range.
	 *
	 * @param dataType
	 *            Data type that need to be created for the column.
	 * @param dataRange
	 *            Data type range that this data type needs.
	 */
	public SqlColumnDataType(SqlDataTypes dataType, int dataRange) {
		fDataType = dataType;
		fDataTypeRange = dataRange;
		fDataTypeDecimal = Integer.MIN_VALUE;
	}

	/**
	 * Create a SQL data type with data range and data decimal.
	 *
	 * @param dataType
	 *            Data type that need to be created for the column.
	 * @param dataRange
	 *            Data type range that this data type needs.
	 * @param dataDecimal
	 *            Data type decimal that this data type needs.
	 */
	public SqlColumnDataType(SqlDataTypes dataType, int dataRange, int dataDecimal) {
		fDataType = dataType;
		fDataTypeRange = dataRange;
		fDataTypeDecimal = dataDecimal;
	}

	/**
	 * Check a column data type is text based type.
	 *
	 * @return True if data type is text based.
	 */
	public boolean isTextDataType() {
		switch (fDataType) {
		case CHAR:
		case VARCHAR:
		case TINYTEXT:
		case TEXT:
		case BLOB:
		case MEDIUMTEXT:
		case MEDIUMBLOB:
		case LONGTEXT:
		case LONGBLOB:
			return true;
		default:
			return false;
		}
	}

	/**
	 * Check a column data type is date based type.
	 *
	 * @return True if data type is date based.
	 */
	public boolean isDateDataType() {
		switch (fDataType) {
		case DATE:
		case DATETIME:
		case TIMESTAMP:
		case TIME:
		case YEAR:
			return true;
		default:
			return false;
		}
	}

	/**
	 * Get SQL column data type as string for creating a column during creating
	 * table via SQL.
	 *
	 * @return String Full SQL column data type string.
	 */
	public String getSqlColumnDataType() {
		switch (fDataType) {
		case CHAR:
		case VARCHAR:
		case TINYINT:
		case SMALLINT:
		case MEDIUMINT:
		case INT:
		case BIGINT:
			return fDataType.dataTypeString() + "(" + fDataTypeRange + ")";
		case FLOAT:
		case DOUBLE:
		case DECIMAL:
			return fDataType.dataTypeString() + "(" + fDataTypeRange + ", " + fDataTypeDecimal + ")";
		default:
			return fDataType.dataTypeString();
		}
	}
}
