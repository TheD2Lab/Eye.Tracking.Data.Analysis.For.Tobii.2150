package analysis;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Date;

/*
 * responsible for keeping track of the progress of the program
 */
public class systemLogger {
	private static String logFilePath = "";
	
	/*
	 * Creates a text file named SystemLog.txt and puts the file in the given folder path
	 * @param	folderPath	path of where the text file belongs
	 */
	public static void createSystemLog(String folderPath)
	{
		try 
		{
			logFilePath = folderPath + "\\SystemLog.txt";
			File logFile = new File(logFilePath);
			if(logFile.createNewFile())
			{
				System.out.println("INFO: SystemLog.txt has successfully been created");
				FileWriter logWriter = new FileWriter(logFilePath);
				Date date = new Date();
				logWriter.write("Log was created on" + date.toString());
				logWriter.close();
			}
			else
			{
				System.out.println("INFO: SystemLog.txt already exists");
				return;
			}
		}
		catch(IOException e)
		{
			System.out.println("FATAL: unable to create SystemLog.txt");
			e.printStackTrace();
			System.exit(0);
		}
		
	}
	
	/*
	 * Writes a descriptive statement to the system log outlining the time, the level of importance
	 * what class the method was called, and the given message
	 * @param	level			a set of standard logging level 
	 * @param	sourceClass		what class called the method
	 * @param	message			the message to be written
	 */
	public static void writeToSystemLog(Level level, String sourceClass, String message)
	{	
		try
		{
			FileWriter logWriter = new FileWriter(logFilePath);
			java.util.Date date = new java.util.Date();
			logWriter.write(date.toString());
			logWriter.write(level.getName() + ": " + sourceClass + "-"  + message);
			logWriter.close();
		}
		catch(IOException e)
		{
			System.out.println("ERROR: unable to write to log");
			return;
		}
	}

}
