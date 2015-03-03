package tp.pr5.gui;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import tp.pr5.Controller;
import tp.pr5.gui.components.Map;

public class MapContainer extends JPanel {
	private static final long serialVersionUID = 1L;

	public MapContainer(Controller c, JTextArea j) {
		this.setLayout(new BorderLayout());
		this.setBorder(new TitledBorder("City Map"));
		this.add(new Map(c,j), BorderLayout.CENTER);
	}
}
