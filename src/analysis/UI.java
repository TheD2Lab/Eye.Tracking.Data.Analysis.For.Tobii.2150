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
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

public class UI {

	private  int screenWidth;
	private  int screenHeight;
	private  JFrame mainFrame = new JFrame("");
	private  String gazepointGZDPath = "";
	private  String gazepointFXDPath = "";
	private  String outputPath = "";

	public  JFrame UISetUp()
	{
		Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
		screenWidth = (int)size.getWidth();
		screenHeight = (int)size.getHeight();
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setSize(screenWidth, screenHeight);
		return mainFrame;

	}

	public  void tabbedPage() throws IOException
	{

		JPanel dataAnalysisPanel=new JPanel();
		dataAnalysisPanel = dataAnalysisPage();
		JPanel p2=new JPanel();  
		JPanel p3=new JPanel();  
		JTabbedPane tp=new JTabbedPane();  
		tp.setBounds(50,50,200,200);  
		tp.add("Data Analysis Page",dataAnalysisPanel);  
		tp.add("Machine Learning",p2);  
		tp.add("Help",p3);    
		mainFrame.add(tp);    
		mainFrame.setVisible(true);  
	}

	public  JPanel dataAnalysisPage() throws IOException
	{	
		JPanel panel = new JPanel(new GridBagLayout());

		BufferedImage myPicture = ImageIO.read(new File("C:\\Users\\kayla\\Desktop\\Eye.Tracking.Data.Analysis.For.Tobii.2150\\data\\sharksLogo.png"));
		JLabel image = new JLabel(new ImageIcon(myPicture));

		JLabel title = new JLabel();
		title.setFont(new Font("Verdana", Font.PLAIN, 30));

		JLabel gazeLabel = new JLabel("Location of gaze file: ");
		JTextField gazeTextF = new JTextField("Location of gaze file: ", 50);
		JButton gazeBrowseBtn = new JButton("Browse");
		//gazeTextF.setBorder(BorderFactory.createCompoundBorder(gazeTextF.getBorder(), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		gazeTextF.setBackground(Color.WHITE);
		gazeTextF.setEditable(false);
		gazeTextF.setPreferredSize(new Dimension(50, 30));

		JLabel fixationLabel = new JLabel("Location of fixation file: ");
		JTextField fixationTextF = new JTextField("Location of fixation file: " , 50);
		//fixationTextF.setBorder(BorderFactory.createCompoundBorder(gazeTextF.getBorder(), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		fixationTextF.setBackground(Color.WHITE);
		JButton fixationBrowseBtn = new JButton("Browse");
		fixationTextF.setEditable(false);
		fixationTextF.setPreferredSize(new Dimension(50, 30));

		JLabel outputLabel = new JLabel("Location of output file: ");
		JTextField outputTextF = new JTextField("Location of output file: ", 50);
		//outputTextF.setBorder(BorderFactory.createCompoundBorder(gazeTextF.getBorder(), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
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

		c.gridy = 1;//change the y location
		c.insets = new  Insets(40, 15, 15, 0);
		panel.add(gazeTextF,c);
		c.gridx = 1;
		panel.add(gazeBrowseBtn,c);

		c.gridy = 2;//change the y location
		c.gridx = 0;
		c.insets = new  Insets(40, 15, 15, 0);
		panel.add(fixationTextF,c);
		c.gridx = 1;
		panel.add(fixationBrowseBtn,c);

		c.gridy = 3;//change the y location
		c.gridx = 0;
		c.insets = new  Insets(40, 15, 15, 0);
		panel.add(outputTextF,c);
		c.gridx = 1;
		panel.add(outputBrowseBtn,c);

		c.gridy = 4;//change the y location
		c.gridx = 0;
		c.insets = new  Insets(40, 15, 15, 0);
		panel.add(pPanel,c);

		c.gridy=5;
		JButton submitBtn = new JButton("Submit");
		panel.add(submitBtn, c);

		gazeBrowseBtn.addActionListener(e -> {
			String temp = fileChooser("Select the gaze .csv file you would like to use", "/data/");
			if(!temp.equals(""))
			{
				gazeTextF.setText(temp);
			}

		});
		fixationBrowseBtn.addActionListener(e -> { 
			String temp = fileChooser("Select the fixation .csv file you would like to use", "/data/");
			if(!temp.equals(""))
			{	
				fixationTextF.setText(temp);
			}

		});
		outputBrowseBtn.addActionListener(e -> {
			String temp = folderChooser("Choose a directory to save your file");
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
				outputPath = outputTextF.getText() + "/" + partTextF.getText();
				
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

				}
			}
			
			panel.removeAll();
			panel.repaint();
			System.out.println("Made it to the end");
			
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
		return outputPath;
	}

	/*
	 * UI for users to select the file they want to use
	 * 
	 * @param	dialogTitle		title of the window
	 * @param 	directory		directory to choose file from relative to project directory		
	 */
	private  String fileChooser(String dialogTitle, String directory)
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
		return "";
	}


	/*
	 * UI for users to select the folder they would want to use to place files in
	 * 
	 * @param	dialogTitle		title of the window
	 */
	private  String folderChooser(String dialogTitle)
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
		return "";
	}
}
