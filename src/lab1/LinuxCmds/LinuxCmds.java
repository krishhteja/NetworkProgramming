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

import org.apache.commons.lang3.math.NumberUtils;

public final class LinuxCmds extends Frame implements ActionListener, Runnable{

	private Label label;
	private Button submit, loop;
	private TextField input, type;
	private TextArea output, loopOp;
	
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
		type = new TextField(5);
		add(type);
		type.hide();

		loop = new Button("Loop");
		add(loop);
		loop.addActionListener(this);

		output = new TextArea();
		output.setEditable(false);
		output.setSize(30, 30);
		add(output);

		loopOp = new TextArea();
		loopOp.setEditable(false);
		loopOp.setSize(30, 30);
		add(loopOp);
		setTitle("Run your linux commands or enter a number");
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
		Object src = e.getSource();
		// TODO Auto-generated method stub
		if(src == loop){
			type.setText("loop");
			loopOp.setText("");
		}else{
			type.setText("command");
			output.setText("");
		}
		run();
	}
	
	@Override
	public void run() {
		command = input.getText();
		String typeOfEvent = type.getText();
		if(typeOfEvent.equals("loop") && !NumberUtils.isNumber(command)){
			output.setText("Please enter a valid number");
		}
		// TODO Auto-generated method stub
		else{
			if(typeOfEvent.equals("loop")){
				synchronized(this){
		            this.runningThread = Thread.currentThread();
		        }
		        	new Thread(
		            		new RunCmds(typeOfEvent, command, loopOp)
		            		).start();
			}else{
				synchronized(this){
		            this.runningThread = Thread.currentThread();
		        }
		        	new Thread(
		            		new RunCmds(typeOfEvent, command, output)
		            		).start();

			}
		}
        	
	}
}