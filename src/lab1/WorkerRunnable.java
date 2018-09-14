package lab1;
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
public class WorkerRunnable implements Runnable{
	static Logger logger = Logger.getLogger("GuiLinuxCommands");
	private String OUTPUT = "Krishna";
	private static final String OUTPUT_HEADERS = "HTTP/1.1 200 OK\r\n" +
	    "Content-Type: text/html\r\n" + 
	    "Content-Length: ";
	private static final String OUTPUT_END_OF_HEADERS = "\r\n\r\n";
	
    protected Socket clientSocket = null;
    protected String serverText   = null;

    public WorkerRunnable(Socket clientSocket, String serverText) {
        this.clientSocket = clientSocket;
        this.serverText   = serverText;
    }

    Map<String, String> paramMap = null;  
    
    public void run() {
        try {
        	InputStream input  = clientSocket.getInputStream();
        	paramMap = parse(input);  
        	System.out.println("URL CHECK AT SERVER --- " + input.toString());
            System.out.println("Param Map run  - " + paramMap);
            Integer cmdLength = Integer.valueOf(paramMap.get("count"));
            String finalCmdToExecute = paramMap.get("cmd1");
            for(int i = 2; i <= cmdLength; i++){
            	String cmdName = "cmd" + i;
            	String cmd = paramMap.get(cmdName);
            	System.out.println("####$$$$$$$ " + cmd + " #### $$$$$");
            	finalCmdToExecute = finalCmdToExecute + ";" + cmd;
                System.out.println("Commnad to be executed - " + cmd);
            }
            OUTPUT = computeCommand(finalCmdToExecute);
            
            //String cmd = paramMap.get("cmd");
            System.out.println("Output is " + OUTPUT);
            OutputStream output = clientSocket.getOutputStream();
            long time = System.currentTimeMillis();
            output.write((OUTPUT_HEADERS + OUTPUT.length() + OUTPUT_END_OF_HEADERS + OUTPUT).getBytes());
            output.flush();
            output.close();
            input.close();
            System.out.println("Request processed at time : " + time);
        } catch (IOException e) {
            //report exception somewhere.
            e.printStackTrace();
        }
    }
    
    public Map<String, String> parse(InputStream is) throws IOException {  
        Map<String, String> paramMap = new HashMap<String, String>();  
        LineNumberReader lr = new LineNumberReader(new InputStreamReader(is));  
        String inputLine = null;  
        String method = null;  
        String httpVersion = null;  
        String uri = null;  
        
        // read request line  
        inputLine = lr.readLine();  
        System.out.println("URL HIT FROM CLIENT IS " + inputLine);
        String[] requestCols = inputLine.split("\\s");  
        method = requestCols[0];  
        uri = requestCols[1];  
        for(int i = 0; i < requestCols.length; i++){
        	System.out.println("URL PARAM " + i + " being used is ----- " + requestCols[i]);
        }
        System.out.println("URI Being hit is - " + uri);
        httpVersion = requestCols[2];  
        System.out.println("http version:\t" + httpVersion);  
  
        // parse GET param  
        if (uri.contains("?")) {  
            paramMap.putAll(parseParam(uri.split("\\?", 2)[1], false));  
        }  
  
        // read header  
        while (StringUtils.isNotBlank(inputLine = lr.readLine())) {  
            System.out.println("post header line:\t" + inputLine);  
        }  
  
        // read body - POST method  
        if (method.toUpperCase().equals("POST")) {  
            StringBuffer bodySb = new StringBuffer();  
            char[] bodyChars = new char[1024];  
            int len;  
  
            // ready() make sure it will not block,  
            while (lr.ready() && (len = lr.read(bodyChars)) > 0) {  
                bodySb.append(bodyChars, 0, len);  
            }  
            paramMap.putAll(parseParam(bodySb.toString(), true));  
  
            System.out.println("post body:\t" + bodySb.toString());  
        }  
  
        return paramMap;  
    }  
    
    public Map<String, String> parseParam(String paramStr, boolean isBody) {  
        String[] paramPairs = paramStr.trim().split("&");  
        Map<String, String> paramMap = new HashMap<String, String>();  
  
        String[] paramKv;  
        for (String paramPair : paramPairs) {  
            if (paramPair.contains("=")) {  
                paramKv = paramPair.split("=");  
                if (isBody) {  
                    // replace '+' to ' ', because in body ' ' is replaced by '+' automatically when post,  
                    paramKv[1] = paramKv[1].replace("+", " ");  
                }  
                paramMap.put(paramKv[0], paramKv[1]);  
            }  
        }  
        return paramMap;  
    }  
    
	private String computeCommand(String command){
		String s = null;
		String output = "Command not found";
		try {
//        	Scanner scanner = new Scanner(System.in);
        	String[] cmds = command.split(";");
        	output = "##";
        	for(int i = 0; i < cmds.length; i++){
//            	String command = scanner.nextLine();
        		String cmdToExec = cmds[i].replaceAll("MINUS", "-");
        		cmdToExec = cmdToExec.replaceAll("SPACE", " ");
        		cmdToExec = cmdToExec.replaceAll("PIPE", "\\|");
        		System.out.println("Command before adding it to array is ---- " + cmdToExec);
        		logger.info("Command before adding it to array ----- " + cmdToExec);
        		String[] temp = {"/bin/sh", "-c", cmdToExec};
        		System.out.println("Final Commnad being executed ---- " + temp[0]);
        		logger.info("Final Commnad being executed ---- " + temp[0]);
        		
        		Process p = Runtime.getRuntime().exec(temp);
                
                BufferedReader stdInput = new BufferedReader(new 
                     InputStreamReader(p.getInputStream()));

                BufferedReader stdError = new BufferedReader(new 
                     InputStreamReader(p.getErrorStream()));

                // read the output from the command
                System.out.println("Here is the standard output of the command: "+cmdToExec+"\n");
                
                output = output + ("NXTCommand --> " + cmds[i]);
                while ((s = stdInput.readLine()) != null) {
                	output = output + "NXT"+s;
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
            logger.info("Exception found is ---- " + e.getMessage());
            e.printStackTrace();
            System.exit(-1);
        }
		return output;
	}

}