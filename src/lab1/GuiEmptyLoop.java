package lab1;

import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GuiEmptyLoop extends Frame implements ActionListener{
	private Label label;
	private Button submit;
	private TextField input, output;
	
	public GuiEmptyLoop() {
		setLayout(new FlowLayout());
		label = new Label("Enter number of iterations");
		add(label);
		input = new TextField(30);
		input.setText("10");
		input.setEditable(true);
		add(input);
		submit = new Button("Run");
		add(submit);
		submit.addActionListener(this);
		output = new TextField(50);
		output.setEditable(false);
		add(output);
		setTitle("Run empty loop");
		setSize(500, 500);
		setVisible(true);
		
		addWindowListener(new WindowAdapter(){  
			public void windowClosing(WindowEvent e) {  
				dispose();  
			}  
		});  
		
	}
	
	public static void main(String args[]) {
		GuiEmptyLoop app = new GuiEmptyLoop();
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		computeCommand();
	}
	
	private void computeCommand(){
		output.setText("Empty loop is run for " + input.getText() + " times");
		int count = Integer.parseInt(input.getText());
		for(int i = 0; i < count; i++){
			System.out.println(i);
		}
	}
}
