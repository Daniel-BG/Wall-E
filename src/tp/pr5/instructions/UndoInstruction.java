package tp.pr5.instructions;

import tp.pr5.NavigationModule;
import tp.pr5.RobotEngine;
import tp.pr5.instructions.exceptions.InstructionExecutionException;
import tp.pr5.instructions.exceptions.WrongInstructionFormatException;
import tp.pr5.items.ItemContainer;
import tp.pr5.lang.CmdDic;
import tp.pr5.lang.ErrorMessages;
import tp.pr5.lang.Messages;
import tp.pr5.util.InstructionParser;

public class UndoInstruction extends NotUndoableInstruction {
	private RobotEngine robotEngine;
	
	@Override
	public Instruction parse(String cad) throws WrongInstructionFormatException {
		InstructionParser.parseCommand(cad, CmdDic.undoCommand);
		return new UndoInstruction();
	}

	@Override
	public String getHelp() {
		return CmdDic.undoHelp;
	}

	@Override
	public void configureContext(RobotEngine engine, NavigationModule navigation, ItemContainer robotContainer) {
		this.robotEngine = engine;
	}

	@Override
	public void execute() throws InstructionExecutionException {
		if (this.robotEngine.undoInstruction()) 
			robotEngine.saySomething(Messages.OUTPUT_UNDO_SUCCESS);
		else
			throw new InstructionExecutionException(ErrorMessages.IEE_UNDO_UNEXISTENT);
	}



}
