package tp.pr5.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import tp.pr5.Controller;
import tp.pr5.Direction;
import tp.pr5.PlaceInfo;
import tp.pr5.Rotation;
import tp.pr5.observers.NavigationModuleObserverHelper;
import tp.pr5.observers.RobotObserverHelper;

public class MainWindow extends JFrame {
	private static final long serialVersionUID = 1L;
	private int width = 900;
	private int height = 600;
	
	private Controller c;
	
	
	/**
	 * Creates the window and the panels using Swing Components.
	 * @param r
	 */
	public MainWindow (final Controller c, boolean inputEnabled) {
		super("WALL-E: The game");
		this.c = c;
		this.setLayout(new BorderLayout());
		this.add(new RobotPanel(c, inputEnabled), BorderLayout.NORTH);
		this.add(new NavigationPanel(c), BorderLayout.CENTER);
		this.add(new RobotStatusBar(c), BorderLayout.SOUTH);
		this.setJMenuBar(new TopMenuBar(c));
		
		//una vez hemos añadido todo forzamos una actualización para que salga
		
		if (inputEnabled)
			this.addKeyController();
		this.setParams();
		

		
		final JFrame frame = this;
		
		c.addEngineObserver(new RobotObserverHelper() {
			@Override
			public void communicationCompleted() { //aquí habrá llegado a través de los botones de quit
				System.exit(0);
			}

			@Override
			public void engineOff(boolean atSpaceShip) {
				if (atSpaceShip) {
					JOptionPane.showMessageDialog(frame, "Pulsa aceptar para ganar");	
					c.requestQuit();
				}
				while (JOptionPane.showConfirmDialog(frame, "Pulsa sí para perder", "Eres malísimo", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {}
				c.requestQuit();
			}
		});
		c.addNavigationModuleObserver(new NavigationModuleObserverHelper() {
			@Override
			public void robotArrivesAtPlace(Direction heading, PlaceInfo place) {
				if (place.isSpaceship()) {
									
				}
			}
		});
	}
	
	/**
	 * Sets various parameters to default values
	 */
	private void setParams() {
		this.setSize(this.width, this.height);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setMinimumSize(new Dimension(600,300));
	}
	
	/**
	 * Adds a key controller to the window. It will catch any key events when the window has the OS focus. Any unprocessed events will continue down the priority line thus not being consumed.
	 */
	private void addKeyController() {
		//la basura del addKeyListener no va porque el control se pira hacia los componentes de dentro
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
			@Override
			public boolean dispatchKeyEvent(KeyEvent arg0) {
				if (arg0.getID() == KeyEvent.KEY_PRESSED) {
					if (arg0.getKeyCode() == KeyEvent.VK_UP)
						c.communicateMoveInstruction();
					else if (arg0.getKeyCode() == KeyEvent.VK_LEFT)
						c.communicateTurnInstruction(Rotation.LEFT);
					else if (arg0.getKeyCode() == KeyEvent.VK_RIGHT)
						c.communicateTurnInstruction(Rotation.RIGHT);
					else if (arg0.isControlDown() && arg0.getKeyCode() == KeyEvent.VK_Z )
						c.communicateUndoInstruction();
					else if (arg0.isControlDown() && arg0.getKeyCode() == KeyEvent.VK_Y )
						c.communicateRedoInstruction();
					else return false;
					arg0.consume();
				}
				return false;
			}
		});
	}

}
