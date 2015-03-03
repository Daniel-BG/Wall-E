package tp.pr5.instructions.exceptions;

public class InstructionExecutionException extends Exception{

	/**
	 * Default constructor
	 */
	public InstructionExecutionException() {}
	/**
	 * Constructor with message attached
	 * @param arg0
	 */
	public InstructionExecutionException(String arg0) {
		super(arg0);
	}
	/**
	 * Constructor with throwable attached
	 * @param arg0
	 */
	public InstructionExecutionException(Throwable arg0) {
		super(arg0);
	}
	/**
	 * Constructor with both message and throwable attached
	 * @param arg0
	 * @param arg1
	 */
	public InstructionExecutionException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}
	


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
