package tp.pr5.gui.components;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.ImageObserver;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class ImageButton extends JButton {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Image[] images = new Image[3];
	ImageObserver[] imageObservers = new ImageObserver[3];

	public static final int IMAGE_DEFAULT = 0;
	public static final int IMAGE_ONHOVER = 1;
	public static final int IMAGE_ONCLICK = 2;
	
	private boolean shift = false;
	private boolean onClick = false;
	
	/**
	 * @param fileDefault Image displayed by default
	 * @param fileOnHover Image displayed when the mouse is over the component
	 * @param fileOnClick Image displayed when the mouse is clicking the component
	 * @param text Text shown in the component
	 */
	public ImageButton (String fileDefault, String fileOnHover, String fileOnClick, String text) {
		super(text);
		this.loadImage(IMAGE_DEFAULT, fileDefault);
		this.loadImage(IMAGE_ONHOVER, fileOnHover);
		this.loadImage(IMAGE_ONCLICK, fileOnClick);
		this.setMouseListener();
	}
	
	/**
	 * Sets the image "c" (should be called with one of the static constants in this class) to the one in the path
	 * @param c
	 * @param path
	 */
	public void setImage(int c, String path) {
		this.loadImage(c, path);
	}
	/**
	 * Effectively loads the image sent in the previous function
	 * @param c
	 * @param path
	 */
	private void loadImage(int c, String path) {
		if (c >= 3)
			throw new IndexOutOfBoundsException();
		ImageIcon icon = new ImageIcon(path);
		images[c] = icon.getImage();
		imageObservers[c] = icon.getImageObserver();
	}
	
	/**
	 * Creates a mouse listener for this component
	 */
	private void setMouseListener() {
		if (this.getMouseListeners().length == 0)
		this.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent arg0) {}
			@Override
			public void mouseEntered(MouseEvent arg0) {
				shift = true;
			}
			@Override
			public void mouseExited(MouseEvent arg0) {
				shift = false;
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
	}

	public void paint( Graphics g ) {
		//super.paint( g ); //obviamos el pintado de super pues va a ser sobreescrito por el nuestro enteramente
		if (onClick) 
			g.drawImage(images[IMAGE_ONCLICK], 0, 0, getWidth(), getHeight(), imageObservers[IMAGE_ONCLICK]);
		else
			if (shift)
				g.drawImage(images[IMAGE_ONHOVER],  0 , 0 , getWidth() , getHeight() , imageObservers[IMAGE_ONHOVER]);
			else
				g.drawImage(images[IMAGE_DEFAULT],  0 , 0 , getWidth() , getHeight() , imageObservers[IMAGE_DEFAULT]);
		g.drawString(super.getText(), 
				(int) ((getWidth()-g.getFontMetrics().getStringBounds(super.getText(),g).getWidth())/2),					//centramos la x
				-3+(int) (((double)getHeight()+g.getFontMetrics().getStringBounds(super.getText(),g).getHeight())/2.0));	//centramos la y
	}
	
}
