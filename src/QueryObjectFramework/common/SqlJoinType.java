package QueryObjectFramework.common;

/**
 * Enum class for different JOIN types that
 * available in SELECT query Object.
 *
 * @author Bohui Axelsson
 */
public enum SqlJoinType {
	INNER_JOIN(SqlStatementStrings.SQL_TABLE_INNER_JOIN),
	LEFT_JOIN(SqlStatementStrings.SQL_TABLE_LEFT_JOIN),
	RIGHT_JOIN(SqlStatementStrings.SQL_TABLE_RIGHT_JOIN),
	FULL_JOIN(SqlStatementStrings.SQL_TABLE_FULL_JOIN);

	private String fSqlJoinType;

	SqlJoinType(String sqlJoinType) {
		this.fSqlJoinType = sqlJoinType;
	}

	public String sqlJoinType() {
		return fSqlJoinType;
	}
}