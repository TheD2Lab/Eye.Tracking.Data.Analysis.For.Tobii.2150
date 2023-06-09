package xplane12_data_parser;
/*
 * How the Scoring Works
 * For every data point, we will assign 3 possible points (latitude, height, speed)
 * All the measurements will be given the same values: 1 point for latitude, 1 for height, and 1 for speed
 * For every mistake in latitude, height, or speed, the deduction will either be 1/4, 1/2, or 1 point off
 */

public class scoreCalcuations {
	private int dataPoints = 0;
	private int highestScore = 0;
	private double totalScore = 0; // Double because we will subtract decimals from it
	private double percentageScore = 0; // Score out of 100%
	
	private double speedPenalty = 0;
	private double localizerPenalty = 0;
	private double glideSlopePenalty = 0;
	
	public scoreCalcuations()
	{
		// Highest score and totalScore start out at the highest possible score, and then totalScore 
		// decreases with each penalty
		dataPoints = parser.getNumberOfData();
		highestScore = dataPoints * 3;
		totalScore = dataPoints * 3.0;
	}
	
	/*
	 * returns the total score penalty for the localizer portion of the ILS approach
	 * @param double[] horizontalDef is all of the localizer position of the aircraft
	 * @return double Returns the total penalty
	 */
	//For the lateral/vertical of the plane
	//Check if its within +/- 2 degrees of the designated line
	private double localizerScorePenalty(double[]horizontalDef)
	{
		double penalty = 0;
		for(int i=0; i<horizontalDef.length; i++)
		{
			if(Math.abs(horizontalDef[i]) < 0.1)
			{
				continue;
			}
			else if(Math.abs(horizontalDef[i]) <= 0.5) // 0.5 degrees
			{
				penalty += 0.25;
			}
			else if(Math.abs(horizontalDef[i]) <= 1) // 1 degree
			{
				penalty += 0.50;
			}
			else if(Math.abs(horizontalDef[i]) <= 1.5) // 1.5 degrees
			{
				penalty += 0.75;
			}
			else if(Math.abs(horizontalDef[i]) > 1.5) // Above 1.5 degrees
			{
				penalty += 1;
			}
		}
		return penalty;
	}
	
	/*
	 * returns the total score penalty for the glideslope portion of the ILS approach
	 * @param double[] verticalDef is all of the vertical position of the aircraft
	 * @return double Returns the total penalty
	 */
	//For the lateral/vertical of the plane
	//Check if its within +/- 2 degrees of the designated line
	private double glideSlopeScorePenalty(double[]verticalDef)
	{
		double penalty = 0;
		for(int i=0; i<verticalDef.length; i++)
		{
			if(Math.abs(verticalDef[i]) < 0.1)
			{
				continue;
			}
			else if(Math.abs(verticalDef[i]) <= 0.5) // 0.5 degrees
			{
				penalty += 0.25;
			}
			else if(Math.abs(verticalDef[i]) <= 1) // 1 degree
			{
				penalty += 0.50;
			}
			else if(Math.abs(verticalDef[i]) <= 1.5) // 1.5 degrees
			{
				penalty += 0.75;
			}
			else if(Math.abs(verticalDef[i]) > 1.5)
			{
				penalty += 1; // Above 1.5 degrees 
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
				penalty += 0.25;
			}
			else if(75 < speed && speed < 105)
			{
				penalty += 0.50;
			}
			else
			{
				penalty += 1;
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
		localizerPenalty = localizerScorePenalty(horiDef);
		glideSlopePenalty = glideSlopeScorePenalty(vertDef);
		speedPenalty = speedILSCalcPenalty(speed);
		
		return speedPenalty + localizerPenalty + glideSlopePenalty;
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
		percentageScore = totalScore / highestScore;
	}
	
	public int getDataPoints()
	{
		return dataPoints;
	}
	public int getHighestScore() {
		return highestScore;
	}
	public double getTotalScore() {
		return totalScore;
	}
	public double getPercentageScore()
	{
		return percentageScore;
	}
	public double getSpeedPenalty()
	{
		return speedPenalty;
	}
	public double getGlideSlopePenalty()
	{
		return glideSlopePenalty;
	}
	public double getLocalizerPenalty()
	{
		return localizerPenalty;
	}
}
