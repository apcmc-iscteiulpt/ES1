package antiSpamFilterControl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;


public class BoardControl {

	JFrame frame =  new JFrame("antiSpamFilter");
	JTextArea SpamToolsFile_Input;
	JTextArea ValidMailsFile_Input;
	JTextArea SpamMailsFile_Input;
	JButton GetFiles = new JButton("Get Files");
	Dimension PanelsDimension = new Dimension(300, 400);
	
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
		
		
		//Files Panel
		Dimension inputsDimension = new Dimension(150, 15);
		JPanel FilesPanel = new JPanel();
		FilesPanel.setSize(PanelsDimension);
		FilesPanel.setBackground(Color.CYAN);
		FilesPanel.setLayout(new GridLayout(4, 2, 5, 3));
			//Spam Tools File - rules.cf
		SpamToolsFile_Input = new JTextArea("rules.cf");
		SpamToolsFile_Input.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		SpamToolsFile_Input.setSize(inputsDimension);
		FilesPanel.add(SpamToolsFile_Input);
		
		
		frame.add(FilesPanel);
		
			//Valid Emails - ham.log
		ValidMailsFile_Input = new JTextArea("ham.log");
		ValidMailsFile_Input.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		ValidMailsFile_Input.setSize(inputsDimension);
		FilesPanel.add(ValidMailsFile_Input);
		
			//Spam Emails - spam.log
		SpamMailsFile_Input = new JTextArea("spam.log");
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
	
	
	
	
	
	
	
	public static void main(String[] args) {
		BoardControl board = new BoardControl();
		board.frame.setVisible(true);
		
	}
}
