package tp.pr5.citySolver;

import java.util.ArrayList;
import java.util.Stack;

import tp.pr5.instructions.Instruction;
import tp.pr5.City;
import tp.pr5.Direction;
import tp.pr5.Place;
import tp.pr5.RobotEngine;
import tp.pr5.Rotation;
import tp.pr5.instructions.MoveInstruction;
import tp.pr5.instructions.OperateInstruction;
import tp.pr5.instructions.PickInstruction;
import tp.pr5.instructions.TurnInstruction;
import tp.pr5.instructions.UndoInstruction;
import tp.pr5.instructions.UndoableInstruction;

public class AutoPlayer extends RobotEngine {
	private static final int STATUS_EXECUTED = 0;
	private static final int STATUS_FAILED = 1;
	private enum OperateStatus {
		POINTS_MODIFIED, FUEL_DECREASE, STATUSES_UNTOUCHED, FUEL_INCREASE, DOOR_CLOSED;
	}

	private int maxMoves;
	private int currentSteps;
	private int solutionsFound;
	private int maxDepth;
	
	private static int MIN_FUEL = 5;
	
	private Stack<Instruction> currentList  = new Stack<Instruction>();
	private Stack<Instruction> bestList = null;

	private ArrayList<String> blackItemList = new ArrayList<String>();
	

	
	/**
	 * Constructor for the parent class
	 * @param cityMap
	 * @param initialPlace
	 * @param direction
	 */
	public AutoPlayer(City cityMap, Place initialPlace, Direction direction, int maxDepth) {
		super(cityMap, initialPlace, direction);
		this.maxDepth = maxDepth;
	}
	
	@Override
	protected void onEngineOff() {
		//evitamos así activar los eventos cuando se nos gasta el fuel
	}
	

