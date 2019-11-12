package gq.dynitios.cheatbuster.recorder;

public class FinishedRecording {
    private double tpsDifference;
    private long recordingLength;
    private int clicks;
    private long maxClickDelay;
    private double averageClickDelay;
    private long minClickDelay;
    private int performedHits;
    private int classification;

    FinishedRecording(Recording recording, int classification) {
        this.tpsDifference = recording.getTpsDifference();
        this.recordingLength = recording.getRecordingLength();
        this.clicks = recording.getTotalLeftClicks();
        this.maxClickDelay = recording.getMaxLeftClickDelay();
        this.averageClickDelay = recording.getAverageLeftClickDelay();
        this.minClickDelay = recording.getMinLeftClickDelay();
        this.performedHits = recording.getPerformedHits();
        this.classification = classification;
    }

    /**
     * Creates an array with all data for the algorithm to process.
     */
    double[] toArray() {
        return new double[]{
                tpsDifference,
                recordingLength,
                clicks,
                maxClickDelay,
                averageClickDelay,
                minClickDelay,
                performedHits,
                classification};
    }
}
