package us.raudi.manli;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
/**
 * Defines the Model contract.
 * @author Raudius
 *
 */
public abstract class Model {
	private int amend_cnt = 0;
	
	protected boolean amend(Connection con, Amend data) {
		int[] ids = {1,2,0,3,5,4,10,12,6,8,7,11,9};
		data.id = amend_cnt++; 
		data.clientID = con.getID();
		return update(data);
	}
	
	/**
	 * Update the model with data from a client.
	 * @return Whether the update meant a change on the model.
	 */
	public abstract boolean update(Amend data);

	public abstract void addClient(Connection con);

	public abstract void removeClient(Connection con);
	
	/**
	 * Register all referenced objects for Kryo serialization.
	 */
	public void register(Kryo k) {
		k.register(Model.class);
	}

	public int expectedAmendId() {
		return amend_cnt;
	}

	public void increaseAmendCounter() {
		amend_cnt++;
	}

	
}
