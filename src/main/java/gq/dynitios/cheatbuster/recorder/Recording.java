package gq.dynitios.cheatbuster.recorder;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a recording of a player that started when the played engaged a recordable action.
 * A recording can be considered 'expired' after which data *should* no longer be appended.
 */
public class Recording {
    private long firstUpdated;
    private long lastUpdated;

    private int clicks;
    private int performedHits;

    private int shots;
    private List<Float> shotForceList;

    private int brokenBlocks;
    private List<Float> blockHardnessList;

    private int placedBlocks;

    Recording() {
        this.firstUpdated = System.currentTimeMillis();
        this.lastUpdated = System.currentTimeMillis();
        shotForceList = new ArrayList<>();
        blockHardnessList = new ArrayList<>();
    }

    /**
     * Returns the recording time in milliseconds
     */
    private int getRecordingTime() {
        return Math.toIntExact(lastUpdated - firstUpdated);
    }

    /**
     * Updates the lastUpdated timestamp to the current time.
     */
    private void setUpdated() {
        if (!isExpired()) {
            this.lastUpdated = System.currentTimeMillis();
        } else {
            throw new IllegalStateException("Data should not be added to an expired recording.");
        }
    }

    /**
     * Calculates the average shot force based on shotForceList
     */
    private double averageShotForce() {
        return shotForceList.stream().mapToDouble(a -> a).average().orElse(0);
    }

    /**
     * Calculates the average block hardness based on blockHardnessList
     */
    private double averageBlockHardness() {
        return blockHardnessList.stream().mapToDouble(a -> a).average().orElse(0);
    }

    /**
     * Calculates the accuracy of bow and melee hits.
     */
    private double accuracyPercentage() {
        if (clicks + shots != 0) {
            return performedHits / (clicks + shots) * 100;
        } else return 0;
    }

    /**
     * Adds a click to this recording
     */
    void addClick() {
        clicks++;
        this.setUpdated();
    }

    /**
     * Adds a performed hit to this recording. (Damaging an entity)
     */
    void addPerformedHit() {
        performedHits++;
        this.setUpdated();
    }

    /**
     * Adds a bow shot to this recording.
     *
     * @param force The force the arrow was shot with.
     */
    void addShot(float force) {
        shots++;
        shotForceList.add(force);
        this.setUpdated();
    }

    /**
     * Adds a block break to this recording.
     *
     * @param hardness The hardness of the block that was broken.
     */
    void addBlockBreak(float hardness) {
        brokenBlocks++;
        blockHardnessList.add(hardness);
        this.setUpdated();
    }

    /**
     * Adds a block place to this recording.
     */
    void addBlockPlace() {
        placedBlocks++;
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

    @Override
    public String toString() {
        return "Length: " + getRecordingTime() + "\n" +
                "Clicks: " + clicks + "\n" +
                "Performed hits: " + performedHits + "\n" +
                "Shots: " + shots + "\n" +
                "Average shot force: " + averageShotForce() + "\n" +
                "Blocks broken: " + brokenBlocks + "\n" +
                "Average Block Strength: " + averageBlockHardness() + "\n" +
                "Blocks placed: " + placedBlocks;
    }

    public double[] toArray() {
        return new double[]{getRecordingTime(), clicks, performedHits, accuracyPercentage(), shots, averageShotForce(), brokenBlocks, averageBlockHardness(), placedBlocks, 0};
    }
}
