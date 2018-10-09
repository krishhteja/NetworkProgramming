package lab1.sockets;


import java.net.*;// need this for InetAddress, Socket, ServerSocket 
import java.nio.charset.StandardCharsets;
import java.io.*;// need this for I/O stuff

public class UDPEchoServer { 
	static final int BUFSIZE=1024;
	
	static public void main(String args[]) throws SocketException 
	{ 
		/*
		if (args.length != 1) {
			throw new IllegalArgumentException("Must specify a port!"); 
						
		}
		*/
		int port = Integer.parseInt("9000");
		//int port = Integer.parseInt(args[0]);
		DatagramSocket s = new DatagramSocket(port);
		DatagramPacket dp = new DatagramPacket(new byte[BUFSIZE], BUFSIZE);
		try { 
			while (true) {
				s.receive(dp);
				// print out client's address 
				System.out.println("Message from " + dp.getAddress().getHostAddress());

				byte[] inputData = dp.getData();
				String name = "Krishna";
				byte[] myName = name.getBytes();
				int totalLength = inputData.length + myName.length; 
				byte[] reply = new byte[totalLength];
				System.arraycopy(myName, 0, reply, 0, myName.length);
				System.arraycopy(inputData, 0, reply, myName.length, inputData.length);
				DatagramPacket out = new DatagramPacket(reply, reply.length, dp.getAddress(), dp.getPort());
				// Send it right back 
				s.send(out); 
				out.setLength(out.getLength());// avoid shrinking the packet buffer
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Fatal I/O Error !"); 
			System.exit(0);
			
		} 

	}
}