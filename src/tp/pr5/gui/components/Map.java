package tp.pr5.gui.components;

import java.awt.GridLayout;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTextArea;

import tp.pr5.Controller;
import tp.pr5.Direction;
import tp.pr5.PlaceInfo;
import tp.pr5.items.Item;
import tp.pr5.observers.ItemContainerObserverHelper;
import tp.pr5.observers.NavigationModuleObserverHelper;
import tp.pr5.observers.RobotObserverHelper;

public class Map extends JPanel{
	private static final long serialVersionUID = 1L;

	private PlaceCell currentCell;
	private PlaceCell[][] map;
	private JTextArea target;
	private Controller c;
	int currentWidth = 1;
	int currentHeight = 1;
	int currentDrawnWidth = 0;
	int currentDrawnHeight = 0;
	
	/**
	 * Creates the basic Map (1x1 grid) and adds the corresponding listeners to the robot in order to capture the events
	 * @param r RobotEngine to listen to
	 * @param target Text Field to output stuff
	 */
	public Map(final Controller c, final JTextArea target) {
		this.map = new PlaceCell[currentWidth][currentHeight];
		this.map[0][0] = new PlaceCell(c, target, null,null,null,null);
		this.currentCell = this.map[0][0];
		this.target = target;
		this.c = c;

		c.addNavigationModuleObserver(new NavigationModuleObserverHelper() {
			Direction lastDir;
			@Override
			public void headingChanged(Direction newDirection) {
				lastDir = newDirection;
				currentCell.setDirection(newDirection);
			}

			@Override
			public void initNavigationModule(PlaceInfo place, Direction heading) {
				lastDir = heading;
				currentCell.setOnWalle(true);
				currentCell.setDirection(heading);
				currentCell.setPlace(place);
			}


			@Override
			public void robotArrivesAtPlace(Direction heading, PlaceInfo place) {
				currentCell.setOnWalle(false);
				switch(lastDir) {
				case SOUTH: if (currentCell.down == null)
								growSouth();
							currentCell = currentCell.down;
							currentCell.setPlace(place);
							break;
				case NORTH: if (currentCell.up == null)
								growNorth();
							currentCell = currentCell.up;
							currentCell.setPlace(place);
							break;
				case EAST: if (currentCell.right == null)
								growEast();
							currentCell = currentCell.right;
							currentCell.setPlace(place);
							break;
				case WEST: if (currentCell.left == null)
								growWest();
							currentCell = currentCell.left;
							currentCell.setPlace(place);
							break;
				default:	break;
				}
				currentCell.setOnWalle(true);
				currentCell.setDirection(this.lastDir);
				reDraw();
			}
			
			@Override
			public void robotCrashes(Direction heading, int status) {
				currentCell.setWall(heading, status);
			}

			@Override
			public void doorToggled(Direction dir, boolean isOpen) {
				currentCell.switchDoor(dir, isOpen);
			}

		});

		
		c.addEngineObserver(new RobotObserverHelper() {
			@Override
			public void raiseError(String message) {
				currentCell.setInstructionFail();
			}
		});
		
		c.addItemContainerObserver(new ItemContainerObserverHelper() {

			@Override
			public void inventoryChange(List<Item> inventory) {
				currentCell.repaint();
			}
			@Override
			public void itemEmpty(String itemName) {
				currentCell.repaint();
			}			
		});
		
		this.reDraw();
	}
	
	/**
	 * Gets the cell in the grid corresponding to the (x,y) position
	 * @param x
	 * @param y
	 * @return The cell or null if x or y were out of bounds
	 */
	private PlaceCell getCell(int x, int y) {
		if (x >= this.currentWidth || y >= this.currentHeight || x < 0 || y < 0)
			return null;
		return this.map[x][y];
	}
	/**
	 * Links all the cells on the array so they point to each other
	 */
	private void linkAllCells() {
		for (int i = 0; i < this.currentWidth; i++)
			for (int j = 0; j < this.currentHeight; j++) {
				PlaceCell pC = this.map[i][j];
				pC.up = this.getCell(i, j-1);
				pC.right = this.getCell(i+1, j);
				pC.left = this.getCell(i-1, j);
				pC.down = this.getCell(i, j+1);
			}
	}
	
