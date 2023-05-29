package bulkAnalysis;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import analysis.SingleAnalytics;

public class main {
	
	private static final String GZD_SUFFIX = "all_gaze.csv";
	private static final String FXD_SUFFIX = "fixations.csv";

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		JFrame frame = new JFrame("");
		//JTabbedPane tp=new JTabbedPane();  
		JFileChooser chooser = new JFileChooser();
		HashMap<String, String>partInfo = new HashMap<>();

		chooser.setFileFilter(new FileNameExtensionFilter("CSV", "csv"));
		chooser.setMultiSelectionEnabled(true);
		do {
		int returnValue = chooser.showOpenDialog(frame);
		if (returnValue != JFileChooser.APPROVE_OPTION) 
		{
			JOptionPane.showMessageDialog(null, "Must pick a file", "Error Message", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		
		
		}while(!validation(chooser.getSelectedFiles(),partInfo));
		
		
		String outputLocation = analysis.modifier.folderChooser("Please choose the location where you would like your files to reside in");
		if(!createFolders(outputLocation, partInfo))
		{
			JOptionPane.showMessageDialog(null, "Error in creating files. Please see error log for more details", "Error Message", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		
		runAnalysis(outputLocation,partInfo );
		
		System.out.println("done");
	}
	
	private static boolean createFolders(String outputLocation, HashMap<String, String>partInfo)
	{
		String message = "";
		for(String name: partInfo.keySet())
		{
			File folder = new File(outputLocation + "/" + name);
			
			// Create a folder to store the input files if it doesn't already exist
			if(!folder.exists()) {
				boolean folderCreated = folder.mkdir();
				if(!folderCreated)
				{
					message += name + ": Unable to create modified data files folder.";
					return false;
				}
			}
		}
		
		if(!message.equals(""))
		{
			return false;
		}
		return true;		
	}

	private static void runAnalysis(String outputLocation, HashMap<String,String>partInfo) throws IOException
	{
		for(String name: partInfo.keySet())
		{
			String currentPartFolderName = outputLocation + "/" + name;
			String gzdPath = partInfo.get(name) + "/" + name + "_" + GZD_SUFFIX;
			String fxdPath = partInfo.get(name) + "/" + name + "_" + FXD_SUFFIX;
			
			SingleAnalytics panels = new SingleAnalytics();
			panels.analyzeData(gzdPath, fxdPath, currentPartFolderName);
			
		}
	}
	
	
	private static boolean validation(File[]files, HashMap<String,String>partInfo)
	{
		String message = "";
		HashMap<String, Integer>partName = new HashMap<>();
		for(File file: files)
		{
			String[]fName = file.getName().split("_",2);

			//checks if the naming convention was incorrect
			if(!fName[1].equals(GZD_SUFFIX) && !fName[1].equals(FXD_SUFFIX))
			{
				message += file.getName() + " Unable to use this file. Please check naming convention \n";
			}


			if(partName.containsKey(fName[0]))
			{
				partName.put(fName[0],partName.get(fName[0])+1);
			}
			else
			{
				partName.put(fName[0],1);
				partInfo.put(fName[0], file.getParent());
			}
		}

		//finds the extra or missing files
		for(String p: partName.keySet())
		{
			if(partName.get(p) > 2)
			{
				message += p + " has too many files. # of files attached to particpant " + partName.get(p) + "\n";
			}
			if(partName.get(p) < 2)
			{
				message += p + " does not have enough files. # of files attached to particpant " + partName.get(p) + "\n";
			}
		}

		//checks for any error message
		if(!message.equals(""))
		{
			JOptionPane.showMessageDialog(null, message, "Error Message", JOptionPane.ERROR_MESSAGE);
			partInfo = new HashMap<>();
			return false;
		}
		
		return true;
		
	}

}
