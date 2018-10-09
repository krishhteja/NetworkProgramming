package lab1.sockets;

//Requires a single command line arg - the port number
import java.net.*;	// need this for InetAddress, Socket, ServerSocket 
import java.nio.charset.StandardCharsets;

import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

import java.awt.HeadlessException;
import java.io.*;	// need this for I/O stuff


public class TCPEchoServer {
	// define a constant used as size of buffer 
	static final int BUFSIZE=1024;
	// main starts things rolling
	static public void main(String args[]) { 
		
		/*if (args.length != 1){
			throw new IllegalArgumentException("Must specify a port!");
		}*/
		
		int port = Integer.parseInt("9080");
		try { 
			// Create Server Socket (passive socket) 
			ServerSocket ss = new ServerSocket(port);
			
			while (true) { 
				Socket s = ss.accept();
				handleClient(s);
				
			} 
			
		} catch (IOException e) {
			System.out.println("Fatal I/O Error !"); 
			System.exit(0);
			
		}
		
	}
	
	//this method handles one client
	// declared as throwing IOException - this means it throws 
	// up to the calling method (who must handle it!)
	//try taking out the "throws IOException" and compiling, 
	// the compiler will tell us we need to deal with this!
	
	static void handleClient(Socket s) throws IOException 
	{ 
		byte[] buff = new byte[BUFSIZE];
		int bytesread = 0;
		
		//print out client's address
		System.out.println("Connection from " + s.getInetAddress().getHostAddress());
		
		//Set up streams 
		InputStream in = s.getInputStream(); 
		OutputStream out = s.getOutputStream();
		String name = "Krishna";
		byte[] myName = name.getBytes();
		int totalLength = BUFSIZE + myName.length; 
		byte[] reply = new byte[totalLength];
		System.arraycopy(buff, 0, reply, 0, buff.length);
		System.arraycopy(myName, 0, reply, BUFSIZE, myName.length);
		//read/write loop 
//Modify your code here so that it sends back your name in addition to the echoed symbols
		while ((bytesread = in.read(reply)) != -1) {
			String str = new String(reply, StandardCharsets.UTF_8);
			System.out.println("Input is - " + str);
			out.write(reply,0,reply.length);
			out.flush();
		} 
		
		System.out.println("Client has left\n"); 
		
		s.close();
		
	}
	
}