package smartBarUI;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.*;

@SuppressWarnings("serial")
public class Main extends JFrame {
	
	//set variables for basic elements
	JPanel warning = new JPanel();
	JLabel warningLabel = new JLabel(getIntro());
	JButton ingredients, recipes, pedia;
	
	public Main() {
		
		//UI basics
		super("Koonce SmartBar v4.0");
		setSize(150,100);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		FlowLayout flo = new FlowLayout();
		setLayout(flo);
		setLocationRelativeTo(null);
		
		File check = new File("C:\\Users\\" + System.getProperty("user.name") + "\\Ingredients.txt");
		if (!check.exists()) {
			try {
				FileWriter check12 = new FileWriter("C:\\Users\\" + System.getProperty("user.name") + "\\Ingredients.txt");
				check12.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		//makes the SBRecipes folder if it doesn't already exist
		File checkRecipes = new File("C:\\Users\\" + System.getProperty("user.name") + "\\Documents\\SBRecipes");
		if (!checkRecipes.exists()) resetRecipes();
		
		//add elements
		ingredients = new JButton("Ingredients");
		recipes = new JButton("Recipes");
		pedia = new JButton("LiquorPedia");
		add(ingredients);
		add(recipes);
		add(pedia);
		
		warning.add(warningLabel);
		warning.setBackground(Color.YELLOW);
		add(warning);
		
		//set button functions
		ActionListener act = (event) -> {
			if (event.getSource() == ingredients) {
				new Ingredients();
			}
			
			if (event.getSource() == recipes) {
				new Recipes();
			}
			
			if (event.getSource() == pedia) {
				new LiquorPedia();
			}
		};
		
		//close lambda function
		ingredients.addActionListener(act);
		recipes.addActionListener(act);
		pedia.addActionListener(act);
		
		//open GUI
		setVisible(true);
	}
	
	public static void resetRecipes() {
		//clear folder
		File recipes = new File("C:\\Users\\" + System.getProperty("user.name") + "\\Documents\\SBRecipes");
		if(recipes.exists()) recipes.delete();
		
		//create new folder
		File stock = new File("C:\\Users\\" + System.getProperty("user.name") + "\\Documents\\SBRecipes");
		stock.mkdir();
		
		//Set 3 basic recipes back in there
		try {
			//set up writers for the recipes
			FileWriter fw1 = new FileWriter("C:\\Users\\" + System.getProperty("user.name") + "\\Documents\\SBRecipes\\Margarita.txt");
			FileWriter fw2 = new FileWriter("C:\\Users\\" + System.getProperty("user.name") + "\\Documents\\SBRecipes\\Old Fashioned.txt");
			FileWriter fw3 = new FileWriter("C:\\Users\\" + System.getProperty("user.name") + "\\Documents\\SBRecipes\\White Russian.txt");
			BufferedWriter write1 = new BufferedWriter(fw1);
			BufferedWriter write2 = new BufferedWriter(fw2);
			BufferedWriter write3 = new BufferedWriter(fw3);
			
			//write margarita recipe
			write1.write("*Blanco Tequila,2.0");
			write1.newLine();
			write1.write("*Cointreau,0.5");
			write1.newLine();
			write1.write("*Orange Liqueur,0.5");
			write1.newLine();
			write1.write("Lime Juice,1.0");
			write1.newLine();
			write1.write("Instructions:");
			write1.newLine();
			write1.write("Shake ingredients, strain into margarita glass");
			write1.newLine();
			write1.write("Rim glass in lime juice and salt");
			write1.close();
			
			//write old fashioned recipe
			write2.write("*Bourbon,2.0");
			write2.newLine();
			write2.write("Simple Syrup,0.2");
			write2.newLine();
			write2.write("Angosutra Bitters,0.1");
			write2.newLine();
			write2.write("Instructions:");
			write2.newLine();
			write2.write("Stir ingredients into a stout glass with a large ice cube");
			write2.newLine();
			write2.write("Garnish with orange peel, and a burned cinnamon stick extinguished in the drink");
			write2.close();
			
			//white russian recipe
			write3.write("*Coffee Liqueur,1.5");
			write3.newLine();
			write3.write("*Vodka,0.5");
			write3.newLine();
			write3.write("Half and Half,1.0");
			write3.newLine();
			write3.write("Instructions:");
			write3.newLine();
			write3.write("Stir ingredients in a stout glass, like the Dude");
			write3.close();
		}catch(IOException ex) {
			ex.printStackTrace();
		}
	}
	
	private String getIntro() {
		String intro = "";
		
		//pick an intro line
		int introNum = (int)(Math.random()*6);
		if (introNum == 0) intro = "Remember to drink responsibly!";
		if (introNum == 1) intro = "Don't drink and drive!";
		if (introNum == 2) intro = "Drink a cup of water with your drinks!";
		if (introNum == 3) intro = "Beer before liquor, never been sicker";
		if (introNum == 4) intro = "Liquor before beer, you're in the clear";
		if (introNum == 5) intro = "If the liquid is brown, it makes you a clown";
		
		return intro;
	}
	
	public static void main(String[] args) {
		new Main();
	}
}