package tp.pr5;

import tp.pr5.instructions.exceptions.InstructionExecutionException;
import tp.pr5.items.CodeCard;
import tp.pr5.items.Item;
import tp.pr5.lang.ErrorMessages;
import tp.pr5.observers.NavigationModuleObserver;
import tp.pr5.util.Observable;

public class NavigationModule extends Observable<NavigationModuleObserver> {
	private City city;
	private Place currentPlace;
	private Direction direction  = Direction.NORTH;
	
	/**
	 * Navigation module constructor. It needs the city map and the initial place
	 * @param aCity A city map
	 * @param initialPlace An initial place for the robot
	 */
	public NavigationModule(City aCity, Place initialPlace) {
		this.city = aCity;
		this.currentPlace = initialPlace;
		this.onInitNavigationModule();
	}
	/**
	 * Initializes the current heading according to the parameter 
	 * @param heading New direction for the robot
	 */
	public void initHeading(Direction heading) {
		this.direction = heading; 
	}
	
	
	/**
	 * Updates the current direction of the robot according to the rotation
	 * @param rotation left or right
	 */
	public void rotate(Rotation rotation) {
		if (rotation == Rotation.UNKNOWN)
			return;
		
		if (rotation == Rotation.LEFT) 
			this.direction = this.direction.leftTurn();
		else 
			this.direction = this.direction.rightTurn();

		this.onHeadingChanged(); 
	}
	/**
	 * Flips the current direction of the robot.
	 */
	public void flip() {
		this.direction = this.direction.opposite();
		this.onHeadingChanged();
	}
	/**
	 * The method tries to move the robot following the current direction.
	 * If the movement is not possible because there is no street, or there is a street which is closed, then it throws an exception.
	 * Otherwise the current place is updated according to the movement.
	 * @throws InstructionExecutionException An exception with a message about the encountered problem
	 */
	public void move() throws InstructionExecutionException {
		Street currentStreet = this.getHeadingStreet();
		if (currentStreet == null) {
			this.onRobotCrashed(NavigationModuleObserver.ON_NOSTREET_CRASH);
			throw new InstructionExecutionException(ErrorMessages.IEE_STREET_NO); //No calle
		}
		if (!currentStreet.isOpen()) {
			this.onRobotCrashed(NavigationModuleObserver.ON_CLOSEDSTREET_CRASH);
			throw new InstructionExecutionException(ErrorMessages.IEE_STREET_CLOSED); //No abierta
		}
		
		Place newPlace = this.getHeadingStreet().nextPlace(this.currentPlace);
		
		this.currentPlace = newPlace;
	
		this.onRobotArrivesAtPlace();
		this.onPlaceHasChanged();
	}
	/**
	 * Toggles the state of the current heading street
	 * @param codeCard that will try to toggle it
	 * @return true if the item was successfully used, false if not (the street didn't exist or the door was closed
	 */
	public boolean toggleHeadingStreet(CodeCard codeCard) {
		if (this.getHeadingStreet() == null)
			return false;
		boolean ret = this.getHeadingStreet().toggle(codeCard);
		if (ret)
			this.onDoorToggled(this.getHeadingStreet().isOpen());
		return ret;
	}
	
	
	/**
	 * Tries to pick an item characterized by a given identifier from the current place.
	 * If the action was completed the item is removed from the current place.
	 * @param id The identifier of the item
	 * @return The item of identifier id if it exists in the place. Otherwise the method returns null
	 */
	public Item pickItemFromCurrentPlace(String id) {
		Item it = this.currentPlace.pickItem(id);
		if (it != null)
			this.onPlaceHasChanged();
		return it;
	}
	/**
	 * Drop an item in the current place. It assumes that there is no other item with the same name/id there. Otherwise, the behaviour is undefined.
	 * @param it  The name of the item to be dropped.
	 */
	public void dropItemAtCurrentPlace(Item it) {
		this.currentPlace.addItem(it);
		this.onPlaceHasChanged();
	}
	/**
	 * Checks if there is an item with a given id in this place
	 * @param id Identifier of the item we are looking for
	 * @return true if and only if an item with this id is in the current place
	 */
	public boolean findItemAtCurrentPlace(String id) {
		Item item;
		boolean picked = ((item = this.currentPlace.pickItem(id)) != null);
		if (picked) {
			this.currentPlace.addItem(item);
			return true;
		}
		return false;
		
	}
	/**
	 * Prints the information (description + inventory) of the current place
	 */
	public void scanCurrentPlace() {
		this.onPlaceScanned();
	}
	
	
	/**
	 * Returns the street opposite the robot
	 * @return The street which the robot is facing, or null, if there is not any street in this direction
	 */
	public Street getHeadingStreet() {
		return city.lookForStreet(this.currentPlace, this.direction);
	}
	/**
	 * Returns the street to the left of the robot
	 * @return
	 */
	public Street getLeftStreet() {
		return city.lookForStreet(this.currentPlace, this.direction.leftTurn());
	}
	/**
	 * Returns the street to the right of the robot
	 * @return
	 */
	public Street getRightStreet() {
		return city.lookForStreet(this.currentPlace, this.direction.opposite());
	}
	/**
	 * Returns the street to the back of the robot
	 * @return
	 */
	public Street getBackStreet() {
		return city.lookForStreet(this.currentPlace, this.direction.rightTurn());
	}
	/**
	 * Returns the robot heading
	 * @return The direction where the robot is facing to.
	 */
	public Direction getCurrentHeading() {
		return this.direction;
	}
	/**
	 * Returns the current place where the robot is (for testing purposes)
	 * @return The current place
	 */
	public Place getCurrentPlace() {
		return this.currentPlace;
	}
	/**
	 * Checks if the robot has arrived at a spaceship
	 * @return true if an only if the current place is the spaceship
	 */
	public boolean atSpaceship() {
		return this.currentPlace.isSpaceship();
	}
	

