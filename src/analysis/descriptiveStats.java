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
import java.util.Arrays;
import java.util.Collections;


public class descriptiveStats {
	
	public static double getSumOfIntegers(ArrayList<Integer> allIntegers){
		
		double total = 0.0;
		for (Integer i:allIntegers){
			total = total + i;
		}
		return total; 
	}
	
	public static double getSumOfDoubles(ArrayList<Double> allDoubles){
		double total = 0.0;
		for(int i=0; i<allDoubles.size(); i++){
			double value = allDoubles.get(i);
			total += value;
		}
		
		return total;
	}
	
	public static double getSum(Double[] allDoubles){
		
		double total = 0.0;
		for (Double i:allDoubles){
			total = total + i;
		}
		return total; 
		
	}
	

	public static double getMeanOfIntegers(ArrayList<Integer> allIntegers){
		return getSumOfIntegers(allIntegers)/allIntegers.size();
	}
	
	public static double getMeanOfDoubles(ArrayList<Double> allDoubles){
		return getSumOfDoubles(allDoubles)/allDoubles.size();
	}
	
	public static double getMean(Double[] allDoubles){
		double average = getSum(allDoubles)/allDoubles.length;
		return average;
	}
	
	public static double getMedianOfIntegers(ArrayList<Integer> allIntegers){
		Collections.sort(allIntegers);
		int middle = allIntegers.size()/2;
		if(allIntegers.size()%2 == 1){
			return allIntegers.get(middle);
		}else{
			return (allIntegers.get(middle-1) + allIntegers.get(middle))/2.0; 
		}
	}
	
	public static double getMedianOfDoubles(ArrayList<Double> allDoubles){
		Collections.sort(allDoubles);
		int middle = allDoubles.size()/2;
		if(allDoubles.size()%2 == 1){
			return allDoubles.get(middle);
		}else{
			return (allDoubles.get(middle-1) + allDoubles.get(middle))/2.0;
		}
	}
	
	public static double getMedian(Double[] allDoubles){
		Arrays.sort(allDoubles);
		if (allDoubles.length%2 == 1){
			return (double) allDoubles[allDoubles.length/2];
		}else{
			return ((double)allDoubles[allDoubles.length/2] + (double)allDoubles[allDoubles.length/2+1])/2;
		}
		
	}
	
	public static double getStDevOfIntegers(ArrayList<Integer> allIntegers){
		double sum = 0;
        double mean = getMeanOfIntegers(allIntegers);
 
        for (double i : allIntegers){
            sum += Math.pow((i - mean), 2);
        }
        
        return Math.sqrt( sum / (allIntegers.size()-1) );
	}
	
	public static double getStDevOfDoubles(ArrayList<Double> allDoubles){
		double sum = 0;
		double mean = getMeanOfDoubles(allDoubles);
		for(double i:allDoubles){
			sum += Math.pow((i-mean), 2);
		}
		return Math.sqrt(sum/(allDoubles.size()-1));
	}
	
	public static double getStDev(Double[] allDoubles){
		double sum = 0;
        double mean = getMean(allDoubles);
 
        for (double i : allDoubles){
            sum += Math.pow((i - mean), 2);
        }
        
        return Math.sqrt( sum / (allDoubles.length-1) );
	}
	
	public static double getMinOfIntegers(ArrayList<Integer> allIntegers){
		return Collections.min(allIntegers);
	}
	
	public static double getMinOfDoubles(ArrayList<Double> allDoubles){
		return Collections.min(allDoubles);
	}
	
	public static double getMin(Double[] allDoubles){
		return Collections.min(Arrays.asList(allDoubles));
	}
	
	public static double getMaxOfIntegers(ArrayList<Integer> allIntegers){
		return Collections.max(allIntegers);
	}
	
	public static double getMaxOfDoubles(ArrayList<Double> allDoubles){
		return Collections.max(allDoubles);
	}
	
	public static double getMax(Double[] allDoubles){
		return Collections.max(Arrays.asList(allDoubles));
	}

}
