package tp.pr5.gui.components;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class ImagePanel extends JPanel{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Image image = null;

	/**
	 * Constructor specifying an image
	 * @param path
	 */
    public ImagePanel(String path) {
		this.setImage(path);
    }
    
    /**
     * Default constructor specifying no image
     */
    public ImagePanel() {}

    /**
     * Sets the image on the display to the one sent in the path
     * @param path
     */
    public void setImage(String path) {
		ImageIcon icon = new ImageIcon(path);
		image = icon.getImage();
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null)
        	g.drawImage(image, 0, 0, null);        
    }

}
