package tp.pr5.console;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;

import tp.pr5.Controller;
import tp.pr5.Direction;
import tp.pr5.PlaceInfo;
import tp.pr5.items.Item;
import tp.pr5.lang.Messages;
import tp.pr5.observers.ItemContainerObserver;
import tp.pr5.observers.NavigationModuleObserver;
import tp.pr5.observers.RobotObserver;

public class Console implements RobotObserver, NavigationModuleObserver, ItemContainerObserver {
	private Controller controller;
	private InputStream normalInput;
	private PrintStream normalOut, debugOut;
	private boolean endInput = false;
	private int trimSize = TRIM_NONE;
	
	public static final int TRIM_NONE = -1;
	public static final int TRIM_ALL = 0;
	
	/**
	 * Constructor for console output/input
	 * @param c The controller where input will be sent
	 * @param out PrintStream where to print default output, null if no output is wanted
	 * @param debug PrintStream where to print debug output, null if no debug is wanted
	 * @param input InputStream from where to take input, null if no input is wanted
	 */
	public Console(Controller c, PrintStream out, PrintStream debug, InputStream input) {
		if (out == null && debug == null && input == null)
			throw new IllegalArgumentException("You need some streams to work with!");
		c.addEngineObserver(this);
		c.addItemContainerObserver(this);
		c.addNavigationModuleObserver(this);
		this.normalOut = out;
		this.debugOut = debug;
		this.normalInput = input;
		this.controller = c;
		if (this.normalInput != null)
			this.initInput();
	}
	/**
	 * Initializes the input for this console. <br>
	 * Method will only be called if isInput is true in the constructor.
	 */
	public void initInput() {
		new ConsoleListener(this.normalInput) {
			@Override
			public void lineReceived(String str) {
				debug("CONSOLE: Sending: " + trim(str));
				controller.communicateInstruction(str);
			}
		};
	}

	
	//MÉTODOS DE ROBOT OBSERVER
	@Override
	public void communicationCompleted() {
		this.debug("R: Comm. End");
		System.exit(0);
	}
	@Override
	public void communicationHelp(String help) {
		this.debug("R: Help");
	}
	@Override
	public void engineOff(boolean atSpaceShip) {
		this.endInput = true;
		this.debug("R: Engine off. Spaceship: " + atSpaceShip);
	}
	@Override
	public void raiseError(String message) {
		this.debug("R: Error: " + this.trim(message));
		this.robotSays(message);
	}
	@Override
	public void robotSays(String message) {
		this.debug("R: Says: " + this.trim(message));		
		this.output(Messages.ROBOT_NAME + Messages.OUTPUT_SAYS + message);
	}
	@Override
	public void robotUpdate(int newFuel, int newPoints) {
		this.debug("R: Update: F=" + newFuel + ", P=" + newPoints);
		this.output(Messages.OUTPUT_SAYFUEL + newFuel + "\n" + Messages.OUTPUT_SAYRM + newPoints);
	}

	
	//MÉTODOS DE NAV OBSERVER
	@Override
	public void headingChanged(Direction newDirection) {
		this.debug("N: New Dir: " + newDirection);
		this.output(Messages.DIRECTION_LOOKINGAT + newDirection);
	}
	@Override
	public void initNavigationModule(PlaceInfo place, Direction heading) {
		this.debug("N: Init: " + place.getName() + ", " + heading);
		this.output(place.toString());
		this.output(Messages.DIRECTION_LOOKINGAT + heading);
	}
	@Override
	public void placeHasChanged(PlaceInfo place) {
		this.debug("N: Changed: " + place.getName());
	}
	@Override
	public void placeScanned(PlaceInfo place) {
		this.debug("N: Scanned: " + place.getName());
		this.output(place.toString());
	}
	@Override
	public void robotArrivesAtPlace(Direction heading, PlaceInfo place) {
		this.debug("N: @ " + place.getName() + " from " + heading);
		this.output(place.toString());		
	}
	@Override
	public void robotCrashes(Direction heading, int status) {
		this.debug("N: Crashed " + heading + "(" + status + ")");
	}
	@Override
	public void doorToggled(Direction dir, boolean isOpen) {
		this.debug("N: Toggled " + dir + " (" + isOpen + ")");
	}
	
	//MÉTODOS DE INVENTORY OBSERVER
	@Override
	public void inventoryChange(List<Item> inventory) {
		this.debug("I: Change");
	}
	@Override
	public void inventoryScanned(String inventoryDescription) {
		this.debug("I: Scanned");
		this.output(inventoryDescription);
	}
	@Override
	public void itemEmpty(String itemName) {
		this.debug("I: Empty: " + itemName);
	}
	@Override
	public void itemScanned(String description) {
		this.debug("I: Scanned " + this.trim(description));
		this.output(description);
	}
	@Override
	public void itemAdded(String itemName) {
		this.debug("I: Added " + itemName);
	}
	@Override
	public void itemRemoved(String itemName) {
		this.debug("I: Removed " + itemName);
	}


	//MÉTODOS AUXILIARES:
	
	/**
	 * Cuts the first chars of the string and returns those, or returns the whole string if it is less than trimSize chars
	 * @param input
	 * @return the resulting string
	 */
	private String trim (String input) {
		return Console.trim(input, this.trimSize);
	}
	/**
	 * Trims the specified string to the specified lenght
	 * @param input
	 * @param trimSize
	 */
	public static String trim (String input, int trimSize) {
		if (trimSize == TRIM_NONE)
			return input;
		if (trimSize == TRIM_ALL)
			return "";
		if (input == null || input.length() <= trimSize)
			return input;
		return trimSize > 3 ? input.substring(0,trimSize-3) + "..." : "...";
	}
	
	/**
	 * Sends a message to the debug output
	 * @param message
	 */
	private void debug(String message) {
		if (this.debugOut != null)
			this.debugOut.println(message);
	}
	/**
	 * Sends a message to the normal output
	 * @param message
	 */
	private void output(String message) {
		if (this.normalOut != null)
			this.normalOut.println(message);
	}
	/**
	 * Sets the trim output for debug mode. Strings no longer than "trimSize" characteres will be sent. <br>
	 * This only applies to the received Strings via observer methods.
	 * @param trimSize The new trim size
	 */
	public void setTrimSize(int trimSize) {
		if (trimSize < -1)
			throw new IllegalArgumentException("Cannot trim negative sizes!");
		this.trimSize = trimSize;
	}
	
	//COSAS AUXILIARES EXTRAS
	/**
	 * 
	 * Listener for input strings to avoid clogging the main thread
	 *
	 */
	private abstract class ConsoleListener {
		/**
		 * Default constructor
		 */
		public ConsoleListener(final InputStream console) {
			try {
				//Launch a scanner in a new thread
				new Thread() {
					public void run() {
						java.util.Scanner sc = new java.util.Scanner(console);
						while (!endInput) {
							System.out.print(Messages.PROMPT);
							//call line received when nextLine sends an input
							lineReceived(sc.nextLine());
						}		
						sc.close();
					}
				}.start();
			} catch (Throwable e) {
				
			}
		}
		public abstract void lineReceived(String str);
	}

}
