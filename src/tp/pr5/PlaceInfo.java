package tp.pr5;

public interface PlaceInfo {
	/**
	 * Return the place description
	 * @return
	 */
	public String getDescription();
	/**
	 * Return the place name
	 * @return
	 */
	public String getName();
	/**
	 * Is this place the space ship?
	 * @return
	 */
	public boolean isSpaceship();
	/**
	 * Return the number of items the place has
	 * @return
	 */
	public int numberOfItems();
}
