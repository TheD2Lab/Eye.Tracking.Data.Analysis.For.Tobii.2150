Abstract
	The purpose of this codebase is to provide an eye gaze analysis tool that is compatible with Gazepoint's GP3 Desktop and GP3HD V2 eyetrackers.


Setup
	Requirements
		1) Ensure that you have all the required libraries installed. These libraries are necessary for the program to run properly, and they should be located in the "libs" folder. If for some reason they are not present, you will need to download them and attach them to the program manually.
			Required libraries
				arpack_combined.jar
				common-lang3.jar
				commons-math3-3.6.1.jar
				core.jar
				desktop.ini
				mt.jar
				opencsv-5.7.0.jar
				weka.jar
		2) Please note that the screen size is hard-coded into the main.java file. If the screen size of the monitor that the experiment was conducted on differs from the size specified in the code, it is necessary to edit the code to reflect the correct screen size. Failure to do so may result 
		   in incorrect calculations.

	How To clone the repository 
		1) Clone the repository to your local machine using git or your preferred version control system.
		2) Ensure that you have all the required libraries installed and run the program
		3) Upon launching the program, the Data Analysis Page will initialize. Users can navigate through the various tabs located in the top left corner to access the desired analysis features. The program will prompt the user to input the gaze and fixation files, specify the desired location 
		   for the output files, and provide the participant’s name for the output folder naming convention.
		4) The program will then display a new screen where the user will have the option to select specific windows for the output. If the user chooses not to select a window, they can simply select the ”Exit” option and submit, and the program will automatically terminate. However, if the user 
		   wishes to select a window, they can choose from the various options provided. For added convenience, hovering over the different options will reveal a hint describing the function of each option, providing guidance for those who may be unsure of which option to select.
		
		Four different window options:
			Continuous Snapshot: This option generates gaze data in a series of fixed, non-overlapping windows.
			Cumulative Snapshot: This option generates gaze data in a series of expanding windows that increases with every interval.
			Overlapping Snapshot: This option generates gaze data in a series of fixed and overlapping windows.
			Event Analytics: This option generates a baseline file based on the first two minutes of the gaze data, and then compares it to the rest of the file. If the data exceeds the baseline value, it will be counted as an event, and the program will continue to search for the next event within a 
							 specified time period. If no event is found, the program will close at a specific period. If another event is found, the session window will continue searching.

		5) Depending on the option you selected, the program will ask for different input. For the first three options, the program will ask you to select the window size and/or overlapping amount (both in terms of time in seconds).
		6) If you choose the last option, Event Analytics, the program will ask you which gaze or fixation file you would like to analyze. The program will create a baseline file based on the first two minutes and then ask you to pick a baseline value and a value from the file you inputted to compare 
		   to each other, as well as a maximum duration of an event.

Analysis
	Inputs: 
		Gaze and Fixation data
	Output: 
		FXD analysis including: 
			total number of fixations; 
			sum of all fixation duration; 
			mean duration; 
			median duration;
			StDev of durations; 
			Min. duration;
			Max. duration;  
			total number of saccades; 
			sum of all saccade length; 
			mean saccade length;
			median saccade length; 
			StDev of saccade lengths;
			min saccade length; 
			max saccade length; 
			sum of all saccade durations;
			mean saccade duration;
			median saccade duration; 
			StDev of saccade durations; 
			Min. saccade duration;
			Max. saccade duration; 
			scanpath duration; 
			fixation to saccade ratio; 
			sum of all absolute degrees; 
			mean absolute degree; 
			median absolute degree; 
			StDev of absolute degrees; 
			min absolute degree; 
			max absolute degree; 
			sum of all relative degrees; 
			mean relative degree; 
			median relative degree; 
			StDev of relative degrees; 
			min relative degree; 
			max relative degree; 
		EVD analysis including:
			total number of L mouse clicks
		GZD analysis including:
			average pupil size of left eye;
			average pupil size of right eye;
			average pupil size of both eyes.
		AOIResults
			Convex Hull Area
			Fixation Count
			Total Duration 
			Mean Duration
			Median Duration 
			StDev of Duration 
			Min Duration
			Max Duration
		System Log
			a text file that outlines what was successfully created or any errors that was encountered
		InputFiles Folder: Updated Fixation and Gaze file
			included the saccade velocity in both files
		Snapshot Folder (If yes was selected)
			contains all the snapshot files

Limitations
	1) There are currently some sections of the codebase that rely on hard-coded locations that are specific to the paid version of Gazepoint. To ensure that the program functions correctly, it is important to avoid modifying the Gazepoint files in any way.
	2) The program will create a folder for the participant regardless of whether the program executes successfully.
	3) If the names and location selected for the files match those of an existing file, the existing file will be automatically overwritten.