	/**
	 * Grows the array adding one layer to the bottom. Initializes all new cells too with a null Place
	 */
	private void growSouth() {
		PlaceCell[][] tempMap = new PlaceCell[this.currentWidth][this.currentHeight+1];
		for (int i = 0; i < this.currentWidth; i++)
			for (int j = 0; j < this.currentHeight; j++)
				tempMap[i][j] = this.map[i][j];
		for (int i = 0; i < this.currentWidth; i++) 
			tempMap[i][this.currentHeight] = new PlaceCell(target,c);
		
		this.map = tempMap;
		this.currentHeight++;
		this.linkAllCells();
	}
	/**
	 * Grows the array adding one layer to the top. Initializes all new cells too with a null Place
	 */
	private void growNorth() {
		PlaceCell[][] tempMap = new PlaceCell[this.currentWidth][this.currentHeight+1];
		for (int i = 0; i < this.currentWidth; i++)
			for (int j = 0; j < this.currentHeight; j++)
				tempMap[i][j+1] = this.map[i][j];
		for (int i = 0; i < this.currentWidth; i++) 
			tempMap[i][0] = new PlaceCell(target,c);
		
		this.map = tempMap;
		this.currentHeight++;
		this.linkAllCells();
	}
	/**
	 * Grows the array adding one column to the right. Initializes all new cells too with a null Place
	 */
	private void growEast() {
		PlaceCell[][] tempMap = new PlaceCell[this.currentWidth+1][this.currentHeight];
		for (int i = 0; i < this.currentWidth; i++)
			for (int j = 0; j < this.currentHeight; j++)
				tempMap[i][j] = this.map[i][j];
		for (int i = 0; i < this.currentHeight; i++) 
			tempMap[this.currentWidth][i] = new PlaceCell(target,c);
		
		this.map = tempMap;
		this.currentWidth++;
		this.linkAllCells();
	}
	/**
	 * Grows the array adding one column to the left. Initializes all new cells too with a null Place
	 */
	private void growWest() {
		PlaceCell[][] tempMap = new PlaceCell[this.currentWidth+1][this.currentHeight];
		for (int i = 0; i < this.currentWidth; i++)
			for (int j = 0; j < this.currentHeight; j++)
				tempMap[i+1][j] = this.map[i][j];
		for (int i = 0; i < this.currentHeight; i++) 
			tempMap[0][i] = new PlaceCell(target,c);
		
		this.map = tempMap;
		this.currentWidth++;
		this.linkAllCells();
	}
	
	// Cosas de pintar aqui debajo
	
	private boolean lastReDrawInternal = false;
	private int lastx;
	private int lasty;
	private int lastwidth;
	private int lastheight;
	
	/**
	 * Forces the component to be redrawn. All the old cells are removed from the container, and added again along with new ones, if existing
	 */
	protected void reDraw() {
		if (this.currentDrawnHeight != this.currentHeight || this.currentDrawnWidth != this.currentWidth) {
			this.removeAll();
			this.setLayout(new GridLayout (this.currentHeight, this.currentWidth));
			for (int j = 0; j < this.currentHeight; j++)
				for (int i = 0; i < this.currentWidth; i++)
					this.add(this.map[i][j]);
			
			this.currentDrawnHeight = this.currentHeight;
			this.currentDrawnWidth = this.currentWidth;
		}
		this.lastReDrawInternal = true;
		this.reshape(this.lastx, this.lasty,this.lastwidth, this.lastheight);
		this.paintAll(getGraphics());
	}

	/**
	 * Even though reshape is deprecated, its new version .setBounds() doesn't fully update the component on every draw, 
	 * so the screen might display the wrong layout before truly updating. This way we ensure that the object is redrawn properly. <br>
	 * This method forces the component to have the maximum dimensions that allow all of its cells to be exactly square.
	 */
	@SuppressWarnings("deprecation")
	public void reshape(int x, int y, int width, int height) {
		int currentheight = height, currentx = x, currenty = y, currentwidth = width;
		if (this.lastReDrawInternal) {
			currentheight = this.lastheight; currentwidth = this.lastwidth; currentx = this.lastx; currenty = this.lasty;
			this.lastReDrawInternal = false;
		} else {
			this.lastheight = height; this.lastwidth = width; this.lastx = x; this.lasty = y;
		}

		this.lastReDrawInternal = false;
		double squareSide = Math.min((double) currentheight/(double) this.currentHeight, (double) currentwidth/ (double) this.currentWidth);
		int newWidth = (int) squareSide * this.currentWidth;
		int newHeight = (int) squareSide * this.currentHeight;
		super.reshape(currentx + (currentwidth-newWidth)/2, currenty + (currentheight-newHeight)/2, newWidth, newHeight);
	}
}
