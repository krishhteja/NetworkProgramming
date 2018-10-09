package lab1.sockets;


import java.net.*;	// need this for InetAddress 

public class Lookup {
	static public void main(String args[]) 
	{ 
		// lookup the address of each hostname found on the command line
		for (int i=0;i<args.length;i++) 
		{ 
			printAddress(args[i]);
			
		}
		
	}
	
	static void printAddress( String hostname ) 
	{ 
		// use a try/catch to handle failed lookups 
		try {
			InetAddress a = InetAddress.getByName(hostname);
			hostname = hostname + "Krishna";
			System.out.println(hostname + ":" + a.getHostAddress());
		} catch (UnknownHostException e) { 
			System.out.println("No address found for " + hostname);
			
		}
		
	}
	
}