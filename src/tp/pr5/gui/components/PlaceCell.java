package tp.pr5.gui.components;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JTextArea;

import tp.pr5.Controller;
import tp.pr5.Direction;
import tp.pr5.PlaceInfo;
import tp.pr5.gui.util.ImageLoader;
import tp.pr5.lang.Messages;

public class PlaceCell extends JButton {
	private static final long serialVersionUID = 1L;
	
	private PlaceInfo place = null;
	protected PlaceCell up = null;
	protected PlaceCell down = null;
	protected PlaceCell left = null;
	protected PlaceCell right = null;
	
	private static final int WALL_NOWALL = 0;
	private static final int WALL_NOSTREET = 1;
	private static final int WALL_CLOSED = 2;
	private static final int WALL_UNKNOWN = 3;
	
	private Integer southWall = WALL_UNKNOWN;
	private Integer northWall = WALL_UNKNOWN;
	private Integer eastWall = WALL_UNKNOWN;
	private Integer westWall = WALL_UNKNOWN;
	
	private boolean lastInstructionFailed = false;

	/**
	 * Basic constructor that should only be used if the class fields are set afterwards
	 * @param jText
	 * @param r
	 */
	public PlaceCell(JTextArea jText, Controller c) {
		super();
		this.setAll(null, jText, null, null, null, null,c);
	}
	/**
	 * Constructor specifying all the data needed for the component
	 * @param p
	 * @param jText
	 * @param up
	 * @param right
	 * @param down
	 * @param left
	 * @param r
	 */
	public PlaceCell(Controller c, final JTextArea jText, PlaceCell up, PlaceCell right, PlaceCell down, PlaceCell left) {
		super();
		this.setAll(null, jText, up, right, down, left,c);
	}
	
