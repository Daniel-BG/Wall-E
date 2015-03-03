package tp.pr5.observers;

import java.util.List;

import tp.pr5.items.Item;

public interface ItemContainerObserver {
	/**
	 * Notifies that the container has changed
	 * @param inventory
	 */
	public void inventoryChange(List<Item> inventory);
	/**
	 * Notifies that the user requests a SCAN instruction over the inventory.
	 * @param inventoryDescription
	 */
	public void inventoryScanned(String inventoryDescription);
	/**
	 * Notifies that an item is empty and it will be removed from the container.
	 * @param itemName
	 */
	public void itemEmpty(String itemName);
	/**
	 * Notifies that the user wants to scan an item allocated in the inventory
	 * @param description
	 */
	public void itemScanned(String description);
	/**
	 * Notifies when an item is added
	 * @param itemName
	 */
	public void itemAdded(String itemName);
	/**
	 * Notifies when an item is removed
	 * @param itemName
	 */
	public void itemRemoved(String itemName);
}
