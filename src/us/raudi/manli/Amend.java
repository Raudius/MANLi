package us.raudi.manli;


import com.esotericsoftware.kryo.Kryo;

import us.raudi.manli.containers.Bundle;

/**
 * The Amend class holds the data that is sent to the Server.
 * Its ID is used to apply Amends in the correct order.
 * @see Bundle (superclass) for implementation details
 * @author Raul Ferreira Fuentes
 *
 */
public class Amend extends Bundle{
	// Amend id is determined by server to establish Amend order.
	protected int id = -1;
	protected int clientID = -1;
	
	
	public Amend() {}
	
	public static void register(Kryo k) {
		Bundle.register(k);
		k.register(Amend.class);
		k.register(String.class);
	}
	
	/**
	 * Returns the Amend id.
	 * @return Ammend's identifier
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Returns the client ID.
	 * @return ID of the client who sent the Amend
	 */
	public int getClientID() {
		return clientID;
	}
}
