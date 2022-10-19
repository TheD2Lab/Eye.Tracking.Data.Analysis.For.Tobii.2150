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

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

public class AOI {
	
	public static void processAOIs(String inputFile, String outputFile) throws IOException, CsvValidationException {
		// Resolution of monitor
		int SCREEN_WIDTH = 1024;
		int SCREEN_HEIGHT = 576;
		
		try {
			// Read input CSV file and initalize the column indexes for the data needed
			FileReader fileReader = new FileReader(inputFile);
            CSVReader csvReader = new CSVReader(fileReader);
            String[] nextLine = csvReader.readNext();
            int aoiIndex = -1, xIndex = -1, yIndex = -1;
            
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
            		default:
            			break;
            	}
            }
            
            // Iterate through input file and group points by AOI
            HashMap<String, ArrayList<Point2D.Double>> map = new HashMap<String, ArrayList<Point2D.Double>>();
            while ((nextLine = csvReader.readNext()) != null) {
            	// If the data point is not part of an AOI, skip it
            	// Else if we've already encountered this AOI, add it to it's corresponding list
            	// Otherwise create a new list if it's a new AOI
            	String aoi = nextLine[aoiIndex];
            	if (aoi.equals(""))
            		continue;
            	else if (map.containsKey(aoi))
            		map.get(aoi).add(new Point2D.Double(Double.valueOf(nextLine[xIndex]) * SCREEN_WIDTH, Double.valueOf(nextLine[yIndex]) * SCREEN_HEIGHT));
            	else
            		map.put(aoi, new ArrayList<Point2D.Double>());
            }
            
            // Initializing list of headers and their corresponding data for .CSV output
            ArrayList<String> headers = new ArrayList<>();
            ArrayList<String> data = new ArrayList<>();
            
            // Calculate the convex hull and its area for each AOI and output into a .csv file
            for (String aoi : map.keySet()) {
            	List<Point2D.Double> boundingPoints = convexHull.getConvexHull(map.get(aoi));
            	Point2D[] points = listToArray(boundingPoints);
            	
            	headers.add(aoi + " convex hull area");
                data.add(String.valueOf(convexHull.getPolygonArea(points)));
            }
            
            FileWriter outputFileWriter = new FileWriter(new File (outputFile));
            CSVWriter outputCSVWriter = new CSVWriter(outputFileWriter);
            outputCSVWriter.writeNext(headers.toArray(new String[headers.size()]));
            outputCSVWriter.writeNext(data.toArray(new String[data.size()]));
            outputCSVWriter.close();
            csvReader.close();
            System.out.println("done writing AOI data to" + outputFile);
		}
		catch(FileNotFoundException e) {
			System.out.println("Unable to open file '" + inputFile + "'");
	    }
		catch(IOException e) {
	        System.out.println("Error reading file '" + inputFile + "'");
	    }
	}
	
	public static Point2D.Double[] listToArray(List<Point2D.Double> allPoints){
		Point2D.Double[] points = new Point2D.Double[allPoints.size()];
        for(int i=0; i<points.length; i++){
        	points[i] = allPoints.get(i);
        }
        return points;
	}
}
