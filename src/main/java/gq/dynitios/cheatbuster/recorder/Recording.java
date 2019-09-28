package gq.dynitios.cheatbuster.recorder;

import java.util.ArrayList;
import java.util.List;

public class Recording {
    private long firstUpdated;
    private long lastUpdated;

    private int clicks;
    private int performedHits;

    private int shots;
    private List<Float> shotForce;

    private int brokenBlocks;
    private List<Float> blockHardness;

    private int placedBlocks;

    public Recording() {
        this.firstUpdated = System.currentTimeMillis();
        this.lastUpdated = System.currentTimeMillis();
        shotForce = new ArrayList<Float>();
        blockHardness = new ArrayList<Float>();
    }

    /**
     * Returns the recording time in milliseconds
     */
    private int getRecordingTime() {
        return Math.toIntExact(lastUpdated - firstUpdated);
    }

    private void setUpdated() {
        this.lastUpdated = System.currentTimeMillis();
    }

    public void addClick() {
        clicks++;
        this.setUpdated();
    }

    public void addPerformedHit() {
        performedHits++;
        this.setUpdated();
    }

    public void addShot(float force) {
        shots++;
        shotForce.add(force);
        this.setUpdated();
    }

    public void addBlockBreak(float hardness) {
        brokenBlocks++;
        blockHardness.add(hardness);
        this.setUpdated();
    }

    public void addBlockPlace() {
        placedBlocks++;
        this.setUpdated();
    }


    public boolean isExpired() {
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

    private double averageShotForce() {
        return shotForce.stream().mapToDouble(a -> a).average().orElse(0);
    }

    private double averageBlockHardness() {
        return blockHardness.stream().mapToDouble(a -> a).average().orElse(0);
    }
}
