package tp.pr5.gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import tp.pr5.Controller;
import tp.pr5.Rotation;

public class InstructionsPanel extends JPanel {
	
	/**
	 * Creates and adds all the controls to the Instructions Panel
	 * @param r Robot to link commands to
	 */
	public InstructionsPanel(final Controller c, boolean inputEnabled, final JTable table) {
		this.setBorder(new TitledBorder("Instructions"));
		this.setLayout(new GridLayout(5,2));
		
		final JButton move = new JButton("MOVE") {
			private static final long serialVersionUID = 1L;
			{
				this.setEnabled(false);
				this.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						c.communicateMoveInstruction();
					}
				});
			}
		};
		this.add(move);
		move.setEnabled(inputEnabled);
		
		final JButton quit = new JButton("QUIT") {
			private static final long serialVersionUID = 1L;
			{
				this.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (JOptionPane.showConfirmDialog(getTopLevelAncestor(), "¿De verdad deseas salir?", "WALL·E says", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
							c.requestQuit();
					}
				});
			}
		};
		this.add(quit);
		quit.setEnabled(inputEnabled);
		
		final JComboBox<Rotation> jCombo = new JComboBox<Rotation>();
		jCombo.addItem(Rotation.LEFT);
		jCombo.addItem(Rotation.RIGHT);
		
		final JButton turn = new JButton("TURN") {
			private static final long serialVersionUID = 1L;
			{
				this.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						c.communicateTurnInstruction(jCombo.getItemAt(jCombo.getSelectedIndex()));
					}
				});
			}
		};
		this.add(turn);
		turn.setEnabled(inputEnabled);
		
		this.add(jCombo);
		jCombo.setEnabled(inputEnabled);
		
		final JTextField jtext = new JTextField();
		jtext.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				c.communicatePickInstruction(jtext.getText());
			}
		});
		
		final JButton pick = new JButton("PICK") {
			private static final long serialVersionUID = 1L;
			{
				this.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						c.communicatePickInstruction(jtext.getText());
					}
				});
			}
		};
		
		this.add(pick);
		pick.setEnabled(inputEnabled);
		
		this.add(jtext);
		jtext.setEnabled(inputEnabled);
		
		final JButton drop = new JButton("DROP") {
			private static final long serialVersionUID = 1L;
			{
				this.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (table.getSelectedRow() != 0xffffffff)
							c.communicateDropInstruction(table.getModel().getValueAt(table.getSelectedRow(), 0).toString());
					}
				});
			}
		};
		this.add(drop);
		drop.setEnabled(inputEnabled);
		
		final JButton operate = new JButton("OPERATE") {
			private static final long serialVersionUID = 1L;
			{
				this.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (table.getSelectedRow() != 0xffffffff)
							c.communicateOperateInstruction(table.getModel().getValueAt(table.getSelectedRow(), 0).toString());
					}
				});
			}
		};
		this.add(operate);
		operate.setEnabled(inputEnabled);
	
		final JButton undo = new JButton("UNDO") {
			private static final long serialVersionUID = 1L;
			{
				this.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						c.communicateUndoInstruction();
					}
				});
			}
		};
		this.add(undo);
		undo.setEnabled(inputEnabled);
		
		final JButton redo = new JButton("REDO") {
			private static final long serialVersionUID = 1L;
			{
				this.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						c.communicateRedoInstruction();
					}
				});
			}
		};
		
		this.add(redo);
		redo.setEnabled(inputEnabled);
	}

	private static final long serialVersionUID = 1L;
}