	@Override
	public void requestStart() {
		super.requestStart();
		this.maxMoves = 0x7fffffff;
		this.currentSteps = 0;
		this.solutionsFound = 0;
		
		this.play(0);
		
		if (this.bestList == null)
			this.saySomething("I couldn't find a solution for that map!");
		else {
			this.saySomething("Solution for the map found. Solving...");
			for (Instruction i: this.bestList) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {}
				this.communicateRobot(i);	
			}
		//this.saySomething	
			this.saySomething(	"\n" +
								"\tSteps taken to complete the map: " + this.bestList.size() + "\n" + 
								"\tAfter " + this.currentSteps + " total instructions performed\n" + 
								"\tAnd " + this.solutionsFound + " solutions found\n");
			super.onEngineOff(); //avisamos de que hemos acabado con lo que sea, bueno o malo
		}
	}

	/**
	 * Function that given a state for the robot tries to get to the end from there
	 */
	private void play(int moves) {
		//check if anything has failed already
		if (moves > this.maxMoves -1 || !this.hasFuel() || moves > this.maxDepth) {
			this.communicateRobot(new UndoInstruction());
			return; //pues requerirá de al menos los mismos movimientos para acabar
		}
		//check if next move wins the game
		if (this.isFacingExit() && this.hasFuel()) {
			this.solutionsFound++;
			if (this.bestList == null || this.bestList.size() > this.currentList.size() + 1) {
				this.copyToBest(); //añade además la moveInstruction necesaria
				this.maxMoves = this.bestList.size();
			}
			this.communicateRobot(new UndoInstruction());
			return;
		}		
		
		
		//try to move only if the robot has fuel
		if (this.hasFuel())  {
			if (this.communicateAndCheck(new MoveInstruction()) == STATUS_EXECUTED)
				play(moves + 1);
		}
		
		//try to use an item only if the robot needs fuel or there is a closed street in front of it
		if (this.getFuel() <= MIN_FUEL || (this.gps.getHeadingStreet() != null && !this.gps.getHeadingStreet().isOpen())) { 
			for (int i = 0; i < this.pocket.numberOfItems(); i++) {
				//POINTS_MODIFIED, FUEL_DECREASE, STATUSES_UNTOUCHED, FUEL_INCREASE, DOOR_CLOSED;
				switch (this.statusAfterUsing(this.pocket.getId(i))) {
					case FUEL_INCREASE: {
						if (this.getFuel() <= MIN_FUEL) {
							this.communicateAndCheck(new OperateInstruction(this.pocket.getId(i)));
							play(moves + 1);
						}
						break;
					}
					case DOOR_CLOSED: break;	//don't wanna close doors
					default: {					//might open a door
						if (this.communicateAndCheck(new OperateInstruction(this.pocket.getId(i))) == STATUS_EXECUTED)
							play(moves + 1);//plays if a door was opened					
					}
				}
			}
		}
		
		//try to pick an item
		for (int i = 0; i < this.gps.getCurrentPlace().getContainer().numberOfItems(); i++) {
			String itemId = this.gps.getCurrentPlace().getContainer().getId(i); //item we are working with
			if (this.blackItemList.contains(itemId)) 
				continue; //if it is on the black list (useless) ignore its picking
			this.communicateAndCheck(new PickInstruction(itemId)); //this instruction never fails
			switch (this.statusAfterUsing(itemId)) { //use that item and see what happens
				case FUEL_DECREASE:		//If the item lowers fuel
				case POINTS_MODIFIED: {	//or modifies points
					this.blackItemList.add(itemId);	//add it to the black list
					this.communicateRobot(new UndoInstruction()); //undo the picking
					continue;
				}
				default: break; 		//otherwise proceed
			}
			play(moves + 1);
		}
		
		//try to turn 
		if (this.hasFuel()) {
			if (this.gps.getLeftStreet() != null) {//para qué girar a la izquierda si no hay calle?
				this.communicateAndCheck(new TurnInstruction(Rotation.LEFT));
				play(moves + 1);
			} else if (this.gps.getRightStreet() != null || this.gps.getBackStreet() != null) { //y a la derecha o hacia abajo?
				this.communicateAndCheck(new TurnInstruction(Rotation.RIGHT));
				play(moves + 1);
			} 
		} 
		
		
		//everything failed. Undo and try to start over from last succesfull point
		this.communicateRobot(new UndoInstruction());
	}
	
	
	private OperateStatus statusAfterUsing(String item) {
		int fuel = this.getFuel(), points = this.getRecycledMaterial();
		boolean isOpen = this.gps.getHeadingStreet() != null && this.gps.getHeadingStreet().isOpen();
		if(this.communicateAndCheck(new OperateInstruction(item)) == STATUS_EXECUTED) {
			int postFuel = this.getFuel(), postPoints = this.getRecycledMaterial();
			boolean postIsOpen = this.gps.getHeadingStreet() != null && this.gps.getHeadingStreet().isOpen();
			this.communicateRobot(new UndoInstruction()); //deshacemos el operate
			if (postFuel < fuel) 
				return OperateStatus.FUEL_DECREASE;
			if (postFuel > fuel)
				return OperateStatus.FUEL_INCREASE;
			if (postPoints != points)
				return OperateStatus.POINTS_MODIFIED;
			if (isOpen && !postIsOpen)
				return OperateStatus.DOOR_CLOSED;
		}
		return OperateStatus.STATUSES_UNTOUCHED;
	}
	
	
	
	/**
	 * Communicates an undoable instruction to the robot returning different values according to the result. 
	 * @param i Instruction to communicate
	 * @return 	AutoPlayer.STATUS_EXECTUED if the instruction executed properly
	 * 			AutoPlayer.STATUS_FAILED if the instruction failed or the robot run out of fuel
	 */
	private int communicateAndCheck(UndoableInstruction i){
		i.configureContext(this, this.gps, this.pocket);
		int ret;
		if (!this.executeInstruction(i)) //if it executes, add to the undo list
			ret = STATUS_FAILED;		
		else {
			this.currentSteps++;
			this.undoManager.addInstruction(i);
			this.currentList.push(i);
			ret = STATUS_EXECUTED;
		}
		return ret;
	}
	
	
	private void copyToBest() {
		//System.out.println((currentList.size()+1) + "size " + this.currentSteps + "steps "); //TODO QUITAR
		this.bestList = new Stack<Instruction>();
		for (Instruction i: this.currentList)
			this.bestList.push(i);
		this.bestList.push(new MoveInstruction());
	}
	
	
	@Override
	public boolean undoInstruction() {
		if (!this.currentList.isEmpty()) //esto ocurre al finalizar la primera llamada a play de todas.
			this.currentList.pop();
		return super.undoInstruction();
	}

}



