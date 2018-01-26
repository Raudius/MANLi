package us.raudi.manli;

import java.io.IOException;
import java.net.InetAddress;
import java.util.List;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import us.raudi.manli.containers.Bundle;
import us.raudi.manli.examples.SimpleQuiz;
import us.raudi.manli.netutils.PortFinder;

/**
 * MANLi Client.
 * Connects to a MANLi-server from which it also receives a version of the model. 
 * It can then send Amends to this model as well as continuously receive Amends made by other clients.
 * @author Raul Ferreira Fuentes
 *
 */
public class ManliClient {
	private static final int DISCOVER_TIMEOUT = 3000;
	
	private AmendBuffer buffer = new AmendBuffer();
	
	private Client client = new Client();
	private Model model;
	private InetAddress host;
	private int host_port;
	
	public ManliClient(InetAddress host) {
		this(host, ManliServer.TCP_PORT_DEFAULT);
	}
	
	public ManliClient(String host) throws IOException {
		this(host, ManliServer.TCP_PORT_DEFAULT);
	}
	
	public ManliClient(String host, int port) throws IOException {
		this(InetAddress.getByName(host), port);
	}
	
	/**
	 * Starts an instance of the Client. 
	 * (NOTE: It does not connect to the server until the start method is called)
	 * @param host address of the host
	 * @param port port of the host
	 */
	public ManliClient(InetAddress host, int port) {
		this.host = host;
		this.host_port = port;
	}
	
	/**
	 * Starts the client and initiates a connection with the server specified in the constructor.
	 * @param mClass Class of the model expected to receive from the server.
	 * @throws InstantiationException if the model class was not possible to instantiate
	 * @throws IllegalAccessException if class to be instantiated is not accessible
	 * @throws IOException if connection times out or kryo client cannot be accessed
	 */
	public void connect(Class<?> mClass) throws InstantiationException, IllegalAccessException, IOException {
		if(!Model.class.isAssignableFrom(mClass))
			throw new UnsupportedOperationException("ManliClient.start expects a class that implements the Model interface");
		
		// register kryo serializers
		Model m = (Model) mClass.newInstance();
		Kryo kryo = client.getKryo();
		m.register(kryo);
		Amend.register(kryo);
		
		// initiate client and connect to host
		client.start();
		client.connect(PortFinder.findUniqueFreePort(), host, host_port, ManliServer.UDP_PORT);
		
		
		// Connection Handling
		client.addListener(new Listener() {
			public void received(Connection connection, Object object) {
				if(object instanceof Model) {
					model = (Model) object;
				}
				else if(object instanceof Amend) {
					Amend a = (Amend) object;
					a.clientID = connection.getID();
					handleAmend((Amend) object);
				}
			}

		});
		
	}
	
	

	
	/**
	 * Disconnects from the server.
	 */
	public void disconnect() {
		client.stop();
	}


	/**
	 * Sends an amend to the server.
	 * @param a Amend to be sent
	 */
	public void sendAmend(Amend a) {
		client.sendTCP(a);
	}

	/**
	 * Returns the model object that is being updated.
	 * @return the instance of the model maintained by the client.
	 */
	public Model getModel() {
		return model;
	}
	
	// Checks if received ammend has to be applied, if not it saves it in a queue.
	// If an amend is applied it also checks if it can empty the queue and applies any consecutive amends.
	private void handleAmend(Amend a) {
		buffer.insert(a);

		while(!buffer.isEmpty() && buffer.top().id == model.amend_cnt) {
			Amend valid = buffer.dequeue();
			model.update(valid);
			model.amend_cnt++;
		}
	}	


	/**
	 * Tries to discover a host in LAN through UDP broadcasting.
	 * @return the first host to reply in LAN
	 */
	public static InetAddress findLocalServer() {
		return new Client().discoverHost(ManliServer.UDP_PORT, DISCOVER_TIMEOUT);
	}


	/**
	 * Tries to discover hosts in LAN through UDP broadcasting.
	 * @return A list of hosts available in LAN.
	 */
	public static List<InetAddress> findLocalServers() {
		return new Client().discoverHosts(ManliServer.UDP_PORT, DISCOVER_TIMEOUT);
	}
}
