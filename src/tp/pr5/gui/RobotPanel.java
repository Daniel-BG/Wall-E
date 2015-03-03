package tp.pr5.gui;

import javax.swing.JSplitPane;

import tp.pr5.Controller;
/**
 * RobotPanel displays information about the robot and its inventory. More specifically, it contains labels to show the robot fuel and the weight of recycled material and a table that represents the robot inventory. Each row displays information about an item contained in the inventory.
 */
public class RobotPanel extends JSplitPane{
	private static final long serialVersionUID = 1L;

	/**
	 * Creates and initializes all components within the RobotPanel
	 * @param r
	 */
	public RobotPanel(Controller c, boolean inputEnabled) {
		RobotInfo rI = new RobotInfo(c);
		this.setLeftComponent(new InstructionsPanel(c, inputEnabled, rI.getJTable()));
		this.setRightComponent(rI);
	}
}
