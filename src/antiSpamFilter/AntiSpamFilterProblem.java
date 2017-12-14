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
	    this(15);
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
	    double aux, xi, xj;
	    double[] fx = new double[getNumberOfObjectives()];
	    double[] x = new double[getNumberOfVariables()];
	    ArrayList<Double> list = new ArrayList<Double>();
	    for (int i = 0; i < solution.getNumberOfVariables(); i++) {
	    		list.add(solution.getVariableValue(i));
	    }
	    AntiSpamFilterControl.Evaluater(list, false);
	    
	    fx[0] = AntiSpamFilterControl.falsePositiveAuto;
	    fx[1] = AntiSpamFilterControl.falseNegativeAuto;

	    solution.setObjective(0, fx[0]);
	    solution.setObjective(1, fx[1]);
	  }
	}
