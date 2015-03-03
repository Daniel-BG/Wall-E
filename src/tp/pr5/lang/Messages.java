package tp.pr5.lang;

public class Messages {
	//constantes de mensajes a mostrar del ROBOT
	public static final String ROBOT_NAME = "WALL·E";
	public static final String PROMPT = ROBOT_NAME + "> ";
	public static final String OUTPUT_SAYS = " says: ";
	public static final String OUTPUT_QUIT = "I have communication problems. Bye bye";
	public static final String OUTPUT_WRONGCOMMAND = "I do not understand. Please repeat";
	public static final String OUTPUT_ATSPACESHIP = "I am at my spaceship. Bye bye";
	public static final String OUTPUT_STREET_NO = "There is no street in direction ";
	public static final String OUTPUT_STREET_CLOSED = "Arrggg, there is a street but it is closed!";
	public static final String OUTPUT_ITEM_PICKED = "I am happy! Now I have "; 
	public static final String OUTPUT_ITEM_NOPICKED = "I am stupid! I had already the object ";
	public static final String OUTPUT_SCAN_ALL = "I am carrying the following items";
	public static final String OUTPUT_SCAN_NONE = "My inventory is empty";
	public static final String OUTPUT_OPERATE_USED = "What a pity! I have no more <id> in my inventory";
	public static final String OUTPUT_OPERATE_FAIL = "I have problems using the object ";
	public static final String OUTPUT_OPERATE_NOITEM = "I don't have that";
	public static final String OUTPUT_FUEL_NOFUEL = "I run out of fuel. I cannot move. Shutting down...";
	public static final String OUTPUT_SAYFUEL = "      * My power is ";
	public static final String OUTPUT_SAYRM = "      * My recycled material is ";
	public static final String OUTPUT_DROP_SUCCESS = "Great! I have dropped ";
	public static final String OUTPUT_DROP_PLACEFAIL = "The place has already the item ";
	public static final String OUTPUT_DROP_INVENTORYFAIL = "You do not have any ";
	public static final String OUTPUT_UNDO_SUCCESS = "Undo Sucessfull";
	public static final String OUTPUT_REDO_SUCCESS = "I redid the last instruction";
	
	//otras
	public static final String OUTPUT_SCANPLACE_NONE = "The place is empty. There are no objects to pick";
	public static final String OUTPUT_SCANPLACE_CONTAINS = "The place contains these objects:"; 
	public static final String OUTPUT_ROTATION_FAIL = " is not a valid Rotation";
	public static final String OUTPUT_INSTRUCTION_NO = "That is not a valid instruction";
	public static final String OUTPUT_INSTRUCTIONS_VALID = "The valid instructions for " + "WALL-E" + /*Messages.ROBOT_NAME +*/ " are:";
	public static final String POWER = "// power = ";
	public static final String TIMES = ", times = ";
	public static final String RECYCLED_MAT = "// recycled material = ";
	public static final String MAP_UNEXPLORED = "You have not explored this yet";
	
	
	//Direction
	public static final String DIRECTION_LOOKINGAT = ROBOT_NAME + " is looking at direction ";
	public static final String DIRECTION_MOVINGIN = "Moving in direction ";
	
	//City
	public static final String CITY_COMPILATING = "Compilating city...";
	public static final String CITY_COMPILATION_OK = "Compilation ended successfully\n";
	public static final String CITY_COMPILATION_FAIL = "Compilation ended with errors. Instability is possible\n";
	
	//Main
	public static final String BAD_PARAMS = "Bad params.";
	public static final String MAIN_USAGE_1 = "Usage: java tp.pr3.Main <mapfile>";
	public static final String MAIN_USAGE_2 = "<mapfile> : file with the description of the city.";
	
	





}
