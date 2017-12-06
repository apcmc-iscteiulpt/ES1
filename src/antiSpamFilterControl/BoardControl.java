package antiSpamFilterControl;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.apache.commons.io.FileUtils;


public class BoardControl {

	JFrame frame =  new JFrame("antiSpamFilter");
	JTextArea SpamToolsFile_Input;
	JTextArea ValidMailsFile_Input;
	JTextArea SpamMailsFile_Input;
	JButton GetFiles = new JButton("Get Files");
	Dimension PanelsDimension = new Dimension(300, 400);
	File rules = new File("rules.cf");
	File ham = new File("ham.log");
	File spam = new File("spam.log");
	HashMap<String, Double> hmRules = new HashMap<String, Double>();
	
	
	JTextArea ManualResults;
	JTextArea AutomaticResults;
	JTextArea screen;
	JButton addContactButton = new JButton("add Contact");
	JList SpamToolsListManual;
	JList SpamToolsListAuto;
	
	public BoardControl() {
		
		// Frame definition
		frame.setSize(300, 2000);
		frame.setLayout(new GridLayout(3, 1, 4, 2));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Files Panel
		Dimension inputsDimension = new Dimension(150, 15);
		JPanel FilesPanel = new JPanel();
		FilesPanel.setSize(PanelsDimension);
		FilesPanel.setBackground(Color.CYAN);
		FilesPanel.setLayout(new GridLayout(4, 2, 5, 3));
		//Spam Tools File - rules.cf
		SpamToolsFile_Input = new JTextArea("/Users/Tiago/Downloads/rules.cf");
		SpamToolsFile_Input.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		SpamToolsFile_Input.setSize(inputsDimension);
		FilesPanel.add(SpamToolsFile_Input);

		GetFiles.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent e){
					try {
						copyFiles(BoardControl.getFile(SpamToolsFile_Input.getText()), rules);
						hmRules = rulesToHM(rules);
						copyFiles(BoardControl.getFile(ValidMailsFile_Input.getText()), ham);
						copyFiles(BoardControl.getFile(SpamMailsFile_Input.getText()), spam);
						GetFiles.setEnabled(false);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		);
		
		frame.add(FilesPanel);
		
		//Valid Emails - ham.log
		ValidMailsFile_Input = new JTextArea("/Users/Tiago/Downloads/ham.log");
		ValidMailsFile_Input.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		ValidMailsFile_Input.setSize(inputsDimension);
		FilesPanel.add(ValidMailsFile_Input);
		
		//Spam Emails - spam.log
		SpamMailsFile_Input = new JTextArea("/Users/Tiago/Downloads/spam.log");
		SpamMailsFile_Input.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		SpamMailsFile_Input.setSize(inputsDimension);
		FilesPanel.add(SpamMailsFile_Input);
		
		//Get Files Button
		FilesPanel.add(GetFiles);
		
		//Manual Configurations panel
		JPanel ManualPanel = new JPanel();
		ManualPanel.setSize(PanelsDimension);
		ManualPanel.setBackground(Color.darkGray);
		frame.add(ManualPanel);
		
		//A Configurations panel
		JPanel AutoPanel = new JPanel();
		AutoPanel.setSize(PanelsDimension);
		AutoPanel.setBackground(Color.green);
		frame.add(AutoPanel);
	}
	
	private static File getFile(String pathFile) {
		if(!pathFile.isEmpty()){
			return new File(pathFile);
		}
		return null;
	}
	
	private static void copyFiles(File source, File dest) throws IOException {
		FileUtils.copyFile(source, dest);
	}
	
	private static HashMap<String, Double> rulesToHM(File file) throws FileNotFoundException{
		HashMap<String, Double> hm = new HashMap<String, Double>();
		if(file.exists()){
			Scanner scanner = new Scanner(file);
			String line;
			while(scanner.hasNext()){
				line = scanner.nextLine();
				String[] parts = line.split(" ");
				hm.put(parts[0], Double.parseDouble(parts[1]));
			}
			scanner.close();
		}
		return hm;
	}
	
	public static void main(String[] args) {
		BoardControl board = new BoardControl();
		board.frame.setVisible(true);
		
	}
}
