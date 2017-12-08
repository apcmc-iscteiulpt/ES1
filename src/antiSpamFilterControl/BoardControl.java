package antiSpamFilterControl;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.io.FileUtils;

import antiSpamFilter.AntiSpamFilterProblem;

public class BoardControl {

	JFrame frame =  new JFrame("antiSpamFilter");
	private Dimension PanelsDimension = new Dimension(300, 400);
	private Dimension inputsDimension = new Dimension(150, 15);
	
	JTextField SpamToolsFile_Input;
	JTextField ValidMailsFile_Input;
	JTextField SpamMailsFile_Input;
	JButton GetFiles = new JButton("Get Files");
	
	JComboBox<String> CBRules = new JComboBox<String>();
	DefaultComboBoxModel<String> rulesCB;
	JTextField ruleValue;
	JButton manualTest = new JButton("Test Values");
	JButton manualSaveRuleValues = new JButton("Save configurations");
	JTextArea manualResults =  new JTextArea();
	
	JList<String> SpamToolsListAuto;
	
	JTextArea autoResults =  new JTextArea();
	String currRule = "";
	
	public BoardControl() {
		
		// Frame definition
		frame.setSize(300, 2000);
		frame.setLayout(new GridLayout(3, 1, 4, 2));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Files Configuration
		filesPanel();
		
		//Manual Configurations panel
		manualTestPanel();
		
		//Auto Configurations panel
		JPanel AutoPanel = new JPanel();
		AutoPanel.setSize(PanelsDimension);
		AutoPanel.setBackground(Color.green);
		frame.add(AutoPanel);
	}
	
	private void filesPanel() {
		JPanel FilesPanel = new JPanel();
		FilesPanel.setSize(PanelsDimension);
		FilesPanel.setBackground(Color.CYAN);
		FilesPanel.setLayout(new GridLayout(4, 2, 5, 3));
		
		//Spam Tools File - rules.cf
		SpamToolsFile_Input = new JTextField("rules.cf"); // 		/Users/Tiago/Downloads/rules.cf
		SpamToolsFile_Input.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		SpamToolsFile_Input.setSize(inputsDimension);
		FilesPanel.add(SpamToolsFile_Input);

		GetFiles.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent e){
					try {
						copyFiles(BoardControl.getFile(SpamToolsFile_Input.getText()), AntiSpamFilterControl.rules);
						AntiSpamFilterControl.rulesToHM();
						for (String rule : AntiSpamFilterControl.hmRules.keySet()) {
							rulesCB.addElement(rule);
						}
						copyFiles(BoardControl.getFile(ValidMailsFile_Input.getText()), AntiSpamFilterControl.ham);
						copyFiles(BoardControl.getFile(SpamMailsFile_Input.getText()), AntiSpamFilterControl.spam);
						startFilesConfig(false);
						startSpamFilterTest(true);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		);
		
		frame.add(FilesPanel);
		
		//Valid Emails - ham.log   
		ValidMailsFile_Input = new JTextField("ham.log");
		ValidMailsFile_Input.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		ValidMailsFile_Input.setSize(inputsDimension);
		FilesPanel.add(ValidMailsFile_Input);
		
		//Spam Emails - spam.log
		SpamMailsFile_Input = new JTextField("spam.log");
		SpamMailsFile_Input.setBorder(BorderFactory.createLineBorder(Color.GREEN));
		SpamMailsFile_Input.setSize(inputsDimension);
		FilesPanel.add(SpamMailsFile_Input);
		
		//Get Files Button
		FilesPanel.add(GetFiles);
	}
	
	private void manualTestPanel() {
		JPanel ManualPanel = new JPanel();
		ManualPanel.setSize(PanelsDimension);
		ManualPanel.setBackground(Color.darkGray);
		ManualPanel.setLayout(new GridLayout(4, 2, 5, 3));

		rulesCB = new DefaultComboBoxModel<String>();
		CBRules.setModel(rulesCB);
		ruleValue = new JTextField();
		
		ActionListener selectRuleCB = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setValueToRule();
			}
		};
		
		CBRules.addActionListener(selectRuleCB);
		
		manualTest.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent e){
					AntiSpamFilterProblem solver = new AntiSpamFilterProblem(AntiSpamFilterControl.hmRules.size());
					AntiSpamFilterControl.setManualResults(solver.createSolution());
					solver.evaluate(AntiSpamFilterControl.manualResults);
					setResultString("manual");
				}
			}
		);
		
		manualSaveRuleValues.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent e){
					try {
						setValueToRule();
						AntiSpamFilterControl.saveRulesFile();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		);
		
		manualResults.setEnabled(false);
		ManualPanel.add(CBRules);
		ManualPanel.add(ruleValue);
		ManualPanel.add(manualTest);
		ManualPanel.add(manualSaveRuleValues);
		ManualPanel.add(manualResults);
		frame.add(ManualPanel);
	}
	
	private static File getFile(String pathFile) {
		if(!pathFile.isEmpty()){
			return new File(pathFile);
		}
		return null;
	}
	
	private static void copyFiles(File source, File dest) throws IOException {
		if(!source.equals(dest))
			FileUtils.copyFile(source, dest);
	}
	
	
	private void setResultString(String testType) {
		switch(testType) {
			case "manual": 
//				manualResults.setText("Falsos positivos:" + AntiSpamFilterControl.manualResults.getVariableValueString(1) + " Falsos Negativos:" + AntiSpamFilterControl.manualResults.getVariableValueString(0));
				manualResults.setText("FP :" + AntiSpamFilterControl.manualResults.getObjective(1) + 
									" FN:" +  AntiSpamFilterControl.manualResults.getObjective(0));
			case "auto": 
				/*autoResults.setText("FP:" + AntiSpamFilterControl.autoResults.getObjective(1) + 
								  " FN:" + AntiSpamFilterControl.autoResults.getObjective(0));*/
		}
	}
	
	private void setValueToRule() {
		if(!currRule.isEmpty())
			AntiSpamFilterControl.hmRules.put(currRule, Double.parseDouble(ruleValue.getText()));
		currRule = CBRules.getSelectedItem().toString();
		ruleValue.setText(Double.toString(AntiSpamFilterControl.hmRules.get(currRule)));
	}
	
	private void start() {
		frame.setVisible(true);
		startFilesConfig(true);
		startSpamFilterTest(false);
	}
	
	private void startFilesConfig(Boolean bool) {
		GetFiles.setEnabled(bool);
		SpamToolsFile_Input.setEnabled(bool);
		ValidMailsFile_Input.setEnabled(bool);
		SpamMailsFile_Input.setEnabled(bool);
	}
	
	private void startSpamFilterTest(Boolean bool) {
		CBRules.setEnabled(bool);
		ruleValue.setEnabled(bool);
		manualTest.setEnabled(bool);
		manualSaveRuleValues.setEnabled(bool);
	}
	
	public static void main(String[] args) {
		BoardControl board = new BoardControl();
		board.start();
	}
	

}
