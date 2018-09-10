package lab1;

import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class GuiWebRequest extends Frame implements ActionListener{

	private Label label;
	private Button submit;
	private TextField input;
	private TextArea output;
	
	public GuiWebRequest() {
		setLayout(new FlowLayout());
		label = new Label("Enter url");
		add(label);
		input = new TextField(80);
		input.setText("ls");
		input.setEditable(true);
		add(input);
		submit = new Button("Run");
		add(submit);
		submit.addActionListener(this);
		output = new TextArea();
		output.setEditable(false);
		output.setSize(30, 30);
		add(output);
		setTitle("Run your linux commands");
		setSize(500, 500);
		setVisible(true);
		
		addWindowListener(new WindowAdapter(){  
			public void windowClosing(WindowEvent e) {  
				dispose();  
			}  
		});  
		
	}
	
	public static void main(String args[]) {
		GuiWebRequest app = new GuiWebRequest();
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		output.setText("");
		try {
			computeCommand();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	private void computeCommand() throws IOException{
		String s = null;
		try{
			URL url = new URL("http://localhost:9000");
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			
			Map<String, String> parameters = new HashMap<>();
			parameters.put("cmd", "ls");
			 
			con.setDoOutput(true);
			con.setRequestProperty("Content-Type", "application/json");
			con.setConnectTimeout(5000);
			con.setReadTimeout(5000);
			con.setInstanceFollowRedirects(false);
			DataOutputStream out = new DataOutputStream(con.getOutputStream());
			out.writeBytes(ParameterStringBuilder.getParamsString(parameters));
			out.flush();
			out.close();
			
			int status = con.getResponseCode();
			System.out.print(status);
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer content = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				content.append(inputLine);
			}
			in.close();
			con.disconnect();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
}
