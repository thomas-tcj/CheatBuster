package gq.dynitios.cheatbuster.recorder;

import gq.dynitios.cheatbuster.weka.LearningSet;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * Manages recordings of players.
 */
public class PlayerRecorder {
    private HashMap<Player, Recording> playerRecordings;
    private LearningSet learningSet;

    public PlayerRecorder() {
        playerRecordings = new HashMap<>();
        learningSet = new LearningSet();
    }

    /**
     * Retrieves a player's unexpired recording or starts a new one.
     *
     * @param player The player for which to retrieve a recording.
     * @return a recording to which data can be appended.
     */
    private Recording getOrStartRecording(Player player) {
        Recording recording = playerRecordings.get(player);
        if (recording == null) {
            recording = new Recording();
            playerRecordings.put(player, recording);
        } else if (recording.isExpired()) {
            finishRecording(recording);
            recording = new Recording();
            playerRecordings.put(player, recording);
        }
        return recording;
    }

    /**
     * Finishes an expired recording.
     */
    private void finishRecording(Recording recording) {
        Bukkit.broadcastMessage("Finished recording: " + recording.toString());
        learningSet.addRecording(recording);
    }

    public void stopRecorder() {
        learningSet.saveToFile();
    }

    /**
     * Adds a click to the given player's recording.
     *
     * @param player The player who clicked.
     */
    public void recordClick(Player player) {
        getOrStartRecording(player).addClick();
    }

    /**
     * Adds a performed hit to the given player's recording.
     *
     * @param player The player who damaged an entity.
     */
    public void recordPerformHit(Player player) {
        getOrStartRecording(player).addPerformedHit();
    }

    /**
     * Adds a shot to the given player's recording.
     *
     * @param player The player who shot.
     * @param force  The force at which was shot.
     */
    public void recordShoot(Player player, float force) {
        getOrStartRecording(player).addShot(force);
    }

    /**
     * Adds a block break to the given player's recording.
     *
     * @param player   The player who broke a block.
     * @param hardness The hardness of the block.
     */
    public void recordBlockBreak(Player player, float hardness) {
        getOrStartRecording(player).addBlockBreak(hardness);
    }

    /**
     * Adds a block place to the given player's recording
     *
     * @param player The player who placed a block.
     */
    public void recordBlockPlace(Player player) {
        getOrStartRecording(player).addBlockPlace();
    }
}
