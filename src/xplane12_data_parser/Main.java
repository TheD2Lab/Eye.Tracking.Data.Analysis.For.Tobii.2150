package xplane12_data_parser;

import java.io.IOException;
import com.opencsv.exceptions.CsvValidationException;

public class Main {

	public static void main(String[] args) throws CsvValidationException, IOException 
	{
		// Create Strings for input file and output file directories
		String txtFilePath = "C:\\Users\\capta\\OneDrive\\Documents\\result\\Data.txt";
		String outputFilePath = "C:\\Users\\capta\\OneDrive\\Documents\\result";
		
		// Parse the CSV files for the data points
		String originalCSVFilePath = parser.txtToCSV(txtFilePath, outputFilePath);
		String refactoredCSVFilePath = parser.parseData(originalCSVFilePath, outputFilePath);
		double[]horiDef = parser.getData(refactoredCSVFilePath, "copN1,h-def");
		double[]speed = parser.getData(refactoredCSVFilePath, "_Vind,_kias");
		double[]vertDef = parser.getData(refactoredCSVFilePath, "copN1,v-def");
		
		// Calculate the score for the ILS approach
		scoreCalcuations score = new scoreCalcuations();
		score.scoreCalc(horiDef,speed, vertDef);
		
		// Print out values
		System.out.println("Data Points = " + score.getDataPoints() + " * 3 = Highest Possible Points = " + score.getHighestScore() + "\n");
		System.out.println("Speed Penatly: " + score.getSpeedPenalty());
		System.out.println("Glideslope: " + score.getGlideSlopePenalty());
		System.out.println("Localizer Penalty: " + score.getLocalizerPenalty());
		System.out.println("\n" + score.getTotalScore() + " / " + score.getHighestScore() + ":");
		System.out.println(score.getPercentageScore() + " %");
		
	}
	
}