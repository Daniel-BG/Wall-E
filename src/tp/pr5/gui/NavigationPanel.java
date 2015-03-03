package tp.pr5.gui;

import java.awt.BorderLayout;
import javax.swing.JPanel;

import tp.pr5.Controller;

/**
 * This class is in charge of the panel that displays the information about the robot heading and the city that is traversing. It contains the grid that represents the city in the Swing interface, a text area to show the place descriptions, and a label with an icon which represents the robot heading
 * The 11x11 grid contains PlaceCell objects and the first place starts at (5,5). This panel will update the visited places when the robot moves from one place to another. Additionally it will show the place description on a text area if the user clicks on a visited place.
 *
 */

public class NavigationPanel extends JPanel{
	private static final long serialVersionUID = 1L;

	/**
	 * Creates the navigation panel for the robot r
	 * @param r
	 */
	public NavigationPanel(Controller c) {
		this.setLayout(new BorderLayout());
		PlaceStatusPanel pSP = new PlaceStatusPanel(c);
		this.add(pSP, BorderLayout.SOUTH);
		this.add(new GraphicalView(c,pSP.getJTextArea()), BorderLayout.CENTER);
	}	
}
