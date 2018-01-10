package us.raudi.manli;

import java.util.LinkedList;

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
	
	@Override
	public String toString() {
		return buffer.toString();
	}
}
