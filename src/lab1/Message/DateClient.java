package lab1.Message;

public class DateClient {

	public static void main(String[] args) {
		if (args.length < 2) {
			System.out.println("Usage: DateClient host port");
		}
		//String host = args[0];
		String host = "18.223.164.91";
		int port;
		try {
			port = Integer.parseInt("9000");
			//port = Integer.parseInt(args[1]);
		} catch(Exception e) {
			port = DateService.DATE_SERVICE_PORT;
		}
		MessageClient conn;
		try {
			conn = new MessageClient(host,port);
		} catch(Exception e) {
			System.err.println(e);
			return;
		}
		Message m = new Message();
		m.setType(DateService.DATE_SERVICE_MESSAGE);
		m.setParam("person","krishna");
		m = conn.call(m);
		System.out.println("Date " + m.getParam("date"));
		m.setType(75);
		m = conn.call(m);
		System.out.println("Bad reply " + m);
		conn.disconnect();
	}
}