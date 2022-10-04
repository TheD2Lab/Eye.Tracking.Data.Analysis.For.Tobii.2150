package analysis;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;


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

public class gaze {
	
	public static void processGaze(String inputFile, String outputFile) throws IOException, CsvValidationException, NumberFormatException{
        ArrayList<Object> allValidData = new ArrayList<Object>();
        
        FileWriter outputFileWriter = new FileWriter(new File (outputFile));
        CSVWriter outputCSVWriter = new CSVWriter(outputFileWriter);
        try {
            FileReader fileReader = new FileReader(inputFile);
            CSVReader csvReader = new CSVReader(fileReader);
            String[]nextLine = csvReader.readNext();
            
            //finds the index where the left and right validity code is at
            //finds the index where the left and right diameter in millimeters is at
            int pupilLeftValidityIndex = -1; 
            int pupilRightValidityIndex = -1; 
            int pupilLeftDiameterIndex = -1; 
            int pupilRightDiameterIndex = -1; 
        	for(int i = 0; i < nextLine.length; i++)
        	{
        		String header = nextLine[i];
        		
        		switch(header)
        		{
        		case "LPMMV": 
        			pupilLeftValidityIndex = i;
        			break;
        		case "RPMMV": 
        			pupilRightValidityIndex = i; 
        			break;
        		case "LPMM": 
        			pupilLeftDiameterIndex = i; 
        			break;
        		case "RPMM": 
        			pupilRightDiameterIndex = i;
        			break;
        		default: 
        			break;
        		}
        	}
            
        	//starts to look at the data
            while((nextLine = csvReader.readNext()) != null) {	
            	
            	//checking the validity of the recording
                //a code with 0 indicates the eye tracker was confident with this data
                //note that only instances where BOTH pupil sizes are valid will be used in the analysis
                //if pupilLeft and pupilRight is missing coordinates than the entry is skipped
                if(nextLine[pupilLeftValidityIndex].equals("1") && nextLine[pupilRightValidityIndex].equals("1") && nextLine[pupilLeftDiameterIndex]!=null && nextLine[pupilRightDiameterIndex] != null){
                	double pupilLeft = Double.parseDouble(nextLine[pupilLeftDiameterIndex]);
                	double pupilRight = Double.parseDouble(nextLine[pupilRightDiameterIndex]);
                	double[] pupilSizes = new double[2];
                	pupilSizes[0] = pupilLeft;
                	pupilSizes[1] = pupilRight;
                	
                	//checks if pupil sizes are possible (between 2mm to 8mm)
                	if(pupilLeft >=2 && pupilLeft <=8 && pupilRight >=2 && pupilRight <=8)
                	{
                		
                		//checks if the difference in size between the left and right is 1mm or less
                		if(Math.abs(pupilRight - pupilLeft) <= 1)
                		{
                			allValidData.add(pupilSizes);
                		}
                	}
                	
                }
            }
            
            String[]headers = {"total number of valid recordings", "average pupil size of left eye", "average pupil size of right eye", "average pupil size of both eyes"};
            String[]data = {String.valueOf(allValidData.size()),String.valueOf(getAverageOfLeft(allValidData)),String.valueOf(getAverageOfRight(allValidData)),String.valueOf(getAverageOfBoth(allValidData))};

            outputCSVWriter.writeNext(headers);
            outputCSVWriter.writeNext(data);
            outputCSVWriter.close();
            csvReader.close();
            System.out.println("done writing gaze data to: " + outputFile);
		
		}catch(FileNotFoundException ex) {
	        System.out.println("Unable to open file '" + inputFile + "'");				
	    }catch(IOException ex) {
	        System.out.println("Error reading file '" + inputFile + "'");			
	    }
        catch(Error e)
        {
        	System.out.println("Error with csv file");
        }
	}
	
	//calculate the average pupil size of the left eye
	public static double getAverageOfLeft(ArrayList<Object> allValidData){
		ArrayList<Double> allLeftSizes = new ArrayList<Double>();
		for (int i=0; i<allValidData.size(); i++){
			double[] eachPair = (double[]) allValidData.get(i);
			double pupilSizeLeft = eachPair[0];
			allLeftSizes.add(pupilSizeLeft);
			
		}
		return descriptiveStats.getMeanOfDoubles(allLeftSizes);
	}

	//calculate the average pupil size of the right eye
	public static double getAverageOfRight(ArrayList<Object> allValidData){
		ArrayList<Double> allRightSizes = new ArrayList<Double>();
		for (int i=0; i<allValidData.size(); i++){
			double[] eachPair = (double[]) allValidData.get(i);
			double pupilSizeRight = eachPair[1];
			allRightSizes.add(pupilSizeRight);
			
		}
		return descriptiveStats.getMeanOfDoubles(allRightSizes);
	}
	
	//computes the average pupil size of both eyes
	public static double getAverageOfBoth(ArrayList<Object> allValidData){
		double averageOfLeft = getAverageOfLeft(allValidData);
		double averageOfRight = getAverageOfRight(allValidData);
		return (averageOfLeft + averageOfRight)/2.0;
	}
}
