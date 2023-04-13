package xplane12_data_parser;
/*
 * How the scoring works
 * For every data point, we will assign 0.99 pts
 * All the measurements will be given the same values 0.33(latitude, height, speed)
 * For every mistake in latitude, height, or speed it will either be 1/4, 1/2, or 1 pt off 
 * The point system is in place because the severity of a mistake increases exponentially as it gets bigger.
 */
public class scoreCalcuations {
	//For the lateral of the plane
	//Check if its within +- 2 degrees of the designated line
	private boolean isBetweenLatLineMinusZero(double x, double y)
	{
		//checks if y is within the range of y1 and y2
	    double y1 = (y < 47.43139) ? Math.tan(86.48447*Math.PI/180)*x + 2038.29426 : Double.NEGATIVE_INFINITY;
	    double y2 = (y < 47.43139) ? Math.tan(92.48447*Math.PI/180)*x - 2771.416 : Double.POSITIVE_INFINITY;
	    double minY = Math.min(y1, y2);
	    double maxY = Math.max(y1, y2);
	    return y >= minY && y <= maxY;
	}
	private boolean isBetweenLatLineMinusOneFourth(double x, double y)
	{
		//checks if y is within the range of y1 and y2
	    double y1a = (y < 47.43139) ? Math.tan(86.48447*Math.PI/180)*x + 2038.29426 : Double.NEGATIVE_INFINITY;
	    double y1b = (y < 47.43139) ? Math.tan(83.48447*Math.PI/180)*x + 1118.33447 : Double.NEGATIVE_INFINITY;
	    double y2a = (y < 47.43139) ? Math.tan(92.48447*Math.PI/180)*x - 2771.416 : Double.POSITIVE_INFINITY;
	    double y2b = (y < 47.43139) ? Math.tan(95.48447*Math.PI/180)*x - 1226.40511 : Double.POSITIVE_INFINITY;
	    double min1Y = Math.min(y1a, y1b);
	    double min2Y = Math.min(y2a, y2b);
	    double max1Y = Math.max(y1a, y1b);
	    double max2Y = Math.max(y2a, y2b);
	    return (y >= min1Y && y <= max1Y) || (y >= min2Y && y <= max2Y);
	}
	
	public double highestPossibleScore(int numDataPoint)
	{
		return numDataPoint*0.33;
		
	}

}
