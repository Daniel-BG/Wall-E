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

public class MoveInstruction extends UndoableInstruction{
	public static final int FUEL_DECREMENT_MOVE = -5;
	
	private NavigationModule navigationModule;
	private RobotEngine robotEngine;
	
	@Override
	public Instruction parse(String cad) throws WrongInstructionFormatException {
		InstructionParser.parseCommand(cad, CmdDic.moveCommand);
		return new MoveInstruction();
	}

	@Override
	public String getHelp() {
		return CmdDic.moveHelp;
	}

	@Override
	public void configureContext(RobotEngine engine, NavigationModule navigation, ItemContainer robotContainer) {
		this.navigationModule = navigation;
		this.robotEngine = engine;
	}

	@Override
	public void execute() throws InstructionExecutionException {
		if (!this.robotEngine.hasFuel())
			throw new InstructionExecutionException(ErrorMessages.IEE_FUEL_NOFUELLEFT);
		
		if (this.navigationModule.getHeadingStreet() != null && this.navigationModule.getHeadingStreet().isOpen())
			this.robotEngine.saySomething(this.navigationModule.getCurrentHeading().moveDirection());
		
		try {
			this.navigationModule.move();
			this.robotEngine.addFuel(FUEL_DECREMENT_MOVE);
		} catch (InstructionExecutionException ex) {
			if (ex.getMessage() == "NoStreet") 
				throw new InstructionExecutionException(Messages.OUTPUT_STREET_NO + this.navigationModule.getCurrentHeading().toString());
			throw new InstructionExecutionException(Messages.OUTPUT_STREET_CLOSED);	
		} 
		

		if (this.navigationModule.getCurrentPlace().isSpaceship())
			this.robotEngine.saySomething(Messages.OUTPUT_ATSPACESHIP);
	}

	
	
	@Override
	public void undo() throws InstructionExecutionException {
		this.navigationModule.flip();
		this.navigationModule.move();
		this.navigationModule.flip();
		this.robotEngine.addFuel(-FUEL_DECREMENT_MOVE);
		this.robotEngine.unRequestQuit(); //por si había muerto
	}
	

	


	
}
