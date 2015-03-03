package tp.pr5.cityLoader;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import tp.pr5.City;
import tp.pr5.Direction;
import tp.pr5.Place;
import tp.pr5.Street;
import tp.pr5.cityLoader.cityLoaderExceptions.WrongCityFormatException;
import tp.pr5.items.CodeCard;
import tp.pr5.items.Fuel;
import tp.pr5.items.Garbage;
import tp.pr5.items.Item;
import tp.pr5.lang.ErrorMessages;
import tp.pr5.lang.Messages;
import tp.pr5.util.InputStreamReader;

public class CityLoaderFromTxtFile {
	/**
	 * Creates the different array lists
	 */
	public CityLoaderFromTxtFile() {
		places = new ArrayList<Place>(); //TEst
		streets = new ArrayList<Street>();
	}
	
	/**
	 * Returns the place where the robot will start the simulation
	 * @return The initial place
	 */
	public Place getInitialPlace() {
		return this.places.get(0);
	}
	
	/**
	 * 
	 * @param file The input stream where the city is stored
	 * @return The city from the file
	 * @throws IOException  When there is some format error in the file (WrongCityFormatException) or some errors in IO operations.
	 */
	public City loadCity(InputStream file) throws IOException{
		this.fileReader = new InputStreamReader(file);
		
		this.error = false;
		
		if (CityLoaderFromTxtFile.CEDS_ON)
			System.out.println(Messages.CITY_COMPILATING);
		
		
		try {
			this.checkNextWord(BEGIN_CITY);
			this.getPlaces();
			this.getStreets();
			this.getItems();
			this.checkNextWord(END_CITY);
		} catch (IOException ex) {
			if (CityLoaderFromTxtFile.CEDS_ON) {
				System.err.println(ex.getMessage());
				System.err.println("Compilation ended");
			}
			throw ex;
		}
		
		if (CityLoaderFromTxtFile.CEDS_ON)
			if (error) 
				System.err.println(Messages.CITY_COMPILATION_FAIL);
			else 
				System.out.println(Messages.CITY_COMPILATION_OK);
				
		this.fileReader = null;
		
		Street [] s = new Street[0];
		s = streets.toArray(s);
		return new City(s);
	}
	
	
	/**
	 * Gets all places within the BEGIN_PLACES...END_PLACES block
	 * @param iOStream stream from which the places are got
	 * @throws IOException When a syntax error occurs or the file unexpectedly ends
	 */
	private void getPlaces() throws IOException {
		this.checkNextWord(BEGIN_PLACES);
		int contador = 0;
		String firstWord;
		boolean salir = (firstWord = this.readWordFromFile()).equals(END_PLACES);
		while (!salir) {
			if (!firstWord.equals(PLACE))
				syntaxError(PLACE);
			this.checkNextWord(Integer.toString(contador));
			
			
			String placeName = this.readWordFromFile();
			String placeDescription = this.readWordFromFile().replace('_', ' '); 
			String placeSpaceShip = this.readWordFromFile();
			boolean isSpaceShip = false;
			switch (placeSpaceShip) {
				case PLACE_SPACESHIP: isSpaceShip = true; break;
				case PLACE_NOSPACESHIP: isSpaceShip = false; break;
				default: fatalError(PLACE_SPACESHIP + " | " + PLACE_NOSPACESHIP);
			}
			salir = (firstWord = this.readWordFromFile()).equals(END_PLACES);
			contador++;
			
			this.places.add(new Place(placeName,isSpaceShip,placeDescription));
		}
	}
	/**
	 * Gets all streets within the BEGIN_STREETS...END_STREETS block
	 * @param iOStream stream from which the streets are got
	 * @throws IOException When a syntax error occurs, the file unexpectedly ends, or a street calls for a place which doesn't exist
	 */
	private void getStreets() throws IOException {
		this.checkNextWord(BEGIN_STREETS);
		int contador = 0;
		String firstWord;
		boolean salir = (firstWord = this.readWordFromFile()).equals(END_STREETS);
		while (!salir) {
			if (!firstWord.equals(STREET))
				syntaxError(STREET);
			this.checkNextWord(Integer.toString(contador));
			this.checkNextWord(PLACE);
			int placeInicial, placeFinal;
			if ((placeInicial = Integer.parseInt(this.readWordFromFile())) >= this.places.size()) 
				fatalError(ErrorMessages.WCFE_NUMBER_LESS + this.places.size());
			
			Direction direction = Direction.UNKNOWN;
			switch (this.readWordFromFile()) {
			case "north": direction = Direction.NORTH; break;
			case "south": direction = Direction.SOUTH; break;
			case "east": direction = Direction.EAST; break;
			case "west": direction = Direction.WEST; break;
			default: fatalError("north | south | east | west");
			}
			
			this.checkNextWord(PLACE);
			if ((placeFinal = Integer.parseInt(this.readWordFromFile())) >= this.places.size()) 
				fatalError(ErrorMessages.WCFE_NUMBER_LESS + this.places.size());
			
			boolean isOpen = false;
			String key = "";
			switch (this.readWordFromFile()) {
			case "open": isOpen = true; break;
			case "closed": isOpen = false; key = this.readWordFromFile(); break;
			default: fatalError("open | closed");
			}

			if (isOpen) 
				this.streets.add(new Street(this.places.get(placeInicial), direction, this.places.get(placeFinal)));
			else 
				this.streets.add(new Street(this.places.get(placeInicial), direction, this.places.get(placeFinal), isOpen, key));
			
			salir = (firstWord = this.readWordFromFile()).equals(END_STREETS);
			contador++;
		}
	}
	/**
	 * Gets all the items within the BEGIN_ITEMS...END_ITEMS block
	 * @param iOStream stream from which the items are got
	 * @throws IOException When a syntax error occurs, the file unexpectedly ends, or an item calls for a plce which doesn't exist
	 */
	private void getItems() throws IOException {
		this.checkNextWord(BEGIN_ITEMS);
		int contador = 0;
		String firstWord;
		boolean salir = (firstWord = this.readWordFromFile()).equals(END_ITEMS);
		while (!salir) {
			switch (firstWord) {
			case "codecard": break;
			case "fuel": break;
			case "garbage": break;
			default: fatalError("<item>");
			}
			this.checkNextWord(Integer.toString(contador));
			

			
			String itemName = this.readWordFromFile();
			//aquí iría la comprobación de las comillas
			String itemDescription = this.readWordFromFile().replace('_', ' '); 
			
			Item currentItem = null;
			switch (firstWord) {
			case "codecard": {
				currentItem = new CodeCard(itemName, itemDescription, this.readWordFromFile());
				break;
			}
			case "fuel": {
				try {
					currentItem = new Fuel(itemName, itemDescription, 
							Integer.parseInt(this.readWordFromFile()),
							Integer.parseInt(this.readWordFromFile()));
				} catch (NumberFormatException ex) {
					fatalError("<number>");
				}
				break;
			}
			case "garbage": {
				try {
					currentItem = new Garbage(itemName, itemDescription,
							Integer.parseInt(this.readWordFromFile()));
				} catch (NumberFormatException ex) {
					fatalError("<number>");
				}
				break;
			}
			default: fatalError("<item>");
			}
			this.checkNextWord(PLACE);
			int place;
			if ((place = Integer.parseInt(this.readWordFromFile())) >= this.places.size()) 
				fatalError(ErrorMessages.WCFE_NUMBER_LESS + this.places.size());
			
			this.places.get(place).addItem(currentItem);
			
			contador++;
			salir = (firstWord = this.readWordFromFile()).equals(END_ITEMS);
		}
	}
	
