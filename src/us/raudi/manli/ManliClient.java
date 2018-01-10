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

public class ManliClient {
	private static final int DISCOVER_TIMEOUT = 3000;
	
	private AmendBuffer buffer = new AmendBuffer();
	
	Client client = new Client();
	private Amend amend;
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
	
	public ManliClient(InetAddress host, int port) {
		this.amend = new Amend();
		this.host = host;
		this.host_port = port;
	}
	
	/**
	 * Starts the client.
	 * @param c
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IOException
	 */
	public void start(Class<?> c) throws InstantiationException, IllegalAccessException, IOException {
		// first register kryo serializers
		Model m = (Model) c.newInstance();
		Kryo kryo = client.getKryo();
		m.register(kryo);
		Amend.register(kryo);
		
		// initiate client (connect to host);
		client.start();
		client.connect(PortFinder.findUniqueFreePort(), host, host_port, ManliServer.UDP_PORT);
		
		
		// Input Handling
		client.addListener(new Listener() {
			public void received(Connection connection, Object object) {
				if(object instanceof Model) {
					System.out.println("Model Received!");
					model = (Model) object;
				}
				else if(object instanceof Amend) {
					Amend a = (Amend) object;
					a.clientID = connection.getID();
					handleAmend((Amend) object);
				}
			}

		});
		
		if(!Model.class.isAssignableFrom(c)) {
			throw new UnsupportedOperationException("ManliClient.start() expects a class that implements the QuizModel interface");
		}
		
		// initial amend to register with server
		// TODO: necessary?
		//// sendAmend();
	}
	
	

	private void handleAmend(Amend a) {
		buffer.insert(a);
		System.out.println(model.expectedAmendId());
		System.out.println(buffer);
		while(!buffer.isEmpty() && buffer.top().id == model.expectedAmendId()) {
			Amend valid = buffer.dequeue();
			model.update(valid);
			model.increaseAmendCounter();
		}
	}
	
	
	public void disconnect() {
		client.stop();
	}

	// find a local server
	public static InetAddress findLocalServer() {
		return new Client().discoverHost(ManliServer.UDP_PORT, DISCOVER_TIMEOUT);
	}
	// find a list of local servers
	public static List<InetAddress> findLocalServers() {
		return new Client().discoverHosts(ManliServer.UDP_PORT, DISCOVER_TIMEOUT);
	}
	
	public void sendAmend(Amend a) {
		client.sendTCP(a);
	}
	
	
	public Model getModel() {
		return model;
	}
	
	public Bundle getBundle() {
		return amend;
	}
	
	
	

	public static void main(String[] args) throws IOException, InstantiationException, IllegalAccessException, InterruptedException {
		
		ManliClient c = new ManliClient("192.168.178.41");
		System.out.println( ManliClient.findLocalServers().size() );
		c.start(SimpleQuiz.class);
		
		
		while(true) {
			Thread.sleep(5000);
			Amend a = new Amend();
			a.putBool("buzz", true);
			c.sendAmend(a);
		}
	}
}
