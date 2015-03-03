package tp.pr5.instructions;

import tp.pr5.NavigationModule;
import tp.pr5.RobotEngine;
import tp.pr5.instructions.exceptions.InstructionExecutionException;
import tp.pr5.instructions.exceptions.WrongInstructionFormatException;
import tp.pr5.items.Item;
import tp.pr5.items.ItemContainer;
import tp.pr5.lang.CmdDic;
import tp.pr5.lang.Messages;
import tp.pr5.util.InstructionParser;

public class DropInstruction extends UndoableInstruction{
	private String inputId = "";

	
	private NavigationModule navigationModule;
	private RobotEngine robotEngine;
	private ItemContainer robotContainer;
	/**
	 * Constructor to set the id directly, used by the parse method
	 * @param id
	 */
	public DropInstruction(String id) {
		this.inputId = id;
	}
	
	/**
	 * Used by the interpreter
	 */
	public DropInstruction() {}
	
	@Override
	public Instruction parse(String cad) throws WrongInstructionFormatException {
		return new DropInstruction(InstructionParser.getArg(InstructionParser.parseCommand(cad, CmdDic.dropCommand)));
	}

	@Override
	public String getHelp() {
		return CmdDic.dropHelp;
	}

	@Override
	public void configureContext(RobotEngine engine, NavigationModule navigation, ItemContainer robotContainer) {
		this.navigationModule = navigation;
		this.robotContainer = robotContainer;
		this.robotEngine = engine;
	}

	@Override
	public void execute() throws InstructionExecutionException {
		Item it;
		if ((it = this.navigationModule.getCurrentPlace().pickItem(inputId)) != null) {
			this.navigationModule.dropItemAtCurrentPlace(it);
			throw new InstructionExecutionException(Messages.OUTPUT_DROP_PLACEFAIL + inputId); 
		}
		if (this.robotContainer.getItem(inputId) == null) 
			throw new InstructionExecutionException(Messages.OUTPUT_DROP_INVENTORYFAIL + inputId); 
		
		this.navigationModule.dropItemAtCurrentPlace(this.robotContainer.getItem(inputId));
		it = this.robotContainer.pickItem(inputId);
		
		this.robotEngine.saySomething(Messages.OUTPUT_DROP_SUCCESS + it.getId());
	}

	

	@Override
	public void undo() throws InstructionExecutionException {
		this.robotContainer.addItem(this.navigationModule.getCurrentPlace().pickItem(this.inputId));
	}

}
