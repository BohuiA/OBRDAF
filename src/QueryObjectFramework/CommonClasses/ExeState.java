package QueryObjectFramework.CommonClasses;

/**
 * Enum class includes runtime execution status.
 *
 * @author Bohui Axelsson
 */
public enum ExeState {
	SUCESSFUL("Sucessful"),
	FAIL("Fail"),
	ERROR("Error");

	private String fExeState;

	ExeState(String exeState) {
		fExeState = exeState;
	}

	public String state() {
		return fExeState;
	}
}
