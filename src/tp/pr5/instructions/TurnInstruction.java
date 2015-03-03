package tp.pr5.instructions;

import tp.pr5.NavigationModule;
import tp.pr5.RobotEngine;
import tp.pr5.Rotation;
import tp.pr5.instructions.exceptions.InstructionExecutionException;
import tp.pr5.instructions.exceptions.WrongInstructionFormatException;
import tp.pr5.items.ItemContainer;
import tp.pr5.lang.CmdDic;
import tp.pr5.lang.ErrorMessages;
import tp.pr5.lang.Messages;
import tp.pr5.util.InstructionParser;

public class TurnInstruction extends UndoableInstruction {
	public static final int FUEL_DECREMENT_TURN = -5;
	private Rotation rotation = Rotation.UNKNOWN;

	private NavigationModule navigationModule;
	private RobotEngine robotEngine;
	
	/**
	 * Constructor with rotation
	 * @param rotation: The caller of this method must make sure that rotation != UNKNOWN, otherwise instruction behaviour is unpredictable
	 */
	public TurnInstruction(Rotation rotation) {
		this.rotation = rotation;
	}
	/**
	 * Default constructor for the interpreter's use
	 */
	public TurnInstruction() {}

	@Override
	public Instruction parse(String cad) throws WrongInstructionFormatException {
		//esto debería hacerse con un nuevo diccionario de instrucciones para turn (left y right como subcomandos)
		//pero dado que es fácil cambiarlo y no se va a hacer, lo dejamos así
		String inputId = InstructionParser.getArg(InstructionParser.parseCommand(cad, CmdDic.turnCommand)).toUpperCase();
		switch (inputId) {
			case CmdDic.turnLeftCommand: return new TurnInstruction(Rotation.LEFT);
			case CmdDic.turnRightCommand: return new TurnInstruction(Rotation.RIGHT);	
			default: throw new WrongInstructionFormatException(ErrorMessages.IEE_RUNTIMEERROR);//this.rotation = Rotation.UNKNOWN;
		}
	}

	@Override
	public String getHelp() {
		return CmdDic.turnHelp;
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
		if (this.rotation == Rotation.UNKNOWN)
			throw new InstructionExecutionException(ErrorMessages.IEE_RUNTIMEERROR);
		this.navigationModule.rotate(this.rotation);
		this.robotEngine.sayAndUpdateDataAfterFuelConsumingAction(FUEL_DECREMENT_TURN);
		if (this.robotEngine.getFuel() == 0) 
			this.robotEngine.saySomething(Messages.OUTPUT_FUEL_NOFUEL);
	}

	@Override
	public void undo() throws InstructionExecutionException {
		if (this.rotation == Rotation.UNKNOWN)
			throw new InstructionExecutionException(ErrorMessages.IEE_RUNTIMEERROR);
		this.robotEngine.addFuel(-FUEL_DECREMENT_TURN); //no mostramos mensaje de ningún tipo en principio
		this.navigationModule.rotate(this.rotation.opposite());
		this.robotEngine.unRequestQuit(); //por si había muerto
	}
}
