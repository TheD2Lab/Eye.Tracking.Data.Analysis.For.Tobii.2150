package analysis;

import java.awt.Color;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.logging.Level;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

import wekaext.WekaExperiment;

public class Panels {

	private final BufferedImage myPicture;
	private final JLabel image; 
	private JPanel panel=new JPanel(new GridBagLayout());
	
	public Panels() throws IOException
	{
		myPicture = ImageIO.read(new File("data/d2logo.jpg"));
		image = new JLabel(new ImageIcon(myPicture));
	}
	
	public void setUp()
	{
		GridBagConstraints c = new GridBagConstraints();

		c.gridx = 0;//set the x location of the grid for the next component
		c.gridy = 0;//set the y location of the grid for the next component
		panel.add(image,c);
	}
	
	public JPanel getPanel()
	{
		return panel;
	}
}
