package tp.pr5.cityLoader.cityLoaderExceptions;


public class WrongCityFormatException extends java.io.IOException {

	/**
	 * Default constructor
	 */
	public WrongCityFormatException() {}
	/**
	 * Constructor with message attached
	 * @param arg0
	 */
	public WrongCityFormatException(String arg0) {
		super(arg0);
	}
	/**
	 * Constructor with throwable attached
	 * @param arg0
	 */
	public WrongCityFormatException(Throwable arg0) {
		super(arg0);
	}
	/**
	 * Constructor with both message and throwable attached
	 * @param arg0
	 * @param arg1
	 */
	public WrongCityFormatException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}
	


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
