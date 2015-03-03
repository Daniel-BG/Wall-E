package tp.pr5.gui;

import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import tp.pr5.Controller;
import tp.pr5.observers.RobotObserverHelper;

public class RobotFuelAndPointsStatus extends JPanel{
	private static final long serialVersionUID = 1L;

	/**
	 * Initializes the component creating a label that has a listener linked to the robot sent, so every time it sends a change, the label picks it up and makes the correct changes
	 * @param r
	 */
	public RobotFuelAndPointsStatus(final Controller c) {
		this.setLayout(new FlowLayout());
		JLabel label = new JLabel() {
			{ //esto lo ejecutamos al crear el bicho. Addfuel es para activar el listener y que actualice el fuel
								
				c.addEngineObserver(new RobotObserverHelper() {

					@Override
					public void robotUpdate(int newFuel, int newPoints) {
						setFuel(newFuel);
						setPoints(newPoints);
						reDraw();
					}
					
				});
				this.reDraw();
			}
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			private int fuel;
			private int points;
			
			/**
			 * Sets the points for the label to show
			 * @param p
			 */
			public void setPoints(int p) {
				this.points = p;
				this.reDraw();
			}
			/**
			 * Sets the fuel for the label to show
			 * @param f
			 */
			public void setFuel(int f) {
				this.fuel = f;
				this.reDraw();
			}
			
			/**
			 * makes sure the object is redrawn after any change sent by the listener
			 */
			private void reDraw() {
				this.setText("Fuel: " + this.fuel + " Recycled: " + this.points);
				this.repaint();
			}
		};
		this.add(label);
	}
}
