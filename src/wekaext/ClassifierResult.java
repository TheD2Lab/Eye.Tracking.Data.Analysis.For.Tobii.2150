package wekaext;

import weka.classifiers.Classifier;

public class ClassifierResult {
    private Classifier classifier;
    private Double score;
    private int significance;

    public  ClassifierResult(Classifier classifier, Double score, int significance){
        this.classifier = classifier;
        this.score = score;
        this.significance = significance;
    }

    public Classifier getClassifier() {
        return classifier;
    }

    public String getClassifierName() {
        return classifier.getClass().getSimpleName();
    }

    public Double getScore() {
        return score;
    }

    public int getSignificance() {
        return significance;
    }
}
