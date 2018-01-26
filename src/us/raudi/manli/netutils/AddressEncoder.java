package us.raudi.manli.netutils;

import java.net.InetAddress;
import java.net.UnknownHostException;
/**
 * (CONCEPT CLASS)
 * This class is a work in progress.
 * 
 * This class encodes an Address:Port pair into a more readable string.
 * The encoded string can then be converted back into an address:port pair.
 * @author Raul Ferreira Fuentes
 *
 */
public class AddressEncoder {
	private static final int BASE_ENCODE = 36;
	
	
	public static String encode(InetAddress address, int port) {
		return AddressEncoder.encode(address.getHostName(), port);
	}
	
	public static String encode(String ip, int port) {
		String enc = "";
		
		for(String part : ip.split("\\."))
			enc += getCode(Integer.parseInt(part));
		
		return enc + port;
	}
	
	public static Address decode(String enc) throws UnknownHostException {
		String ip = "";
		int i=0;
		for(; i<4; i++) {
			ip += getInt(enc.substring(i*2, i*2+2));
			if(i != 3) ip += ".";
		}
		String remaining = enc.substring(i*2, enc.length());
		int port = Integer.parseInt(remaining);
		
		return new Address(ip, port);
	}

	private static String getCode(int i) {
		String str = Integer.toString(i, BASE_ENCODE);
		
		if(str.length() < 2) 
			str = "0" + str;
		else if(str.length() > 2)
			throw new UnsupportedOperationException("Encoded integer produces too large String.\nThe BASE_ENCODE should encode all integers from 0-255 into at most 2 characters.");
		
		return str;
	}
	
	private static int getInt(String code) {
		return Integer.parseInt(code, BASE_ENCODE);
	}
	
	
	public static void main(String[] args) throws UnknownHostException  {
		String encoded = AddressEncoder.encode("192.168.1.62", 37701);
		Address decoded = AddressEncoder.decode(encoded);

		System.out.println( encoded );
		System.out.println( decoded );
		
	}
}
