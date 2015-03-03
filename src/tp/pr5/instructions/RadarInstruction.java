package tp.pr5.instructions;

import tp.pr5.NavigationModule;
import tp.pr5.RobotEngine;
import tp.pr5.instructions.exceptions.InstructionExecutionException;
import tp.pr5.instructions.exceptions.WrongInstructionFormatException;
import tp.pr5.items.ItemContainer;
import tp.pr5.lang.CmdDic;
import tp.pr5.util.InstructionParser;

public class RadarInstruction extends NotUndoableInstruction {	
	private NavigationModule navigationModule;


	
	@Override
	public Instruction parse(String cad) throws WrongInstructionFormatException {
		InstructionParser.parseCommand(cad, CmdDic.radarCommand);
		return new RadarInstruction();
	}

	@Override
	public String getHelp() {
		return CmdDic.radarHelp;
	}

	@Override
	public void configureContext(RobotEngine engine, NavigationModule navigation, ItemContainer robotContainer) {
		this.navigationModule = navigation;
	}

	@Override
	public void execute() throws InstructionExecutionException {
		this.navigationModule.scanCurrentPlace();
	}
	
}
