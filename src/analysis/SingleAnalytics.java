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
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

public class SingleAnalytics {

	final static int SCREEN_WIDTH = 1920;
	final static int SCREEN_HEIGHT = 1080;
	private static JPanel panel=new JPanel(new GridBagLayout());
	private  static String gazepointGZDPath = "";
	private  static String gazepointFXDPath = "";
	private  static String outputFolderPath = "";
	private static String pName = "";
	private static BufferedImage myPicture;
	private static JLabel image;
	
	/**
	 * sets up the picture
	 */
	public static void setUp() throws IOException
	{
		 myPicture = ImageIO.read(new File("data/d2logo.jpg")); 
		 image = new JLabel(new ImageIcon(myPicture));
	}
	
	/**
	 * Acquires all the path required to run an analysis
	 * 
	 * @return	JPanel	returns the UI for this page
	 */
	public static JPanel acquirePathsPage() throws IOException
	{	

		JPanel pPanel = new JPanel(new FlowLayout());
		GridBagConstraints c = new GridBagConstraints();


		JLabel title = new JLabel("D\u00B2 Lab Eye Tracking Data Analysis Tool");
		JTextField gazeTextF = new JTextField("Location of gaze file: ", 50);
		JButton gazeBrowseBtn = new JButton("Browse");
		JTextField fixationTextF = new JTextField("Location of fixation file: " , 50);
		JButton fixationBrowseBtn = new JButton("Browse");
		JTextField outputTextF = new JTextField("Location of output file: ", 50);
		JButton outputBrowseBtn = new JButton("Browse");
		JLabel partLabel = new JLabel("Participant: ");
		JTextField partTextF = new JTextField(15);
		JButton submitBtn = new JButton("Submit");

		title.setFont(new Font("Verdana", Font.PLAIN, 30));
		gazeTextF.setBackground(Color.WHITE);
		gazeTextF.setEditable(false);
		gazeTextF.setPreferredSize(new Dimension(50, 30));
		fixationTextF.setBackground(Color.WHITE);
		fixationTextF.setEditable(false);
		fixationTextF.setPreferredSize(new Dimension(50, 30));
		outputTextF.setBackground(Color.WHITE);
		outputTextF.setEditable(false);
		outputTextF.setPreferredSize(new Dimension(50, 30));
		partTextF.setPreferredSize(new Dimension(50, 30));
		partLabel.setFont(new Font("Verdana", Font.PLAIN, 20));

		pPanel.add(partLabel);
		pPanel.add(partTextF);



		c.gridx = 0;//set the x location of the grid for the component
		c.gridy = 0;//set the y location of the grid for the component
		panel.add(image,c);
		c.gridy = 1;
		c.insets = new  Insets(10, 15, 15, 0);
		panel.add(title,c);
		c.gridy = 2;
		panel.add(gazeTextF,c);
		c.gridx = 1;
		panel.add(gazeBrowseBtn,c);
		c.gridy = 3;
		c.gridx = 0;
		panel.add(fixationTextF,c);
		c.gridx = 1;
		panel.add(fixationBrowseBtn,c);
		c.gridy = 4;
		c.gridx = 0;
		panel.add(outputTextF,c);
		c.gridx = 1;
		panel.add(outputBrowseBtn,c);
		c.gridy = 5;
		c.gridx = 0;
		panel.add(pPanel,c);
		c.gridy=6;
		panel.add(submitBtn, c);

		gazeBrowseBtn.addActionListener(e -> {
			String temp = modifier.fileChooser("Select the gaze .csv file you would like to use", "/data/");
			if(!temp.equals(""))
			{
				gazeTextF.setText(temp);
			}

		});
		fixationBrowseBtn.addActionListener(e -> { 
			String temp = modifier.fileChooser("Select the fixation .csv file you would like to use", "/data/");
			if(!temp.equals(""))
			{	
				fixationTextF.setText(temp);
			}

		});
		outputBrowseBtn.addActionListener(e -> {
			String temp = modifier.folderChooser("Choose a directory to save your file");
			if(!temp.equals(""))
			{	
				outputTextF.setText(temp);
			}

		});

		submitBtn.addActionListener(e-> {
			//ensure that participant is not empty
			if(partTextF.getText().equals("") || partTextF.getText()==null)
			{
				JOptionPane.showMessageDialog(null, "Must input the participants name", "Error Message", JOptionPane.ERROR_MESSAGE);
			}
			else if(gazeTextF.getText().equals("") || gazeTextF.getText()==null || gazeTextF.getText().equals("Location of gaze file: "))
			{
				JOptionPane.showMessageDialog(null, "Must select a gaze file", "Error Message", JOptionPane.ERROR_MESSAGE);
			} 
			else if(fixationTextF.getText().equals("") || fixationTextF.getText()==null || fixationTextF.getText().equals("Location of fixation file: "))
			{
				JOptionPane.showMessageDialog(null, "Must select a fixaiton file", "Error Message", JOptionPane.ERROR_MESSAGE);
			} 
			else if(outputTextF.getText().equals("") || outputTextF.getText() == null || outputTextF.getText().equals("Location of output file: "))
			{
				JOptionPane.showMessageDialog(null, "Must select an outputFolder", "Error Message", JOptionPane.ERROR_MESSAGE);
			}
			else
			{
				gazepointGZDPath = gazeTextF.getText();
				gazepointFXDPath = fixationTextF.getText();
				outputFolderPath = outputTextF.getText() + "/" + partTextF.getText();
				pName = partTextF.getText();
				
				File participantFolder = new File(outputTextF.getText() + "/" + partTextF.getText());

				//creates the folder only if it doesn't exists already
				if(!participantFolder.exists())
				{
					boolean folderCreated = participantFolder.mkdir();
					if(!folderCreated)
					{
						JOptionPane.showMessageDialog(null, "Unable to create participant's folder", "Error Message", JOptionPane.ERROR_MESSAGE);
						systemLogger.writeToSystemLog(Level.SEVERE, SingleAnalytics.class.getName(), "Unable to create participant's folder");
						System.exit(0);
					}
					else
					{
						analyzeData(gazepointGZDPath, gazepointFXDPath, outputFolderPath);
						try
						{	
							gazeAnalyticsOptions();
						} 
						catch (IOException e1) 
						{
							systemLogger.writeToSystemLog(Level.WARNING, SingleAnalytics.class.getName(), "Error in Windowing Operation \n" + e1);
						}
						panel.repaint();	
					}

				}
			}
			
		});
		

		return panel;

	}
	
	
	/**
	 * Runs an analysis on the two files and all generated files will be in the ouputPath folder
	 * 
	 * @param	gzdPath		file path of the gaze file
	 * @param	fxdPath		file path of the fixation file
	 * @param	outputPath	folder path of the output location
	 */
	public static void analyzeData(String gzdPath, String fxdPath, String outputPath)
	{
		String[] paths = {gzdPath, fxdPath, outputPath};

		String[] modifiedData = modifier.processData(new String[] {paths[0], paths[1]}, paths[2], 1920, 1080);
		gazepointGZDPath = modifiedData[0];
		gazepointFXDPath = modifiedData[1];
		outputFolderPath = paths[2];

		systemLogger.createSystemLog(outputFolderPath);
		//create the system log    
		systemLogger.createSystemLog(outputFolderPath);

		//output file paths
		String graphFixationResults = "/" + pName + "_graphFXDResults.csv";
		String graphFixationOutput = outputFolderPath + graphFixationResults;

		String graphEventResults = "/" + pName + "_graphEVDResults.csv";
		String graphEventOutput = outputFolderPath + graphEventResults;

		String graphGazeResults = "/" + pName + "_graphGZDResults.csv";
		String graphGazeOutput = outputFolderPath + graphGazeResults;


		String aoiResults = "/" + pName + "_aoiResults.csv";
		String aoiOutput = outputFolderPath + aoiResults;
		ScanPath scanPath = new ScanPath(gazepointFXDPath, outputFolderPath);
		try {
			scanPath.runAllClimbScan();
			// Analyze graph related data
			fixation.processFixation(gazepointFXDPath, graphFixationOutput, SCREEN_WIDTH, SCREEN_HEIGHT);
			event.processEvent(gazepointGZDPath, graphEventOutput);
			gaze.processGaze(gazepointGZDPath, graphGazeOutput);
			modifier.createBaselineFile(gazepointGZDPath, outputFolderPath);
	
	
			// Gaze Analytics 
			modifier.csvToARFF(graphFixationOutput);
			modifier.csvToARFF(graphEventOutput);
			modifier.csvToARFF(graphGazeOutput);
	
			//combining all result files
			modifier.mergingResultFiles(graphFixationOutput, graphEventOutput, graphGazeOutput, outputFolderPath + "/combineResults.csv");
			modifier.csvToARFF(outputFolderPath + "/combineResults.csv");
	
			// Analyze AOI data
			AOI.processAOIs(gazepointGZDPath, aoiOutput, SCREEN_WIDTH, SCREEN_HEIGHT);
		}
		catch(Exception ex)
		{
			systemLogger.writeToSystemLog(Level.WARNING, SingleAnalytics.class.getName(), "Error in analyzing data \n" + ex);
		}
	}
	

