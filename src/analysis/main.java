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

import java.awt.geom.Point2D;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.WindowConstants;
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
	public static void main(String args[]) throws Exception 
	{
		
		// Resolution of monitor 
		
				
//		String[] paths = {"C:\\Users\\kayla\\Downloads\\Hannah Park_all_gaze.csv", "C:\\Users\\kayla\\Downloads\\Hannah Park_fixations.csv","C:\\Users\\kayla\\OneDrive\\Documents\\TestCode\\TesterHannah/"};
//
//		String[] modifiedData = processData(new String[] {paths[0], paths[1]}, paths[2], SCREEN_WIDTH, SCREEN_HEIGHT);
		
		JFrame mainFrame = new JFrame("");
		Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
		int screenWidth = (int)size.getWidth();
		int screenHeight = (int)size.getHeight();
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setSize(screenWidth, screenHeight);
		
		//tabbed pages
		Panels pages = new Panels();
		JPanel acquirePathsPanel=new JPanel();
		acquirePathsPanel = pages.acquirePathsPage();
		JPanel p2=new JPanel();  
		JPanel helpPgPanel =new JPanel();  
		JTabbedPane tp=new JTabbedPane();  
		
		JTextArea ta=new JTextArea(200,150);
		ta.setFont(new Font("Verdana", Font.PLAIN, 15));
		ta.setAutoscrolls(false);
		ta.setMargin(new Insets(20,900,20,20) );
		BufferedReader in = new BufferedReader(new FileReader("src/analysis/helpPg.txt"));
		String line = in.readLine();
		while(line != null){
		  ta.append(line + "\n");
		  line = in.readLine();
		}
		helpPgPanel.add(ta);

		
		tp.setBounds(50,50,200,200);  
		tp.add("Data Analysis Page",acquirePathsPanel);  
		tp.add("Machine Learning",p2);  
		tp.add("Help",helpPgPanel);    

		mainFrame.add(tp);    
		mainFrame.setVisible(true);
		
		
		
		
		//waits for the UI to finish
		//while(pages.getGZDPath().equals("")) {Thread.sleep(2000);};
		
		
		
		tp.setComponentAt(0, pages.gazeAnalyticsOptions());
		tp.repaint();
		
	
	}





	

}
