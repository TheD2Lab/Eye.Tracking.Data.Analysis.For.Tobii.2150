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


public class saccade {

	public static Double[] getAllSaccadeLength(ArrayList<Object> allCoordinates){
		int objectSize = allCoordinates.size();
		Double[] allLengths = new Double[(objectSize-1)];
		for(int i=0; i<objectSize; i++){
			Integer[] earlyCoordinate = (Integer[]) allCoordinates.get(i);
			
			if(i+1<objectSize){
				Integer[] laterCoordinate = (Integer[]) allCoordinates.get(i+1);
				allLengths[i] = Math.sqrt(Math.pow((laterCoordinate[0] - earlyCoordinate[0]), 2) + Math.pow((laterCoordinate[1] - earlyCoordinate[1]), 2));
			}
			
		}
		
		return allLengths;
	}
	
	//the saccade duration is the duration between two fixations
	//e.g. given a fixation A that has timestamp T1 and duration D1,
	//and a subsequent fixation B that has timestamp T2 and duration T2,
	//the saccade duration between A and B is: T2-(T1+D1)
	public static ArrayList<Integer> getAllSaccadeDurations(ArrayList<Object> saccadeDetails){
		ArrayList<Integer> allSaccadeDurations = new ArrayList<Integer>();
		for (int i=0; (i+1)<saccadeDetails.size(); i++){
			Integer[] currentDetail = (Integer[]) saccadeDetails.get(i);
			Integer[] subsequentDetail = (Integer[]) saccadeDetails.get(i+1);
			
			int currentTimestamp = currentDetail[0];
			int currentFixationDuration = currentDetail[1];
			int subsequentTimestamp = subsequentDetail[0];
			
			int eachSaccadeDuration = subsequentTimestamp - (currentTimestamp + currentFixationDuration);
			
			allSaccadeDurations.add(eachSaccadeDuration);
		}
		return allSaccadeDurations;
	}
}
