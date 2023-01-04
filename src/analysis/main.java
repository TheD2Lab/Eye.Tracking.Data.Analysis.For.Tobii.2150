package analysis;
/*
 * Copyright (c) 2013, Bo Fu
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

import java.io.File;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import org.apache.commons.lang3.math.NumberUtils;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;
import java.util.Arrays;
import java.awt.*;

public class main 
{
	public static void main(String args[]) throws IOException, CsvValidationException, NumberFormatException 
	{

		//createBaselineFile("C:\\Users\\kayla\\Desktop\\Eye.Tracking.Data.Analysis.For.Tobii.2150\\data\\Kayla _all_gaze.csv", "C:\\Users\\kayla\\Documents\\temp");
		
		//find the folder and input file paths
		String[] paths = new String[3];

		findFolderPath(paths);
		String[] modifiedData = addDataMetrics(new String[] {paths[0], paths[1]}, paths[2]);
		String gazepointGZDPath = modifiedData[0];
		String gazepointFXDPath = modifiedData[1];
		String outputFolderPath = paths[2];
		
		//create the system log
		systemLogger.createSystemLog(outputFolderPath);

		// Resolution of monitor 
		final int SCREEN_WIDTH = 1024;
		final int SCREEN_HEIGHT = 768;

		//output file paths
		String graphFixationResults = "/graphFXDResults.csv";
		String graphFixationOutput = outputFolderPath + graphFixationResults;

		String graphEventResults = "/graphEVDResults.csv";
		String graphEventOutput = outputFolderPath + graphEventResults;

		String graphGazeResults = "/graphGZDResults.csv";
		String graphGazeOutput = outputFolderPath + graphGazeResults;


		String aoiResults = "/aoiResults.csv";
		String aoiOutput = outputFolderPath + aoiResults;

		// Analyze graph related data
		fixation.processFixation(gazepointFXDPath, graphFixationOutput, SCREEN_WIDTH, SCREEN_HEIGHT);
		event.processEvent(gazepointGZDPath, graphEventOutput);
		gaze.processGaze(gazepointGZDPath, graphGazeOutput);
		createBaselineFile(gazepointGZDPath, outputFolderPath);


		// Gaze Analytics 
		gazeAnalytics.csvToARFF(graphFixationOutput);
		gazeAnalytics.csvToARFF(graphEventOutput);
		gazeAnalytics.csvToARFF(graphGazeOutput);
		
		//combining all result files
		mergingResultFiles(graphFixationOutput, graphEventOutput, graphGazeOutput, outputFolderPath + "/combineResults.csv");
		gazeAnalytics.csvToARFF(outputFolderPath + "/combineResults.csv");

		// Analyze AOI data
		AOI.processAOIs(gazepointGZDPath, aoiOutput, SCREEN_WIDTH, SCREEN_HEIGHT);


		//User Interface for selecting type of gaze analytics
		JFrame mainFrame = new JFrame("Would you like the program to output snapshots of the gaze/fixation data output");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setVisible(true);
		mainFrame.setSize(400,400);

		JPanel mainPanel = new JPanel();
		JRadioButton yesButton = new JRadioButton("Yes");
		JRadioButton noButton = new JRadioButton("No");
		ButtonGroup bg = new ButtonGroup();

		bg.add(yesButton);
		bg.add(noButton);
		mainPanel.add(yesButton);
		mainPanel.add(noButton);
		mainFrame.add(mainPanel);

		JButton btn = new JButton("OK");
		mainPanel.add(btn);
		btn.addActionListener(e -> {
			if(yesButton.isSelected())
			{
				//removes all the objects from the panel
				mainPanel.removeAll();
				gazeAnalyticsOptions(mainPanel, outputFolderPath);
			}
			else if(noButton.isSelected())
			{
				System.exit(0);
			}
		});
		
		

	}


	/*
	 * merges all the input files 
	 * 
	 * @param	filePaths	an array where all the file paths will be stored
	 */
	private static void mergingResultFiles(String FXD, String EVD, String GZD, String outputFile) throws IOException
	{

 		FileWriter outputFileWriter = new FileWriter(new File (outputFile));
        CSVWriter outputCSVWriter = new CSVWriter(outputFileWriter);
        
        FileReader fileReaderFXD = new FileReader(FXD);
        CSVReader csvReaderFXD = new CSVReader(fileReaderFXD);
        FileReader fileReaderEVD = new FileReader(EVD);
        CSVReader csvReaderEVD = new CSVReader(fileReaderEVD);
        FileReader fileReaderGZD = new FileReader(GZD);
        CSVReader csvReaderGZD = new CSVReader(fileReaderGZD);

        try
        {
    		Iterator<String[]> iterFXD = csvReaderFXD.iterator();
    		Iterator<String[]> iterEVD = csvReaderEVD.iterator();
    		Iterator<String[]> iterGZD = csvReaderGZD.iterator();
    		String[] rowFXD= new String[0];
    		String[] rowEVD = new String[0];
    		String[] rowGZD = new String[0];

        	while(iterFXD.hasNext())
        	{
        		rowGZD = iterGZD.next();
        		rowEVD = iterEVD.next();
        		rowFXD = iterFXD.next();
            	String[]results = new String[rowGZD.length + rowEVD.length + rowFXD.length];
            	System.arraycopy(rowGZD, 0, results, 0, rowGZD.length);
            	System.arraycopy(rowEVD, 0, results, rowGZD.length, rowEVD.length);
            	System.arraycopy(rowFXD, 0, results, rowEVD.length + rowGZD.length, rowFXD.length);
            	outputCSVWriter.writeNext(results);
        	}

        }
        catch(Exception e)
        {
        	System.out.println(e);
			systemLogger.writeToSystemLog(Level.WARNING, main.class.getName(), "");
        }
        finally
        {
        	outputCSVWriter.close();
        	csvReaderFXD.close();
        	csvReaderEVD.close();
        	csvReaderGZD.close();
        }
	}
	/*
	 * find all the paths for the input files and the output folder location
	 * 
	 * @param	filePaths	an array where all the file paths will be stored
	 */
	private static void findFolderPath(String[]filePaths)
	{
		String gazepointGZDPath = fileChooser("Select the gaze .csv file you would like to use", "/data/");
		String gazepointFXDPath = fileChooser("Select the fixation .csv file you would like to use", "/data/");
		String outputPath = folderChooser("Choose a directory to save your file");

		String participant = JOptionPane.showInputDialog(null, "Participant's Name", null , JOptionPane.INFORMATION_MESSAGE);
		File participantFolder = new File(outputPath + "\\" + participant);

		//creates the folder only if it doesn't exists already
		if(!participantFolder.exists())
		{
			boolean folderCreated = participantFolder.mkdir();
			if(!folderCreated)
			{
				JOptionPane.showMessageDialog(null, "Unable to create participant's folder", "Error Message", JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			}
		}

		outputPath += "\\" + participant;

		filePaths[0] = gazepointGZDPath;
		filePaths[1] = gazepointFXDPath;
		filePaths[2] =  outputPath;

	}



	/*
	 * UI where the user will select which the type of gaze analytics that they will want to output
	 * UI where the user will input the information needed for the program to output the desired files
	 * 
	 * @param	p				the panel the UI will be placed on
	 * @param	outputfolder	the folder path where all the files will be placed in
	 */
	private static void gazeAnalyticsOptions(JPanel p, String outputFolderPath)
	{
		String dir = "/results/" + outputFolderPath.substring(outputFolderPath.lastIndexOf("/") + 1) + "/inputFiles/";
		
		//All the gaze analytics options
		p.removeAll();

		//create folder to put the anaylsis in
		File snapshotFolder = new File(outputFolderPath + "/SnapshotFolder");
		snapshotFolder.mkdir();
		String outputFolder = snapshotFolder.getPath();

		JRadioButton continuousSnapshotButton = new JRadioButton("Continuous Snapshot");
		JRadioButton cumulativeSnapshotButton = new JRadioButton("Cumulative Snapshot");
		JRadioButton overlappingSnapshotButton = new JRadioButton("Overlapping Snapshot");
		JRadioButton eventAnalyticsButton = new JRadioButton("Event Analytics");

		//Adds all the JRadioButton to a layout
		ButtonGroup bg = new ButtonGroup();
		bg.add(continuousSnapshotButton);
		bg.add(cumulativeSnapshotButton);
		bg.add(overlappingSnapshotButton);
		bg.add(eventAnalyticsButton);

		//adds the buttons to a panel
		p.add(continuousSnapshotButton);
		p.add(cumulativeSnapshotButton);
		p.add(overlappingSnapshotButton);
		p.add(eventAnalyticsButton);

		JButton btn = new JButton("OK");
		p.add(btn);
		p.revalidate();

		//checks what button has been selected and generates the required files 
		btn.addActionListener(e -> {
			p.removeAll();
			p.repaint();

			if(continuousSnapshotButton.isSelected()||cumulativeSnapshotButton.isSelected())
			{
				String gazepointFile = fileChooser("Please select which file you would like to parse out", dir);
				JTextField windowSizeInput = new JTextField("", 5);
				JLabel windowSizeLabel = new JLabel("Window Size: ");
				p.add(windowSizeLabel);
				p.add(windowSizeInput);
				JButton contBtn = new JButton("OK");
				p.add(contBtn);
				p.revalidate();

				contBtn.addActionListener(ev -> {
					if(continuousSnapshotButton.isSelected())
					{
						try 
						{
							gazeAnalytics.continuousWindow(gazepointFile, outputFolder,Integer.parseInt(windowSizeInput.getText()) );
						} 
						catch (NumberFormatException e1) 
						{
							systemLogger.writeToSystemLog(Level.SEVERE, main.class.getName(), "User input was not a valid number. Unable to create gaze analytics files");
						}
						System.exit(0);
					}
					else
					{
						try 
						{
							gazeAnalytics.cumulativeWindow(gazepointFile, outputFolder, Integer.parseInt(windowSizeInput.getText()));
						} 
						catch (NumberFormatException e1) 
						{
							systemLogger.writeToSystemLog(Level.SEVERE, main.class.getName(), "User input was not a valid number. Unable to create gaze analytics files");
						}
						System.exit(0);
					}
				});

			}
			else if(overlappingSnapshotButton.isSelected())
			{
				String gazepointFile = fileChooser("Please select which file you would like to parse out", dir);
				JTextField windowSizeInput = new JTextField("", 5);
				JTextField overlappingInput = new JTextField("", 5);
				JLabel windowSizeLabel = new JLabel("Window Size: ");
				JLabel overlappingLabel = new JLabel("Overlapping Amount: ");

				p.add(windowSizeLabel);
				p.add(windowSizeInput);
				p.add(overlappingLabel);
				p.add(overlappingInput);
				JButton overlappingBtn = new JButton("OK");
				p.add(overlappingBtn);
				p.revalidate();
				overlappingBtn.addActionListener(ev -> {
					try 
					{
						gazeAnalytics.overlappingWindow(gazepointFile, outputFolder,Integer.parseInt(windowSizeInput.getText()), Integer.parseInt(overlappingInput.getText()) );
					} 
					catch (NumberFormatException e1) 
					{
						systemLogger.writeToSystemLog(Level.SEVERE, main.class.getName(), "User input was not a valid number. Unable to create gaze analytics files");
					}
					System.exit(0);


				});

			}
			else if(eventAnalyticsButton.isSelected())
			{
				String gazepointFilePath = fileChooser("Please select your gaze file", dir);
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

					p.add(bLabel);
					p.add(baselineCB);
					p.add(gzptLabel);
					p.add(gazepointCB);
					p.add(durLabel);
					p.add(maxDurInput);

					JButton eventBtn = new JButton("OK");
					p.add(eventBtn);
					p.revalidate();

					eventBtn.addActionListener(et -> {
						try 
						{
							gazeAnalytics.eventWindow(gazepointFilePath, outputFolder, baselineFilePath, Arrays.asList(baselineHeader).indexOf(baselineCB.getSelectedItem()), Arrays.asList(header).indexOf(gazepointCB.getSelectedItem()), Integer.valueOf(maxDurInput.getText()));
						} 
						catch (NumberFormatException | IOException e1) 
						{
							systemLogger.writeToSystemLog(Level.SEVERE, main.class.getName(), "User input was not a valid number. Unable to create gaze analytics files");
						}
						System.exit(0);

					});

				} 
				catch (IOException | CsvValidationException e1) 
				{
					systemLogger.writeToSystemLog(Level.SEVERE, main.class.getName(), "Unable to find selected baseline or input files" + e1);
					System.exit(0);
				}




			}
		});
	}

	/*
	 * create baseline file
	 * grabs the first two minutes of the file and averages it
	 * 
	 */
	private static void createBaselineFile(String filePath, String outputFolder) throws IOException, CsvValidationException
	{
		FileWriter outputFileWriter = new FileWriter(new File (outputFolder + "/baselineModifiedFile.csv"));
		CSVWriter outputCSVWriter = new CSVWriter(outputFileWriter);
		FileReader fileReader = new FileReader(filePath);
		CSVReader csvReader = new CSVReader(fileReader);
		String[]nextLine = csvReader.readNext();//header
		outputCSVWriter.writeNext(nextLine);
		
		try 
		{
			
			while((nextLine = csvReader.readNext()) != null)
			{
				if(Double.valueOf(nextLine[3]) <= 120) //two minutes
				{
					outputCSVWriter.writeNext(nextLine);
				}
			}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		finally
		{
			outputCSVWriter.close();
			csvReader.close();

		}
		gaze.processGaze(outputFolder + "/baselineModifiedFile.csv", outputFolder + "/baseline.csv");
		gazeAnalytics.csvToARFF(outputFolder + "/baseline.csv");
	}


	/*
	 * UI for users to select the file they want to use
	 * 
	 * @param	dialogTitle		title of the window
	 * @param 	directory		directory to choose file from relative to project directory		
	 */
	private static String fileChooser(String dialogTitle, String directory)
	{
		//Initializes the user to a set directory
		JFileChooser jfc = new JFileChooser(System.getProperty("user.dir")  + directory);

		//ensures that only CSV files will be able to be selected
		jfc.setFileFilter(new FileNameExtensionFilter("CSV", "csv"));
		jfc.setDialogTitle(dialogTitle);
		int returnValue = jfc.showOpenDialog(null);

		if (returnValue == JFileChooser.APPROVE_OPTION) 
		{
			File selectedFile = jfc.getSelectedFile();
			return selectedFile.getAbsolutePath();
		}
		else
		{
			JOptionPane.showMessageDialog(null, "Must pick a file", "Error Message", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		return "";
	}


	/*
	 * UI for users to select the folder they would want to use to place files in
	 * 
	 * @param	dialogTitle		title of the window
	 */
	private static String folderChooser(String dialogTitle)
	{
		//Initializes the user to a set directory
		JFileChooser jfc = new JFileChooser(System.getProperty("user.dir") + "/results/");
		jfc.setDialogTitle("Choose a directory to save your file: ");

		//only directories can be selected
		jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnValue = jfc.showSaveDialog(null);
		if (returnValue == JFileChooser.APPROVE_OPTION) 
		{
			if (jfc.getSelectedFile().isDirectory()) 
				return jfc.getSelectedFile().toString();
		}
		else
		{
			JOptionPane.showMessageDialog(null, "Must pick a location to output the file", "Error Message", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		return "";
	}
	
	/*
	 * Modifies input files to contain a saccade velocity column and stores those files in a new folder
	 * 
	 * @param	inputFiles	Array of size 2 containing the path to the input fixation and gaze data files
	 * @param	outputPath	String of the output path
	 */
	private static String[] addDataMetrics(String[] inputFiles, String dir) 
	{
		String name = dir.substring(dir.lastIndexOf("\\"));
		String[] outputFiles = new String[] {dir + "/inputFiles/" + name + "_all_gaze.csv", dir + "/inputFiles/" + name + "_fixation.csv"};
		File folder = new File(dir + "/inputFiles");
		
		// Create a folder to store the input files if it doesn't already exist
		if(!folder.exists()) 
		{
			boolean folderCreated = folder.mkdir();
			if(!folderCreated)
				System.err.println("Unable to create modified data files folder.");
		}
		
		// Clone the input files and create a new column SACCADE_VEL
		try 
		{
			for (int i = 0; i < inputFiles.length; i++) 
			{
				CSVReader reader = new CSVReader(new FileReader(new File(inputFiles[i])));
				CSVWriter writer = new CSVWriter(new FileWriter(new File(outputFiles[i])));
				Iterator<String[]> iter = reader.iterator();
				
				// Write the headers to the file
				ArrayList<String> headers = new ArrayList<String>(Arrays.asList(iter.next()));
				int validityIndex = headers.indexOf("FPOGV");
				int fixationID = headers.indexOf("FPOGID");
				headers.add("SACCADE_VEL");
				writer.writeNext(headers.toArray(new String[headers.size()]));
				
				// Write the first row to the file
				String[] prevRow = iter.next();
				
				ArrayList<String> row = new ArrayList<String>(Arrays.asList(prevRow));
				row.add("" + 0);
				writer.writeNext(row.toArray(new String[row.size()]));
				
				// Find the indexes of time and saccade direction
				int time = -1;
				int sacDir = -1;
				
				for (int j = 0; j < headers.size(); j++) 
				{
					if (time == -1 && headers.get(j).contains("TIME"))
						time = j;
					if (headers.get(j).equals("SACCADE_DIR"))
						sacDir = j;
				}
				
				if (time == -1 || sacDir == -1)
				{
					JOptionPane.showMessageDialog(null, "Data file does not contain time column or saccade direction column", "Error Message", JOptionPane.ERROR_MESSAGE);
					System.exit(0);
				}
				
				
				
				while (iter.hasNext()) 
				{
					String[] currRow = iter.next();
					row = new ArrayList<String>(Arrays.asList(currRow));
					row.add(Double.toString(Double.valueOf(currRow[sacDir])/Math.abs(Double.valueOf(currRow[time]) - Double.valueOf(prevRow[time]))));
					writer.writeNext(row.toArray(new String[row.size()]));
					
					// Check to make sure the current row is a fixation
					if (Double.valueOf(currRow[sacDir]) != 0)
						prevRow = currRow;
					
					// For the very first fixation, find the last valid point
					if (Double.valueOf(currRow[fixationID]) == 1)
					{
						if (Integer.valueOf(currRow[validityIndex]) == 1)
							prevRow = currRow;
						System.out.println(prevRow[time]);
					}
						
				}
				
				reader.close();
				writer.close();
			}
		}
		catch (Exception e) 
		{
			System.err.println(e);
			System.exit(0);
		}

		return outputFiles;
	}
}
