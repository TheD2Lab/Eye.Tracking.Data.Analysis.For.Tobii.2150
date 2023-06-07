package xplane12_data_parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

public class parser {

	private static int numOfData = 0; 
	public static String parseData(String filePath, String outputFolderPath) throws IOException, CsvValidationException
	{
		String refactoredFilePath = outputFolderPath + "\\Refactored_Data.csv";
		FileWriter outputFileWriter = new FileWriter(new File (refactoredFilePath));
		CSVWriter outputCSVWriter = new CSVWriter(outputFileWriter);
		FileReader fileReader = new FileReader(filePath);
		CSVReader csvReader = new CSVReader(fileReader);
		List<String[]> selectedColumns = new ArrayList<>();
		List<String> columnNames = Arrays.asList(
				"_totl,_time",
				"_Vind,_kias",
				"Vtrue,_ktas",
				"Vtrue,_ktgs",
				"hpath,_true",
				"vpath,__deg",
				"pitch,__deg",
				"_roll,__deg",
				"hding,_true",
				"hding,__mag",
				"__mag,_comp",
				"__lat,__deg",
				"__lon,__deg",
				"___CG,ftMSL",
				"copN1,h-def",
				"copN1,v-def"
				);
		int[] columnIndex = new int[columnNames.size()];
		try 
		{
			String[] headers = csvReader.readNext();
			for (int i = 0; i < columnNames.size(); i++) 
			{
				columnIndex[i] = Arrays.asList(headers).indexOf(columnNames.get(i));
				if (columnIndex[i] == -1) 
				{
					throw new IOException("Column not found: " + columnNames.get(i));
				}
			}
			String[] row;
			while ((row = csvReader.readNext()) != null) 
			{
				String[] selectedRow = new String[columnIndex.length];
				for (int i = 0; i < columnIndex.length; i++) 
				{
					selectedRow[i] = row[columnIndex[i]];
				}
				selectedColumns.add(selectedRow);
			}
			outputCSVWriter.writeNext(columnNames.toArray(new String[0])); // write the header row
			outputCSVWriter.writeAll(selectedColumns); // write the selected columns
		} 
		catch (IOException e) 
		{
			System.out.println(e);
		}
		finally
		{
			outputCSVWriter.close();
			csvReader.close();
		}
		return refactoredFilePath;
		
	}

	public static String txtToCSV(String filePath, String outputFolderPath)
	{
		String csvFilePath = outputFolderPath + "\\Reformatted_Data1.csv";
		try 
		{
			FileReader fileReader = new FileReader(filePath);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			FileWriter outputFileWriter = new FileWriter(new File (csvFilePath));
			CSVWriter outputCSVWriter = new CSVWriter(outputFileWriter);
			String line = bufferedReader.readLine();
			while ((line = bufferedReader.readLine()) != null) 
			{
				String[] fields = line.split("\\|");
				//removes all the spaces from each element
				for (int i = 0; i < fields.length; i++) 
				{
				    fields[i] = fields[i].replaceAll("\\s+", "");
				}
				outputCSVWriter.writeNext(fields);
				numOfData++;
			}
			outputCSVWriter.close();
			bufferedReader.close();
			fileReader.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			System.out.println(e);
		}
		return csvFilePath;
	}

	public static int getNumberOfData() {
		return numOfData;
	}

	public static void setNumberOfData(int numberOfData) {
		parser.numOfData = numberOfData;
	}
	
	
	
	public static double[]getData(String filePath, String dataHeader) throws FileNotFoundException
	{
		double[]data = new double[numOfData];
		FileReader fileReader = new FileReader(filePath);
		CSVReader csvReader = new CSVReader(fileReader);
		int headerIndex = -1;
		try 
		{
			String[] headers = csvReader.readNext();
			for (int i = 0; i < headers.length; i++) 
			{
				if(headers[i].equals(dataHeader))
				{
					headerIndex = i;
				}
			}
			int index = 0; 
			String[] row;
			while ((row = csvReader.readNext()) != null) 
			{
				data[index] = Double.valueOf(row[headerIndex]);
				index++;
			}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		return data;
	}
	

		
}
