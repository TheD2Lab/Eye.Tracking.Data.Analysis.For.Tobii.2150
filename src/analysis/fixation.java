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
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.opencsv.CSVWriter;


public class fixation {
	
	public static void processFixation(String inputFile, String outputFile) throws IOException{

        String line = null;
        ArrayList<Integer> allFixationDurations = new ArrayList<Integer>();
        ArrayList<Object> allCoordinates = new ArrayList<Object>();
        List<Point> allPoints = new ArrayList<Point>();
        ArrayList<Object> saccadeDetails = new ArrayList<Object>();
        
        FileWriter outputFileWriter = new FileWriter(new File (outputFile));
        CSVWriter outputCSVWriter = new CSVWriter(outputFileWriter);

        try {
            FileReader fileReader = new FileReader(inputFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while((line = bufferedReader.readLine()) != null) {
                
                String[] lineArray = lineToArray(line);
                
                //get each fixation's duration
                String eachFixationDuration = lineArray[2];
                int eachDuration = Integer.parseInt(eachFixationDuration);
                
                //get each fixation's (x,y) coordinates
                String eachFixationX = lineArray[3];
                String eachFixationY = lineArray[4];
                int x = Integer.parseInt(eachFixationX);
                int y = Integer.parseInt(eachFixationY);
                
                Point eachPoint = new Point(x,y);
                
                Integer[] eachCoordinate = new Integer[2];
                eachCoordinate[0] = x;
                eachCoordinate[1] = y;
                
                //get timestamp of each fixation
                int timestamp = Integer.parseInt(lineArray[1]);
                Integer[] eachSaccadeDetail = new Integer[2];
                eachSaccadeDetail[0] = timestamp;
                eachSaccadeDetail[1] = eachDuration;
                
                
                allFixationDurations.add(eachDuration);
                
                allCoordinates.add(eachCoordinate);
                
                allPoints.add(eachPoint);
                
                saccadeDetails.add(eachSaccadeDetail);
               
            }	
            
            ArrayList<String>headers = new ArrayList<String>();
            ArrayList<String>data = new ArrayList<String>();
            
            headers.add("total number of fixations");
            data.add(String.valueOf(getFixationCount(inputFile)));
            
            headers.add("sum of all fixation duration");
            data.add(String.valueOf(descriptiveStats.getSumOfIntegers(allFixationDurations)));
            
            headers.add("mean duration");
            data.add(String.valueOf(descriptiveStats.getMeanOfIntegers(allFixationDurations)));
            
            headers.add("median duration");
            data.add(String.valueOf(descriptiveStats.getMedianOfIntegers(allFixationDurations)));
            
            headers.add("StDev of durations");
            data.add(String.valueOf(descriptiveStats.getStDevOfIntegers(allFixationDurations)));

            headers.add("Min. duration");
            data.add(String.valueOf(descriptiveStats.getMinOfIntegers(allFixationDurations)));
            
            headers.add("Max. duration");
            data.add(String.valueOf(descriptiveStats.getMaxOfIntegers(allFixationDurations)));
            
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

            ArrayList<Integer> allSaccadeDurations = saccade.getAllSaccadeDurations(saccadeDetails);
            
            headers.add("sum of all saccade durations"); 
            data.add(String.valueOf(descriptiveStats.getSumOfIntegers(allSaccadeDurations)));
            
            headers.add("mean saccade duration"); 
            data.add(String.valueOf(descriptiveStats.getMeanOfIntegers(allSaccadeDurations)));
            
            headers.add("median saccade duration"); 
            data.add(String.valueOf(descriptiveStats.getMedianOfIntegers(allSaccadeDurations)));
 
            
            headers.add("StDev of saccade durations"); 
            data.add(String.valueOf(descriptiveStats.getStDevOfIntegers(allSaccadeDurations)));
            
            headers.add("Min. saccade duration"); 
            data.add(String.valueOf(descriptiveStats.getMinOfIntegers(allSaccadeDurations)));
            
            headers.add("Max. saccade duration"); 
            data.add(String.valueOf(descriptiveStats.getMaxOfIntegers(allSaccadeDurations)));
            
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
            List<Point> boundingPoints = convexHull.getConvexHull(allPoints);
            Point2D[] points = listToArray(boundingPoints);
            
            headers.add("convex hull area");
            data.add(String.valueOf(convexHull.getPolygonArea(points)));

            outputCSVWriter.writeNext(headers.toArray(new String[headers.size()]));
            outputCSVWriter.writeNext(data.toArray(new String[data.size()]));
            outputCSVWriter.close();
            bufferedReader.close();	
            System.out.println("done writing fixation data to" + outputFile);
            
        }catch(FileNotFoundException ex) {
            System.out.println("Unable to open file '" + inputFile + "'");				
        }catch(IOException ex) {
            System.out.println("Error reading file '" + inputFile + "'");			
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
	
	public static Point2D[] listToArray(List<Point> allPoints){
		Point2D[] points = new Point2D[allPoints.size()];
        for(int i=0; i<points.length; i++){
        	points[i] = allPoints.get(i);
        }
        return points;
	}
	
	public static String getFixationCount(String inputFile) throws IOException {
		File file = new File(inputFile); 
	  
	        RandomAccessFile fileHandler = new RandomAccessFile( file, "r" );
	        long fileLength = file.length() - 1;
	        StringBuilder sb = new StringBuilder();

	        for(long filePointer = fileLength; filePointer != -1; filePointer--){
	            fileHandler.seek( filePointer );
	            int readByte = fileHandler.readByte();

	            if( readByte == 0xA ) {
	                if( filePointer == fileLength ) {
	                    continue;
	                } else {
	                    break;
	                }
	            } else if( readByte == 0xD ) {
	                if( filePointer == fileLength - 1 ) {
	                    continue;
	                } else {
	                    break;
	                }
	            }

	            sb.append( ( char ) readByte );
	        }

	        String lastLine = sb.reverse().toString();
	        
	        //get the first value in the last line, which is the total number of fixations
	        String[] lineArray = lineToArray(lastLine);
            String totalFixations = lineArray[0];
	        
	        return totalFixations;
	 
	}
	

	public static double getScanpathDuration(ArrayList<Integer> allFixationDurations, ArrayList<Integer> allSaccadeDurations){
		double fixationDuration = descriptiveStats.getSumOfIntegers(allFixationDurations);
		double saccadeDuration = descriptiveStats.getSumOfIntegers(allSaccadeDurations);
		return fixationDuration + saccadeDuration;
	}
	
	public static double getFixationToSaccadeRatio(ArrayList<Integer> allFixationDurations, ArrayList<Integer> allSaccadeDurations){
		double fixationDuration = descriptiveStats.getSumOfIntegers(allFixationDurations);
		double saccadeDuration = descriptiveStats.getSumOfIntegers(allSaccadeDurations);
		return fixationDuration/saccadeDuration;
	}
	
}
