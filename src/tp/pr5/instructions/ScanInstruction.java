package tp.pr5.instructions;

import tp.pr5.NavigationModule;
import tp.pr5.RobotEngine;
import tp.pr5.instructions.exceptions.InstructionExecutionException;
import tp.pr5.instructions.exceptions.WrongInstructionFormatException;
import tp.pr5.items.ItemContainer;
import tp.pr5.lang.CmdDic;
import tp.pr5.lang.ErrorMessages;
import tp.pr5.util.InstructionParser;

public class ScanInstruction extends NotUndoableInstruction{
	private String inputId = "";
	private boolean isAll = false; //true if scan has no arguments
	
	
	private ItemContainer robotContainer;
	
	/**
	 * Constructor for ScanInstruction in case it received an argument
	 * @param id
	 * @param isAll
	 */
	public ScanInstruction(String id, boolean isAll) {
		this.inputId = id;
		this.isAll = isAll;
	}
	/**
	 * Constructor in case it didn't receive an argument
	 * @param isAll
	 */
	public ScanInstruction(boolean isAll) {
		this.inputId = "";
		this.isAll = isAll;
	}
	/**
	 * Default constructor for the interpreter's use
	 */
	public ScanInstruction() {}

	@Override
	public Instruction parse(String cad) throws WrongInstructionFormatException {
		String arg = InstructionParser.parseCommand(cad, CmdDic.scanCommand);
		try {
			return new ScanInstruction(InstructionParser.getArg(arg), false);
		} catch (WrongInstructionFormatException ex) {
			return new ScanInstruction(true);
		}
	}

	@Override
	public String getHelp() {
		return CmdDic.scanHelp;
	}

	@Override
	public void configureContext(RobotEngine engine, NavigationModule navigation, ItemContainer robotContainer) {
		this.robotContainer = robotContainer;
	}

	/**
	 * scans an item from the pocket or the whole pocket if the instruction was constructed without an argument
	 */
	@Override
	public void execute() throws InstructionExecutionException {
		if (this.isAll)  //Caso de escanear todo
			this.robotContainer.requestScanCollection();		
		else { //Caso de escanear uno solo
			if (this.robotContainer.getItem(inputId) == null)
				throw new InstructionExecutionException(ErrorMessages.IEE_SCAN_FAIL);
			else
				this.robotContainer.requestScanItem(inputId);
		}
	}

}
