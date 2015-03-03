package tp.pr5;

import tp.pr5.instructions.Instruction;
import tp.pr5.instructions.UndoManager;
import tp.pr5.instructions.exceptions.InstructionExecutionException;
import tp.pr5.items.ItemContainer;
import tp.pr5.observers.ItemContainerObserver;
import tp.pr5.observers.NavigationModuleObserver;
import tp.pr5.observers.RobotObserver;
import tp.pr5.util.Observable;

public class RobotEngine extends Observable<RobotObserver>{
	//constantes numéricas
	private static final int FUEL_INITIAL = 100;
	//private static final int FUEL_MAX = 9001;
	private static final int POINTS_INITIAL = 0;
	

	private Integer fuel = 0;
	private Integer points = 0;

	protected ItemContainer pocket;
	protected NavigationModule gps;
	
	protected UndoManager undoManager;
	
	private boolean endSimulation = false;
	
	
	/**
	 * 
	 * @param initialPlace sitio desde donde se parte
	 * @param direction	dirección inicial
	 * @param cityMap mapa de la ciudad
	 */
	public RobotEngine (City cityMap, Place initialPlace, Direction direction) {
		this.fuel = FUEL_INITIAL;
		this.points = POINTS_INITIAL;
		this.pocket = new ItemContainer();
		this.gps = new NavigationModule(cityMap, initialPlace);
		this.undoManager = new UndoManager();
		this.onRobotUpdate();
		
//		this.gps.addListener(new AbstractNavigationModuleListener() {
//			@Override
//			public void onSpaceShipReached() {
//				requestWin();
//			}
//		});
	}

	

	/**
	 * Adds an amount of fuel to the robot (it can be negative)
	 * @param fuel Amount of fuel added to the robot
	 */
	public void addFuel(int fuel) {
		this.fuel += fuel;
		if (this.fuel < 0) 
			this.fuel = 0;
		this.onRobotUpdate();
		
		//if (this.fuel > FUEL_MAX)
		// 	this.fuel = FUEL_MAX;
	}
	/**
	 * Adds an amount of fuel to the robot (it can be negative)
	 * @param weight Amount of fuel added to the robot
	 */
	public void addRecycledMaterial (int weight) {
		if (weight == 0)
			return;
		points += weight;
		this.onRobotUpdate();
	}
	
	/**
	 * for testing
	 * @return current fuel level
	 */
	public int getFuel() {
		return this.fuel;
	}	
	/**
	 * for testing
	 * @return current engine points
	 */
	public int getRecycledMaterial() {
		return this.points;
	}
	/**
	 * Checks whether or not the robot is facing the exit and can go to it
	 * @return true if the next place is the exit, false otherwise
	 */
	public boolean isFacingExit() {
		return (this.gps.getHeadingStreet() != null)
				&& this.gps.getHeadingStreet().isOpen()
				&& this.gps.getHeadingStreet().nextPlace(gps.getCurrentPlace()).isSpaceship();
	}
	
	
	/**
	 * prints all info related to the robot status after a fuel consumption
	 * @param consumedFuel fuel that was consumed
	 */
	public void sayAndUpdateDataAfterFuelConsumingAction(int consumedFuel) {
		this.addFuel(consumedFuel);
	}	
	
	/**
	 * It executes an instruction. The instruction must be configured with the context before executing it. It controls the end of the simulation. If the execution of the instruction throws an exception, then the corresponding message is printed <br>
	 * It also adds the instruction to the undo manager (if the execution was correct), meaning it will be the next undid if the undo instruction is executed, overwriting any previous.
	 * @param c The instruction to be executed
	 */
	public void communicateRobot(Instruction c) {
		c.configureContext(this, this.gps, this.pocket);
		if (this.executeInstruction(c) && c.isUndoable()) 
			this.undoManager.addInstruction(c);
		if (this.fuel == 0 || this.gps.atSpaceship()) //si hemos fundido al robot, llamamos a engineOff
			this.onEngineOff();
	}
	/**
	 * Executes an instruction
	 * @param c instruction to be executed
	 * @return true if the instruction did not fail, false otherwise
	 */
	public boolean executeInstruction(Instruction c) {
		try {
			c.execute();
			return true;
		} catch (InstructionExecutionException ex) {
			this.onErrorRaised(ex.getMessage());
			return false;
		}
	}
	/**
	 * Undoes the last executed instruction, or does nothing if there was no such instruction
	 */
	public boolean undoInstruction() {
		try {
			return this.undoManager.undoLastInstruction();
		} catch (InstructionExecutionException e) {
			return false;
		}
	}
	/**
	 * Redoes the last executed instruction, or does nothing if there was no such instruction
	 */
	public boolean redoInstruction() {
		try {
			this.executeInstruction(this.undoManager.getLastUndidInstruction());
			return true;
		} catch (IndexOutOfBoundsException e) {
			return false;
		}
	}

