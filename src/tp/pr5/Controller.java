package tp.pr5;

import tp.pr5.instructions.DropInstruction;
import tp.pr5.instructions.HelpInstruction;
import tp.pr5.instructions.MoveInstruction;
import tp.pr5.instructions.OperateInstruction;
import tp.pr5.instructions.PickInstruction;
import tp.pr5.instructions.QuitInstruction;
import tp.pr5.instructions.RadarInstruction;
import tp.pr5.instructions.RedoInstruction;
import tp.pr5.instructions.ScanInstruction;
import tp.pr5.instructions.TurnInstruction;
import tp.pr5.instructions.UndoInstruction;
import tp.pr5.instructions.exceptions.WrongInstructionFormatException;
import tp.pr5.observers.ItemContainerObserver;
import tp.pr5.observers.NavigationModuleObserver;
import tp.pr5.observers.RobotObserver;

public class Controller {
	private RobotEngine robot;
	public Controller(RobotEngine r) {
		this.robot = r;
	}
	
	public void communicateDropInstruction(String id) {
		this.robot.communicateRobot(new DropInstruction(id));
	}
	
	public void communicateHelpInstruction() {
		this.robot.communicateRobot(new HelpInstruction());
	}
	
	public void communicateMoveInstruction() {
		this.robot.communicateRobot(new MoveInstruction());
	}
	
	public void communicateOperateInstruction(String id) {
		this.robot.communicateRobot(new OperateInstruction(id));
	}
	
	public void communicatePickInstruction(String id) {
		this.robot.communicateRobot(new PickInstruction(id));
	}

	public void communicateQuitInstruction() {
		this.robot.communicateRobot(new QuitInstruction());
	}
	
	public void communicateRadarInstruction() {
		this.robot.communicateRobot(new RadarInstruction());
	}
	
	public void communicateRedoInstruction() {
		this.robot.communicateRobot(new RedoInstruction());
	}
	
	public void communicateScanInstruction() {
		this.robot.communicateRobot(new ScanInstruction());
	}
	
	public void communicateTurnInstruction(Rotation rot) {
		this.robot.communicateRobot(new TurnInstruction(rot));
	}
	
	public void communicateUndoInstruction() {
		this.robot.communicateRobot(new UndoInstruction());
	}

	public void communicateInstruction(String toParse) {
		try {
			this.robot.communicateRobot(Interpreter.generateInstruction(toParse));
		} catch (WrongInstructionFormatException ex) {
			this.robot.requestError(ex.getMessage());
		}
	}
	
	public void requestQuit() {
		this.robot.requestQuit();
	}
	
	/**
	 * Adds an engine observer to this engine
	 * @param r
	 */
	public void addEngineObserver(RobotObserver r) {
		this.robot.addObserver(r);
	}
	/**
	 * Removes an observer from this engine
	 * @param r
	 */
	public void removeEngineObserver(RobotObserver r) {
		this.robot.removeObserver(r);
	}
	/**
	 * Adds an observer to this robot's inventory
	 * @param i
	 */
	public void addItemContainerObserver(ItemContainerObserver i) {
		this.robot.addItemContainerObserver(i);
	}
	/**
	 * Removes an observer from this robot's inventory
	 * @param i
	 */
	public void removeItemContainerObserver(ItemContainerObserver i) {
		this.robot.removeItemContainerObserver(i);
	}
	/**
	 * Adds a navigation module observer to this robot
	 * @param n
	 */
	public void addNavigationModuleObserver(NavigationModuleObserver n) {
		this.robot.addNavigationModuleObserver(n);
	}
	/**
	 * Removes a navigation module observer from this robot's nav mod
	 * @param n
	 */
	public void removeNavigationModuleObserver(NavigationModuleObserver n) {
		this.robot.removeNavigationModuleObserver(n);
	}
}
