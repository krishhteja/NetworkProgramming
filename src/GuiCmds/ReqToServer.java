package GuiCmds;

import java.awt.TextArea;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class ReqToServer extends Thread {

	String type,cmd,ipAddr;
	TextArea outputArea;
	public ReqToServer(String type, String cmd, String ipAddr, TextArea outputArea) {
		this.type = type;
		this.cmd = cmd;
		this.ipAddr = ipAddr;
		this.outputArea = outputArea;
	}
	
	public void run() {
		sendReqToServer(type, cmd, ipAddr, outputArea);
	}
	
	public String sendReqToServer(String type, String cmd, String ipAddr, TextArea outputArea) { //Parameters are type of command(loop/cmd) and the command(cmd/number)
		Socket clientSocket = new Socket();
		String outputToSend = "";
		System.out.println("\nSending request to server with ipAddress - " + ipAddr);
		try {
			clientSocket = new Socket(ipAddr, 5000);
	        // obtaining input and out streams 
	        PrintWriter  outputData = new PrintWriter(clientSocket.getOutputStream()); // like output buffer
	        
	        String dataToSend = type + "#" + cmd ;
			System.out.println("\nSending request to server with Data - " + dataToSend);
	        outputData.println(dataToSend);
	        outputData.flush();
	        
	        BufferedReader inputData = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); 
	        String line;
	        while ((line = inputData.readLine()) != null) { //Output is a stream of data and hence we read it line by line
	        	outputToSend = outputToSend + line + "\n";
	        	outputArea.append(line + "\n");
	        	System.out.println("Output Data - " + line);
	        }
        	System.out.println("End of output");
		} catch (UnknownHostException error) {
			error.printStackTrace();
		} catch (IOException error) {
			error.printStackTrace();
		}
		return outputToSend;
	}
}