	/**
	 * Called when the heading changes
	 */
	private void onHeadingChanged() {
		for (int i = 0; i < this.observers.size(); i++)
			this.observers.get(i).headingChanged(getCurrentHeading());
	}
	/**
	 * Called when initializing the module
	 */
	private void onInitNavigationModule() {
		for (int i = 0; i < this.observers.size(); i++)
			this.observers.get(i).initNavigationModule(currentPlace, getCurrentHeading());
	}
	/**
	 * Called when the current place changes
	 */
	private void onPlaceHasChanged() {
		for (int i = 0; i < this.observers.size(); i++)
			this.observers.get(i).placeHasChanged(currentPlace);
	}
	/**
	 * Called when the current place is scanned
	 */
	private void onPlaceScanned() {
		for (int i = 0; i < this.observers.size(); i++)
			this.observers.get(i).placeScanned(currentPlace);
	}
	/**
	 * Called when the robot arrives somewhere
	 */
	private void onRobotArrivesAtPlace() {
		for (int i = 0; i < this.observers.size(); i++)
			this.observers.get(i).robotArrivesAtPlace(getCurrentHeading(), getCurrentPlace());
	}
	/**
	 * Called when the robot crashes
	 * @param status
	 */
	private void onRobotCrashed(int status) {
		for (int i = 0; i < this.observers.size(); i++)
			this.observers.get(i).robotCrashes(this.direction, status);
	}
	/**
	 * Called when the robot toggles a door
	 * @param wasClosed If the door was closed
	 */
	private void onDoorToggled(boolean isOpen) {
		for (int i = 0; i < this.observers.size(); i++)
			this.observers.get(i).doorToggled(direction, isOpen);
		}
	
	
	/**
	 * Called when starting
	 */
	public void requestStart() {
		this.onInitNavigationModule();
	}
}
