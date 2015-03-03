package tp.pr5.util;

import java.util.List;
import java.util.Vector;

import tp.pr5.items.Item;



/**
 * Useful vector to use with comparable classes
 * Saves the elements ordered by their compareTo function
 * 
 * @author Daniel Báscones García
 * @author David Roldán Santos
 * @see Comparable.java
 * @version 2.0
 */

public class Vec_thor <T extends Comparable>{
	/**
	 * Default constructor.
	 */
	public Vec_thor() {}
	/**
	 * Constructor where the default (initial and reset) size is specified
	 * @param initialSize size of the vector when constructed
	 */
	public Vec_thor(int defaultSize) {
		this.defaultSize = defaultSize;
		this.vector = new Comparable[defaultSize];
	}
	
	
	/**
	 * Looks for a comparable c in the array
	 * @param c comparable
	 * @return position of c if it was found, position where c should go if it wasn't found
	 */
	private int search(T c){
		int inic = 0;
		int fin = currentElements - 1;
		int pos;
		
		while (inic <= fin) {
			pos = (inic+fin) / 2;
			if (vector[pos].CompareTo(c) >= 0){
				fin = pos - 1;
			} else {
				inic = pos + 1;
			}
		}
		
		return inic;
	}
	/**
	 * doubles the size of the array
	 * @return true if it was doubled, false otherwise
	 */
	private boolean doubleSize() {
		Comparable[] auxVector = this.vector;
		this.vector = new Comparable[this.vector.length*2];
		
		for (int i = 0; i < this.currentElements; i++)
			vector[i] = auxVector[i];
		
		return true; //devolveria false si no hubiera memoria pero suponemos que esta accion siempre se va a poder hacer y que vamos a disponer de memoria para ello
	}
	/**
	 * halves the size of the array
	 * @return true if the operation was successfully completed
	 */
	private boolean halveSize() {
		Comparable[] auxVector = this.vector;
		this.vector = new Comparable[this.vector.length/2];
		
		for (int i = 0; i < this.currentElements; i++)
			vector[i] = auxVector[i];
		
		return true; //devolveria false si no hubiera memoria pero suponemos que esta accion siempre se va a poder hacer y que vamos a disponer de memoria para ello
	}
	/**
	 * Inserts the comparable "c" in the position "a"
	 * @param a position
	 * @param c comparable
	 * @return true if c was inserted
	 */
	private boolean insert(int a, T c) {
		if (this.currentElements == this.vector.length)
			if (!this.doubleSize())
				return false;
		
		for (int i = currentElements; i > a; i--)
			vector[i] = vector[i-1];
		
		vector[a] = c;
		this.currentElements++;
		return true;
	}
	/**
	 * deletes the element at position i
	 * @param i position
	 * @return true if the element was deleted
	 */
	private boolean delete(int pos) {
		for (int i = pos; i < this.currentElements-1; i++)
			this.vector[i] = this.vector[i+1];
		this.currentElements--;
		
		if (this.currentElements < this.vector.length/2)
			halveSize();
		
		return true;
	}
	
	
	/**
	 * Adds "a" to the list, keeping it sorted by "compareTo". "a" is added in the first position it can go
	 * @param a Element to add
	 * @return True if the element was successfully added
	 */
	public boolean add(T a) {
		int resBus = search(a);
		
		if (resBus < this.currentElements && vector[search(a)].CompareTo(a) == 0) return false;
		else return this.insert(resBus, a);
	}
	/**
	 * 
	 * @param i Index of the element
	 * @return the element at the specified position of the list, null if said position is out of range
	 */
	@SuppressWarnings("unchecked") //al entrar solo cosas de tipo T al array, no va a dar problemasn (insert sólo recibe Ts)
	public T get(int i) {
		if (i >= this.currentElements) return null;
		else return (T) vector[i];
	}
	/**
	 * 
	 * @return the size of the vector
	 */
	public int size() {
		return this.currentElements;
	}
	/**
	 * Gets the current capacity of the array
	 * @return current capacity of the array
	 */
	public int capacity() {
		return this.vector.length;
	}	
	/**
	 * Removes, if present, the item a
	 * @param a item to be removed
	 * @return true if "a" was found and removed
	 */
	public boolean remove(T a) {
		int resBus = search(a);
		if (vector[resBus].CompareTo(a) == 0)
			return remove(resBus);
		else return false;
	}
	/**
	 * Removes, if not outOfRange, the element at index i
	 * @param i index of object to be removed
	 * @return true if the index was !outOfRange and the object was removed
	 */
	public boolean remove(int i) {
		if (i >= this.currentElements) return false;
		else return delete(i);
	}
	/**
	 * Clears all the elements in the vector
	 */
	public void clear() {
		this.vector = new Comparable[this.defaultSize];
		currentElements = 0;
	}
	/**
	 * Looks for a specific object in the vector
	 * @param a object to look for
	 * @return true if it was found
	 */
	public boolean contains(T a) {
		return this.vector[search(a)].CompareTo(a) == 0;
	}

	/**
	 * Makes the vec_thor into a list
	 * @return
	 */
	public List<Item> toList() {
		List<Item> lista = new Vector<Item>();
		for (int i = 0; i < this.currentElements; i++)
			lista.add((Item)vector[i]);
		return lista;
	}

	
	
	private int defaultSize = 2;
	private int currentElements = 0;
	private Comparable[] vector = new Comparable[defaultSize];	
}
