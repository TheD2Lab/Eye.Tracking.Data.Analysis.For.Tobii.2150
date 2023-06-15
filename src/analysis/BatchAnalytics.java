package analysis;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.ScrollPane;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

public class BatchAnalytics {
	private static final String GZD_SUFFIX = "all_gaze.csv";
	private static final String FXD_SUFFIX = "fixations.csv";

	/**
	 * UI for batch processing
	 * @return JPanel	return panel where the UI elements are contained
	 */
	public static JPanel batchAnalyticsPage() throws IOException
	{
		JPanel panel = new JPanel(new GridBagLayout());
		JPanel bulkPanel = new JPanel(new FlowLayout());
		JPanel outputPanel = new JPanel(new FlowLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		BufferedImage myPicture = ImageIO.read(new File("data/d2logo.jpg"));
		JLabel image = new JLabel(new ImageIcon(myPicture));
		JLabel partFilesLabel = new JLabel("Please select all participant files: ");
		JTextField partFilesTextF = new JTextField("Location of files: ", 50);
		JButton partFilesBrowseBtn = new JButton("Browse");
		JLabel outputLabel = new JLabel("Please select the output location: ");
		JTextField outputTextF = new JTextField("Output location: ", 50);
		JButton outputBrowseBtn = new JButton("Browse");
		JButton submitBtn = new JButton("Submit");
		JLabel title = new JLabel("D\u00B2 Lab Batch Analytics");
		
		JFileChooser chooser = new JFileChooser();
		HashMap<String, String>partInfo = new HashMap<>();
		
		title.setFont(new Font("Verdana", Font.PLAIN, 30));
		partFilesTextF.setBackground(Color.WHITE);
		partFilesTextF.setEditable(false);
		partFilesTextF.setPreferredSize(new Dimension(50, 30));
		outputTextF.setBackground(Color.WHITE);
		outputTextF.setEditable(false);
		outputTextF.setPreferredSize(new Dimension(50, 30));
		
		bulkPanel.add(partFilesLabel);
		bulkPanel.add(partFilesTextF);
		bulkPanel.add(partFilesBrowseBtn);
		outputPanel.add(outputLabel);
		outputPanel.add(outputTextF);
		outputPanel.add(outputBrowseBtn);
		
		c.insets = new  Insets(10, 15, 15, 0);
		c.gridx = 0;
		c.gridy = 0;
		panel.add(image,c);
		c.gridy = 1;
		panel.add(title, c);
		c.gridy = 2;
		panel.add(bulkPanel,c);
		c.gridy = 3;
		panel.add(outputPanel,c);
		c.gridy = 4;
		panel.add(submitBtn,c);
		
		partFilesBrowseBtn.addActionListener(e->{
			chooser.setFileFilter(new FileNameExtensionFilter("CSV", "csv"));
			chooser.setMultiSelectionEnabled(true);
			do
			{
				int returnValue = chooser.showOpenDialog(null);
				if (returnValue != JFileChooser.APPROVE_OPTION) 
				{
					JOptionPane.showMessageDialog(null, "Must pick a file", "Error Message", JOptionPane.ERROR_MESSAGE);
					systemLogger.writeToSystemLog(Level.INFO, BatchAnalytics.class.getName(), "No Files Selected");
				}
			}
			while(!validation(chooser.getSelectedFiles(),partInfo));
			partFilesTextF.setText(partInfo.toString());
		});
		
		outputBrowseBtn.addActionListener(e->{
			String temp = modifier.folderChooser("Please choose the location where you would like your files to reside in");
			if(!temp.equals(""))
			{	
				outputTextF.setText(temp);
			}
		});
		
		submitBtn.addActionListener(e->{
			//ensures that the fields are not empty before being sent out
			if(outputTextF.getText().equals("") || outputTextF.getText().equals("Output location: ") || outputTextF.getText() == null)
			{
				JOptionPane.showMessageDialog(null, "Please ensure that you have selected an output location", "Error Message", JOptionPane.ERROR_MESSAGE);
				systemLogger.writeToSystemLog(Level.INFO, BatchAnalytics.class.getName(), "Attempted to analyize files without output location");

			}
			else if(partInfo.isEmpty())
			{
				JOptionPane.showMessageDialog(null, "Please ensure that you have selected files", "Error Message", JOptionPane.ERROR_MESSAGE);
				systemLogger.writeToSystemLog(Level.INFO, BatchAnalytics.class.getName(), "Attempted to analyize files without selecting any files");
			}
			else
			{
				if(!modifier.createFolders(outputTextF.getText(), partInfo))
				{
					JOptionPane.showMessageDialog(null, "Error in creating files. Please see error log for more details", "Error Message", JOptionPane.ERROR_MESSAGE);
					systemLogger.writeToSystemLog(Level.SEVERE, BatchAnalytics.class.getName(), "Error in creating the required folders for batch Analytics");
					System.exit(0);
				}
				try 
				{
					runAnalysis(outputTextF.getText(),partInfo);
					JOptionPane.showMessageDialog(null, "Analysis is finish running", "Info", JOptionPane.INFORMATION_MESSAGE);

				} 
				catch (IOException e1) 
				{
					systemLogger.writeToSystemLog(Level.SEVERE, BatchAnalytics.class.getName(), "Error in running the analysis on the files \n"+ e1);
					System.exit(0);
				}
			}
		});

		return panel;
	}
	
	/**
	 * Ensures that each participant has a gaze and fixation file and that the naming convention is correct
	 * @param	files	an array of file paths
	 * @param	partInfo	a hashmap with the key being the participants name and value being the parent folder of the two files
	 * @return	boolean		true if the validation is successful, false otherwise
	 */
	private static boolean validation(File[]files, HashMap<String,String>partInfo)
	{
		String message = "";
		HashMap<String, Integer>partName = new HashMap<>(); //participant's name and number of files attached to the name
		for(File file: files)
		{
			String[]fName = file.getName().split("_",2); //splits the name and the suffix from the file name

			//checks if the naming convention was incorrect
			if(!fName[1].equals(GZD_SUFFIX) && !fName[1].equals(FXD_SUFFIX))
			{
				message += file.getName() + " Unable to use this file. Please check naming convention \n";
			}

			
			//counts the amount of time the name appears. Create a new key if it is not contained in both the hashmap
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
			systemLogger.writeToSystemLog(Level.INFO, BatchAnalytics.class.getName(), "Error in the validation fo the selected files ");
			return false;
		}
		
		return true;
		
	}

	/*
	 *	runs the analysis on all the files submitted
	 *	@param	outputLocation	the path where the generated files will reside
	 *	@param	partInfo		a hashmap with the key being the participants name and value being the parent folder of both the gaze and fixation file
	 */
	private static void runAnalysis(String outputLocation, HashMap<String,String>partInfo) throws IOException
	{
		for(String name: partInfo.keySet())
		{
			String currentPartFolderName = outputLocation + "/" + name;
			String gzdPath = partInfo.get(name) + "/" + name + "_" + GZD_SUFFIX;
			String fxdPath = partInfo.get(name) + "/" + name + "_" + FXD_SUFFIX;
			
			SingleAnalytics.setpName(name);
			SingleAnalytics.analyzeData(gzdPath, fxdPath, currentPartFolderName);
			
		}
	}
}
