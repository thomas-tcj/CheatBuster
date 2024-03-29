package gq.dynitios.cheatbuster.recorder;

import gq.dynitios.cheatbuster.exception.LearningSetException;
import gq.dynitios.cheatbuster.http.Logstash;
import gq.dynitios.cheatbuster.message.MessageHelper;
import gq.dynitios.cheatbuster.weka.LearningSet;
import me.rayzr522.jsonmessage.JSONMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

/**
 * Manages recordings of players.
 */
public class PlayerRecorder {
    private HashMap<Player, Recording> playerRecordings;
    private LearningSet learningSet;
    private Logstash logstash;

    public PlayerRecorder() throws LearningSetException {
        playerRecordings = new HashMap<>();
        logstash = new Logstash();
        try {
            learningSet = new LearningSet();
            learningSet.testDataset();
        } catch (Exception e) {
            throw new LearningSetException();
        }
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
            finishRecording(recording, player);
            recording = new Recording();
            playerRecordings.put(player, recording);
        }
        return recording;
    }

    /**
     * Finishes an expired recording.
     */
    private void finishRecording(Recording recording, Player player) {
        try {
            long preClassifyTimestamp = System.currentTimeMillis();
            int classification = learningSet.classify(recording.toArray());
            long postClassifyTimestamp = System.currentTimeMillis();
            long classificationTime = postClassifyTimestamp - preClassifyTimestamp;
            String classificationString;

            if (classification == 0) {
                classificationString = "No hacks";
            } else if (classification == 1) {
                classificationString = "Autoclicker";
            } else if (classification == 2) {
                classificationString = "KillAura";
            } else {
                classificationString = "Unknown";
            }

            FinishedRecording finishedRecording = new FinishedRecording(recording, classification);
            logstash.sendToLogstash(finishedRecording);

            JSONMessage message = MessageHelper.getPluginPrefix()
                    .then("Evaluated ")
                    .then(player.getName())
                    .then(": ")
                    .then(classificationString);

            if (classificationTime > 100) {
                message.newline()
                        .then("Warning: evaluation took " + classificationTime + "ms. Either the server is slow" +
                                " or the learning set has grown too big.")
                        .color(ChatColor.RED);
            }
            MessageHelper.sendToAllWithPermission(message, "cheatbuster.admin");
        } catch (Exception e) {
            e.printStackTrace();
        }
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
     * Adds a performed hit to the given player's recording.
     *
     * @param player The player who damaged an entity.
     */
    public void recordPerformHit(Player player) {
        getOrStartRecording(player).addPerformedHit();
    }
}
