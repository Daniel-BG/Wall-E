package tp.pr5.util;

import tp.pr5.instructions.exceptions.WrongInstructionFormatException;
import tp.pr5.lang.ErrorMessages;

public class InstructionParser {
	/**
	 * Checks for a match between the first argument in args and command, splitting args with .split(" ") and checking the first resulting field
	 * @param args Input args
	 * @param command Command
	 * @return The rest of the arguments. "" if there are no more arguments.
	 * @throws WrongInstructionFormatException When args was empty or when the first argument didn't match the command
	 */
	public static String parseCommand(String args, String[] command) throws WrongInstructionFormatException{
		if (args == null || args == "")
			throw new WrongInstructionFormatException(ErrorMessages.WIFE_EMPTY); //empty String
		
		String[] splitted = args.split(" ");
		boolean match = false;
		for (int i = 0; i < command.length; i++) 
			if (splitted[0].toUpperCase().equals(command[i])) 
				match = true;
		if (!match)
			throw new WrongInstructionFormatException(ErrorMessages.WIFE_COMMAND_FAIL.replace("<command>", splitted[0])); //argument didn't match the command
		
		if (splitted.length == 1)
			return "";
		String ret;
		ret = InstructionParser.removeFirstArg(args); 
		return ret;
	}
	
	
	/**
	 * Gets the first argument in string args and removes it from args, leaving the next argument in the first place
	 * @param args 
	 * @return The first argument, separated by " " in the string
	 * @throws WrongInstructionFormatException When the string is empty
	 */
	public static String getArg(String args) throws WrongInstructionFormatException {
		if (args == null || args == "")
			throw new WrongInstructionFormatException(ErrorMessages.WIFE_ARGUMENT_EXPECTED); // argument expected
		String ret = args.split(" ")[0];
		args = InstructionParser.removeFirstArg(args); //actualizamos args
		return ret;
	}
	
	
	private static String removeFirstArg(String args) {
		String[] splitted = args.split(" ");
		String ret;
		try {
			ret = args.substring(splitted[0].length()+1, args.length()); 
		} catch (Exception ex) { //OutOfBounds
			ret = "";
		}
		return ret;
	}
	
}
