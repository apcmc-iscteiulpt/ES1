package antiSpamFilterControl;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

class TestAntiSpamFilterControl {

	BoardControl b = new BoardControl();;
	
	@Test
	void testGetFiles() {
		b.start();
		 b.rulesFile_Input.setText("jUnitTests/rules.cf");
		 b.hamFile_Input.setText("jUnitTests/ham.log");
		 b.spamFile_Input.setText("jUnitTests/spam.log");
		 b.getFilesButton.doClick();
		 assertNotNull(AntiSpamFilterControl.rules);
		 assertNotNull(AntiSpamFilterControl.ham);
		 assertNotNull(AntiSpamFilterControl.spam);
	}
	
	
	@Test
	void testManualConfiguration() {
		b.manualTestButton.doClick();
		assertEquals(0, AntiSpamFilterControl.falsePositiveManual);
		assertEquals(239, AntiSpamFilterControl.falseNegativeManual);
	}
	
	@Test
	void testSetRulesWeight() {
		AntiSpamFilterControl.setWeigthByRuleManual("BAYES_00", 4.5);
		assertEquals(4.5, AntiSpamFilterControl.getWeigthByRule("BAYES_00", true));
		AntiSpamFilterControl.setWeigthByRuleManual("AAAAAAAAAAAAA", 4.5); //To test
	}
	
	@Test
	void saveManualFile() {
		b.manualSaveValuesButton.doClick();
		try {
			assertEquals(
				    FileUtils.readFileToString(new File("jUnitTests/rulesConfirm.cf"), "utf-8"), 
				    FileUtils.readFileToString(new File("rules.cf"), "utf-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	void testAutoConfig() {
		AntiSpamFilterControl.falsePositiveAuto = -1;
		AntiSpamFilterControl.falseNegativeAuto = -1;
		b.autoTestButton.doClick();
		assertNotEquals(-1, AntiSpamFilterControl.falsePositiveAuto);
		assertNotEquals(-1, AntiSpamFilterControl.falseNegativeAuto);
		
	}
	
	@Test
	void saveAutoFile() {
		b.autoSaveValuesButton.doClick();
		try {
			assertNotEquals(
				    FileUtils.readFileToString(new File("jUnitTests/rulesConfirm.cf"), "utf-8"), 
				    FileUtils.readFileToString(new File("AntiSpamConfigurationForProfessionalMailbox/rules.cf"), "utf-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