	/**
	 * UI where the user will select which the type of gaze analytics that they will want to output
	 * UI where the user will input the information needed for the program to output the desired files
	 * 
	 * @param	p				the panel the UI will be placed on
	 * @param	outputfolder	the folder path where all the files will be placed in
	 * @return 	JPanel			return panel where the UI elements are contained 
	 */
	public static JPanel gazeAnalyticsOptions() throws IOException
	{
		//All the gaze analytics options
		panel.removeAll();
		panel.revalidate();
		panel.repaint();
		
		//create folder to put the analysis in
		String dir = "/results/" + outputFolderPath.substring(outputFolderPath.lastIndexOf("/") + 1) + "/inputFiles/";
		File snapshotFolder = new File(outputFolderPath + "/" + pName + "_SnapshotFolder");
		snapshotFolder.mkdir();
		String outputFolder = snapshotFolder.getPath();
		
		GridBagConstraints c = new GridBagConstraints();
		ButtonGroup bg = new ButtonGroup();
		
		JLabel qLabel = new JLabel("Please pick an option");		
		JPanel optionsPanel = new JPanel(new FlowLayout());
		JRadioButton continuousSnapshotButton = new JRadioButton("Continuous Snapshot");
		JRadioButton cumulativeSnapshotButton = new JRadioButton("Cumulative Snapshot");
		JRadioButton overlappingSnapshotButton = new JRadioButton("Overlapping Snapshot");
		JRadioButton eventAnalyticsButton = new JRadioButton("Event Analytics");
		JRadioButton exitBtn = new JRadioButton("Exit");
		JButton btn = new JButton("OK");

		qLabel.setFont(new Font("Verdana", Font.PLAIN, 20));
		continuousSnapshotButton.setToolTipText("This option generates gaze data in a series of fixed, non-overlapping windows");
		cumulativeSnapshotButton.setToolTipText("This option generates gaze data in a series of expanding windows that increases with every interval");
		overlappingSnapshotButton.setToolTipText("This option generates gaze data in a series of fixed and overlapping windows");
		eventAnalyticsButton.setToolTipText("This option generates a baseline file based on the first two minutes of the gaze data, and then compares it to the rest of the file");
		
		//Adds all the JRadioButton to a layout
		bg.add(continuousSnapshotButton);
		bg.add(cumulativeSnapshotButton);
		bg.add(overlappingSnapshotButton);
		bg.add(eventAnalyticsButton);
		bg.add(exitBtn);
		//Adds all the JRadioButton to a a flow layout
		optionsPanel.add(continuousSnapshotButton);
		optionsPanel.add(cumulativeSnapshotButton);
		optionsPanel.add(overlappingSnapshotButton);
		optionsPanel.add(eventAnalyticsButton);
		optionsPanel.add(exitBtn);
		
		c.gridx = 0;//set the x location of the grid for the next component
		c.gridy = 0;//set the y location of the grid for the next component
		c.insets = new  Insets(40, 15, 15, 0);
		panel.add(image,c);
		c.gridy = 1;
		panel.add(qLabel, c);
		c.gridy = 2;
		panel.add(optionsPanel, c);
		c.gridy = 3;
		c.gridx = 0;
		panel.add(btn, c);
		panel.revalidate();

		//checks what button has been selected and generates the required files 
		btn.addActionListener(e -> {
			if(exitBtn.isSelected())
			{
				JOptionPane.showMessageDialog(null, "Exiting out of program", "Exit", JOptionPane.INFORMATION_MESSAGE);
				System.exit(0);
			}
			
			panel.removeAll();
			panel.repaint();
			//resets the grid
			c.gridx = 0;
			c.gridy = 0;
			panel.add(image,c);
			
			if(continuousSnapshotButton.isSelected()||cumulativeSnapshotButton.isSelected())
			{
				contCumulWindowAction(c, outputFolder,dir, continuousSnapshotButton.isSelected());
			}
			else if(overlappingSnapshotButton.isSelected())
			{
				overlappingWindowAction(c, outputFolder,dir);
			}
			else if(eventAnalyticsButton.isSelected())
			{
				eventWindowAction(c, outputFolder,dir);	
			}
		});
		
		return panel;
	}