	/**
	 * Masks the InputStreamReader call to convert all native IOExceptions into WCFExceptions
	 * @return the fileReader read word
	 * @throws IOException
	 */
	private String readWordFromFile() throws IOException{
		try {
			return this.fileReader.nextWord();
		} catch (IOException e) {
			throw new WrongCityFormatException(e.getMessage());
		}
	}

	


	/**
	 * @throws IOException Always
	 */
	private void fatalError(String expected) throws WrongCityFormatException {
		if (!expected.equals(""))
			throw new WrongCityFormatException((ErrorMessages.WCFE_FATALERROR + fileReader.getLine() + "(" + fileReader.getColumn() + ") ''" + fileReader.getLastWordRead() + "''  [" + expected + "] " + ErrorMessages.WCFE_EXPECTED));
		throw new WrongCityFormatException((ErrorMessages.WCFE_FATALERROR + fileReader.getLine() + "(" + fileReader.getColumn() + ") ''" + fileReader.getLastWordRead() + "''"));
	}
	/**
	 * Error corregible por 
	 * @param expected
	 * @return
	 */
	private String syntaxError(String expected) throws WrongCityFormatException {
		if (!CityLoaderFromTxtFile.CEDS_ON) 
			throw new WrongCityFormatException(ErrorMessages.WCFE_CEDS_OFF);
		
			
		error = true;
		if (!expected.equals(""))
			System.err.println(ErrorMessages.WCFE_SYNTAXERROR + fileReader.getLine() + "(" + fileReader.getColumn() + ") ''" + fileReader.getLastWordRead() + "''  [" + expected + "] " + ErrorMessages.WCFE_EXPECTED);
		else 
			System.err.println(ErrorMessages.WCFE_SYNTAXERROR + fileReader.getLine() + "(" + fileReader.getColumn() + ") ''" + fileReader.getLastWordRead() + "''");
		return expected; //por si acaso en un futuro para corregir falta de parámetros
	}
	
	
	/**
	 * Checks that the next word in iOStream is equal to the one sent as "expected"
	 * @param iOStream stream from where to read the word
	 * @param expected expected word to be read
	 * @throws IOException when getWord would throw an exception or when the word does not match the expected one
	 */
	private void checkNextWord (String expected) throws IOException {
		if (!this.readWordFromFile().equals(expected)) 
			syntaxError(expected);
	}
	
	

	ArrayList<Place> places;
	ArrayList<Street> streets;
	private boolean error;
	//change to true to enable the auto-correcting syntax system
	private static final boolean CEDS_ON = false;
	private InputStreamReader fileReader;
	
	private static final String BEGIN_CITY = "BeginCity";
	private static final String END_CITY = "EndCity";
	private static final String BEGIN_PLACES = "BeginPlaces";
	private static final String END_PLACES = "EndPlaces";
	private static final String PLACE = "place";
	private static final String PLACE_SPACESHIP = "spaceShip";
	private static final String PLACE_NOSPACESHIP = "noSpaceShip";
	private static final String BEGIN_STREETS = "BeginStreets";
	private static final String END_STREETS = "EndStreets";
	private static final String STREET = "street";
	private static final String BEGIN_ITEMS = "BeginItems";
	private static final String END_ITEMS = "EndItems";
		
	
}
