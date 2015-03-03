package tp.pr5.items;

import tp.pr5.NavigationModule;
import tp.pr5.RobotEngine;
import tp.pr5.lang.Messages;

public class Garbage extends Item {	
	private boolean hasBeenUsed = false;
	private int recycledMaterial;

	/**
	 * 
	 * @param id item id
	 * @param description item desc
	 * @param recycledMaterial the amount of r.m. that the item generates
	 */
	public Garbage(String id, String description, int recycledMaterial) {
		super(id, description);
		this.recycledMaterial = recycledMaterial;
	}

	@Override
	/**
	 * @return true if the item has not been used yet
	 */
	public boolean canBeUsed() {
		return !hasBeenUsed;
	}

	@Override
	/**
	 * @return true if the garbage was transformed in recycled material
	 */
	public boolean use(RobotEngine r, NavigationModule navMod) {
		if (this.hasBeenUsed)
			return false;
		r.addRecycledMaterial(this.recycledMaterial);
		hasBeenUsed = true;
		return true;
	}
	
	public String toString() {
		return super.toString() + Messages.RECYCLED_MAT + this.recycledMaterial; 
	}

	@Override
	public boolean unUse(RobotEngine r, NavigationModule navMod) {
		if (!this.hasBeenUsed)
			return false;
		r.addRecycledMaterial(-this.recycledMaterial);
		hasBeenUsed = false;
		return true;
	}


}
