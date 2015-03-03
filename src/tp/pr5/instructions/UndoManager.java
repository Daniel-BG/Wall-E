package tp.pr5.instructions;

import tp.pr5.instructions.exceptions.InstructionExecutionException;
import tp.pr5.util.DoubleLinkedStack;


public class UndoManager {
	private DoubleLinkedStack<Instruction> list = new DoubleLinkedStack<Instruction>();
	
	/**
	 * Adds a new instruction to the undo manager
	 * @param instruction
	 */
	public void addInstruction(Instruction instruction) {
		this.list.add(instruction);
	}
	
	/**
	 * Undoes the last Instruction on the list
	 * @throws InstructionExecutionException if the instruction throws it when undoing
	 */
	public boolean undoLastInstruction() throws InstructionExecutionException {
		if (this.list.isEmpty())
			return false;
		Instruction i = this.list.removeLast();
		i.undo();
		return true;
	}
	
	/**
	 * Executes the last undid instruction on the list
	 * @throws InstructionExecutionException if the instruction throws it when executing
	 */
	public Instruction getLastUndidInstruction() throws IndexOutOfBoundsException{
		return this.list.recoverLast();
	}
	
}
