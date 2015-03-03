package tp.pr5.items;

import tp.pr5.NavigationModule;
import tp.pr5.RobotEngine;

public class CodeCard extends Item{
	
	/**
	 * Code card constructor
	 * @param id code card name
	 * @param description code card description
	 * @param code Secret code stored in the code card
	 */
	public CodeCard(String id, String description, String code) {
		super(id, description);
		this.code = code;
	}

	@Override
	/**
	 * A code card always can be used. Since the robot has the code card it never loses it
	 * @return true because it always can be used
	 */
	public boolean canBeUsed() {
		return true;
	}

	@Override
	/**
	 * The method to use a code card. If the robot is in a place which contains a street in the direction he is looking at, then the street is opened or closed if the street code and the card code match.
	 * @r the robot engine employed to use the card
	 * @p the place where the card is gonna be used
	 * @return true if the codecard can complete the action of opening or closing a street. False otherwise
	 */
	public boolean use(RobotEngine r, NavigationModule nav) {
		return nav.toggleHeadingStreet(this);
	}
	
	/**
	 * Gets the code stored in the code card
	 * @return a String that represents the code
	 */
	public String getCode() {
		return this.code;
	}
	

	
	private String code;



	@Override
	public boolean unUse(RobotEngine r, NavigationModule navMod) {
		return this.use(r, navMod); //dado que invierte el estado de la puerta, su unUse es el propio use
	}
}
