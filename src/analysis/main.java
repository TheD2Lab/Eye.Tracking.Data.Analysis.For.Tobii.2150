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
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import com.opencsv.exceptions.CsvValidationException;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;


public class main {

	public static void main(String args[]) throws IOException, CsvValidationException, NumberFormatException {
		String inputFile = "C:\\Users\\kayla\\OneDrive\\Desktop\\Boeing Test Folder\\Data\\gazepoint.GZD.csv";
		inputFile = "C:\\Users\\kayla\\Desktop\\Eye.Tracking.Data.Analysis.For.Tobii.2150\\data\\User 1_all_gaze.csv";
		String outputFolder = "C:\\Users\\kayla\\OneDrive\\Desktop\\Boeing Test Folder\\Results\\Window";
		outputFolder = "C:\\Users\\kayla\\Desktop\\Eye.Tracking.Data.Analysis.For.Tobii.2150\\data\\results";
		gazeAnalytics.continuousWindow(inputFile, outputFolder, 3);
		gazeAnalytics.cumulativeWindow(inputFile, outputFolder, 2);
		gazeAnalytics.overlappingWindow(inputFile, outputFolder, 3, 1);
		/*
		String inputGazeURL = "";
		String inputFixationURL = "";
		String outputURL = "";
		
		JFileChooser jfc = new JFileChooser(System.getProperty("user.dir") + "/data/");

		jfc.setFileFilter(new FileNameExtensionFilter("CSV", "csv"));
		jfc.setDialogTitle("Select the gaze .csv file you would like to use: ");
		int returnValue = jfc.showOpenDialog(null);

		if (returnValue == JFileChooser.APPROVE_OPTION) 
		{
			File selectedFile = jfc.getSelectedFile();
			inputGazeURL = selectedFile.getAbsolutePath();
		}
		else
		{
			JOptionPane.showMessageDialog(null, "Must pick an input file", "Error Message", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		// Prompts user to select fixation .csv file
		jfc.setDialogTitle("Select the fixation .csv file you would like to use: ");
		returnValue = jfc.showOpenDialog(null);
		if (returnValue == JFileChooser.APPROVE_OPTION) 
		{
			File selectedFile = jfc.getSelectedFile();
			inputFixationURL = selectedFile.getAbsolutePath();
		}
		else
		{
			JOptionPane.showMessageDialog(null, "Must pick an input file", "Error Message", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		// Prompts user to select a location to save output files
		jfc = new JFileChooser(System.getProperty("user.dir") + "/results/");
		jfc.setDialogTitle("Choose a directory to save your file: ");
		jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		returnValue = jfc.showSaveDialog(null);
		if (returnValue == JFileChooser.APPROVE_OPTION) 
		{
			if (jfc.getSelectedFile().isDirectory()) 
			{
				outputURL = jfc.getSelectedFile().toString();
			}
		}
		else
		{
			JOptionPane.showMessageDialog(null, "Must pick a location to output the file", "Error Message", JOptionPane.ERROR_MESSAGE);
			return;
		}
				
		String participant = JOptionPane.showInputDialog(null, "Participant's Name", null , JOptionPane.INFORMATION_MESSAGE);
		File participantFolder = new File(outputURL + "\\" + participant);
		if(!participantFolder.exists())
		{
			boolean folderCreated = participantFolder.mkdir();
			if(!folderCreated)
			{
				JOptionPane.showMessageDialog(null, "Unable to create participant's folder", "Error Message", JOptionPane.ERROR_MESSAGE);
				return;
			}
		}
		
		outputURL += "\\" + participant;
		
	
		//File paths
        String graphFixationResults = "\\graphFXDResults.csv";
        String graphFixationOutput = outputURL + graphFixationResults;

        String graphEventResults = "\\graphEVDResults.csv";
        String graphEventOutput = outputURL + graphEventResults;

        String graphGazeResults = "\\graphGZDResults.csv";
        String graphGazeOutput = outputURL + graphGazeResults;


		// Analyze graph related data
        fixation.processFixation(inputFixationURL, graphFixationOutput);
        event.processEvent(inputGazeURL, graphEventOutput);
        gaze.processGaze(inputGazeURL, graphGazeOutput);

        
        gazeAnalytics.csvToARFF(graphFixationOutput);
        gazeAnalytics.csvToARFF(graphEventOutput);
        gazeAnalytics.csvToARFF(graphGazeOutput);
	}
	*/
	
	}
}
