package tp.pr5;

import tp.pr5.items.Item;
import tp.pr5.items.ItemContainer;
import tp.pr5.lang.Messages;

public class Place implements PlaceInfo {

	/**
	 * 
	 * @param name nombre dellugar
	 * @param isSpaceShip si es la nave o no
	 * @param description datos del lugar que se mostrarán al llegar a él
	 */
	public Place (String name, boolean isSpaceShip, String description){
		this.name = name;
		this.description = description;
		this.isSpaceShip = isSpaceShip;
		this.chest = new ItemContainer();
	}
	
	
	/**
	 * Returns the name of the place and its descption, as well as a list of objects the place has
	 * @return A string with information about the place
	 */
	public String toString (){
		String base = this.name + Interpreter.LINE_SEPARATOR + 
				   this.description + Interpreter.LINE_SEPARATOR;
		//variable String temporal para devolver o place is empty o place not empty y lo que hay
		String temp = this.chest.toString();
		if (temp.equals("")) //caso de que no haya items
			return base + Messages.OUTPUT_SCANPLACE_NONE;
		else 
			return base + Messages.OUTPUT_SCANPLACE_CONTAINS + Interpreter.LINE_SEPARATOR + temp; 
	}
	
	/**
	 * Tries to pick an item characterized by a given identifier from the place. If the action was completed the item must be removed from the place.
	 * @param id The identifier of the item
	 * @return The item of identifier id if it exists in the place. Otherwise the method returns null
	 */
	public Item pickItem(String id) {
		return this.chest.pickItem(id);
	}
	/**
	 * Checks if an item is in this place
	 * @param id Identifier of an item
	 * @return true if and only if the place contains the item identified by id
	 */
	public boolean existItem(String id) {
		return (this.chest.getItem(id) != null);
	}
	
	/**
	 * Tries to add an item to the place. The operation can fail (if the place already contains an item with the same name)
	 * @param it The item to be added
	 * @return true if and only if the item can be added to the place, i.e., the place does not contain an item with the same name
	 */
	public boolean addItem(Item it) {
		if (this.chest.addItem(it))
			return true;
		return false;
	}
	// para qué hacer esta función si es la misma que addItem???
	/**
	 * Drop an item in this place. The operation can fail, returning false
	 * @param it The name of the item to be dropped.
	 * @return true if and only if the item is dropped in the place, i.e., an item with the same identifier does not exists in the place
	 */ 
	public boolean dropItem(Item it) {
		return this.addItem(it); //
	}
	
	/**
	 * Gets the itemcontainer from the place
	 * @return This place's itemcontainer
	 */
	public ItemContainer getContainer() {
		return this.chest;
	}
	/**
	 * @return True if the place's container is not empty
	 */
	public boolean hasItems() {
		return this.chest.numberOfItems() != 0;
	}
	
	public String getPlaceName () {
		return this.name;
	}
	
	private String name; 
	private boolean isSpaceShip;
	private String description;
	private ItemContainer chest;
	
	@Override
	public String getDescription() {
		return this.description;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public boolean isSpaceship (){
		return this.isSpaceShip;
	}


	@Override
	public int numberOfItems() {
		return this.chest.numberOfItems();
	}
	



}