	/**
	 * Sets all the parameters of the class. Also initializes listeners for the mouse and for the robot itself and its nav. mod
	 * @param p 
	 * @param jText 
	 * @param up
	 * @param right
	 * @param down
	 * @param left
	 * @param r Can't be null
	 */
	private void setAll (PlaceInfo p, final JTextArea jText, PlaceCell up, PlaceCell right, PlaceCell down, PlaceCell left, Controller c) {
		this.up = up;
		this.down = down;
		this.left = left;
		this.right = right;
		this.setPlace(p);
		this.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent arg0) {
				onHover = true;
			}
			@Override
			public void mouseExited(MouseEvent arg0) {
				onHover = false;
			}
			@Override
			public void mousePressed(MouseEvent arg0) {
				onClick = true;
			}
			@Override
			public void mouseReleased(MouseEvent arg0) {
				onClick = false;
			}
		});
		this.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (jText == null)
					return;
				if (place == null) 
					jText.setText(Messages.MAP_UNEXPLORED);
				else
					jText.setText(place.toString());
			}
		});
	}
	/**
	 * @param wall
	 * @param isOpen
	 * @return the changed wall state
	 */
	private int switchWall(int wall, boolean isOpen) {
		if (isOpen)
			return PlaceCell.WALL_NOWALL;
		else
			return PlaceCell.WALL_CLOSED;
	}
	/**
	 * Sets the place
	 * @param p
	 */
	public void setPlace(PlaceInfo p) {
		this.place = p;
		if (p != null)
			super.setText(p.getName());
	}
	
	/**
	 * Sets the direction of the robot when inside this cell, for drawing purposes
	 * @param dir
	 */
	public void setDirection(Direction dir) {
		this.walleDirection = dir;
		repaint();
	}
	
	/**
	 * Sets if the current PlaceCell has walle on it.
	 * @param isOnWalle
	 */
	public void setOnWalle(boolean isOnWalle) {
		this.onWalle = isOnWalle;
		repaint();
	}
	
	/**
	 * Sets the wall in a given direction to a given wallType. Use constants given by PlaceCell
	 * @param dir
	 * @param type
	 */
	public void setWall(Direction dir, int type) {
		switch(dir) {
		case NORTH: northWall = type; break;
		case SOUTH: southWall = type; break;
		case EAST:	eastWall = type; break;
		case WEST:	westWall = type; break;
		default: break;
		}
		repaint();
	}
	/**
	 * Switches a door
	 * @param dir Direction where to switch
	 * @param wasOpened whether the door was opened or closed
	 */
	public void switchDoor(Direction dir, boolean wasOpened) {
		switch(dir) {
		case NORTH: northWall = switchWall(northWall, wasOpened); break;
		case SOUTH: southWall = switchWall(southWall, wasOpened); break;
		case EAST:	eastWall = switchWall(eastWall, wasOpened); break;
		case WEST:	westWall = switchWall(westWall, wasOpened); break;
		default: break;
		}
		repaint();
	}
	
	/**
	 * Sets to true the flag that indicates that the last instruction failed, forcing to draw the exclamation mark above the player's head.
	 */
	public void setInstructionFail() {
		this.lastInstructionFailed = true;
		repaint();
	}

	
	//A PARTIR DE AQUÍ ES LA PARTE DE PINTAR

	private boolean onHover = false; //activa la máscara de ratón sobre el componente
	private boolean onClick = false; //" " " " click " " "
	private boolean onWalle = false; //añade al robot al lugar
	private Direction walleDirection = Direction.NORTH;
	
	@Override
	public void paint(Graphics g) {
		this.drawBase(g);
		this.drawPlaceName(g);
		this.applyMasks(g);
	}
	/**
	 * draws the base image (including entities)
	 * @param g
	 */
	private void drawBase(Graphics g) {
		if (this.place != null) {
			//dibujamos el fondo
			if (this.place.isSpaceship())
				this.drawTile(ImageLoader.PLACE_EXIT.getImage(), g, 0, 0, 8, 8);
			else
				this.drawTile(ImageLoader.PLACE_DEFAULT.getImage(), g, 0, 0, 8, 8);
			//dibujamos uno o dos items
			if (this.place.numberOfItems() != 0)
				this.drawTile(ImageLoader.OBJECT_POKEBALL.getImage(), g, 6, 3, 1, 1);
			if (this.place.numberOfItems() > 1)
				this.drawTile(ImageLoader.OBJECT_POKEBALL.getImage(), g, 3, 6, 1, 1);
		}
		else
			//dibujamos el fondo de si no hay place
			g.drawImage(ImageLoader.PLACE_UNKNOWN.getImage(), 0, 0, getWidth(), getHeight(), null);
		//si tenemos al robot lo dibujamos
		
		if (this.onWalle) 
			this.drawTile(ImageLoader.getWalleImageIcon(this.walleDirection).getImage(), g, 4, 3, 1, 2);
	}
	/**
	 * draws the text on top of the base image (which should be already drawn when this method is called)
	 * @param g
	 */
	private void drawPlaceName(Graphics g) {
		//ajustamos la fuente a seis píxeles (relativos) de alto
		g.setFont(g.getFont().deriveFont((float)getHeight()*(6f/128f)));
		//ajustamos el ancho disponible a 24 píxeles para centrar el texto
		float availableWidth = (float)getWidth()*(24f/128f);
		//dibujamos el texto en la pancarta del edificio
		g.drawString(super.getText(), 
				//centramos la esquina inferior izquierda en el píxel (20,26)
				(int) ((double)getWidth()*(20/128.0)+(availableWidth-g.getFontMetrics().getStringBounds(super.getText(), g).getWidth())/2),
				(int) ((double)getHeight()*(26/128.0)));
	}
	/**
	 * Applies different transparent masks on top of the drawn component
	 * @param g
	 */
	private void applyMasks(Graphics g) {
		//si estamos haciendo click lo denotamos mediatne una máscara transparente
		if (this.onClick) 
			this.drawTile(ImageLoader.PLACE_ONCLICK.getImage(), g, 0, 0, 8, 8);
			
		//y si tenemos el ratón encima, igual
		else if (this.onHover)
			this.drawTile(ImageLoader.PLACE_ONHOVER.getImage(), g, 0, 0, 8, 8);
		
		if (this.northWall == PlaceCell.WALL_CLOSED) 
			this.drawTile(ImageLoader.OBJECT_SNORLAX.getImage(), g, 4, 0, 2, 2);
		if (this.northWall == PlaceCell.WALL_NOSTREET)
			this.drawTile(ImageLoader.OBJECT_STOMP.getImage(), g, 4, 0, 2, 2);
		
		if (this.southWall == PlaceCell.WALL_CLOSED) 
			this.drawTile(ImageLoader.OBJECT_SNORLAX.getImage(), g, 4, 6, 2, 2);
		if (this.southWall == PlaceCell.WALL_NOSTREET)
			this.drawTile(ImageLoader.OBJECT_STOMP.getImage(), g, 4, 6, 2, 2);
		
		if (this.eastWall == PlaceCell.WALL_CLOSED) 
			this.drawTile(ImageLoader.OBJECT_SNORLAX.getImage(), g, 6, 4, 2, 2);
		if (this.eastWall == PlaceCell.WALL_NOSTREET)
			this.drawTile(ImageLoader.OBJECT_STOMP.getImage(), g, 6, 4, 2, 2);
		
		if (this.westWall == PlaceCell.WALL_CLOSED) 
			this.drawTile(ImageLoader.OBJECT_SNORLAX.getImage(), g, 0, 4, 2, 2);
		if (this.westWall == PlaceCell.WALL_NOSTREET)
			this.drawTile(ImageLoader.OBJECT_STOMP.getImage(), g, 0, 4, 2, 2);
		
		if (this.lastInstructionFailed) {
			this.lastInstructionFailed = false;
			if (onWalle)
				this.drawTile(ImageLoader.MASK_FAIL.getImage(), g, 4, 3, 1, 1);
		}
	}
	
	/**
	 * Draws a tile on the component
	 * @param i tile image to draw
	 * @param g graphics to use to draw
	 * @param tileXpos position (between 0 and 7) of the tile, along the x axis, where the top-left corner will be located
	 * @param tileYpos position (between 0 and 7) of the tile, along the y axis, where the top-left corner will be located
	 * @param tileWidth	width (in tile units) of this tile
	 * @param tileHeight height (in tile units) of this tile
	 */
	private void drawTile(Image i, Graphics g, int tileXpos, int tileYpos, int tileWidth, int tileHeight) {
		g.drawImage(i, tileXpos*getWidth()/8, tileYpos*getHeight()/8, tileWidth*getWidth()/8, tileHeight*getHeight()/8, null);
	}
	
}
