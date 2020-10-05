package smartBarUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

@SuppressWarnings("serial")
public class LiquorPedia extends JFrame{
	//In spirits, a button for all the central spirits
	//	each spirit gets history, types, general flavor, and recommended bottles
	//	of those, only recommended bottles needs to have locally saved files so that the user can log favorites
	//In mixers, a section on liqueurs, garnishes, juices, and bitters
	//	Talk about flavor content and usage for each
	//	In bitters and liqueurs, another recommended bottles section
	//In technique, a section on physical technique as well as one on drink theory
	//	Include how to practice the techniques as well
	//In history, do the history of cocktails
	//	separate by buttons for eras. Beginning, dark ages, beachcomber, golden (current) age
	
	JButton spirit = new JButton("Spirits");
	JButton mixer = new JButton("Mixers");
	JButton technique = new JButton("Technique");
	JButton history = new JButton("History");
	
	public LiquorPedia() {
		//UI basics
		super("LiquorPedia");
		setSize(150,100);
		FlowLayout flo = new FlowLayout();
		setLayout(flo);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		//Slap the buttons in
		add(spirit);
		add(mixer);
		add(technique);
		add(history);
		
		//create button functionalist
		ActionListener act = event -> {
			if(event.getSource() == spirit) new Spirits();
			if(event.getSource() == mixer) new Mixers();
			if(event.getSource() == technique) new Technique();
			if(event.getSource() == history) new History();
		};
		
		spirit.addActionListener(act);
		mixer.addActionListener(act);
		technique.addActionListener(act);
		history.addActionListener(act);
		
		//make it real
		setVisible(true);
	}
}
