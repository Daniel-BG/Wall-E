package tp.pr5.util;

/**
 * Double linked list
 *
 * @param <T> Type stored on the list
 */
public class DoubleLinkedStack <T> {
	private Node<T> current = new Node<T>(null, null, null); //Starting phantom node
	
	/**
	 * Adds an element to the double linked list
	 * @param elem element to add
	 */
	public void add (T elem) {
		if (this.current != null) {
			this.current.nextElement = new Node<T>(this.current, elem, null);
			this.current = this.current.nextElement;
		}
		else
			this.current = new Node<T>(null, elem, null);
	}
	
	/**
	 * Removes and returns the last element on the list
	 * @return the last element on the list
	 * @throws IndexOutOfBoundsException if the list is empty
	 */
	public T removeLast() throws IndexOutOfBoundsException{
		if (!this.current.hasPrevious())
			throw new IndexOutOfBoundsException("An empty list cannot return an element");
		this.current = this.current.prevElement;
		return this.current.nextElement.element;
	}
	
	/**
	 * Adds again and returns the last element that was removed, if it existed <br>
	 * This function recovers the last state of the list, meaning that using it again will return the element that was removed two times ago
	 * @return the last element that was removed. 
	 */
	public T recoverLast() throws IndexOutOfBoundsException{
		if (!this.current.hasNext())
			throw new IndexOutOfBoundsException("Cannot access unexisting elements");
		this.current = this.current.nextElement;
		return this.current.element;
	}
	
	/**
	 * The list is considered empty if the phantom node is the current node. Remember that isEmpty() might return true but isLastIndexNull false at the same time
	 * @return true if the list is empty 
	 */
	public boolean isEmpty() {
		return !this.current.hasPrevious();
	}
	
	/**
	 * 
	 * @return true if the last index is null. That is, if recoverLast would throw an exception
	 */
	public boolean isLastIndexNull() {
		return !this.current.hasNext();
	}
	
	
	
	/**
	 * Node used by the list
	 *
	 * @param <C> Element the Node stores
	 */
	private class Node<C>{
		protected C element;					//element stored
		protected Node<C> nextElement = null;	//next node pointer
		protected Node<C> prevElement = null;	//previous node pointer
		
		/**
		 * Constructor for the node. It is mandatory to set the previous and next element as well as the element stored. <br>
		 * All of them can be null
		 * @param previous
		 * @param element
		 * @param next
		 */
		public Node(Node<C> previous, C element, Node<C> next) {
			this.prevElement = previous;
			this.element = element;
			this.nextElement = next;
		}
		
		/**
		 * 
		 * @return true if nextElement is not empty
		 */
		public boolean hasNext() {
			return this.nextElement != null;
		}
		
		/**
		 * 
		 * @return true if prevElement is not empty
		 */
		public boolean hasPrevious() {
			return this.prevElement != null;
		}
	}
}
