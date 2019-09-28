package gq.dynitios.cheatbuster.recorder;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class PlayerRecorder {
    private HashMap<Player, Recording> playerRecordings;

    public PlayerRecorder() {
        playerRecordings = new HashMap<Player, Recording>();
    }

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

    private void finishRecording(Recording recording) {
        Bukkit.broadcastMessage("Finished recording: " + recording.toString());
    }

    public void recordClick(Player player) {
        getOrStartRecording(player).addClick();
    }

    public void recordPerformHit(Player player) {
        getOrStartRecording(player).addPerformedHit();
    }

    public void recordShoot(Player player, float force) {
        getOrStartRecording(player).addShot(force);
    }

    public void recordBlockBreak(Player player, float hardness) {
        getOrStartRecording(player).addBlockBreak(hardness);
    }

    public void recordBlockPlace(Player player) {
        getOrStartRecording(player).addBlockPlace();
    }
}
