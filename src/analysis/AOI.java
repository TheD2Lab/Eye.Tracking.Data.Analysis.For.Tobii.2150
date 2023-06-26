package analysis;

import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

public class AOI {
	
	public static void processAOIs(String inputFile, String outputFile, int SCREEN_WIDTH, int SCREEN_HEIGHT) throws IOException, CsvValidationException {		
		try {
			// Read input CSV file and initalize the column indexes for the data needed
			FileReader fileReader = new FileReader(inputFile);
            CSVReader csvReader = new CSVReader(fileReader);
            String[] nextLine = csvReader.readNext();
            
            // Locate the indexes for required fields
            int aoiIndex = -1, xIndex = -1, yIndex = -1, fixDurIndex = -1, fixIdIndex = -1, timeIndex = -1, peakVelocityIndex = -1, blinkRateIndex = -1;
            
            for (int i = 0; i < nextLine.length; i++) {
            	String header = nextLine[i];
            	
            	if (header.contains("TIME") && !header.contains("TICK")) {
            		timeIndex = i;
            		continue;
            	}
            	switch(header) {
            		case "AOI":
            			aoiIndex = i;
            			break;
            		case "FPOGX":
            			xIndex = i;
            			break;
            		case "FPOGY":
            			yIndex = i;
            			break;
            		case "FPOGD":
             			fixDurIndex = i;
             			break;
            		case "FPOGID":
            			fixIdIndex = i;
            			break;
            		case "SACCADE_PV":
            			peakVelocityIndex = i;
            			break;
            		case "BKPMIN":
            			blinkRateIndex = i;
            			break;
            		default:
            			break;
            	}
            }
            
            // Iterate through input file and group points by AOI
            HashMap<String, ArrayList<String[]>> map = new HashMap<String, ArrayList<String[]>>();
            while ((nextLine = csvReader.readNext()) != null) {
            	// If the data point is not part of an AOI, skip it
            	// Else if we've already encountered this AOI, add it to it's corresponding list
            	// Otherwise create a new list if it's a new AOI
            	String aoi = nextLine[aoiIndex];
            	if (aoi.equals(""))
            		continue;
            	else if (map.containsKey(aoi))
            		map.get(aoi).add(nextLine);
            	else {
            		String[] aois = aoi.split("-");
            		
            		for (int i = 0; i < aois.length; i++) {
            			if (!map.containsKey(aois[i]))
            				map.put(aois[i], new ArrayList<String[]>());
            			map.get(aois[i]).add(nextLine);
            		}
            	}
            }
            
            // Initializing output writers
            FileWriter outputFileWriter = new FileWriter(new File (outputFile));
            CSVWriter outputCSVWriter = new CSVWriter(outputFileWriter);
            
            // Initialize list of headers and write it to the output .csv file
            ArrayList<String> headers = new ArrayList<>();
            headers.add("AOI Name");
        	headers.add("Convex Hull Area");
        	
        	// Fixation columns
        	headers.add("Fixation Count");
        	headers.add("Total Duration");
        	headers.add("mean fixation duration (ms)");
        	headers.add("median fixation duration (ms)");
        	headers.add("StDev of fixation durations (ms)");
        	headers.add("Min. fixation duration (ms)");
        	headers.add(" Max. fixation duration (ms)");
        	
        	// Saccade columns
        	headers.add("total number of saccades");
        	headers.add("sum of all saccade length");
        	headers.add("mean saccade length");
        	headers.add("median saccade length");
        	headers.add("StDev of saccade lengths");
        	headers.add("min saccade length");
        	headers.add("max saccade length");
        	headers.add("sum of all saccade durations");
        	headers.add("mean saccade duration");
        	headers.add("median saccade duration");
        	headers.add("StDev of saccade durations");
        	headers.add("Min. saccade duration");
        	headers.add("Max. saccade duration");
        	headers.add("scanpath duration");
        	headers.add("fixation to saccade ratio");
        	
        	// Degree columns
        	headers.add("sum of all absolute degrees");
        	headers.add("mean absolute degree");
        	headers.add("median absolute degree");
        	headers.add("StDev of absolute degrees");
        	headers.add("min absolute degree");
        	headers.add("max absolute degree");
        	headers.add("sum of all relative degrees");
        	headers.add("mean relative degree");
        	headers.add("median relative degree");
        	headers.add("StDev of relative degrees");
        	headers.add("min relative degree");
        	headers.add("max relative degree");
        	
        	outputCSVWriter.writeNext(headers.toArray(new String[headers.size()]));
      
            // Iterate through each AOI and calculate their gaze analytics
            for (String aoi : map.keySet()) {
            	// Data row for output .csv file
            	ArrayList<String> data = new ArrayList<>();
            	data.add(aoi);
            	
            	ArrayList<String[]> aoiData = map.get(aoi);
            	ArrayList<Point2D.Double> allPoints = new ArrayList<Point2D.Double>();
            	HashMap<Integer, Integer> fixCount = new HashMap<Integer, Integer>();
            	ArrayList<Double> allFixationDurations = new ArrayList<Double>();
            	ArrayList<Object> allCoordinates = new ArrayList<Object>();
            	ArrayList<Object> saccadeDetails = new ArrayList<Object>();
            	
            	// Iterate through each AOI data to populate the above lists
            	for (int i = 0; i < aoiData.size(); i++) {
            		String[] entry = aoiData.get(i);
            		
            		// Initalize details about each fixation
            		double x = Double.valueOf(entry[xIndex]) * SCREEN_WIDTH;
            		double y = Double.valueOf(entry[yIndex]) * SCREEN_HEIGHT;
            		double id = Double.valueOf(entry[fixIdIndex]);
            		double duration = Double.valueOf(entry[fixDurIndex]);
            		double timestamp = Double.valueOf(timeIndex);
            		
            		
            		// Add each point to a list
            		Point2D.Double point = new Point2D.Double(x, y);
            		allPoints.add(point);
            		
            		// Add each coordinate to a list
            		Double[] coordinate = new Double[3];
            		coordinate[0] = x;
            		coordinate[1] = y;
            		coordinate[2] = id;
            		allCoordinates.add(coordinate);
            		
            		// Add each saccade detail into a list
            		Double[] saccadeDetail = new Double[3];
            		saccadeDetail[0] = timestamp;
            		saccadeDetail[1] = duration;
            		saccadeDetail[2] = id;
            		saccadeDetails.add(saccadeDetail);
            		
            		// Count the number of unique fixations in each AOI
            		if (!fixCount.containsKey(Integer.valueOf(entry[fixIdIndex])))
            			fixCount.put(Integer.valueOf(entry[fixIdIndex]), 1);
            		
            		// Add duration value to list
            		allFixationDurations.add(duration);
            		
            	}
            	Double[] allSaccadeLengths = saccade.getAllSaccadeLength(allCoordinates);
            	ArrayList<Double> allSaccadeDurations = saccade.getAllSaccadeDurations(saccadeDetails);
            	ArrayList<Double> allAbsoluteDegrees = angle.getAllAbsoluteAngles(allCoordinates);
            	ArrayList<Double> allRelativeDegrees = angle.getAllRelativeAngles(allCoordinates);
            	
            	// Calculate the convex hull and its area 
            	List<Point2D.Double> boundingPoints = convexHull.getConvexHull(allPoints);
            	Point2D[] points = fixation.listToArray(boundingPoints);
            	data.add(String.valueOf(convexHull.getPolygonArea(points)));
            	
            	data.add(String.valueOf(fixCount.keySet().size()));
            	data.add(String.valueOf(descriptiveStats.getSumOfDoubles(allFixationDurations)));
                data.add(String.valueOf(descriptiveStats.getMeanOfDoubles(allFixationDurations)));
                data.add(String.valueOf(descriptiveStats.getMedianOfDoubles(allFixationDurations)));
                data.add(String.valueOf(descriptiveStats.getStDevOfDoubles(allFixationDurations)));
                data.add(String.valueOf(descriptiveStats.getMinOfDoubles(allFixationDurations)));
                data.add(String.valueOf(descriptiveStats.getMaxOfDoubles(allFixationDurations)));
                
                data.add(String.valueOf(allSaccadeLengths.length));
                data.add(String.valueOf(descriptiveStats.getSum(allSaccadeLengths)));
                data.add(String.valueOf(descriptiveStats.getMean(allSaccadeLengths)));
                data.add(String.valueOf(descriptiveStats.getMedian(allSaccadeLengths)));
                data.add(String.valueOf(descriptiveStats.getStDev(allSaccadeLengths)));
                data.add(String.valueOf(descriptiveStats.getMin(allSaccadeLengths)));
                data.add(String.valueOf(descriptiveStats.getMax(allSaccadeLengths)));
                data.add(String.valueOf(descriptiveStats.getSumOfDoubles(allSaccadeDurations)));
                data.add(String.valueOf(descriptiveStats.getMeanOfDoubles(allSaccadeDurations)));
                data.add(String.valueOf(descriptiveStats.getMedianOfDoubles(allSaccadeDurations)));
                data.add(String.valueOf(descriptiveStats.getStDevOfDoubles(allSaccadeDurations)));
                data.add(String.valueOf(descriptiveStats.getMinOfDoubles(allSaccadeDurations)));
                data.add(String.valueOf(descriptiveStats.getMaxOfDoubles(allSaccadeDurations)));
                data.add(String.valueOf(fixation.getScanpathDuration(allFixationDurations, allSaccadeDurations)));
                data.add(String.valueOf(fixation.getFixationToSaccadeRatio(allFixationDurations, allSaccadeDurations)));
                
                data.add(String.valueOf(descriptiveStats.getSumOfDoubles(allAbsoluteDegrees)));
                data.add(String.valueOf(descriptiveStats.getMeanOfDoubles(allAbsoluteDegrees)));
                data.add(String.valueOf(descriptiveStats.getMedianOfDoubles(allAbsoluteDegrees)));
                data.add(String.valueOf(descriptiveStats.getStDevOfDoubles(allAbsoluteDegrees)));
                data.add(String.valueOf(descriptiveStats.getMinOfDoubles(allAbsoluteDegrees)));

                data.add(String.valueOf(descriptiveStats.getSumOfDoubles(allRelativeDegrees)));
                data.add(String.valueOf(descriptiveStats.getMeanOfDoubles(allRelativeDegrees)));
                data.add(String.valueOf(descriptiveStats.getMedianOfDoubles(allRelativeDegrees)));
                data.add(String.valueOf(descriptiveStats.getStDevOfDoubles(allRelativeDegrees)));
                data.add(String.valueOf(descriptiveStats.getMinOfDoubles(allRelativeDegrees)));
                data.add(String.valueOf(descriptiveStats.getMaxOfDoubles(allRelativeDegrees)));
                
                data.add(String.valueOf(getAvgPeakSaccadeVelocity(map.get(aoi), peakVelocityIndex)));
            	
                // Write the data into the .csv file as a new row
                outputCSVWriter.writeNext(data.toArray(new String[data.size()]));
            }
            
            outputCSVWriter.close();
            csvReader.close();
            System.out.println("done writing AOI data to" + outputFile);
            systemLogger.writeToSystemLog(Level.INFO, WindowOperations.class.getName(), "done writing AOI data to " + outputFile );
		}
		catch(FileNotFoundException e) {
			 systemLogger.writeToSystemLog(Level.WARNING, WindowOperations.class.getName(), "Unable to open file " + inputFile + "\n" + e.toString());
			System.out.println("Unable to open file '" + inputFile + "'");
	    }
		catch(IOException e) {
			 systemLogger.writeToSystemLog(Level.WARNING, WindowOperations.class.getName(), "Error reading file " + inputFile + "\n" + e.toString());

	        System.out.println("Error reading file '" + inputFile + "'");
	    }
	}
	
	public static double getAvgPeakSaccadeVelocity(ArrayList<String[]> data, int peakVelocityIndex) {
		double total = 0;
		
		for (String[] entry : data) {
			total += Double.parseDouble(entry[peakVelocityIndex]);
		}
		
		return total / data.size();
	}
}