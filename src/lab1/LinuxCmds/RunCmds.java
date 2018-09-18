package lab1.LinuxCmds;

import java.awt.TextArea;
import java.io.BufferedReader;
import java.io.InputStreamReader;


/**

 */
public class RunCmds implements Runnable{
	protected String serverText   = null, type = null;
	protected TextArea output = null;

    public RunCmds(String type, String Text, TextArea output) {
        this.serverText   = Text;
        this.type = type;
        this.output = output;
    }

    public void run() {
    	if(type.equalsIgnoreCase("loop")){
    		computeLoop(serverText, output);
    	}else{
    		computeCommand(serverText, output);
    	}
    }
    
    public void computeCommand(String command, TextArea outputTextArea){
		System.out.println(Thread.currentThread().getName());
		String s = null;
		try {
//        	Scanner scanner = new Scanner(System.in);
        	String[] temp = {"/bin/sh", "-c", command};
        	
        	Process p = Runtime.getRuntime().exec(temp);
               
        	BufferedReader stdInput = new BufferedReader(new 
                     InputStreamReader(p.getInputStream()));

        	BufferedReader stdError = new BufferedReader(new 
                     InputStreamReader(p.getErrorStream()));

                // read the output from the command
        	System.out.println("Here is the standard output of the command: "+command+"\n");
                
        	while ((s = stdInput.readLine()) != null) {
        		outputTextArea.append("\n"+s);
        		System.out.println(s);
        	}
                
                // read any errors from the attempted command
        	System.out.println("Here is the standard error of the command (if any):\n");
        	while ((s = stdError.readLine()) != null) {
        		outputTextArea.append("\n"+s);
        		System.out.println(s);
        	}
                //System.exit(0);	
        }
        catch (Exception e) {
            System.out.println("exception happened - here's what I know: ");
            e.printStackTrace();
            System.exit(-1);
        }
	}
    
    public void computeLoop(String count, TextArea outputTextArea){
		System.out.println(Thread.currentThread().getName());
		outputTextArea.append("Running empty loop for " + count + " times");
		long current  = 1; //n1
		long previous = 0; //n2
		long next;
		for(int i = 0; i < Integer.valueOf(count); i++){
			next = current + previous;
			outputTextArea.append("\nRunning " + i + " iteration. Result is : " + next);
			current = previous;
			previous = next;
			System.out.println("Current value is : " + current + " previous value is : " + next);
		}
    }
}