package tp.pr5.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import tp.pr5.Controller;
import tp.pr5.Direction;
import tp.pr5.PlaceInfo;
import tp.pr5.items.Item;
import tp.pr5.observers.ItemContainerObserverHelper;
import tp.pr5.observers.NavigationModuleObserverHelper;

public class PlaceStatusPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextArea jText; 
	
	/**
	 * Creates and adds the place information text field to the screen
	 * @param r
	 */
	public PlaceStatusPanel(final Controller c) {
		this.setLayout(new BorderLayout());
		this.add(new JScrollPane(new JTextArea() {
			private static final long serialVersionUID = 1L;
			{
				this.setEditable(false);
				jText = this;
				c.addItemContainerObserver(new ItemContainerObserverHelper() {
					{
						c.addNavigationModuleObserver(new NavigationModuleObserverHelper() {
							@Override
							public void initNavigationModule(PlaceInfo place, Direction heading) {
								this.setAndUpdatePlace(place);
							}
							@Override
							public void placeHasChanged(PlaceInfo place) {
								this.setAndUpdatePlace(place);
							}
							/**
							 * Sets the place in advance of any changes on the inventory and sets the text too
							 * @param place New place received
							 */
							private void setAndUpdatePlace(PlaceInfo place) {
								currentPlace = place;
								jText.setText(place.toString()); 
							}
						});
					}
					PlaceInfo currentPlace = null;
					
					@Override
					public void inventoryChange(List<Item> inventory) {
						jText.setText(currentPlace.toString());
					}
					
				});
			}
			
			
		},JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);
		this.setBorder(new TitledBorder("Log"));
		setPreferredSize(new Dimension(0, 120));
	}
	
	/**
	 * @return the text area this component has
	 */
	public JTextArea getJTextArea() {
		return jText;
	}
}
