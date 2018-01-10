package us.raudi.manli;


import com.esotericsoftware.kryo.Kryo;

import us.raudi.manli.containers.Bundle;

public class Amend extends Bundle{
	// Amend id is determined by server to establish Amend order.
	protected Integer id = -1;
	protected int clientID = -1;
	
	public Amend() {}
		
	public static void register(Kryo k) {
		Bundle.register(k);
		k.register(Amend.class);
		k.register(String.class);
	}
	
	public int getId() {
		return id;
	}
	
	@Override
	public String toString() {
		return "" + id;
	}

	public int getClientID() {
		return clientID;
	}
}
