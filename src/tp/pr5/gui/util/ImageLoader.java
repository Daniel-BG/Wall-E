package tp.pr5.gui.util;

import java.awt.Image;
import javax.swing.ImageIcon;

import tp.pr5.Direction;

public class ImageLoader {
	public static final ImageIcon W_NORTH = new ImageIcon(ImageLoader.class.getResource("/walleImages/NORTH_OLDMAN.png"));
	public static final ImageIcon W_SOUTH = new ImageIcon(ImageLoader.class.getResource("/walleImages/SOUTH_OLDMAN.png"));
	public static final ImageIcon W_EAST = new ImageIcon(ImageLoader.class.getResource("/walleImages/EAST_OLDMAN.png"));
	public static final ImageIcon W_WEST = new ImageIcon(ImageLoader.class.getResource("/walleImages/WEST_OLDMAN.png"));

	public static final ImageIcon PLACE_DEFAULT = new ImageIcon(ImageLoader.class.getResource("/placeImages/Place.png"));
	public static final ImageIcon PLACE_EXIT = new ImageIcon(ImageLoader.class.getResource("/placeImages/Place_exit.png"));
	public static final ImageIcon PLACE_ONHOVER = new ImageIcon(ImageLoader.class.getResource("/placeImages/PLACE_ONHOVER.png"));
	public static final ImageIcon PLACE_ONCLICK = new ImageIcon(ImageLoader.class.getResource("/placeImages/PLACE_ONCLICK.png"));
	public static final ImageIcon PLACE_UNKNOWN = new ImageIcon(ImageLoader.class.getResource("/placeImages/UnknownPlace.png"));
	
	public static final ImageIcon OBJECT_POKEBALL = new ImageIcon(ImageLoader.class.getResource("/Object/POKEBALL.png"));
	public static final ImageIcon OBJECT_STOMP = new ImageIcon(ImageLoader.class.getResource("/Object/NOSTREET_STOMP.png"));
	public static final ImageIcon OBJECT_ROCK = new ImageIcon (ImageLoader.class.getResource("/Object/CLOSEDSTREET_ROCK.png"));
	public static final ImageIcon OBJECT_SNORLAX = new ImageIcon(ImageLoader.class.getResource("/Object/CLOSED_STREET_SNORLAX.png"));
	public static final ImageIcon MASK_FAIL = new ImageIcon(ImageLoader.class.getResource("/masks/MASK_EXCLAMATION_MARK.png"));

	/**
	 * Loads and returns an image
	 * @param path to the image to load
	 * @return the loaded image
	 */
	public static Image loadImage(String path) {
		ImageIcon icon = new ImageIcon(path);
		return icon.getImage();
		//TODO igual hace falta esto: imageObservers[c] = icon.getImageObserver();
	}
	
	/**
	 * Gets the correct robot ImageIcon that faces the direction "walleDirection"
	 * @param walleDirection
	 * @return
	 */
	public static ImageIcon getWalleImageIcon(Direction walleDirection) {
		switch(walleDirection) {
		case NORTH: return W_NORTH;
		case SOUTH: return W_SOUTH;
		case EAST:	return W_EAST;
		case WEST:	return W_WEST;
		default: return null;
		}
	}
}
