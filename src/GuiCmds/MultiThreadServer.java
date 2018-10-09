package GuiCmds;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class MultiThreadServer implements Runnable{
	
    protected int          serverPort   = 8080;
    protected ServerSocket serverSocket = null;
    protected boolean      isStopped    = false;
    protected Thread       runningThread= null;
	public MultiThreadServer(int port) {
		this.serverPort = port;
	}
	public void run(){
        synchronized(this){ // a pattern is followed for execution for threads
            this.runningThread = Thread.currentThread();
        }
        openServerSocket();
        while(! isStopped()){ //If server is running
            Socket clientSocket = null;
            try {
                clientSocket = this.serverSocket.accept(); //Accept new clients
            } catch (IOException e) {
                if(isStopped()) {
                	logger.log(Level.SEVERE,"Server stopped", e);
                    return;
                }
                
                throw new RuntimeException(
                    "Error accepting client connection", e);
            }
        	logger.info("Creating Thread for client");
            new Thread(new ThreadClass(clientSocket)).start(); // Create a new thread and assign it to Client Request
        }
    	logger.info("Server stopped");
        System.out.println("Server Stopped.") ;
    }


    private synchronized boolean isStopped() {
        return this.isStopped;
    }

    public synchronized void stop(){
        this.isStopped = true;
        try {
        	logger.info("Server socket closed");
            this.serverSocket.close();
        } catch (IOException e) {
        	logger.log(Level.SEVERE,"Error closing server ", e);
            throw new RuntimeException("Error closing server", e);
        }
    }

    private void openServerSocket() { //Creating socket on given port
        try {
        	logger.info("Server Running on " + this.serverPort);
            this.serverSocket = new ServerSocket(this.serverPort);
        } catch (IOException e) {
        	logger.log(Level.SEVERE,"Cannot open port 8080");
            throw new RuntimeException("Cannot open port 8080", e);
        }
    }
    
    static Logger logger = Logger.getLogger("MultiThreadServer"); // Initiliaze the logger
    
    public static void main(String[] args){
    	MultiThreadServer server = new MultiThreadServer(5000);
    	new Thread(server).start();
    	FileHandler fh;
	    try {  

	        // This block configure the logger with handler and formatter  
	        fh = new FileHandler("/home/ubuntu/logs/Gui.log");  //Create which file to use for logs
	        logger.addHandler(fh); //Adding a handler to write to the file
	        SimpleFormatter formatter = new SimpleFormatter();  
	        fh.setFormatter(formatter);

	    } catch (SecurityException e) {  
	        e.printStackTrace();  
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    }  
    	/////// UNCOMMENT IF YOU WISH TO STOP SERVER AFTER SPECIFIC TIME
    	/* 
    	try {
    	    Thread.sleep(200 * 1000);
    	} catch (InterruptedException e) {
    	    e.printStackTrace();
    	}
    	System.out.println("Stopping Server");
    	server.stop();*/
    }
}