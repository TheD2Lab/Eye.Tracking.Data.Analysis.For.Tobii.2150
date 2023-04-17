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
	private double totalScore = 0;
	private double highestScore = 0;
	private final double MAX_PTS_PER_MEASURMENT = 0.33;
	public scoreCalcuations()
	{
		highestScore = totalScore = parser.getNumberOfData()*0.33;
		
	}
	private static boolean isOnLatLine(double x, double y)
	{
		//checks if y is within the range of y1 and y2
		//Actual line: (y < 47.43139) ? Math.tan(89.48447*Math.PI/180)*x + 13640.326
	    double y1 = (y < 47.43139) ? Math.tan(89.48447*Math.PI/180)*x + 13640.306 : Double.NEGATIVE_INFINITY;
	    double y2 = (y < 47.43139) ? Math.tan(89.48447*Math.PI/180)*x + 13640.346 : Double.POSITIVE_INFINITY;
	    double minY = Math.min(y1, y2);
	    double maxY = Math.max(y1, y2);
	    return y >= minY && y <= maxY;
	}
	private static boolean isBetweenLatLineMinusOneFourth(double x, double y)
	{
		//checks if y is within the range of y1 and y2
	    double y1 = (y < 47.43139) ? Math.tan(86.48447*Math.PI/180)*x + 2038.29426 : Double.NEGATIVE_INFINITY;
	    double y2 = (y < 47.43139) ? Math.tan(92.48447*Math.PI/180)*x - 2771.416 : Double.POSITIVE_INFINITY;
	    double minY = Math.min(y1, y2);
	    double maxY = Math.max(y1, y2);
	    return y >= minY && y <= maxY;
	}
	private static boolean isBetweenLatLineMinusOneHalf(double x, double y)
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

	
	private double latScorePenalty(double[]x, double[]y)
	{
		double penalty = 0;
		for(int i=0; i<x.length; i++)
		{
			if(isOnLatLine(x[i], y[i]))
			{
				continue;
			}
			else if(isBetweenLatLineMinusOneFourth(x[i], y[i]))
			{
				penalty += 0.25 * MAX_PTS_PER_MEASURMENT;
			}
			else if(isBetweenLatLineMinusOneHalf(x[i], y[i]))
			{
				penalty += 0.50 * MAX_PTS_PER_MEASURMENT;
			}
		}
		return penalty;
	}
	
	private double speedCalcPenalty(double[] speeds)
	{
		double penalty = 0;
		for(double speed: speeds)
		{
			if(speed >89 && speed < 91)
			{
				continue;
			}
			else if(speed >80 && speed < 100)
			{
				penalty += 0.25 * MAX_PTS_PER_MEASURMENT;
			}
			else if(speed >75 && speed < 105)
			{
				penalty += 0.50 * MAX_PTS_PER_MEASURMENT;
			}
			else
			{
				penalty += MAX_PTS_PER_MEASURMENT;
			}
		}
		return penalty;
	}
	
	public void scoreCalc(double[]x, double[]y, double[]speed)
	{
		double lateralPen = latScorePenalty(x,y);
		double speedPen = speedCalcPenalty(speed);
		System.out.println("Speed Calc Penalty" + speedPen);
		System.out.println("Lateral Calc Penalty" + lateralPen);
		
		
		totalScore -= lateralPen + speedPen;
	}
	
	public double getHighestScore() {
		return highestScore;
	}
	public double getTotalScore() {
		return totalScore;
	}
}
