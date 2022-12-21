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

import java.awt.Point;

import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;


public class fixation {

	public static void processFixation(String inputFile, String outputFile, int SCREEN_WIDTH, int SCREEN_HEIGHT) throws IOException, CsvValidationException{

        String line = null;
        ArrayList<Double> allFixationDurations = new ArrayList<>();
        ArrayList<Object> allCoordinates = new ArrayList<>();
        List<Point2D.Double> allPoints = new ArrayList<>();
        ArrayList<Object> saccadeDetails = new ArrayList<>();

        FileWriter outputFileWriter = new FileWriter(new File (outputFile));
        CSVWriter outputCSVWriter = new CSVWriter(outputFileWriter);
        try {
        	 FileReader fileReader = new FileReader(inputFile);
             CSVReader csvReader = new CSVReader(fileReader);
             String[]nextLine = csvReader.readNext();
             int fixationValidityIndex = -1;
             int fixationDurationIndex = -1;
             int fixationXIndex = -1;
             int fixationYIndex = -1;
             int timestampIndex = -1;
         	for(int i = 0; i < nextLine.length; i++)
         	{
         		String header = nextLine[i];

         		switch(header)
         		{
         		case "FPOGV":
         			fixationValidityIndex = i;
         			break;
         		case "FPOGD":
         			fixationDurationIndex = i;
         			break;
         		case "FPOGX":
         			fixationXIndex = i;
         			break;
         		case "FPOGY":
         			fixationYIndex = i;
         			break;
         		default:
         			break;
         		}
         		if(nextLine[i].contains("TIME"))
         		{
         			timestampIndex = i;
         		}
         	}



            while((nextLine = csvReader.readNext()) != null) {

            	if(!nextLine[fixationValidityIndex].equals("1"))
            	{
            		continue;
            	}
                //get each fixation's duration
                String fixationDurationSeconds = nextLine[fixationDurationIndex];
                double eachDuration = Double.valueOf(fixationDurationSeconds) * 1000;


                String [] lineArray = new String[10];

                //get each fixation's (x,y) coordinates
                String eachFixationX = nextLine[fixationXIndex];
                String eachFixationY = nextLine[fixationYIndex];
                double x = Double.valueOf(eachFixationX) * SCREEN_WIDTH;
                double y = Double.valueOf(eachFixationY) * SCREEN_HEIGHT;

                Point2D.Double eachPoint = new Point2D.Double(x,y);

                Double[] eachCoordinate = new Double[2];
                eachCoordinate[0] = x;
                eachCoordinate[1] = y;

                //get timestamp of each fixation
                double timestamp = Double.valueOf(nextLine[timestampIndex])* 1000;
                Double[] eachSaccadeDetail = new Double[2];
                eachSaccadeDetail[0] = timestamp;
                eachSaccadeDetail[1] = eachDuration;


                allFixationDurations.add(eachDuration);

                allCoordinates.add(eachCoordinate);

                allPoints.add(eachPoint);

                saccadeDetails.add(eachSaccadeDetail);

            }

            ArrayList<String>headers = new ArrayList<>();
            ArrayList<String>data = new ArrayList<>();

            headers.add("total number of fixations");
            data.add(String.valueOf(getFixationCount(inputFile)));

            headers.add("sum of all fixation duration");
            data.add(String.valueOf(descriptiveStats.getSumOfDoubles(allFixationDurations)));

            headers.add("mean duration");
            data.add(String.valueOf(descriptiveStats.getMeanOfDoubles(allFixationDurations)));

            headers.add("median duration");
            data.add(String.valueOf(descriptiveStats.getMedianOfDoubles(allFixationDurations)));

            headers.add("StDev of durations");
            data.add(String.valueOf(descriptiveStats.getStDevOfDoubles(allFixationDurations)));

            headers.add("Min. duration");
            data.add(String.valueOf(descriptiveStats.getMinOfDoubles(allFixationDurations)));

            headers.add("Max. duration");
            data.add(String.valueOf(descriptiveStats.getMaxOfDoubles(allFixationDurations)));

            Double[] allSaccadeLengths = saccade.getAllSaccadeLength(allCoordinates);

            headers.add("total number of saccades");
            data.add(String.valueOf(allSaccadeLengths.length));

            headers.add("sum of all saccade length");
            data.add(String.valueOf(descriptiveStats.getSum(allSaccadeLengths)));

            headers.add("mean saccade length");
            data.add(String.valueOf(descriptiveStats.getMean(allSaccadeLengths)));


            headers.add("median saccade length");
            data.add(String.valueOf(descriptiveStats.getMedian(allSaccadeLengths)));

            headers.add("StDev of saccade lengths");
            data.add(String.valueOf(descriptiveStats.getStDev(allSaccadeLengths)));

            headers.add("min saccade length");
            data.add(String.valueOf(descriptiveStats.getMin(allSaccadeLengths)));


            headers.add("max saccade length");
            data.add(String.valueOf(descriptiveStats.getMax(allSaccadeLengths)));

            ArrayList<Double> allSaccadeDurations = saccade.getAllSaccadeDurations(saccadeDetails);

            headers.add("sum of all saccade durations");
            data.add(String.valueOf(descriptiveStats.getSumOfDoubles(allSaccadeDurations)));

            headers.add("mean saccade duration");
            data.add(String.valueOf(descriptiveStats.getMeanOfDoubles(allSaccadeDurations)));

            headers.add("median saccade duration");
            data.add(String.valueOf(descriptiveStats.getMedianOfDoubles(allSaccadeDurations)));


            headers.add("StDev of saccade durations");
            data.add(String.valueOf(descriptiveStats.getStDevOfDoubles(allSaccadeDurations)));

            headers.add("Min. saccade duration");
            data.add(String.valueOf(descriptiveStats.getMinOfDoubles(allSaccadeDurations)));

            headers.add("Max. saccade duration");
            data.add(String.valueOf(descriptiveStats.getMaxOfDoubles(allSaccadeDurations)));

            headers.add("scanpath duration");
            data.add(String.valueOf(getScanpathDuration(allFixationDurations, allSaccadeDurations)));


            headers.add("fixation to saccade ratio");
            data.add(String.valueOf(getFixationToSaccadeRatio(allFixationDurations, allSaccadeDurations)));

            ArrayList<Double> allAbsoluteDegrees = angle.getAllAbsoluteAngles(allCoordinates);

            headers.add("sum of all absolute degrees");
            data.add(String.valueOf(descriptiveStats.getSumOfDoubles(allAbsoluteDegrees)));

            headers.add("mean absolute degree");
            data.add(String.valueOf(descriptiveStats.getMeanOfDoubles(allAbsoluteDegrees)));

            headers.add("median absolute degree");
            data.add(String.valueOf(descriptiveStats.getMedianOfDoubles(allAbsoluteDegrees)));

            headers.add("StDev of absolute degrees");
            data.add(String.valueOf(descriptiveStats.getStDevOfDoubles(allAbsoluteDegrees)));


            headers.add("min absolute degree");
            data.add(String.valueOf(descriptiveStats.getMinOfDoubles(allAbsoluteDegrees)));


            headers.add("max absolute degree");
            data.add(String.valueOf(descriptiveStats.getMaxOfDoubles(allAbsoluteDegrees)));


            ArrayList<Double> allRelativeDegrees = angle.getAllRelativeAngles(allCoordinates);

            headers.add("sum of all relative degrees");
            data.add(String.valueOf(descriptiveStats.getSumOfDoubles(allRelativeDegrees)));

            headers.add("mean relative degree");
            data.add(String.valueOf(descriptiveStats.getMeanOfDoubles(allRelativeDegrees)));

            headers.add("median relative degree");
            data.add(String.valueOf(descriptiveStats.getMedianOfDoubles(allRelativeDegrees)));

            headers.add("StDev of relative degrees");
            data.add(String.valueOf(descriptiveStats.getStDevOfDoubles(allRelativeDegrees)));

            headers.add("min relative degree");
            data.add(String.valueOf(descriptiveStats.getMinOfDoubles(allRelativeDegrees)));


            headers.add("max relative degree");
            data.add(String.valueOf(descriptiveStats.getMaxOfDoubles(allRelativeDegrees)));

            //getting the convex hull using Graham Scan
            //i.e. Choose point p with smallest y-coordinate.
            //Sort points by polar angle with p to get simple polygon.
            //Consider points in order, and discard those that would create a clockwise turn.
            List<Point2D.Double> boundingPoints = convexHull.getConvexHull(allPoints);
            Point2D[] points = listToArray(boundingPoints);

            headers.add("convex hull area");
            data.add(String.valueOf(convexHull.getPolygonArea(points)));
            
            headers.add("Average Saccade Velocity");
            data.add(avgSaccadeVelocity(inputFile, outputFile));
            
            headers.add("Average Blink Rate per Minute");
            data.add(blinkRate(inputFile));

            outputCSVWriter.writeNext(headers.toArray(new String[headers.size()]));
            outputCSVWriter.writeNext(data.toArray(new String[data.size()]));
            outputCSVWriter.close();
            csvReader.close();
            System.out.println();
            systemLogger.writeToSystemLog(Level.INFO, fixation.class.getName(), "done writing fixation data to " + outputFile);


        }
        catch(FileNotFoundException ex) 
        {
            systemLogger.writeToSystemLog(Level.WARNING, fixation.class.getName(), "Error with outputFile " + outputFile + "\n" + ex.toString());
        }
        catch(IOException ex) 
        {
        	systemLogger.writeToSystemLog(Level.WARNING, fixation.class.getName(), "Error with outputFile " + outputFile + "\n" + ex.toString());
        }
	}

