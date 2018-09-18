package lab1.LinuxCmds;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpClientRequest {

	private final String USER_AGENT = "Mozilla/5.0";

	public static void main(String[] args) throws Exception {

		HttpClientRequest http = new HttpClientRequest();

		System.out.println("Testing 1 - Send Http GET request");
		http.sendGet("url", "ls", "command");
	}

	// HTTP GET request
	public String sendGet(String urlPath, String params, String type) throws Exception {
		StringBuffer response = new StringBuffer();
		String url = urlPath;
		if(type.equals("loop")){
			response.append("Running empty loop for " + params + " timesNXT");
			url = url+":9000?type=loop&count="+params;
			System.out.println("URL " + url);
		}else{
			String[] paramArraySemiColon = params.split(";");
			String finalParams = "";
			for(int i = 0; i < paramArraySemiColon.length; i++){
				String currentCmd = paramArraySemiColon[i].replaceAll(" ", "SPACE");
				currentCmd = currentCmd.replaceAll("-", "MINUS");
				currentCmd = currentCmd.replaceAll("\\|", "PIPE");
				currentCmd = currentCmd.replaceAll("&", "AMPERSAND");
				finalParams = finalParams + "&cmd"+(i+1)+"="+currentCmd; 
			}
			int count = paramArraySemiColon.length;
			System.out.println(finalParams);
			url = url+":9000?type=cmd&count="+count+finalParams;
		}
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
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		return response.toString();
	}
}