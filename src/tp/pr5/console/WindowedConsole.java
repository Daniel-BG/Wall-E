package tp.pr5.console;

import java.awt.Color;
import java.awt.Dimension;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

import tp.pr5.Controller;

public class WindowedConsole extends JFrame {
	private static final long serialVersionUID = 1L;
	
	public WindowedConsole (Controller c) {
		final JTextArea jText = new JTextArea();
		jText.setFont(jText.getFont().deriveFont(10f));
		jText.setBackground(Color.black);
		jText.setCaretColor(Color.white);
		jText.setForeground(Color.white);
		((DefaultCaret) jText.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		jText.setEditable(false);
		
		this.add(new JScrollPane(jText));
		
		
		OutputStream o = new OutputStream() {
			@Override
			public void write(int arg0) throws IOException {}			
		};
		PrintStream pD = new PrintStream(o) {
			@Override
			public void println(String x) {
				jText.append(x + "\n");
				jText.setCaretPosition(jText.getDocument().getLength());
			}
		};
		
		new Console(c, null, pD, null).setTrimSize(Console.TRIM_NONE); //nueva salida de consola a las etiquetas con trimSize a 10
		this.setSize(new Dimension(300,600));
		this.setVisible(true);
	}

}
