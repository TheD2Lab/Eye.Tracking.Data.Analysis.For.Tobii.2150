package wekaext;

public class wekamain {
	public static void main(String[] args) throws Exception {
		
		
		WekaExperiment weka = new WekaExperiment();
		
//		weka.setupExperiment(true, "/Users/pgatsby/Desktop/ARFF_Files");
		
		CSVFilterData f = new CSVFilterData();
//		
		 f.CSVDataBinarySuccess("/Users/pgatsby/Desktop/preliminary_data/preliminary_data.csv", "Xplane Score");
//		 
//		 f.convertCSVToArff("/Users/pgatsby/Desktop/preliminary_data/binary_success_data-preliminary_data.csv");
//			
	}

}
