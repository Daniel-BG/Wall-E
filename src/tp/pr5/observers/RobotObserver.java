package tp.pr5.observers;

public interface RobotObserver {
	/**
	 * The robot engine informs that the communication is over.
	 */
	public void communicationCompleted();
	
	/**
	 * The robot engine informs that the help has been requested
	 * @param help the help
	 */
	public void communicationHelp(String help);
	
	/**
	 * The robot engine informs that the robot has shut down (because it has arrived at the spaceship or it has run out of fuel)
	 * @param atSpaceShip if it was at the spaceShip
	 */
	public void engineOff(boolean atSpaceShip);
	
	/**
	 * The robot engine informs that it has raised an error
	 * @param message
	 */
	public void raiseError(String message);
	
	/**
	 * The robot engine informs that the robot wants to say something
	 * @param message
	 */
	public void robotSays(String message);
	
	/**
	 * The robot engine informs that the fuel and/or the amount of recycled material has changed
	 * @param newFuel
	 * @param newPoints
	 */
	public void robotUpdate(int newFuel, int newPoints);
	
	
	
}
