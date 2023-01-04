
# Abstract

The purpose of this codebase is to provide an eye gaze analysis tool that utilizes machine learning algorithms to predict the fatigue level of participants. The codebase is compatible with Gazepoint's GP3 Desktop and GP3HD V2 eyetrackers and uses the WEKA software collection for its data mining procedures.

# Setup

- You will need to setup an eyetracker from Gazepoint and a monitor that is up to 24". Follow the directions as listed by your Gazepoint eyetracker to setup your experiment enviroment. This codebase was developed using the GP3 Desktop and GP3HD V2 eyetrackers, other eyetrackers may not work correctly.
- Ensure that you have all the required libraries installed. These libraries are necessary for the program to run properly, and they should be located in the **libs** folder. If for some reason they are not present, you will need to download them and attach them to the program manually.
Required libraries:
    - [arpack_combined.jar](https://weka.sourceforge.io/packageMetaData/)
    - [common-lang3.jar](https://commons.apache.org/proper/commons-lang/download_lang.cgi)
    - [commons-math3-3.6.1.jar](https://commons.apache.org/proper/commons-math/download_math.cgi)
    - [core.jar](https://weka.sourceforge.io/packageMetaData/)
    - [mt.jar](https://weka.sourceforge.io/packageMetaData/)
    - [opencsv-5.7.0.jar](https://opencsv.sourceforge.net/)
    - [weka.jar](https://waikato.github.io/weka-wiki/downloading_weka/)
- **Please note that the screen size is hard-coded into the main.java file.** If the screen size of the monitor that the experiment was conducted on differs from the size specified in the code, it is necessary to edit the code to reflect the correct screen size. Failure to do so may result in incorrect calculations.

# How To Use The Repository

1. Clone the repository to your local machine using git or your preferred version control system.
2. Ensure that you have all the required libraries installed and run the program
3. A prompt will appear asking you to select the gaze file you want the program to analyze 
4. After selecting the gaze file, the program will ask you to select the corresponding fixation file.
5. A prompt will then appear asking you to select the location where you would like all the output files to be saved.
6. The program will then ask you to input the name of the folder/participant.
7. Once the program finishes analyzing and outputting the initial data, it will ask whether or not you would like to generate snapshots of the gaze/fixation data.
    * If you select yes then the program will give you four different snapshot options:
        - Continuous Snapshot: This option generates gaze data in a series of fixed, nonoverlapping windows.
        - Cumulative Snapshot: This option generates gaze data in a series of expanding windows that increases with every interval.
        - Overlapping Snapshot: This option generates gaze data in a series of fixed and overlapping windows.
        - Event Analytics: This option generates a baseline file based on the first two minutes of the gaze data, and then compares it to the rest of the file. If the data exceeds the baseline value, it will be counted as an event, and the program will continue to search for the next event within a specified time period. If no event is found, the program will close at a specific period. If another event is found, the session window will continue searching.
8. Depending on the option you choose, the program will ask for different input. For the first three options, the program will ask you to select the window size and/or overlapping amount (both in terms of time in seconds).
9. If you choose the last option, Event Analytics, the program will ask you which gaze or fixation file you would like to analyze. The program will create a baseline file based on the first two minutes and then ask you to pick a baseline value and a value from the file you inputted to compare 
    to each other, as well as a maximum duration of an event.

# Analysis

## Inputs: 

    Gaze and Fixation raw data files

## Output: 

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

# Limitations

1. Several sections of the codebase rely on the existence of data columns in the raw data files. To ensure that the program functions correctly, select all the possible columns when exporting data from the Gazepoint Analysis software.
2. The program will create a folder for the participant regardless of whether the program executes successfully.
3. If the names and location selected for the files match those of an existing file, the existing file will be automatically overwritten.