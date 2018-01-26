package us.raudi.manli;

import java.io.IOException;
import java.util.Observable;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.FrameworkMessage;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;



/**
 * MANLi server.
 * Maintains an instance of a Model and synchronises it across multiple clients.
 * @author Raul Ferreira Fuentes
 *
 */
public class ManliServer extends Observable{

	public static final int TCP_PORT_DEFAULT = 57343;
	public static final int UDP_PORT = 57342;
	
	private Server server = new Server();
	private int port;
	private Model model;
	
	
	/**
	 * Starts a server on port: ManliServer.TCP_PORT_DEFAULT
	 * @param model instance of model to be updated by clients
	 */
	public ManliServer(Model model) {
		this(model, TCP_PORT_DEFAULT);
	}
	
	/**
	 * Starts a server on the specified port.
	 * @param model instance of model to be updated by clients
	 * @param port TCP port in which the server will accept client requests.
	 */
	public ManliServer(Model model, int port) {
		this.model = model;
		this.port = port;
	}
	
	
	/**
	 * Starts the server and listens for incomming connectons.
	 */
	public void start() {
		server.start();

		// define how to handle connections
		addListeners();

		// register objects for serialization
		Kryo kryo = server.getKryo();
		model.register(kryo);
		Amend.register(kryo);
		
		
		try {
			server.bind(port, UDP_PORT);
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	/**
	 * Stops the server and disconects all clients.
	 */
	public void stop() {
		server.stop();
	}
	

	private void broadcastAmend(Amend a) {
		server.sendToAllTCP(a);
	}
	
	/**
	 * Port in which the server is listening for TCP connections.
	 * @return TCP port of the server.
	 */
	public int getPort() {
		return port;
	}
	
	
	
	/* Creates listeners to deal with incoming connections */
	private void addListeners() {
		// server listener
		server.addListener(new Listener() {
			public void received(Connection connection, Object obj) {
				if(obj instanceof FrameworkMessage) {
					// received framewowrk message (Kryonet.FrameworkMessage) (keep-alives etc.)
				}
				else if ((obj instanceof Amend)) {
					boolean changed = model.amend(connection, (Amend) obj);

					if(changed) {
						setChanged();
						notifyObservers();

						broadcastAmend((Amend) obj);
					}
				}
				else {
					System.err.println("Server received non-(Amend) object. " + obj.getClass());
				}
			}


			public void connected(Connection connection) {
				model.addClient(connection);
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				server.sendToTCP(connection.getID(), model);
			}
			
			public void disconnected (Connection connection) {
				model.removeClient(connection);
			}
		});
	}
}
