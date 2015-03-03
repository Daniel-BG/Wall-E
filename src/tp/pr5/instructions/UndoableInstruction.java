package tp.pr5.instructions;

public abstract class UndoableInstruction implements Instruction{

	@Override
	public boolean isUndoable() {
		return true;
	}

}
