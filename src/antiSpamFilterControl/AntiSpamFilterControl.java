package antiSpamFilterControl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Scanner;

import org.uma.jmetal.solution.DoubleSolution;

import antiSpamFilter.AntiSpamFilterProblem;

public final class AntiSpamFilterControl {

	static File rules = new File("rules.cf");
	static File ham = new File("ham.log");
	static File spam = new File("spam.log");
	static HashMap<String, Double> hmRules = new HashMap<String, Double>();
	int falsePositiveManual;
	int falseNegativeManual;
	int falsePositiveAuto;
	int falseNegativeAuto;
	public static DoubleSolution manualResults;
	public static DoubleSolution autoResults;
	
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
	
	public static void saveRulesFile() throws IOException {
		FileWriter rulesFile = new FileWriter(rules, false);
		PrintWriter saveRules = new PrintWriter(rulesFile);
		
		for (String line : hmRules.keySet()) {
			saveRules.write(line + " " + hmRules.get(line) + "\n");
		}
		rulesFile.close();
		saveRules.close();
	}
	
	
	
}


