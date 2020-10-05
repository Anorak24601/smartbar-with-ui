package smartBarUI;

import java.awt.FlowLayout;

import javax.swing.*;

@SuppressWarnings("serial")
public class Error extends JFrame {
	JLabel errorMessage = new JLabel();
	JButton ok = new JButton("Ok");
	
	public Error(String message) {
		//Basic UI stuff
		super("Error!");
		setSize(message.length()*7,100);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		FlowLayout flo = new FlowLayout();
		setLayout(flo);
		
		//display message
		errorMessage.setText("ERROR: " + message);
		add(errorMessage);
		add(ok);
		
		//button can have local lambda function, closes frame
		ok.addActionListener(e -> {
			dispose();
		});
		
		setVisible(true);
	}
}