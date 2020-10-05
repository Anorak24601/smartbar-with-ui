package smartBarUI;

import javax.swing.*;
import java.awt.FlowLayout;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

@SuppressWarnings("serial")
public class SpecificRecipe extends JFrame {
	
	//UI elements
	JTextArea recipe = new JTextArea(12,28);
	JScrollPane scroll = new JScrollPane(recipe);
	JButton make = new JButton("Make this recipe");
	
	public SpecificRecipe(String recipeName) {
		//basics of the UI
		super(recipeName + " Recipe");
		setSize(100,300);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		FlowLayout flo = new FlowLayout();
		setLayout(flo);
		
		//fill the label
		recipe.setText(getRecipe(recipeName));
		recipe.setEditable(false);
		recipe.setCaretPosition(0);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		//add elements
		add(scroll);
		add(make);
		
		//set button behavior
		make.addActionListener(e -> {
			if (isCreatable(recipeName)) {
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
				
				//subtract amounts from the ingredients
				for (int i = 0; i < iList.length; i++) {
					for (int j = 0; j < rList.length; j++) {
						if (iList[i].equals(rList[j])) iAmts[i] -= rAmts[j];
					}
				}
				
				//rewrite ingredients file, starting by deleting the old one
				File ingredience = new File("C:\\Users\\" + System.getProperty("user.name") + "\\Ingredients.txt");
				ingredience.delete();
				
				//write new amounts in a new file
				try {
					FileWriter fw = new FileWriter("C:\\Users\\" + System.getProperty("user.name") + "\\Ingredients.txt");
					BufferedWriter write = new BufferedWriter(fw);
					
					for (int i = 0; i < iList.length; i++) {
						write.write(iList[i] + "," + iAmts[i]);
						if (i < iList.length - 1) write.newLine();
					}
					
					write.close();
				} catch (IOException ex) {
					
				}
			}
		});
		
		//open the UI
		setVisible(true);
	}
	
	//get the quote if it exists
	private static String getQuote(String recipeName) {
		String quote = "";
		
		try{
			FileReader recipe = new FileReader("C:\\Users\\" + System.getProperty("user.name") + "\\Documents\\SBRecipes\\" + recipeName + ".txt");
			BufferedReader read = new BufferedReader(recipe);
			
			//keeps track of if the quote is there
			boolean hasQuote = false;
			
			String line = read.readLine();
			
			boolean first = false;
			while (line != null) {
				//do this one first so as to not print dashes
				if (hasQuote && first) {
					quote += line;
					first = false;
				}
				else if (hasQuote) quote += "\n" + line;
				//the dashes indicate a quote beneath
				if (line.equals("-----")) {
					quote += "\n\n\"";
					hasQuote = true;
					first = true;
				}
				line = read.readLine();
			}
			
			//endquotes
			if(hasQuote) quote += "\"";
			
			read.close();
		} catch(IOException ex) {
			
		}
		
		return quote;
	}
	
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
						creatable = false;
						break;
					} 
					//these 3 nested ifs check if there is not enough of an ingredient
					else if (rList[j].equals(iList[k])) {
						if (iAmts[k] < rAmts[j]) {
							new Error("Not enough of ingredient: " + rList[j]);
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
	
	private String getRecipe(String recipeName) {
		
		String recipe = "";
		
		//grab ingredients and instructions
		String[][] ingredients = getRecipeIngredients(recipeName);
		String[] instructions = getRecipeInstructions(recipeName);
		
		//print size of the drink
		double total = 0;
		for (String[] i : ingredients) total += Double.parseDouble(i[1]);
		recipe += ("Size of drink: " + total + " oz\n");
		if (total >= 8) recipe += (", or " + (total/8) + " cups\n");
		else recipe += "\n";
		
		//print ingredients
		recipe += ("Ingredients:\n");
		for (String[] i : ingredients) {
			if (i[0].contains("Egg")) recipe += ("-" + i[1] + " " + i[0] + "\n");
			//dont display alcohol asterisk
			else if (i[0].charAt(0) == '*') recipe += ("-" + i[1] + " oz. " + i[0].substring(1) + "\n");
			else recipe += ("-" + i[1] + " oz. " + i[0] + "\n");
		}
		
		//space between ingredients and instructions
		recipe += "\n";
		
		//print instructions
		for (int i = 0; i < instructions.length; i++) {
			if (i > 0) recipe += (i + ". ");
			recipe += (instructions[i]);
			if(i < instructions.length - 1) recipe += "\n";
		}
		
		//print quote
		recipe += getQuote(recipeName);
		
		return recipe;
	}
	
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
		System.out.println("This recipe is not saved in your SBRecipes folder, at least not as a .txt document");
		return null;
	}
	
	public static String[] getRecipeInstructions(String recipeName) {
		File check = new File("C:\\Users\\" + System.getProperty("user.name") + "\\Documents\\SBRecipes\\" + recipeName + ".txt");
		if (check.exists()) {
			try {
				FileReader r = new FileReader("C:\\Users\\" + System.getProperty("user.name") + "\\Documents\\SBRecipes\\" + recipeName + ".txt");
				BufferedReader read = new BufferedReader(r);
				
				//so the while loop can evaluate and operations can still be performed
				String line = read.readLine();
				//this variable makes sure only steps are added
				boolean addStep = false;
				
				//just for counting steps
				int counter = 0;
				while (line != null) {
					if (line.equals("Instructions:")) addStep = true;
					if (line.equals("-----")) break;
					if (addStep) counter++;
					line = read.readLine();
				}
				read.close();
				
				//now read it back
				FileReader fr = new FileReader("C:\\Users\\" + System.getProperty("user.name") + "\\Documents\\SBRecipes\\" + recipeName + ".txt");
				BufferedReader reader = new BufferedReader(fr);
				
				addStep = false;
				line = reader.readLine();
				String[] instructions = new String[counter];
				int add = 0;
				while (line != null) {
					if (line.equals("Instructions:")) addStep = true;
					if (line.equals("-----")) break;
					if (addStep) {
						instructions[add] = line;
						add++;
					}
					line = reader.readLine();
				}
				reader.close();
				
				//return the array
				return instructions;
				
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		System.out.println("This recipe is not saved in your SBRecipes folder, at least not as a .txt document");
		return null;
	}
	
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
