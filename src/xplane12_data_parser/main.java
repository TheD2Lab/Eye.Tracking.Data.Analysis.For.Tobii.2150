package xplane12_data_parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.opencsv.CSVWriter;

public class main {

	public static void main(String[] args) 
	{
		String filePath = "C:\\Users\\kayla\\Downloads\\Data.txt";
		String outputFilePath = "C:\\Users\\kayla\\Documents\\TestingReformatted_data.csv";
		try 
		{
			FileReader fileReader = new FileReader(filePath);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			FileWriter outputFileWriter = new FileWriter(new File (outputFilePath));
			CSVWriter outputCSVWriter = new CSVWriter(outputFileWriter);
			String line = bufferedReader.readLine();
			while ((line = bufferedReader.readLine()) != null) 
			{
				String[] fields = line.split("\\|");
				outputCSVWriter.writeNext(fields);
			}
			outputCSVWriter.close();
			bufferedReader.close();
			fileReader.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
}
