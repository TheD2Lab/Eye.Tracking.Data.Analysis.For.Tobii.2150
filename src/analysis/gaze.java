package analysis;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;

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
		ArrayList<Object> allValidData = new ArrayList<>();

		FileWriter outputFileWriter = new FileWriter(new File (outputFile));
		CSVWriter outputCSVWriter = new CSVWriter(outputFileWriter);
		try {
			FileReader fileReader = new FileReader(inputFile);
			CSVReader csvReader = new CSVReader(fileReader);
			String[]nextLine = csvReader.readNext();
			//finds the index where the left and right diameter in millimeters is at
			int pupilLeftDiameterIndex = Arrays.asList(nextLine).indexOf("LPMM");
			int pupilRightDiameterIndex = Arrays.asList(nextLine).indexOf("RPMM");

			while((nextLine = csvReader.readNext()) != null) 
			{

				double pupilLeft = Double.parseDouble(nextLine[pupilLeftDiameterIndex]);
				double pupilRight = Double.parseDouble(nextLine[pupilRightDiameterIndex]);
				double[] pupilSizes = new double[2];
				pupilSizes[0] = pupilLeft;
				pupilSizes[1] = pupilRight;
				allValidData.add(pupilSizes);

			}


			String[]headers = {"total number of valid recordings", "average pupil size of left eye", "average pupil size of right eye", "average pupil size of both eyes"};
			String[]data = {String.valueOf(allValidData.size()),String.valueOf(getAverageOfLeft(allValidData)),String.valueOf(getAverageOfRight(allValidData)),String.valueOf(getAverageOfBoth(allValidData))};

			outputCSVWriter.writeNext(headers);
			outputCSVWriter.writeNext(data);
			outputCSVWriter.close();
			csvReader.close();
			systemLogger.writeToSystemLog(Level.INFO, gaze.class.getName(), "done writing gaze data to " + outputFile);

		}
		catch(FileNotFoundException ex) 
		{
			systemLogger.writeToSystemLog(Level.WARNING, gaze.class.getName(), "Error with outputFile " + outputFile + "\n" + ex.toString());
		}
		catch(IOException ex) 
		{
			systemLogger.writeToSystemLog(Level.WARNING, gaze.class.getName(), "Error with outputFile " + outputFile + "\n" + ex.toString());
		}
		catch(Error e)
		{
			systemLogger.writeToSystemLog(Level.SEVERE, gaze.class.getName(), "Error with outputFile " + outputFile + "\n" + e.toString());
		}
	}

	//calculate the average pupil size of the left eye
	public static double getAverageOfLeft(ArrayList<Object> allValidData){
		ArrayList<Double> allLeftSizes = new ArrayList<>();
		for (int i=0; i<allValidData.size(); i++){
			double[] eachPair = (double[]) allValidData.get(i);
			double pupilSizeLeft = eachPair[0];
			allLeftSizes.add(pupilSizeLeft);

		}
		return descriptiveStats.getMeanOfDoubles(allLeftSizes);
	}

	//calculate the average pupil size of the right eye
	public static double getAverageOfRight(ArrayList<Object> allValidData){
		ArrayList<Double> allRightSizes = new ArrayList<>();
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
