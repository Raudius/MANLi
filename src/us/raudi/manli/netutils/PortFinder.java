package us.raudi.manli.netutils;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashSet;

public class PortFinder {

	private static HashSet<Integer> used = new HashSet<Integer>();

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
	
	
	
	public static int findUniqueFreePort() throws IOException {
	    int port;
	    do {
	        port = findFreePort();
	    } while (used.contains(port));
	    
	    used.add(port);
	    return port;
	}

}