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

import java.io.BufferedReader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;


public class event {

	public static void processEvent(String inputFile, String outputFile) throws IOException, CsvValidationException {
		
		String line = null;
        ArrayList<Object> allMouseLeft = new ArrayList<Object>();
        
        FileWriter outputFileWriter = new FileWriter(new File (outputFile));
        CSVWriter outputCSVWriter = new CSVWriter(outputFileWriter);

        try {
        	 FileReader fileReader = new FileReader(inputFile);
             CSVReader csvReader = new CSVReader(fileReader);
             String[]nextLine = csvReader.readNext();
             
            int leftMouseButtonIndex = -1;
         	for(int i = 0; i < nextLine.length; i++)
         	{
         		String header = nextLine[i];
         		
         		if(header.equals("CS"))
         		{
         			leftMouseButtonIndex = i;
         		}
         	}
             
            while((nextLine = csvReader.readNext()) != null) {     
                if(nextLine[leftMouseButtonIndex].equals("1")){
                	allMouseLeft.add(nextLine);
                }
              
            }	
            outputCSVWriter.writeNext(new String[]{"total number of L mouse clicks"});
            outputCSVWriter.writeNext(new String[] {String.valueOf(allMouseLeft.size())});
            outputCSVWriter.close();
            csvReader.close();
            System.out.println("done writing event data to: " + outputFile);
		
		}catch(FileNotFoundException ex) {
	        System.out.println("Unable to open file '" + inputFile + "'");				
	    }catch(IOException ex) {
	        System.out.println("Error reading file '" + inputFile + "'");			
	    }
	}
	
}
	
