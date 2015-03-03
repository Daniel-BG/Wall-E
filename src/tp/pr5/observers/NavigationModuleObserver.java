package tp.pr5.observers;

import tp.pr5.Direction;
import tp.pr5.PlaceInfo;

public interface NavigationModuleObserver {
	public static int ON_NOSTREET_CRASH = 1;
	public static int ON_CLOSEDSTREET_CRASH = 2;
	/**
	 * Notifies that the robot heading has changed
	 * @param newDirection
	 */
	public void headingChanged(Direction newDirection);
	
	/**
	 * Notifies that the navigation module has been initialized
	 * @param Place
	 * @param heading
	 */
	public void initNavigationModule(PlaceInfo place, Direction heading);
	
	/**
	 * Notifies that the place where the robot stays has changed (because the robot picked or dropped an item)
	 * @param place
	 */
	public void placeHasChanged(PlaceInfo place);
	
	/**
	 * Notifies that the user requested a RADAR instruction
	 * @param place
	 */
	public void placeScanned(PlaceInfo place);
	
	/**
	 * Notifies that the robot has arrived at a place
	 * @param heading
	 * @param place
	 */
	public void robotArrivesAtPlace(Direction heading, PlaceInfo place);
	/**
	 * Notifies that the robot crashed against something
	 * @param status
	 */
	public void robotCrashes(Direction heading, int status);
	/**
	 * Notifies when a door is toggled
	 * @param dir Direction the robot is facing
	 * @param wasClosed If the door is now open
	 */
	public void doorToggled(Direction dir, boolean isOpen);
	
}
