package tp.pr5;

import tp.pr5.instructions.DropInstruction;
import tp.pr5.instructions.HelpInstruction;
import tp.pr5.instructions.Instruction;
import tp.pr5.instructions.MoveInstruction;
import tp.pr5.instructions.OperateInstruction;
import tp.pr5.instructions.PickInstruction;
import tp.pr5.instructions.QuitInstruction;
import tp.pr5.instructions.RadarInstruction;
import tp.pr5.instructions.RedoInstruction;
import tp.pr5.instructions.ScanInstruction;
import tp.pr5.instructions.TurnInstruction;
import tp.pr5.instructions.UndoInstruction;
import tp.pr5.instructions.exceptions.WrongInstructionFormatException;
import tp.pr5.lang.Messages;


public class Interpreter {
	/**	
	 * Generates a new instruction according to the user input.
	 * @param 	line a string with the user input
	 * @return	instruction The instruction read from the given line. If the instruction is not correct, then it throws an exception.
	 */
	public static Instruction generateInstruction (String line) throws WrongInstructionFormatException{
		//comprobamos que la instrucción tenga una longitud válida pues el parser no lo tiene en cuenta
		if (line.split(" ").length == 0) 
			throw new WrongInstructionFormatException(); 
		
		//miramos a ver si cuadra con alguna de las instrucciones guardadas en el array de instrucciones posibles.
		for (int i = 0; i < Interpreter.instruccionesPosibles.length; i++) {
			try {
				return Interpreter.instruccionesPosibles[i].parse(line);
			} catch (WrongInstructionFormatException ex){
				//throw ex;
			}
		}
		
		throw new WrongInstructionFormatException(Messages.OUTPUT_INSTRUCTION_NO);
	}
	
	
	/**	
	 * @return String containing help about the Instructions the interpreter knows
	 */
	public static String interpreterHelp (){
		String result = Messages.OUTPUT_INSTRUCTIONS_VALID;
		for (int i = 0; i < Interpreter.instruccionesPosibles.length; i++) 
			result += LINE_SEPARATOR + HELPTAB + Interpreter.instruccionesPosibles[i].getHelp();
		result += LINE_SEPARATOR;
		//line separator y "WALL·E" sobran, están  para el validador
		
		return result;
	}
	

	
	//constantes de comandos para poder cambiarlas luego a otros idiomas
	
	
	//line separator
	public static final String LINE_SEPARATOR = System.getProperty("line.separator");
	//constantes de la ayuda
	private static final String HELPTAB = "     ";

	private static final Instruction[] instruccionesPosibles = {new MoveInstruction(), new TurnInstruction(),
		new PickInstruction(), new ScanInstruction(), new OperateInstruction(), new RadarInstruction(),
		new DropInstruction(), new HelpInstruction(), new QuitInstruction(), new UndoInstruction(), new RedoInstruction()};
	
	/**
	 * 	MOVE
		TURN { LEFT | RIGHT }
		PICK <ITEM>
		SCAN [ <ITEM> ]
		OPERATE <ITEM>
		RADAR
		DROP <ITEM>
		HELP
		QUIT
	 */
}
