package antiSpamFilterControl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import org.uma.jmetal.solution.DoubleSolution;

import antiSpamFilter.AntiSpamFilterProblem;

public final class AntiSpamFilterControl {

	static File rules = new File("rules.cf");
	static File ham = new File("ham.log");
	static File spam = new File("spam.log");
	static HashMap<String, Double> hmRules = new HashMap<String, Double>();
	static int falsePositiveManual;
	static int falseNegativeManual;
	int falsePositiveAuto;
	static int falseNegativeAuto;
	public static DoubleSolution manualResults;
	public static DoubleSolution autoResults;
	public static ArrayList<String> rulesList = new ArrayList<String>();
	
	public AntiSpamFilterControl() {
		falsePositiveManual = 0;
		falseNegativeManual = 0;
		falsePositiveAuto = 0;
		falseNegativeAuto = 0;
	}
		
		
	static void rulesToHM() throws FileNotFoundException{
		HashMap<String, Double> hm = new HashMap<String, Double>();
		if(rules.exists()){
			Scanner scanner = new Scanner(rules);
			String line;
			while(scanner.hasNext()){
				line = scanner.nextLine();
				String[] parts = line.split(" ");
				double value = 0.0;
				if(parts.length > 1)
					value = Double.parseDouble(parts[1]);
				hm.put(parts[0], value);
				rulesList.add(parts[0]);
			}
			scanner.close();
		}
		hmRules = hm;
	}
	
	public static HashMap<String, Double> getHMRules() {
		return hmRules;
	}
	
	public static File getHamFile() {
		return ham;
	}
	
	public static File getSpamFile() {
		return spam;
	}
	
	public static void setManualResults(DoubleSolution solution) {
		manualResults = solution;
	}
	
	public static int getIndexRule(String Rule) {
		
		
		return falseNegativeAuto;
		
	}
	
	public static void saveRulesFile() throws IOException {
		FileWriter rulesFile = new FileWriter(rules, false);
		PrintWriter saveRules = new PrintWriter(rulesFile);
		
		for (String line : hmRules.keySet()) {
			saveRules.write(line + " " + hmRules.get(line) + "\n");
		}
		rulesFile.close();
		saveRules.close();
	}
	
	public static void manualEvaluate() {
		falsePositiveManual = 0;
		falseNegativeManual = 0;
		double counter = 0.0;
	    if(ham.exists()){
			Scanner scanner;
			try {
				scanner = new Scanner(ham);
				String line;
				while(scanner.hasNext()){
					counter = 0;
					line = scanner.nextLine();
					String[] parts = line.split("\\t");
					for(int i = 1; i < parts.length; i++) {
						if(hmRules.containsKey(parts[i]))
							counter = counter + ((double) hmRules.get(parts[i]));
					}
					if(counter > 5)
						falseNegativeManual += 1;
				}
				scanner.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
	    }
	    
	    if(spam.exists()){
			Scanner scanner;
			try {
				scanner = new Scanner(spam);
				String line;
				while(scanner.hasNext()){
					counter = 0;
					line = scanner.nextLine();
					String[] parts = line.split("\\t");
					for(int i = 1; i < parts.length; i++) {
						if(hmRules.containsKey(parts[i]))
							counter = counter + ((double) hmRules.get(parts[i]));
					}
					if(counter <= 5)
						falsePositiveManual += 1;
				}
				scanner.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
	    }
		
	}
	
	
	
}


