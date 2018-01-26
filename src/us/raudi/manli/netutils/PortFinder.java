package us.raudi.manli.netutils;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashSet;


/**
 * Static class used to find available ports in the network adapter.
 * @author Raul Ferreira Fuentes
 *
 */
public class PortFinder {

	private static HashSet<Integer> used = new HashSet<Integer>();

	
	/**
	 * Finds a free port in the network adapter.
	 * @return an available port
	 * @throws IOException if no socket can be opened
	 */
	public static int findFreePort() throws IOException {
	    ServerSocket socket = new ServerSocket(0);
	    try {
	        return socket.getLocalPort();
	    } finally {
	        try {
	            socket.close();
	        } catch (IOException e) {
	        }
	    }
	}
	
	
	/**
	 * Find multiple open port without needing to use them.
	 * Unlike findFreePort() this method will never return the same port twice.
	 * @return A unique available port
	 * @throws IOException if no socket can be opened
	 */
	public static int findUniqueFreePort() throws IOException {
	    int port;
	    do {
	        port = findFreePort();
	    } while (used.contains(port));
	    
	    used.add(port);
	    return port;
	}
	
	/**
	 * Resets the ports for the findUniquePort method such that the method can now
	 * return previously returned ports.
	 */
	public static void clearUniquePorts() {
		used.clear();
		
	}

}