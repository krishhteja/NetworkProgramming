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
import java.io.IOException;
import java.io.InputStreamReader;

public class GuiLinuxCmds extends Frame implements ActionListener{

	private Label label;
	private Button submit;
	private TextField input;
	private TextArea output;
	
	public GuiLinuxCmds() {
		setLayout(new FlowLayout());
		label = new Label("Enter command");
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
		GuiLinuxCmds app = new GuiLinuxCmds();
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		output.setText("");
		computeCommand();
	}
	
	private void computeCommand(){
		String s = null;
		try {
//        	Scanner scanner = new Scanner(System.in);
        	System.out.print("Enter your command to run: ");
        	String command = input.getText();
        	String[] cmds = command.split("&&");
        	for(int i = 0; i < cmds.length; i++){
//            	String command = scanner.nextLine();
                Process p = Runtime.getRuntime().exec(cmds[i]);
                
                BufferedReader stdInput = new BufferedReader(new 
                     InputStreamReader(p.getInputStream()));

                BufferedReader stdError = new BufferedReader(new 
                     InputStreamReader(p.getErrorStream()));

                // read the output from the command
                System.out.println("Here is the standard output of the command: "+command+"\n");
                
                output.append("\n######################################################");
                output.append("\nCommand --> " + cmds[i]);
                while ((s = stdInput.readLine()) != null) {
                	output.append("\n"+s);
                    System.out.println(s);
                }
                
                // read any errors from the attempted command
                System.out.println("Here is the standard error of the command (if any):\n");
                while ((s = stdError.readLine()) != null) {
                    System.out.println(s);
                    output.setText(s);
                }
                //System.exit(0);	
        	}
        }
        catch (IOException e) {
            System.out.println("exception happened - here's what I know: ");
            e.printStackTrace();
            System.exit(-1);
        }
	}
}
