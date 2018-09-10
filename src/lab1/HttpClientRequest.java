package lab1;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class HttpClientRequest {

	private final String USER_AGENT = "Mozilla/5.0";

	public static void main(String[] args) throws Exception {

		HttpClientRequest http = new HttpClientRequest();

		System.out.println("Testing 1 - Send Http GET request");
		http.sendGet("ls");
	}

	// HTTP GET request
	public String sendGet(String params) throws Exception {
		
		String url = "http://localhost:9000?cmd="+params;
		
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		//add request header
		con.setRequestProperty("User-Agent", USER_AGENT);

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		
		//print result
		System.out.println("Response - " + response.toString().replaceAll("NXT", "\n"));
		return response.toString();
	}
}