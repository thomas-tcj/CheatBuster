package gq.dynitios.cheatbuster.recorder;

import gq.dynitios.cheatbuster.tps.TpsMeter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a recording of a player that started when the played engaged a recordable action.
 * A recording can be considered 'expired' after which data *should* no longer be appended.
 */
public class Recording {
    private long firstUpdated;
    private long lastUpdated;

    private double initialTPS;

    private List<Long> leftClickTimestamps;
    private List<Long> leftClickDelays;

    private int performedHits;

    Recording() {
        this.firstUpdated = System.currentTimeMillis();
        this.lastUpdated = System.currentTimeMillis();
        this.leftClickTimestamps = new ArrayList<>();
        this.initialTPS = TpsMeter.getTps();
    }

    /**
     * Updates the lastUpdated timestamp to the current time.
     */
    private void setUpdated() {
        setUpdated(System.currentTimeMillis());
    }

    /**
     * Updates the lastUpdated timestamp to the given time.
     *
     * @param timestamp Timestamp to set lastUpdated to.
     */
    private void setUpdated(long timestamp) {
        if (!isExpired()) {
            this.lastUpdated = timestamp;
        } else {
            throw new IllegalStateException("Data should not be added to an expired recording.");
        }
    }

    void addLeftClick() {
        long currentTimeStamp = System.currentTimeMillis();
        leftClickTimestamps.add(currentTimeStamp);
        this.setUpdated(currentTimeStamp);
    }


    /**
     * Adds a performed hit to this recording. (Damaging an entity)
     */
    void addPerformedHit() {
        performedHits++;
        this.setUpdated();
    }

    /**
     * Checks if this recording has not been updated for more than 2000 milliseconds.
     *
     * @return true if recording has not been updated for 2000 milliseconds, otherwise false.
     */
    boolean isExpired() {
        return System.currentTimeMillis() - lastUpdated > 2000;
    }

    double getTpsDifference() {
        return initialTPS - TpsMeter.getTps();
    }


    int getRecordingLength() {
        return Math.toIntExact(lastUpdated - firstUpdated);
    }

    int getTotalLeftClicks() {
        return leftClickTimestamps.size();
    }

    long getMaxLeftClickDelay() {
        if (leftClickTimestamps.size() < 2) {
            return 0;
        }
        if (leftClickDelays == null) {
            leftClickDelays = getDelays(leftClickTimestamps);
        }
        return Collections.max(leftClickDelays);
    }

    double getAverageLeftClickDelay() {
        if (leftClickDelays == null) {
            leftClickDelays = getDelays(leftClickTimestamps);
        }
        return leftClickDelays.stream().mapToDouble(x -> x).average().orElse(0.0);
    }

    long getMinLeftClickDelay() {
        if (leftClickTimestamps.size() < 2) {
            return 0;
        }
        if (leftClickDelays == null) {
            leftClickDelays = getDelays(leftClickTimestamps);
        }
        return Collections.min(leftClickDelays);
    }

    int getPerformedHits() {
        return performedHits;
    }

    private static List<Long> getDelays(List<Long> timestamps) {
        List<Long> delays = new ArrayList<>();
        for (int i = 0; i < timestamps.size() - 1; i++) {
            long stamp = timestamps.get(i);
            long next = timestamps.get(i + 1);
            delays.add(next - stamp);
        }
        return delays;
    }

    @Override
    public String toString() {
        return "TPS Difference: " + getTpsDifference() + "\n" +
                "Recording length: " + getRecordingLength() + "\n" +
                "Total left clicks: " + getTotalLeftClicks() + "\n" +
                "Maximum left click delay: " + getMaxLeftClickDelay() + "\n" +
                "Average left click delay: " + getAverageLeftClickDelay() + "\n" +
                "Minimum left click delay: " + getMinLeftClickDelay() + "\n" +
                "Performed hits: " + getPerformedHits();
    }

    /**
     * Creates an array with all data for the algorithm to process.
     */
    double[] toArray() {
        return new double[]{
                getTpsDifference(),
                getRecordingLength(),
                getTotalLeftClicks(),
                getMaxLeftClickDelay(),
                getAverageLeftClickDelay(),
                getMinLeftClickDelay(),
                getPerformedHits(),
                0};
    }

}
