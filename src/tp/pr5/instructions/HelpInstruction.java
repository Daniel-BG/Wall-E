package tp.pr5.instructions;


import tp.pr5.NavigationModule;
import tp.pr5.RobotEngine;
import tp.pr5.instructions.exceptions.InstructionExecutionException;
import tp.pr5.instructions.exceptions.WrongInstructionFormatException;
import tp.pr5.items.ItemContainer;
import tp.pr5.lang.CmdDic;
import tp.pr5.util.InstructionParser;

public class HelpInstruction extends NotUndoableInstruction {
	private RobotEngine robotEngine;

	@Override
	public Instruction parse(String cad) throws WrongInstructionFormatException {
		InstructionParser.parseCommand(cad, CmdDic.helpCommand);
		return new HelpInstruction();
	}

	@Override
	public String getHelp() {
		return CmdDic.helpHelp;
	}

	@Override
	public void configureContext(RobotEngine engine, NavigationModule navigation, ItemContainer robotContainer) {
		this.robotEngine = engine;
	}

	@Override
	public void execute() throws InstructionExecutionException {
		this.robotEngine.requestHelp();
	}


}
