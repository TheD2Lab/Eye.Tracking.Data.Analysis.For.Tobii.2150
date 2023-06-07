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
	private final double MAX_PTS_PER_MEASURMENT = 1;
	public scoreCalcuations()
	{
		highestScore = totalScore = parser.getNumberOfData()*3;
	}
	
	/*
	 * returns the total score penalty for the localizer portion of the ILS approach
	 * @param double[] horizontalDef is all of the localizer position of the aircraft
	 * @return double Returns the total penalty
	 */
	private double locScorePenalty(double[]horizontalDef)
	{
		double penalty = 0;
		for(int i=0; i<horizontalDef.length; i++)
		{
			if(Math.abs(horizontalDef[i]) < 0.1)
			{
				continue;
			}
			else if(Math.abs(horizontalDef[i]) <= 0.5) //2 degree
			{
				penalty += 0.25 * MAX_PTS_PER_MEASURMENT;
			}
			else if(Math.abs(horizontalDef[i]) <= 1) //4 degree
			{
				penalty += 0.50 * MAX_PTS_PER_MEASURMENT;
			}
			else if(Math.abs(horizontalDef[i]) <= 1.5) //6 degree
			{
				penalty += 0.75 * MAX_PTS_PER_MEASURMENT;
			}
			else if(Math.abs(horizontalDef[i]) > 1.5)
			{
				penalty += MAX_PTS_PER_MEASURMENT;
			}
		}
		return penalty;
	}
	
	/*
	 * returns the total score penalty for the glideslope portion of the ILS approach
	 * @param double[] verticalDef is all of the vertical position of the aircraft
	 * @return double Returns the total penalty
	 */
	private double glideSlopeScorePenalty(double[]verticalDef)
	{
		double penalty = 0;
		for(int i=0; i<verticalDef.length; i++)
		{
			if(Math.abs(verticalDef[i]) < 0.1)
			{
				continue;
			}
			else if(Math.abs(verticalDef[i]) <= 0.5) //2 degree
			{
				penalty += 0.25 * MAX_PTS_PER_MEASURMENT;
			}
			else if(Math.abs(verticalDef[i]) <= 1) //4 degree
			{
				penalty += 0.50 * MAX_PTS_PER_MEASURMENT;
			}
			else if(Math.abs(verticalDef[i]) <= 1.5) //6 degree
			{
				penalty += 0.75 * MAX_PTS_PER_MEASURMENT;
			}
			else if(Math.abs(verticalDef[i]) > 1.5)
			{
				penalty += MAX_PTS_PER_MEASURMENT;
			}
		}
		return penalty;
	}
	
	/*
	 * returns the total speed penalty for the speed portion of the ILS approach
	 * @param double[]speed The speed of the aircraft during the ILS approach
	 * @return double Returns the total penalty
	 */
	private double speedILSCalcPenalty(double[] speeds)
	{
		double penalty = 0;
		for(double speed: speeds)
		{
			if(89 < speed && speed < 91)
			{
				continue;
			}
			else if(80 < speed && speed < 100)
			{
				penalty += 0.25 * MAX_PTS_PER_MEASURMENT;
			}
			else if(75 < speed && speed < 105)
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
	
	/*
	 * returns the total penalty for the ILS approach
	 * @param double[] horiDef all of the localizer position of the aircraft
	 * @param double[] speed The speed of the aircraft during the ILS approach
	 * @param double[] vertDef all of the glideslope position of the aircraft
	 * @return double Returns the total penalty
	 */
	public double scoreILSCalc(double[]horiDef, double[]speed, double[]vertDef)
	{	
		double lateralPen = locScorePenalty(horiDef);
		double speedPen = speedILSCalcPenalty(speed);
		double glideSlopePen = glideSlopeScorePenalty(vertDef);
		System.out.println("Speed Calc Penalty: " + speedPen);
		System.out.println("Lateral Calc Penalty: " + lateralPen);
		System.out.println("Glide Slope Penalty: " + glideSlopePen);
		
		return lateralPen + speedPen + glideSlopePen;
	}
	
	/*
	 * returns the total penalty for the approach and landing
	 * @param double[] horiDef all of the localizer position of the aircraft
	 * @param double[]speed The speed of the aircraft during the ILS approach
	 * @return double Returns the total penalty
	 */
	public void scoreCalc(double[]horiDef, double[]speed, double[]vertDef)
	{
		totalScore -= scoreILSCalc(horiDef,speed, vertDef);
	}
	
	public double getHighestScore() {
		return highestScore;
	}
	public double getTotalScore() {
		return totalScore;
	}
}
