package tp.pr5.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;

import tp.pr5.Controller;

public class TopMenuBar extends JMenuBar {
	private static final long serialVersionUID = 1L;

	public TopMenuBar(final Controller c) {
		JMenu menu, submenu;
		JMenuItem menuItem;
		JRadioButtonMenuItem rbMenuItem;
		JCheckBoxMenuItem cbMenuItem;

		//Create the menu bar.

		//Build the first menu.
		menu = new JMenu("Help");
		menu.setMnemonic(KeyEvent.VK_P);
		menu.getAccessibleContext().setAccessibleDescription(
		        "The only menu in this program that has menu items");
		//this.add(menu);

		//a group of JMenuItems
		menuItem = new JMenuItem("A text-only menu item", KeyEvent.VK_T);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription("This doesn't really do anything");
		menu.add(menuItem);

		menuItem = new JMenuItem("Both text and icon", new ImageIcon("images/middle.gif"));
		menuItem.setMnemonic(KeyEvent.VK_B);
		menu.add(menuItem);

		menuItem = new JMenuItem(new ImageIcon("images/middle.gif"));
		menuItem.setMnemonic(KeyEvent.VK_D);
		menu.add(menuItem);

		//a group of radio button menu items
		menu.addSeparator();
		ButtonGroup group = new ButtonGroup();
		rbMenuItem = new JRadioButtonMenuItem("A radio button menu item");
		rbMenuItem.setSelected(true);
		rbMenuItem.setMnemonic(KeyEvent.VK_R);
		group.add(rbMenuItem);
		menu.add(rbMenuItem);

		rbMenuItem = new JRadioButtonMenuItem("Another one");
		rbMenuItem.setMnemonic(KeyEvent.VK_O);
		group.add(rbMenuItem);
		menu.add(rbMenuItem);

		//a group of check box menu items
		menu.addSeparator();
		cbMenuItem = new JCheckBoxMenuItem("A check box menu item");
		cbMenuItem.setMnemonic(KeyEvent.VK_C);
		menu.add(cbMenuItem);

		cbMenuItem = new JCheckBoxMenuItem("Another one");
		cbMenuItem.setMnemonic(KeyEvent.VK_H);
		menu.add(cbMenuItem);

		//a submenu
		menu.addSeparator();
		submenu = new JMenu("A submenu");
		submenu.setMnemonic(KeyEvent.VK_S);

		menuItem = new JMenuItem("An item in the submenu");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, ActionEvent.ALT_MASK));
		submenu.add(menuItem);

		menuItem = new JMenuItem("Another item");
		submenu.add(menuItem);
		menu.add(submenu);

		//Build second menu in the menu bar.
		menu = new JMenu("File");
		menu.setMnemonic(KeyEvent.VK_F);
		menu.getAccessibleContext().setAccessibleDescription("This menu does nothing");
		this.add(menu);
		
		//Build third menu in the menu bar
		menuItem = new JMenuItem("Quit");
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (JOptionPane.showConfirmDialog(getTopLevelAncestor(), "¿De verdad deseas salir?", "WALL·E says", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
					c.requestQuit();
			}
		});
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.ALT_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription("Help");
		menu.add(menuItem);

	}
	
}
