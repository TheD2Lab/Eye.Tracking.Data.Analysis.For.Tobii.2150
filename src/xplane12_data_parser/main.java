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

	public static void main(String[] args) {
		String filePath = "C:\\Users\\kayla\\Downloads\\Data.txt";
		String outputFilePath = "C:\\Users\\kayla\\Downloads\\Reformatted_data.csv";
		
        List<String>header = new ArrayList<>();
        List<String>elements = new ArrayList<>();
		//parse the file based off of "|" and check if the separated string is a number or not. 
		 try {
	            FileReader fileReader = new FileReader(filePath);
	            BufferedReader bufferedReader = new BufferedReader(fileReader);
	            String line;
	            while ((line = bufferedReader.readLine()) != null) {
	                String[] fields = line.split("\\|");
	                for (String field : fields) {
	                	field.replaceAll("\\s", "");
	                    if(!isNumeric(field))
	                    {
	                    	header.add(field);
	                    }
	                    else
	                    {
	                    	elements.add(field);
	                    }
	                }
	            }
	            header.remove(0);
	            FileWriter outputFileWriter = new FileWriter(new File (outputFilePath));
	            CSVWriter outputCSVWriter = new CSVWriter(outputFileWriter);
	            outputCSVWriter.writeNext(header.toArray(String[]::new));
	            String[]elementLine = new String[header.size()];
	            int count = 0;
	            for(String element: elements)
	            {
	            	elementLine[count++] = element;
	            	if(count >= header.size())
	            	{
	            		outputCSVWriter.writeNext(elementLine);
	            		elementLine = new String[header.size()];
	            		count = 0;
	            	}
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
	
	private static boolean isNumeric(String str) { 
		  try {  
		    Double.parseDouble(str);  
		    return true;
		  } catch(NumberFormatException e){  
		    return false;  
		  }  
		}
}
