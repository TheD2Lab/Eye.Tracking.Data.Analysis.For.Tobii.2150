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
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

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

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;

public class main {

	public static void main(String args[]) throws IOException, CsvValidationException, NumberFormatException {
		//find the folder and input file paths
		String[] paths = new String[3];
		
		findFolderPath(paths);
		String inputGazePath = paths[0];
		String inputFixationPath = paths[1];
		String outputFolderPath = paths[2];
		
		//create the system log
		systemLogger.createSystemLog(outputFolderPath);
		
		// Resolution of monitor
		final int SCREEN_WIDTH = 1024;
		final int SCREEN_HEIGHT = 768;

		//output file paths
		String graphFixationResults = "\\graphFXDResults.csv";
		String graphFixationOutput = outputFolderPath + graphFixationResults;

		String graphEventResults = "\\graphEVDResults.csv";
		String graphEventOutput = outputFolderPath + graphEventResults;

		String graphGazeResults = "\\graphGZDResults.csv";
		String graphGazeOutput = outputFolderPath + graphGazeResults;
		
		
		String aoiResults = "\\aoiResults.csv";
		String aoiOutput = outputFolderPath + aoiResults;
		
		//combining all result files
		mergingCSVFiles(inputFixationPath, inputGazePath, outputFolderPath+ "\\combine.csv");
		 
		// Analyze graph related data
		fixation.processFixation(inputFixationPath, graphFixationOutput, SCREEN_WIDTH, SCREEN_HEIGHT);
		event.processEvent(inputGazePath, graphEventOutput);
		gaze.processGaze(inputGazePath, graphGazeOutput);

		
		// Gaze Analytics 
		gazeAnalytics.csvToARFF(graphFixationOutput);
		gazeAnalytics.csvToARFF(graphEventOutput);
		gazeAnalytics.csvToARFF(graphGazeOutput);

		// Analyze AOI data
		AOI.processAOIs(inputGazePath, aoiOutput, SCREEN_WIDTH, SCREEN_HEIGHT);
		
		//User Interface for selecting type of gaze analytics
		JFrame f = new JFrame("Would you like to select a window");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
		f.setSize(400,400);

		JPanel p = new JPanel();
		JRadioButton yesButton = new JRadioButton("Yes");
		JRadioButton noButton = new JRadioButton("No");
		ButtonGroup bg = new ButtonGroup();
		
		bg.add(yesButton);
		bg.add(noButton);
		p.add(yesButton);
		p.add(noButton);
		f.add(p);
		
		JButton btn = new JButton("OK");
		p.add(btn);
		btn.addActionListener(e -> {
			if(yesButton.isSelected())
			{
				//removes all the objects from the panel
				p.removeAll();
				gazeAnalyticsOptions(p, outputFolderPath);
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
	private static void mergingCSVFiles(String FXD, String GZD, String outputFile) throws IOException
	{
 		FileWriter outputFileWriter = new FileWriter(new File (outputFile));
        CSVWriter outputCSVWriter = new CSVWriter(outputFileWriter);
        
        FileReader fileReaderFXD = new FileReader(FXD);
        CSVReader csvReaderFXD = new CSVReader(fileReaderFXD);
        FileReader fileReaderGZD = new FileReader(GZD);
        CSVReader csvReaderGZD = new CSVReader(fileReaderGZD);
		Iterator<String[]> iterFXD = csvReaderFXD.iterator();
		Iterator<String[]> iterGZD = csvReaderGZD.iterator();
		String[] rowFXD= new String[0];
		String[] rowGZD = new String[0];
    	String[]results = new String[120];
        try
        {
        	while(iterFXD.hasNext())
        	{
        		rowGZD = iterGZD.next();
        		rowFXD = iterFXD.next();
            	System.arraycopy(rowGZD, 0, results, 0, rowGZD.length);
            	System.arraycopy(rowFXD, 0, results, rowGZD.length, rowFXD.length);
            	outputCSVWriter.writeNext(results);
        	}
        	
        	while(iterGZD.hasNext())
        	{
        		outputCSVWriter.writeNext(iterGZD.next());
        	}

        }
        catch(Exception e)
        {
        	System.out.println(e);
        	System.exit(0);
        }
        finally
        {
        	outputCSVWriter.close();
        	csvReaderFXD.close();
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
		String inputGazePath = fileChooser("Select the gaze .csv file you would like to use");
		String inputFixationPath = fileChooser("Select the fixation .csv file you would like to use");
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

		filePaths[0] = inputGazePath;
		filePaths[1] = inputFixationPath;
		filePaths[2] =  outputPath;

	}
	

	
	/*
	 * UI where the user will select which the type of gaze analytics that they will want to output
	 * UI where the user will input the information needed for the program to output the desired files
	 * 
	 * @param	p				the panel the UI will be placed on
	 * @param	outputfolder	the folder path where all the files will be placed in
	 */
	private static void gazeAnalyticsOptions(JPanel p, String outputFolder)
	{
		//All the gaze analytics options
		p.removeAll();
		JRadioButton continuousWindowButton = new JRadioButton("Continuous Window");
		JRadioButton cumulativeWindowButton = new JRadioButton("Cumulative Window");
		JRadioButton overlappingWindowButton = new JRadioButton("Overlapping Window");
		
		//Adds all the JRadioButton to a layout
		ButtonGroup bg = new ButtonGroup();
		bg.add(continuousWindowButton);
		bg.add(cumulativeWindowButton);
		bg.add(overlappingWindowButton);
		
		//adds the buttons to a panel
		p.add(continuousWindowButton);
		p.add(cumulativeWindowButton);
		p.add(overlappingWindowButton);
		
		JButton btn = new JButton("OK");
		p.add(btn);
		p.revalidate();
		
		//checks what button has been selected and generates the required files 
		btn.addActionListener(e -> {
			String inputFile = fileChooser("Please select which file you would like to parse out");
			p.removeAll();
			p.repaint();
			
			if(continuousWindowButton.isSelected()||cumulativeWindowButton.isSelected())
			{
				JTextField windowSizeInput = new JTextField("", 5);
				JLabel windowSizeLabel = new JLabel("Window Size: ");
				p.add(windowSizeLabel);
				p.add(windowSizeInput);
				JButton contBtn = new JButton("OK");
				p.add(contBtn);
				p.revalidate();
				
				contBtn.addActionListener(ev -> {
					if(continuousWindowButton.isSelected())
					{
						try 
						{
							gazeAnalytics.continuousWindow(inputFile, outputFolder,Integer.parseInt(windowSizeInput.getText()) );
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
							gazeAnalytics.cumulativeWindow(inputFile, outputFolder, Integer.parseInt(windowSizeInput.getText()));
						} 
						catch (NumberFormatException e1) 
						{
							 systemLogger.writeToSystemLog(Level.SEVERE, main.class.getName(), "User input was not a valid number. Unable to create gaze analytics files");
						}
						System.exit(0);
					}
				});

			}
			else if(overlappingWindowButton.isSelected())
			{
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
						gazeAnalytics.overlappingWindow(inputFile, outputFolder,Integer.parseInt(windowSizeInput.getText()), Integer.parseInt(overlappingInput.getText()) );
					} 
					catch (NumberFormatException e1) 
					{
						 systemLogger.writeToSystemLog(Level.SEVERE, main.class.getName(), "User input was not a valid number. Unable to create gaze analytics files");
					}
					System.exit(0);


				});

			}
		});
	}
		

	
	/*
	 * UI for users to select the file they want to use
	 * 
	 * @param	dialogTitle		title of the window
	 */
	private static String fileChooser(String dialogTitle)
	{
		//Initializes the user to a set directory
		JFileChooser jfc = new JFileChooser(System.getProperty("user.dir") + "/data/");

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
			{
				return jfc.getSelectedFile().toString();
			}
		}
		else
		{
			JOptionPane.showMessageDialog(null, "Must pick a location to output the file", "Error Message", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		return "";
	}



}
