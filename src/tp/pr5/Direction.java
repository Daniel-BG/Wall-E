package tp.pr5;

import tp.pr5.lang.Messages;

public enum Direction {
	EAST, NORTH, SOUTH, WEST, UNKNOWN;

	public Direction opposite() {
		switch(this) {
		case EAST: return WEST;
		case WEST: return EAST;
		case NORTH: return SOUTH;
		case SOUTH: return NORTH;
		default: return UNKNOWN;
		}
	}
	
	/**
	 * 
	 * @return la cadena que informa de la dirección a la que miramos
	 */
	public String sayDirection() {
		return Messages.DIRECTION_LOOKINGAT + this.toString();
	}
	
	/**
	 * 
	 * @return la cadena que informa de la dirección a la que nos vamos
	 */
	public String moveDirection() {
		return Messages.DIRECTION_MOVINGIN + this.toString();
	}
	
	/**
	 * 
	 * @param rotation
	 * @return resultado de aplicar la rotación rotation a la direccion desde la que se llama al método
	 */
	public Direction rightTurn() {
		switch (this) {
		case WEST:	return NORTH; 
		case NORTH:	return EAST; 
		case EAST:	return SOUTH;
		case SOUTH: return WEST;
		default: return UNKNOWN; 
		}
	}
	public Direction leftTurn() {
		return this.opposite().rightTurn();
	}

	
	

}
