package tp.pr5.util;

import java.io.IOException;
import java.io.InputStream;
/**
 * Useful class for reading from InputStreams. Can handle:
 * <il>
 * <ul>-Escape characters</ul>
 * <ul>-Ignorable characters</ul>
 * <ul>-Jump characters</ul>
 * <ul>-Quote characters</ul>
 * </il>
 * The details of the implementation are commented on each function
 * 
 * @author Daniel Báscones García
 * @version 1.0
 */
public class InputStreamReader {
	/**
	 * Constructor
	 * @param iOStream
	 */
	public InputStreamReader (InputStream iOStream) {
		this.iOStream = iOStream;
	}
	
	//array checks
	
	/**
	 * Checks whether a character is contained in an array of characters
	 * @param a character to look for in the array
	 * @param array where to look for "a"
	 * @return true if a match was found, false otherwise
	 */
	public static boolean charIsInArray(char a, char[] array) {
		for (int i = 0; i < array.length; i++)
			if (a == array[i])
				return true;
		return false;
	}
	/**
	 * Checks if a character is especial 
	 * @param a character to check
	 * @param array array of possible especial characters
	 * @return false if the escape character was read before or the character is not present on the array. True otherwise
	 */
	private boolean isSpecialChar(char a, char[] array) {
		if (this.escapeWasRead)
			return false;
		else
			return InputStreamReader.charIsInArray(a, array);
	}
	
	/**
	 * Checks if a character is a jump character
	 * @param a
	 * @return true if a is indeed a jump character, false if not
	 */
	private boolean isJumpChar(char a) {
		return this.isSpecialChar(a, this.jumpChars);
	}
	/**
	 * Checks if a character is an ignorable character
	 * @param a
	 * @return true if a is indeed an ignorable character, false if not
	 */
	private boolean isIgnorable(char a) {
		return this.isSpecialChar(a, this.ignorableChars);
	}
	/**
	 * Checks if a character is a quote character
	 * @param a
	 * @return true if a is a quote character, false otherwise
	 */
	private boolean isQuote (char a) {
		return this.isSpecialChar(a, this.quoteChar);
	}
	/**
	 * Checks if a character is an escape character
	 * @param a char to check
	 * @return true if a is an escape character, false otherwise
	 */
	private boolean isEscapeChar(char a){
		return this.isSpecialChar(a, this.escapeChar);	
	}
	
	//Word getter
	
	/**
	 * Mask for this.getWord()
	 * @return next Word read by this following the reading rules. The upper in this list, the stronger they are: <br/>
	 * <il>
	 * <ul>Any time a character is read, the column count increases by 1 </ul> 
	 * <ul>Any time an escape character is found, it is ignored and the next one is added to the word no matter what, except when the escape character was preceded by an escape character, in which case it is added to the word.</ul>
	 * <ul>Any time a jump character is found, 1 is added to the row count and the column count is reset to 0</ul>
	 * <ul>Any time a quote character preceded by an escape character is found, every character until the next quote is taken as a word, ignoring both quotes</ul>
	 * <ul>Whenever a ignorable character is found, it is excluded out of the word and, if the word wasn't empty, ends it.</ul>
	 * </il>
	 * This implementation leads to a quote inside a word being taken as part of the word. E.G:<br/>
	 * 	- Hello w"orld. This is "Input Stream Reader" at your Ser\ vice<br/>
	 * 	Will be split like so (with the default cofiguration)<br/>
	 * <il>
	 * <ul>-Hello</ul>
	 * <ul>-w"orld.</ul>
	 * <ul>-This</ul>
	 * <ul>-is</ul>
	 * <ul>-Input Stream Reader</ul>
	 * <ul>-at</ul>
	 * <ul>-your</ul>
	 * <ul>-Ser vice</ul>
	 * </il>
	 * @throws IOException 
	 */
	public String nextWord() throws IOException {
		return this.getWord();
	}