	/**
	 * UI for both continuous and cumulative window
	 * 
	 * @param	c					layout type
	 * @param	outputFolder		the path where the generated files will reside
	 * @param	dir					sets the directory 
	 * @param	contiWindowSelect	returns true if the user selected continuous window
	 */
	private static void contCumulWindowAction(GridBagConstraints c, String outputFolder, String dir, boolean contiWindowSelected)
	{
		JPanel userInputPanel = new JPanel(new FlowLayout());
		String gazepointFile = modifier.fileChooser("Please select which file you would like to parse out", dir);
		JTextField windowSizeInput = new JTextField("", 5);
		JLabel windowSizeLabel = new JLabel("Window Size(seconds): ");
		JButton contBtn = new JButton("OK");

		userInputPanel.add(windowSizeLabel);
		userInputPanel.add(windowSizeInput);
		
		c.gridy = 1;
		panel.add(userInputPanel, c);	
		c.gridx = 0;
		c.gridy = 2;
		panel.add(contBtn, c);
		panel.revalidate();

		contBtn.addActionListener(ev -> {
			if(contiWindowSelected)
			{
				try 
				{
					WindowOperations.continuousWindow(gazepointFile, outputFolder,Integer.parseInt(windowSizeInput.getText()) );
				} 
				catch (NumberFormatException | CsvValidationException e1) 
				{
					JOptionPane.showMessageDialog(null, "User input was not a valid number. Unable to create gaze analytics files. Please check system log", "Exit", JOptionPane.ERROR_MESSAGE);
					systemLogger.writeToSystemLog(Level.SEVERE, main.class.getName(), "User input was not a valid number. Unable to create gaze analytics files");
				}
				JOptionPane.showMessageDialog(null, "Exiting out of program", "Exit", JOptionPane.INFORMATION_MESSAGE);
				System.exit(0);
			}
			else
			{
				try 
				{
					WindowOperations.cumulativeWindow(gazepointFile, outputFolder, Integer.parseInt(windowSizeInput.getText()));
				} 
				catch (NumberFormatException | CsvValidationException e1) 
				{
					systemLogger.writeToSystemLog(Level.SEVERE, main.class.getName(), "User input was not a valid number. Unable to create gaze analytics files");
					JOptionPane.showMessageDialog(null, "User input was not a valid number. Unable to create gaze analytics files. Please check system log", "Exit", JOptionPane.ERROR_MESSAGE);
				}
				JOptionPane.showMessageDialog(null, "Exiting out of program", "Exit", JOptionPane.INFORMATION_MESSAGE);
				System.exit(0);
			}
		});
	}
	
