package us.raudi.manli;

import java.util.LinkedList;

/**
 * Priority queue implementation used to keep a buffer of the out-of-sync Amends.
 * Lower-IDs are sent to the front of the Queue.
 * 
 * @author Raul Ferreira Fuentes
 *
 */
public class AmendBuffer {
	private LinkedList<Amend> buffer = new LinkedList<>();

	public void insert(Amend a) {
		int idx = buffer.size() -1;
		
		while(idx >= 0 && buffer.get(idx).id > a.id)
			idx--;
		
		
		buffer.add(idx+1, a);
	}

	public Amend top() {
		if(buffer.isEmpty()) return null;
		return buffer.peek();
	}

	public Amend dequeue() {
		return buffer.removeFirst();
	}

	public boolean isEmpty() {
		return buffer.isEmpty();
	}
}
