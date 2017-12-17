package antiSpamFilterControl;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import org.apache.commons.io.FileUtils;

import antiSpamFilter.AntiSpamFilterAutomaticConfiguration;

public class BoardControl {
	int frameWidth = 500;
	int frameWeigth = 800;
	JFrame frame =  new JFrame("antiSpamFilter");
	private Dimension filesPanelDimension = new Dimension(frameWidth, frameWeigth/3);
	private Dimension PanelsDimension = new Dimension(frameWidth, frameWeigth/3);
	private Dimension inputsDimension = new Dimension(150, 15);
	
	JTextField SpamToolsFile_Input;
	JTextField ValidMailsFile_Input;
	JTextField SpamMailsFile_Input;
	JButton getFilesButton = new JButton("Get Files");
	
	JComboBox<String> manualCBRules = new JComboBox<String>();
	DefaultComboBoxModel<String> rulesCB;
	JTextField manualRuleValue;
	JButton manualTestButton;
	JButton manualSaveValuesButton;
	JTextArea manualResults;
	
	JComboBox<String> autoCBRules = new JComboBox<String>();
	DefaultComboBoxModel<String> autoRulesCB;
	JTextField autoRuleValue;
	JButton autoTest = new JButton("Test Values");
	JButton autoSaveRuleValues = new JButton("Save configurations");
	JTextArea autoResults =  new JTextArea();
	
	JList<String> SpamToolsListAuto;
	
	String currRule = "";
	
	/**
	 * Constructor of BoardControl
	 */
	public BoardControl() {
		
		// Frame definition
		frame.setSize(frameWidth, frameWeigth);
		frame.setLayout(new GridLayout(3, 1, 4, 2));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Files Configuration
		filesPanel();
		
		//Manual Configurations panel
		manualTestPanel();
		
		//Auto Configurations panel
		autoTestPanel();
	}
	
	/**
	 * Procedure that establishes the Files selection panel
	 */
	private void filesPanel() {
		JPanel FilesPanel = new JPanel();
		FilesPanel.setSize(filesPanelDimension);
		FilesPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		FilesPanel.setLayout(new GridLayout(5, 1, 6, 2));
		FilesPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		
		//Label title
		JLabel filesLabel = new JLabel("Files Import");
		filesLabel.setHorizontalAlignment(0);
		FilesPanel.add(filesLabel);
		
		//Spam Tools File - rules.cf
		JPanel rulesPanel = new JPanel();
		rulesPanel.setSize(frameWidth, 40);
		rulesPanel.setLayout(new GridLayout(2, 1, 1, 2));
		JLabel rulesFileTitle = new JLabel("Rules File");
		rulesPanel.add(rulesFileTitle);
		SpamToolsFile_Input = new JTextField("rules.cf"); // 		/Users/Tiago/Downloads/rules.cf
		SpamToolsFile_Input.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		SpamToolsFile_Input.setSize(inputsDimension);
		rulesPanel.add(SpamToolsFile_Input);
		FilesPanel.add(rulesPanel);
		
		//Valid Emails - ham.log
		JPanel hamPanel = new JPanel();
		hamPanel.setSize(frameWidth, 40);
		hamPanel.setLayout(new GridLayout(2, 1, 1, 2));
		JLabel hamFileTitle = new JLabel("Ham File");
		hamPanel.add(hamFileTitle);
		ValidMailsFile_Input = new JTextField("ham.log");
		ValidMailsFile_Input.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		ValidMailsFile_Input.setSize(inputsDimension);
		hamPanel.add(ValidMailsFile_Input);
		FilesPanel.add(hamPanel);
		
		//Spam Emails - spam.log
		JPanel spamPanel = new JPanel();
		spamPanel.setSize(frameWidth, 40);
		spamPanel.setLayout(new GridLayout(2, 1, 1, 2));
		JLabel spamFileTitle = new JLabel("SPAM File");
		spamPanel.add(spamFileTitle);
		SpamMailsFile_Input = new JTextField("spam.log");
		SpamMailsFile_Input.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		SpamMailsFile_Input.setSize(inputsDimension);
		spamPanel.add(SpamMailsFile_Input);
		FilesPanel.add(spamPanel);
		
		//Get Files Button
		JPanel buttonPanel = new JPanel();
		buttonPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		buttonPanel.setSize(frameWidth/2, 40);
		getFilesButton.setHorizontalAlignment(0);
		buttonPanel.add(getFilesButton);
		FilesPanel.add(buttonPanel);
		
		getFilesButton.addActionListener(
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
	}
	
	/**
	 * Procedure that establishes the manual configuration test panel
	 */
	private void manualTestPanel() {
		JPanel manualPanel = new JPanel();
		manualPanel.setSize(PanelsDimension);
		manualPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		manualPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		//Title
		c.weighty = 0.5;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2;
		JLabel manualConfigLabel = new JLabel("Manual Configuration");
		manualConfigLabel.setHorizontalAlignment(0);
		manualPanel.add(manualConfigLabel, c);
		
		//ComboBox
		c.weightx = 0.7;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		c.ipady = 20;
		c.gridwidth = 1;
		rulesCB = new DefaultComboBoxModel<String>();
		manualCBRules.setPrototypeDisplayValue("XXXXXXXXXXXXXXXXXXXXXXXXXX");
		manualCBRules.setModel(rulesCB);
		manualCBRules.addActionListener(
			new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					setValueToRule();
				}
			}
		);
		manualPanel.add(manualCBRules, c);
		
