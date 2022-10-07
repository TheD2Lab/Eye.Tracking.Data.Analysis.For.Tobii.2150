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


public class main {

	public static void main(String args[]) throws IOException, CsvValidationException, NumberFormatException{

		
		String inputURL = "";
		String outputURL = "";
		
		JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

		jfc.setFileFilter(new FileNameExtensionFilter("CSV", "csv"));
		int returnValue = jfc.showOpenDialog(null);
		jfc.setDialogTitle("Select the .csv file you would like to use: ");
		if (returnValue == JFileChooser.APPROVE_OPTION) 
		{
			File selectedFile = jfc.getSelectedFile();
			inputURL = selectedFile.getAbsolutePath();
		}
		else
		{
			JOptionPane.showMessageDialog(null, "Must pick an input file", "Error Message", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
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
	

		/*
		//FXD data
		//testing cases where X axis values are the same
		//String fixationData = "fxdSameXValues.txt";
		String treeFixation = participant + ".treeFXD.txt";
		String treeFixationResults = "treeFXDResults.csv";
		String treeFixationInput = inputLocation + treeFixation;
        String treeFixationOutput = outputLocation + treeFixationResults;
        */
        //String graphFixation = participant + ".GZD.csv";
        String graphFixationResults = "graphFXDResults.csv";
        String graphFixationInput = inputURL;
        String graphFixationOutput = outputLocation + graphFixationResults;
		/*
		//EVD data
		String treeEvent = participant + ".treeEVD.txt";
		String treeEventResults = "treeEVDResults.csv";
		String treeEventInput = inputLocation + treeEvent;
        String treeEventOutput = outputLocation + treeEventResults;
        */
        String graphEvent = participant + ".GZD.csv";
        String graphEventResults = "graphEVDResults.csv";
        String graphEventInput = inputLocation + graphEvent;
        String graphEventOutput = outputLocation + graphEventResults;

        /*
        //GZD data
        String gazeBaseline = participant + "GZD.txt";//input
        String baselineResults = "baselineResults.csv";//output
        String baselineInput = inputLocation + gazeBaseline;
        String baselineOutput = outputLocation + baselineResults;

        String treeGaze = participant + ".treeGZD.txt";
        String treeGazeResults = "treeGZDResults.csv";
        String treeGazeInput = inputLocation + treeGaze;
        String treeGazeOutput = outputLocation + treeGazeResults;
        */
        String graphGaze = participant + ".GZD.csv";
        String graphGazeResults = "graphGZDResults.csv";
        String graphGazeInput = inputLocation + graphGaze;
        String graphGazeOutput = outputLocation + graphGazeResults;
        /*
        //analyze gaze baseline
        gaze.processGaze(baselineInput, baselineOutput);

        //analyze tree related data

        fixation.processFixation(treeFixationInput, treeFixationOutput);
        event.processEvent(treeEventInput, treeEventOutput);
        gaze.processGaze(treeGazeInput, treeGazeOutput);
        */

		// Analyze graph related data
        fixation.processFixation(graphFixationInput, graphFixationOutput);
        event.processEvent(graphEventInput, graphEventOutput);
        gaze.processGaze(graphGazeInput, graphGazeOutput);
	}
}
