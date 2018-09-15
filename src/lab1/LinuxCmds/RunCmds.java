package lab1.LinuxCmds;
import java.io.BufferedReader;
import java.io.IOException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;


/**

 */
public class RunCmds implements Runnable{
	protected String serverText   = null;
	LinuxCmds lx = new LinuxCmds();

    public RunCmds(String Text) {
        this.serverText   = Text;
    }

    public void run() {
    	computeCommand(serverText);
    }
    
	public String computeCommand(String command){
		System.out.println(Thread.currentThread().getName());
		String s = null;
		String output = "Command not found";
		try {
//        	Scanner scanner = new Scanner(System.in);
        	String[] cmds = command.split(";");
        	output = "##";
        	for(int i = 0; i < cmds.length; i++){
//            	String command = scanner.nextLine();
        		String cmdToExec = cmds[i];
        		System.out.println("Command before adding it to array is ---- " + cmdToExec);
        		String[] temp = {"/bin/sh", "-c", cmdToExec};
        		
        		Process p = Runtime.getRuntime().exec(temp);
                
                BufferedReader stdInput = new BufferedReader(new 
                     InputStreamReader(p.getInputStream()));

                BufferedReader stdError = new BufferedReader(new 
                     InputStreamReader(p.getErrorStream()));

                // read the output from the command
                System.out.println("Here is the standard output of the command: "+cmdToExec+"\n");
                
                output = output + ("NXTCommand --> " + cmds[i]);
                while ((s = stdInput.readLine()) != null) {
                	output = output + s;
                    System.out.println(s);
                }
                
                // read any errors from the attempted command
                System.out.println("Here is the standard error of the command (if any):\n");
                while ((s = stdError.readLine()) != null) {
                    System.out.println(s);
                }
                //System.exit(0);	
        	}
        }
        catch (Exception e) {
            System.out.println("exception happened - here's what I know: ");
            e.printStackTrace();
            System.exit(-1);
        }
		
		lx.printOutput(output);
		return output;
	}

}