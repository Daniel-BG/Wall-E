package tp.pr5.util;

import java.io.IOException;
/**
 * @deprecated Use ArgumentParser instead
 *
 */
public class CommandParser {
	private String[] args;
	private int currentArg = 0;
	
	/**
	 * Builds the object with the proper command line
	 * @param args arguments the program received
	 */
	public CommandParser (String[] args) {
		this.args = args;
	}
	
	/**
	 * Gets the current argument in the input String[]
	 * @return the current argument
	 * @throws IOException if there are no arguments left
	 */
	public String getCurrentArg() throws IOException{
		if (this.args.length <= this.currentArg)
			throw new IOException("End of command line reached");
		return this.args[currentArg];
	}

	/**
	 * @param s String to check for command syntax
	 * @return true if s is "-X", X being any alphanumeric character
	 */
	private boolean isCommand(String s) {
		if (s.length() != 2)
			return false;
		if (s.charAt(0) == '-')
			return true;
		return false;
	}
	
	/**
	 * Removes and returns the current argument from the list of arguments stored
	 * @return the current argument
	 * @throws IOException whenever there are no arguments left (getCurrentArg() would return the last argument on the input)
	 */
	private String removeFirstArg() throws IOException {
		String ret = this.getCurrentArg();
		this.currentArg++;
		return ret;
	}
	
	/**
	 * @return the next command in the input list
	 * @throws IOException when there are no commands left on the input
	 */
	public String getCommand() throws IOException{
		while (!isCommand(this.getCurrentArg())) 
			this.removeFirstArg();
		
		return this.getCurrentArg();
	}
	
	/**
	 * 
	 * @return the next argument on the list, leaving the pointer to the next one
	 * @throws IOException if there were no more arguments left
	 */
	public String nextArg() throws IOException {
		return this.removeFirstArg();
	}
	
	/**
	 * 
	 * @return the next command on the list, leaving the pointer to the next one
	 * @throws IOException if there were no more commands left
	 */
	public String nextCommand() throws IOException {
		this.getCommand();
		return this.removeFirstArg();
	}
	
	/**
	 * @return true if there are no arguments left
	 */
	public boolean isEmpty() {
		return this.args.length == this.currentArg;
	}
}
