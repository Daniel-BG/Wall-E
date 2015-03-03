package tp.pr5.lang;

public class ErrorMessages {

	//InstructionExecutionException
	public static final String IEE_STREET_NO = "NoStreet";
	public static final String IEE_STREET_CLOSED = "ClosedStreet";
	public static final String IEE_RUNTIMEERROR = "Parameters not valid for that instruction";
	public static final String IEE_SCAN_FAIL = "I lost it when I was looking for garbage";
	public static final String IEE_ITEM_NOITEM = "Ooops, this place has not the object ";
	public static final String IEE_UNDO_UNEXISTENT = "There is no undo to do!";
	public static final String IEE_REDO_UNEXISTENT = "I have nothing to redo!";
	public static final String IEE_FUEL_NOFUELLEFT = "I can't do that, i don't have enough fuel left!";
	
	//WrongCityFormatException
	public static final String WCFE_CEDS_OFF = "CEDS is off, couldn't recover from Syntax error";
	public static final String WCFE_FATALERROR = "Fatal error at: ";
	public static final String WCFE_SYNTAXERROR = "Syntax error at: ";
	public static final String WCFE_EXPECTED = "Expected";
	public static final String WCFE_NUMBER_LESS = "A number less than ";	
	//IOException 
	public static final String IOE_EOF = "End of file reached";
	
	//WrongInstructionFormatException
	public static final String WIFE_EMPTY = "An empty String is not a valid Instruction";
	public static final String WIFE_COMMAND_FAIL = "The command <command> didn't match any command";
	public static final String WIFE_ARGUMENT_EXPECTED = "Argument expected";
	
	//main, errores derivados de excepciones de fichero que NO van contemplados en el mensaje de la excepción
	public static final String MAP_FILE_READING = "Error reading the map file: ";
	public static final String MAP_FILE_WRONG_SYNTAX = " (Fichero con sintaxis incorrecta)";
	public static final String MAP_FILE_NO_EXIST = " (No existe el fichero o el directorio)";
	
	
	
	
}
