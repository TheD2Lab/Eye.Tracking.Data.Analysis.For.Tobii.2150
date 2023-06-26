package analysis;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

public class ScanPath {


	private LinkedHashMap<Double,String>aoi = new LinkedHashMap<>(); 	//key is the time and the value is the aoi 
	private HashMap<String, Double> scanpathTimes = new HashMap<>();
	private String fixationFile; 
	private String outputPath;
	private static final double APPROVEDINTERVAL = 1.5;

	public ScanPath(String fixationFile, String outputPath)
	{
		this.fixationFile = fixationFile;
		File snapshotFolder = new File(outputPath + "/ScanPathFolder/");
		this.outputPath = outputPath + "/ScanPathFolder/";
		snapshotFolder.mkdir();
		parseFile();

	}

	public void runAllClimbScan() throws Exception
	{
		TScan();
		primaryInstrumentAS();
		primaryInstrumentVSI();
		pitchTriangle();
		radialScan();
		circularScan();
		avgScanPercentage(outputPath + "percentage.csv");

	}
	//parses the files and puts all the needed value in a hashmap
	private void parseFile()
	{
		FileReader fileReader;
		CSVReader csvReader;
		try {
			fileReader = new FileReader(fixationFile);
			csvReader = new CSVReader(fileReader);
			String[]nextLine = csvReader.readNext();
			int aoiColumnIndex = Arrays.asList(nextLine).indexOf("AOI");
			int timeIndex = -1;

			for(int i = 0; i < nextLine.length; i++)
			{
				if(nextLine[i].contains("TIME")) {
					timeIndex = i;
					break;
				}
			}

			while((nextLine = csvReader.readNext()) != null) 
			{
				String value = nextLine[aoiColumnIndex];
				double key = Double.valueOf(nextLine[timeIndex]);
				if(!value.isBlank() && !value.equals("Outside"))
				{
					aoi.put(key, value);
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (CsvValidationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	//Good
	public void TScan() throws IOException
	{
		//AS, AI,HI,ALT
		Iterator<Entry<Double,String>> aoiIterator = aoi.entrySet().iterator();
		int counter = 0; 
		boolean AI, HI, ALT, AS;
		double prevElementTime, startTime; 
		AI = HI = ALT = AS = false;
		prevElementTime = startTime = -1;

		while(aoiIterator.hasNext())
		{
			Map.Entry<Double, String> element = (Map.Entry<Double, String>)aoiIterator.next();
			if(prevElementTime == -1 || element.getKey()- prevElementTime < 1)
			{
				switch(element.getValue())
				{
				case "Horizontal Altitude":
					AI = true;
					if(startTime == -1) startTime = element.getKey();
					prevElementTime = element.getKey();
					break;
				case "Airspeed":
					AS = true;
					if(startTime == -1) startTime = element.getKey();
					prevElementTime = element.getKey();
					break;
				case "Heading" :
					HI = true;
					if(startTime == -1) startTime = element.getKey();
					prevElementTime = element.getKey();
					break;
				case "MSL Altitude":
					ALT = true;
					if(startTime == -1) startTime = element.getKey();
					prevElementTime = element.getKey();
					break;
				}

			}
			else if(element.getKey()- prevElementTime < APPROVEDINTERVAL)
			{
				continue;
			}
			else
			{
				if(AI && HI && ALT && AS)
				{

					writeToFile(fixationFile, outputPath + "T_Scan" + counter + ".csv",startTime, prevElementTime);

					if(scanpathTimes.containsKey("TScan"))
					{
						scanpathTimes.put("TScan", scanpathTimes.get("TScan")+(prevElementTime - startTime));
					}
					else
					{
						scanpathTimes.put("TScan", prevElementTime - startTime);
					}
					counter++;
				}
				AI = HI = ALT = AS = false;
				prevElementTime = startTime = -1;

			}

		}
	}

	public void primaryInstrumentAS() throws IOException
	{
		Iterator<Entry<Double,String>> aoiIterator = aoi.entrySet().iterator();
		int counter = 0; 
		boolean AI, HI, AS;
		double prevElementTime, startTime; 
		prevElementTime = startTime = -1;
		HI = AI = AS = false;

		while(aoiIterator.hasNext())
		{
			Map.Entry<Double, String> element = (Map.Entry<Double, String>)aoiIterator.next();
			if(prevElementTime == -1 || element.getKey()- prevElementTime < 1)
			{
				switch(element.getValue())
				{
				case "Horizontal Altitude":
					AI = true;
					if(startTime == -1) startTime = element.getKey();
					prevElementTime = element.getKey();
					break;
				case "Airspeed":
					AS = true;
					if(startTime == -1) startTime = element.getKey();
					prevElementTime = element.getKey();
					break;
				case "Heading" :
					HI = true;
					if(startTime == -1) startTime = element.getKey();
					prevElementTime = element.getKey();
					break;
				}

			}
			else if(element.getKey()- prevElementTime < APPROVEDINTERVAL)
			{
				continue;
			}
			else
			{
				if(AI && HI && AS)
				{

					writeToFile(fixationFile, outputPath + "primaryInstrumentAS" + counter + ".csv",startTime, prevElementTime);


					if(scanpathTimes.containsKey("primaryInstrumentAS"))
					{
						scanpathTimes.put("primaryInstrumentAS", scanpathTimes.get("primaryInstrumentAS")+(prevElementTime - startTime));
					}
					else
					{
						scanpathTimes.put("primaryInstrumentAS", prevElementTime - startTime);
					}
					counter++;
				}
				AI = HI = AS = false;
				prevElementTime = startTime = -1;

			}

		}
	}

	public void primaryInstrumentVSI() throws IOException
	{
		Iterator<Entry<Double,String>> aoiIterator = aoi.entrySet().iterator();
		int counter = 0; 
		boolean AI, HI, VSI;
		double prevElementTime, startTime; 
		prevElementTime = startTime = -1;
		HI = AI = VSI = false;

		while(aoiIterator.hasNext())
		{
			Map.Entry<Double, String> element = (Map.Entry<Double, String>)aoiIterator.next();
			if(prevElementTime == -1 || element.getKey()- prevElementTime < 1)
			{
				switch(element.getValue())
				{
				case "Horizontal Altitude":
					AI = true;
					if(startTime == -1) startTime = element.getKey();
					prevElementTime = element.getKey();
					break;
				case "Vertical Speed":
					VSI = true;
					if(startTime == -1) startTime = element.getKey();
					prevElementTime = element.getKey();
					break;
				case "Heading" :
					HI = true;
					if(startTime == -1) startTime = element.getKey();
					prevElementTime = element.getKey();
					break;
				}

			}
			else if(element.getKey()- prevElementTime < APPROVEDINTERVAL)
			{
				continue;
			}
			else
			{
				if(AI && HI && VSI)
				{

					writeToFile(fixationFile, outputPath + "primaryInstrumentVSI" + counter + ".csv",startTime, prevElementTime);

					if(scanpathTimes.containsKey("primaryInstrumentVSI"))
					{
						scanpathTimes.put("primaryInstrumentVSI", scanpathTimes.get("primaryInstrumentVSI")+(prevElementTime - startTime));
					}
					else
					{
						scanpathTimes.put("primaryInstrumentVSI", prevElementTime - startTime);
					}
					counter++;
				}
				AI = HI = VSI = false;
				prevElementTime = startTime = -1;

			}

		}
	}

	public void pitchTriangle() throws IOException
	{
		Iterator<Entry<Double,String>> aoiIterator = aoi.entrySet().iterator();
		int counter = 0; 
		boolean AI, ALT, VSI, AS;
		double prevElementTime, startTime; 
		prevElementTime = startTime = -1;
		ALT = AI = VSI = AS = false;

		while(aoiIterator.hasNext())
		{
			Map.Entry<Double, String> element = (Map.Entry<Double, String>)aoiIterator.next();
			if(prevElementTime == -1 || element.getKey()- prevElementTime < 1)
			{
				switch(element.getValue())
				{
				case "Horizontal Altitude":
					AI = true;
					if(startTime == -1) startTime = element.getKey();
					prevElementTime = element.getKey();
					break;
				case "Vertical Speed":
					VSI = true;
					if(startTime == -1) startTime = element.getKey();
					prevElementTime = element.getKey();
					break;
				case "MSL Altitude":
					ALT = true;
					if(startTime == -1) startTime = element.getKey();
					prevElementTime = element.getKey();
					break;
				case "Airspeed":
					AS = true;
					if(startTime == -1) startTime = element.getKey();
					prevElementTime = element.getKey();
					break;
				}

			}
			else if(element.getKey()- prevElementTime < APPROVEDINTERVAL)
			{
				continue;
			}
			else
			{
				if(AI && ALT && VSI && AS)
				{

					writeToFile(fixationFile, outputPath + "pitchTriangle" + counter + ".csv",startTime, prevElementTime);
					if(scanpathTimes.containsKey("pitchTriangle"))
					{
						scanpathTimes.put("pitchTriangle", scanpathTimes.get("pitchTriangle")+(prevElementTime - startTime));
					}
					else
					{
						scanpathTimes.put("pitchTriangle", prevElementTime - startTime);
					}
					counter++;
				}
				AI = ALT = VSI = AS = false;
				prevElementTime = startTime = -1;

			}

		}
	}

	public void radialScan() throws IOException
	{

		Iterator<Entry<Double,String>> aoiIterator = aoi.entrySet().iterator();
		int counter = 0; // 6 
		int csvCounter = 0;
		boolean AI,otherInstr;
		double prevElementTime, startTime; 
		String prevOthElementValue = ""; //o
		AI = otherInstr = false;
		prevElementTime = startTime = -1;

		while(aoiIterator.hasNext())
		{
			Map.Entry<Double, String> element = (Map.Entry<Double, String>)aoiIterator.next();
			if((prevElementTime == -1 || element.getKey()- prevElementTime < 1)&&element.getValue()!=null)
			{

				if(element.getValue().equals("Horizontal Altitude"))
				{
					if(AI == true)
					{
						continue;
					}
					else
					{
						AI = true;
						if(startTime==-1) startTime = element.getKey();
						prevElementTime = element.getKey();
						otherInstr = false;
						counter++;
						continue;
					}
				}
				else if(!(element.getValue() ==null)&& !(element.getValue().equals("outside")))
				{ 
					if(otherInstr == true)
					{
						if(prevOthElementValue.equals(element.getValue()))
						{
							continue;
						}
					}
					else
					{
						otherInstr= true;
						if(startTime==-1) startTime = element.getKey();
						prevElementTime = element.getKey();
						prevOthElementValue = element.getValue();
						AI = false;
						counter++;
						continue;
					}
				}


			}
			else if(element.getKey()- prevElementTime < 1)//if the element is null but within one minute
			{
				continue;
			}
			else
			{
				if(counter>=6)
				{

					writeToFile(fixationFile, outputPath + "RadialScan" + csvCounter + ".csv",startTime, prevElementTime);
					csvCounter++;
					if(scanpathTimes.containsKey("radialScan"))
					{
						scanpathTimes.put("radialScan", scanpathTimes.get("radialScan")+(prevElementTime - startTime));
					}
					else
					{
						scanpathTimes.put("radialScan", prevElementTime - startTime);
					}

				}
				AI = otherInstr = false;
				prevElementTime = startTime = -1;
				counter = 0;
				prevOthElementValue = "";

			}

		}
	}



	public void circularScan() throws Exception 
	{
		String[]order = {"Airspeed", "Horizontal Altitude", "MSL Altitude", "Vertical Speed", "Heading", "Turn Coordinator"};
		int prevElementIndex = -1;
		double prevElementTime, startTime; 
		prevElementTime = startTime = -1;
		Iterator<Entry<Double,String>> aoiIterator = aoi.entrySet().iterator();
		int counter = 0; 
		int csvCounter = 0;
		while(aoiIterator.hasNext())
		{
			Map.Entry<Double, String> element = (Map.Entry<Double, String>)aoiIterator.next();
			int currentElementIndex = Arrays.asList(order).indexOf(element.getValue());
			if(prevElementIndex == -1)
			{
				prevElementIndex = currentElementIndex;
				counter++; 
				startTime = element.getKey();
				prevElementTime = element.getKey();
			}
			else if(element.getKey()- prevElementTime < APPROVEDINTERVAL)
			{
				if(prevElementIndex == 0)
				{
					if(currentElementIndex == 1 || currentElementIndex == order.length -1)
					{
						prevElementIndex = currentElementIndex;
						counter++; 
						prevElementTime = element.getKey();
						continue;
					}
				}
				else if(prevElementIndex == order.length -1)
				{
					if(currentElementIndex == 0 || currentElementIndex == prevElementIndex - 1)
					{
						prevElementIndex = currentElementIndex;
						counter++; 
						prevElementTime = element.getKey();
						continue;
					}
				}
				else
				{
					if(currentElementIndex == prevElementIndex + 1 || currentElementIndex == prevElementIndex - 1)
					{
						prevElementIndex = currentElementIndex;
						counter++; 
						prevElementTime = element.getKey();
						continue;
					}
				}
			}
			else
			{
				if(counter >= 3)
				{
					writeToFile(fixationFile, outputPath + "CircularScan" + csvCounter + ".csv",startTime, prevElementTime);
					csvCounter++;
					if(scanpathTimes.containsKey("CircularScan"))
					{
						scanpathTimes.put("CircularScan", scanpathTimes.get("CircularScan")+(prevElementTime - startTime));
					}
					else
					{
						scanpathTimes.put("CircularScan", prevElementTime - startTime);
					}
				}

				prevElementTime = startTime = prevElementIndex = -1;
				counter = 0;
			}

		}
	}

	private static void writeToFile(String inputFile, String outputFile, double start, double end) throws IOException
	{
		FileWriter outputFileWriter = new FileWriter(new File (outputFile));
		CSVWriter outputCSVWriter = new CSVWriter(outputFileWriter);
		FileReader fileReader = new FileReader(inputFile);
		CSVReader csvReader = new CSVReader(fileReader);

		try 
		{
			//header
			String[]nextLine = csvReader.readNext();
			outputCSVWriter.writeNext(nextLine);

			int timestampIndex = -1;
			for(int i = 0; i < nextLine.length; i++)
			{
				if(nextLine[i].contains("TIME("))
				{
					timestampIndex = i;
					break;
				}
			}

			while((nextLine = csvReader.readNext()) != null) 
			{
				if(Double.valueOf(nextLine[timestampIndex]) < start)
				{
					continue;
				}
				else if(Double.valueOf(nextLine[timestampIndex]) > end)
				{
					break;
				}
				else
				{
					outputCSVWriter.writeNext(nextLine);
				}
			}

			if((nextLine = csvReader.readNext()).equals(null))
			{
				System.exit(0);
			}	

			systemLogger.writeToSystemLog(Level.INFO, WindowOperations.class.getName(), "Successfully created file " + outputFile );
		}
		catch(NullPointerException ne)
		{
			System.out.println("done writing file: " + outputFile);
			outputCSVWriter.close();
			modifier.csvToARFF(outputFile);
		}
		catch(Exception e)
		{
			systemLogger.writeToSystemLog(Level.SEVERE, WindowOperations.class.getName(), "Error with window method  " + outputFile + "\n" + e.toString());
			System.exit(0);

		}
		finally
		{
			outputCSVWriter.close();
			csvReader.close();
		}

		modifier.csvToARFF(outputFile);
	}


	public void avgScanPercentage(String outputFile) throws IOException
	{
		double totalTimes = 0; 
		String []header = new String[scanpathTimes.size()];
		String [] avg = new String[scanpathTimes.size()];
		int counter = 0;
		for (Map.Entry<String, Double> times : scanpathTimes.entrySet()) 
		{
			totalTimes += times.getValue();
			header[counter++] = times.getKey() + " percentage";

		}
		counter = 0; 
		for (Map.Entry<String, Double> times : scanpathTimes.entrySet()) 
		{
			//check
			avg[counter++] =String.valueOf((times.getValue() / totalTimes) * 100);

		}
		FileWriter outputFileWriter = new FileWriter(new File (outputFile));
		CSVWriter outputCSVWriter = new CSVWriter(outputFileWriter);
		try
		{
			outputCSVWriter.writeNext(header);
			outputCSVWriter.writeNext(avg);
		}catch(Exception e)
		{
			System.out.println(e);
		}
		finally
		{
			outputCSVWriter.close();
		}
	}






















}
