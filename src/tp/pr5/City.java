package tp.pr5;

public class City {
	/**
	 * Default constructor. Needed for testing purposes
	 */
	public City() {
		this.cityMap = null; 
	}
	/**
	 * Creates a city using an array of streets
	 * @param cityMap
	 */
	public City(Street[] cityMap) {
		this.cityMap = cityMap;
	}
	
	/**
	 * Looks for the street that starts from the given place in the given direction.
	 * @param currentPlace The place where to look for the street
	 * @param currentHeading The direction to look for the street
	 * @return the street that stars from the given place in the given direction. It returns null if there is not any street in this direction from the given place
	 */
	public Street lookForStreet(Place currentPlace, Direction currentHeading) {
		if (this.cityMap == null)
			return null;
		//recorremos el mapa buscando la calle adecuada
		for (Street s: this.cityMap) {
			if (s.comeOutFrom(currentPlace,currentHeading)) { //??? s != null??
				return s;
			}	
		}
	
		return null; 
	}
	
	/**
	 * Adds an street to the city
	 * @param street The street to be added
	 */
	public void addStreet(Street street) {
		if (street == null) 
			return; //Por si acaso
		
		//posibilidad de comprobar la existencia de la calle que se está añadiendo? (aunque va a funcionar igual)
		
		Street[] newCityMap = new Street[this.cityMap.length+1];
		for (int i = 0; i < this.cityMap.length; i++) {
			newCityMap[i] = this.cityMap[i];
		}
		newCityMap[this.cityMap.length] = street;
		this.cityMap = newCityMap;
	}
	
	
	private Street[] cityMap = new Street[0];
}
