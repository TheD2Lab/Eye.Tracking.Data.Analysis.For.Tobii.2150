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

import java.util.ArrayList;


public class angle {
	
	
	//given two points A & B on the screen with (X1, Y1) and (X2, Y2) respectively
	//the absolute saccade slope = |(Y2-Y1)|/|(X2-X1)|
	//and the absolute saccade arctangent = arctan(slope)
	//finally turn the arctangent into degrees
	//in the rare cases where the fixation B is a straight vertical line to fixation A,
	//in other words, A and B have the same X value,
	//90 degrees is returned as the absolute angle (with respect to X axis) since we cannot divide numbers by zero. 
	public static ArrayList<Double> getAllAbsoluteAngles(ArrayList<Object> allCoordinates){
		
		ArrayList<Double> allAbsoluteDegrees = new ArrayList<Double>();
		double absoluteDegree = 0.0;
		
		for(int i=0; i<allCoordinates.size(); i++){
			Integer[] earlyCoordinate = (Integer[]) allCoordinates.get(i);
			
			if((i+1)<allCoordinates.size()){
				
				Integer[] laterCoordinate = (Integer[]) allCoordinates.get(i+1);
				
				double differenceInY = laterCoordinate[1] - earlyCoordinate[1];
				double differenceInX = laterCoordinate[0] - earlyCoordinate[0];
				
				if(differenceInX==0.0){
					//when A&B are in a straight vertical line
					absoluteDegree = 90.00;
				}else if(differenceInY==0.0){
					//when A&B are in a straight horizontal line
					absoluteDegree = 0.0;
				}else{
					//all other cases where A&B draw a sloppy line
					double absoluteSlope = Math.abs(differenceInY)/Math.abs(differenceInX);
					//returns the arctangent of a number as a value between -PI/2 and PI/2 radians
					double absoluteArctangent = Math.atan(absoluteSlope);
					absoluteDegree = Math.abs(Math.toDegrees(absoluteArctangent));
				}
				
				allAbsoluteDegrees.add(absoluteDegree);
			}
		}
		
		return allAbsoluteDegrees;
		
		
	}
	
