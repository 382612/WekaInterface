package nl.bioinf.wekainterface.classifiers;

import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.rules.ZeroR;
import weka.core.Instances;

import java.util.Random;

public class ZeroRClassifier {
    public String TestZeroR(Instances data) throws Exception {
        ZeroR rule = new ZeroR();
        Evaluation evaluation = new Evaluation(data);
        evaluation.crossValidateModel(rule, data, 10, new Random(1));
        return evaluation.toSummaryString("\nResults\n======\n", false);
    }
}
