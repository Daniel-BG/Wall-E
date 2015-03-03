package tp.pr5.gui;


import java.awt.BorderLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import tp.pr5.Controller;
import tp.pr5.Direction;
import tp.pr5.PlaceInfo;
import tp.pr5.gui.util.ImageLoader;
import tp.pr5.observers.NavigationModuleObserverHelper;

public class GraphicalView extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates and sets everything from both the robot image and map, setting every connection between component
	 * @param r
	 * @param jText
	 */
	public GraphicalView(final Controller c, JTextArea jText) {
		this.setLayout(new BorderLayout());

		this.add(new JLabel() {
			private static final long serialVersionUID = 1L;
			{
				c.addNavigationModuleObserver(new NavigationModuleObserverHelper() {
					@Override
					public void initNavigationModule(PlaceInfo place,Direction heading) {
						RotationChanged(heading);
					}

					@Override
					public void headingChanged(Direction newDirection) {
						RotationChanged(newDirection);
					}
				});
			}
			
			public void RotationChanged(Direction newDirection) {
				switch(newDirection) {
				case NORTH: this.setIcon(new ImageIcon(ImageLoader.W_NORTH.getImage().getScaledInstance(64, 128, 0))); break;
				case SOUTH: this.setIcon(new ImageIcon(ImageLoader.W_SOUTH.getImage().getScaledInstance(64, 128, 0))); break;
				case EAST:	this.setIcon(new ImageIcon(ImageLoader.W_EAST.getImage().getScaledInstance(64, 128, 0))); break;
				case WEST:	this.setIcon(new ImageIcon(ImageLoader.W_WEST.getImage().getScaledInstance(64, 128, 0))); break;
				default:
				}
			}
			
			
		}, BorderLayout.WEST);
		
		this.add(new MapContainer(c, jText), BorderLayout.CENTER);
		
	}
}
