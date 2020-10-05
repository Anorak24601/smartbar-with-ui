package smartBarUI;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;

@SuppressWarnings("serial")
public class AddRecipe extends JFrame {
	//First panel, recipe name
	JPanel pane1 = new JPanel();
	JLabel nameLabel = new JLabel("Recipe Name: ");
	JTextField name = new JTextField("", 10);
	
	//Second panel, ingredients
	JPanel pane2 = new JPanel();
	JLabel ingLabel = new JLabel("Ingredients list (liquids only): ");
	JTextArea ingArea = new JTextArea("", 5,10);
	JLabel amtLabel = new JLabel("Amounts list (in oz): ");
	JTextArea amtArea = new JTextArea("", 5,10);
	
	//Third panel, instructions
	JPanel pane3 = new JPanel();
	JLabel instLabel = new JLabel("Instructions: ");
	JTextArea instArea = new JTextArea("",7,30);
	
	//Fourth panel, button to create file
	JPanel pane4 = new JPanel();
	JButton create = new JButton("Create Recipe");
	
	public AddRecipe() {
		//basic UI stuff
		super("Add Recipe");
		setSize(550,400);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		FlowLayout flo = new FlowLayout();
		setLayout(flo);
		
		//set pane 1
		pane1.add(nameLabel);
		pane1.add(name);
		pane1.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		add(pane1);
		
		//set pane 2
		pane2.add(ingLabel);
		pane2.add(ingArea);
		pane2.add(amtLabel);
		pane2.add(amtArea);
		pane2.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		add(pane2);
		
		//set pane 3
		pane3.add(instLabel);
		pane3.add(instArea);
		pane3.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		add(pane3);
		
		//set pane 4
		pane4.add(create);
		add(pane4);
		
		//set button function
		ActionListener act = (event) ->{
			if(event.getSource() == create) {
				//create lists for stuff from text areas
				ArrayList<String> ingredients = new ArrayList<String>();
				ArrayList<Double> amounts = new ArrayList<Double>();
				ArrayList<String> instructions = new ArrayList<String>();
				
				//populate lists
				for (String i : ingArea.getText().split("\\n")) {
					//denote alcohol with asterisk
					if(i.contains("Rum") || i.contains("Wine") || i.contains("Tequila") || i.contains("Whiskey") ||
							i.contains("Vodka") || i.contains("Beer") || i.contains("Liqueur") ||
							i.contains("Cognac") || i.contains("Brandy") || i.contains("Fireball") ||
							i.contains("Curacao") || i.contains("Gin") || i.contains("Chartreuse") ||
							i.contains("Malibu") || i.contains("Creme") || i.contains("Absinthe") ||
							i.contains("Schnapps") || i.contains("Cointreau") || i.contains("Irish Cream") ||
							i.contains("Campari") || i.contains("Vermouth") || i.contains("Sake") ||
							i.contains("Pimm's") || i.contains("Arrack") || i.contains("Champagne") ||
							i.contains("St. Germain") || i.contains("Benedictine") || i.contains("Chambord") ||
							i.contains("Aperol") || i.contains("Drambuie") || i.contains("Cachaca") ||
							i.contains("Suze") || i.contains("Everclear") || i.contains("Mezcal") ||
							i.contains("Sherry") || i.contains("Americano")) {
						ingredients.add("*" + i);
					}
					else ingredients.add(i);
				}
				for (String i : amtArea.getText().split("\\n")) amounts.add(Double.parseDouble(i));
				for (String i : instArea.getText().split("\\n")) instructions.add(i);
				
				//turn them into arrays
				String[][] allIng = new String[ingredients.size()][2];
				for (int i = 0; i < allIng.length; i++) {
					allIng[i][0] = ingredients.get(i);
					allIng[i][1] = amounts.get(i).toString();
				}
				String[] insts = new String[instructions.size()];
				for (int i = 0; i < insts.length; i++) insts[i] = instructions.get(i);
				
				//call method that makes the file
				createRecipe(name.getText(), allIng, insts);
			}
			
			dispose();
		};
		
		//end lambda function and make UI visible
		create.addActionListener(act);
		setVisible(true);
	}
	
	//method to make the file
	public static void createRecipe(String recipeName, String[][] ings, String[] inst) {
		File recipe = new File("C:\\Users\\" + System.getProperty("user.name") + "\\Documents\\SBRecipes\\" + recipeName + ".txt");
		
		if (!recipe.exists()) {
			try {
				//make writer
				FileWriter fw = new FileWriter("C:\\Users\\" + System.getProperty("user.name") + "\\Documents\\SBRecipes\\" + recipeName + ".txt");
				BufferedWriter write = new BufferedWriter(fw);
				
				//write ingredients
				for (int i = 0; i < ings.length; i++) {
					write.write(ings[i][0] + "," + ings[i][1]);
					write.newLine();
				}
				
				//write instructions
				write.write("Instructions:");
				write.newLine();
				for (int i = 0; i < inst.length; i++) {
					write.write(inst[i]);
					if (i < inst.length - 1) write.newLine();
				}
				
				//close the writer
				write.close();
			} catch (IOException ex) {
				
			}
		} else new Error("Recipe \"" + recipeName + "\" already exists");
	}
}