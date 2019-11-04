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
        Bukkit.broadcastMessage("Finished recording:\n" + recording.toString());
        learningSet.addRecording(recording);
    }

    public void stopRecorder() {
        learningSet.saveToFile();
    }

    /**
     * Adds a left click to the given player's recording.
     *
     * @param player The player who clicked.
     */
    public void recordLeftClick(Player player) {
        getOrStartRecording(player).addLeftClick();
    }

    /**
     * Adds a right click to the given player's recording.
     *
     * @param player The player who clicked.
     */
    public void recordRightClick(Player player) {
        getOrStartRecording(player).addRightClick();
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
     * Adds a block place to the given player's recording
     *
     * @param player The player who placed a block.
     */
    public void recordBlockPlace(Player player) {
        getOrStartRecording(player).addBlockPlace();
    }
}
