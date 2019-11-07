package gq.dynitios.cheatbuster.weka;

import org.bukkit.Bukkit;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.evaluation.NominalPrediction;
import weka.classifiers.trees.RandomForest;
import weka.core.DenseInstance;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;

public class LearningSet {
    private Instances dataSet;

    public LearningSet() throws IOException {
        dataSet = readDataFile();
        dataSet.setClassIndex(dataSet.numAttributes() - 1);
    }

    private Instances readDataFile() throws IOException {
        BufferedReader inputReader;
        inputReader = new BufferedReader(new FileReader("./plugins/CheatBuster/learningset.arff"));
        return new Instances(inputReader);
    }

    public int classify(double[] values) throws Exception {
        Classifier model = new RandomForest();
        model.buildClassifier(dataSet);

        Instance testInstance = new DenseInstance(1.0, values);
        testInstance.setDataset(dataSet);
        return (int) model.classifyInstance(testInstance);
    }

    private Evaluation classify(Classifier model,
                                Instances trainingSet, Instances testingSet) throws Exception {
        Evaluation evaluation = new Evaluation(trainingSet);

        model.buildClassifier(trainingSet);
        evaluation.evaluateModel(model, testingSet);

        return evaluation;
    }

    private double calculateAccuracy(FastVector predictions) {
        double correct = 0;

        for (int i = 0; i < predictions.size(); i++) {
            NominalPrediction np = (NominalPrediction) predictions.elementAt(i);
            if (np.predicted() == np.actual()) {
                correct++;
            }
        }

        return 100 * correct / predictions.size();
    }

    private Instances[][] crossValidationSplit(Instances data) {
        int numberOfFolds = 10;
        Instances[][] split = new Instances[2][numberOfFolds];

        for (int i = 0; i < numberOfFolds; i++) {
            split[0][i] = data.trainCV(numberOfFolds, i);
            split[1][i] = data.testCV(numberOfFolds, i);
        }

        return split;
    }

    public double testDataset() throws Exception {
        // Do 10-split cross validation
        Instances[][] split = crossValidationSplit(dataSet);

        // Separate split into training and testing arrays
        Instances[] trainingSplits = split[0];
        Instances[] testingSplits = split[1];

        // Use RandomForest classifier
        Classifier model = new RandomForest();

        // Collect every group of predictions for model in a FastVector
        FastVector predictions = new FastVector();

        // For each training-testing split pair, train and test the classifier
        for (int i = 0; i < trainingSplits.length; i++) {
            Evaluation validation = classify(model, trainingSplits[i], testingSplits[i]);
            predictions.appendElements(validation.predictions());
        }

        // Calculate overall accuracy of current classifier on all splits
        double accuracy = calculateAccuracy(predictions);

        Bukkit.getLogger().log(Level.INFO, "Succesfully tested learningset.arff. Accuracy:"
                + String.format("%.2f%%", accuracy));
        return accuracy;
    }
}
