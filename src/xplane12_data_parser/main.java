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
import com.opencsv.exceptions.CsvValidationException;

public class main {

	public static void main(String[] args) throws CsvValidationException, IOException 
	{
		String txtFilePath = "C:\\Users\\kayla\\Downloads\\Data (1).txt";
		String outputFilePath = "C:\\Users\\kayla\\OneDrive\\Desktop";
		String originalCSVFilePath = parser.txtToCSV(txtFilePath, outputFilePath);
		String refactoredCSVFilePath = parser.parseData(originalCSVFilePath, outputFilePath);
		
	}
	
}
