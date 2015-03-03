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

public class OperateInstruction extends UndoableInstruction {
	private String inputId = "";

	
	private RobotEngine robotEngine;
	private NavigationModule navigationModule;
	private ItemContainer robotContainer;
	
	private Item operatedItem = null;
	
	public OperateInstruction(String id) {
		this.inputId = id;	
	}
	/**
	 * Default constructor for the interpreter's use
	 */
	public OperateInstruction() {}

	@Override
	public Instruction parse(String cad) throws WrongInstructionFormatException {
		return new OperateInstruction(InstructionParser.getArg(InstructionParser.parseCommand(cad, CmdDic.operateCommand)));
	}

	@Override
	public String getHelp() {
		return CmdDic.operateHelp;
	}

	@Override
	public void configureContext(RobotEngine engine, NavigationModule navigation, ItemContainer robotContainer) {
		this.robotEngine = engine;
		this.navigationModule = navigation;
		this.robotContainer = robotContainer;
	}

	/**
	 * Uses an item from the pocket, which ID is defined by the argument of the instruction
	 */
	@Override
	public void execute() throws InstructionExecutionException {
		this.operatedItem = this.robotContainer.getItem(inputId);
		if (this.operatedItem == null)
			throw new InstructionExecutionException(Messages.OUTPUT_OPERATE_NOITEM + inputId); 

		//de ser usado, el objeto dirá qué ha hecho. Si no, saltará el fallo y lo mostrará el robot.
		if (!this.operatedItem.use(this.robotEngine, this.navigationModule))
			throw new InstructionExecutionException(Messages.OUTPUT_OPERATE_FAIL + inputId);
		
		//si ya no puede ser usado lo quitamos
		if (!this.operatedItem.canBeUsed())  {
			this.robotContainer.pickItem(inputId);
			this.robotEngine.saySomething(Messages.OUTPUT_OPERATE_USED.replace("<id>", inputId));	
		}
			
		this.robotContainer.useItem(this.operatedItem);
	}

	@Override
	public void undo() throws InstructionExecutionException {
		this.operatedItem.unUse(this.robotEngine,this.navigationModule);
		this.robotContainer.addItem(this.operatedItem); //si no se había gastado ya estaba allí y no se añade
	}


}
