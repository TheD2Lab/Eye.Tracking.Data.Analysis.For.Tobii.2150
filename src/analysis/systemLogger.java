package analysis;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Date;

public class systemLogger {
	private static String logFilePath = "";
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
