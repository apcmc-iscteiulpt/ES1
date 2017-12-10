package antiSpamFilter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import org.uma.jmetal.problem.impl.AbstractDoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;

import antiSpamFilterControl.AntiSpamFilterControl;

public class AntiSpamFilterProblem extends AbstractDoubleProblem {

	  public AntiSpamFilterProblem() {
	    // 10 variables (anti-spam filter rules) by default 
		// fazer o length do ficheiro Rules
	    this(10);
	  }

	  public AntiSpamFilterProblem(Integer numberOfVariables) {
	    setNumberOfVariables(numberOfVariables);
	    setNumberOfObjectives(2);
	    setName("AntiSpamFilterProblem");

	    List<Double> lowerLimit = new ArrayList<>(getNumberOfVariables()) ;
	    List<Double> upperLimit = new ArrayList<>(getNumberOfVariables()) ;

	    for (int i = 0; i < getNumberOfVariables(); i++) {
	      lowerLimit.add(-5.0);
	      upperLimit.add(5.0);
	    }

	    setLowerLimit(lowerLimit);
	    setUpperLimit(upperLimit);
	  }

	  public void evaluate(DoubleSolution solution){
		// Neste Método adicionar metodo que avalia a qualidade das pesos definidos
		HashMap<String, Double> hm = AntiSpamFilterControl.getHMRules();
	    double aux, xi, xj;
	    double[] fx = new double[getNumberOfObjectives()];
	    double[] x = new double[getNumberOfVariables()];
	    for (int i = 0; i < solution.getNumberOfVariables(); i++) {
	      x[i] = solution.getVariableValue(i) ;
	    }
	    File hamFile = AntiSpamFilterControl.getHamFile();
	    File spamFile = AntiSpamFilterControl.getSpamFile();
	    fx[0] = 0.0;
	    double counter = 0.0;
	    if(hamFile.exists()){
			Scanner scanner;
			try {
				scanner = new Scanner(hamFile);
				String line;
				while(scanner.hasNext()){
					counter = 0;
					line = scanner.nextLine();
					String[] parts = line.split("\\t");
					String auxString = "";
					for(int i = 1; i < parts.length; i++) {
						auxString = parts[i];
						int j = AntiSpamFilterControl.rulesList.indexOf(auxString);	
						counter = counter + Double.parseDouble(solution.getVariableValueString(j));
					}
					if(counter > 5)
						fx[0] += 1.0;
				}
				scanner.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
	    }
	    /*
	    for (int var = 0; var < solution.getNumberOfVariables() - 1; var++) {
		  fx[0] += Math.abs(x[0]); // Example for testing
	    }
	    */
	    fx[1] = 0.0;
	    if(spamFile.exists()){
			Scanner scanner;
			try {
				scanner = new Scanner(spamFile);
				String line;
				while(scanner.hasNext()){
					counter = 0;
					line = scanner.nextLine();
					String[] parts = line.split("\\t");
					String auxString = "";
					for(int i = 1; i < parts.length; i++) {
						auxString = parts[i];
						int j = AntiSpamFilterControl.rulesList.indexOf(auxString);	
						counter = counter + Double.parseDouble(solution.getVariableValueString(j));
					}
					if(counter <= 5)
						fx[1] += 1.0;
				}
				scanner.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
	    }
	    /*
	    for (int var = 0; var < solution.getNumberOfVariables(); var++) {
	    	fx[1] += Math.abs(x[1]); // Example for testing
	    }
	    */
	    solution.setObjective(0, fx[0]);
	    solution.setObjective(1, fx[1]);
	  }
	}