	/**
	 * Requests the end of the simulation
	 */
	public void requestQuit() {
		this.endSimulation = true;
		this.onCommunicationCompleted();
	}
	/**
	 * Unrequests quit for the autoplayer stuff
	 */
	public void unRequestQuit() {
		this.endSimulation = false;
	}
	/**
	 * Prints the information about all possible instructions
	 */
	public void requestHelp() {
		this.onCommunicationHelp();
	}
	/**
	 * Requests the engine to inform that an error has been raised
	 * @param message The error message
	 */
	public void requestError(String message) {
		this.onErrorRaised(message);
	}
	/**
	 * Requests the engine to inform the observers that the simulation starts
	 */
	public void requestStart() {
		this.gps.requestStart();
		this.onRobotUpdate();
	}
	/**
	 * Request the engine to say something 
	 * @param message the message
	 */
	public void saySomething(String message) {
		this.onRobotSays(message);
	}
	
	
	/**
	 * Checks if the simulation is finished
	 * @return true if the game has finished
	 */
	public boolean isOver() {
		return this.endSimulation;
	}
	/**
	 * Returns true if the robot has any fuel left
	 * @return 
	 */
	public boolean hasFuel() {
		return this.fuel != 0;
	}	
	
	/**
	 * It starts the robot engine. 
	 * Basically, it reads a line, the interpreter generates the corresponding instruction and executes it. 
	 * The simulation finishes when the robot arrives at the space ship, the user types "QUIT", or the robot runs out of fuel
	 * @param hasInput true if the robot needs to wait for console input, false if it receives it otherwise
	 */
	public void startEngine(){}

	/**
	 * Called when the communication is completed
	 */
	protected void onCommunicationCompleted() {
		for (int i = 0; i < this.observers.size(); i++)
			this.observers.get(i).communicationCompleted();
	}
	/**
	 * Called when the robot is communicating help
	 */
	private void onCommunicationHelp() {
		for (int i = 0; i < this.observers.size(); i++)
			this.observers.get(i).communicationHelp("HELP GOES HERE (ROBOTENGINE.java)"); //TODO
	}
	/**
	 * Called when the engine turns off
	 */
	protected void onEngineOff() {
		for (int i = 0; i < this.observers.size(); i++)
			this.observers.get(i).engineOff(this.gps.atSpaceship());
	}
	/**
	 * Called when an error is produced
	 * @param msg error message
	 */
	private void onErrorRaised(String msg) {
		for (int i = 0; i < this.observers.size(); i++)
			this.observers.get(i).raiseError(msg);
	}
	/**
	 * Called when the robot wants to say something
	 * @param msg message to say
	 */
	private void onRobotSays(String msg) {
		for (int i = 0; i < this.observers.size(); i++)
			this.observers.get(i).robotSays(msg);
	}
	/**
	 * Called when an update (fuel or points) occurs	
	 */
	private void onRobotUpdate() {
		for (int i = 0; i < this.observers.size(); i++)
			this.observers.get(i).robotUpdate(getFuel(), getRecycledMaterial());
	}
	
	
	
	/**
	 * Adds an engine observer to this engine
	 * @param r
	 */
	public void addEngineObserver(RobotObserver r) {
		this.addObserver(r);
	}
	/**
	 * Removes an observer from this engine
	 * @param r
	 */
	public void removeEngineObserver(RobotObserver r) {
		this.removeObserver(r);
	}
	/**
	 * Adds an observer to this robot's inventory
	 * @param i
	 */
	public void addItemContainerObserver(ItemContainerObserver i) {
		this.pocket.addObserver(i);
	}
	/**
	 * Removes an observer from this robot's inventory
	 * @param i
	 */
	public void removeItemContainerObserver(ItemContainerObserver i) {
		this.pocket.removeObserver(i);
	}
	/**
	 * Adds a navigation module observer to this robot
	 * @param n
	 */
	public void addNavigationModuleObserver(NavigationModuleObserver n) {
		this.gps.addObserver(n);
	}
	/**
	 * Removes a navigation module observer from this robot's nav mod
	 * @param n
	 */
	public void removeNavigationModuleObserver(NavigationModuleObserver n) {
		this.gps.removeObserver(n);
	}
}
