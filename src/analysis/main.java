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

import java.io.BufferedReader;

import java.io.FileReader;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import java.awt.*;

public class main 
{
	public static void main(String args[]) throws Exception 
	{
		JFrame mainFrame = new JFrame("");
		Dimension size = Toolkit.getDefaultToolkit().getScreenSize();//size of the computer monitor
	
		
		SingleAnalytics.setUp();
		//tabbed pages
		JTabbedPane tp=new JTabbedPane();  
		JPanel singleAnalyticsPg= SingleAnalytics.acquirePathsPage();
		JPanel p2=new JPanel();  
		JPanel helpPg =new JPanel(); 
		JPanel batchAnalyticsPg = BatchAnalytics.batchAnalyticsPage();
		JTextArea ta=new JTextArea(20, 10);
		
		int screenWidth = (int)size.getWidth();
		int screenHeight = (int)size.getHeight();
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setSize(screenWidth, screenHeight);

		ta.setFont(new Font("Verdana", Font.PLAIN, 15));
		ta.setAutoscrolls(false);
		ta.setMargin(new Insets(20,90,20,20) );
		BufferedReader in = new BufferedReader(new FileReader("src/analysis/helpPg.txt"));
		String line = in.readLine();
		while(line != null){
		  ta.append(line + "\n");
		  line = in.readLine();
		}
		helpPg.add(ta);

		
		tp.add("Data Analysis",singleAnalyticsPg);  
		tp.add("Machine Learning",p2);  
		tp.add("Batch Analysis",batchAnalyticsPg);  
		tp.add("Help", helpPg);

		mainFrame.add(tp);    
		mainFrame.setVisible(true);
		
		
	
	}





	

}
