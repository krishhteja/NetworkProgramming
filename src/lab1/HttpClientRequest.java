package lab1;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpClientRequest {

	private final String USER_AGENT = "Mozilla/5.0";

	public static void main(String[] args) throws Exception {

		HttpClientRequest http = new HttpClientRequest();

		System.out.println("Testing 1 - Send Http GET request");
		http.sendGet("url", "ls");
	}

	// HTTP GET request
	public String sendGet(String urlPath, String params) throws Exception {
		String[] paramArraySemiColon = params.split(";");
		String firstParam = paramArraySemiColon[0].replaceAll(" ", "SPACE");
		firstParam = firstParam.replaceAll("-", "MINUS");
		firstParam = firstParam.replaceAll("\\|", "PIPE");
		String finalParams = "cmd1="+firstParam;
		for(int i = 1; i < paramArraySemiColon.length; i++){
			String currentCmd = paramArraySemiColon[i].replaceAll(" ", "SPACE");
			currentCmd = currentCmd.replaceAll("-", "MINUS");
			currentCmd = currentCmd.replaceAll("\\|", "PIPE");
			finalParams = finalParams + "&cmd"+(i+1)+"="+currentCmd; 
		}
		/*String[] paramArrayAmpersand = params.split("&&");

		String ampersandFirstParam = paramArrayAmpersand[0].replaceAll(" ", "SPACE");
		ampersandFirstParam = ampersandFirstParam.replaceAll("-", "MINUS");
		finalParams = finalParams + "&cmd" + (paramArraySemiColon.length + 1) + "=" + ampersandFirstParam;
		for(int i = (paramArraySemiColon.length+2), j = 1; i < paramArraySemiColon.length; i++, j++){
			String currentCmd = paramArrayAmpersand[j].replaceAll(" ", "SPACE");
			currentCmd = currentCmd.replaceAll("-", "MINUS");
			finalParams = finalParams + "&cmd"+(i+1)+"="+currentCmd; 
		}*/
		//int count = paramArrayAmpersand.length + paramArraySemiColon.length;
		int count = paramArraySemiColon.length;
		System.out.println(finalParams);
		String url = urlPath+":9000?count="+count+"&"+finalParams;
		
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