	public static String[] lineToArray(String lineOfData){

		StringTokenizer str = new StringTokenizer(lineOfData);
		String[] values = new String[str.countTokens()];

		while(str.hasMoreElements()) {
			for(int i=0; i<values.length; i++){
				values[i] = (String) str.nextElement();
			}
		}

		return values;
	}

	public static Point2D.Double[] listToArray(List<Point2D.Double> allPoints){
		Point2D.Double[] points = new Point2D.Double[allPoints.size()];
        for(int i=0; i<points.length; i++){
        	points[i] = allPoints.get(i);
        }
        return points;
	}

	public static String getFixationCount(String inputFile) throws IOException {
		File file = new File(inputFile);
		FileReader fileReader = new FileReader(file);
		CSVReader csvReader = new CSVReader(fileReader);
		Iterator<String[]> iter = csvReader.iterator();
		String[] line = new String[0];
		
		// Iterate through each entry until you reach the last
		while (iter.hasNext())
			line = iter.next();
		
		csvReader.close();
		
		// Return the 9th column of the last row, which is the fixation ID
		return line[9] + "";
	}
	
	public static String avgSaccadeVelocity(String inputFile, String outputFile) throws IOException 
	{
		File file = new File(inputFile);
		FileReader fileReader = new FileReader(file);
		CSVReader csvReader = new CSVReader(fileReader);
		FileWriter fileWriter = new FileWriter(new File (outputFile.replace("graphFXDResults.csv", "saccadeVelocity.csv")));
        CSVWriter csvWriter = new CSVWriter(fileWriter);
		Iterator<String[]> iter = csvReader.iterator();
		String[] rowA = new String[0];
		String[] rowB = new String[0];
		double totalSaccadeVelocity = 0;
		
		csvWriter.writeNext(new String[] {"saccade velocity"});
		csvWriter.writeNext(new String[] {"" + 0});
		
		//skips header row
		iter.next();
		rowA = iter.next();
		
		while (iter.hasNext())
		{
			rowB = iter.next();
			
			//column 57 is SACCADE_DIR and column 4 is the timestamp
			double saccadeVelocity = Double.valueOf(rowB[57])/Math.abs(Double.valueOf(rowB[3]) - Double.valueOf(rowA[3]));
			csvWriter.writeNext(new String[] {"" + saccadeVelocity});
			totalSaccadeVelocity += saccadeVelocity;
			rowA = rowB; 
		}
		
		csvWriter.close();
		csvReader.close();
		return (totalSaccadeVelocity/Double.valueOf(rowB[9])) + "";
	}
	
