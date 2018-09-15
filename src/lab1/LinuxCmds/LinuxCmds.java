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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

public final class LinuxCmds extends Frame implements ActionListener, Runnable{

	private Label label;
	private Button submit;
	private TextField input;
	private TextArea output;
	
    protected boolean      isStopped    = false;
    protected Thread       runningThread= null;
	public volatile String command = null;
	
	public LinuxCmds() {
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
		LinuxCmds app = new LinuxCmds();
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		output.setText("");
		run();
//		computeCommand();
	}
	
	@Override
	public void run() {
		command = input.getText();
		output.setText("");
		// TODO Auto-generated method stub
		synchronized(this){
            this.runningThread = Thread.currentThread();
        }
        	new Thread(
            		new RunCmds(command)
            		).start();
        	
	}
	
	public final void printOutput(String outputOfCmd){
		outputOfCmd = outputOfCmd.replaceAll("NXT", "\n");
		output.append(outputOfCmd);
	}

}