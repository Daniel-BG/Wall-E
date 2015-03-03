package tp.pr5.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import tp.pr5.Controller;
import tp.pr5.items.Item;
import tp.pr5.observers.ItemContainerObserverHelper;

public class RobotInfo extends JPanel{
	private static final long serialVersionUID = 1L;
	private final JTable table;
	final DefaultTableModel model = new DefaultTableModel(); 
	/**
	 * Creates the robot info, adding a new ponts and status label, and creating a new JTable to hold the items
	 * @param r
	 */
	public RobotInfo(final Controller c) {
		this.setLayout(new BorderLayout());
		this.setBorder(new TitledBorder("Robot Info"));
		this.add(new RobotFuelAndPointsStatus(c), BorderLayout.NORTH);
		model.addColumn("Item ID"); 
		model.addColumn("Item Description"); 
		this.table = new JTable(model) {
			private static final long serialVersionUID = 1L;
			{
				c.addItemContainerObserver(new ItemContainerObserverHelper() {
					private Thread changeThread;
					@Override
					public void inventoryChange(final List<Item> inventory) {
						//sólo si podemos lo interrumpimos, si no, no es necesario
						if (changeThread != null && changeThread.isAlive())
							changeThread.interrupt();
						//así aseguramos que sólo se cambie la tabla de una vez en una vez, porque estaba dando
						//errores rojos feos en la consola
						(changeThread = new Thread() {
							public void run() {
								//si tras 25 ms no ha cambiado el inventario, lo actualizamos
								try {
									Thread.sleep(25);
								} catch (InterruptedException e) {
									//si nos interrumpen por otro cambio, pasamos de actualizar y ya lo hará el siguiente
									return;
								}
								
								model.getDataVector().removeAllElements();
								model.fireTableDataChanged();
								for (int i = 0; i < inventory.size(); i++) 
									model.addRow(new Object[]{inventory.get(i).getId(),inventory.get(i).toString()});
								model.fireTableDataChanged();
							}
						}).start();

					}
				});
			}	
		};
		
		this.add(new JScrollPane(table,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER) {
			private static final long serialVersionUID = 1L;
			{	
				this.setPreferredSize(new Dimension(0, 0)); //así se limita a ocupar lo que le dejen		
			}
		},BorderLayout.CENTER);
		this.addKeyController();
	}

	public JTable getJTable() {
		return this.table;
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
					if (arg0.getKeyCode() == KeyEvent.VK_W)
						upTable();
					else if (arg0.getKeyCode() == KeyEvent.VK_S)
						downTable();
					else return false;
					arg0.consume();
				}
				return false;
			}
		});
	}
	
	private void upTable() {
		int index;
		if (model.getRowCount() == 0)
			return;
		if (table.getSelectedRow() <= 0)
			index = model.getRowCount()-1;
		else
			index = table.getSelectedRow()-1;
		table.changeSelection(index, 0, false, false);
	}
	
	private void downTable() {
		int index;
		if (model.getRowCount() == 0)
			return;
		if (table.getSelectedRow() == -1)
			index = 0;
		else if (table.getSelectedRow() == model.getRowCount()-1)
			index = 0;
		else
			index = table.getSelectedRow()+1;
		table.changeSelection(index, 0, false, false);
	}

	
}
