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
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;


public class main {

	public static void main(String args[]) throws IOException, CsvValidationException, NumberFormatException {
		//		String inputFile = "C:\\Users\\kayla\\OneDrive\\Desktop\\Boeing Test Folder\\Data\\gazepoint.GZD.csv";
		//		inputFile = "C:\\Users\\kayla\\Desktop\\Eye.Tracking.Data.Analysis.For.Tobii.2150\\data\\User 1_all_gaze.csv";
		//		String outputFolder = "C:\\Users\\kayla\\OneDrive\\Desktop\\Boeing Test Folder\\Results\\Window";
		//		outputFolder = "C:\\Users\\kayla\\Desktop\\Eye.Tracking.Data.Analysis.For.Tobii.2150\\data\\results";
		//		gazeAnalytics.continuousWindow(inputFile, outputFolder, 3);
		//		gazeAnalytics.cumulativeWindow(inputFile, outputFolder, 2);
		//		gazeAnalytics.overlappingWindow(inputFile, outputFolder, 3, 1);
		
//		String baselineFilePath = "C:\\Users\\kayla\\Desktop\\School\\Direct Studies\\graphGZDResults.csv";
//		String inputFilePath = "C:\\Users\\kayla\\Downloads\\User 3_all_gaze.csv";
//		String outputFolderPath0 = "C:\\Users\\kayla\\Desktop\\Eye.Tracking.Data.Analysis.For.Tobii.2150\\data\\results";
//
//		gazeAnalytics.eventWindow(inputFilePath, outputFolderPath0, baselineFilePath, 3, 35, 5);

		
		String[] paths = new String[3];
		calcuations(paths);
		String inputGazePath = paths[0];
		String inputFixationPath = paths[1];
		String outputFolderPath = paths[2];

		//File paths
		String graphFixationResults = "\\graphFXDResults.csv";
		String graphFixationOutput = outputFolderPath + graphFixationResults;

		String graphEventResults = "\\graphEVDResults.csv";
		String graphEventOutput = outputFolderPath + graphEventResults;

		String graphGazeResults = "\\graphGZDResults.csv";
		String graphGazeOutput = outputFolderPath + graphGazeResults;
		
		// Analyze graph related data
		fixation.processFixation(inputFixationPath, graphFixationOutput);
		event.processEvent(inputGazePath, graphEventOutput);
		gaze.processGaze(inputGazePath, graphGazeOutput);
		
		//Gaze Analytics
		
		

		//        
		//        gazeAnalytics.csvToARFF(graphFixationOutput);
		//        gazeAnalytics.csvToARFF(graphEventOutput);
 c 		//        gazeAnalytics.csvToARFF(graphGazeOutput);
	}

	private static void calcuations(String[]filePaths)
	{
		String inputGazePath = fileChooser("Select the gaze .csv file you would like to use");
		String inputFixationPath = fileChooser("Select the fixation .csv file you would like to use");
		String outputPath = folderChooser("Choose a directory to save your file");

		String participant = JOptionPane.showInputDialog(null, "Participant's Name", null , JOptionPane.INFORMATION_MESSAGE);
		File participantFolder = new File(outputPath + "\\" + participant);
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

	private static void gazeAnalyticsSnapshot(String outputFolderPath) throws CsvValidationException, IOException
	{
		String baselineFilePath = "";
		String inputFilePath = "";
		
		baselineFilePath = fileChooser("Select the baseline CSV file you would like to use ");
		inputFilePath = fileChooser("Select the input CSV file you would like to use ");
		
		
		FileReader baslineFileReader = new FileReader(baselineFilePath);
		CSVReader baselineCSVReader = new CSVReader(baslineFileReader);
		String[]baselineHeader = baselineCSVReader.readNext();

		FileReader inputFileReader = new FileReader(inputFilePath);
		CSVReader inputCSVReader = new CSVReader(inputFileReader);
		String[]inputHeader = inputCSVReader.readNext();

		JFrame eventFrame = new JFrame("Define the event");
		eventFrame.setVisible(true);
		eventFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		eventFrame.setSize(400, 250);
		JPanel eventPanel = new JPanel();
		eventFrame.add(eventPanel);

		JLabel baselineLabel = new JLabel("Please select the baseline value you would like to use");
		eventPanel.add(baselineLabel);

		final JComboBox<String> baselineHeaderOption = new JComboBox<String>(baselineHeader);
		baselineHeaderOption.setVisible(true);
		baselineHeaderOption.setEditable(true);
		eventPanel.add(baselineHeaderOption);
		
		
		JLabel inputLabel = new JLabel("Please select the input header that correspond with the baslineHeader");
		eventPanel.add(inputLabel);
		
		final JComboBox<String> inputHeaderOption = new JComboBox<String>(inputHeader);
		inputHeaderOption.setVisible(true);
		inputHeaderOption.setEditable(true);
		eventPanel.add(inputHeaderOption);

		JButton btn = new JButton("OK");
		eventPanel.add(btn);
		btn.addActionListener(e -> {
			eventFrame.dispose();
		});
		
		gazeAnalytics.eventWindow(inputFilePath, outputFolderPath, baselineFilePath, baselineHeaderOption.getSelectedIndex(), inputHeaderOption.getSelectedIndex(), 5);
        
	}
	
	private static String fileChooser(String dialogTitle)
	{
		JFileChooser jfc = new JFileChooser(System.getProperty("user.dir") + "/data/");

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
	
	private static String folderChooser(String dialogTitle)
	{
		JFileChooser jfc = new JFileChooser(System.getProperty("user.dir") + "/results/");
		jfc.setDialogTitle("Choose a directory to save your file: ");
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