		//WeigthValue
		c.weightx = 0.3;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 1;
		c.ipady = 0;
		c.gridwidth = 1;
		manualRuleValue = new JTextField();
		manualPanel.add(manualRuleValue, c);
		
		//Save Configurations Button
		c.weightx = 0.5;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 2;
		c.ipady = 15;
		c.gridwidth = 2;
		manualSaveValuesButton = new JButton("Save Manual configurations");
		manualSaveValuesButton.addActionListener(
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
		manualSaveValuesButton.setSize(frameWidth/2, 80);
		manualPanel.add(manualSaveValuesButton, c);
		
		//Test Values Button
		c.weightx = 0.5;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 3;
		c.ipady = 15;
		c.gridwidth = 2;
		manualTestButton = new JButton("Test Values");
		manualTestButton.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent e){
					setValueToRule();
					AntiSpamFilterControl.Evaluater(true);
					setResultString("manual");
				}
			}
		);
		manualTestButton.setSize(frameWidth/2, 80);
		manualPanel.add(manualTestButton, c);
		
		//Show Results
		c.weightx = 0.5;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 4;
		c.ipady = 0;
		manualResults =  new JTextArea();
		manualResults.setEnabled(false);
		manualPanel.add(manualResults, c);
		
		frame.add(manualPanel);
	}

	/**
	 * Procedure that establishes the manual configuration test panel
	 */
	private void autoTestPanel() {
		JPanel autoPanel = new JPanel();
		autoPanel.setSize(PanelsDimension);
		autoPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		autoPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		//Title
		c.weighty = 0.5;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2;
		JLabel autoConfigLabel = new JLabel("Automatic Config");
		autoConfigLabel.setHorizontalAlignment(0);
		autoPanel.add(autoConfigLabel, c);
		
		//ComboBox Auto
		c.weightx = 0.7;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		c.ipady = 20;
		c.gridwidth = 1;		
		autoRulesCB = new DefaultComboBoxModel<String>();
		autoCBRules.setPrototypeDisplayValue("XXXXXXXXXXXXXXXXXXXXXXXXXX");
		autoCBRules.setModel(autoRulesCB);
		autoCBRules.addActionListener(
			new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					autoRuleValue.setText(AntiSpamFilterControl.getWeigthByRule(autoRulesCB.getSelectedItem().toString(), false) + "");
				}
			}
		);
		autoPanel.add(autoCBRules, c);
		
		//WeigthValue
		c.weightx = 0.3;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 1;
		c.ipady = 0;
		c.gridwidth = 1;
		autoRuleValue = new JTextField();
		autoPanel.add(autoRuleValue, c);
		
		
		//Save Configurations Button
		c.weightx = 0.5;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 2;
		c.ipady = 15;
		c.gridwidth = 2;
		autoSaveRuleValues = new JButton("Save Auto configurations");
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
		autoSaveRuleValues.setSize(frameWidth/2, 80);
		autoPanel.add(autoSaveRuleValues, c);
		
		//Test Values Button
		c.weightx = 0.5;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 3;
		c.ipady = 15;
		c.gridwidth = 2;
		autoTest = new JButton("Run Auto Algorithm");
		autoTest.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent e){
					//Call evaluate Here!!!!
					AntiSpamFilterAutomaticConfiguration w  = new AntiSpamFilterAutomaticConfiguration();
					try {
						w.run(AntiSpamFilterControl.ruleList.size());
						AntiSpamFilterControl.selectAutoConfiguration();
						setResultString("auto");
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					System.out.println("Configuração automática terminou!");
				}
			}
		);
		autoTest.setSize(frameWidth/2, 80);
		autoPanel.add(autoTest, c);
		
		//Show Results
		c.weightx = 0.5;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 4;
		c.ipady = 0;
		autoResults =  new JTextArea();
		autoResults.setEnabled(false);
		autoPanel.add(autoResults, c);
		
		frame.add(autoPanel);
	}
	
	/**
	 * Action to get file by path
	 * @param pathFile Path of file to get
	 * @return File, Null
	 */
	private static File getFile(String pathFile) {
		if(!pathFile.isEmpty()){
			return new File(pathFile);
		}
		return null;
	}
	
	
	/**
	 * Procedure to copy files from a source to a destiny
	 * @param source Path of source file
	 * @param dest Path of destination file
	 * @throws IOException
	 */
	private static void copyFiles(File source, File dest) throws IOException {
		if(!source.equals(dest))
			FileUtils.copyFile(source, dest);
	}
	
	/**
	 * Procedure that sets the metrics of configuration selected by testType parameter to corresponding text area
	 * @param testType type of configuration results to be shown
	 */
	private void setResultString(String testType) {
		switch(testType) {
			case "manual": 
				manualResults.setText("FP :" + AntiSpamFilterControl.falsePositiveManual + 
									" FN:" +  AntiSpamFilterControl.falseNegativeManual);
				break;
			case "auto": 
				autoResults.setText("FP :" + AntiSpamFilterControl.falsePositiveAuto + 
								   " FN:" +  AntiSpamFilterControl.falseNegativeAuto);
				break;
		}
	}
	
	
	/**
	 * Procedure that sets the weight (defined on TextArea) to corresponding rule
	 */
	private void setValueToRule() {
		if(!currRule.isEmpty()) {
			AntiSpamFilterControl.setWeigthByRule(currRule, Double.parseDouble(manualRuleValue.getText()));
		}
		currRule = manualCBRules.getSelectedItem().toString();
		manualRuleValue.setText(AntiSpamFilterControl.getWeigthByRule(currRule, true) + "");
	}
	
	/**
	 * procedure that sets the window visible, and launchs the program
	 */
	private void start() {
		frame.setVisible(true);
		startFilesConfig(true);
		startSpamFilterTest(false);
	}
	
	/**
	 * Procedure that sets visible/launch the window where the files are get
	 */
	private void startFilesConfig(Boolean bool) {
		getFilesButton.setEnabled(bool);
		SpamToolsFile_Input.setEnabled(bool);
		ValidMailsFile_Input.setEnabled(bool);
		SpamMailsFile_Input.setEnabled(bool);
	}
	
	
	/**
	 * Procedure that sets visible/launch the window where the configurations (manual and auto) are operated
	 */
	private void startSpamFilterTest(Boolean bool) {
		if(bool) {
			for(String rule : AntiSpamFilterControl.ruleList) {
				rulesCB.addElement(rule);
				autoRulesCB.addElement(rule);
			}
		}
		manualCBRules.setEnabled(bool);
		manualRuleValue.setEnabled(bool);
		manualTestButton.setEnabled(bool);
		manualSaveValuesButton.setEnabled(bool);
		manualResults.setEnabled(false);
		
		autoCBRules.setEnabled(bool);
		autoRuleValue.setEnabled(bool);
		autoTest.setEnabled(bool);
		autoSaveRuleValues.setEnabled(bool);
		autoResults.setEnabled(false);
	}
	
	public static void main(String[] args) {
		BoardControl board = new BoardControl();
		board.start();
	}
	

}
