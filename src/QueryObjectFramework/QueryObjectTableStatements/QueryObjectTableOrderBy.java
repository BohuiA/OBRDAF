package QueryObjectFramework.QueryObjectTableStatements;

import java.util.logging.Logger;

import org.eclipse.jdt.annotation.NonNull;

/**
 * Data class contains ORDER BY SQL statement relative
 * information.
 *
 * ORDER BY relative information:
 * - Order By Column Name
 * - Order By Ordering.
 *
 * @author Bohui Axelsson
 */
public class QueryObjectTableOrderBy {
	private static final Logger LOGGER = Logger.getLogger(QueryObjectTableOrderBy.class.getName());

	private @NonNull String fOrderByColumn = "";
	private @NonNull String fOrderByOrdering = "";

	/**
	 * Create a QueryObjectTableOrderBy instance.
	 *
	 * TIP: Don't reuse an existing instance, creating a new order by instance
	 * if any filed will be updated.
	 *
	 * @param orderByColumn
	 * 			Column name for order by
	 * @param orderByOrdering
	 * 			Order by ordering, Can only be ASC or DESC.
	 */
	public QueryObjectTableOrderBy(String orderByColumn, String orderByOrdering) {
		if (orderByColumn != null) {
			fOrderByColumn = orderByColumn;
		} else {
			fOrderByColumn = "";
		}
		if (orderByOrdering != null) {
			fOrderByOrdering = orderByOrdering;
		} else {
			fOrderByOrdering = "";
		}
	}

	public String getOrderByColumn() {
		return fOrderByColumn;
	}

	public String getOrderByOrdering() {
		return fOrderByOrdering;
	}

	/**
	 * Validate SQL ORDER BY columns and orders settings.
	 *
	 * fOrderByOrderings can only be ASC or DESC.
	 *
	 * @return True if orderings are valid.
	 */
	public boolean validateOrderByOrderings() {
		if (!fOrderByOrdering.equalsIgnoreCase("ASC") && !fOrderByOrdering.equalsIgnoreCase("DESC")) {
			LOGGER.severe("Failed to select all fileds from table, OrderBy orders must by ASC or DESC.");
			return false;
		}
		return true;
	}
}
