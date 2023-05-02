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
		String txtFilePath = "C:\\Users\\capta\\OneDrive\\Documents\\result\\Data.txt";
		String outputFilePath = "C:\\Users\\capta\\OneDrive\\Documents\\result";
		String originalCSVFilePath = parser.txtToCSV(txtFilePath, outputFilePath);
		String refactoredCSVFilePath = parser.parseData(originalCSVFilePath, outputFilePath);
		double[]horiDef = parser.getData(refactoredCSVFilePath, "copN1,h-def");
		double[]speed = parser.getData(refactoredCSVFilePath, "_Vind,_kias");
		double[]vertDef = parser.getData(refactoredCSVFilePath, "copN1,v-def");
		scoreCalcuations score = new scoreCalcuations();
		System.out.println(score.getHighestScore());
		score.scoreCalc(horiDef,speed, vertDef);
		System.out.println(score.getTotalScore() + " / " + score.getHighestScore() + ":");
		System.out.println(score.getTotalScore() / score.getHighestScore());
		
	}
	
}