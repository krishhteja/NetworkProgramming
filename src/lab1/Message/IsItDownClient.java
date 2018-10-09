package lab1.Message;

public class IsItDownClient {
    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Usage: IsItDown host port destination");
        }
        String host = args[0];
        int port;
        try {
            port = Integer.parseInt(args[1]);
        } catch(Exception e) {
            // just why?
//
            port = IsItDownService.ISITDOWN_SERVICE_PORT;
        }
        MessageClient conn;
        try {
            conn = new MessageClient(host, port);
        } catch(Exception e) {
            System.err.println(e);
            return;
        }
        Message m = new Message();
        m.setType(IsItDownService.ISITDOWN_SERVICE_MESSAGE);
        m.setParam("dst", args[2]);
        m = conn.call(m);
        System.out.println(m.getParam("status"));
//        m.setType(75);
//        m = conn.call(m);
//        // What is this?
//        System.out.println("Bad reply " + m);
        conn.disconnect();
    }
}