	//Internal getters
	/**
	 * Gets the next word (separated by ignorableCharacters) on the stream. If the word starts with quoteChar, it will pick the text between quoteChars as the word.<br/>
	 * It leaves the reading pointer at the location after the first ignorableChar after the word, or out of the InputStream if the word was the last one in the stream.
	 * @param iOStream file to get the word from
	 * @return The read word or the previous read word if there was a syntax Error previously
	 * @throws IOException if there is no word to be read
	 */
	private String getWord() throws IOException {
		String res = "";
		char currentChar = this.getChar(iOStream);
		
		//remove all ignorable characters
		while (isIgnorable(currentChar)) {
			currentChar = this.getChar(iOStream);
		}
		
		//initialize various variables to control word parsing
		boolean salir = false, comillas = false, continuar = true;
		//check for a quote starting the word to remove it and initialize quote word splitting
		if (this.isQuote(currentChar)){
			comillas = true;
			currentChar = this.getChar(iOStream);
		}
		//read characters until the word is complete (either EOF, ignorable character, or quote mark
		while (!salir && continuar) {
			res += currentChar;
			try {
				currentChar = this.getChar(iOStream);
			} catch (IOException ex) {
				//EOF reached
				salir = true;
			}
			if (comillas) 	//if quote word splitting is active the end of word comes with a quote
				continuar = !(this.isQuote(currentChar));
			else			//if not, any ignorable character will end the word
				continuar = !isIgnorable(currentChar);
		}
		lastWordRead = res;
		return res;
	}
	/**
	 * gets the next char on iOStream and updates the current line and column the cursor is reading at
	 * @param iOStream stream to read from
	 * @return the next valid character
	 * @throws IOException if the char couldn't be read
	 */
	private char getChar(InputStream iOStream) throws IOException {
		column++;
		char a = readChar(iOStream);
		escapeWasRead = false;			//quitamos el flag de escape leído
		if (isJumpChar(a)) {
			line++;
			column = 0;
		}
		if (isEscapeChar(a)) {
			column++;
			a = readChar(iOStream);		//cogemos el siguiente sea cual sea
			this.escapeWasRead = true;	//ponemos a true el flag de escape leído para que las comprobaciones de caracter especial se anulen hasta lectura de uno nuevo
		}
		return a;
	}
	/**
	 * Reads the next character in the stream
	 * @param iOStream stream to read from
	 * @return the character that was read
	 * @throws IOException when EOF was reached
	 */
	private char readChar(InputStream iOStream) throws IOException {
		int result = iOStream.read();
		if (result == -1)
			throw new IOException("End of file reached"/*ErrorMessages.IOE_EOF*/); //change this with anything you like
		return (char) result;
	}
	
	//Getters for various variables
	
	/**
	 * Getter for lastwordread
	 * @return the last word that was read, for errors mainly, to know where they happened
	 */
	public String getLastWordRead() {
		return this.lastWordRead;
	}
	/**
	 * Getter for line
	 * @return current line of the file (separated by ends of line) where the last character has been read
	 */
	public int getLine() {
		return this.line;
	}
	/**
	 * Getter for column
	 * @return current column of the file (number of characters read since last end of line) where the last character has been read
	 */
	public int getColumn() {
		return this.column;
	}
	
	//Setters for especial characters and various varialbes
	
	/**
	 * Sets the array of ignorable characters. These will be ignored when reading (treated as spacing characters) except when preceded by the escape character.<br/>
	 * Default: {' ','\r','\n'}
	 * @param array
	 */
	public void setIgnorableChars(char[] array) {
		this.ignorableChars = array;
	}
	/**
	 * Sets the quote char. This character will be ignored when reading and everything within it taken as a valid character (including ignorable ones).<br/>
	 * Ignoring it can be bypassed by preceding it with the escape character
	 * Default: '"'
	 * @param quote
	 */
	public void setQuoteChar(char quote) {
		this.quoteChar[0] = quote;
	}
	/**
	 * Sets the escape char. This character will always be ignored but the next character (even if it is the escape character) will be taken bypassing any ignoration<br/>
	 * Default: '\\'
	 * @param escape
	 */
	public void setEscapeChar(char escape) {
		this.escapeChar[0] = escape;
	}
	/**
	 * Sets the array of jump characters. These will NOT be ignored unless being set as ignorable chars. Only used to know the position of the current line.<br/>
	 * Default: {'\n'}
	 * @param array
	 */
	public void setJumpChars(char[] array) {
		this.jumpChars = array;
	}
	/**
	 * Sets the current reading line (only the number is changed, not the position of the reading pointer)
	 * @param newLine
	 */
	public void setCurrentLine(int newLine) {
		this.line = newLine;
	}
	/**
	 * Sets the current reading column (only the number is changed, not the position of the reading pointer)
	 * @param newColumn
	 */
	public void setCurrentColumn(int newColumn) {
		this.column = newColumn;
	}
	
	private String lastWordRead = "";					//last word read by the reader
	private int line = 1;								//current line of reading pointer
	private int column = 0;								//current column of reading pointer
	private boolean escapeWasRead = false;				//variable that indicates the finding of the escape character
	private InputStream iOStream;						//iOStream from where the class is reading
	
	private char[] ignorableChars = {' ','\r','\n'};	//ignored characters when reading (except when inside quoteChars). Also used as separations between words
	private char[] quoteChar = {'"'};					//char used to bypass the ignored characters that fall between two of these
	private char[] jumpChars = {'\n'};					//char(s) used to indicate an end of line
	private char[] escapeChar = {'\\'};					//char used to bypass all other characters. It will always force the next character to be taken as a valid one, and it will be ignored

}
