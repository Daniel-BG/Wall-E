package tp.pr5.items;

import tp.pr5.NavigationModule;
import tp.pr5.RobotEngine;
import tp.pr5.util.Comparable;

public abstract class Item implements Comparable{
	/**
	 * Builds an item from a given identifier and description
	 * @param id item identifier
	 * @param description item description
	 */
	public Item(String id, String description) {
		this.id = id;
		this.description = description;
	}
	/**
	 * Checks if the item can be used. Subclasses must override this method
	 * @return true if the item can be used
	 */
	public abstract boolean canBeUsed();
	/**
	 * Return the item identifier
	 * @return the item identifier
	 */
	public String getId() {
		return this.id;
	}
	/**
	 * Generates a String with the Item description
	 */
	public String toString() {
		return this.id + ": " + this.description;
	}

	/**
	 * Try to use the item with a robot in a given place. It returns whether the action was completed. Subclasses must override this method
	 * @param r The robot that uses the item
	 * @param p The Place where the item is used
	 * @return true if the action was completed. False otherwise.
	 */
	public abstract boolean use(RobotEngine r, NavigationModule navMod);
	
	/**
	 * Try to unuse the item with a robot in a given place. It returns whether the action was completed. Subclasses must override this method
	 * @param r The robot that uses the item
	 * @param p The Place where the item is used
	 * @return true if the action was completed. False otherwise.
	 */
	public boolean unUse(RobotEngine r, NavigationModule navMod) {
		return false; //no puede ser abstracto para pasar los testes
	}
	
	public int CompareTo(Comparable i) {
		//hacemos el lowercase pues si no, los items que empiezan por mayús van siempre antes y queremos ordenarlos alfabéticamente ignorando eso
		return id.toLowerCase().compareTo(((Item) i).getId().toLowerCase());
	}
	

	
	private String id;
	private String description;

	
}
