package gq.dynitios.cheatbuster.weka;

import gq.dynitios.cheatbuster.recorder.Recording;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;

import java.util.ArrayList;

import static weka.core.converters.ConverterUtils.DataSink;

public class LearningSet {
    private Instances dataRaw;

    public LearningSet() {
        ArrayList<Attribute> attributes = new ArrayList<>();
        attributes.add(new Attribute("recordingTime"));
        attributes.add(new Attribute("clicks"));
        attributes.add(new Attribute("performedHits"));
        attributes.add(new Attribute("accuracy"));
        attributes.add(new Attribute("shots"));
        attributes.add(new Attribute("averageShotForce"));
        attributes.add(new Attribute("brokenBlocks"));
        attributes.add(new Attribute("averageBlockHardness"));
        attributes.add(new Attribute("placedBlocks"));
        attributes.add(new Attribute("hackType"));

        dataRaw = new Instances("TestInstances", attributes, 0);
        dataRaw.setClassIndex(dataRaw.numAttributes() - 1); // Use last attribute as classIndex
    }

    public void addRecording(Recording recording) {
        double[] recordingValues = recording.toArray();
        if (recordingValues.length != dataRaw.numAttributes()) {
            throw new IllegalStateException("Number of attributes in array does not correspond to dataset.");
        }
        dataRaw.add(new DenseInstance(1.0, recordingValues));
    }

    public void saveToFile() {
        String outputFilename = "./data/test" + System.currentTimeMillis() + ".arff";
        try {
            DataSink.write(outputFilename, dataRaw);
        } catch (Exception e) {
            System.err.println("Failed to save data to: " + outputFilename);
            e.printStackTrace();
        }
    }
}
