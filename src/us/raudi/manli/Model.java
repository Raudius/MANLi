package us.raudi.manli;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
/**
 * Defines the Model contract.
 * @author Raul Ferreira Fuentes
 *
 */
public abstract class Model {
	protected int amend_cnt = 0;
	
	/**
	 * Update the model with data from a client.
	 * If the Amend was accepted and it had a meaningful change to the model update()
	 * returns TRUE and the server will broadcast the Amend to the clients.
	 * @return Whether the update changed the model.
	 */
	public abstract boolean update(Amend data);

	/**
	 * Notifies the Model that a new client has connected.
	 * @param con Connection to the client
	 */
	public abstract void addClient(Connection con);

	/**
	 * Notifies the Model that a client has disconnected.
	 * @param con Connection to the client that has disconnected
	 */
	public abstract void removeClient(Connection con);
	
	/**
	 * Register all field objects for Kryo serialization.
	 */
	public void register(Kryo k) {
		k.register(Model.class);
	}

	
	// The following methods are used for Amend synchronisation
	
	protected boolean amend(Connection con, Amend data) {
		data.id = amend_cnt++; 
		data.clientID = con.getID();
		return update(data);
	}
}
