package antiSpamFilterControl;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import org.apache.commons.io.FileUtils;

import antiSpamFilter.AntiSpamFilterAutomaticConfiguration;

public class BoardControl {

	JFrame frame =  new JFrame("antiSpamFilter");
	private Dimension PanelsDimension = new Dimension(500, 400);
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
	
	JComboBox<String> autoCBRules = new JComboBox<String>();
	DefaultComboBoxModel<String> autoRulesCB;
	JTextField autoRuleValue;
	JButton autoTest = new JButton("Test Values");
	JButton autoSaveRuleValues = new JButton("Save configurations");
	JTextArea autoResults =  new JTextArea();
	
	JList<String> SpamToolsListAuto;
	
	String currRule = "";
	
	public BoardControl() {
		
		// Frame definition
		frame.setSize(500, 2000);
		frame.setLayout(new GridLayout(3, 1, 4, 2));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Files Configuration
		filesPanel();
		
		//Manual Configurations panel
		manualTestPanel();
		
		//Auto Configurations panel
		autoTestPanel();
		/*
		JPanel AutoPanel = new JPanel();
		AutoPanel.setSize(PanelsDimension);
		AutoPanel.setBackground(Color.green);
		frame.add(AutoPanel);*/
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
						AntiSpamFilterControl.treatRulesFile();
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
					setValueToRule();
					AntiSpamFilterControl.Evaluater(true);
					setResultString("manual");
				}
			}
		);
		
		manualSaveRuleValues.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent e){
					try {
						setValueToRule();
						AntiSpamFilterControl.saveRulesFile(true);
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

	private void autoTestPanel() {
		JPanel autoPanel = new JPanel();
		autoPanel.setSize(PanelsDimension);
		autoPanel.setBackground(Color.darkGray);
		autoPanel.setLayout(new GridLayout(4, 2, 5, 3));

		autoRulesCB = new DefaultComboBoxModel<String>();
		autoCBRules.setModel(autoRulesCB);
		autoRuleValue = new JTextField();
		
		ActionListener selectRuleCB = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				autoRuleValue.setText(AntiSpamFilterControl.getWeigthByRule(autoRulesCB.getSelectedItem().toString(), false) + "");
			}
		};
		
		autoCBRules.addActionListener(selectRuleCB);
		
		autoTest.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent e){
					//Call evaluate Here!!!!
					AntiSpamFilterAutomaticConfiguration w  = new AntiSpamFilterAutomaticConfiguration();
					try {
						w.run(AntiSpamFilterControl.ruleList.size());
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					System.out.println("Configuração automática terminou!");
				}
			}
		);
		
		autoSaveRuleValues.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent e){
					try {
						setValueToRule();
						AntiSpamFilterControl.saveRulesFile(false);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		);
		autoRuleValue.setEditable(false);
		autoResults.setEnabled(false);
		autoPanel.add(autoCBRules);
		autoPanel.add(autoRuleValue);
		autoPanel.add(autoTest);
		autoPanel.add(autoSaveRuleValues);
		autoPanel.add(autoResults);
		frame.add(autoPanel);
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
				manualResults.setText("FP :" + AntiSpamFilterControl.falsePositiveManual + 
									" FN:" +  AntiSpamFilterControl.falseNegativeManual);
			case "auto": 
				/*autoResults.setText("FP:" + AntiSpamFilterControl.autoResults.getObjective(1) + 
								  " FN:" + AntiSpamFilterControl.autoResults.getObjective(0));*/
		}
	}
	
	private void setValueToRule() {
		if(!currRule.isEmpty()) {
			AntiSpamFilterControl.setWeigthByRule(currRule, Double.parseDouble(ruleValue.getText()));
		}
		currRule = CBRules.getSelectedItem().toString();
		ruleValue.setText(AntiSpamFilterControl.getWeigthByRule(currRule, true) + "");
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
		if(bool) {
			for(String rule : AntiSpamFilterControl.ruleList) {
				rulesCB.addElement(rule);
				autoRulesCB.addElement(rule);
			}
		}
		CBRules.setEnabled(bool);
		ruleValue.setEnabled(bool);
		manualTest.setEnabled(bool);
		manualSaveRuleValues.setEnabled(bool);
		autoCBRules.setEnabled(bool);
		autoRuleValue.setEnabled(bool);
		autoTest.setEnabled(bool);
		autoSaveRuleValues.setEnabled(bool);
	}
	
	public static void main(String[] args) {
		BoardControl board = new BoardControl();
		board.start();
	}
	

}
