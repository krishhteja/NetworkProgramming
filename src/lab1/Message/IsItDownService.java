package lab1.Message;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

import org.omg.SendingContext.RunTime;
import java.io.IOException;
import java.net.InetAddress;

public class IsItDownService implements Deliverable {

    public static final int ISITDOWN_SERVICE_MESSAGE = 100;
    public static final int ISITDOWN_SERVICE_PORT = 1999;
    public Message send(Message m) {
//        Date today = new Date();
//        String dst =;
        try {
            InetAddress dst = InetAddress.getByName(m.getParam("dst"));
            boolean reachable = dst.isReachable(5000);
//            Process p1 = java.lang.Runtime.getRuntime().exec("ping -n 1" + m.getParam("dst"));
//            int returnVal = p1.waitFor();
//            boolean reachable = (returnVal == 0);
            m.setParam("status", reachable ? "UP" : "DOWN");
        } catch (IOException e) {
            m.setParam("status", "I/O ERROR !!!");
        }

        return m;
    }
    public static void main(String args[]) {
        IsItDownService ds = new IsItDownService();
        MessageServer ms;
        try {
            ms = new MessageServer(ISITDOWN_SERVICE_PORT);
        } catch(Exception e) {
            System.err.println("Could not start service " + e);
            return;
        }
        Thread msThread = new Thread(ms);
        ms.subscribe(ISITDOWN_SERVICE_MESSAGE, ds);
        msThread.start();
    }
}