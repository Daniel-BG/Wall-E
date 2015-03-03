package tp.pr5.items;

import tp.pr5.NavigationModule;
import tp.pr5.RobotEngine;
import tp.pr5.lang.Messages;

public class Fuel extends Item {
	private int power;
	private int times;


	
	/**
	 * Fuel constructior
	 * @param id item id
	 * @param description item desc
	 * @param power amount of power energy this provides the robot
	 * @param times number of times the robot can use this
	 */
	public Fuel(String id, String description, int power, int times) {
		super(id, description);
		this.power = power;
		this.times = times;
	}

	@Override
	/**
	 * @return true if the item still can be used
	 */
	public boolean canBeUsed() {
		return this.times != 0;
	}

	@Override
	/**
	 * @r robot that is gonna use the fuel
	 * @p place where the fuel is gonna be used
	 * @return true if the fuel has been used
	 */
	public boolean use(RobotEngine r, NavigationModule navMod) {
		if (this.canBeUsed()) {
			times--;
			r.addFuel(power);
			return true;
		}
		return false;
	}


	public String toString() {
		return super.toString() + Messages.POWER + this.power + Messages.TIMES+ this.times; 
	}

	
	@Override
	public boolean unUse(RobotEngine r, NavigationModule navMod) {
		this.times++;
		r.addFuel(-this.power);
		return true;
	}

}