	//given three points A, B and C with (X1, Y1) (X2, Y2) and (X3, Y3) respectively
	//the relative saccade angle = 180 degrees - ( arctan(|(Y2-Y1)/(X2-X1)|).toDegrees + arctan(|(Y3-Y2)/(X3-X2)|).toDegrees )
	//in cases where X1=X2=X3, the relative angle is 0 degree
	public static ArrayList<Double> getAllRelativeAngles(ArrayList<Object> allCoordinates){
		
		ArrayList<Double> allRelativeDegrees = new ArrayList<Double>();
		double relativeDegree = 0.0;
		double firstDegree = 0.0;
		double secondDegree = 0.0;
		
		for(int i=1; (i+1)<allCoordinates.size(); i++){
			Integer[] previousCoordinate = (Integer[]) allCoordinates.get(i-1);
			Integer[] currentCoordinate = (Integer[]) allCoordinates.get(i);
			Integer[] nextCoordinate = (Integer[]) allCoordinates.get(i+1);
			
			//System.out.println("i=" + i + "; previous X: " + previousCoordinate[0] + "; current X: " + currentCoordinate[0] + "; next X: " + nextCoordinate[0]);
			
			
			//degree between A and B
			double firstDifferenceInY = currentCoordinate[1]-previousCoordinate[1];
			double firstDifferenceInX = currentCoordinate[0]-previousCoordinate[0];
			
			//degree between B and C
			double secondDifferenceInY = nextCoordinate[1]-currentCoordinate[1];
			double secondDifferenceInX = nextCoordinate[0]-currentCoordinate[0];
			
			if((firstDifferenceInX==0.0 && secondDifferenceInX==0.0) || (firstDifferenceInY==0.0 && secondDifferenceInY==0.0)){
				//when A, B and C are all in a straight line, either horizontally or vertically
				relativeDegree = 180.0;
				
			}else if(firstDifferenceInX==0.0 && secondDifferenceInY<0.0){
				//when A&B are in a straight vertial line, C is to the lower (left or right) of B
				double secondSlope = Math.abs(secondDifferenceInX)/Math.abs(secondDifferenceInY);
				//returns the arctangent of a number as a value between -PI/2 and PI/2 radians
				double secondArctangent = Math.atan(secondSlope);
				secondDegree = Math.abs(Math.toDegrees(secondArctangent));
				//System.out.println("second difference in Y: " + Math.abs(nextCoordinate[1]-currentCoordinate[1]) + "; second different in X: " + Math.abs(nextCoordinate[0]-currentCoordinate[0]));
				//System.out.println("i=" + i +"; second slope: " + secondSlope + "; second angle: " + secondArctangent + "; second degree: " + secondDegree);
				
				//finally, the relative degree between A, B and C
				relativeDegree =  180.0 - secondDegree;
				//System.out.println("i=" + i + "; relative degree: " + relativeDegree);
				
				if (Double.isNaN(relativeDegree)){
					System.out.println("i=" + i + "; relative degree: " + relativeDegree);
					System.out.println("first degree: " + firstDegree + "; second degree: " + secondDegree);
					System.out.println("first differnce in X: " + firstDifferenceInX + "; second difference in X: " + secondDifferenceInX);
				}
				
			}else if(firstDifferenceInX==0.0 && secondDifferenceInY>0.0){
				//when A&B are in a straight vertical line, C is to the upper (left or right) of B
				double secondSlope = Math.abs(secondDifferenceInX)/Math.abs(secondDifferenceInY);
				double secondArctangent = Math.atan(secondSlope);
				relativeDegree = Math.toDegrees(secondArctangent);
				
				if (Double.isNaN(relativeDegree)){
					System.out.println("i=" + i + "; relative degree: " + relativeDegree);
					System.out.println("first degree: " + firstDegree + "; second degree: " + secondDegree);
					System.out.println("first differnce in X: " + firstDifferenceInX + "; second difference in X: " + secondDifferenceInX);
				}
				
			}else if(secondDifferenceInX==0.0 && firstDifferenceInY<0.0){
				//when B&C are in a stright vertical line, A is to the upper (left or right) of B
				double firstSlope = Math.abs(firstDifferenceInX)/Math.abs(firstDifferenceInY);
				//returns the arctangent of a number as a value between -PI/2 and PI/2 radians
				double firstArctangent = Math.atan(firstSlope);
				firstDegree = Math.abs(Math.toDegrees(firstArctangent));
				//finally, the relative degree between A, B and C
				relativeDegree = 180.0 - firstDegree;
				
				if (Double.isNaN(relativeDegree)){
					System.out.println("i=" + i + "; relative degree: " + relativeDegree);
					System.out.println("first degree: " + firstDegree + "; second degree: " + secondDegree);
					System.out.println("first differnce in X: " + firstDifferenceInX + "; second difference in X: " + secondDifferenceInX);
				}
				
			}else if(secondDifferenceInX==0.0 && firstDifferenceInY>0.0){
				//when B&C are in a straight vertical line, A is to the lower (left or right) of B
				double firstSlope = Math.abs(firstDifferenceInX)/Math.abs(firstDifferenceInY);
				double firstArctangent = Math.atan(firstSlope);
				relativeDegree = Math.toDegrees(firstArctangent);
				
				if (Double.isNaN(relativeDegree)){
					System.out.println("i=" + i + "; relative degree: " + relativeDegree);
					System.out.println("first degree: " + firstDegree + "; second degree: " + secondDegree);
					System.out.println("first differnce in X: " + firstDifferenceInX + "; second difference in X: " + secondDifferenceInX);
				}
				
			}else if(firstDifferenceInY==0.0 && secondDifferenceInX<0.0){
				//when A&B are in a straight horizontal line, C is to the lower left of B (note if C is to the lower right of B, it is included in the last if-else statement below)
				double secondSlope = Math.abs(secondDifferenceInY)/Math.abs(secondDifferenceInX);
				double secondArctangent = Math.atan(secondSlope);
				relativeDegree = Math.toDegrees(secondArctangent);
				
				if (Double.isNaN(relativeDegree)){
					System.out.println("i=" + i + "; relative degree: " + relativeDegree);
					System.out.println("first degree: " + firstDegree + "; second degree: " + secondDegree);
					System.out.println("first differnce in X: " + firstDifferenceInX + "; second difference in X: " + secondDifferenceInX);
				}
				
			}else if(secondDifferenceInY==0.0 && firstDifferenceInX<0.0){
				//when B&C are in a straight horizontal line, A is to the upper right of B (note if A is to the upper left of B, it is included in the last if-else statement below)
				double firstSlop = Math.abs(firstDifferenceInY)/Math.abs(firstDifferenceInX);
				double firstArctangent = Math.atan(firstSlop);
				relativeDegree = Math.toDegrees(firstArctangent);
				
				if (Double.isNaN(relativeDegree)){
					System.out.println("i=" + i + "; relative degree: " + relativeDegree);
					System.out.println("first degree: " + firstDegree + "; second degree: " + secondDegree);
					System.out.println("first differnce in X: " + firstDifferenceInX + "; second difference in X: " + secondDifferenceInX);
				}
				
			}else{
				//all other regular cases where A, B and C are spread from one another; and
				//when A&B are in a straight horizontal line, C is to the lower right of B; and
				//when B&C are in a straight horizontal line, A is to the upper left of B.
				double firstSlope = Math.abs(firstDifferenceInY)/Math.abs(firstDifferenceInX);
				//returns the arctangent of a number as a value between -PI/2 and PI/2 radians
				double firstArctangent = Math.atan(firstSlope);
				firstDegree = Math.abs(Math.toDegrees(firstArctangent));
				
				double secondSlope = Math.abs(secondDifferenceInY)/Math.abs(secondDifferenceInX);
				//returns the arctangent of a number as a value between -PI/2 and PI/2 radians
				double secondArctangent = Math.atan(secondSlope);
				secondDegree = Math.abs(Math.toDegrees(secondArctangent));
				
				//finally, the relative degree between A, B and C
				relativeDegree = 180.0 - firstDegree - secondDegree;
				
				if (Double.isNaN(relativeDegree)){
					System.out.println("i=" + i + "; relative degree: " + relativeDegree);
					System.out.println("first degree: " + firstDegree + "; second degree: " + secondDegree);
					System.out.println("first arctangent: " + firstArctangent + "; second arctangent: " + secondArctangent);
					System.out.println("first slope: " + firstSlope + "; second slope: " + secondSlope);
					System.out.println("first differnce in X: " + firstDifferenceInX + "; second difference in X: " + secondDifferenceInX);
				}
			}
			
			
			
			
			
			
			
			allRelativeDegrees.add(relativeDegree);
			
			
		}
		
		return allRelativeDegrees;
		
		
	}

}