	/**
	 * UI for overlapping window
	 * 
	 * @param	c					layout type
	 * @param	outputFolder		the path where the generated files will reside
	 * @param	dir					sets the directory 
	 */
	private static void overlappingWindowAction(GridBagConstraints c, String outputFolder, String dir)
	{
		JPanel userInputPanel0 = new JPanel(new FlowLayout());
		JPanel userInputPanel1 = new JPanel(new FlowLayout());
		
		String gazepointFile = modifier.fileChooser("Please select which file you would like to parse out", dir);
		JTextField windowSizeInput = new JTextField("", 5);
		JTextField overlappingInput = new JTextField("", 5);
		JLabel windowSizeLabel = new JLabel("Window Size(seconds): ");
		JLabel overlappingLabel = new JLabel("Overlapping Amount(seconds): ");
		JButton overlappingBtn = new JButton("OK");
		
		userInputPanel0.add(windowSizeLabel);
		userInputPanel0.add(windowSizeInput);
		userInputPanel1.add(overlappingLabel);
		userInputPanel1.add(overlappingInput);

		c.gridy = 1;
		panel.add(userInputPanel0,c);
		c.gridy = 2;
		panel.add(userInputPanel1,c);
		c.gridy = 3;
		panel.add(overlappingBtn, c);
		panel.revalidate();
		
		overlappingBtn.addActionListener(ev -> {
			try 
			{
				WindowOperations.overlappingWindow(gazepointFile, outputFolder,Integer.parseInt(windowSizeInput.getText()), Integer.parseInt(overlappingInput.getText()) );
			} 
			catch (NumberFormatException | CsvValidationException e1) 
			{
				systemLogger.writeToSystemLog(Level.SEVERE, main.class.getName(), "User input was not a valid number. Unable to create gaze analytics files");
				JOptionPane.showMessageDialog(null, "User input was not a valid number. Unable to create gaze analytics files. Please check system log", "Exit", JOptionPane.ERROR_MESSAGE);

			}
			System.exit(0);
		});
	}
	
