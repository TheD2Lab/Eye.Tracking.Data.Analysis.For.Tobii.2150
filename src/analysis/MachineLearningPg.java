package analysis;

public class MachineLearningPg {
	
	public JPanel machineLearnPage() throws IOException
	{	
		JLabel title = new JLabel("D\u00B2 Lab Eye Tracking Machine Learning Tool");
		title.setFont(new Font("Verdana", Font.PLAIN, 30));

		JTextField csvTextF = new JTextField("Location of CSV file: ", 50);
		JButton csvBrowseBtn = new JButton("Browse");
		csvTextF.setBackground(Color.WHITE);
		csvTextF.setEditable(false);
		csvTextF.setPreferredSize(new Dimension(50, 30));

		JTextField outputTextF = new JTextField("Location of output file: ", 50);
		outputTextF.setBackground(Color.WHITE);
		JButton outputBrowseBtn = new JButton("Browse");
		outputTextF.setEditable(false);
		outputTextF.setPreferredSize(new Dimension(50, 30));


		GridBagConstraints c = new GridBagConstraints();

		c.gridx = 0;//set the x location of the grid for the next component
		c.gridy = 0;//set the y location of the grid for the next component
		panel.add(image,c);
		
		c.gridy = 1;
		c.insets = new  Insets(10, 15, 15, 0);
		panel.add(title,c);

		c.gridy = 2;//change the y location
		c.gridx = 0;
		panel.add(outputTextF,c);
		c.gridx = 1;
		panel.add(outputBrowseBtn,c);

		c.gridy = 3;//change the y location
		c.gridx = 0;

		c.gridy=6;
		JButton submitBtn = new JButton("Submit");
		panel.add(submitBtn, c);

		csvBrowseBtn.addActionListener(e -> {
			String temp = ManipFilePaths.fileChooser("Select the gaze .csv file you would like to use", "/data/");
			if(!temp.equals(""))
			{
				csvTextF.setText(temp);
			}

		});
		outputBrowseBtn.addActionListener(e -> {
			String temp = ManipFilePaths.folderChooser("Choose a directory to save your file");
			if(!temp.equals(""))
			{	
				outputTextF.setText(temp);
			}

		});

		submitBtn.addActionListener(e-> {
			if(csvTextF.getText().equals("") || csvTextF.getText()==null || csvTextF.getText().equals("Location of gaze file: "))
			{
				JOptionPane.showMessageDialog(null, "Must select a gaze file", "Error Message", JOptionPane.ERROR_MESSAGE);
			} 
			else if(outputTextF.getText().equals("") || outputTextF.getText() == null || outputTextF.getText().equals("Location of output file: "))
			{
				JOptionPane.showMessageDialog(null, "Must select an outputFolder", "Error Message", JOptionPane.ERROR_MESSAGE);
			}
			else
			{
				outputPath = outputTextF.getText();
				
				File participantFolder = new File(outputTextF.getText());

				//checks if it exists
				if(!participantFolder.exists())
				{
						JOptionPane.showMessageDialog(null, "Folder does not exist. Please select a valid folder", "Error Message", JOptionPane.ERROR_MESSAGE);
						System.exit(0);

				}
				else
				{
					WekaExperiment weka = new WekaExperiment();
					try {
						weka.setupExperiment(true, outputPath);
						System.out.println("in");
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
			
			panel.removeAll();
			panel.repaint();			
		});
		

		return panel;

	}

}
