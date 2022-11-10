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
            int aoiIndex = -1, xIndex = -1, yIndex = -1, fixDurIndex = -1, fixId = -1;
            
            for (int i = 0; i < nextLine.length; i++) {
            	String header = nextLine[i];
            	
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
            			fixId = i;
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
        	headers.add("Fixation Count");
        	headers.add("Total Duration");
        	headers.add("Mean Duration");
        	headers.add("Median Duration");
        	headers.add("StDev of Durations");
        	headers.add("Min Duration");
        	headers.add("Max Duration");
        	outputCSVWriter.writeNext(headers.toArray(new String[headers.size()]));
        	
            
            // Iterate through each AOI and calculate their gaze analytics
            for (String aoi : map.keySet()) {
            	// Data row for output .csv file
            	ArrayList<String> data = new ArrayList<>();
            	data.add(aoi);
            	
            	ArrayList<String[]> aoiData = map.get(aoi);
            	ArrayList<Point2D.Double> aoiPoints = new ArrayList<Point2D.Double>();
            	HashMap<Integer, Integer> fixCount = new HashMap<Integer, Integer>();
            	ArrayList<Double> allDurations = new ArrayList<Double>();
            	
            	// Iterate through each AOI data to populate the above lists
            	for (int i = 0; i < aoiData.size(); i++) {
            		String[] entry = aoiData.get(i);
            		
            		// Add each coordinate to the list
            		aoiPoints.add(new Point2D.Double(Double.valueOf(entry[xIndex]) * SCREEN_WIDTH, Double.valueOf(entry[yIndex]) * SCREEN_HEIGHT));
            		
            		// Count the number of unique fixations in each AOI
            		if (!fixCount.containsKey(Integer.valueOf(entry[fixId])))
            			fixCount.put(Integer.valueOf(entry[fixId]), 1);
            		
            		// Add duration value to list
            		allDurations.add(Double.valueOf(entry[fixDurIndex]));
            		
            	}
            	
            	// Calculate the convex hull and its area 
            	List<Point2D.Double> boundingPoints = convexHull.getConvexHull(aoiPoints);
            	Point2D[] points = fixation.listToArray(boundingPoints);
            	data.add(String.valueOf(convexHull.getPolygonArea(points)));
            	
            	// Write the number of fixations located in each AOI
            	data.add(String.valueOf(fixCount.keySet().size()));
            	
            	// Write the total duration of the AOI
            	data.add(String.valueOf(descriptiveStats.getSumOfDoubles(allDurations)));
            	
            	// Write the mean duration
                data.add(String.valueOf(descriptiveStats.getMeanOfDoubles(allDurations)));

                // Write the median duration
                data.add(String.valueOf(descriptiveStats.getMedianOfDoubles(allDurations)));

                // Write the standard deviation between durations
                data.add(String.valueOf(descriptiveStats.getStDevOfDoubles(allDurations)));

                // Write the minimum duration
                data.add(String.valueOf(descriptiveStats.getMinOfDoubles(allDurations)));

                // Write the max duration
                data.add(String.valueOf(descriptiveStats.getMaxOfDoubles(allDurations)));
            	
            	// Write the data into the .csv file as a new row
                outputCSVWriter.writeNext(data.toArray(new String[data.size()]));
            }
            
            outputCSVWriter.close();
            csvReader.close();
            System.out.println("done writing AOI data to" + outputFile);
            systemLogger.writeToSystemLog(Level.INFO, gazeAnalytics.class.getName(), "done writing AOI data to " + outputFile );
		}
		catch(FileNotFoundException e) {
			 systemLogger.writeToSystemLog(Level.WARNING, gazeAnalytics.class.getName(), "Unable to open file " + inputFile + "\n" + e.toString());
			System.out.println("Unable to open file '" + inputFile + "'");
	    }
		catch(IOException e) {
			 systemLogger.writeToSystemLog(Level.WARNING, gazeAnalytics.class.getName(), "Error reading file " + inputFile + "\n" + e.toString());

	        System.out.println("Error reading file '" + inputFile + "'");
	    }
	}
}