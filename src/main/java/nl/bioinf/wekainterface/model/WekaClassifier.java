package nl.bioinf.wekainterface.model;

import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.lazy.IBk;
import weka.classifiers.rules.OneR;
import weka.classifiers.rules.ZeroR;
import weka.classifiers.trees.J48;
import weka.core.Instances;

import java.util.Random;

/**
 * @author Marijke Eggink
 */

public class WekaClassifier {
    /**
     * This method classifies instances with a 10-fold cross validation.
     * @param instances instances to be classified.
     * @param classifier name of the classifier.
     * @return String with results of 10-fold cross validation.
     * @throws Exception
     */
    public String Test(Instances instances, String classifier) throws Exception {
        weka.classifiers.Classifier rule;
        switch (classifier){
            case "ZeroR":
                rule = new ZeroR();
                break;
            case "OneR":
                rule = new OneR();
                break;
            case "Naives Bayes":
                rule = new NaiveBayes();
                break;
            case "J48":
                rule = new J48();
                break;
            case "IBK":
                rule = new IBk();
                break;
            default:
                throw new IllegalArgumentException();
        }
        Evaluation evaluation = new Evaluation(instances);
        evaluation.crossValidateModel(rule, instances, 10, new Random(1));
        return evaluation.toSummaryString("\nResults\n======\n", false);
    }
}