	public static String blinkRate(String inputFile) throws IOException
	{
		File file = new File(inputFile);
		FileReader fileReader = new FileReader(file);
		CSVReader csvReader = new CSVReader(fileReader);
		Iterator<String[]> iter = csvReader.iterator();
		String[] row = new String[0];
		int minutes = 1; 
		int totalBlinks = 0;
		
		//skips header row
		iter.next();
			
		while (iter.hasNext())
		{
			row = iter.next();
			
			if(Double.valueOf(row[3]) > minutes)
			{
				//column 31 is BKPMIN which is number of blinks per minute
				totalBlinks += Integer.valueOf(row[30]);
				minutes++;
			}
		}
		
		csvReader.close();
		//returns avg blinks per minute
		return (totalBlinks/minutes) + "";
	}

	public static double getScanpathDuration(ArrayList<Double> allFixationDurations, ArrayList<Double> allSaccadeDurations) {
		double fixationDuration = descriptiveStats.getSumOfDoubles(allFixationDurations);
		double saccadeDuration = descriptiveStats.getSumOfDoubles(allSaccadeDurations);
		return fixationDuration + saccadeDuration;
	}

	public static double getFixationToSaccadeRatio(ArrayList<Double> allFixationDurations, ArrayList<Double> allSaccadeDurations){
		double fixationDuration = descriptiveStats.getSumOfDoubles(allFixationDurations);
		double saccadeDuration = descriptiveStats.getSumOfDoubles(allSaccadeDurations);
		return fixationDuration/saccadeDuration;
	}

}
