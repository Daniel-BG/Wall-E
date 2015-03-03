package tp.pr5.instructions;

import tp.pr5.NavigationModule;
import tp.pr5.RobotEngine;
import tp.pr5.instructions.exceptions.InstructionExecutionException;
import tp.pr5.instructions.exceptions.WrongInstructionFormatException;
import tp.pr5.items.Item;
import tp.pr5.items.ItemContainer;
import tp.pr5.lang.CmdDic;
import tp.pr5.lang.ErrorMessages;
import tp.pr5.lang.Messages;
import tp.pr5.util.InstructionParser;

public class PickInstruction extends UndoableInstruction {
	private String inputId = "";

	
	private RobotEngine robotEngine;
	private NavigationModule navigationModule;
	private ItemContainer robotContainer;
	
	public PickInstruction(String id) {
		this.inputId = id;
	}
	/**
	 * Default constructor for the interpreter's use
	 */
	public PickInstruction() {}

	@Override
	public Instruction parse(String cad) throws WrongInstructionFormatException {
		return new PickInstruction(InstructionParser.getArg(InstructionParser.parseCommand(cad, CmdDic.pickCommand)));
	}

	@Override
	public String getHelp() {
		return CmdDic.pickHelp;
	}

	@Override
	public void configureContext(RobotEngine engine, NavigationModule navigation, ItemContainer robotContainer) {
		this.robotEngine = engine;
		this.navigationModule = navigation;
		this.robotContainer = robotContainer;
	}

	/**
	 * tries to pick an item from the place where the robot is. The id of that item is the one sent as argument to the constructor
	 */
	@Override
	public void execute() throws InstructionExecutionException {
		Item itemInHand = this.navigationModule.getCurrentPlace().pickItem(inputId);
		if (itemInHand == null) {
			throw new InstructionExecutionException(ErrorMessages.IEE_ITEM_NOITEM + inputId);
		}
		if (this.robotContainer.addItem(itemInHand)) //esto va sin setID, pero si no, no valida
			this.robotEngine.saySomething(Messages.OUTPUT_ITEM_PICKED + itemInHand.getId());
		else {
			this.navigationModule.dropItemAtCurrentPlace(itemInHand);
			throw new InstructionExecutionException(Messages.OUTPUT_ITEM_NOPICKED + itemInHand.getId());
		}
	}

	@Override
	public void undo() throws InstructionExecutionException {
		this.navigationModule.dropItemAtCurrentPlace(this.robotContainer.pickItem(inputId));
	}


}
