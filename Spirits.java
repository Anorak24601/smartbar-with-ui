package smartBarUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

@SuppressWarnings("serial")
public class Spirits extends JFrame {
	
	//buttons
	JButton whiskey = new JButton("Whisk(e)y");
	JButton tequila = new JButton("Tequila");
	JButton rum = new JButton("Rum");
	JButton gin = new JButton("Gin");
	JButton brandy = new JButton("Brandy");
	JButton other = new JButton("Other");
	
	public Spirits() {
		//UI basics
		super("Spirits");
		setSize(150,200);
		GridLayout grid = new GridLayout(3,3,5,5);
		setLayout(grid);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		add(whiskey);
		add(tequila);
		add(rum);
		add(gin);
		add(brandy);
		add(other);
		
		ActionListener act = event -> {
			if (event.getSource() == whiskey) new Whiskey();
			if (event.getSource() == tequila) new Tequila();
			if (event.getSource() == rum) new Rum();
			if (event.getSource() == gin) new Gin();
			if (event.getSource() == brandy) new Brandy();
			if (event.getSource() == other) new OtherSpirit();
		};
		
		whiskey.addActionListener(act);
		tequila.addActionListener(act);
		rum.addActionListener(act);
		gin.addActionListener(act);
		brandy.addActionListener(act);
		other.addActionListener(act);
		
		setVisible(true);
	}
}
