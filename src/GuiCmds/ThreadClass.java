package GuiCmds;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.logging.Level;

public class ThreadClass implements Runnable{
	protected Socket clientSocket;
	protected String outputToSend = null;

	//All the actions to be done by thread will be done here
	
	public ThreadClass(Socket clientSocket) { //Constructor for this class
		this.clientSocket = clientSocket;
	}

	@Override
	public void run() { //Execution of this class starts here
		// TODO Auto-generated method stub
		try {
        	MultiThreadServer.logger.info("Got a request from client");
			InputStream inputStream = clientSocket.getInputStream();  // get the input stream and store it in input inputstream
			OutputStream outputStream = clientSocket.getOutputStream();
			while(true) { //Checks if there is more information coming from the client
	            DataInputStream dataToInputStream = new DataInputStream(inputStream);  // pass the data from inputstream variable to dataToInpurStream variable which is readble data type
				
	            // Read the bytes from the input stream of the socket line by line
				
				String commandInfo = dataToInputStream.readLine(); //You only get one line from client cmd/loop + "#" + command/number 
	        	MultiThreadServer.logger.info("Received command to execute - " + commandInfo);
				String commandOrLoop = commandInfo.split("#")[0]; // and now you split it, first part is either loop or cmd,
				String numberOrCommand = commandInfo.split("#")[1]; // second part with index [1] is the commadn itslef 
	        	MultiThreadServer.logger.info("Command type - " + commandOrLoop + " with command text - " + numberOrCommand);
				if(commandOrLoop.equals("loop")) {
					outputToSend = executeLoop(numberOrCommand); //Run executeloop method and save its output to outputToSend variable
				}else {
					outputToSend = executeCommand(numberOrCommand); //Run executeCommand method and save its output to outputToSend Variable
				}
				MultiThreadServer.logger.info("Output of running command - " + commandInfo + " is " + outputToSend);
				outputStream.write(outputToSend.getBytes()); //Send data to client in bytes
				outputStream.flush(); //Put it on network
			}
		} catch (IOException e) {
			MultiThreadServer.logger.log(Level.SEVERE, "Receiving data from client ", e);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public String executeLoop(String commandText) {
		int number = Integer.parseInt(commandText); //Conversion of string to int
		String outputToReturn = "";
		for(int i = 0; i < number; i++) {
			outputToReturn = outputToReturn + "Currently executing iteration " + i + "\n";
			MultiThreadServer.logger.info("Loop executing. Current iteration " + i);
		}
		return outputToReturn;
	}
	
	public String executeCommand(String commandText) {
		String outputToReturn = "";
		String[] cmdToRun = {"bash", "-c", commandText}; //Commamds to run
		try {
			Process p = Runtime.getRuntime().exec(cmdToRun); //Execute java
			MultiThreadServer.logger.info("Running commands - " + Arrays.toString(cmdToRun));
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream())); 
			// Read input Stream of process with input stream reader and store it in buffered reader
			String readLine;
			while((readLine = reader.readLine()) != null) {
				outputToReturn = outputToReturn + readLine + "\n"; //Create output and append it to exisiting output
				MultiThreadServer.logger.info("Capturing line by line output for command - " + commandText + " --> " + readLine);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			MultiThreadServer.logger.log(Level.SEVERE, "Command execution failed", e);
			e.printStackTrace();
		}
		return outputToReturn;
	}
}
