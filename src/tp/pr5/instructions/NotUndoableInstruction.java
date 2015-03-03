package tp.pr5.instructions;

import tp.pr5.instructions.exceptions.InstructionExecutionException;

public abstract class NotUndoableInstruction implements Instruction{

	@Override
	public void undo() throws InstructionExecutionException {
		throw new InstructionExecutionException("Undoing not possible");
	}

	@Override
	public boolean isUndoable() {
		return false;
	}

}
