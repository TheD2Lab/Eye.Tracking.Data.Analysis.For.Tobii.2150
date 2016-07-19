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
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class event {

	public static void processEvent(String inputFile, String outputFile) throws IOException {
		
		String line = null;
        ArrayList<Object> allMouseLeft = new ArrayList<Object>();
        
        FileWriter fileWriter = new FileWriter(outputFile);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

        try {
            FileReader fileReader = new FileReader(inputFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while((line = bufferedReader.readLine()) != null && line.isEmpty()==false) {
                
                String[] lineArray = fixation.lineToArray(line);
                
                if(lineArray[1].equals("LMouseButton") && lineArray[2].equals("1")){
                	allMouseLeft.add(lineArray);
                }
              
            }	
            
            bufferedWriter.write("total number of L mouse clicks: " + allMouseLeft.size());
            bufferedWriter.newLine();
            
            bufferedWriter.close();
            bufferedReader.close();	
            
            System.out.println("done writing event data to: " + outputFile);
		
		}catch(FileNotFoundException ex) {
	        System.out.println("Unable to open file '" + inputFile + "'");				
	    }catch(IOException ex) {
	        System.out.println("Error reading file '" + inputFile + "'");			
	    }
	}
	
}
	
