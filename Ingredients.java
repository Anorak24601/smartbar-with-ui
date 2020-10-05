package smartBarUI;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.Arrays;

@SuppressWarnings("serial")
public class Ingredients extends JFrame {
	//set first pane, label for add an ingredient
	JPanel pane1 = new JPanel();
	JLabel addIngLabel = new JLabel("Add Ingredient:");
	
	//second pane: fields to add ingredient
	JPanel pane2 = new JPanel();
	JLabel ingName1 = new JLabel("Ingredient name: ");
	JTextField ingNameField1 = new JTextField("", 10);
	JLabel ingAmt1 = new JLabel("Amount in oz: ");
	JTextField amtField1 = new JTextField("", 10);
	JButton add = new JButton("Add");
		
	//third pane: label for update ingredient
	JPanel pane3 = new JPanel();
	JLabel updateIngLabel = new JLabel("Update Ingredient");
		
	//fourth pane: fields to update ingredient
	JPanel pane4 = new JPanel();
	JLabel ingName2 = new JLabel("Ingredient name: ");
	JTextField ingNameField2 = new JTextField("", 10);
	JLabel ingAmt2 = new JLabel("Amount in oz: ");
	JTextField amtField2 = new JTextField("", 10);
	JButton update = new JButton("Update");
		
	//fifth pane: label for all ingredients
	JPanel pane5 = new JPanel();
	JLabel allIng = new JLabel("All Ingredients");
		
	//sixth pane: ingredients
	JPanel pane6 = new JPanel();
	JTextArea allIngs = new JTextArea(10, 15);
	JScrollPane scroll = new JScrollPane(allIngs);
	
	//the actual class method
	public Ingredients() {
		//basic UI stuff
		super("Ingredients");
		setSize(550,450);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		FlowLayout flo = new FlowLayout();
		setLayout(flo);
		
		//add first pane in
		pane1.add(addIngLabel);
		add(pane1);
		
		//add second pane in
		pane2.add(ingName1);
		pane2.add(ingNameField1);
		pane2.add(ingAmt1);
		pane2.add(amtField1);
		pane2.add(add);
		pane2.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		add(pane2);
		
		//add third pane in
		pane3.add(updateIngLabel);
		add(pane3);
		
		//add fourth pane in
		pane4.add(ingName2);
		pane4.add(ingNameField2);
		pane4.add(ingAmt2);
		pane4.add(amtField2);
		pane4.add(update);
		pane4.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		add(pane4);
		
		//add fifth pane in
		pane5.add(allIng);
		add(pane5);
		
		//add sixth pane in
		allIngs.setText(textArea());
		allIngs.setCaretPosition(0);
		allIngs.setEditable(false);
		pane6.add(scroll);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		add(pane6);
		
		//set button behaviors
		ActionListener act = (event) -> {
			if (event.getSource() == add) {
				addIngredient(ingNameField1.getText(), Double.parseDouble(amtField1.getText()));
				allIngs.setText(textArea());
				ingNameField1.setText(null);
				amtField1.setText(null);
			}
			if (event.getSource() == update) {
				updateIngredient(ingNameField2.getText(), Double.parseDouble(amtField2.getText()));
				allIngs.setText(textArea());
				ingNameField2.setText(null);
				amtField2.setText(null);
			}
		};
		
		//finish lambda function
		add.addActionListener(act);
		update.addActionListener(act);
		
		//open frame
		setVisible(true);
	}
	
	//add an ingredient
	public static void addIngredient(String name, double amt) {
		String[][] list1 = getIngredients();
		String[][] list2 = new String[list1.length + 1][2];
		
		//set new list values equal to the first, except for the ones that will be added
		for (int i = 0; i < list1.length; i++) {
			for (int j = 0; j < list1[i].length; j++) list2[i][j] = list1[i][j];
		}
		
		//put new value in
		list2[list1.length][0] = name;
		list2[list1.length][1] = Double.toString(amt);
		
		//get rid of old ingredient list, make a new one
		File i = new File("C:\\Users\\" + System.getProperty("user.name") + "\\Ingredients.txt");
		i.delete();
		try {
			FileWriter newI = new FileWriter("C:\\Users\\" + System.getProperty("user.name") + "\\Ingredients.txt");
			BufferedWriter write = new BufferedWriter(newI);
			
			for (int j = 0; j < list2.length; j++) {
				write.write(list2[j][0] + "," + list2[j][1]);
				if (j < list2.length - 1) write.newLine();
			}
			
			write.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	private static String textArea() {
		String text = "";
		String[][] ings = getIngredients();
		
		for (int i = 0; i < ings.length; i++) {
			if (i < ings.length - 1) text += ings[i][0] + " - " + ings[i][1] + "\n";
			else text += ings[i][0] + " - " + ings[i][1];
		}
		
		return text;
	}
	
	//update ingredient
	public static void updateIngredient(String ing, double newVal) {
		//create basic arrays
		String[][] all = getIngredients();
		String[] ingredients = new String[all.length];
		double[] amts = new double[all.length];
		
		//populate ingredient and amount arrays
		for (int i = 0; i < all.length; i++) {
			ingredients[i] = all[i][0];
			amts[i] = Double.parseDouble(all[i][1]);
		}
		
		//only do this if the ingredient exists
		if (Arrays.asList(ingredients).contains(ing)) {
			//find where it is
			int index = 0;
			for (int i = 0; i < amts.length; i++) {
				if (ingredients[i].equals(ing)) {
					amts[i] = newVal;
					index = i;
				}
			}
			
			if(amts[index] == 0) {
				all = removeIngredient(all, index);
			}
			
			//to defeat a bug
			if (all[all.length-1][1].equals("0.0")) all = removeIngredient(all, all.length-1);
			
			//update values of 'all'
			all[index][1] = Double.toString(amts[index]);
		} else new Error("Ingredient\"" + ing + "\" does not exist");
		
		//delete old ingredients file
		File file = new File("C:\\Users\\" + System.getProperty("user.name") + "\\Ingredients.txt");
		file.delete();
		
		//create new ingredients file
		try {
			FileWriter fw = new FileWriter("C:\\Users\\" + System.getProperty("user.name") + "\\Ingredients.txt");
			BufferedWriter write = new BufferedWriter(fw);
			
			for (int i = 0; i < all.length; i++) {
				write.write(all[i][0] + "," + all[i][1]);
				if (i < all.length - 1) write.newLine();
			}
			
			write.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
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
	
	public static String[][] removeIngredient(String[][] arr, int index) { 
		String[][] newArr = new String[arr.length][2]; 
		
		for (int i = 0, j = 0; i < arr.length; i++) {
			if (i == index) continue;
			newArr[j][0] = arr[i][0];
			newArr[j][1] = arr[i][1];
			j++;
		}
		
		return newArr;
	}
}