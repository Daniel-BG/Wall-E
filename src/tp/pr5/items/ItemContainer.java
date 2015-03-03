package tp.pr5.items;

import tp.pr5.Interpreter;
import tp.pr5.lang.Messages;
import tp.pr5.observers.ItemContainerObserver;
import tp.pr5.util.Observable;
import tp.pr5.util.Vec_thor;



public class ItemContainer extends Observable<ItemContainerObserver>{
	private Vec_thor<Item> items;	
	
	
	/**
	 * Creates the empty container
	 */
	public ItemContainer() {
		items = new Vec_thor<Item>();
	}
	
	
	/**
	 * Add an item to the container. If failure, return false
	 * @param item the name of the item to be added
	 * @return true if and only the item is added (there are no itmes with the same name in the container)
	 */
	public boolean addItem(Item item) {
		if (this.items.add(item)) {
			this.onInventoryChange();
			return true;
		} else
			return false;
	}
	/**
	 * returns the item from the container with id = id
	 * @param id item name
	 * @return item with that name or NULL
	 */
	public Item getItem(String id) {
		for (int i = 0; i < this.items.size(); i++)
			if ((this.items.get(i)).getId().equalsIgnoreCase(id))
				return this.items.get(i);

		return null;
	}
	/**
	 * Returns and deletes an item from the inventory. This operation can fail, returning null
	 * @param id Name of the item
	 * @return An item if and only if the item identified by id exists in the inventory. Otherwise it returns null
	 */
	public Item pickItem(String id) {
		Item item = getItem(id);
		if (item != null) {
			items.remove(item);
			this.onInventoryChange();
		}
		return item;
	}
	/**
	 * Gets the ID of the item in the index position. <br>
	 * Used by the solver
	 * @param index
	 * @return
	 */
	public String getId(int index) {
		if (index >= this.items.size())
			return null;
		else 
			return this.items.get(index).getId();
	}
	
	/**
	 * Generates a String with information about the items contained in the container. Note that the items must appear sorted but the item name.
	 * Returns an empty String "" if the container had no objects
	 */
	public String toString() {
		
		String str = "";
		for (int i = 0; i < this.items.size(); i++)  {
			str = str + "   " + (this.items.get(i)).getId();
			if (i != this.items.size() - 1) str = str + Interpreter.LINE_SEPARATOR; //añadimos line separator menos en la última
		}

		return str;
	}
	/**
	 * @returns the number of items in the container
	 */
	public int numberOfItems() {
		return this.items.size();
	}
	/**
	 * For testing purposes
	 * @return Whether the container has or not an item
	 */
	public boolean containsItem(String item) {
		return this.getItem(item) != null;
	}
	
	/**
	 * Method called by the OperateInstruction when an item stored in the collection is successfully used.
	 * @param it
	 */
	public void useItem(Item it) {
		if (!it.canBeUsed())
			this.onItemEmpty(it.getId());	
	}
	/**
	 * Requests this to be scanned
	 */
	public void requestScanCollection() {
		this.onInventoryScanned();
	}
	/**
	 * Requests the item with id id to be scanned
	 * @param id
	 */
	public void requestScanItem(String id) {
		this.onItemScanned(this.getItem(id).toString());
	}
	
	
	/**
	 * Called when inventory changes
	 */
	private void onInventoryChange() {
		for (int i = 0; i < this.observers.size(); i++)
			this.observers.get(i).inventoryChange(this.items.toList());
	}
	/**
	 * Called when scanned
	 */
	private void onInventoryScanned() {
		String res = "";
		if (this.toString().equals("")) 
			res = Messages.OUTPUT_SCAN_NONE;
		else 
			res = Messages.OUTPUT_SCAN_ALL + "\n" + this.toString();
		
		for (int i = 0; i < this.observers.size(); i++)
			this.observers.get(i).inventoryScanned(res);
	}
	/**
	 * Called when an item is removed
	 * @param itemName
	 */
	private void onItemEmpty(String itemName) {
		for (int i = 0; i < this.observers.size(); i++)
			this.observers.get(i).itemEmpty(itemName);
	}
	/**
	 * Called when an item is scanned
	 * @param description
	 */
	private void onItemScanned(String description) {
		for (int i = 0; i < this.observers.size(); i++)
			this.observers.get(i).itemScanned(description);
	}

	/**
	 * Returns a vecthor representation of the item container
	 * @return
	 */
	public Vec_thor<Item> toVec_thor() {
		return this.items;
	}
}
