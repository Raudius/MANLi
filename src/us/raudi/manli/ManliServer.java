package us.raudi.manli;

import java.io.IOException;
import java.util.Observable;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.FrameworkMessage;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import us.raudi.manli.examples.SimpleQuiz;



public class ManliServer extends Observable implements Runnable {

	public static final int TCP_PORT_DEFAULT = 57343;
	public static final int UDP_PORT = 57342;
	
	private static final long INTERVAL_UPDATE = 1000;
	private Server server = new Server();
	private int port;
	private Model model;
	
	
	public ManliServer(Model model) {
		this(model, TCP_PORT_DEFAULT);
	}
	
	public ManliServer(Model model, int port) {
		this.model = model;
		this.port = port;
	}
	
	public void init() throws IOException {
		server.start(); // initiate server

		addListeners();

		// register objects for serialization
		Kryo kryo = server.getKryo();
		model.register(kryo);
		Amend.register(kryo);
		
		
		server.bind(port, UDP_PORT); // bind ports for server
	}
	

	private void broadcastAmend(Amend a) {
		server.sendToAllTCP(a);
	}
	
	
	public int getPort() {
		return port;
	}
	
	
	@Override
	public void run() {
		try {
			init();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		while(true) {
			try {
				Thread.sleep(INTERVAL_UPDATE);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			//System.out.println(port);
			//System.out.println(model);
		}	
	}
	
	/* Creates listeners to deal with incomming connections */
	private void addListeners() {
		// server listener
		server.addListener(new Listener() {
			public void received(Connection connection, Object obj) {
				if(obj instanceof FrameworkMessage) {
					// received framewowrk message (Kryonet.FrameworkMessage) (keep-alives etc.)
					// TODO: Action required (??)
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
				System.out.println("CLIENT CONNECTED");
				model.addClient(connection);
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				server.sendToTCP(connection.getID(), model);
			}
			
			public void disconnected (Connection connection) {
				model.removeClient(connection);
			}
		});
	}
	
	public static void main(String[] args) {
		Thread t = new Thread(new ManliServer( new SimpleQuiz() ));
		t.start();
	}
}
