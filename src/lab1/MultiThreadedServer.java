package lab1;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.io.IOException;

public class MultiThreadedServer implements Runnable{
	
	static Logger logger = Logger.getLogger("GuiLinuxCommands");
    protected int          serverPort   = 8080;
    protected ServerSocket serverSocket = null;
    protected boolean      isStopped    = false;
    protected Thread       runningThread= null;

    public MultiThreadedServer(int port){
        this.serverPort = port;
    }

    public void run(){
        synchronized(this){
            this.runningThread = Thread.currentThread();
        }
        openServerSocket();
        while(! isStopped()){
            Socket clientSocket = null;
            try {
                clientSocket = this.serverSocket.accept();
            } catch (IOException e) {
                if(isStopped()) {
                    logger.warning("Server stopped unexpectedly");
                    System.out.println("Server Stopped.") ;
                    return;
                }
                throw new RuntimeException(
                    "Error accepting client connection", e);
            }
            new Thread(
                new WorkerRunnable(
                    clientSocket, "Multithreaded Server")
            ).start();
        }
        logger.info("Server stopped successfully");
        System.out.println("Server Stopped.") ;
    }


    private synchronized boolean isStopped() {
        return this.isStopped;
    }

    public synchronized void stop(){
        this.isStopped = true;
        try {
            this.serverSocket.close();
        } catch (IOException e) {
        	logger.warning("Unable to close the server");
            throw new RuntimeException("Error closing server", e);
        }
    }

    private void openServerSocket() {
        try {
            this.serverSocket = new ServerSocket(this.serverPort);
        } catch (IOException e) {
        	logger.warning("Unable to open port 8080");
            throw new RuntimeException("Cannot open port 8080", e);
        }
    }
    
    public static void main(String[] args){
    	FileHandler fh;
	    try {  

	        // This block configure the logger with handler and formatter  
	        fh = new FileHandler("/Users/krishnavaddepalli/Desktop/MultiThreadServer.log");  
	        logger.addHandler(fh);
	        SimpleFormatter formatter = new SimpleFormatter();  
	        fh.setFormatter(formatter);

	    } catch (SecurityException e) {  
	        e.printStackTrace();  
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    }  
    	MultiThreadedServer server = new MultiThreadedServer(9000);
    	new Thread(server).start();
    	logger.info("Server started successfully");
    	
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