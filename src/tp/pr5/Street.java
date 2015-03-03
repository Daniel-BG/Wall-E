package tp.pr5;

import tp.pr5.items.CodeCard;

public class Street {
	/**
	 * 
	 * @param source Inicio de la calle
	 * @param direction Dirección de la calle
	 * @param target Destino de la calle
	 */
	public Street (Place source, Direction direction, Place target){
		this.source = source;
		this.direction = direction;
		this.target = target;
	}
	
	/**
	 * Creates a street that has a code to open and close it
	 * @param source Source place
	 * @param direction Represents how is placed the target place with respect to the source place.
	 * @param target Target place
	 * @param isOpen Determines is the street is opened or closed
	 * @param code The code that opens and closes the street
	 */
	public Street (Place source, Direction direction, Place target, boolean isOpen, String code) {
		this.source = source;
		this.direction = direction;
		this.target = target;
		this.isOpen = isOpen;
		this.code = code;
	}
	
	/**
	 * 
	 * @param place
	 * @param whichDirection
	 * @return Returns true if the street comes out from the input Place.
	 */
	public boolean comeOutFrom (Place place, Direction whichDirection){
		return (this.source.equals(place) && this.direction.equals(whichDirection) || this.target.equals(place) && this.direction.equals(whichDirection.opposite()));
	}
	
	/**
	 * 
	 * @param whereAmI
	 * @return It returns the Place at the other side of the street. Returns null if whereAmI does not belong to the street.
	 */
	public Place nextPlace (Place whereAmI){
		if (this.source.equals(whereAmI))
			return this.target;
		else if (this.target.equals(whereAmI))
			return this.source;
		
		return null;
	}
	
	/**
	 * Checks if the street is open or closed
	 * @return true, if the street is open, and false when the street is closed
	 */
	public boolean isOpen() {
		return isOpen;
	}
	
	/**
	 * Tries to open a street using a code card. Codes must match in order to complete this action
	 * @param card A code card to open the street
	 * @return true if the action has been completed
	 */
	public boolean open(CodeCard card) {
		if (!this.code.equals(card.getCode())) 
			return false;	
		
		this.isOpen = true;
		return true;
	}
	
	/**
	 * Tries to close a street using a code card. Codes must match in order to complete this action
	 * @param card A code card to close the street
	 * @return true if the action has been completed
	 */
	public boolean close(CodeCard card) {
		if (!this.code.equals(card.getCode())) 
			return false;	
		
		this.isOpen = false;
		return true;
	}
	
	/**
	 * tries to toggle the current state of the steet gate
	 * @param card card to be used
	 * @return true if the gate was successfully closed
	 */
	public boolean toggle(CodeCard card) {
		if (this.isOpen())
			return this.close(card);
		else
			return this.open(card);
	}
	
	
	private Place source;
	private Direction direction;
	private Place target;
	private boolean isOpen = true;
	private String code = "";

}
