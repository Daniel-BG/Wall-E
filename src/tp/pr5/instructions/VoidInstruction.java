package tp.pr5.instructions;

import tp.pr5.NavigationModule;
import tp.pr5.RobotEngine;
import tp.pr5.instructions.exceptions.InstructionExecutionException;
import tp.pr5.instructions.exceptions.WrongInstructionFormatException;
import tp.pr5.items.ItemContainer;

/**
 * This is only used for the "UNDO" method when there is no undo to do
 * 
 */
public class VoidInstruction extends NotUndoableInstruction{

	@Override
	public Instruction parse(String cad) throws WrongInstructionFormatException {
		throw new WrongInstructionFormatException(); //an instruction of this type cannot be parsed
	}

	@Override
	public String getHelp() {
		return null; 
	}

	@Override
	public void configureContext(RobotEngine engine, NavigationModule navigation, ItemContainer robotContainer) {
	}

	@Override
	public void execute() throws InstructionExecutionException {
	}

}
