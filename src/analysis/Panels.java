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

public class Panels {

	final int SCREEN_WIDTH = 1920;
	final int SCREEN_HEIGHT = 1080;
	private  String gazepointGZDPath = "";
	private  String gazepointFXDPath = "";
	private  String outputFolderPath = "";
	private JPanel panel=new JPanel(new GridBagLayout());
	private final BufferedImage myPicture;
	private final JLabel image; 
	
	public Panels() throws IOException
	{
		myPicture = ImageIO.read(new File("data/d2logo.jpg"));
		image = new JLabel(new ImageIcon(myPicture));
	}
	public  JPanel acquirePathsPage() throws IOException
	{	
		

		JLabel title = new JLabel("D\u00B2 Lab Eye Tracking Data Analysis Tool");
		title.setFont(new Font("Verdana", Font.PLAIN, 30));

		JLabel gazeLabel = new JLabel("Location of gaze file: ");
		JTextField gazeTextF = new JTextField("Location of gaze file: ", 50);
		JButton gazeBrowseBtn = new JButton("Browse");
		gazeTextF.setBackground(Color.WHITE);
		gazeTextF.setEditable(false);
		gazeTextF.setPreferredSize(new Dimension(50, 30));

		JLabel fixationLabel = new JLabel("Location of fixation file: ");
		JTextField fixationTextF = new JTextField("Location of fixation file: " , 50);
		fixationTextF.setBackground(Color.WHITE);
		JButton fixationBrowseBtn = new JButton("Browse");
		fixationTextF.setEditable(false);
		fixationTextF.setPreferredSize(new Dimension(50, 30));

		JLabel outputLabel = new JLabel("Location of output file: ");  
		JTextField outputTextF = new JTextField("Location of output file: ", 50);
		outputTextF.setBackground(Color.WHITE);
		JButton outputBrowseBtn = new JButton("Browse");
		outputTextF.setEditable(false);
		outputTextF.setPreferredSize(new Dimension(50, 30));

		JPanel pPanel = new JPanel(new FlowLayout());
		JLabel partLabel = new JLabel("Participant: ");
		JTextField partTextF = new JTextField(15);
		partTextF.setPreferredSize(new Dimension(50, 30));
		partLabel.setFont(new Font("Verdana", Font.PLAIN, 20));
		pPanel.add(partLabel);
		pPanel.add(partTextF);


		GridBagConstraints c = new GridBagConstraints();

		c.gridx = 0;//set the x location of the grid for the next component
		c.gridy = 0;//set the y location of the grid for the next component
		panel.add(image,c);
		
		c.gridy = 1;
		c.insets = new  Insets(10, 15, 15, 0);
		panel.add(title,c);

		c.gridy = 2;//change the y lo cation
		panel.add(gazeTextF,c);
		c.gridx = 1;
		panel.add(gazeBrowseBtn,c);

		c.gridy = 3;//change the y location
		c.gridx = 0;
		panel.add(fixationTextF,c);
		c.gridx = 1;
		panel.add(fixationBrowseBtn,c);

		c.gridy = 4;//change the y location
		c.gridx = 0;
		panel.add(outputTextF,c);
		c.gridx = 1;
		panel.add(outputBrowseBtn,c);

		c.gridy = 5;//change the y location
		c.gridx = 0;
		panel.add(pPanel,c);

		c.gridy=6;
		JButton submitBtn = new JButton("Submit");
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
				
				File participantFolder = new File(outputTextF.getText() + "/" + partTextF.getText());

				//creates the folder only if it doesn't exists already
				if(!participantFolder.exists())
				{
					boolean folderCreated = participantFolder.mkdir();
					if(!folderCreated)
					{
						JOptionPane.showMessageDialog(null, "Unable to create participant's folder", "Error Message", JOptionPane.ERROR_MESSAGE);
						System.exit(0);
					}
					else
					{
						analyzeData();
						try {
							
							gazeAnalyticsOptions();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						panel.repaint();	
					}

				}
			}
			
		});
		

		return panel;

	}
	
	private void analyzeData()
	{
		String[] paths = {getGZDPath(), getFXDPath(), getOutputPath()};

		String[] modifiedData = modifier.processData(new String[] {paths[0], paths[1]}, paths[2], 1920, 1080);
		gazepointGZDPath = modifiedData[0];
		gazepointFXDPath = modifiedData[1];
		outputFolderPath = paths[2];

		systemLogger.createSystemLog(outputFolderPath);
		//create the system log    
		systemLogger.createSystemLog(outputFolderPath);

		//output file paths
		String graphFixationResults = "/graphFXDResults.csv";
		String graphFixationOutput = outputFolderPath + graphFixationResults;

		String graphEventResults = "/graphEVDResults.csv";
		String graphEventOutput = outputFolderPath + graphEventResults;

		String graphGazeResults = "/graphGZDResults.csv";
		String graphGazeOutput = outputFolderPath + graphGazeResults;


		String aoiResults = "/aoiResults.csv";
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
			System.out.println(ex);
		}
	}
	

	/*
	 * UI where the user will select which the type of gaze analytics that they will want to output
	 * UI where the user will input the information needed for the program to output the desired files
	 * 
	 * @param	p				the panel the UI will be placed on
	 * @param	outputfolder	the folder path where all the files will be placed in
	 */
	public JPanel gazeAnalyticsOptions() throws IOException
	{
		String dir = "/results/" + outputFolderPath.substring(outputFolderPath.lastIndexOf("/") + 1) + "/inputFiles/";
		
		//All the gaze analytics options
		panel.removeAll();
		panel.revalidate();
		panel.repaint();
		//create folder to put the analysis in
		File snapshotFolder = new File(outputFolderPath + "/SnapshotFolder");
		snapshotFolder.mkdir();
		String outputFolder = snapshotFolder.getPath();
		
		GridBagConstraints c = new GridBagConstraints();
		ButtonGroup bg = new ButtonGroup();
		
		JLabel qLabel = new JLabel("Please pick an option");
		qLabel.setFont(new Font("Verdana", Font.PLAIN, 20));
		
		JPanel optionsPanel = new JPanel(new FlowLayout());
		
		JRadioButton continuousSnapshotButton = new JRadioButton("Continuous Snapshot");
		JRadioButton cumulativeSnapshotButton = new JRadioButton("Cumulative Snapshot");
		JRadioButton overlappingSnapshotButton = new JRadioButton("Overlapping Snapshot");
		JRadioButton eventAnalyticsButton = new JRadioButton("Event Analytics");
		JRadioButton exitBtn = new JRadioButton("Exit");

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

		//adds the buttons to a panel
		c.gridy = 2;
		panel.add(optionsPanel, c);

		JButton btn = new JButton("OK");
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
				String gazepointFile = modifier.fileChooser("Please select which file you would like to parse out", dir);
				JTextField windowSizeInput = new JTextField("", 5);
				JLabel windowSizeLabel = new JLabel("Window Size(seconds): ");
				
				c.gridy = 1;
				JPanel userInputPanel = new JPanel(new FlowLayout());
				userInputPanel.add(windowSizeLabel);
				userInputPanel.add(windowSizeInput);
				panel.add(userInputPanel, c);
				
				JButton contBtn = new JButton("OK");
				c.gridx = 0;
				c.gridy = 2;
				panel.add(contBtn, c);
				panel.revalidate();

				contBtn.addActionListener(ev -> {
					if(continuousSnapshotButton.isSelected())
					{
						try 
						{
							gazeAnalytics.continuousWindow(gazepointFile, outputFolder,Integer.parseInt(windowSizeInput.getText()) );
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
							gazeAnalytics.cumulativeWindow(gazepointFile, outputFolder, Integer.parseInt(windowSizeInput.getText()));
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
			else if(overlappingSnapshotButton.isSelected())
			{
				String gazepointFile = modifier.fileChooser("Please select which file you would like to parse out", dir);
				JTextField windowSizeInput = new JTextField("", 5);
				JTextField overlappingInput = new JTextField("", 5);
				JLabel windowSizeLabel = new JLabel("Window Size(seconds): ");
				JLabel overlappingLabel = new JLabel("Overlapping Amount(seconds): ");

				JPanel userInputPanel0 = new JPanel(new FlowLayout());
				JPanel userInputPanel1 = new JPanel(new FlowLayout());

				c.gridy = 1;
				userInputPanel0.add(windowSizeLabel);
				userInputPanel0.add(windowSizeInput);
				panel.add(userInputPanel0,c);
				
				c.gridy = 2;
				userInputPanel1.add(overlappingLabel);
				userInputPanel1.add(overlappingInput);
				panel.add(userInputPanel1,c);
				
				c.gridy = 3;
				JButton overlappingBtn = new JButton("OK");
				panel.add(overlappingBtn, c);
				panel.revalidate();
				overlappingBtn.addActionListener(ev -> {
					try 
					{
						gazeAnalytics.overlappingWindow(gazepointFile, outputFolder,Integer.parseInt(windowSizeInput.getText()), Integer.parseInt(overlappingInput.getText()) );
					} 
					catch (NumberFormatException | CsvValidationException e1) 
					{
						systemLogger.writeToSystemLog(Level.SEVERE, main.class.getName(), "User input was not a valid number. Unable to create gaze analytics files");
						JOptionPane.showMessageDialog(null, "User input was not a valid number. Unable to create gaze analytics files. Please check system log", "Exit", JOptionPane.ERROR_MESSAGE);

					}
					System.exit(0);


				});

			}
			else if(eventAnalyticsButton.isSelected())
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
					JLabel bLabel = new JLabel("Please pick the baseline value you would want to compare");
					JLabel gzptLabel = new JLabel("Please pick the gaze/fixation value you would want to compare");
					JLabel durLabel = new JLabel("Maxium Duration of an event (seconds): ");
					JTextField maxDurInput = new JTextField("", 5);

					JComboBox<String> baselineCB = new JComboBox<String>(baselineHeader);
					JComboBox<String> gazepointCB = new JComboBox<String>(header);
					baselineCB.setMaximumSize(baselineCB.getPreferredSize());
					gazepointCB.setMaximumSize(gazepointCB.getPreferredSize());

					JPanel userInputPanel0 = new JPanel(new FlowLayout());
					JPanel userInputPanel1 = new JPanel(new FlowLayout());
					JPanel userInputPanel2 = new JPanel(new FlowLayout());
					
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
					JButton eventBtn = new JButton("OK");
					panel.add(eventBtn, c);
					panel.revalidate();

					eventBtn.addActionListener(et -> {
						try 
						{
							gazeAnalytics.eventWindow(gazepointFilePath, outputFolder, baselineFilePath, Arrays.asList(baselineHeader).indexOf(baselineCB.getSelectedItem()), Arrays.asList(header).indexOf(gazepointCB.getSelectedItem()), Integer.valueOf(maxDurInput.getText()));
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
		});
		return panel;
	}

	
	public  String getGZDPath()
	{
		return gazepointGZDPath;
	}
	
	public  String getFXDPath()
	{
		return gazepointFXDPath;
	}
	
	public  String getOutputPath()
	{
		return outputFolderPath;
	}

}
