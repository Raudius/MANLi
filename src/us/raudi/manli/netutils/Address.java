package us.raudi.manli.netutils;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * THIS CLASS IS USED IN CONJUCTION WITH AddressEncoder BUT IS NOT YET IMPLEMENTED ANYWHERE
 * ELSE IN THE LIBRARY.
 * 
 * Addres:Port pair
 * @author Raul Ferreira Fuentes
 *
 */
public class Address {
	private InetAddress ip;
	private int port;
	
	public Address(String ip, int port) {
		try {
			this.ip = InetAddress.getByName( ip );
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		this.port = port;
	}

	public int getPort() {
		return port;
	}
	
	public void setPort(int port) {
		this.port = port;
	}

	public InetAddress getIp() {
		return ip;
	}

	public void setIp(InetAddress ip) {
		this.ip = ip;
	}
	
	@Override
	public String toString() {
		return ip.getHostAddress() + " : " + port;
	}

}
