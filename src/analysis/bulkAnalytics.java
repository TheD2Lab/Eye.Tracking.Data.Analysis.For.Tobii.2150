package analysis;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

public class bulkAnalytics {
	private static final String GZD_SUFFIX = "all_gaze.csv";
	private static final String FXD_SUFFIX = "fixations.csv";

	/*
	 * UI for bulk anaylsis
	 */
	public static JPanel bulkAnalyticsUI() throws IOException
	{
		JPanel panel = new JPanel();
		JLabel gazeLabel = new JLabel("Please select all participant files: ");
		JTextField gazeTextF = new JTextField("Location of files: ", 50);
		JButton gazeBrowseBtn = new JButton("Browse");
		gazeTextF.setBackground(Color.WHITE);
		gazeTextF.setEditable(false);
		gazeTextF.setPreferredSize(new Dimension(50, 30));
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;//set the x location of the grid for the next component
		c.gridy = 0;//set the y location of the grid for the next component
		panel.add(gazeLabel,c);
		
		c.gridx = 1;
		c.insets = new  Insets(10, 15, 15, 0);
		panel.add(gazeTextF,c);

		c.gridx = 2;//change the y location
		panel.add(gazeBrowseBtn,c);
		
		
		JFrame frame = new JFrame("");
		JFileChooser chooser = new JFileChooser();
		HashMap<String, String>partInfo = new HashMap<>();
		gazeBrowseBtn.addActionListener(e->{
			chooser.setFileFilter(new FileNameExtensionFilter("CSV", "csv"));
			chooser.setMultiSelectionEnabled(true);
			do{
				int returnValue = chooser.showOpenDialog(frame);
				if (returnValue != JFileChooser.APPROVE_OPTION) 
				{
					JOptionPane.showMessageDialog(null, "Must pick a file", "Error Message", JOptionPane.ERROR_MESSAGE);
					System.exit(0);
				}
				
				
			}while(!validation(chooser.getSelectedFiles(),partInfo));
		});
		
		panel.add(gazeLabel);
		panel.add(gazeBrowseBtn);
		return panel;
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