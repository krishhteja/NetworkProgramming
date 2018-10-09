package GuiCmds;

import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Gui extends Frame implements ActionListener{ // you are predefining action listener as it runs on real time not sure though
	
	TextField urlTextBox, cmdTextBox;
	TextArea outputTextArea, loopOutput;
	Button submitBtn, loopBtn;
	
	//Constructor
	public Gui() {
		setLayout(new FlowLayout()); // Layout of type flowlayout
		urlTextBox = new TextField(50);
		urlTextBox.setText("localhost");
		urlTextBox.setEditable(true);
		add(urlTextBox); // add 
		cmdTextBox = new TextField(50);
		cmdTextBox.setText("ls");
		cmdTextBox.setEditable(true);
		add(cmdTextBox);
		loopBtn = new Button("Loop"); //Name of button is loop
		add(loopBtn); 
		loopBtn.addActionListener(this);
		submitBtn = new Button();
		submitBtn.setLabel("Submit");
		add(submitBtn);
		submitBtn.addActionListener(this); //Listens when button is clicked
		outputTextArea = new TextArea();
		outputTextArea.setEditable(false);
		add(outputTextArea);
		loopOutput = new TextArea();
		loopOutput.setEditable(false);
		add(loopOutput);

		setTitle("Run your linux commands");
		setSize(500, 500);
		setVisible(true);		
		addWindowListener(new WindowAdapter(){  
			public void windowClosing(WindowEvent e) {  
				dispose();  
			}  
		});  
	}
	
	public static void main(String[] args) {
		Gui temp = new Gui();
	}

	@Override
	public void actionPerformed(ActionEvent e) {  // e is action listener variable that will keep results after clicking the button
		// TODO Auto-generated method stub
		String ipAddr = urlTextBox.getText();
		String receivedOutput = "";
		Object clicked = e.getSource();// get the source when it is clicked 
		if(clicked.equals(loopBtn)) {  
			loopOutput.setText("");
			String enteredText = cmdTextBox.getText();
			ReqToServer req = new ReqToServer("loop", enteredText, ipAddr, loopOutput);
			Thread t = new Thread(req);
			t.start();
		}else {
			outputTextArea.setText("");
			String enteredText = cmdTextBox.getText();
			ReqToServer req = new ReqToServer("cmd", enteredText, ipAddr, outputTextArea);
			Thread t = new Thread(req);
			t.start();
		}
		outputTextArea.append(receivedOutput);
	}
}
