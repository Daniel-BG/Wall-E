package tp.pr5.instructions;

import tp.pr5.NavigationModule;
import tp.pr5.RobotEngine;
import tp.pr5.instructions.exceptions.InstructionExecutionException;
import tp.pr5.instructions.exceptions.WrongInstructionFormatException;
import tp.pr5.items.ItemContainer;
import tp.pr5.lang.CmdDic;
import tp.pr5.lang.Messages;
import tp.pr5.util.InstructionParser;

public class QuitInstruction extends NotUndoableInstruction{
	private RobotEngine robotEngine;

	
	@Override
	public Instruction parse(String cad) throws WrongInstructionFormatException {
		InstructionParser.parseCommand(cad, CmdDic.quitCommand);
		return new QuitInstruction();
	}

	@Override
	public String getHelp() {
		return CmdDic.quitHelp;
	}

	@Override
	public void configureContext(RobotEngine engine, NavigationModule navigation, ItemContainer robotContainer) {
		robotEngine = engine;
	}

	@Override
	public void execute() throws InstructionExecutionException {
		this.robotEngine.saySomething(Messages.OUTPUT_QUIT);
		this.robotEngine.requestQuit();
	}


}
