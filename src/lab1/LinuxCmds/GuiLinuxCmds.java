package lab1.LinuxCmds;

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
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class GuiLinuxCmds extends Frame implements ActionListener{

	static Logger logger = Logger.getLogger("GuiLinuxCommands");
	private Label label;
	private Button submit;
	private TextField input, url;
	private TextArea output;
	
	public GuiLinuxCmds() {
		setLayout(new FlowLayout());
		label = new Label("Enter command");
		add(label);

		url = new TextField(80);
		url.setEditable(true);
		url.setText("http://localhost");
		add(url);
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
		FileHandler fh;  

	    try {  

	        // This block configure the logger with handler and formatter  
	        fh = new FileHandler("/Users/krishnavaddepalli/Desktop/GuiLinuxCommands.log");  
	        logger.addHandler(fh);
	        SimpleFormatter formatter = new SimpleFormatter();  
	        fh.setFormatter(formatter);  
	        // the following statement is used to log any messages  
	        logger.info("My first log");  
	    } catch (SecurityException e) {  
	        e.printStackTrace();  
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    }  

		GuiLinuxCmds app = new GuiLinuxCmds();
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		output.setText("");
		try {
			computeCommand();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	private void computeCommand() throws Exception{
		HttpClientRequest http = new HttpClientRequest();
		try {
//        	Scanner scanner = new Scanner(System.in);
			String command = input.getText();
			String urlPath = url.getText();
			command = command.replaceAll("&&", ";");
			logger.info("Commands to be executed are - " + command);
    		String out = http.sendGet(urlPath ,command);
    		String display = out.replaceAll("SPACE", " ");
    		display = display.replaceAll("MINUS", "-");
    		display = display.replaceAll("PIPE", "\\|");
    		display = display.replaceAll("NXT", "\n");
    		output.setText(display);
        }
        catch (IOException e) {
            System.out.println("exception happened - here's what I know: ");
            logger.warning("Some issue found");
            e.printStackTrace();
            System.exit(-1);
        }
	}
}