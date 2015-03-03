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

public class RedoInstruction extends NotUndoableInstruction {
	private RobotEngine robotEngine;
	
	@Override
	public Instruction parse(String cad) throws WrongInstructionFormatException {
		InstructionParser.parseCommand(cad, CmdDic.redoCommand);
		return new RedoInstruction();
	}

	@Override
	public String getHelp() {
		return CmdDic.redoHelp;
	}

	@Override
	public void configureContext(RobotEngine engine, NavigationModule navigation, ItemContainer robotContainer) {
		this.robotEngine = engine;
	}

	@Override
	public void execute() throws InstructionExecutionException {
		if (this.robotEngine.redoInstruction())
			robotEngine.saySomething(Messages.OUTPUT_REDO_SUCCESS);
		else
			throw new InstructionExecutionException(ErrorMessages.IEE_REDO_UNEXISTENT);
	}

}
