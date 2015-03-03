package tp.pr5.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.JLabel;
import javax.swing.JPanel;

import tp.pr5.Controller;
import tp.pr5.console.Console;

public class RobotStatusBar extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RobotStatusBar(Controller c) {
		this.setPreferredSize(new Dimension(Integer.MAX_VALUE,16));
		this.setLayout(new BorderLayout());
		final JLabel outLabel = new JLabel(); outLabel.setHorizontalAlignment(JLabel.CENTER); outLabel.setText("OUT"); 
		this.add(outLabel, BorderLayout.WEST);
		OutputStream o = new OutputStream() {
			@Override
			public void write(int arg0) throws IOException {}			
		};
		PrintStream pO = new PrintStream(o) {
			@Override
			public void println(String x) {
				String res = "<html>OUT: <font color='green'>" + x + "   </font></html>";
				try {
					outLabel.setText(res);
				} catch (Exception e) {
					//ignore
				}
			}
		};
		
		new Console(c, pO, null, null).setTrimSize(10); //nueva salida de consola a las etiquetas con trimSize a 10
	}
}
