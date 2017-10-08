package QueryObjectFramework.QueryObjectDBTableColumnConstraint;

import org.eclipse.jdt.annotation.NonNull;

/**
 * FOREIGN KEY:
 * A FOREIGN KEY is a key used to link two tables together.
 * A FOREIGN KEY is a field (or collection of fields) in one
 * table that refers to the PRIMARY KEY in another table.
 *
 * The table containing the foreign key is called the child table,
 * and the table containing the candidate key is called the referenced
 * or parent table.
 *
 * <example>
 *  CREATE TABLE Orders (
 *  		OrderID int NOT NULL PRIMARY KEY,
 *  		OrderNumber int NOT NULL,
 *  		PersonID int FOREIGN KEY REFERENCES Persons(PersonID)
 *  );
 * </example>
 *
 * <example>
 *  ALTER TABLE Orders
 *  ADD FOREIGN KEY (PersonID) REFERENCES Persons(PersonID);
 * </example>
 *
 * <example>
 * 	ALTER TABLE Orders
 * 	DROP FOREIGN KEY FK_PersonOrder;
 * </example>
 *
 * NOTE: At the moment, only one FOREIGN KEY column can be declared.
 * Multiple columns FOREIGN KEYs is not supported currently.
 *
 * TODO: Introducing multiple FOREIGN KEYs feature into this class.
 *
 * @author Bohui Axelsson
 */
public class QueryObjectDBTableConstraintForeignKey implements QueryObjectDBTableConstraintDecorator {
	private QueryObjectDBTableConstraintDecorator fConstraintDecorator = null;
	private @NonNull String fReferencedTableName = "";
	private @NonNull String fReferencedColumnName = "";

	public QueryObjectDBTableConstraintForeignKey(QueryObjectDBTableConstraintDecorator constraintDecorator,
			@NonNull String referencedTableName, @NonNull String referencedColumnName) {
		fConstraintDecorator = constraintDecorator;
		fReferencedTableName = referencedTableName;
		fReferencedColumnName = referencedColumnName;
	}
	
	public String getReferencedTableName() {
		return fReferencedTableName;
	}
	
	public String getReferencedColumnName() {
		return fReferencedColumnName;
	}

	@Override
	public String createConstraintString() {
		return fConstraintDecorator == null ? ""
				: fConstraintDecorator.createConstraintString() + " ";
	}

	@Override
	public QueryObjectDBTableConstraintDecorator getConstraintDecorator() {
		return fConstraintDecorator;
	}

}
