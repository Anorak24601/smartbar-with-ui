package smartBarUI;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class Recipes extends JFrame {
	//First panel, buttons to limit displayed recipes
	JPanel pane1 = new JPanel();
	JButton all = new JButton("All");
	JButton alc = new JButton("Alcoholic");
	JButton nonAlc = new JButton("Non-Alcoholic");
	JButton makeable = new JButton("Makeable");
	
	//had to add this pane for search function
	JPanel pane = new JPanel();
	JTextField search = new JTextField("", 10);
	JButton searchButton = new JButton("<- Search Ingredient");
	
	//Second panel, display of recipes
	JPanel pane2 = new JPanel();
	JTextArea recipes = new JTextArea(10, 18);
	JLabel list = new JLabel("List of Recipes:");
	JScrollPane scroll = new JScrollPane(recipes);
	
	//Third panel, options to add recipe and view specific recipe
	JPanel pane3 = new JPanel();
	JButton add = new JButton("Add Recipe");
	JTextField view = new JTextField("", 10);
	JButton viewButton = new JButton("<- View Recipe");
	
	//Central method
	public Recipes() {
		
		//basic UI stuff
		super("Recipes");
		setSize(425,375);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		FlowLayout flo = new FlowLayout();
		setLayout(flo);
		
		//add pane 1
		pane1.add(all);
		pane1.add(alc);
		pane1.add(nonAlc);
		pane1.add(makeable);
		pane1.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		add(pane1);
		
		//add search pane
		pane.add(search);
		pane.add(searchButton);
		add(pane);
		
		//add pane 2
		pane2.add(list);
		recipes.setEditable(false);
		recipes.setText(allRecipes());
		recipes.setCaretPosition(0); //this makes the scroll bar start at the top
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		pane2.add(scroll);
		pane2.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		add(pane2);
		
		//add pane 3
		pane3.add(view);
		pane3.add(viewButton);
		pane3.add(add);
		add(pane3);
		
		//set what the buttons do
		ActionListener act = (event) -> {
			if (event.getSource() == all) {
				recipes.setText(allRecipes());
				recipes.setCaretPosition(0);
			}
			
			if(event.getSource() == alc) {
				recipes.setText(alcRecipes());
				recipes.setCaretPosition(0);
			}
			
			if (event.getSource() == nonAlc) {
				recipes.setText(nonAlcRecipes());
				recipes.setCaretPosition(0);
			}
			
			//list makeable recipes
			if(event.getSource() == makeable) {
				//this is the returned string
				String all = "Completely Makeable:\n";
				
				File recipe = new File("C:\\Users\\" + System.getProperty("user.name") + "\\Documents\\SBRecipes");
				File[] list = recipe.listFiles();
				
				for (int i = 0; i < list.length; i++) {
					int split = list[i].getName().indexOf('.');
					String add = list[i].getName().substring(0, split);
					if (isCreatable(add)) all += "-" + add + "\n";
				}
				
				//split completely makeable and ones that you have all the alcohol for
				all += "\nHave Alcohol Components:";
				
				for (int i = 0; i < list.length; i++) {
					int split = list[i].getName().indexOf('.');
					String add = list[i].getName().substring(0, split);
					if (haveAlcohol(add) && !isNonAlc(add + ".txt")) all += "\n-" + add;
				}
				
				recipes.setText(all);
				recipes.setCaretPosition(0);
			}
			
			if (event.getSource() == searchButton) {
				recipes.setText(searchFor(search.getText()));
				recipes.setCaretPosition(0);
				search.setText("");
			}
			
			if (event.getSource() == add) {
				new AddRecipe();
			}
			
			if (event.getSource() == viewButton) {
				File r = new File("C:\\Users\\" + System.getProperty("user.name") + "\\Documents\\SBRecipes\\" + view.getText() + ".txt");
				if (r.exists()) {
					new SpecificRecipe(view.getText());
				} else new Error("The recipe \"" + view.getText() + "\" does not exist in your SBRecipes folder");
				view.setText("");
			}
		};
		
		//finish lambda function
		all.addActionListener(act);
		alc.addActionListener(act);
		nonAlc.addActionListener(act);
		makeable.addActionListener(act);
		searchButton.addActionListener(act);
		add.addActionListener(act);
		viewButton.addActionListener(act);
		
		//start up the UI
		setVisible(true);
	}
	
	//returns a string of non-alcoholic drinks stored
	public static String alcRecipes() {
		File folder = new File("C:\\Users\\" + System.getProperty("user.name") + "\\Documents\\SBRecipes");
		File[] list = folder.listFiles();
		
		//set variables
		boolean used = false;
		String all = "";
		for (File i : list) {
			if(!isNonAlc(i.getName())) {
				int index = i.getName().indexOf('.');
				
				//only add new line if it is not the first one
				if (!used) {
					all += ("-" + i.getName().substring(0, index));
					used = true;
				} else all += ("\n-" + i.getName().substring(0, index));
			}
		}
		
		return all;
	}
	
	//returns a string of non-alcoholic drinks stored
	public static String nonAlcRecipes() {
		File folder = new File("C:\\Users\\" + System.getProperty("user.name") + "\\Documents\\SBRecipes");
		File[] list = folder.listFiles();
		
		//set variables
		boolean used = false;
		String all = "";
		for (File i : list) {
			if(isNonAlc(i.getName())) {
				int index = i.getName().indexOf('.');
				
				//only add new line if it is not the first one
				if (!used) {
					all += ("-" + i.getName().substring(0, index));
					used = true;
				} else all += ("\n-" + i.getName().substring(0, index));
			}
		}
		
		return all;
	}
	
	//checks if a recipe is non-alcoholic
	public static boolean isNonAlc(String recipeName) {
		
		try {
			FileReader fr = new FileReader("C:\\Users\\" + System.getProperty("user.name") + "\\Documents\\SBRecipes\\" + recipeName);
			@SuppressWarnings("resource")
			BufferedReader read = new BufferedReader(fr);
			 
			//string to read lines
			String line = read.readLine();
			
			//if the line has alcohol asterisk, return false
			while (line != null) {
				if (line.charAt(0) == '*') return false;
				line = read.readLine();
			}
			 
			read.close();
		} catch (IOException ex) {
			 
		}
		
		//only gets here if it is non-alc
		return true;
	}
	
	//shows whether a recipe can be made
	public static boolean isCreatable(String recipeName) {
		File recipe = new File("C:\\Users\\" + System.getProperty("user.name") + "\\Documents\\SBRecipes\\" + recipeName + ".txt");
		
		//continue only if the recipe exists
		if (recipe.exists()) {
			//get lists of content
			String[][] ingr = getIngredients();
			String[][] ring = getRecipeIngredients(recipeName);
			
			//making arrays for all ingredients and amounts in both cabinet and recipe
			String[] iList = new String[ingr.length];
			double[] iAmts = new double[ingr.length];
			String[] rList = new String[ring.length];
			double[] rAmts = new double[ring.length];
			
			//populate ingredient arrays
			for (int j = 0; j < ingr.length; j++) {
				iList[j] = ingr[j][0];
				iAmts[j] = Double.parseDouble(ingr[j][1]);
			}
			
			//populate recipe arrays
			for (int j = 0; j < ring.length; j++) {
				//take out alcohol asterisks
				if (ring[j][0].charAt(0) == '*') rList[j] = ring[j][0].substring(1);
				else rList[j] = ring[j][0];
				rAmts[j] = Double.parseDouble(ring[j][1]);
			}
			
		//this variable is here so that all missing and not enough ingredients are printed, but it still returns false if needed
			boolean creatable = true;
			
			//goes through recipe ingredients
			for (int j = 0; j < ring.length; j++) {
				//goes through ingredients
				for (int k = 0; k < ingr.length; k++) {
					//sees if the ingredient exists at all
					if (!Arrays.asList(iList).contains(rList[j])) {
						//System.out.println("Missing ingredient: " + rList[j]);
						creatable = false;
						break;
					} 
					//these 3 nested ifs check if there is not enough of an ingredient
					else if (rList[j].equals(iList[k])) {
						if (iAmts[k] < rAmts[j]) {
							//System.out.println("Not enough of ingredient: " + rList[j]);
							creatable = false;
							break;
						}
					}
				}
			}
			
			//return of whether it is creatable
			return creatable;
			
		} else new Error("Recipe " + recipeName + " does not exist");
		
		//only happens if it does not exist
		return false;
	}
	
	//shows whether 
	public static boolean haveAlcohol(String recipeName) {
		File recipe = new File("C:\\Users\\" + System.getProperty("user.name") + "\\Documents\\SBRecipes\\" + recipeName + ".txt");
		
		//continue only if the recipe exists
		if (recipe.exists()) {
			//get lists of content
			String[][] ingr = getIngredients();
			String[][] ring = getRecipeIngredients(recipeName);
			
			//making arrays for all ingredients and amounts in both cabinet and recipe
			String[] iList = new String[ingr.length];
			double[] iAmts = new double[ingr.length];
			String[] rList = new String[ring.length];
			double[] rAmts = new double[ring.length];
			
			//populate ingredient arrays
			for (int j = 0; j < ingr.length; j++) {
				iList[j] = ingr[j][0];
				iAmts[j] = Double.parseDouble(ingr[j][1]);
			}
			
			//populate recipe arrays
			for (int j = 0; j < ring.length; j++) {
				rList[j] = ring[j][0];
				rAmts[j] = Double.parseDouble(ring[j][1]);
			}
			
		//this variable is here so that all missing and not enough ingredients are printed, but it still returns false if needed
			boolean creatable = true;
			
			//goes through recipe ingredients
			for (int j = 0; j < ring.length; j++) {
				//only check it if the component is alcohol
				if (rList[j].charAt(0) == '*') {
					//since it is checked, take out asterisk to prevent later errors
					rList[j] = rList[j].substring(1);
					
					//goes through ingredients
					for (int k = 0; k < ingr.length; k++) {
						//sees if the ingredient exists at all
						if (!Arrays.asList(iList).contains(rList[j])) {
							creatable = false;
							break;
						} 
						//these 3 nested ifs check if there is not enough of an ingredient
						else if (rList[j].equals(iList[k])) {
							if (iAmts[k] < rAmts[j]) {
								creatable = false;
								break;
							}
						}
					}
				}
			}
			
			//return of whether it is creatable
			return creatable;
			
		} else new Error("Recipe " + recipeName + " does not exist");
		
		//only happens if it does not exist
		return false;
	}
	
	//get all recipe names
	public static String allRecipes() {
		String all = "";
		
		File recipes = new File("C:\\Users\\" + System.getProperty("user.name") + "\\Documents\\SBRecipes");
		File[] list = recipes.listFiles();
		
		for (int i = 0; i < list.length; i++) {
			int split = list[i].getName().indexOf('.');
			all += "-" + list[i].getName().substring(0, split);
			if (i < list.length - 1) all += "\n";
		}
		
		return all;
	}
	
	//get recipe names by ingredient
	
	public static String searchFor(String in) {
		File recipes = new File("C:\\Users\\" + System.getProperty("user.name") + "\\Documents\\SBRecipes");
		File[] list = recipes.listFiles();
		
		//arraylist to hold names of recipes
		ArrayList<String> names = new ArrayList<String>();
		String all = "";
		
		for (int i = 0; i < list.length; i++) {
			//name of current recipe
			String name = list[i].getName();
			name = name.substring(0, name.indexOf('.'));
			
			//get ingredients
			String[][] ings = getRecipeIngredients(name);
			String[] ingredients = new String[ings.length];
			for (int j = 0; j < ings.length; j++) ingredients[j] = ings[j][0];
			
			//check if they have the ingredient (or a type of ingredient, like amber rum if search rum)
			//add the name if they do
			for (int j = 0; j < ingredients.length; j++) {
				if (ingredients[j].contains(in)) {
					names.add(name);
					break;
				}
			}
		}
		
		//add each recipe to the all string
		for (int i = 0; i < names.size(); i++) {
			all += "-" + names.get(i);
			if (i < names.size() - 1) all += "\n";
		}
		
		//return names
		return all;
	}
	
	//returns the ingredients and amounts for a specific recipe
	public static String[][] getRecipeIngredients(String recipeName) {
		File ingredience = new File("C:\\Users\\" + System.getProperty("user.name") + "\\Documents\\SBRecipes\\" + recipeName + ".txt");
		
		if (ingredience.exists()) {
			ArrayList<String[]> ingredients = new ArrayList<String[]>();
			try {
				FileReader r = new FileReader("C:\\Users\\" + System.getProperty("user.name") + "\\Documents\\SBRecipes\\" + recipeName + ".txt");
				BufferedReader read = new BufferedReader(r);
				
				//adds String arrays into the ArrayList
				String line = read.readLine();
				while (line != null) {
					if(line.contentEquals("Instructions:")) break;
					int index = line.indexOf(',');
					String[] ingredient = {line.substring(0, index), line.substring(index + 1)};
					ingredients.add(ingredient);
					line = read.readLine();
				}
				
				//turn it into an array
				String[][] product = new String[ingredients.size()][2];
				for (int i = 0; i < product.length; i++) {
					product[i][0] = ingredients.get(i)[0];
					product[i][1] = ingredients.get(i)[1];
				}
				read.close();
				return product;
			} catch(IOException ex) {
				ex.printStackTrace();
			}
		}
		new Error("You do not have this recipe saved");
		return null;
	}
	
	//get current ingredients
	public static String[][] getIngredients() {
		File ingredience = new File("C:\\Users\\" + System.getProperty("user.name") + "\\Ingredients.txt");
		
		if (ingredience.exists()) {
			try {
				FileReader fr = new FileReader("C:\\Users\\" + System.getProperty("user.name") + "\\Ingredients.txt");
				BufferedReader count = new BufferedReader(fr);
				
				//count lines
				int lines = 0;
				while (count.readLine() != null) lines++;
				count.close();
				
				//read file
				FileReader fr2 = new FileReader("C:\\Users\\" + System.getProperty("user.name") + "\\Ingredients.txt");
				BufferedReader read = new BufferedReader(fr2);
				String[][] ingredient = new String[lines][2];
				for (int i = 0; i < lines; i++) {
					String line = read.readLine();
					int index = line.indexOf(',');
					ingredient[i][0] = line.substring(0, index);
					ingredient[i][1] = line.substring(index + 1);
				}
				
				read.close();
				return ingredient;
			} catch(IOException ex) {
				ex.printStackTrace();
			}
		}
		return null;
	}
}