	/**
	 * UI for event window
	 * 
	 * @param	c					layout type
	 * @param	outputFolder		the path where the generated files will reside
	 * @param	dir					sets the directory 
	 */
	private static void eventWindowAction(GridBagConstraints c, String outputFolder, String dir)
	{
		String gazepointFilePath = modifier.fileChooser("Please select your gaze file", dir);
		String baselineFilePath = outputFolderPath + "/baseline.csv";

		try 
		{
			FileReader baselineFR = new FileReader(baselineFilePath);
			CSVReader baselineCR = new CSVReader(baselineFR);	
			FileReader gazepointFR = new FileReader(gazepointFilePath);
			CSVReader gazepointCR = new CSVReader(gazepointFR);	
			String[] baselineHeader = baselineCR.readNext();
			String[]header = gazepointCR.readNext();
			JPanel userInputPanel0 = new JPanel(new FlowLayout());
			JPanel userInputPanel1 = new JPanel(new FlowLayout());
			JPanel userInputPanel2 = new JPanel(new FlowLayout());
			JLabel bLabel = new JLabel("Please pick the baseline value you would want to compare");
			JLabel gzptLabel = new JLabel("Please pick the gaze/fixation value you would want to compare");
			JLabel durLabel = new JLabel("Maxium Duration of an event (seconds): ");
			JTextField maxDurInput = new JTextField("", 5);
			JComboBox<String> baselineCB = new JComboBox<String>(baselineHeader);
			JComboBox<String> gazepointCB = new JComboBox<String>(header);
			JButton eventBtn = new JButton("OK");

			
			baselineCB.setMaximumSize(baselineCB.getPreferredSize());
			gazepointCB.setMaximumSize(gazepointCB.getPreferredSize());
			
			userInputPanel0.add(bLabel);
			userInputPanel0.add(baselineCB);
			userInputPanel1.add(gzptLabel);
			userInputPanel1.add(gazepointCB);
			userInputPanel2.add(durLabel);
			userInputPanel2.add(maxDurInput);
			
			c.gridy = 1;
			panel.add(userInputPanel0, c);
			c.gridy = 2;
			panel.add(userInputPanel1, c);
			c.gridy = 3;
			panel.add(userInputPanel2, c);
			
			c.gridy = 4;
			panel.add(eventBtn, c);
			panel.revalidate();

			eventBtn.addActionListener(et -> {
				try 
				{
					WindowOperations.eventWindow(gazepointFilePath, outputFolder, baselineFilePath, Arrays.asList(baselineHeader).indexOf(baselineCB.getSelectedItem()), Arrays.asList(header).indexOf(gazepointCB.getSelectedItem()), Integer.valueOf(maxDurInput.getText()));
				} 
				catch (NumberFormatException | IOException e1) 
				{
					systemLogger.writeToSystemLog(Level.SEVERE, main.class.getName(), "User input was not a valid number. Unable to create gaze analytics files");
				}
				JOptionPane.showMessageDialog(null, "Exiting out of program", "Exit", JOptionPane.INFORMATION_MESSAGE);
				System.exit(0);

			});

		} 
		catch (IOException | CsvValidationException e1) 
		{
			systemLogger.writeToSystemLog(Level.SEVERE, main.class.getName(), "Unable to find selected baseline or input files" + e1);
			JOptionPane.showMessageDialog(null, "Unable to find selected baseline or input files. Please check system log", "Exit", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}

	}
	
	/**
	 * get the participant's name
	 * 
	 * @return	String	name
	 */
	public static String getpName() {
		return pName;
	}

	/**
	 * sets the participant's name
	 * 
	 * @param	pName	participant's name
	 */
	public static void setpName(String pName) {
		SingleAnalytics.pName = pName;
	}


}
